package org.ldlood.handler;


import com.hrs.apac.soap.client.HRSHotelAvailHotelOffer;
import com.hrs.apac.soap.client.HRSHotelAvailResponse;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.ldlood.builder.NewsBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class LocationHandler extends AbstractHandler {


  @Override
  public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                  Map<String, Object> context, WxMpService wxMpService,
                                  WxSessionManager sessionManager) {
    if (wxMessage.getMsgType().equals(XmlMsgType.LOCATION)) {
      //TODO 接收处理用户发送的地理位置消息
      try {
       // HotelAvailThread th=new HotelAvailThread("49184");
    // HRSHotelAvailResponse response= th.getHotelAvail( wxMessage.getLocationX().toString(), wxMessage.getLocationY().toString());

       //  HRSHotelAvailResponse response= th.getHotelAvail();
       //   HRSHotelAvailResponse resp=    getHotelByRadius(response,wxMessage);
          String content = "感谢反馈，您的的地理位置已收到！";


        return new NewsBuilder().buildEvents( wxMessage, wxMpService);
        } catch (Exception e) {
        this.logger.error("位置消息接收处理失败", e);
        return null;
      }
    }

    //上报地理位置事件
    this.logger.info("\n上报地理位置 。。。 ");
    this.logger.info("\n纬度 : " + wxMessage.getLatitude());
    this.logger.info("\n经度 : " + wxMessage.getLongitude());
    this.logger.info("\n精度 : " + String.valueOf(wxMessage.getPrecision()));

    //TODO  可以将用户地理位置信息保存到本地数据库，以便以后使用

    return null;
  }

    public HRSHotelAvailResponse  getHotelByRadius(HRSHotelAvailResponse response, WxMpXmlMessage wxMessage
                                                   ){
        HRSHotelAvailResponse  resp=new HRSHotelAvailResponse();
        double longi=wxMessage.getLocationY();
        double lati=wxMessage.getLocationX();
        for(HRSHotelAvailHotelOffer offer:response.getHotelAvailHotelOffers()) {
            double radius_radius=(offer.getHotel().getGeoPosition().getLongitude()-longi)*(offer.getHotel().getGeoPosition().getLongitude()-longi)+(offer.getHotel().getGeoPosition().getLatitude()-lati)*(offer.getHotel().getGeoPosition().getLatitude()-lati);

            if(Math.sqrt(radius_radius)<0.01)
            {

                resp.getHotelAvailHotelOffers().add(offer);
            }
        }
        return resp;

    }

}
