import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class ScrollPane extends JScrollPane{
	ScrollPane(Component view, int x, int y, int width, int height){
		super(view, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.setBounds(x, y, width, height);
		this.getVerticalScrollBar().setUnitIncrement(14);
		this.getHorizontalScrollBar().setUnitIncrement(14);
	}
	ScrollPane(int x, int y, int width, int height){
		super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.setBounds(x, y, width, height);
		this.getVerticalScrollBar().setUnitIncrement(14);
		this.getHorizontalScrollBar().setUnitIncrement(14);
	}
}
