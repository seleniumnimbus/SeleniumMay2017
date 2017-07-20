package DriverScript;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TestDataReadWrite {

	private FileInputStream fs = null;
	public FileOutputStream fos = null;
	private String path;
	private XSSFWorkbook workBook = null;
	private XSSFSheet sheet = null;
	private XSSFRow row = null;
	private XSSFRow rowData = null;
	private XSSFCell cell = null;
	private XSSFCell cellData = null;
	private String testCaseID = null;
	private String scenariosSheetName = null;
	private HashMap<String,Object> hash;

	public TestDataReadWrite() throws IOException{
		path = System.getProperty("user.dir")+"\\TestData\\Automation_Input_Data_Sheet.xlsx";
		fs = new FileInputStream(path);
		workBook = new XSSFWorkbook(fs);
		sheet = workBook.getSheet("TestCaseDetails");
	}
	//get row count
	private int getRowCount(String sheetName){
		sheet = workBook.getSheet(sheetName);
		return(sheet.getLastRowNum() - sheet.getFirstRowNum());
	}
	//get column count
	private int getColumnCount(String sheetName){
		sheet = workBook.getSheet(sheetName);
		row = sheet.getRow(0);
		return (row.getLastCellNum());
	}
	//get column index
	private int getColumnIndex(String sheetName,String colName){
		int colCount = getColumnCount(sheetName);
		int cellIndex = -1;
		row = sheet.getRow(0);
		for(int i = 0 ; i<colCount+1;i++){
			cell = row.getCell(i);
			if(cell.getStringCellValue().equals(colName)){
				cellIndex = cell.getColumnIndex();
				break;
			}
		}
		return cellIndex;
	}
	//get row index
	private int getRowIndex(String sheetName,int colNumber,String rowData){
		int rowCount = getRowCount(sheetName);
		int rowIndex = -1;
		for(int i = 0;i<rowCount+1;i++){
			row = sheet.getRow(i);
			cell = row.getCell(colNumber);
			if(cell.getStringCellValue().equals(rowData)){
				rowIndex = cell.getRowIndex();
			}
		}
		return rowIndex;
	}
	//get Scenarios sheet name
	private void getScenariosSheetName(){
		int colNumber_TestCaseID = getColumnIndex("TestCaseDetails", "TestCaseID");
		int rowNumber_TestCaseID = getRowIndex("TestCaseDetails", colNumber_TestCaseID, testCaseID);
		int colScenariosSheetName = getColumnIndex("TestCaseDetails", "Scenarios_SheetName");
		row = sheet.getRow(rowNumber_TestCaseID);
		cell = row.getCell(colScenariosSheetName);
		scenariosSheetName = cell.getStringCellValue();
	}
	//store Specific Row Data To HashMap
	public void storeSpecificRowDataToHashMap(String getTestCaseID){
		testCaseID = getTestCaseID;
		getScenariosSheetName();
		int colNumber_TestCaseID = getColumnIndex(scenariosSheetName, "TestCaseID");
		int rowNumber_TestCaseID = getRowIndex(scenariosSheetName, colNumber_TestCaseID, testCaseID);
		int colNumber_ScenarioSheet = getColumnCount(scenariosSheetName);
		//Create Hash Map
		hash = new HashMap<String, Object>();
		row = sheet.getRow(0);
		rowData = sheet.getRow(rowNumber_TestCaseID);
		Iterator<Cell> cellItr = row.cellIterator();
		Iterator<Cell> cellDataItr = rowData.cellIterator();
		while(cellDataItr.hasNext()){
			cell = (XSSFCell) cellItr.next();
			cellData = (XSSFCell) cellDataItr.next();
			if(!cell.toString().equals("")){
				hash.put(cell.toString(), cellData.toString());
			}	
		}
	}
	//get value
	public Object getValue(String colName){
		return(hash.get(colName));
	}
	//update value
	public void updateValue(String colName, String value){
		int colNumber_TestCaseID = getColumnIndex(scenariosSheetName, "TestCaseID");
		int rowNumber_TestCaseID = getRowIndex(scenariosSheetName, colNumber_TestCaseID, testCaseID);
		int colNumber_SpecificColName = getColumnIndex(scenariosSheetName, colName);
		row = sheet.getRow(rowNumber_TestCaseID);
		cell = row.createCell(colNumber_SpecificColName);
		cell.setCellValue(value);
		try {
			fos = new FileOutputStream(path);
			try {
				workBook.write(fos);
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		storeSpecificRowDataToHashMap(testCaseID);
	}


}
