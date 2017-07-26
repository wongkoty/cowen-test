import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Label extends JLabel{
	Label(String string) {
		super();
		this.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setBounds(18, 6, 850, 35);
	}
}
