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
		
		
	//TEMP CODE FOR PROJECT
		//initializing objects
		Iterator<Sheet> sheetIterator = workbook.sheetIterator();
		DataFormatter dataFormatter = new DataFormatter();
		Iterator<Row> rowIterator; //used to iterate rows
		Iterator<Cell> cellIterator; //used to iterate cells
		Sheet sheet; //used to store current sheet to be processed
		Cell cell; //temp storage for individual cells
		String cellValue; //value from a cell cast as a String
		int rowCounter = 0; //counter to determine which row we are on. primarily to store column headers. 
		int columnCounter = 0; //used to refer each cell to its specific header column
		ArrayList<String> columnHeaders = new ArrayList<String>();
		//player info to store
		String first_name = "";
		String last_name = "";
		String GUID = "";
		int player_id = 0;//TODO: temp int for id
		//stat info to store
		int stat_id = 0; //TODO: temp int for id check for actual id through oracle.
		String stat_abbr = "";
		String stat_value = "";
		String stat_name = "";
		String stat_cast_as = "";// TODO: also a temp (set in the actual code)
		ArrayList<String> statNames = new ArrayList<String>();//INITIALIZE NAMES 
		ArrayList<String> statValues = new ArrayList<String>();//to store values of stat from each row
		ArrayList<Stat> playerStats = new ArrayList<Stat>();  // to store each stat for a row
		int stat_start = 0; // first column in sheet per row that contains a stat.
		int stat_final = 0; //final column in sheet per row that contains stat values to store
		//position info to store

		
		//ITERATING SHEETS: while loop obtaining individual sheets from file
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
							switch(columnHeaders.get(columnCounter)) {
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
						}else if(rowCounter < stat_final){
							stat_value = cellValue; //save each stat value in a list
							statValues.add(stat_value);
							stat_abbr = columnHeaders.get(columnCounter);
							stat_name = statNames.get(columnCounter - stat_start);
							stat_cast_as = "int"; //TODO: get actual casts from excel
							Stat tempStat = new Stat(stat_name, stat_abbr, stat_cast_as);
							playerStats.add(tempStat);//add new stat to a list for each row
						}
					}else {//only enters statement for the first 3 rows in the sheet
						if (rowCounter == 2) {//columns with the headers of the table
							columnHeaders.add(cellValue);//add each header to the list of headers
						}else if (rowCounter == 1) {//row containing stat locations
							if(columnCounter == 0) {//location in excel with the start location of stat
								stat_start = Integer.valueOf(cellValue);
							}else if(columnCounter == 1) {//location of last stat in file
								stat_final = Integer.valueOf(cellValue);
							}else if(columnCounter > 2){
								statNames.add(cellValue);
							}
						}
					}//end else in 3rd while
					
				}//end third while (cells)
				columnCounter++;
			}//end second while (rows)
			//TODO: get player id using player GUID or generated one for new players
			//create player from player info in row gathered
			Player tempPlayer = new Player(player_id, GUID, first_name, last_name);
			//TODO: create season* classes
			//TODO: add all classes to the database
			rowCounter++;
			
		}//end first while (sheets)
		
	//END TEMP CODE FOR PROJECT

		// Closing the workbook
		workbook.close();
	}
}