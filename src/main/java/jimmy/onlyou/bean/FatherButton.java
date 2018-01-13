package jimmy.onlyou.bean;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * @author 高远</n> 邮箱：wgyscsf@163.com</n> 博客 http://blog.csdn.net/wgyscsf</n>
 *         编写时期 2016-4-7 下午7:25:59
 */
public class FatherButton extends BaseButton {
	private String button;

	public String getButton() {
		return button;
	}

	public void setButton(String button) {
		this.button = button;
	}

	public List<SonButton> getSonButtons() {
		return sonButtons;
	}

	public void setSonButtons(List<SonButton> sonButtons) {
		this.sonButtons = sonButtons;
	}

	@SerializedName("sub_button")
	private List<SonButton> sonButtons;

}
