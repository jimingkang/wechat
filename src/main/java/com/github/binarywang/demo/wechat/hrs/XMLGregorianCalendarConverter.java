package com.github.binarywang.demo.wechat.hrs;

import com.google.gson.*;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.reflect.Type;
import java.util.GregorianCalendar;

public class XMLGregorianCalendarConverter {
    public static class Serializer implements JsonSerializer {
        public Serializer() {
            super();
        }
        public JsonElement serialize(Object t, Type type,
                                     JsonSerializationContext jsonSerializationContext) {
            XMLGregorianCalendar xgcal = (XMLGregorianCalendar) t;
            GregorianCalendar gregorianCalendar=   xgcal.toGregorianCalendar();
           // gregorianCalendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
          //  xgcal =new XMLGregorianCalendarImpl(gregorianCalendar);
            return new JsonPrimitive(xgcal.toXMLFormat());
        }
    }
    public static class Deserializer implements JsonDeserializer {
        public Object deserialize(JsonElement jsonElement, Type type,
                                  JsonDeserializationContext jsonDeserializationContext) {
            try {

                return
                        DatatypeFactory.newInstance().newXMLGregorianCalendar(
                                jsonElement.getAsString());
            } catch (Exception e) {
                return null;
            }
        }
    }
}
