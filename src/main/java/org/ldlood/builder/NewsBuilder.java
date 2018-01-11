package com.github.binarywang.demo.wechat.builder;

import com.github.binarywang.demo.wechat.utils.Event;
import com.hrs.apac.soap.client.HRSHotelAvailHotelOffer;
import com.hrs.apac.soap.client.HRSHotelAvailResponse;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
public class NewsBuilder extends AbstractBuilder {

  @Override
  public WxMpXmlOutMessage build(String content, WxMpXmlMessage wxMessage,
                                      WxMpService service) {

    WxMpXmlOutNewsMessage m = WxMpXmlOutMessage.NEWS().build();


    return m;
  }
  public List<Event> getEvents(String city){
    List<Event> list=null;
    try {
      FileReader fi = new FileReader("/home/jka07@int.hrs.com/hrs_doc/weixin-java-mp-demo-springboot-master/src/main/python/"+city+".txt");
      BufferedReader bf = new BufferedReader(fi);
      String str=null;
      int cnt=0;
      Event e= new Event();;
       list=new ArrayList<Event>();

   /*   file.write("活动名称"+":"+event_info['活动名称'])
      file.write('\n')
      file.write("时间"+":"+event_info['时间'])
      file.write('\n')
      file.write("地点"+":"+event_info['地点'])
      file.write('\n')
      file.write("geo_latitude"+":"+event_info['geo_latitude'])
      file.write('\n')
      file.write("geo_longitude"+":"+event_info['geo_longitude'])
      file.write('\n')
      file.write("参加的人"+":"+event_info['参加的人'])
      file.write('\n')
      file.write("活动uid"+":"+event_info['活动uid'])
      file.write('\n')
      file.write("费用"+":"+ event_info['费用'])
      file.write('\n')
      file.write("类型"+":"+ event_info['类型'])
      file.write('\n')*/
      while((str=bf.readLine())!=null)
      {
        cnt++;
        if  (cnt==1)
            e.setUname(str);
        else if  (cnt==2)
              e.setUtime(str);
        else if  (cnt==3)
          e.setUplace(str);
        else if  (cnt==4)
            e.setUlogintude(str.split(":")[1].trim());
        else if  (cnt==5)
            e.setUlatitude(str.split(":")[1].trim());
        else  if(cnt==6)
          e.setUattendant_cnt(str);
        else if  (cnt==7)
          e.setUid(str.split(":")[1].trim());
        else if  (cnt==8)
         e.setUprice(str);
        else if  (cnt==9)
          e.setUcatgory(str);

      else  if(cnt==10) {
          cnt=0;
          list.add(e);
          e = new Event();
        }



      }
    }catch (Exception e)
    {
      e.printStackTrace();
    }
    return list;

  }
  public List getEventsNearby(List<Event> events,double longi,double lati)
  {List<Event> li=new ArrayList<>();

    for(Event e:events)
    {
      double elongi=Double.parseDouble(e.getUlogintude());
      double elati=Double.parseDouble(e.getUlatitude());
      double radius_radius=(elongi-longi)*(elongi-longi)+(elati-lati)*(elati-lati);

      if(Math.sqrt(radius_radius)<0.02)
      {

       li.add(e);
      }
    }
    return li;
  }

  public WxMpXmlOutMessage buildEvents( WxMpXmlMessage wxMessage,
                                 WxMpService service) {


    me.chanjar.weixin.mp.builder.outxml.NewsBuilder m = WxMpXmlOutMessage.NEWS();
    List<WxMpXmlOutNewsMessage.Item> list=new ArrayList<WxMpXmlOutNewsMessage.Item>();

    WxMpXmlOutNewsMessage meg=new WxMpXmlOutNewsMessage();

    int count=0;
    List<Event> events=getEvents("shanghai");
    List<Event> nearby=getEventsNearby(events,wxMessage.getLocationX(),wxMessage.getLocationY());
    for(Event e:nearby)
    {
      if(count++==7)
        break;
      WxMpXmlOutNewsMessage.Item item =new WxMpXmlOutNewsMessage.Item();
      item.setDescription(e.getUname());
      item.setTitle(e.getUname());
      item.setUrl("https://www.douban.com/event/"+e.getUid());

      list.add(item);

    }

    return m.articles(list).fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).build();
  }

  public WxMpXmlOutMessage buildHotelOffers(HRSHotelAvailResponse response, WxMpXmlMessage wxMessage,
                                      WxMpService service) {

/*    HRSHotelAvailResponse resp= getHotelByRadius( response,  wxMessage,
             service);*/
    me.chanjar.weixin.mp.builder.outxml.NewsBuilder m = WxMpXmlOutMessage.NEWS();
    List<WxMpXmlOutNewsMessage.Item> list=new ArrayList<WxMpXmlOutNewsMessage.Item>();

    WxMpXmlOutNewsMessage meg=new WxMpXmlOutNewsMessage();

    int count=0;

for(HRSHotelAvailHotelOffer offer:response.getHotelAvailHotelOffers())
{
  if(count++==5)
    break;
  WxMpXmlOutNewsMessage.Item item =new WxMpXmlOutNewsMessage.Item();
  item.setDescription(offer.getHotel().getHotelName()+" 地址:"+offer.getHotel().getCity()+" "+offer.getHotel().getDistrict()+" "+offer.getHotel().getStreet());
  //item.setUrl("http://");
  if(offer.getHotel().getMedia()!=null&&offer.getHotel().getMedia().size()>0&&offer.getHotel().getMedia().get(0)!=null&&offer.getHotel().getMedia().get(0).getThumbnailURL()!=null)item.setPicUrl(offer.getHotel().getMedia().get(0).getThumbnailURL());
  item.setTitle(offer.getHotel().getHotelName());
  list.add(item);
}

    return m.articles(list).fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).build();
  }

}
