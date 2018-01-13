package com.github.binarywang.demo.wechat.controller;


import com.github.binarywang.demo.wechat.config.ProjectUrlConfig;
import com.google.gson.Gson;
import jimmy.onlyou.bean.UserInfo;
import jimmy.onlyou.utils.MyHttpUtils;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ldlood on 2017/7/23.
 */
@Controller
@RequestMapping("/wechat")
public class WebWechatController {
    private Logger log = LoggerFactory.getLogger(getClass());
    public static final String OAUTH2_SCOPE_USER_INFO = "snsapi_userinfo";
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpService wxOpenService;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;


    UserInfo userInfo = new UserInfo();

    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl) {
        //1 配置

       // returnUrl="http://1m929o4661.imwork.net/wechat/test";
       // String encodeerUrl= URLEncoder.encode(returnUrl);
        String url = "http://onlyoucd.free.ngrok.cc"+ "/wechat/userInfo";
       // String redirectUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxadce1a5e37f9f850&redirect_uri="+URLEncoder.encode(url)+"&response_type=code&scope=snsapi_userinfo&state="+URLEncoder.encode(returnUrl)+"#wechat_redirect";
       String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url,OAUTH2_SCOPE_USER_INFO, URLEncoder.encode(returnUrl));
        log.info("[微信网页授权获取 code],resule={}", redirectUrl);

        return "redirect:" + redirectUrl;
    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) {


        /**
         * 第四步：拉取用户信息(需scope为 snsapi_userinfo)
         */


        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);

            String url3 = "https://api.weixin.qq.com/sns/userinfo?access_token="
                    + wxMpOAuth2AccessToken.getAccessToken()
                    + "&openid="
                    + wxMpOAuth2AccessToken.getOpenId() + "&lang=zh_CN";
            String json3 = MyHttpUtils.getReturnJson(url3, null);// 拿去返回值

            Gson gson3 = new Gson();
            userInfo = gson3.fromJson(new String(json3.getBytes(), "utf-8"),
                    new UserInfo().getClass());
            System.out.println(userInfo);
        } catch (WxErrorException ex) {
            log.error("exception【微信网页授权】{}", ex);
           // throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(), ex.getError().getErrorMsg());
        }catch(Exception e)
        {
            e.printStackTrace();

        }

        String openId = wxMpOAuth2AccessToken.getOpenId();
        log.info("opiedId: " + openId);
        return "redirect:" + returnUrl + "?openid=" + openId;
    }


/*    @GetMapping("/qrauthorize")
    public String qrAuthorize(@RequestParam("returnUrl") String returnUrl) {
        //1 配置
        String url = projectUrlConfig.getWechatOpenAuthorize() + "/wechat/qruserInfo";
        String redirectUrl = wxOpenService.buildQrConnectUrl(url, WxConsts.QRCONNECT_SCOPE_SNSAPI_LOGIN, URLEncoder.encode(returnUrl));
        log.info("[微信网页登陆获取 code],resule={}", redirectUrl);

        return "redirect:" + redirectUrl;
    }*/

    @GetMapping("/qruserInfo")
    public String qrUserInfo(@RequestParam("code") String code,
                             @RequestParam("state") String returnUrl) {

        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken = wxOpenService.oauth2getAccessToken(code);
        } catch (WxErrorException ex) {
            log.error("【微信网页登陆】{}", ex);
           // throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(), ex.getError().getErrorMsg());
        }

        String openId = wxMpOAuth2AccessToken.getOpenId();
        log.info("opiedId: " + openId);
        return "redirect:" + returnUrl + "?openid=" + openId;
    }
    @RequestMapping(value = "test", produces = "text/html")
    public ModelAndView testHtml(HttpServletRequest request) {

      Map map=  new HashMap<>();
      map.put("userinfo",userInfo);
      //userInfo.getHeadimgurl()

        return new ModelAndView("index", map);
    }

}
