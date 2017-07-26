import java.awt.Font;

import javax.swing.JTextPane;

public class TextPane extends JTextPane{
	TextPane() {
		this.setFont(new Font("monospaced", Font.PLAIN, 12));
	}
}
