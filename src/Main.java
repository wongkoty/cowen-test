import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.*;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

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

import java.awt.BorderLayout;
import java.awt.Color;


public class Main extends JFrame{

	private JFrame frmCowenTest;
	private static Main window;
	private JLabel msgLabel;
	private JScrollPane oldPosScroll, tableScroll, newPosScrollPane;
	private JPanel oldPosPanel, newPosPanel;
	private JButton btnPos, btnUpdatedPrice, btnGenerateNewPos, btnSaveAs;
	private JTable table;
	private JTextPane posField, newPosField;
	Object data[][] = new Object[570][10];
	List<NewPosition> newPosition = new ArrayList<NewPosition>();
	List<String> originalPositionList = new ArrayList<String>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new Main();
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
		setTitle("Cowen Test");
		setBounds(100, 100, 900, 850);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		makeLabel();
		
		makeOldPosTextPane();
		makeOldPosPanel();
		makeOldPosScroll();
		
		makeTableScroll();
		makeTable();
		
		makeNewPosTextPane();
		makeNewPosPanel();
		makeNewPosScroll();
		
		makePosBtn("Load Positions");
		makeUpdatedPosBtn("Load updated price");
		makeNewPosBtn("Generate New Positions");
		makeSaveAsBtn("Save As");
		
		setVisible(true);
//		initialize();
	}
	
	private void makeSaveAsBtn(String name) {
		btnSaveAs = new JButton(name);
		btnSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileWriter out = null;
				JFileChooser saveFileChooser = new JFileChooser();
				saveFileChooser.setFileFilter(new FileNameExtensionFilter(".txt", "txt"));
				if (saveFileChooser.showSaveDialog(frmCowenTest) == JFileChooser.APPROVE_OPTION) {
					try {
						File file = new File(saveFileChooser.getCurrentDirectory(), saveFileChooser.getSelectedFile().getName() + ".txt");
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
		add(btnSaveAs);
	}

	private void makeNewPosBtn(String name) {
		btnGenerateNewPos = new JButton(name);
		btnGenerateNewPos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int row = 0; row < data.length; row++) {
					if("Option".equals(data[row][8])) {
						newPosition.add(new NewPosition((String)data[row][1], (double)data[row][5], (String)data[row][8],
										(double)data[row][9], (String)data[row][3]));
					} 
				}
				newPosition();
				try {
					Document doc = newPosField.getDocument();
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
		add(btnGenerateNewPos);
	}

	private void makePosBtn(String name) {
		 btnPos = new JButton(name);
		 btnPos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser openOldPosFileChooser = new JFileChooser();
					openOldPosFileChooser.setFileFilter(new FileNameExtensionFilter(".txt", "txt"));

					int returnValue = openOldPosFileChooser.showOpenDialog(window); //Have to reference the frame
					File file = openOldPosFileChooser.getSelectedFile();
					String fileName = file.getAbsolutePath();
					
					if (returnValue == JFileChooser.APPROVE_OPTION) {
						try {
							originalPositionList.clear();
							String positionEntry;
							FileReader reader = new FileReader(fileName);
							BufferedReader br = new BufferedReader(reader);
							Scanner scan = new Scanner(file);
							
							posField.read(br, null);
							posField.requestFocus();
							
							msgLabel.setText("Loaded File");
							while (scan.hasNextLine()) {
								positionEntry = scan.nextLine();
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
			add(btnPos);
	}
	
	private void makeUpdatedPosBtn(String name) {
		btnUpdatedPrice = new JButton(name);
		btnUpdatedPrice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser openNewPosFileChooser = new JFileChooser();
				openNewPosFileChooser.setFileFilter(new FileNameExtensionFilter(".xls", "xls"));
				int returnValue = openNewPosFileChooser.showOpenDialog(window); //Have to reference the frame
				File file = openNewPosFileChooser.getSelectedFile();
				String fileName = file.getAbsolutePath();
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					try {
						Workbook workbook = WorkbookFactory.create(new File(fileName));
						Sheet datatypeSheet = workbook.getSheetAt(0);
							
						int rowStart = Math.min(0, datatypeSheet.getFirstRowNum());
						// Hard coding rowEnd for now because the getLastRow is going to 1000+ even for blank rows
//						int rowEnd = Math.min(1000, datatypeSheet.getLastRowNum());
						int rowEnd = 560;
						
						table.setModel(new DefaultTableModel(rowEnd, 10));
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
			                    	data[rowNum][cn] = currentCell.getStringCellValue();
			                    	table.getModel().setValueAt(currentCell.getStringCellValue(), rowNum, cn);
			                    } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
			                    	data[rowNum][cn] = currentCell.getNumericCellValue();
			                    	table.getModel().setValueAt(currentCell.getNumericCellValue(), rowNum, cn);
			                    }
			                }
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
				
			}
		});
		btnUpdatedPrice.setBounds(250, 777, 152, 29);
		add(btnUpdatedPrice);
	}

	private void makeNewPosScroll() {
		newPosScrollPane = new JScrollPane(newPosPanel);
		newPosScrollPane.setBounds(20, 500, 850, 250);
		newPosScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		newPosScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		add(newPosScrollPane);		
	}

	private void makeNewPosPanel() {
		newPosPanel = new JPanel(new BorderLayout());
		newPosPanel.add(newPosField);
	}

	private void makeNewPosTextPane() {
		newPosField = new JTextPane();
		newPosField.setFont(new Font("monospaced", Font.PLAIN, 12));
	}

	private void makeTable() {
		table = new JTable();
		tableScroll.setViewportView(table);
		table.setGridColor(Color.GRAY);
		table.setRowHeight(18);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setTableHeader(null);
		table.setCellSelectionEnabled(true);
	}

	private void makeTableScroll() {
		tableScroll = new JScrollPane();
		tableScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableScroll.setBounds(18, 280, 850, 203);
		add(tableScroll);
	}

	private void makeOldPosScroll() {
		oldPosScroll = new JScrollPane(oldPosPanel);
		oldPosScroll.setLocation(18, 53);
		oldPosScroll.setSize(850, 210);
		oldPosScroll.getVerticalScrollBar().setUnitIncrement(16);
		oldPosScroll.getHorizontalScrollBar().setUnitIncrement(16);
		add(oldPosScroll);
	}
	
	private void makeLabel() {
		msgLabel = new JLabel("");
		msgLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		msgLabel.setHorizontalAlignment(SwingConstants.CENTER);
		msgLabel.setBounds(18, 6, 850, 35);
		add(msgLabel);
	}
	
	private void makeOldPosTextPane() {
		posField = new JTextPane();
		posField.setFont(new Font("monospaced", Font.PLAIN, 12));
	}
	
	private void makeOldPosPanel() {
		oldPosPanel = new JPanel(new BorderLayout());
		oldPosPanel.add(posField);
	}
	
	public void newPosition() {
		int oldPosListSize = originalPositionList.size();
		int newPosListSize = newPosition.size();
		
		for (int originalPositionCount = 1; originalPositionCount < oldPosListSize; originalPositionCount++) {
			String oldPos = originalPositionList.get(originalPositionCount);
			for (int newPositionCount = 0; newPositionCount < newPosListSize; newPositionCount++) {
				NewPosition newPos = newPosition.get(newPositionCount);
				if (oldPos.substring(19, 24).trim() != null && 
						!oldPos.substring(19, 24).trim().isEmpty() &&
						newPos.getTicker().toLowerCase().contains(oldPos.substring(19, 24).toLowerCase().trim())) {
					if (newPos.getCallOrPut().toLowerCase().equals(oldPos.substring(18, 19).toLowerCase())) {
						if (newPos.getOldPrice().equals(oldPos.substring(44, 56))){
							String newString = oldPos.substring(0, 44) + newPos.getNewPrice() + oldPos.substring(56, 66);
							originalPositionList.set(originalPositionCount, newString);
						}
					}
				}
			}
		}
	}
}

class NewPosition {
	String ticker, secType, oldPrice, newPrice, callOrPut;
	
	NewPosition (String ticker, double oldPrice, String secType, double newPrice, String callOrPut) {
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
