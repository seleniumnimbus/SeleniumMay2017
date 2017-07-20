package DriverScript;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XPathDescription {
	
	public FileInputStream fs = null;
	public String path;
	private XSSFWorkbook workBook = null;
	private XSSFSheet sheet = null;
	private XSSFRow row = null;
	private XSSFCell cell = null;
	//Create Constructor
	public XPathDescription() throws IOException{
		path = System.getProperty("user.dir")+"\\TestData\\XpathDetails.xlsx";
		fs = new FileInputStream(path);
		workBook = new XSSFWorkbook(fs);
		sheet = workBook.getSheetAt(0);
	}
	//get row count
	public int getRowCount(String sheetName){
		sheet = workBook.getSheet(sheetName);
		return(sheet.getLastRowNum() - sheet.getFirstRowNum());
	}
	//get row index
	public int getRowIndex(String sheetName,String rowData){
		int rowCount = getRowCount(sheetName);
		int rowIndex = -1;
		for(int i = 0;i<rowCount+1;i++){
			row = sheet.getRow(i);
			cell = row.getCell(0);
			if(cell.getStringCellValue().equals(rowData)){
				rowIndex = cell.getRowIndex();
			}
		}
		return rowIndex;
	}
	//get value
	public String getValue(String sheetName,String objectName){
		int rowNumber_VariableName = getRowIndex(sheetName,objectName);
		row = sheet.getRow(rowNumber_VariableName);
		cell = row.getCell(1);
		return(cell.getStringCellValue());
	}

}
