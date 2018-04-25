package pkgExcel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class ExcelReader {
	public static final String SAMPLE_XLSX_FILE_PATH = "./StatData.xlsx";


	public static void main(String[] args) throws IOException, InvalidFormatException {

		// Creating a Workbook from an Excel file (.xls or .xlsx)
		System.out.println(new File(".").getCanonicalPath());
		Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));


		//Out file for printer writer
		PrintWriter out = new PrintWriter("LoadStatementsSpring2018.txt");


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
		String stat_position = "";
		ArrayList<String> statNames = new ArrayList<String>();//INITIALIZE NAMES 
		ArrayList<String> statAbbrs = new ArrayList<String>();//stores abbrs
		ArrayList<String> statCastAsValues = new ArrayList<String>();
		ArrayList<Stat> playerStats = new ArrayList<Stat>();  // to store each stat for a row
		int stat_start = 0; // first column in sheet per row that contains a stat.
		int stat_final = 0; //final column in sheet per row that contains stat values to store
		//position info to store
		int positionIndex = 0;
		ArrayList<String> positionsFound = new ArrayList<String>();
		//season info to store
		String season_name = "";
		String season_start_date = "";
		String season_end_date = "";
		//sheet info
		int sheetCounter = 0;
		//sequence info
		String player_seq_init = "CREATE SEQUENCE PLAYER_SEQ INCREMENT BY 1 START WITH 0 MAXVALUE 999999999999 MINVALUE 0;";
		String stat_seq_init = "CREATE SEQUENCE STAT_SEQ INCREMENT BY 1 START WITH 0 MAXVALUE 999999999999 MINVALUE 0;";
		String position_seq_init = "CREATE SEQUENCE POSITION_SEQ INCREMENT BY 1 START WITH 0 MAXVALUE 999999999999 MINVALUE 0;";
		String season_seq_init = "CREATE SEQUENCE SEASON_SEQ INCREMENT BY 1 START WITH 0 MAXVALUE 999999999999 MINVALUE 0;";
		String player_seq = "PLAYER_SEQ.nextval";
		String stat_seq = "STAT_SEQ.nextval";
		String position_seq = "POSITION_SEQ.nextval";
		String season_seq = "SEASON_SEQ.nextval";
		int playerSeqCounter = 0;
		int statSeqCounter = 0;
		int positionSeqCounter = 0;
		int seasonSeqCounter = 0;

		//PRINT SQL SEQUENCE INIT
		System.out.println(season_seq_init);
		System.out.println(player_seq_init);
		System.out.println(stat_seq_init);
		System.out.println(position_seq_init);

		while (sheetIterator.hasNext()) {
			if(sheetCounter == 1) {
				String seasonPrint = "\ninsert into season \n(season_id,start_date,end_date,seasonname) \nvalues \n"
						+ "(" + season_seq + "," + season_start_date + "," + season_end_date + "," + "'" + "Season " + season_name + "');";
				System.out.println(seasonPrint);
				out.println(seasonPrint);
				seasonSeqCounter++;
			}
			sheet = sheetIterator.next(); //obtain next sheet
			// ITERATING ROWS: internal while loop to iterate over individual rows in a sheet
			rowIterator = sheet.rowIterator();
			rowCounter = 0;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next(); //obtain next row

				// ITERATING CELLS: 3rd sub while loop iterating over each cell in a row
				cellIterator = row.cellIterator();
				columnCounter = 0;
				while (cellIterator.hasNext()) {
					cell = cellIterator.next(); // obtain next cell
					cellValue = dataFormatter.formatCellValue(cell);
					if(sheetCounter == 0) {//Only enters when on season sheet
						if(rowCounter == 1) {
							switch(columnCounter) {
							case 0:
								season_name = cellValue;
								break;
							case 1:
								season_start_date = cellValue;
								break;
							case 2:
								season_end_date = cellValue;
								break;
							}//end switch
						}else{//end if rowCounter == 1

						}
					}else{//end if sheetCounter == 0
						//PRINT FOR STATS
						if(rowCounter == 3 && columnCounter == 0) {
							for(int i = 0; i < statNames.size(); i++) {
								String statPrint = "\ninsert into stat \n(stat_id,stat_name,stat_abbr,cast_as) \nvalues \n"
										+ "(" + stat_seq + ",'" + statNames.get(i) + "','" + statAbbrs.get(i) + "'," + statCastAsValues.get(i) + ");";
								System.out.println(statPrint);
								out.println(statPrint);
							}
						}
						//END PRINT FOR STATS
						//NOW ON SHEET 1 AND 2
						if(rowCounter == 0) {
							if(columnCounter == 0) {
								stat_start = Integer.valueOf(cellValue);
								positionIndex = stat_start - 1;
							}else if(columnCounter == 1) {
								stat_final = Integer.valueOf(cellValue);
							}else if(columnCounter >= stat_start) {
								statCastAsValues.add(cellValue);
							}
						}else if (rowCounter == 1 && columnCounter >= stat_start && columnCounter <= stat_final) {
							statNames.add(cellValue);
						}else if (rowCounter == 2 && columnCounter >= stat_start && columnCounter <= stat_final) {
							statAbbrs.add(cellValue);
						}else if (rowCounter >= 3){//now we have entered data rows
							if(columnCounter == 0) {
								first_name = cellValue;
							}else if(columnCounter == 1) {
								last_name = cellValue;
							}else if(columnCounter == 2) {
								GUID = cellValue;
							}else {
								if(columnCounter == 3) {
									//PRINTING PLAYER AND POSITION AND SEASON
									String playerPrint = "\ninsert into player \n(player_id,player_guid,first_name,last_name) \nvalues \n"
											+ "(" + player_seq + "," + GUID + ",'" + first_name + "','" + last_name + "');";
									System.out.println(playerPrint);
									out.println(playerPrint);
									stat_position = cellValue;
									if(sheetCounter == 1 && !positionsFound.contains(cellValue)) {//only for pitchers
										String positionPrint = "\ninsert into position \n(pos_id,pos_num,pos_name,postypeid) \nvalues \n"
												+ "(" + position_seq + "," + 1 + ",'" + cellValue + "'," + 2 + ");";
										System.out.println(positionPrint);
										out.println(positionPrint);
										positionsFound.add(cellValue);
									}else if(sheetCounter == 2 && !positionsFound.contains(cellValue)) {
										int positionNum = 0;
										switch(cellValue) {
										case "C":
											positionNum = 2;
											break;
										case "1B":
											positionNum = 3;
											break;
										case "2B":
											positionNum = 4;
											break;
										case "3B":
											positionNum = 5;
											break;
										case "SS":
											positionNum = 6;
											break;
										case "RF":
											positionNum = 9;
											break;
										case "CF":
											positionNum = 8;
											break;
										case "LF":
											positionNum = 7;
											break;
										}//end switch
										String positionPrint = "\ninsert into position \n(pos_id,pos_num,pos_name,postypeid) \nvalues \n"
												+ "(" + position_seq + "," + positionNum + ",'" + cellValue + "'," + 1 + ");";
										System.out.println(positionPrint);
										out.println(positionPrint);
										positionsFound.add(cellValue);
									}
									//printing seasonPlayer
									String sPPrint = "\ninsert into SeasonPlayer \n(season_id,player_id) \nvalues \n"
											+ "(" + 0 + "," + playerSeqCounter + ");";
									System.out.println(sPPrint);
									out.println(sPPrint);
									//printing sPP

									for(int i = 0; i < positionsFound.size(); i++){
										if(positionsFound.get(i)==cellValue) {
											positionSeqCounter = i;
										}
									}

									String sPPPrint = "\ninsert into SeasonPlayerPosition \n(season_id,player_id,posid) \nvalues \n"
											+ "(" + 0 + "," + playerSeqCounter + "," + positionSeqCounter + ");";
									System.out.println(sPPPrint);
									out.println(sPPPrint);
									//increment after all prints done for a row

								}
							}
						}
					}
					if(columnCounter == stat_start-1 && rowCounter>2) {
						for(int i = 0; i < positionsFound.size(); i++){
							if(positionsFound.get(i)==cellValue) {
								positionSeqCounter = i;
							}
						}
					}
					if(columnCounter >= stat_start && rowCounter>2) {
						String sPPSPrint = "\ninsert into SeasonPlayerPositionStat \n(season_id,player_id,posid,stat_id,stat_value) \nvalues \n"
								+ "(" + 0 + "," + playerSeqCounter + "," + positionSeqCounter + "," + (columnCounter - stat_start) + "," + cellValue + ");";
						System.out.println(sPPSPrint);
						out.println(sPPSPrint);
					}
					columnCounter++;
				}
				// print SQL FOR STAT INSERTS

				//PRINT SQL FOR PLAYER INSERTS

				//PRINT
				if(rowCounter > 2) {
					playerSeqCounter++;
				}
				rowCounter++;
			}
			sheetCounter++;
		}

		/*

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
		 * 
		 */

		// Closing the workbook
		workbook.close();

		//close printer writer
		out.close();
	}
}