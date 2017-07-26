import java.util.*;
import java.util.List;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class Main extends JFrame{
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
		
		makeTextPane();
		makePanel();
		makeScroll();
		makeTable();
		
		makePosBtn();
		
		addPosBtnEvent();
		addUpdatedPosBtnEvent();
		addNewPosBtnEvent();
		addSaveAsBtnEvent();
		
		setVisible(true);
	}
	
	private void addSaveAsBtnEvent() {
		btnSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileWriter out = null;
				JFileChooser saveFileChooser = new JFileChooser();
				saveFileChooser.setFileFilter(new FileNameExtensionFilter(".txt", "txt"));
				if (saveFileChooser.showSaveDialog(window) == JFileChooser.APPROVE_OPTION) {
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
	}
	
	private void addNewPosBtnEvent() {
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
	}
	
	private void addUpdatedPosBtnEvent() {
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
	}
	
	private void addPosBtnEvent() {
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

	private void makePosBtn() {
		btnPos = new Button("Load Positions", 58, 777, 117, 29);
		btnUpdatedPrice = new Button("Load updated price", 250, 777, 152, 29);
		btnGenerateNewPos = new Button("Generate New Positions", 465, 777, 197, 29);
		btnSaveAs = new Button("Save As", 713, 777, 117, 29);
		
		add(btnPos);
		add(btnUpdatedPrice);
		add(btnGenerateNewPos);
		add(btnSaveAs);
	}
	
	private void makeTable() {
		table = new Table();
		tableScroll.setViewportView(table);
	}
	
	private void makeScroll() {
		oldPosScroll = new ScrollPane(oldPosPanel, 18, 53, 850, 210);
		tableScroll = new ScrollPane(18, 280, 850, 203);
		newPosScrollPane = new ScrollPane(newPosPanel, 20, 500, 850, 250);
		add(oldPosScroll);
		add(tableScroll);
		add(newPosScrollPane);	
	}
	
	private void makePanel() {
		oldPosPanel = new Panel(new BorderLayout());
		newPosPanel = new Panel(new BorderLayout());
		oldPosPanel.add(posField);
		newPosPanel.add(newPosField);
	}
	
	private void makeTextPane() {
		posField = new TextPane();
		newPosField = new TextPane();
	}
	
	private void makeLabel() {
		msgLabel = new Label("");
		add(msgLabel);
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
