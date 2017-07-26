import javax.swing.JButton;

public class Button extends JButton{
	Button(String name, int x, int y, int width, int height){
		super(name);
		this.setBounds(x, y, width, height);
	}
}
