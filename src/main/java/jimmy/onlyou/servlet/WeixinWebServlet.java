package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gist.bean.AutoWebParams;
import com.gist.bean.UserInfo;
import com.gist.utils.GlobalParams;
import com.gist.utils.MyHttpUtils;
import com.google.gson.Gson;

/**
 * @author 高远</n>
 * 编写日期   2016-4-29下午8:46:28</n>
 * 邮箱  wgyscsf@163.com</n>
 * 博客  http://blog.csdn.net/wgyscsf</n>
 * TODO</n>
 */
@WebServlet("/WeixinWebServlet")
public class WeixinWebServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 设置编码
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		PrintWriter writer = resp.getWriter();
		/**
		 * 第一步：用户同意授权，获取code:https://open.weixin.qq.com/connect/oauth2/authorize
		 * ?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE
		 * &state=STATE#wechat_redirect
		 */
		// 已经在GuideServlet进行了处理

		/**
		 * 第二步：通过code换取网页授权access_token
		 */
		String code = req.getParameter("code");// 获取返回码
		// 同意授权
		if (code != null) {
			// 拼接请求地址
			String url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
					+ "appid=" + GlobalParams.APPID + "&secret="
					+ GlobalParams.SECERT
					+ "&code=" + code
					+ "&grant_type=authorization_code";
			String json = MyHttpUtils.getReturnJson(url, null);// 拿去返回值
			AutoWebParams autoWebParams = new AutoWebParams();
			Gson gson = new Gson();
			autoWebParams = gson.fromJson(json, new AutoWebParams().getClass());

			/**
			 * 第三步：刷新access_token（如果需要）
			 */
			String url2 = "https://api.weixin.qq.com/sns/oauth2/refresh_token?"
					+ "appid=" + GlobalParams.APPID
					+ "&grant_type=refresh_token&refresh_token="
					+ autoWebParams.getRefresh_token();
			String json2 = MyHttpUtils.getReturnJson(url2, null);// 拿去返回值
			AutoWebParams autoWebParams2 = new AutoWebParams();
			Gson gson2 = new Gson();
			autoWebParams2 = gson2
					.fromJson(json2, new AutoWebParams().getClass());

			/**
			 * 第四步：拉取用户信息(需scope为 snsapi_userinfo)
			 */
			String url3 = "https://api.weixin.qq.com/sns/userinfo?access_token="
					+ autoWebParams2.getAccess_token()
					+ "&openid="
					+ autoWebParams2.getOpenid() + "&lang=zh_CN";
			String json3 = MyHttpUtils.getReturnJson(url3, null);// 拿去返回值
			UserInfo userInfo = new UserInfo();
			Gson gson3 = new Gson();
			userInfo = gson3.fromJson(new String(json3.getBytes(), "utf-8"),
					new UserInfo().getClass());
			System.out.println(userInfo);
			// -----------------------操作结束-------------------------

			// 显示用户信息
			req.setAttribute("userInfo", userInfo);
			req.getRequestDispatcher("userinfo.jsp").forward(req, resp);

		} else {
			writer.print("用户禁止授权，这里处理禁止之后的操作");
			writer.close();
		}
	}

}
