package com.github.binarywang.demo.wechat.utils;

/**
 * Created by jka07@int.hrs.com on 17-12-22.
 */
public class Event {

/*    感兴趣的人: 37
    活动uid: 29888339
    时间: 12月23日 周六 11:00-21:00
    geo_longitude: 121.447258
    地点: 上海 静安区 南京西路1649号 静安公园 8号楼
    活动名称: 圣诞活动 | 限时2天! 魔都一场华丽的白色圣诞，米九十邀你来看雪！
    参加的人: 26
    参加的人_uid: 171410649,84192898,springmashroom,59074813,171401112,162763840,153345150,73193940,171396578,171371663,171355333,145790859,150145590,151013519,157012736,164190542,171270031,119541730,47056396,152304805,77142223,157085793,145061131,76080196,79358148,154388376
    感兴趣的人_uid: 70109977,93318583,151754060,77466566,Youngyuki,49427294,Dorea,137772551,xiaohazz,hara_zeki,134290719,blues1994,166551065,53558951,52370666,Faraille,59468410,69433551,79383066,4626832,120432877,70507211,132296608,54188766,70507849,149872667,4916242,nicetry,35135916,154064809,3802312,147531169,56460280,166251100,140974553,75311522,170625044
    类型: 聚会-集市
    geo_latitude: 31.222584*/
    private  String uinteres_cnt;
  private  String uid;
    private  String utime;
    private  String ulogintude;
    private  String uplace;
    private  String uname;
    private  String uattendant_cnt;
    private  String ulatitude;


    public String getUinteres_cnt() {
        return uinteres_cnt;
    }

    public void setUinteres_cnt(String uinteres_cnt) {
        this.uinteres_cnt = uinteres_cnt;
    }

    public String getUattendant_cnt() {
        return uattendant_cnt;
    }

    public void setUattendant_cnt(String uattendant_cnt) {
        this.uattendant_cnt = uattendant_cnt;
    }




    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUplace() {
        return uplace;
    }

    public void setUplace(String uplace) {
        this.uplace = uplace;
    }

    public String getUtime() {
        return utime;
    }

    public void setUtime(String utime) {
        this.utime = utime;
    }

    public String getUlatitude() {
        return ulatitude;
    }

    public void setUlatitude(String ulatitude) {
        this.ulatitude = ulatitude;
    }

    public String getUlogintude() {
        return ulogintude;
    }

    public void setUlogintude(String ulogintude) {
        this.ulogintude = ulogintude;
    }


}
