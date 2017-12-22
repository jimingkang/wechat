package com.github.binarywang.demo.wechat.builder;

import com.github.binarywang.demo.wechat.utils.Event;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
public class TextBuilder extends AbstractBuilder {
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
      /*      private  String uinteres_cnt;
        private  String uid;
        private  String utime;
        private  String ulogintude;
        private  String uplace;
        private  String uname;
        private  String uattendant_cnt;
        private  String ulatitude;*/
        if(cnt==1)
          e.setUinteres_cnt(str);
        else if  (cnt==2)
          e.setUid(str);
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
  @Override
  public WxMpXmlOutMessage build(String content, WxMpXmlMessage wxMessage,
                                 WxMpService service) {
    List<Event> list=getEvents();
    WxMpXmlOutTextMessage m = WxMpXmlOutMessage.TEXT().content(list.get(0).getUname())
        .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
        .build();
    return m;
  }

}
