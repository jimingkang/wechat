package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.gist.bean.Access_token;
import com.google.gson.Gson;

/**
 * @author 高远</n> 邮箱：wgyscsf@163.com</n> 博客 http://blog.csdn.net/wgyscsf</n>
 *         编写时期 2016-4-7 下午5:44:33
 */
public class AccessTokenUtils {
	private static final long MAX_TIME = 7200 * 1000;// 微信允许最长Access_token有效时间（ms）
	private static final String TAG = "WeixinApiTest";// TAG
	private static final String APPID = "wx889b020b3666b0b8";// APPID
	private static final String SECERT = "6da7676bf394f0a9f15fbf06027856bb";// 秘钥

	/*
	 * 该方法实现获取Access_token、保存并且只保存2小时Access_token。如果超过两个小时重新获取；如果没有超过两个小时，直接获取。该方法依赖
	 * ：public static String getAccessToken()；
	 * 
	 * 思路:将获取到的Access_token和当前时间存储到file里，
	 * 取出时判断当前时间和存储里面的记录的时间的时间差，如果大于MAX_TIME,重新获取，并且将获取到的存储到file替换原来的内容
	 * ，如果小于MAX_TIME，直接获取。
	 */
	// 为了调用不抛异常，这里全部捕捉异常，代码有点长
	public static String getSavedAccess_token() {
		Gson gson = new Gson();// 第三方jar,处理json和bean的转换
		String mAccess_token = null;// 需要获取的Access_token；
		FileOutputStream fos = null;// 输出流
		FileInputStream fis = null;// 输入流
		File file = new File("temp_access_token.temp");// Access_token保存的位置
		try {
			// 如果文件不存在，创建
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 如果文件大小等于0，说明第一次使用，存入Access_token
		if (file.length() == 0) {
			try {
				mAccess_token = getAccessToken();// 获取AccessToken
				Access_token at = new Access_token();
				at.setAccess_token(mAccess_token);
				at.setExpires_in(System.currentTimeMillis() + "");// 设置存入时间
				String json = gson.toJson(at);
				fos = new FileOutputStream(file, false);// 不允许追加
				fos.write((json).getBytes());// 将AccessToken和当前时间存入文件
				fos.close();
				return mAccess_token;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 读取文件内容
			byte[] b = new byte[2048];
			int len = 0;
			try {
				fis = new FileInputStream(file);
				len = fis.read(b);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String mJsonAccess_token = new String(b, 0, len);// 读取到的文件内容
			Access_token access_token = gson.fromJson(mJsonAccess_token,
					new Access_token().getClass());
			if (access_token.getExpires_in() != null) {
				long saveTime = Long.parseLong(access_token.getExpires_in());
				long nowTime = System.currentTimeMillis();
				long remianTime = nowTime - saveTime;
				// System.out.println(TAG + "时间差：" + remianTime + "ms");
				if (remianTime < MAX_TIME) {
					Access_token at = gson.fromJson(mJsonAccess_token,
							new Access_token().getClass());
					mAccess_token = at.getAccess_token();
					return mAccess_token;
				} else {
					mAccess_token = getAccessToken();
					Access_token at = new Access_token();
					at.setAccess_token(mAccess_token);
					at.setExpires_in(System.currentTimeMillis() + "");
					String json = gson.toJson(at);
					try {
						fos = new FileOutputStream(file, false);// 不允许追加
						fos.write((json).getBytes());
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return mAccess_token;
				}

			} else {
				return null;
			}
		}

		return mAccess_token;
	}

	/*
	 * 获取微信服务器AccessToken。该部分和getAccess_token() 一致，不再加注释
	 */
	public static String getAccessToken() {
		String urlString = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
				+ APPID + "&secret=" + SECERT;
		String reslut = null;
		try {
			URL reqURL = new URL(urlString);
			HttpsURLConnection httpsConn = (HttpsURLConnection) reqURL
					.openConnection();
			InputStreamReader isr = new InputStreamReader(
					httpsConn.getInputStream());
			char[] chars = new char[1024];
			reslut = "";
			int len;
			while ((len = isr.read(chars)) != -1) {
				reslut += new String(chars, 0, len);
			}
			isr.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		Gson gson = new Gson();
		Access_token access_token = gson.fromJson(reslut,
				new Access_token().getClass());
		if (access_token.getAccess_token() != null) {
			return access_token.getAccess_token();
		} else {
			return null;
		}
	}
}
