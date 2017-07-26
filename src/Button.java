import javax.swing.JButton;

public class Button extends JButton {
	
	public Button(String name, int x, int y, int width, int height) {
		super(name);
		this.setBounds(x, y, width, height);
	}
	
}
