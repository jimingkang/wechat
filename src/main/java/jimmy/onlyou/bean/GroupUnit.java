package bean;

import java.util.List;

/**
 * @author 高远</n>
 * 编写日期   2016-4-17下午1:43:11</n>
 * 邮箱  wgyscsf@163.com</n>
 * 博客  http://blog.csdn.net/wgyscsf</n>
 * TODO</n>
 */
/*
 * 有时候返回是一个list类型；或者是一个类似这样格式的信息：{
 * 'group':{'name':'组名'}}，请注意这个json的“group”。常规的bean，转化为json是{'name':'组名'}这样的格式。
 * 我们需要这样一个类进行处理，我们称之为Group联合体。
 */
public class GroupUnit {
	private Group group;
	private List<Group> groups;

	public Group getGroup() {
		return group;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}


}
