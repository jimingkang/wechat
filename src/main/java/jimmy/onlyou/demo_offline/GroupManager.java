package jimmy.onlyou.demo_offline;

import com.google.gson.Gson;
import jimmy.onlyou.bean.Group;
import jimmy.onlyou.bean.GroupUnit;
import jimmy.onlyou.utils.AccessTokenUtils;
import jimmy.onlyou.utils.MyHttpUtils;

import java.util.List;

/**
 * @author 高远</n> 编写日期 2016-4-17下午12:31:32</n> 邮箱 wgyscsf@163.com</n> 博客
 *         http://blog.csdn.net/wgyscsf</n> TODO</n>
 */
/*
 * 该类主要测试用户分组管理
 */
public class GroupManager {
	private static final String TAG = "GroupManager";
	
	/*
	 * 测试创建分组
	 */
	//@Test
	public void createGroup() {
		String ACCESS_TOKEN = AccessTokenUtils.getSavedAccess_token();// 获取我们保存且实时更新的ACCESS_TOKEN，实现原理参看第三篇文章
		String url = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token="
				+ ACCESS_TOKEN;// 请求路径
		// String osrJson = "{'group':{'name':'组名'}}";// 简单测试，不再封装对应bean
		// 创建分组群组
		GroupUnit groupUnit = new GroupUnit();
		// 创建分组对象
		Group group = new Group();
		group.setName("测试组名");// 设置新建组名
		groupUnit.setGroup(group);
		Gson gson = new Gson();
		String osrJson = gson.toJson(groupUnit);
		System.out.println(osrJson);
		String returnJson = MyHttpUtils.getReturnJson(url, osrJson);// 我们封装的http工具类
		System.out.println(TAG + "返回结果：" + returnJson);
	}

	/*
	 * 测试查询所有分组
	 */
	//@Test
	public void showAllGroup() {
		String ACCESS_TOKEN = AccessTokenUtils.getSavedAccess_token();// 获取我们保存且实时更新的ACCESS_TOKEN，实现原理参看第三篇文章
		String url = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token="
				+ ACCESS_TOKEN;// 请求路径
		String returnJson = MyHttpUtils.getReturnJson(url, null);// 我们封装的http工具类
		System.out.println(TAG + "返回结果：" + returnJson);

		/*
		 * 我们对返回的数据进行封装
		 */
		GroupUnit groupUnit = new GroupUnit();
		Gson gson = new Gson();
		groupUnit = gson.fromJson(returnJson, groupUnit.getClass());
		List<Group> groups = groupUnit.getGroups();
		for (Group group : groups) {
			System.out.println(TAG + ":组名" + group.getName());
		}
	}
	
	/*
	 * 该方法测试得到用户所在的分组id，需要传递过来一个用户openid
	 */
	public static String getGroup(String openid) {

		String ACCESS_TOKEN = AccessTokenUtils.getSavedAccess_token();// 获取我们保存且实时更新的ACCESS_TOKEN，实现原理参看第三篇文章
		String url = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token="
				+ ACCESS_TOKEN;// 请求路径
		String osrJson = "{\"openid\":\"" + openid + "\"}";// 简单json,不再封装
		System.out.println(osrJson);
		String returnJson = MyHttpUtils.getReturnJson(url, osrJson);// 我们封装的http工具类
		System.out.println(TAG + "返回结果：" + returnJson);
		/*
		 * 封装返回结果
		 */
		Group goup = new Group();
		Gson gson = new Gson();
		goup = gson.fromJson(returnJson, goup.getClass());
		return goup.getGroupid();

	}

	/*
	 * 该方法测试将用户移动到指定分组
	 */
	public static String updateGroup(String openid, String to_groupid) {
		String ACCESS_TOKEN = AccessTokenUtils.getSavedAccess_token();// 获取我们保存且实时更新的ACCESS_TOKEN，实现原理参看第三篇文章
		String url = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token="
				+ ACCESS_TOKEN;// 请求路径
		// 封装请求数据
		Group group = new Group();
		group.setOpenid(openid);
		group.setTo_groupid(to_groupid);
		Gson gson = new Gson();
		String osrJson = gson.toJson(group);
		System.out.println(TAG + osrJson);

		String returnJson = MyHttpUtils.getReturnJson(url, osrJson);// 我们封装的http工具类
		System.out.println(TAG + "返回结果：" + returnJson);

		return returnJson;

	}

	/**
	 * TODO
	 */
	public static void main(String[] args) {

		// 移动分组
		String msg = updateGroup("obM1Qt2oWq3K1FD6MIM1ImCm5ZZE", 101 + "");
		System.out.println(msg);
		// 获取用户分组id
		String id = getGroup("obM1Qt2oWq3K1FD6MIM1ImCm5ZZE");
		System.out.println(id);
	}

}
