package com.ciicsh.gto.salarymanagementcommandservice.service.util.excel;


import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangtianning on 2017/11/21.
 */
public class ExcelUtil {

    static XSSFWorkbook workbook = null;

    /**
     * 判断文件是否存在.
     * @param fileDir  文件路径
     * @return
     */
    public static boolean fileExist(String fileDir){
        boolean flag = false;
        File file = new File(fileDir);
        flag = file.exists();
        return flag;
    }

    /**
     * 判断文件的sheet是否存在.
     * @param fileDir   文件路径
     * @param sheetName  表格索引名
     * @return
     */
    public static boolean sheetExist(String fileDir,String sheetName){
        boolean flag = false;
        File file = new File(fileDir);
        if(file.exists()){    //文件存在
            //创建workbook
            try {
                workbook = new XSSFWorkbook(new FileInputStream(file));
                //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
                XSSFSheet sheet = workbook.getSheet(sheetName);
                if(sheet!=null)
                    flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{    //文件不存在
            flag = false;
        }

        return flag;
    }

    /**
     * 读取excel表中的数据. 
     *
     * @param fileDir    文件路径    
     * @param sheetName 表格索引(EXCEL 是多表文档,所以需要输入表索引号，如sheet1) 
     * @param object   object 
     */
    public static List readFromExcel(String fileDir,String sheetName, Object object) {
        //创建workbook  
        File file = new File(fileDir);
        try {
            workbook = new XSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        List result = new ArrayList();
        // 获取该对象的class对象  
        Class class_ = object.getClass();
        // 获得该类的所有属性  
        Field[] fields = class_.getDeclaredFields();

        // 读取excel数据  
        XSSFSheet sheet = workbook.getSheet(sheetName);
        // 获取表格的总行数  
        int rowCount = sheet.getLastRowNum() + 1; // 需要加一  
        System.out.println("rowCount:"+rowCount);
        if (rowCount < 1) {
            return result;
        }

        // 获取表头的列数
        int colCount = sheet.getRow(0).getLastCellNum();
        // 读取表头信息,确定需要用的方法名---set方法  
        // 用于存储方法名  
        String[] methodNames = new String[colCount];
        // 用于存储属性类型  
        String[] fieldTypes = new String[colCount];
        // 获得表头行对象  
        XSSFRow titleRow = sheet.getRow(0);
        
        // 遍历  
        for (int colIndex = 0; colIndex < colCount; colIndex++) {
            String data = titleRow.getCell(colIndex).toString();
            String capitalizedData = Character.toUpperCase(data.charAt(0))
                    + data.substring(1, data.length());
            methodNames[colIndex] = "set" + capitalizedData;
            for (int i = 0; i < fields.length; i++) {
                if (data.equals(fields[i].getName())) {
                    fieldTypes[colIndex] = fields[i].getType().getName();
                }
            }
        }
        
        // 逐行读取数据 从1开始 忽略表头  
        for (int rowIndex = 1; rowIndex < rowCount; rowIndex++) {
            // 获得行对象  
            XSSFRow row = sheet.getRow(rowIndex);
            if (row != null) {
                Object obj = null;
                // 实例化该泛型类的对象一个对象  
                try {
                    obj = class_.newInstance();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                // 获得本行中各单元格中的数据  
                for (int colIndex = 0; colIndex < colCount; colIndex++) {
                    String data = row.getCell(colIndex).toString();
                    // 获取要调用方法的方法名  
                    String methodName = methodNames[colIndex];
                    Method method = null;
                    try {
                        // 这部分可自己扩展  
                        if (fieldTypes[colIndex].equals("java.lang.String")) {
                            method = class_.getDeclaredMethod(methodName,
                                    String.class); // 设置要执行的方法--set方法参数为String  
                            method.invoke(obj, data); // 执行该方法  
                        } else if (fieldTypes[colIndex].equals("int")) {
                            method = class_.getDeclaredMethod(methodName,
                                    int.class); // 设置要执行的方法--set方法参数为int  
                            double data_double = Double.parseDouble(data);
                            int data_int = (int) data_double;
                            method.invoke(obj, data_int); // 执行该方法  
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                result.add(obj);
            }
        }
        return result;
    }
}
