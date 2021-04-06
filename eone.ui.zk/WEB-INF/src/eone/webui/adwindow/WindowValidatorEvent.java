package eone.webui.adwindow;

public class WindowValidatorEvent {
	private ADWindow window;
	private String name;

	public WindowValidatorEvent(ADWindow window, String name) {
		this.window = window;
		this.name = name;
	}
	
	public ADWindow getWindow() {
		return this.window;
	}
	
	public String getName() {
		return this.name;
	}
}
