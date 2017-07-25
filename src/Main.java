import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ScrollPaneConstants;
import javax.swing.JSplitPane;
import javax.swing.border.LineBorder;
import java.awt.Rectangle;

public class Main {

	
	private JFrame frmCowenTest;
	private final JFileChooser openFileChooser;
	private JTable table;
	private String result = "";
	Object data[][] = new Object[570][10];
//	Object newPosition[][] = new Object[570][5];
//	List<String> newPosition = new ArrayList<String>();
	List<NewPrice> newPosition = new ArrayList<NewPrice>();
	List<String> originalPositionList = new ArrayList<String>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmCowenTest.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
		
		openFileChooser = new JFileChooser();
		openFileChooser.setFileFilter(new FileNameExtensionFilter("Upload file", "txt", "xls"));
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCowenTest = new JFrame();
		frmCowenTest.setTitle("Cowen Test");
		frmCowenTest.setBounds(100, 100, 900, 850);
		frmCowenTest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCowenTest.getContentPane().setLayout(null);
		
		JLabel msgLabel = new JLabel("");
		msgLabel.setHorizontalAlignment(SwingConstants.CENTER);
		msgLabel.setBounds(38, 6, 830, 35);
		frmCowenTest.getContentPane().add(msgLabel);
	
		JScrollPane tableScroll = new JScrollPane();
		tableScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		tableScroll.setBounds(18, 280, 850, 203);
		frmCowenTest.getContentPane().add(tableScroll);
		
		table = new JTable(570, 10);
		tableScroll.setViewportView(table);
		table.setFillsViewportHeight(true);
		table.setGridColor(Color.GRAY);
		table.setRowHeight(20);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		

		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(70);
		columnModel.getColumn(1).setPreferredWidth(155);
		columnModel.getColumn(2).setPreferredWidth(110);
		columnModel.getColumn(3).setPreferredWidth(215);
		columnModel.getColumn(4).setPreferredWidth(90);
		columnModel.getColumn(5).setPreferredWidth(90);
		columnModel.getColumn(6).setPreferredWidth(110);
		columnModel.getColumn(7).setPreferredWidth(100);
		columnModel.getColumn(8).setPreferredWidth(100);
		columnModel.getColumn(9).setPreferredWidth(140);
		table.setTableHeader(null);
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setCellSelectionEnabled(true);
		table.setPreferredScrollableViewportSize(new Dimension(828, 100));
		table.setFillsViewportHeight(true);
		
		JTextPane positionField = new JTextPane();
		positionField.setFont(new Font("monospaced", Font.PLAIN, 12));
		
		JPanel noWrapPanel = new JPanel(new BorderLayout());
		noWrapPanel.add(positionField);
		
		JScrollPane scrollPane = new JScrollPane(noWrapPanel);
		scrollPane.setLocation(18, 53);
		scrollPane.setSize(850, 210);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		frmCowenTest.getContentPane().add(scrollPane);
		

		JTextPane newPositionsField = new JTextPane();
		newPositionsField.setFont(new Font("monospaced", Font.PLAIN, 12));
		
		Document doc = newPositionsField.getDocument();
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(newPositionsField);
		
		JScrollPane newPositionsScrollPane = new JScrollPane(panel);
		newPositionsScrollPane.setBounds(20, 500, 850, 250);
		newPositionsScrollPane.setSize(850, 210);
		newPositionsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		newPositionsScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		frmCowenTest.getContentPane().add(newPositionsScrollPane);
		
		JButton positionButton = new JButton("Load positions");
		positionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnValue = openFileChooser.showOpenDialog(frmCowenTest); //Have to reference the frame
				File file = openFileChooser.getSelectedFile();
				String fileName = file.getAbsolutePath();
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					try {
						String positionEntry;
						FileReader reader = new FileReader(fileName);
						BufferedReader br = new BufferedReader(reader);
						Scanner scan = new Scanner(file);
						msgLabel.setText("Loaded File");
						
						positionField.read(br, null);
						
						positionField.requestFocus();
						while (scan.hasNextLine()) {
							positionEntry = scan.nextLine();
//							 && positionEntry.substring(43, 44).equals("O")
							if (positionEntry.length() >= 66) {
								positionEntry = positionEntry.substring(0, 66);
							}
							originalPositionList.add(positionEntry);
						}
						br.close();
						scan.close();
					} catch(IOException ioe) {
						System.out.println(ioe);
						msgLabel.setText("Failed to load");
					}
				} else {
					msgLabel.setText("No File Chosen");
				}
			}
		});
		
		positionButton.setBounds(58, 777, 117, 29);
		frmCowenTest.getContentPane().add(positionButton);
		
		// Gets XLS new prices
		JButton btnUpdatedPrice = new JButton("Load updated price");
		btnUpdatedPrice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnValue = openFileChooser.showOpenDialog(frmCowenTest); //Have to reference the frame
				File file = openFileChooser.getSelectedFile();
				String fileName = file.getAbsolutePath();

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					try {
						Workbook workbook = WorkbookFactory.create(new File(fileName));
						Sheet datatypeSheet = workbook.getSheetAt(0);
							
						int rowStart = Math.min(0, datatypeSheet.getFirstRowNum());
						// Hard coding rowEnd for now because the getLastRow is going to 1000+ even for blank rows
//						int rowEnd = Math.min(1000, datatypeSheet.getLastRowNum());
						int rowEnd = 570;
						for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
							Row r = datatypeSheet.getRow(rowNum);
							if (r == null) {
								continue;
							}
//							
							// last columns goes to 255+ for some reason, hard coding the last column number for now
							// Working under assumption of broken XLS file, and the dummy file
							int lastColumn = 10;
//							int lastColumn = Math.max(r.getLastCellNum(), 10);
						    for (int cn = 0; cn < lastColumn; cn++) {
			                    Cell currentCell = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
			                    if (currentCell == null || currentCell.getCellTypeEnum() == CellType.BLANK) {
									table.getModel().setValueAt(" ", rowNum, cn);
			                    } else if (currentCell.getCellTypeEnum() == CellType.STRING) {
			                    	result += currentCell.getStringCellValue() + " ";
			                    	data[rowNum][cn] = currentCell.getStringCellValue();
			                    	table.getModel().setValueAt(currentCell.getStringCellValue(), rowNum, cn);
			                    	
			                    } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
			                    	result += currentCell.getNumericCellValue() + " ";
			                    	data[rowNum][cn] = currentCell.getNumericCellValue();
			                    	table.getModel().setValueAt(currentCell.getNumericCellValue(), rowNum, cn);
			                    }
			                }
						    result += "\n";
						}
					} catch(IOException ioe) {
						msgLabel.setText("Failed to load");
					} catch (EncryptedDocumentException e1) {
						e1.printStackTrace();
					} catch (InvalidFormatException e1) {
						e1.printStackTrace();
					}
				} else {
					msgLabel.setText("No File Chosen");
				}
				
			}
		});
		btnUpdatedPrice.setBounds(250, 777, 152, 29);
		frmCowenTest.getContentPane().add(btnUpdatedPrice);
		
		//Generate new positions
		JButton btnGenerateNewPositions = new JButton("Generate new positions file");
		btnGenerateNewPositions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("new position");
				for (int row = 0; row < data.length; row++) {
//					System.out.println(row);
					if("Option".equals(data[row][8])) {
						newPosition.add(new NewPrice((String)data[row][1], (double)data[row][5], (String)data[row][8],
													(double)data[row][9], (String)data[row][3]));
					} 
				}
				System.out.println(newPosition);
				newPosition();
				try {
					for (int originalPosition = 0; originalPosition < originalPositionList.size(); originalPosition++) {
//						if ("346".equals(originalPositionList.get(originalPosition).substring(0, 3))) {
							doc.insertString(doc.getLength(), originalPositionList.get(originalPosition), null);
							doc.insertString(doc.getLength(), "\n", null);
//						}
					}
					
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				newPositionsField.requestFocus();
			}
		});
		btnGenerateNewPositions.setBounds(465, 777, 197, 29);
		frmCowenTest.getContentPane().add(btnGenerateNewPositions);
		
		JButton btnSaveAs = new JButton("Save As");
		btnSaveAs.setBounds(713, 777, 117, 29);
		frmCowenTest.getContentPane().add(btnSaveAs);
		


	}
	
	public void newPosition() {
//		originalPositionList.size();
//		System.out.println(originalPositionList.size());
//		System.out.println(newPosition.size());
//		newPosition.size()
		int testCtrl1 = 5;
//		System.out.println("XLK130".toLowerCase().contains("XLK".toLowerCase()));
		for (int originalPositionCount = 1; originalPositionCount < originalPositionList.size(); originalPositionCount++) {
//			System.out.println(originalPositionList.get(originalPositionCount).substring(19, 24).trim());
//			System.out.println(originalPositionList.get(originalPositionCount).substring(19, 24).trim().isEmpty());
//			System.out.println(originalPositionList.get(originalPositionCount).substring(19, 24).trim() == null);
//			System.out.println(originalPositionList.get(originalPositionCount).substring(19, 24).trim() == "");
//			System.out.println("");
			for (int newPositionCount = 0; newPositionCount < newPosition.size(); newPositionCount++) {
//				System.out.println(newPosition.get(newPositionCount).getTicker());
//				System.out.println(newPosition.get(newPositionCount).getTicker().toLowerCase().contains(originalPositionList.get(originalPositionCount).substring(19, 24).trim()));
//				System.out.println(originalPositionList.get(originalPositionCount).substring(19, 24));
				if (originalPositionList.get(originalPositionCount).substring(19, 24).trim() != null && 
						!originalPositionList.get(originalPositionCount).substring(19, 24).trim().isEmpty() &&
						newPosition.get(newPositionCount).getTicker().toLowerCase().contains(originalPositionList.get(originalPositionCount).substring(19, 24).toLowerCase().trim())) {
//					System.out.println("inside if");
//					System.out.println(newPosition.get(newPositionCount).getCallOrPut());
//					System.out.println(originalPositionList.get(originalPositionCount).substring(18, 19));
					if (newPosition.get(newPositionCount).getCallOrPut().toLowerCase().equals(originalPositionList.get(originalPositionCount).substring(18, 19).toLowerCase())) {
//						System.out.println("it is a call or a put");
						System.out.println(newPosition.get(newPositionCount).getTicker());
						System.out.println(newPosition.get(newPositionCount).getOldPrice());
						System.out.println(originalPositionList.get(originalPositionCount).substring(44, 56));
						if (newPosition.get(newPositionCount).getOldPrice().equals(originalPositionList.get(originalPositionCount).substring(44, 56))){
							System.out.println("PRICES MATCH");
//							System.out.println(newPosition.get(newPositionCount).getNewPrice());
//							System.out.println(newPosition.get(newPositionCount).getOldPrice());
//							System.out.println("=====================================");
							String originalPositionStr = originalPositionList.get(originalPositionCount);
//							System.out.println(originalPositionStr);
							System.out.println(originalPositionStr.substring(0, 44));
							System.out.println(newPosition.get(newPositionCount).getNewPrice());
							System.out.println(originalPositionStr.substring(56, 66));
//							System.out.println(originalPositionStr.length());
							String newString = originalPositionStr.substring(0, 44) + newPosition.get(newPositionCount).getNewPrice() + originalPositionStr.substring(56, 66);
							originalPositionList.set(originalPositionCount, newString);
						}
					}
//					System.out.println(newPosition.get(newPositionCount).getTicker());
//					System.out.println(originalPositionList.get(originalPositionCount).substring(44, 56));
//					&& "o".equals(originalPositionList.get(originalPositionCount).substring(43, 44).toLowerCase())
					
				}
			}
//			System.out.println("===============");
		}

	}
	
}

class NewPrice {
	String ticker, secType, oldPrice, newPrice, callOrPut;
	
	NewPrice (String ticker, double oldPrice, String secType, double newPrice, String callOrPut) {
		this.ticker = this.normalizeTicker(ticker);
		this.oldPrice = this.normalizePrice(oldPrice);
		this.secType = this.normalizeSecType(secType);
		this.newPrice = this.normalizePrice(newPrice);
		this.callOrPut = this.normalizeCallOrPut(callOrPut);
	}
	
	private String normalizePrice(double price) {
		DecimalFormat df = new DecimalFormat("#.####");
		String priceStr = Double.toString(Double.valueOf(df.format(price)));
		
		int lengthOfPrice = priceStr.substring(priceStr.indexOf(".")).length();
		System.out.println(priceStr);
		System.out.println(lengthOfPrice);
		for(int i = 0; i < 7 - lengthOfPrice; i++) {
			priceStr += "0";
		}
		priceStr = priceStr.replace(".", "");
		priceStr = ("000000000000" + priceStr).substring(priceStr.length());
		System.out.println(priceStr);
		return priceStr;
	}

	private String normalizeCallOrPut(String callOrPut) {
		if (callOrPut != null) {
			return callOrPut.substring(0, 1);
		}
		return callOrPut;
	}

	private String normalizeSecType(String secType) {
		if (secType != null) {
			return secType.toUpperCase();
		}
		return secType;
	}

	private String normalizeTicker(String ticker) {
		if(ticker != null && ticker.length() > 6) {
			return ticker.substring(0, 6);
		} else if (ticker == null) {
			return "";
		}
		return ticker;

	}

	public String getTicker() {
		return ticker;
	}
	
	public String getSecType() {
		return secType;
	}
	
	public String getCallOrPut() {
		return callOrPut;
	}


	public String getOldPrice() {
		return oldPrice;
	}

	public String getNewPrice() {
		return newPrice;
	}

}


class CustomButton extends JButton {
	CustomButton(int x, int y, int width, int height) {
		System.out.println("new jbutton");
	}

	
}
