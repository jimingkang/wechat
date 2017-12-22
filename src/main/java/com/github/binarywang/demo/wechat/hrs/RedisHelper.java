package com.github.binarywang.demo.wechat.hrs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.hrs.apac.proto.Prototype;
import com.hrs.apac.soap.client.HRSHotelAvailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jka07@int.hrs.com on 17-10-26.
 */
@Component
public class RedisHelper {

   @Autowired
   private  RedisTemplate<String,String> redisTemplate;

   public boolean checkKeys(String location, List<Object> keys) {
       for (Object key : keys) {
           Boolean hit = redisTemplate.opsForHash().hasKey(location,key);
           if (!hit) {
               return false;
           }
       }
       return true;
   }

   public List<String> getMajorKeys() {
        return redisTemplate.keys("*").stream().collect(Collectors.toList());
   }

   public List<String> getSubKeys(String m_key) {
       return redisTemplate.opsForHash().keys(m_key).stream()
               .map(val -> val.toString())
               .sorted(Comparator.naturalOrder())
               .collect(Collectors.toList());
   }
    public HRSHotelAvailResponse getAvail(String location, String date){
//        String value = redisTemplate.opsForValue().get(key);
        Object value = redisTemplate.opsForHash().get(location, date);
        if (value == null) {
            return null;
        }
        try {
            Gson gson = buildGson();
            Prototype.HRSHotelAvailResponse hrsHotelAvailResponse = Prototype.HRSHotelAvailResponse.parseFrom(Base64Utils.decodeFromString(value.toString()));
            String availJson = JsonFormat.printer().print(hrsHotelAvailResponse);
            HRSHotelAvailResponse hotelAvailResponse = gson.fromJson(availJson, HRSHotelAvailResponse.class);
                return hotelAvailResponse;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<HRSHotelAvailResponse> getAvailList(String location, List<Object> dates) {
        List<HRSHotelAvailResponse> data = new ArrayList<>();
//        List<String> result = redisTemplate.opsForValue().multiGet(keys);
        List<Object> result = redisTemplate.opsForHash().multiGet(location, dates);
        result.parallelStream().forEach(value -> {
            try {
                Gson gson = buildGson();
                Prototype.HRSHotelAvailResponse hrsHotelAvailResponse = Prototype.HRSHotelAvailResponse.parseFrom(Base64Utils.decodeFromString(value.toString()));
                String availJson = JsonFormat.printer().print(hrsHotelAvailResponse);
                HRSHotelAvailResponse hotelAvailResponse = gson.fromJson(availJson, HRSHotelAvailResponse.class);
                data.add(hotelAvailResponse);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        });
        return data;
    }

    public void setAvail(String location, String date, HRSHotelAvailResponse hrsHotelAvailResponse) {
        if (hrsHotelAvailResponse.getHotelAvailHotelOffers() != null &&
                hrsHotelAvailResponse.getHotelAvailHotelOffers().size() > 0) {
            Gson gson = buildGson();
            String availJson = gson.toJson(hrsHotelAvailResponse);
            Prototype.HRSHotelAvailResponse.Builder builder = Prototype.HRSHotelAvailResponse.newBuilder();
            try {
                JsonFormat.parser().merge(availJson, builder);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            Prototype.HRSHotelAvailResponse availProtoBuilder = builder.build();
//        String protoAvail = new String(availProtoBuilder.toByteArray());
            redisTemplate.opsForHash().put(location, date, Base64Utils.encodeToString(availProtoBuilder.toByteArray()));
        }
    }

    private Gson buildGson() {

        GsonBuilder gson_builder = new GsonBuilder();
        gson_builder.registerTypeAdapter(XMLGregorianCalendar.class,
                new XMLGregorianCalendarConverter.Serializer());
        gson_builder.registerTypeAdapter(XMLGregorianCalendar.class,
                new XMLGregorianCalendarConverter.Deserializer());

        Gson gson = gson_builder.create();

        return gson;
    }

    public void del(String m_key, String d_key){
        redisTemplate.opsForHash().delete(m_key,d_key);
    }
}
