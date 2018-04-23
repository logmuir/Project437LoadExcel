package pkgExcel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ExcelReader {
	public static final String SAMPLE_XLSX_FILE_PATH = "./sample-xlsx-file.xlsx";

	public static void main(String[] args) throws IOException, InvalidFormatException {

		// Creating a Workbook from an Excel file (.xls or .xlsx)
		System.out.println(new File(".").getCanonicalPath());
		Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));
		
		
	//TEMP CODE FOR PTOJECT
		//initializing objects
		Iterator<Sheet> sheetIterator = workbook.sheetIterator();
		DataFormatter dataFormatter = new DataFormatter();
		Iterator<Row> rowIterator; //used to iterate rows
		Iterator<Cell> cellIterator; //used to iterate cells
		Sheet sheet; //used to store current sheet to be processed
		Cell cell; //temp storage for individual cells
		String cellValue; //value from a cell cast as a String
		int rowCounter = 0; //counter to determine which row we are on. primarily to store column headers. 
		int columnCounter; //used to refer each cell to its specific header column
		ArrayList<String> columnHeaders = new ArrayList<String>();
		//player info to store
		String first_name;
		String last_name;
		String GUID;
		//stat info to store
		String stat_abbr;
		String stat_value;
		String stat_name;
		ArrayList<String> statNames = new ArrayList<String>();//TODO: INITIALIZE NAMES 
		int stat_start; // first column in sheet per row that contains a stat.
		int stat_final; //final column in sheet per row that contains stat values to store

		
		//ITERATING SHEETS: while loop obtaining indiviual sheets from file
		while (sheetIterator.hasNext()) {
			sheet = sheetIterator.next(); //obtain next sheet
			
			// ITERATING ROWS: internal while loop to iterate over individual rows in a sheet
			rowIterator = sheet.rowIterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next(); //obtain next row
				
				// ITERATING CELLS: 3rd sub while loop iterating over each cell in a row
				cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					cell = cellIterator.next(); // obtain next cell
					cellValue = dataFormatter.formatCellValue(cell);
					if(rowCounter > 2) { //if we are in any row but the header row
						//switch statement to differentiate between types in the cell
						if(rowCounter < stat_start) {
							switch(cellValue) {
								case "FIRST_NAME": 
									first_name = cellValue;
									break;
								case "LAST_NAME": 
									last_name = cellValue;
									break;
								case "GUID": 
									GUID = cellValue;
									break;
									//TODO: ADD MORE CASES FOR POSITIONS
								default:{
									break;
								}
							}//end Switch statement in the if statement
						}else {
							stat_value = cellValue;
							stat_abbr = columnHeaders.get(columnCounter);
							stat_name = statNames.get(columnCounter - stat_start);
						}
					}else {//only enters statement for the first 3 rows in the sheet
						if (rowCounter == 2) {//columns with the headers of the table
						columnHeaders.add(cellValue);//add each header to the list of headers
						}else if (rowCounter == 1) {//row containing stat locations
							if(rowCounter == 0) {//location in excel with the start location of stat
								stat_start = Integer.valueOf(cellValue);
							}else if(rowCounter == 1) {//location of last stat in file
								stat_final = Integer.valueOf(cellValue);
							}
						}
					}//end else in 3rd while
					
				}//end third while (cells)
				columnCounter++;
			}//end second while (rows)
			rowCounter++;
			
		}//end first while (sheets)
		
	//END TEMP CODE FOR PROJECT
		
		

		// Retrieving the number of sheets in the Workbook
		System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

		/*  
		 * ============================================================= Iterating over
		 * all the sheets in the workbook (Multiple ways)
		 * =============================================================
		 */

		// 1. You can obtain a sheetIterator and iterate over it
		Iterator<Sheet> sheetIterator = workbook.sheetIterator();
		System.out.println("Retrieving Sheets using Iterator");
		while (sheetIterator.hasNext()) {
			Sheet sheet = sheetIterator.next();
			System.out.println("=> " + sheet.getSheetName());
		}

		// 2. Or you can use a for-each loop
		System.out.println("Retrieving Sheets using for-each loop");
		for (Sheet sheet : workbook) {
			System.out.println("=> " + sheet.getSheetName());
		}

		// 3. Or you can use a Java 8 forEach with lambda
		System.out.println("Retrieving Sheets using Java 8 forEach with lambda");
		workbook.forEach(sheet -> {
			System.out.println("=> " + sheet.getSheetName());
		});

		/*
		 * ================================================================== Iterating
		 * over all the rows and columns in a Sheet (Multiple ways)
		 * ==================================================================
		 */

		// Getting the Sheet at index zero
		Sheet sheet = workbook.getSheetAt(0);

		// Create a DataFormatter to format and get each cell's value as String
		DataFormatter dataFormatter = new DataFormatter();

		// 1. You can obtain a rowIterator and columnIterator and iterate over them
		System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
		Iterator<Row> rowIterator = sheet.rowIterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			// Now let's iterate over the columns of the current row
			Iterator<Cell> cellIterator = row.cellIterator();

			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				String cellValue = dataFormatter.formatCellValue(cell);
				System.out.print(cellValue + "\t");
			}
			System.out.println();
		}

		// 2. Or you can use a for-each loop to iterate over the rows and columns
		System.out.println("\n\nIterating over Rows and Columns using for-each loop\n");
		for (Row row : sheet) {
			for (Cell cell : row) {
				String cellValue = dataFormatter.formatCellValue(cell);
				System.out.print(cellValue + "\t");
			}
			System.out.println();
		}

		// 3. Or you can use Java 8 forEach loop with lambda
		System.out.println("\n\nIterating over Rows and Columns using Java 8 forEach with lambda\n");
		sheet.forEach(row -> {
			row.forEach(cell -> {
				String cellValue = dataFormatter.formatCellValue(cell);
				System.out.print(cellValue + "\t");
			});
			System.out.println();
		});

		// Closing the workbook
		workbook.close();
	}
}