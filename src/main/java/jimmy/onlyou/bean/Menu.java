package jimmy.onlyou.bean;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * @author 高远</n> 邮箱：wgyscsf@163.com</n> 博客 http://blog.csdn.net/wgyscsf</n>
 *         编写时期 2016-4-7 下午7:29:45
 */
public class Menu {
	@SerializedName("button")
	private List<FatherButton> fatherButtons;
	private MatchRule matchrule;

	public MatchRule getMatchrule() {
		return matchrule;
	}

	public void setMatchrule(MatchRule matchrule) {
		this.matchrule = matchrule;
	}

	public List<FatherButton> getFatherButtons() {
		return fatherButtons;
	}

	public void setFatherButtons(List<FatherButton> fatherButtons) {
		this.fatherButtons = fatherButtons;
	}
}
