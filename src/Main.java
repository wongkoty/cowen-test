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
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import javax.swing.border.LineBorder;

public class Main {

	private JFrame frmCowenTest;
	private JTable table;
	private String result = "";
	Object data[][] = new Object[570][10];
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
		msgLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		msgLabel.setHorizontalAlignment(SwingConstants.CENTER);
		msgLabel.setBounds(18, 6, 850, 35);
		frmCowenTest.getContentPane().add(msgLabel);
	
		// Original Positions
		JTextPane posField = new JTextPane();
		posField.setFont(new Font("monospaced", Font.PLAIN, 12));
		
		JPanel oldPosPanel = new JPanel(new BorderLayout());
		oldPosPanel.add(posField);
		
		JScrollPane oldPosScrollPane = new JScrollPane(oldPosPanel);
		oldPosScrollPane.setLocation(18, 53);
		oldPosScrollPane.setSize(850, 210);
		oldPosScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		oldPosScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		frmCowenTest.getContentPane().add(oldPosScrollPane);
		
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
		

		JTextPane newPosField = new JTextPane();
		newPosField.setFont(new Font("monospaced", Font.PLAIN, 12));
		
		Document doc = newPosField.getDocument();
		
		JPanel newPosPanel = new JPanel(new BorderLayout());
		newPosPanel.add(newPosField);
		
		JScrollPane newPosScrollPane = new JScrollPane(newPosPanel);
		newPosScrollPane.setBounds(20, 500, 850, 250);
		newPosScrollPane.setSize(850, 210);
		newPosScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		newPosScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		frmCowenTest.getContentPane().add(newPosScrollPane);
		
		JButton btnPos = new JButton("Load positions");
		btnPos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser openOldPosFileChooser = new JFileChooser();
				openOldPosFileChooser.setFileFilter(new FileNameExtensionFilter(".txt", "txt"));

				int returnValue = openOldPosFileChooser.showOpenDialog(frmCowenTest); //Have to reference the frame
				File file = openOldPosFileChooser.getSelectedFile();
				String fileName = file.getAbsolutePath();
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					try {
						String positionEntry;
						FileReader reader = new FileReader(fileName);
						BufferedReader br = new BufferedReader(reader);
						Scanner scan = new Scanner(file);
						msgLabel.setText("Loaded File");
						
						posField.read(br, null);
						
						posField.requestFocus();
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
		
		btnPos.setBounds(58, 777, 117, 29);
		frmCowenTest.getContentPane().add(btnPos);
		
		// Gets XLS new prices
		JButton btnUpdatedPrice = new JButton("Load updated price");
		btnUpdatedPrice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser openNewPosFileChooser = new JFileChooser();
				openNewPosFileChooser.setFileFilter(new FileNameExtensionFilter(".xls", "xls"));
				int returnValue = openNewPosFileChooser.showOpenDialog(frmCowenTest); //Have to reference the frame
				File file = openNewPosFileChooser.getSelectedFile();
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
					table.setModel(new DefaultTableModel(data.length, 10));
				} else {
					msgLabel.setText("No File Chosen");
				}
				
			}
		});
		btnUpdatedPrice.setBounds(250, 777, 152, 29);
		frmCowenTest.getContentPane().add(btnUpdatedPrice);
		
		//Generate new positions
		JButton btnGenerateNewPos = new JButton("Generate new positions file");
		btnGenerateNewPos.addActionListener(new ActionListener() {
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
						doc.insertString(doc.getLength(), originalPositionList.get(originalPosition), null);
						doc.insertString(doc.getLength(), "\n", null);
					}
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				newPosField.requestFocus();
			}
		});
		btnGenerateNewPos.setBounds(465, 777, 197, 29);
		frmCowenTest.getContentPane().add(btnGenerateNewPos);
		
		JButton btnSaveAs = new JButton("Save As");
		btnSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileWriter out = null;
				JFileChooser saveFileChooser = new JFileChooser();
				saveFileChooser.setFileFilter(new FileNameExtensionFilter(".txt", "txt"));
				if (saveFileChooser.showSaveDialog(frmCowenTest) == JFileChooser.APPROVE_OPTION) {
				  File file = saveFileChooser.getSelectedFile();
					try {
						System.out.println(saveFileChooser.getCurrentDirectory());
						System.out.println(saveFileChooser.getSelectedFile().getName());
						file = new File(saveFileChooser.getCurrentDirectory(), saveFileChooser.getSelectedFile().getName() + ".txt");
						out = new FileWriter(file);
						out.write(newPosField.getText());
						out.close();
						msgLabel.setText("File saved");
					} catch (FileNotFoundException exception) {
						exception.printStackTrace();
						msgLabel.setText("Error in saving");
					} catch (IOException exception) {
						exception.printStackTrace();
						msgLabel.setText("Error in saving");
					}
				} else {
					msgLabel.setText("No selection");
				}


			}
		});
		btnSaveAs.setBounds(713, 777, 117, 29);
		frmCowenTest.getContentPane().add(btnSaveAs);
		


	}
	
	public void newPosition() {
//		originalPositionList.size();
//		System.out.println(originalPositionList.size());
//		System.out.println(newPosition.size());
//		newPosition.size()
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
				}
			}
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
		for(int i = 0; i < 7 - lengthOfPrice; i++) {
			priceStr += "0";
		}
		priceStr = priceStr.replace(".", "");
		priceStr = ("000000000000" + priceStr).substring(priceStr.length());
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
