package com.bootx.util;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel 工具类
 */
public class ExcelUtils {

    public static void main(String[] args) throws Exception{
        Map<String,String> map = new HashMap<>();
        map.put("姓名","name");
        map.put("简介","introduction");
        map.put("讲师等级","teacherRankName");
        System.out.println(parseExcel("C:\\Users\\11697\\Desktop\\excel\\1.xlsx",map));
    }


    public static Map<String,List<Map<String,Object>>> parseExcel(String path,Map<String,String> map) throws Exception{
        Map<String,List<Map<String,Object>>> excelResult = new HashMap<>();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new File(path));
        List<XSSFSheet> xssfSheets = getXSSFSheets(xssfWorkbook);

        /**
         * 第一行读取excel的表头
         */
        for (XSSFSheet xssfSheet:xssfSheets) {
            List<String> sheetTitles = getSheetTitle(xssfSheet);
            List<Map<String,Object>> data = new ArrayList<>();
            int rowNum = xssfSheet.getLastRowNum();
            for (int i=1;i<=rowNum;i++) {
                Map<String,Object> result = new HashMap<>();
                XSSFRow xssfRow = xssfSheet.getRow(i);
                short cellNum = xssfRow.getLastCellNum();
                for (short j=0;j<cellNum;j++){
                    XSSFCell xssfCell = xssfRow.getCell(j);
                    if(xssfCell.getCellType()==CellType.BOOLEAN){
                        result.put(sheetTitles.get(j),xssfCell.getBooleanCellValue());
                    }else if(xssfCell.getCellType()==CellType._NONE){

                    }else if(xssfCell.getCellType()==CellType.BLANK){
                        result.put(sheetTitles.get(j),null);
                    }else if(xssfCell.getCellType()==CellType.ERROR){
                        result.put(sheetTitles.get(j),null);
                    }else if(xssfCell.getCellType()==CellType.FORMULA){
                        result.put(sheetTitles.get(j),null);
                    }else if(xssfCell.getCellType()==CellType.NUMERIC){
                        result.put(sheetTitles.get(j),xssfCell.getNumericCellValue());
                    }else if(xssfCell.getCellType()==CellType.STRING){
                        result.put(sheetTitles.get(j),xssfCell.getStringCellValue());
                    }
                }
                data.add(result);
            }

            //解析完数据之后，需要映射到map里面
            List<Map<String,Object>> data1 = new ArrayList<>();
            for (Map item:data) {
                Map<String,Object> newItem = new HashMap<>();
                for (String key:sheetTitles) {
                    if(map.keySet().contains(key)){
                        newItem.put(map.get(key),item.get(key));
                    }
                }
                data1.add(newItem);
            }
            excelResult.put(xssfSheet.getSheetName(),data1);
        }

        return excelResult;
    }

    private static List<XSSFSheet> getXSSFSheets(XSSFWorkbook xssfWorkbook){
        List<XSSFSheet> xssfSheets = new ArrayList<>();
        int sheetsNumber = xssfWorkbook.getNumberOfSheets();
        for (int i=0;i<sheetsNumber;i++) {
            xssfSheets.add(xssfWorkbook.getSheetAt(i));
        }
        return xssfSheets;
    }

    private static List<String> getSheetTitle(XSSFSheet xssfSheet){
        List<String> sheetTitles = new ArrayList<>();
        XSSFRow xssfRow = xssfSheet.getRow(0);
        short cellNum = xssfRow.getLastCellNum();
        for (short j=0;j<cellNum;j++) {
            sheetTitles.add(xssfRow.getCell(j).getStringCellValue());
        }
        return sheetTitles;
    }



}
