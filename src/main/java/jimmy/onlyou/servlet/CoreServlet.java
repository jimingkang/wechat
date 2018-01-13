/*
package jimmy.onlyou.servlet;

import com.thoughtworks.xstream.XStream;
import jimmy.onlyou.bean.Article;
import jimmy.onlyou.bean.PicAndTextMsg;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

*/
/**
 * @author 高远</n> 邮箱：wgyscsf@163.com</n> 博客 http://blog.csdn.net/wgyscsf</n>
 *         编写时期 2016-4-3 下午4:34:05
 *//*

@RestController
@RequestMapping("/CoreServlet")
public class CoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String TAG = "CoreServlet";

	*/
/*
	 * 第二步：验证服务器地址的有效性 开发者提交信息后，微信服务器将发送GET请求到填写的服务器地址URL上，
	 * GET请求携带四个参数：signature、timestamp、nonce、echostr
	 * 开发者通过检验signature对请求进行校验（下面有校验方式）。 若确认此次GET请求来自微信服务器，请原样返回echostr参数内容，
	 * 则接入生效， 成为开发者成功，否则接入失败。
	 * 
	 * 加密/校验流程如下： 1. 将token、timestamp、nonce三个参数进行字典序排序 2.
	 * 将三个参数字符串拼接成一个字符串进行sha1加密 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
	 *//*

	*/
/*
	 * 字典排序（lexicographical
	 * order）是一种对于随机变量形成序列的排序方法。其方法是，按照字母顺序，或者数字小大顺序，由小到大的形成序列。
	 *//*

	@Override
	@GetMapping(produces = "text/plain;charset=utf-8")
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// 设置编码
		req.setCharacterEncoding("utf-8");
		resp.setContentType("html/text;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		// 获取输出流
		PrintWriter printWriter = resp.getWriter();

		// 设置一个全局的token,开发者自己设置。api这样解释：Token可由开发者可以任意填写，
		// 用作生成签名（该Token会和接口URL中包含的Token进行比对，从而验证安全性）
		String token = "hrs";
		// 根据api说明，获取上述四个参数
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		// // temp:临时打印，观看返回参数情况
		// System.out.println(TAG + ":signature:" + signature + ",timestamp:"
		// + timestamp + ",nonce:" + nonce + ",echostr:" + echostr);
		// 根据api所说的“加密/校验流程”进行接入。共计三步

		// 第一步:将token、timestamp、nonce三个参数进行字典序排序
		String[] parms = new String[] { token, timestamp, nonce };// 将需要字典序排列的字符串放到数组中
		Arrays.sort(parms);// 按照api要求进行字典序排序
		// 第二步:将三个参数字符串拼接成一个字符串进行sha1加密
		// 拼接字符串
		String parmsString = "";// 注意，此处不能=null。
		for (int i = 0; i < parms.length; i++) {
			parmsString += parms[i];
		}
		// sha1加密
		String mParms = null;// 加密后的结果
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		digest.update(parmsString.getBytes());
		byte messageDigest[] = digest.digest();
		// Create Hex String
		StringBuffer hexString = new StringBuffer();
		// 字节数组转换为 十六进制 数
		for (int i = 0; i < messageDigest.length; i++) {
			String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
			if (shaHex.length() < 2) {
				hexString.append(0);
			}
			hexString.append(shaHex);
		}
		mParms = hexString.toString();// 加密结果

		*/
/*
		 * api要求： 若确认此次GET请求来自微信服务器，请原样返回echostr参数内容， 则接入生效， 成为开发者成功，否则接入失败。
		 *//*

		// 第三步： 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信接入成功。
		// System.out.println(TAG + ":" + mParms + "---->" + signature);
		if (mParms.equals(signature)) {
			// System.out.println(TAG + ":" + mParms + "---->" + signature);
			printWriter.write(echostr);
		} else {
			// 接入失败,不用回写
			// System.out.println(TAG + "接入失败");
		}
	}

	*/
/*
	 * 查看api文档关于收发消息推送的消息格式基本一致。 如以下格式： <xml>
	 * <ToUserName><![CDATA[toUser]]></ToUserName>
	 * <FromUserName><![CDATA[fromUser]]></FromUserName>
	 * <CreateTime>1348831860</CreateTime> <MsgType><![CDATA[text]]></MsgType>
	 * <Content><![CDATA[this is a test]]></Content>
	 * <MsgId>1234567890123456</MsgId> </xml> 那么，我们就可以进行统一处理。
	 *//*

	*/
/*
	 * 我们先获取输入流，看输入流里面的信息。通过测试打印输出流，我们可以看到每次用户请求，都会收到req请求，请求格式是xml格式，该信息在文档中有说明。
	 *//*

	*/
/*
	 * 特别注意，req.getInputStream()只能获取一次，并且只能读取一次。如果想要多次读取，需要另外想办法。为了简单起见，
	 * 我们只获取一次req.getInputStream()，不再打印输出流信息。直接打印解析后的信息。
	 *//*

	@Override
	@PostMapping(produces = "application/xml; charset=UTF-8")
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 设置编码
		req.setCharacterEncoding("utf-8");
		resp.setContentType("html/text;charset=utf-8");
		resp.setCharacterEncoding("utf-8");

		*/
/*
		 * 该部分我们获取用户发送的信息，并且解析成<K,V>的形式进行显示
		 *//*

		// 解析用户发送过来的信息
		InputStream is = req.getInputStream();// 拿取请求流
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();
		// 解析xml，将获取到的返回结果xml进行解析成我们习惯的文字信息
		SAXReader reader = new SAXReader();// 第三方jar:dom4j【百度：saxreader解析xml】
		Document document = null;
		try {
			document = reader.read(is);
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList)
			map.put(e.getName(), e.getText());

		// 测试输出
		Set<String> keySet = map.keySet();
		// 测试输出解析后用户发过来的信息
		System.out.println(TAG + "：解析用户发送过来的信息开始");
		for (String key : keySet) {
			System.out.println(key + ":" + map.get(key));
		}
		System.out.println(TAG + "：解析用户发送过来的信息结束");

		*/
/*
		 * 该部分我们尝试按照文档的要求格式给用户回复文本信息、图文消息。重点：按照文档要求构造需要的参数。特别注意：参数区分大小写。
		 *//*


		// //实例1：发送普通文本消息,请查看文档关于“回复文本消息”的xml格式
		//
		// // 第一步：按照回复文本信息构造需要的参数
		// TextMsg textMsg = new TextMsg();
		// textMsg.setToUserName(map.get("FromUserName"));// 发送和接收信息“User”刚好相反
		// textMsg.setFromUserName(map.get("ToUserName"));
		// textMsg.setCreateTime(new Date().getTime());// 消息创建时间 （整型）
		// textMsg.setMsgType("text");// 文本类型消息
		// textMsg.setContent("我是服务器回复给用户的信息");
		//
		// // // 第二步，将构造的信息转化为微信识别的xml格式【百度：xstream bean转xml】
		// XStream xStream = new XStream();
		// xStream.alias("xml", textMsg.getClass());
		// String textMsg2Xml = xStream.toXML(textMsg);
		// System.out.println(textMsg2Xml);
		//
		// // // 第三步，发送xml的格式信息给微信服务器，服务器转发给用户
		// PrintWriter printWriter = resp.getWriter();
		// printWriter.print(textMsg2Xml);

		// //实例2，发送图文消息。请查看文档关于“回复图文消息”的xml格式

		// 第一步：按照回复图文信息构造需要的参数
		List<Article> articles = new ArrayList<Article>();
		Article a = new Article();
		a.setTitle("我是图片标题");
		a.setUrl("www.baidu.com");// 该地址是点击图片跳转后
		a.setPicUrl("http://b.hiphotos.baidu.com/image/pic/item/08f790529822720ea5d058ba7ccb0a46f21fab50.jpg");// 该地址是一个有效的图片地址
		a.setDescription("我是图片的描述");
		articles.add(a);
		PicAndTextMsg picAndTextMsg = new PicAndTextMsg();
		picAndTextMsg.setToUserName(map.get("FromUserName"));// 发送和接收信息“User”刚好相反
		picAndTextMsg.setFromUserName(map.get("ToUserName"));
		picAndTextMsg.setCreateTime(new Date().getTime());// 消息创建时间 （整型）
		picAndTextMsg.setMsgType("news");// 图文类型消息
		picAndTextMsg.setArticleCount(1);
		picAndTextMsg.setArticles(articles);
		// 第二步，将构造的信息转化为微信识别的xml格式【百度：xstream bean转xml】
		XStream xStream = new XStream();
		xStream.alias("xml", picAndTextMsg.getClass());
		xStream.alias("item", a.getClass());
		String picAndTextMsg2Xml = xStream.toXML(picAndTextMsg);
		System.out.println(picAndTextMsg2Xml);
		// 第三步，发送xml的格式信息给微信服务器，服务器转发给用户
		PrintWriter printWriter = resp.getWriter();
		printWriter.print(picAndTextMsg2Xml);
	}
}
*/
