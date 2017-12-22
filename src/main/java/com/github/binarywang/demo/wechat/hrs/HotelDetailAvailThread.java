package com.github.binarywang.demo.wechat.hrs;

import com.hrs.apac.soap.client.*;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;

/**
 * Created by jka07@int.hrs.com on 17-12-18.
 */
@Component
public class HotelDetailAvailThread {

    @Autowired
    private RedisHelper redisHelper;
    private HRSHotelDetailAvailRequest hrsHotelDetailAvailRequest = new HRSHotelDetailAvailRequest();
    public  HRSCredentials getCredential() {
        HRSCredentials cre = new HRSCredentials();
        cre.setClientKey("1077551001");
        cre.setClientPassword("tTLTXwv]8q@eN8H");
        cre.setCustomerKey("1077551001");
        cre.setClientType("921");
        return cre;
    }

   public HotelDetailAvailThread()
    {
        this("49184");
    }
    public  HRSLocale getLocal() {

        HRSLocale local = new HRSLocale();
        HRSLanguage lang = new HRSLanguage();
        lang.setIso3Language("ZHO");
        local.setLanguage(lang);
        local.setIsoCurrency("CNY");
        local.setIso3Country("CHN");
        return local;

    }

public HotelDetailAvailThread(String locationId){
    initHotelAvail(locationId);
}

    public  void initHotelAvail(String locationId){

        hrsHotelDetailAvailRequest.setCredentials(getCredential());

        hrsHotelDetailAvailRequest.setLocale(getLocal());


        HRSHotelAvailCriterion hotelAvailCriterion=new HRSHotelAvailCriterion();


        //  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //  Date date1 = simpleDateFormat.parse("2017-08-15");
        // Date date1 = new Date();
        GregorianCalendar gcal_from = new GregorianCalendar();
        //gcal_from.setTime(date1);
        // XMLGregorianCalendar from = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal_from);
        XMLGregorianCalendarImpl from=new XMLGregorianCalendarImpl(gcal_from);
        hotelAvailCriterion.setFrom(from);

        //  Date date2 = new Date();
        GregorianCalendar gcal_to= new GregorianCalendar();
        gcal_to.add(GregorianCalendar.DAY_OF_MONTH,1);
        XMLGregorianCalendarImpl to=new XMLGregorianCalendarImpl(gcal_to);

        hotelAvailCriterion.setTo(to);
        HRSHotelAvailRoomCriterion roomCriterion=new HRSHotelAvailRoomCriterion();
        roomCriterion.setId(0);
        roomCriterion.setAdultCount(1);
        roomCriterion.setRoomType("single");
        HRSHotelAvailRoomCriterion roomCriterion2=new HRSHotelAvailRoomCriterion();
        roomCriterion2.setId(1);
        roomCriterion2.setAdultCount(1);
        roomCriterion2.setRoomType("double");
        hotelAvailCriterion.getRoomCriterias().add(roomCriterion2);
        hotelAvailCriterion.getRoomCriterias().add(roomCriterion);



        HRSGenericCriterion genericCriterion=new HRSGenericCriterion();
        genericCriterion.setKey("returnHotels");
        genericCriterion.setValue("true");
        HRSGenericCriterion genericCriterion2=new HRSGenericCriterion();
        genericCriterion2.setKey("returnMainMedia");
        genericCriterion2.setValue("true");
        HRSGenericCriterion genericCriterion3=new HRSGenericCriterion();
        genericCriterion3.setKey("returnMedia");
        genericCriterion3.setValue("false");
        HRSGenericCriterion genericCriterion4=new HRSGenericCriterion();
        genericCriterion4.setKey("returnFreeServices");
        genericCriterion4.setValue("false");


        hrsHotelDetailAvailRequest.getGenericCriterias().add(genericCriterion);
        hrsHotelDetailAvailRequest.getGenericCriterias().add(genericCriterion2);
        hrsHotelDetailAvailRequest.getGenericCriterias().add(genericCriterion3);
        //  hrsHotelDetailAvailRequest.getGenericCriterias().add(genericCriterion4);

        hrsHotelDetailAvailRequest.setAvailCriterion(hotelAvailCriterion);

    }
    public  HRSHotelDetailAvailResponse getCachedHotelDetailAvail()
    {

        HRSHotelDetailAvailResponse response=null;

            try {

                HRSHotelAvailRoomCriterion roomsingle = new HRSHotelAvailRoomCriterion();
                roomsingle.setRoomType("single");
                roomsingle.setAdultCount(1);
                roomsingle.setId(0);
                HRSHotelAvailRoomCriterion roomdouble = new HRSHotelAvailRoomCriterion();
                roomdouble.setRoomType("double");
                roomdouble.setAdultCount(1);
                roomdouble.setId(1);
                hrsHotelDetailAvailRequest.getAvailCriterion().getRoomCriterias().clear();
                hrsHotelDetailAvailRequest.getAvailCriterion().getRoomCriterias().add(roomsingle);
                hrsHotelDetailAvailRequest.getAvailCriterion().getRoomCriterias().add(roomdouble);
                HRSService service = new HRSService();
                response = service.getHRSSoapServicePort().hotelDetailAvail(hrsHotelDetailAvailRequest);

                service = null;



            } catch (Exception e) {
                e.printStackTrace();
            }




        return response;
    }


    public synchronized HRSHotelDetailAvailResponse getHotellDetailAvail()
    {
        HRSHotelDetailAvailResponse response=null;
        GregorianCalendar today = new GregorianCalendar();
        XMLGregorianCalendar from = hrsHotelDetailAvailRequest.getAvailCriterion().getFrom();
        XMLGregorianCalendar to = hrsHotelDetailAvailRequest.getAvailCriterion().getTo();
        int daydiff_start = (int) (from.toGregorianCalendar().getTimeInMillis() - today.getTimeInMillis()) / (24 * 60 * 60 * 1000);

        int daydiff_period = (int) (to.toGregorianCalendar().getTimeInMillis() - from.toGregorianCalendar().getTimeInMillis()) / (24 * 60 * 60 * 1000);

        XMLGregorianCalendar copy=(XMLGregorianCalendar)from.clone();

        XMLGregorianCalendar nextday= nextDay(copy);
        System.out.println(nextday.getTimezone());


        hrsHotelDetailAvailRequest.getAvailCriterion().setTo(nextday); // find the first one day cache
        response=getCachedHotelDetailAvail();



        return response;
    }
    public synchronized XMLGregorianCalendar nextDay(XMLGregorianCalendar gc)
    {
        //int[] daysInMonth = new int[]{0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int year=gc.getYear();
        int mon=gc.getMonth();
        int day=gc.getDay();
        switch(mon) {
            case 1: {
                if (day < 31)
                    day = day + 1;
                else {
                    mon = mon + 1;
                    day = 1;
                }
                break;
            }
            case 2: {
                if (day < 28)
                    day = day + 1;
                else {
                    mon = mon + 1;
                    day = 1;
                }
                break;
            }
            case 3: {
                if (day < 31)
                    day = day + 1;
                else {
                    mon = mon + 1;
                    day = 1;
                }
                break;
            }
            case 4: {
                if (day < 30)
                    day = day + 1;
                else {
                    mon = mon + 1;
                    day = 1;
                }
                break;
            }
            case 5: {
                if (day < 31)
                    day = day + 1;
                else {
                    mon = mon + 1;
                    day = 1;
                }
                break;
            }
            case 6: {
                if (day < 30)
                    day = day + 1;
                else {
                    mon = mon + 1;
                    day = 1;
                }
                break;
            }
            case 7: {
                if (day < 31)
                    day = day + 1;
                else {
                    mon = mon + 1;
                    day = 1;
                }
                break;
            }
            case 8: {
                if (day < 31)
                    day = day + 1;
                else {
                    mon = mon + 1;
                    day = 1;
                }
                break;
            }
            case 9: {
                if (day < 30)
                    day = day + 1;
                else {
                    mon = mon + 1;
                    day = 1;
                }
                break;
            }
            case 10: {
                if (day < 31)
                    day = day + 1;
                else {
                    mon = mon + 1;
                    day = 1;
                }
                break;
            }
            case 11: {
                if (day < 30)
                    day = day + 1;
                else {
                    mon = mon + 1;
                    day = 1;
                }
                break;
            }
            case 12: {
                if (day < 31)
                    day = day + 1;
                else {
                    year=year+1;
                    mon = 1;
                    day = 1;
                }
                break;
            }


        }
        gc.setYear(year);
        gc.setMonth(mon);
        gc.setDay(day);
        return gc;
    }

}
