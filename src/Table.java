import java.awt.Color;

import javax.swing.JTable;

public class Table extends JTable {
	
	public Table() {
		this.setGridColor(Color.GRAY);
		this.setRowHeight(18);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setTableHeader(null);
		this.setCellSelectionEnabled(true);
	}
	
}
