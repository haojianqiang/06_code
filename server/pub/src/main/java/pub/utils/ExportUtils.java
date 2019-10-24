package pub.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import pub.functions.StrFuncs;
import pub.models.listview.ListViewModel;
import pub.models.listview.MapBasedListViewModel;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExportUtils {
    public static int ROW_LIMIT;

    public static File saveToExcel(ListViewModel model, String name, String fileFolder) {
        Workbook workbook = null;
        OutputStream ouputStream = null;
        File excel = null;
        String attachmentName = name + ".xls";
        try {
            workbook = createWorkbook(model, name);
            excel = new File(fileFolder, attachmentName);
            ouputStream = new FileOutputStream(excel);
            workbook.write(ouputStream);
            return excel;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
//            if(excel.exists()) {
//                excel.delete();
//            }
            try {
                if (ouputStream != null) {
                    ouputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void exportToExcelMoreSheet(HttpServletRequest request, HttpServletResponse response,
                                              Map<String,MapBasedListViewModel> map, String name) {
        try {
            Workbook workbook = createMoreSheetWorkbook(map);
            String attachmentName = name + ".xls";
            String ua = request.getHeader("User-Agent");
            boolean isChrome = (ua != null && ua.contains("Chrome"));

            try {
                if (isChrome) {
                    attachmentName = java.net.URLEncoder.encode(attachmentName, "UTF-8");
                }
                else {
                    attachmentName = new String(attachmentName.getBytes("GB2312"), "iso8859-1");
                }
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            response.setHeader("Content-disposition", "attachment; filename=" + attachmentName);
            response.setContentType("application/msexcel");
            ServletOutputStream os = response.getOutputStream();
            workbook.write(os);
            os.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void exportToExcel(HttpServletRequest request,
                                     HttpServletResponse response,
                                     ListViewModel model, String name) {
        try {
            Workbook workbook = createWorkbook(model, name);

            String attachmentName = name + ".xls";

            String ua = request.getHeader("User-Agent");
            boolean isChrome = (ua != null && ua.contains("Chrome"));

            try {
                if (isChrome) {
                    attachmentName = java.net.URLEncoder.encode(attachmentName, "UTF-8");
                }
                else {
                    attachmentName = new String(attachmentName.getBytes("GB2312"), "iso8859-1");
                }
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            response.setHeader("Content-disposition", "attachment; filename=" + attachmentName);
//            response.setContentType("application/msexcel");
            response.setContentType("application/json");
            ServletOutputStream os = response.getOutputStream();
            workbook.write(os);
            os.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static Workbook createMoreSheetWorkbook(Map<String, MapBasedListViewModel> map) {
        Workbook workbook = new HSSFWorkbook();
        for(String key : map.keySet()){
            String sheetName = key;
            Sheet sheet = null;
            ListViewModel model = map.get(key);
            int a = model.getRowCount();
            if (a > 65535) {
                int length = a / 65535;
                for (int i = 0; i <= length; i++) {
                    if (sheet != null) {
                        sheet = null;
                    }
                    sheet = createSheet(workbook,sheetName+1);
                    int colCount = model.getColCount();
                    List<Object> rowvalue=new ArrayList<Object>();
                    for (int c = 0; c < colCount; c++) {
                        rowvalue.add(model.getColTitle(c));
                    }
                    createRow(sheet,getcellStyle(workbook, 1),rowvalue,0);//创建第一行
                    if ((i + 1) * 65535 < a) {
                        for (int r = i * 65535; r < (i + 1) * 65535; r++) {
                            for (int c = 0; c < colCount; c++) {
                                Object value = model.getCellValue(r, c);
                                String text = StrFuncs.valueOf(value);
                                rowvalue.add(text);
                            }
                            createRow(sheet,getcellStyle(workbook, 2),rowvalue,r + 1);//创建第一行
                        }
                    }
                    else {
                        for (int r = i * 65535; r < a; r++) {
                            int j = r - i * 65535;
                            for (int c = 0; c < colCount; c++) {
                                Object value = model.getCellValue(r, c);
                                String text = StrFuncs.valueOf(value);
                                rowvalue.add(text);
                            }
                            createRow(sheet,getcellStyle(workbook, 2),rowvalue,j + 1);//创建第一行
                        }
                    }
                }
            }
            else {
                sheet = createSheet(workbook,sheetName);
                int colCount = model.getColCount();
                List<Object> rowvalue=new ArrayList<Object>();
                for (int c = 0; c < colCount; c++) {
                    rowvalue.add(model.getColTitle(c));
                }
                CellStyle headStyle = getcellStyle(workbook, 1);
                createRow(sheet,headStyle,rowvalue,0);//创建第一行

                CellStyle style = getcellStyle(workbook, 2);
                for (int r = 0; r < model.getRowCount(); r++) {
                    for (int c = 0; c < colCount; c++) {
                        Object value = model.getCellValue(r, c);
                        String text = StrFuncs.valueOf(value);
                        rowvalue.add(text);
                    }
                    createRow(sheet,style,rowvalue,r + 1);//创建第一行
                }
            }
        }
        return workbook;
    }

    private static Workbook createWorkbook(ListViewModel model, String name) throws Exception {
        String sheetName = name;
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = null;
        int a = model.getRowCount();
        if (a > 65535) {
            int length = a / 65535;
            for (int i = 0; i <= length; i++) {
                if (sheet != null) {
                    sheet = null;
                }
                sheet = createSheet(workbook,sheetName+1);
                sheet.setDefaultColumnWidth(16);
                int colCount = model.getColCount();
                List<Object> rowvalue=new ArrayList<Object>();
                for (int c = 0; c < colCount; c++) {
                    rowvalue.add(model.getColTitle(c));
                }
                createRow(sheet,getcellStyle(workbook, 1),rowvalue,0);//创建第一行
                if ((i + 1) * 65535 < a) {
                    for (int r = i * 65535; r < (i + 1) * 65535; r++) {
                        for (int c = 0; c < colCount; c++) {
                            Object value = model.getCellValue(r, c);
                            String text = StrFuncs.valueOf(value);
                            rowvalue.add(text);
                        }
                        createRow(sheet,getcellStyle(workbook, 2),rowvalue,r + 1);//创建第一行
                    }
                }
                else {
                    for (int r = i * 65535; r < a; r++) {
                        int j = r - i * 65535;
                        for (int c = 0; c < colCount; c++) {
                            Object value = model.getCellValue(r, c);
                            String text = StrFuncs.valueOf(value);
                            rowvalue.add(text);
                        }
                        createRow(sheet,getcellStyle(workbook, 2),rowvalue,j + 1);//创建第一行
                    }
                }
            }
        }
        else {
            sheet = createSheet(workbook,sheetName);
            sheet.setDefaultColumnWidth(16);
            int colCount = model.getColCount();
            List<Object> rowvalue=new ArrayList<Object>();
            for (int c = 0; c < colCount; c++) {
                rowvalue.add(model.getColTitle(c));
            }
            CellStyle headStyle = getcellStyle(workbook, 1);
            createRow(sheet,headStyle,rowvalue,0);//创建第一行

            CellStyle style = getcellStyle(workbook, 2);
            for (int r = 0; r < model.getRowCount(); r++) {
                for (int c = 0; c < colCount; c++) {
                    Object value = model.getCellValue(r, c);
                    String text = StrFuncs.valueOf(value);
                    rowvalue.add(text);
                }
                createRow(sheet,style,rowvalue,r + 1);//创建第一行
            }
        }
        return workbook;
    }

    /**
     * 跨列
     * 设定合并单元格区域范围
     */
    protected static Sheet createSheet(Workbook wb,String name) {
        Sheet sheet = wb.createSheet(name);
        sheet.setColumnWidth(0, columWidth() * 256);
        sheet.setDefaultColumnWidth(columWidth());
        return sheet;
    }

    protected static int columWidth() {
        return 40;
    }

    /**
     * 创建行
     * @param sheet
     * @param style
     * @param value
     * @param n
     */
    private static void createRow(Sheet sheet, CellStyle style, List<Object> value, int n){
        Row row=sheet.createRow(n);
        //创建列 单元格
        for (int i = 0; i < value.size(); i++) {
            Cell cell= row.createCell(i);
            cell.setCellStyle(style);
            if(judge(value.get(i))){
                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue(Integer.valueOf(String.valueOf(value.get(i))));
            }else{
                cell.setCellValue(String.valueOf(value.get(i)));
            }
        }
        value.clear();//清空集合
    }


    /**
     * 设置对应样式
     * @param wb
     * @param type
     * @return
     */
    private static CellStyle getcellStyle(Workbook wb, int type){
        CellStyle cellStyle=wb.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框
        cellStyle.setAlignment(HorizontalAlignment.CENTER);// 居中
        Font font = wb.createFont();
        // 宋体加粗
        font.setFontName("宋体");
        switch (type) {
            case 1: //创建标题栏样式
                font.setFontHeightInPoints((short) 12);
                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                cellStyle.setFont(font);
                break;
            case 2://第二行
                // 宋体加粗
                font.setFontHeightInPoints((short) 11);
                cellStyle.setFont(font);
                break;
            default:
                break;
        }
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 判断数据类型
     * @param obj
     * @return
     */
    private static boolean judge(Object obj){
        if (obj instanceof Integer) {
            return true;
        } else if (obj instanceof Double ){
            return true;
        } else if (obj instanceof Float) {
            return true;
        }else if(obj instanceof String){
            return false;
        }else{
            return false;
        }
    }
}
