package com.zin.record.controller;

import com.zin.record.dto.ADMaster;
import com.zin.record.dto.CollectDto;
import com.zin.record.dto.DistributeDto;
import com.zin.record.dto.SSPADDto;
import com.zin.record.utils.World;
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

/**
 * Created by zhujinming on 2018/7/9.
 */
public class ExcelController {

    private final static String EXCEL_BASE_PATH = "/Users/zhujinming/Desktop/ssp/";
    private final static String EXCEL_PATH = EXCEL_BASE_PATH + "Excel/";
    private final static String EXCEL_SUFFIX = ".xls";
    private final static String EXCEL_NAME = "SDK AD 统计表";

    private static String[] whites = {
            "天天手环", /*"亿动圈",*/ "分动"/*, "芸动汇"*/, "VeryFitPro"
    };

    private static String[] titles = {
            "    日期    ", "媒体位", "操作系统", "广告主",
            "曝光(广告主)", "点击(广告主)", "点击率(广告主)", "CPM 收入（广告主）",
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

    private static String[] facebookKeywords = {
            "日期", "国家/地区", "国家/地区名称", "资产编号",
            "资产", "应用编号", "应用名称", "平台", "请求",
            "广告填充请求", "填充率", "展示次数", "显示比率",
            "点击量", "点击率", "千次展示平均费用", "收入"
    };

    private static String[] admobCountrieKeywords1 = {
            "日期", "应用", "广告单元", "国家/地区", "平台",
            "Active View 符合条件的展示次数", "可衡量的展示次数",
            "可衡量的展示次数占比 (%) (%)", "可见展示次数",
            "可见展示次数占比 (%) (%)", "AdMob 广告联盟请求的每千次展示收入 (USD)",
            "AdMob 广告联盟请求次数", "点击次数", "估算收入 (USD)", "展示点击率 (%)",
            "每千次展示收入 (USD)", "展示次数", "匹配率 (%)", "匹配请求数",
            "激励视频广告开始次数", "激励视频广告完成次数", "展示率 (%)"
    };

    private static String[] adMobKeywords = {
            "广告单元", "日期", "应用",
            "Active View 符合条件的展示次数",
            "可衡量的展示次数", "可衡量的展示次数占比 (%) (%)",
            "可见展示次数", "可见展示次数占比 (%) (%)",
            "AdMob 广告联盟请求的每千次展示收入 (USD)",
            "AdMob 广告联盟请求次数",
            "点击次数", "估算收入 (USD)", "展示点击率 (%)",
            "每千次展示收入 (USD)", "展示次数", "匹配率 (%)",
            "匹配请求数", "激励视频广告开始次数",
            "激励视频广告完成次数", "展示率 (%)"
    };

    private static String[] adMobCountrieKeywords = {
            "日期", "广告单元", "国家/地区", "Active View 符合条件的展示次数",
            "可衡量的展示次数", "可衡量的展示次数占比 (%) (%)",
            "可见展示次数", "可见展示次数占比 (%) (%)",
            "AdMob 广告联盟请求的每千次展示收入 (USD)",
            "AdMob 广告联盟请求次数", "点击次数", "估算收入 (USD)",
            "展示点击率 (%)", "每千次展示收入 (USD)", "展示次数",
            "匹配率 (%)", "匹配请求数", "激励视频广告开始次数",
            "激励视频广告完成次数", "展示率 (%)"
    };

    private static String[] whiteCountries = {
            "日本", "法国", "奥地利", "荷兰", "捷克", "印度",
            "爱尔兰", "南非", "澳大利亚", "比利时", "俄罗斯",
            "乌克兰", "巴西", "波兰", "意大利", "加拿大"
    };


    private static Map<String, CollectDto> collectDtos = new HashMap<>();
    private static Map<String, Object> whiteCountrieMap = new HashMap<>();

    public static void main(String[] args) throws Exception {

        for (String countrie: whiteCountries) {
            whiteCountrieMap.put(countrie, null);
        }

        World.INSTANCE.init();

//        guojia();
//
//        boolean a = true;
//
//        if (a) {
//            return;
//        }

        Map<String, List<Object>> sspMap = ParserExcelUtil.INSTANCE.getExcelSSPData(EXCEL_BASE_PATH + "ssp.xls");
        Map<String, List<Object>> gdtMap = ParserExcelUtil.INSTANCE.getExcelGDTData(EXCEL_BASE_PATH + "gdt.xls", collectDtos);
        Map<String, List<Object>> mobMap = ParserExcelUtil.INSTANCE.getADMobData(EXCEL_BASE_PATH + "admob.xls", collectDtos);
        Map<String, List<DistributeDto>> adMobCountrieData = ParserExcelUtil.INSTANCE.getADMobCountrieData(EXCEL_BASE_PATH + "admob_countrie.xls");

        // 把广告主的数据合并到 ssp
        if (gdtMap != null) {
            sspMap = ParserExcelUtil.INSTANCE.synthetic(sspMap, gdtMap);
            sspMap = ParserExcelUtil.INSTANCE.synthetic(sspMap, mobMap);
        }

        // 存放所有最终数据
        Map<String, Map> allData = new HashMap<>();

        allData.put("ssp", ParserExcelUtil.INSTANCE.getSortData(sspMap));

        if (gdtMap != null) {
            allData.put("gdt", ParserExcelUtil.INSTANCE.getSortData(gdtMap));
        }

        if (mobMap != null) {
            allData.put("admob", ParserExcelUtil.INSTANCE.getSortData(mobMap));
        }

        /****************************************开始汇总****************************************/
        Map<String, ExcelPoJo> excelMap = new HashMap<>();
        String[] rowName = {
                "         时间         ", "软件", "广告主", "收入", "分成后收入", "分成比例"
        };

        ExcelPoJo excel = new ExcelPoJo();
        List<Object[]> data = new ArrayList<>();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = df.format(new Date());

        for (String key1 :
                collectDtos.keySet()) {

            CollectDto collectDto = collectDtos.get(key1);
            String application = collectDto.getApplication();
            String adMaster = collectDto.getAdMaster();
            String expectIncome = collectDto.getExpectIncome();
            try {
                expectIncome = String.valueOf(new DecimalFormat("0.00").format(Double.valueOf(expectIncome)));

            } catch (Exception e) {
                e.printStackTrace();
            }
            String spoilsIncome = collectDto.getSpoilsIncome();
            spoilsIncome = String.valueOf(new DecimalFormat("0.00").format(Double.valueOf(spoilsIncome)));
            String spoilsScale = collectDto.getSpoilsScale();

            String currency = "￥";
            if (adMaster.toLowerCase().contains("go") ||
                    adMaster.toLowerCase().contains("fac")) {
                currency = "$";
            }

            expectIncome = currency + expectIncome;
            spoilsIncome = currency + spoilsIncome;

            data.add(new Object[]{
                    currentTime, application, adMaster,
                    expectIncome, spoilsIncome, spoilsScale
            });

            excel.setDataList(data);
            excel.setRowName(rowName);
            excelMap.put("收益汇总", excel);
        }

        /****************************************结束汇总****************************************/

        /****************************************开始地区计算****************************************/
        if (adMobCountrieData != null) {
            excel = new ExcelPoJo();
            data = new ArrayList<>();
            String[] regionRowName = {
                    "         时间         ", "   系统   ", "地区", "展示数"
            };

            int i = 0;
            for (String osKey :
                    adMobCountrieData.keySet()) {

                List<DistributeDto> distributeOsDtos = adMobCountrieData.get(osKey);
                Date date = distributeOsDtos.get(++i).getData();

                Map<String, Integer> stringIntegerHashMap = new HashMap<>();

                for (DistributeDto distributeDto :
                        distributeOsDtos) {

                    String region = distributeDto.getRegion();
                    int showNumber = new BigDecimal(distributeDto.getShowNumber()).intValue();

                    if (stringIntegerHashMap.containsKey(region)) {
                        stringIntegerHashMap.put(region, stringIntegerHashMap.get(region) + showNumber);
                    } else {
                        stringIntegerHashMap.put(region, showNumber);
                    }
                }

                for (String region :
                        stringIntegerHashMap.keySet()) {

                    data.add(new Object[]{
                            date, osKey, region,
                            stringIntegerHashMap.get(region)
                    });
                }

                excel.setDataList(data);
                excel.setRowName(regionRowName);
                excelMap.put("地区汇总", excel);
            }
        }

        /****************************************结束地区计算****************************************/

        Map sspDataMap = allData.get("ssp");

        for (Object key : sspDataMap.keySet()) {

            ExcelPoJo excelPoJo = new ExcelPoJo();
            List<Object[]> dataList = new ArrayList<>();
            Map mediaMap = (Map) sspDataMap.get(key);


            for (Object dateObject : mediaMap.keySet()) {

                int dateKey = (int) dateObject;
                SSPADDto sspadDtos = (SSPADDto) mediaMap.get(dateKey);
                ADMaster adMaster = sspadDtos.getAdMaster();
                String spoilsScaleStr = "";
                int spoilsScale = sspadDtos.getSpoilsScale();
                String adMasterClickNumber = adMaster.getClickNumber();
                String adMasterClickWight = adMaster.getClickWight();
                adMasterClickWight = adMasterClickWight == null || adMasterClickWight.length() == 0 ?
                        "0.0" : adMasterClickWight;

                adMasterClickWight = String.valueOf(
                        new BigDecimal(adMasterClickWight).
                                setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 10);
                adMasterClickWight += "%";

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

                Double cpc = null;
                if (adMasterExpectIncome != null && adMasterClickNumber != null) {
                    cpc = (Double.valueOf(adMasterExpectIncome) / Double.valueOf(adMasterClickNumber));
                    Double spoilsScaleDouble = Double.valueOf(adMasterExpectIncome);
                    if (spoilsScale != 100) {
                        spoilsScaleDouble = Double.valueOf(adMasterExpectIncome) -
                                (Double.valueOf(adMasterExpectIncome) * (spoilsScale / 100.0));
                    }


                    if (cpc.isNaN() || cpc.isInfinite()) {
                        cpc = 0.0;
                    } else {
                        cpc = Double.valueOf(new DecimalFormat(".00").format(cpc));
                    }

                    if (spoilsScaleDouble.isNaN() || spoilsScaleDouble.isInfinite()) {
                        spoilsScaleStr = "";
                    } else {
                        spoilsScaleStr = String.valueOf(new DecimalFormat("0.00").format(spoilsScaleDouble));
                    }
                }

//                "日期", "媒体位", "操作系统", "广告主",
//                        "曝光(广告主)", "点击(广告主)", "CPM 收入（广告主）", "CPC 收入（广告主）", "收入(广告主)", "收入后分成", "广告位", "日活",
//                        "请求数", "曝光数", "点击数", "点击率", "填充率", "曝光差异", "点击差异"

                dataList.add(new Object[]{
                        date, media, os, adMasterName,
                        adMasterExposureNumber, adMasterClickNumber, adMasterClickWight,
                        adMasterCpm, cpc, adMasterExpectIncome,
                        spoilsScaleStr, adSeat, "", requestNumber, exposureNumber,
                        clickNumber, clickWight, fillWight,
                        exposureDifference, clickDifference
                });
            }

            excelPoJo.setRowName(titles);
            excelPoJo.setDataList(dataList);
            excelMap.put(key.toString(), excelPoJo);
        }


        String filePath = EXCEL_PATH + EXCEL_NAME + EXCEL_SUFFIX;
        new ExcelUtil(filePath, excelMap).export();
    }

    private static void guojia() throws Exception {
        Map<String, List<Object>> facebookMap = ParserExcelUtil.INSTANCE.getFacebookData(EXCEL_BASE_PATH + "facebook.xls", facebookKeywords, collectDtos);
        Map<String, List<Object>> mobMap = ParserExcelUtil.INSTANCE.getADMobCountrieData1(EXCEL_BASE_PATH + "admob.xls", admobCountrieKeywords1, collectDtos);
        Map<String, List<Object>> mobMap1 = ParserExcelUtil.INSTANCE.getADMobCountrieData1(EXCEL_BASE_PATH + "admob1.xls", admobCountrieKeywords1, collectDtos);

        List<Object> facebooks = facebookMap.get("veryfitPro - Audience Network");
        List<Object> verfits = mobMap.get("VeryFitPro");
        List<Object> verfits1 = mobMap1.get("VeryFitPro");

        List<SSPADDto> mainLists = new ArrayList<>();
        for (Object f: facebooks) {
            mainLists.add((SSPADDto) f);
        }

        for (Object v: verfits) {
            mainLists.add((SSPADDto) v);
        }

        for (Object v1: verfits1) {
            mainLists.add((SSPADDto) v1);
        }

        Map<Date, List<SSPADDto>> sspadDtoMap = new HashMap<>();
        List<Object[]> data = new ArrayList<>();

        for (SSPADDto sspAD : mainLists) {
            Date date = sspAD.getDate();

            String requestNumberStr = sspAD.getAdMaster().getRequestNumber();
            if(requestNumberStr.contains(".")) {
                requestNumberStr = requestNumberStr.substring(0, requestNumberStr.indexOf("."));
            }
            int requestNumber = Integer.valueOf(requestNumberStr);
            String exposureNumberStr = sspAD.getAdMaster().getExposureNumber();
            int exposureNumber = Integer.valueOf(exposureNumberStr);

            Double exposureWight = new BigDecimal(
                    ((double) exposureNumber / (double) requestNumber) * 100
            ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            sspAD.getAdMaster().setRequestNumber(requestNumberStr);

            String exposureWightStr = String.valueOf(exposureWight);
            if (exposureWightStr.contains(".")) {
                String a = exposureWightStr.substring(exposureWightStr.indexOf(".") + 1, exposureWightStr.length());
                if (a.equals("0")) {
                    exposureWightStr = exposureWightStr.substring(0, exposureWightStr.indexOf("."));
                }
                exposureWightStr += "%";
            }
            sspAD.getAdMaster().setExposureWight(exposureWightStr);

            // 是否存在天的 Key 存在则取出对象存入对应天数的数据，否则新建对象存储
            boolean isExist = sspadDtoMap.containsKey(date);
            List<SSPADDto> sspadDtos = new ArrayList<>();
            if (isExist) {
                sspadDtos = sspadDtoMap.get(date);
            }
            sspadDtos.add(sspAD);
            sspadDtoMap.put(date, sspadDtos);
        }

        // 国家为 KEY 过滤多个当天相同国家，合并相同国家数据
        Map<String, Map<String, Map<Date, SSPADDto>>> countriesOSSSPADDtoMap = new HashMap<>();

        // 当天下的国家下的系统下的天
        for (Date key: sspadDtoMap.keySet()) {

            List<SSPADDto> sspadDtos = sspadDtoMap.get(key);
            for (SSPADDto sspadDto: sspadDtos) {
                Date date = sspadDto.getDate();
                String os = sspadDto.getOs();
                String countries = sspadDto.getCountrie();
                String requestNumberStr = sspadDto.getAdMaster().getRequestNumber();
                int requestNumber = Integer.valueOf(requestNumberStr);
                String clickNumber = sspadDto.getAdMaster().getClickNumber();
                String exposureNumber = sspadDto.getAdMaster().getExposureNumber();
                String exposureWight = sspadDto.getAdMaster().getExposureWight();

                Map<String, Map<Date, SSPADDto>> osSSPADDtoMap = new HashMap<>();
                Map<Date, SSPADDto> sspadDtos1 = new HashMap<>();

                if (countriesOSSSPADDtoMap.containsKey(countries)) {
                    osSSPADDtoMap = countriesOSSSPADDtoMap.get(countries);

                    if (osSSPADDtoMap.containsKey(os)) {
                        sspadDtos1 = osSSPADDtoMap.get(os);
                        if (sspadDtos1.containsKey(date)) {
                            sspadDto = sspadDtos1.get(date);

                            int requestNumber1 = Integer.valueOf(sspadDto.getAdMaster().getRequestNumber());
                            int clickNumber1 = Integer.valueOf(sspadDto.getAdMaster().getClickNumber());
                            int exposureNumber1 = Integer.valueOf(sspadDto.getAdMaster().getExposureNumber());

                            requestNumber = requestNumber1 + requestNumber;
                            clickNumber1 = clickNumber1 + Integer.valueOf(clickNumber);
                            exposureNumber1 = exposureNumber1 + Integer.valueOf(exposureNumber);

                            sspadDto.getAdMaster().setRequestNumber(String.valueOf(requestNumber));
                            sspadDto.getAdMaster().setClickNumber(String.valueOf(clickNumber1));
                            sspadDto.getAdMaster().setExposureNumber(String.valueOf(exposureNumber1));
                            // 少数据，比如预计收入.
                            Double exposureWight11 = new BigDecimal(
                                    ((double) exposureNumber1 / (double) requestNumber) * 100
                            ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            String exposureWightStr = String.valueOf(exposureWight11);
                            exposureWightStr = exposureWightStr.replace("%", "");
                            if (exposureWightStr.contains(".")) {
                                String a = exposureWightStr.substring(exposureWightStr.indexOf(".") + 1, exposureWightStr.length());
                                if (a.equals("0")) {
                                    exposureWightStr = exposureWightStr.substring(0, exposureWightStr.indexOf("."));
                                }
                                exposureWightStr += "%";
                                System.out.println(a);
                            }
                            sspadDto.getAdMaster().setExposureWight(exposureWightStr);
                        }
                    }
                }

                sspadDtos1.put(date, sspadDto);
                osSSPADDtoMap.put(os, sspadDtos1);
                countriesOSSSPADDtoMap.put(countries, osSSPADDtoMap);
            }
        }

        // 根据白名单优化数据
//        for (String countries: countriesOSSSPADDtoMap.keySet()) {
//            boolean isWhiteCountries = whiteCountrieMap.containsKey(countries);
//            if (!isWhiteCountries) {
//
//                Map<String, Map<Date, SSPADDto>> stringSSPADDtoMap = countriesOSSSPADDtoMap.get(countries);
//                for (String os : stringSSPADDtoMap.keySet()) {
//                    Map<Date, SSPADDto> dateSSPADDtoMap1 = stringSSPADDtoMap.get(os);
//                    for (Date date: dateSSPADDtoMap1.keySet()) {
//                        SSPADDto sspadDto1 = dateSSPADDtoMap1.get(date);
//
//                        Date dateStr = sspadDto1.getDate();
//                        String osStr = sspadDto1.getOs();
//                        String countriesStr = sspadDto1.getCountrie();
//                        String requestNumberStr = sspadDto1.getAdMaster().getRequestNumber();
//                        int requestNumber = Integer.valueOf(requestNumberStr);
//                        String clickNumber = sspadDto1.getAdMaster().getClickNumber();
//                        String exposureNumber = sspadDto1.getAdMaster().getExposureNumber();
//                        String exposureWight = sspadDto1.getAdMaster().getExposureWight();
//
//                        ADMaster jpADDto = countriesOSSSPADDtoMap.get("日本").get(os).get(date).getAdMaster();
//                        jpADDto.setClickNumber(String.valueOf(Integer.valueOf(jpADDto.getClickNumber()) + Integer.valueOf(clickNumber)));
//                        jpADDto.setExposureNumber(String.valueOf(Integer.valueOf(jpADDto.getExposureNumber()) + Integer.valueOf(exposureNumber)));
//
//                        if (requestNumber > 30) {
//                            Random rand = new Random();
//                            // 2.生成5－26之间的随机数，包括26
//                            int randNum = rand.nextInt(22) + 5;
//                            jpADDto.setRequestNumber(String.valueOf(Integer.valueOf(jpADDto.getRequestNumber()) + (requestNumber - randNum)));
//                            requestNumber = randNum;
//                        }
//
//                        Double exposureWight11 = new BigDecimal(
//                                ((double) Integer.valueOf(jpADDto.getExposureNumber()) / (double)Integer.valueOf(jpADDto.getRequestNumber())) * 100
//                        ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                        String exposureWightStr = String.valueOf(exposureWight11);
//                        exposureWightStr = exposureWightStr.replace("%", "");
//                        if (exposureWightStr.contains(".")) {
//                            String a = exposureWightStr.substring(exposureWightStr.indexOf(".") + 1, exposureWightStr.length());
//                            if (a.equals("0")) {
//                                exposureWightStr = exposureWightStr.substring(0, exposureWightStr.indexOf("."));
//                            }
//                            exposureWightStr += "%";
//                        }
//                        jpADDto.setExposureWight(exposureWightStr);
//
//                        countriesOSSSPADDtoMap.get("日本").get(os).get(date).setAdMaster(jpADDto);
//
//                        sspadDto1.getAdMaster().setRequestNumber(String.valueOf(requestNumber));
//                        sspadDto1.getAdMaster().setExposureNumber("0");
//                        sspadDto1.getAdMaster().setClickNumber("0");
//                        sspadDto1.getAdMaster().setExposureWight("0%");
//                        countriesOSSSPADDtoMap.get(countries).get(os).get(date).setAdMaster(sspadDto1.getAdMaster());
//                    }
//                }
//            }
//        }


        for (String countries:
                countriesOSSSPADDtoMap.keySet()) {
            Map<String, Map<Date, SSPADDto>> stringSSPADDtoMap = countriesOSSSPADDtoMap.get(countries);
            for (String os :
                    stringSSPADDtoMap.keySet()) {
                Map<Date, SSPADDto> dateSSPADDtoMap1 = stringSSPADDtoMap.get(os);
                for (Date date:
                     dateSSPADDtoMap1.keySet()) {
                    SSPADDto sspadDto1 = dateSSPADDtoMap1.get(date);

                    Date dateStr = sspadDto1.getDate();
                    String osStr = sspadDto1.getOs();
                    String countriesStr = sspadDto1.getCountrie();
                    String requestNumberStr = sspadDto1.getAdMaster().getRequestNumber();
                    int requestNumber = Integer.valueOf(requestNumberStr);
                    String clickNumber = sspadDto1.getAdMaster().getClickNumber();
                    String exposureNumber = sspadDto1.getAdMaster().getExposureNumber();
                    String exposureWight = sspadDto1.getAdMaster().getExposureWight();

                    if (exposureWight.equals("0.00")) {
                        System.out.println("卧槽");
                    }

                    data.add(new Object[] {
                            dateStr, osStr, countriesStr, requestNumber,
                            Integer.valueOf(exposureNumber), Integer.valueOf(clickNumber), exposureWight
                    });
                }
            }
        }

        Map<String, ExcelPoJo> excelMap = new HashMap<>();
        ExcelPoJo excelPoJo = new ExcelPoJo();

        String[] rowName = {
                "日期", "平台", "国家/地区", "请求次数", "展示次数", "点击次数", "展示率(%)"
        };

        excelPoJo.setRowName(rowName);
        excelPoJo.setDataList(data);

        excelMap.put("国家分布", excelPoJo);
        String filePath = EXCEL_PATH + EXCEL_NAME + EXCEL_SUFFIX;
        new ExcelUtil(filePath, excelMap).export();
        System.out.println("");
    }

}
