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
  public List<Event> getEvents(){
    List<Event> list=null;
    try {
      FileReader fi = new FileReader("/home/jka07@int.hrs.com/hrs_doc/weixin-java-mp-demo-springboot-master/src/main/resources/"+"shanghai_party.txt");
      BufferedReader bf = new BufferedReader(fi);
      String str=null;
      int cnt=0;
      Event e= new Event();;
       list=new ArrayList<Event>();
      while((str=bf.readLine())!=null)
      {
        cnt++;

      if(cnt==1)
        e.setUinteres_cnt(str);
      else if  (cnt==2)
        e.setUid(str.split(":")[1].trim());
      else if  (cnt==3)
        e.setUtime(str);
      else if  (cnt==4)
        e.setUlogintude(str.split(":")[1].trim());
      else if  (cnt==5)
        e.setUplace(str);
      else if  (cnt==6)
        e.setUname(str);
      else if  (cnt==7)
        e.setUattendant_cnt(str);
      else if  (cnt==11)
        e.setUlatitude(str.split(":")[1].trim());

      else  if(cnt==13) {
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

  public WxMpXmlOutMessage buildEvents( WxMpXmlMessage wxMessage,
                                 WxMpService service) {


    me.chanjar.weixin.mp.builder.outxml.NewsBuilder m = WxMpXmlOutMessage.NEWS();
    List<WxMpXmlOutNewsMessage.Item> list=new ArrayList<WxMpXmlOutNewsMessage.Item>();

    WxMpXmlOutNewsMessage meg=new WxMpXmlOutNewsMessage();

    int count=0;
    List<Event> events=getEvents();
    for(Event e:events)
    {
      if(count++==5)
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
