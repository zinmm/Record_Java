package com.zin.record.utils.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zin.record.utils.excel.pojo.ExcelPoJo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelUtil {

    private String filePath;
    private Map<String, ExcelPoJo> excelMap;

    public ExcelUtil(String filePath, Map<String, ExcelPoJo> excelMap) {
        this.filePath = filePath;
        this.excelMap = excelMap;
    }

    /*
     * 导出数据
     */
    public void export() throws Exception {

        try {

            HSSFWorkbook workbook = new HSSFWorkbook();

            for (String pageName :
                    excelMap.keySet()) {

                ExcelPoJo excelPoJo = excelMap.get(pageName);
                String title = excelPoJo.getTitle();
                String[] rowName = excelPoJo.getRowName();
                List<Object[]> dataList = excelPoJo.getDataList();

                // 创建工作簿对象
                HSSFSheet sheet = workbook.createSheet(pageName);

                // sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面 - 可扩展】
                HSSFCellStyle columnTopStyle = getColumnTopStyle(workbook);// 获取列头样式对象
                HSSFCellStyle style = getStyle(workbook); // 单元格样式对象

                // 产生表格标题行, 2 行合并 创建所需行时叠加
                boolean isTitle = !(title == null || title.length() == 0);
                if (isTitle) {
                    HSSFRow hssfRow = sheet.createRow(0);
                    HSSFCell hssfCell = hssfRow.createCell(0);
                    sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowName.length - 1)));
                    hssfCell.setCellStyle(columnTopStyle);
                    hssfCell.setCellValue(title);
                }

                // 定义所需列数
                int columnNum = rowName.length;
                HSSFRow rowRowName = sheet.createRow(isTitle ? 2 : 0); // 在索引2的位置创建行(最顶端的行开始的第二行)

                // 将列头设置到sheet的单元格中
                for (int n = 0; n < columnNum; n++) {
                    HSSFCell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
                    cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
                    HSSFRichTextString text = new HSSFRichTextString(rowName[n]);
                    cellRowName.setCellValue(text); // 设置列头单元格的值
                    cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
                }

                // 将查询出的数据设置到sheet对应的单元格中
                for (int i = 0; i < dataList.size(); i++) {

                    Object[] obj = dataList.get(i);// 遍历每个对象
                    HSSFRow row = sheet.createRow(i + (isTitle ? 3 : 1));// 创建所需的行数

                    for (int j = 0; j < obj.length; j++) {
                        HSSFCell cell = null; // 设置单元格的数据类型
                        cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                        if (!"".equals(obj[j]) && obj[j] != null) {
                            if (obj[j] instanceof Integer) {
                                cell.setCellValue((Integer) obj[j]); // 设置单元格的值
                            } else if (obj[j] instanceof Double) {
                                cell.setCellValue((Double) obj[j]); // 设置单元格的值
                            } else if (obj[j] instanceof String) {
                                cell.setCellValue(obj[j].toString()); // 设置单元格的值
                            } else if (obj[j] instanceof Date) {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                                cell.setCellValue(formatter.format(obj[j]));
                            }
                        } else {
                            cell.setCellValue(""); // 设置单元格的值
                        }
                        cell.setCellStyle(style); // 设置单元格样式
                    }
                }
                // 让列宽随着导出的列长自动适应
                for (int colNum = 0; colNum < columnNum; colNum++) {
                    int columnWidth = sheet.getColumnWidth(colNum) / 256;
                    for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                        HSSFRow currentRow;
                        // 当前行未被使用过
                        if (sheet.getRow(rowNum) == null) {
                            currentRow = sheet.createRow(rowNum);
                        } else {
                            currentRow = sheet.getRow(rowNum);
                        }
                        if (currentRow.getCell(colNum) != null) {
                            HSSFCell currentCell = currentRow.getCell(colNum);
                            if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                                int length = currentCell.getStringCellValue().getBytes().length;
                                if (columnWidth < length) {
                                    columnWidth = length;
                                }
                            }
                        }
                    }
                    if (colNum == 0) {
                        sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
                    } else {
                        sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
                    }
                }
            }

            try {

                if (filePath.contains(".World") || filePath.contains(".c")) {
                    filePath = filePath.substring(0, filePath.lastIndexOf("."));
                    filePath = filePath + ".xls";
                }

                int strLastIndex = filePath.lastIndexOf(".");
                filePath = new StringBuffer(filePath).insert(strLastIndex, "_" + String.valueOf(System.currentTimeMillis()).substring(4, 13)).toString();
//                filePath = new String(filePath.getBytes("GBK"), "iso8859-1");

                File file = new File(filePath);
                if (!file.exists()) {
                    int lastIndex = filePath.lastIndexOf("/");
                    new File(filePath.substring(0, lastIndex)).mkdirs();
                }
                //输出到磁盘中
                FileOutputStream fos = new FileOutputStream(filePath);
                workbook.write(fos);
                workbook.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * 列头单元格样式
     */
    public static HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 字体加粗
        font.setBold(true);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;

    }

    /*
     * 列数据信息单元格样式
     */
    public static HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        // font.setFontHeightInPoints((short)10);
        // 字体加粗
        // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;

    }
}
