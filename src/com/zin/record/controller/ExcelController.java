package com.zin.record.controller;

import com.zin.record.dto.ADMaster;
import com.zin.record.dto.SSPADDto;
import com.zin.record.utils.excel.ExcelUtil;
import com.zin.record.utils.excel.pojo.ExcelPoJo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Double.NaN;

/**
 * Created by zhujinming on 2018/7/9.
 */
public class ExcelController {

    private final static String EXCEL_BASE_PATH = "/Users/zhujinming/Desktop/ssp/";
    private final static String EXCEL_PATH = EXCEL_BASE_PATH + "Excel/";
    private final static String EXCEL_SUFFIX = ".xls";
    private final static String EXCEL_NAME = "SDK AD 统计表";

    private static String[] titles = {
            "    日期    ", "媒体位", "操作系统", "广告主",
            "曝光(广告主)", "点击(广告主)", "CPM 收入（广告主）",
            "CPC 收入（广告主）", "收入(广告主)", "分成后收入",
            "广告位", "日活", "请求数", "曝光数", "点击数",
            "点击率", "填充率", "曝光差异", "点击差异"
    };

    private static String[] sspKeywords = {
            "日期", "广告主", "广告位", "媒体位", "操作系统",
            "是否为真假量", "请求数", "曝光数", "点击数", "点击率",
            "填充率"
    };

    private static String[] gdtKeywords = {
            "时间", "媒体", "广告展示数",
            "点击量", "预计收入",
            "千次展示收益", "点击率"
    };

    private static Map<String, Map> getSortData(Map<String, List<Object>> sourceData) {
        // key 为媒体 ID 存放已经倒叙排列好的 sspDateListMap 数据
        Map<String, Map> dateMap = new HashMap<>();
        for (String channel :
                sourceData.keySet()) {
            List<Object> sspadDtos = sourceData.get(channel);
            // 降序排列广点通数据
            Map<Integer, Object> sspDateListMap = new TreeMap<>();
            for (int i = 0; i < sspadDtos.size(); i++) {
                Object o = sspadDtos.get(i);
                if (o instanceof SSPADDto) {
                    sspDateListMap.put(i, o);
                }
            }
            dateMap.put(channel, sspDateListMap);
        }
        return dateMap;
    }

    public static Map<String, List<Object>> synthetic(Map<String, List<Object>> sspMap,
                                                      Map<String, List<Object>> adMasterMap) {

        List<String> medias = new ArrayList<>();
        for (String mediaKey : adMasterMap.keySet()) {
            medias.add(mediaKey);
        }

        for (String mediaKey :
                sspMap.keySet()) {
            List<Object> sspadDtos = sspMap.get(mediaKey);

            for (int i = 0; i < sspadDtos.size(); i++) {

                Object object = sspadDtos.get(i);
                if (!(object instanceof SSPADDto)) {
                    continue;
                }

                ADMaster sspAdMaster = ((SSPADDto) object).getAdMaster();
                if (sspAdMaster == null) {
                    continue;
                }

                String name = sspAdMaster.getName();
                if (name == null || name.length() == 0) {
                    continue;
                }

                if (!(name.equals("广点通"))) {
                    continue;
                }

                SSPADDto sspAdDto = (SSPADDto) sspadDtos.get(i);
                String adSeat = sspAdDto.getAdSeat();
                boolean haveMedia = false;
                String adMasterMediaKey = null;
                for (String media : medias) {
                    haveMedia = adSeat.contains(media);
                    if (haveMedia) {
                        adMasterMediaKey = media;
                        break;
                    }
                }

                if (!haveMedia || adMasterMediaKey.length() == 0) {
                    continue;
                }

                Date sspDate = sspAdDto.getDate();
                String sspOs = sspAdDto.getOs();

                List<Object> objects = adMasterMap.get(adMasterMediaKey);
                for (Object adMasterObject : objects) {

                    if (!(adMasterObject instanceof SSPADDto)) {
                        continue;
                    }

                    SSPADDto adMasterAdDto = (SSPADDto) adMasterObject;
                    ADMaster adMaster = adMasterAdDto.getAdMaster();

                    if (adMaster == null) {
                        continue;
                    }

                    Date adMasterDate = adMasterAdDto.getDate();
                    String adMasterOs = adMasterAdDto.getOs();

                    if (adMasterOs == null || adMasterOs.length() == 0 ||
                            sspOs == null || sspOs.length() == 0) {
                        continue;
                    }

                    if (adMasterDate == null ||
                            sspDate == null) {
                        continue;
                    }

                    long adMasterDateL = adMasterDate.getTime();
                    long sspDateL = sspDate.getTime();
                    if (!(adMasterDateL == sspDateL) || !(adMasterOs.equals(sspOs))) {
                        continue;
                    }

                    Double sspClickNumber = Double.valueOf(sspAdDto.getClickNumber());
                    Double sspExposureNumber = Double.valueOf(sspAdDto.getExposureNumber());
                    Double adMasterExposureNumber = Double.valueOf(adMaster.getExposureNumber());
                    Double adMasterClickNumber = Double.valueOf(adMaster.getClickNumber());

                    int clickDifferenceInt = 100;
                    int exposureDifferenceInt = 100;

                    try {
                        clickDifferenceInt = (int) (new BigDecimal(
                                (sspClickNumber - adMasterClickNumber) / sspClickNumber
                        ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
                        
                    } catch (Exception ignored) {}
                    try {
                        exposureDifferenceInt = (int) (new BigDecimal(
                                (sspExposureNumber - adMasterExposureNumber) / sspExposureNumber
                        ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
                    } catch (Exception ignored) {}


                    String clickDifferenceStr = clickDifferenceInt + "%";
                    String exposureDifferenceStr = exposureDifferenceInt + "%";

                    sspAdDto.setClickDifference(clickDifferenceStr);
                    sspAdDto.setExposureDifference(exposureDifferenceStr);
                    sspAdDto.setAdMaster(adMaster);
                    sspMap.get(mediaKey).set(i, sspAdDto);
                }
            }
        }
        return sspMap;
    }

    public static boolean isChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find())
            flg = true;

        return flg;
    }

    public static void main(String[] args) throws Exception {

        Map<String, List<Object>> sspMap = getExcelSSPData(EXCEL_BASE_PATH + "ssp.xls");
        Map<String, List<Object>> gdtMap = getExcelGDTData(EXCEL_BASE_PATH + "gdt.xls");

        // 把广告主的数据合并到 ssp
        if (gdtMap != null) {
            sspMap = synthetic(sspMap, gdtMap);
        }

        // 存放所有最终数据
        Map<String, Map> allData = new HashMap<>();

        allData.put("ssp", getSortData(sspMap));

        if (gdtMap != null) {
            allData.put("gdt", getSortData(gdtMap));
        }

        Map<String, ExcelPoJo> excelMap = new HashMap<>();

        Map sspDataMap = allData.get("ssp");

        for (Object key : sspDataMap.keySet()) {

            ExcelPoJo excelPoJo = new ExcelPoJo();
            List<Object[]> dataList = new ArrayList<>();
            Map mediaMap = (Map) sspDataMap.get(key);
            for (Object dateObject : mediaMap.keySet()) {

                int dateKey = (int) dateObject;
                SSPADDto sspadDtos = (SSPADDto) mediaMap.get(dateKey);
                ADMaster adMaster = sspadDtos.getAdMaster();
                String adMasterClickNumber = adMaster.getClickNumber();
                String adMasterClickWight = adMaster.getClickWight();
                String adMasterCpm = adMaster.getCpm();
                String adMasterExpectIncome = adMaster.getExpectIncome();
                String adMasterExposureNumber = adMaster.getExposureNumber();
                String adMasterName = adMaster.getName();
                String exposureNumber = sspadDtos.getExposureNumber();
                String clickWight = sspadDtos.getClickWight();
                Date date = sspadDtos.getDate();
                String adSeat = sspadDtos.getAdSeat();
                String clickDifference = sspadDtos.getClickDifference();
                String clickNumber = sspadDtos.getClickNumber();
                String exposureDifference = sspadDtos.getExposureDifference();
                String fillWight = sspadDtos.getFillWight();
                String media = sspadDtos.getMedia();
                String os = sspadDtos.getOs();
                String requestNumber = sspadDtos.getRequestNumber();
                String spoilsIncomeBig = sspadDtos.getSpoilsIncome();
                String spoilsIncome = spoilsIncomeBig == null ? "" : spoilsIncomeBig;

                Double cpc = null;
                if (adMasterExpectIncome != null && adMasterClickNumber != null) {
                    cpc = (Double.valueOf(adMasterExpectIncome) / Double.valueOf(adMasterClickNumber));
                    if (cpc.isNaN() || cpc.isInfinite()) {
                        cpc = 0.0;
                    } else {
                        cpc = Double.valueOf(new DecimalFormat(".00").format(cpc));
                    }
                }

//                "日期", "媒体位", "操作系统", "广告主",
//                        "曝光(广告主)", "点击(广告主)", "CPM 收入（广告主）", "CPC 收入（广告主）", "收入(广告主)", "收入后分成", "广告位", "日活",
//                        "请求数", "曝光数", "点击数", "点击率", "填充率", "曝光差异", "点击差异"

                dataList.add(new Object[]{
                        date, media, os, adMasterName,
                        adMasterExposureNumber, adMasterClickNumber,
                        adMasterCpm, cpc, adMasterExpectIncome,
                        spoilsIncome, adSeat, "", requestNumber, exposureNumber,
                        clickNumber, clickWight, fillWight,
                        exposureDifference, clickDifference
                });
            }

//            String newKey = key.toString();
//            String keyLowerCase = newKey.toLowerCase();
//            if (keyLowerCase.contains("test") ||
//                    keyLowerCase.contains("测试") ||
//                    keyLowerCase.contains("插屏") ||
//                    keyLowerCase.contains("原生")) {
//                continue;
//            }

//            String key1 = "";
//            String reg = "[^\u4e00-\u9fa5]";
//            for (int i = 0; i < key.toString().length(); i++) {
//                newKey = String.valueOf(key.toString().charAt(i)).replaceAll(reg, "");
//
//                if (newKey.length() == 0) {
//                    if (key1.length() != 0) {
//                        break;
//                    }
//                    continue;
//                }
//                key1 += newKey;
//                System.out.println(key1);
//            }
//            newKey = key1.replaceAll("安卓", "");
//            newKey = newKey.replaceAll("开屏", "");
//            newKey = newKey.replaceAll("插屏", "");

            excelPoJo.setRowName(titles);
            excelPoJo.setDataList(dataList);
            excelMap.put(/*newKey*/key.toString(), excelPoJo);
        }

        String filePath = EXCEL_PATH + EXCEL_NAME + EXCEL_SUFFIX;
        new ExcelUtil(filePath, excelMap).export();
    }

    private static Map<String, List<Object>> getExcelGDTData(String filePath) {

        File file = new File(filePath);

        if (!file.exists()) {
            return null;
        }

        Map<String, List<Object>> stringListMap = new HashMap<>();
        Map<String, Integer> keyIndexMap = new HashMap<>();

        try {
            String[][] result = getData(file, 0);
            for (String gdtKeyword : gdtKeywords) {
                for (int i = 0; i < result.length; i++) {
                    for (int j = 0; j < result[i].length; j++) {
                        String newKey = result[i][j];
                        if (!isChinese(newKey)) {
                            break;
                        }

                        if (gdtKeyword.equals(newKey) && keyIndexMap.get(gdtKeyword) == null) {
                            keyIndexMap.put(gdtKeyword, j);
                            continue;
                        }
                    }
                }

            }

            for (String[] aResult : result) {

                int space = 0;

                for (int i = 0; i < aResult.length; i++) {
                    String str = aResult[i];
                    if (str.isEmpty() || isChinese(str)) {
                        ++space;
                    }
                }

                if (space > aResult.length - 2) {
                    continue;
                }

                String dateStr = aResult[keyIndexMap.get(gdtKeywords[0])];
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                String media = aResult[keyIndexMap.get(gdtKeywords[1])];
                String exposureNumber = aResult[keyIndexMap.get(gdtKeywords[2])];
                String clickNumber = aResult[keyIndexMap.get(gdtKeywords[3])];
                String expectIncome = aResult[keyIndexMap.get(gdtKeywords[4])];
                String cpm = aResult[keyIndexMap.get(gdtKeywords[5])];
                String clickWight = aResult[keyIndexMap.get(gdtKeywords[6])];

                String os = media;
                if (os.toLowerCase().contains("ios")) {
                    os = "iOS";
                } else if (os.toLowerCase().contains("android")) {
                    os = "Android";
                }

                media = media.substring(0, media.indexOf("("));

                SSPADDto sspadDto = new SSPADDto();
                sspadDto.setDate(date);
                ADMaster adMaster = new ADMaster();
                adMaster.setName("广点通");
                adMaster.setExpectIncome(expectIncome);
                adMaster.setCpm(cpm);
                exposureNumber = exposureNumber.substring(0, exposureNumber.indexOf("."));
                adMaster.setExposureNumber(exposureNumber);
                clickNumber = clickNumber.substring(0, clickNumber.indexOf("."));
                adMaster.setClickNumber(clickNumber);
                adMaster.setClickWight(clickWight);

                sspadDto.setAdMaster(adMaster);
                sspadDto.setMedia(media);
                sspadDto.setOs(os);

                List<Object> sspadDtos = new ArrayList<>();
                if (stringListMap.get(media) != null) {
                    sspadDtos = stringListMap.get(media);
                }
                sspadDtos.add(sspadDto);
                stringListMap.put(media, sspadDtos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringListMap;
    }

    private static Map<String, List<Object>> getExcelSSPData(String filePath) {

        Map<String, List<Object>> stringListMap = new HashMap<>();
        Map<String, Integer> keyIndexMap = new HashMap<>();

        try {
            File file = new File(filePath);
            String[][] result = getData(file, 0);
            for (String sspKeyword : sspKeywords) {
                for (int j = 0; j < result[0].length; j++) {
                    String key = sspKeyword;
                    String newKey = result[0][j];
                    if (key.contains(newKey) && keyIndexMap.get(key) == null) {
                        keyIndexMap.put(key, j);
                        break;
                    }
                }
            }

            for (String[] aResult : result) {

                // 跳过标题
                if (isChinese(aResult[0])) {
                    continue;
                }

                String dateStr = aResult[keyIndexMap.get(sspKeywords[0])];
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                String adMasterName = aResult[keyIndexMap.get(sspKeywords[1])];
                String adSeat = aResult[keyIndexMap.get(sspKeywords[2])];
                String media = aResult[keyIndexMap.get(sspKeywords[3])];
                String os = aResult[keyIndexMap.get(sspKeywords[4])];
                String requestNumber = aResult[keyIndexMap.get(sspKeywords[6])];
                String exposureNumber = aResult[keyIndexMap.get(sspKeywords[7])];
                String clickNumber = aResult[keyIndexMap.get(sspKeywords[8])];
                String clickWight = aResult[keyIndexMap.get(sspKeywords[9])];
                String fillWight = aResult[keyIndexMap.get(sspKeywords[10])];

                if (os.toLowerCase().equals("ios")) {
                    os = "iOS";
                } else if (os.toLowerCase().contains("android")) {
                    os = "Android";
                }

                SSPADDto sspadDto = new SSPADDto();
                sspadDto.setDate(date);
                sspadDto.setAdSeat(adSeat);
                ADMaster adMaster = new ADMaster();
                if (adMasterName.contains("帕尔加特")) {
                    continue;
                }
                adMaster.setName(adMasterName);
                sspadDto.setAdMaster(adMaster);
                sspadDto.setMedia(media);
                sspadDto.setOs(os);
                requestNumber = requestNumber.substring(0, requestNumber.indexOf("."));
                sspadDto.setRequestNumber(requestNumber);
                exposureNumber = exposureNumber.substring(0, exposureNumber.indexOf("."));
                sspadDto.setExposureNumber(exposureNumber);
                clickNumber = clickNumber.substring(0, clickNumber.indexOf("."));
                sspadDto.setClickNumber(clickNumber);
                sspadDto.setClickWight(clickWight);
                sspadDto.setFillWight(fillWight);

                List<Object> sspadDtos = new ArrayList<>();
                if (stringListMap.get(media) != null) {
                    sspadDtos = stringListMap.get(media);
                }
                sspadDtos.add(sspadDto);
                stringListMap.put(media, sspadDtos);
            }
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringListMap;
    }

    /**
     * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行
     *
     * @param file       读取数据的源Excel
     * @param ignoreRows 读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1
     * @return 读出的Excel中数据的内容
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String[][] getData(File file, int ignoreRows)
            throws FileNotFoundException, IOException {
        List<String[]> result = new ArrayList<String[]>();
        int rowSize = 0;
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                file));
        // 打开HSSFWorkbook
        POIFSFileSystem fs = new POIFSFileSystem(in);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFCell cell = null;
        for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
            HSSFSheet st = wb.getSheetAt(sheetIndex);
            // 第一行为标题，不取
            for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
                HSSFRow row = st.getRow(rowIndex);
                if (row == null) {
                    continue;
                }
                int tempRowSize = row.getLastCellNum() + 1;
                if (tempRowSize > rowSize) {
                    rowSize = tempRowSize;
                }
                String[] values = new String[rowSize];
                Arrays.fill(values, "");
                boolean hasValue = false;
                for (short columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {
                    String value = "";
                    cell = row.getCell(columnIndex);
                    if (cell != null) {
                        // 注意：一定要设成这个，否则可能会出现乱码,后面版本默认设置
                        //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
                        switch (cell.getCellType()) {
                            case HSSFCell.CELL_TYPE_STRING:
                                value = cell.getStringCellValue();
                                break;
                            case HSSFCell.CELL_TYPE_NUMERIC:
                                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                    Date date = cell.getDateCellValue();
                                    if (date != null) {
                                        value = new SimpleDateFormat("yyyy-MM-dd")
                                                .format(date);
                                    } else {
                                        value = "";
                                    }
                                } else {
                                    value = new DecimalFormat("0.00").format(cell.getNumericCellValue());
//                                    value = String.valueOf(cell.getCellFormula());
                                }
                                break;
                            case HSSFCell.CELL_TYPE_FORMULA:
                                // 导入时如果为公式生成的数据则无值
                                if (!cell.getStringCellValue().equals("")) {
                                    value = cell.getStringCellValue();
                                } else {
                                    value = cell.getNumericCellValue() + "";
                                }
                                break;
                            case HSSFCell.CELL_TYPE_BLANK:
                                break;
                            case HSSFCell.CELL_TYPE_ERROR:
                                value = "";
                                break;
                            case HSSFCell.CELL_TYPE_BOOLEAN:
                                value = (cell.getBooleanCellValue() == true ? "Y" : "N");
                                break;
                            default:
                                value = "";
                        }
                    }
                    if (columnIndex == 0 && value.trim().equals("")) {
                        break;
                    }
                    values[columnIndex] = rightTrim(value);
                    hasValue = true;
                }
                if (hasValue) {
                    result.add(values);
                }
            }
        }
        in.close();
        String[][] returnArray = new String[result.size()][rowSize];
        for (int i = 0; i < returnArray.length; i++) {
            returnArray[i] = result.get(i);
        }
        return returnArray;
    }

    /**
     * 去掉字符串右边的空格
     *
     * @param str 要处理的字符串
     * @return 处理后的字符串
     */

    public static String rightTrim(String str) {
        if (str == null) {
            return "";
        }
        int length = str.length();
        for (int i = length - 1; i >= 0; i--) {
            if (str.charAt(i) != 0x20) {
                break;
            }
            length--;
        }
        return str.substring(0, length);
    }
}
