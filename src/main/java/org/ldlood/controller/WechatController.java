package org.ldlood.controller;

import jimmy.onlyou.bean.UserInfo;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.ldlood.config.ProjectUrlConfig;
import org.ldlood.enums.ResultEnum;
import org.ldlood.exception.SellException;
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
@Slf4j
public class WechatController {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpService wxOpenService;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;
    public static final String OAUTH2_SCOPE_USER_INFO = "snsapi_userinfo";
    UserInfo userInfo = new UserInfo();
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl) {
        //1 配置
        String url = projectUrlConfig.getWechatMpAuthorize() + "/wechat/userInfo";
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, OAUTH2_SCOPE_USER_INFO, URLEncoder.encode(returnUrl));
        log.info("[微信网页授权获取 code],resule={}", redirectUrl);

        return "redirect:" + redirectUrl;
    }
int i=0;
    String oldCode;
    WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) {


        try {i++;
            if(i==1) {
                oldCode = code;
                wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            }
            else if(oldCode.equalsIgnoreCase(code))
            {


                i=0;
            }

        } catch (WxErrorException ex) {
            log.error("【微信网页授权】{}", ex);
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(), ex.getError().getErrorMsg());
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
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(), ex.getError().getErrorMsg());
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
