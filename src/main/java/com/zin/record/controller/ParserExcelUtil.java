package com.zin.record.controller;

import com.zin.record.dto.ADMaster;
import com.zin.record.dto.CollectDto;
import com.zin.record.dto.DistributeDto;
import com.zin.record.dto.SSPADDto;
import com.zin.record.utils.World;
import com.zin.record.utils.excel.ExcelUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import  com.zin.record.controller.ExcelConstants.*;

import static com.zin.record.controller.ExcelConstants.*;

/**
 * Created by zhujinming on 2018/8/30.
 */
public enum ParserExcelUtil {

    INSTANCE;

    public Map<String, List<Object>> getADMobCountrieData1(String filePath, String[] titles, Map<String, CollectDto> collectDtos) {

        File file = new File(filePath);

        if (!file.exists()) {
            return null;
        }

        Map<String, List<Object>> stringListMap = new HashMap<>();
        Map<String, Integer> keyIndexMap = new HashMap<>();

        try {
            String[][] result = getData(file, 0);
//            String aa = "";
//            for (String aaa :
//                    result[1]) {
//                aa+='"'+aaa+'"' + ", ";
//            }
            for (String keyWord : titles) {
                for (int i = 0; i < result.length; i++) {
                    for (int j = 0; j < result[i].length; j++) {
                        String newKey = result[i][j];
                        if (!Utils.INSTANCE.isChinese(newKey)) {
                            break;
                        }

                        if (keyWord.equals(newKey) && keyIndexMap.get(keyWord) == null) {
                            keyIndexMap.put(keyWord, j);
                            continue;
                        }
                    }
                }

            }

            for (String[] aResult : result) {

                CollectDto collectDto = new CollectDto();

                int space = 0;

                for (int i = 0; i < aResult.length; i++) {
                    String str = aResult[i];
                    if (str.isEmpty() || Utils.INSTANCE.isChinese(str)) {
                        ++space;
                    }
                }

                if (space > aResult.length - 2) {
                    continue;
                }

//                "日期", "应用", "广告单元", "国家/地区", "平台",
//                        "Active View 符合条件的展示次数", "可衡量的展示次数",
//                        "可衡量的展示次数占比 (%) (%)", "可见展示次数",
//                        "可见展示次数占比 (%) (%)", "AdMob 广告联盟请求的每千次展示收入 (USD)",
//                        "AdMob 广告联盟请求次数", "点击次数", "估算收入 (USD)", "展示点击率 (%)",
//                        "每千次展示收入 (USD)", "展示次数", "匹配率 (%)", "匹配请求数",
//                        "激励视频广告开始次数", "激励视频广告完成次数", "展示率 (%)"

                String requestNumber = aResult[keyIndexMap.get(titles[11])];
                String dateStr = aResult[keyIndexMap.get(titles[0])];
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                String countries = aResult[keyIndexMap.get(titles[3])];
                String media = aResult[keyIndexMap.get(titles[1])];
                String id = aResult[keyIndexMap.get(titles[1])];
                String exposureNumber = aResult[keyIndexMap.get(titles[16])];
                String clickNumber = aResult[keyIndexMap.get(titles[12])];
                String expectIncome = aResult[keyIndexMap.get(titles[13])];
                String cpm = aResult[keyIndexMap.get(titles[15])];
                String clickWight = aResult[keyIndexMap.get(titles[14])];
                String exposureWight = aResult[keyIndexMap.get(titles[21])];

                String os = aResult[keyIndexMap.get(titles[4])];
                if (os.toLowerCase().contains("io")) {
                    os = "iOS";
                } else if (os.toLowerCase().contains("an")) {
                    os = "Android";
                }

                SSPADDto sspadDto = new SSPADDto();
                sspadDto.setCountrie(countries);
                sspadDto.setDate(date);
                ADMaster adMaster = new ADMaster();
                adMaster.setExposureWight(exposureWight);
                adMaster.setRequestNumber(requestNumber);
                adMaster.setId(id);
//                adMaster.setName(name);
                adMaster.setName("Google");
                adMaster.setExpectIncome(expectIncome);
                adMaster.setCpm(cpm);
                exposureNumber = exposureNumber.substring(0, exposureNumber.indexOf("."));
                adMaster.setExposureNumber(exposureNumber);
                clickNumber = clickNumber.substring(0, clickNumber.indexOf("."));
                adMaster.setClickNumber(clickNumber);
                adMaster.setClickWight(clickWight);

                sspadDto.setAdMaster(adMaster);
                sspadDto.setMedia(media);
                int spoilsScale = 100;
                if (media.contains("分动")) {
                    spoilsScale = 80;
                }

                if (media.contains("分动") || media.contains("V")) {
                    collectDto.setAdMaster(adMaster.getName());
                    collectDto.setApplication(media);

                    CollectDto collectDto1 = collectDtos.get(media);
                    String collectExpectIncome = collectDto1 == null ? "0.0" : collectDto1.getExpectIncome();
                    collectExpectIncome = collectExpectIncome == null ? "0.0" : collectExpectIncome;
                    String expectIncome1 = String.valueOf(Double.valueOf(collectExpectIncome) + Double.valueOf(expectIncome));
                    collectDto.setExpectIncome(expectIncome1);
                    Double spoilsScaleDouble = Double.valueOf(expectIncome);
                    if (spoilsScale != 100) {
                        spoilsScaleDouble = Double.valueOf(expectIncome) -
                                (Double.valueOf(expectIncome) * (spoilsScale / 100.0));
                    }

                    String collectSpoilsIncome = collectDto1 == null ? "0.0" : collectDto1.getSpoilsIncome();
                    collectSpoilsIncome = collectSpoilsIncome == null ? "0.0" : collectSpoilsIncome;
                    String spoilsIncome = String.valueOf(Double.valueOf(collectSpoilsIncome) + spoilsScaleDouble);
                    collectDto.setSpoilsIncome(String.valueOf(spoilsIncome));
                    collectDto.setSpoilsScale(String.valueOf(spoilsScale));
                    collectDtos.put(media, collectDto);
                }

                sspadDto.setSpoilsScale(spoilsScale);
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

    public Map<String, List<Object>> getFacebookData(String filePath, String[] titles, Map<String, CollectDto> collectDtos) {

        File file = new File(filePath);

        if (!file.exists()) {
            return null;
        }

        Map<String, List<Object>> stringListMap = new HashMap<>();
        Map<String, Integer> keyIndexMap = new HashMap<>();

//        String aa = "";

        try {
            String[][] result = getData(file, 0);
//            for (String aaa :
//                    result[1]) {
//                aa+='"'+aaa+'"' + ", ";
//            }
            for (String keyWord : titles) {
                for (int i = 0; i < result.length; i++) {
                    for (int j = 0; j < result[i].length; j++) {
                        String newKey = result[i][j];
                        if (!Utils.INSTANCE.isChinese(newKey)) {
                            break;
                        }

                        if (keyWord.equals(newKey) && keyIndexMap.get(keyWord) == null) {
                            keyIndexMap.put(keyWord, j);
                            continue;
                        }
                    }
                }

            }

            for (String[] aResult : result) {

                CollectDto collectDto = new CollectDto();

                int space = 0;

                for (int i = 0; i < aResult.length; i++) {
                    String str = aResult[i];
                    if (str.isEmpty() || Utils.INSTANCE.isChinese(str)) {
                        ++space;
                    }
                }

                if (space > aResult.length - 2) {
                    continue;
                }

//                "日期", "国家/地区", "国家/地区名称", "资产编号",
//                        "资产", "应用编号", "应用名称", "平台", "请求",
//                        "广告填充请求", "填充率", "展示次数", "显示比率",
//                        "点击量", "点击率", "千次展示平均费用", "收入"
                String dateStr = aResult[keyIndexMap.get(titles[0])];
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                String appName = aResult[keyIndexMap.get(titles[6])];
                String countries = World.INSTANCE.getCnNameToCode(aResult[keyIndexMap.get(titles[1])]);
                String id = aResult[keyIndexMap.get(titles[4])];
                String exposureNumber = aResult[keyIndexMap.get(titles[11])];
                String exposureWidght = aResult[keyIndexMap.get(titles[12])];
                String requestNumber = aResult[keyIndexMap.get(titles[8])];
                String clickNumber = aResult[keyIndexMap.get(titles[13])];
                String expectIncome = aResult[keyIndexMap.get(titles[16])];
                String cpm = aResult[keyIndexMap.get(titles[15])];
                String clickWight = aResult[keyIndexMap.get(titles[14])];

                String os = aResult[keyIndexMap.get(titles[7])];
                if (os.toLowerCase().contains("io")) {
                    os = "iOS";
                } else if (os.toLowerCase().contains("an")) {
                    os = "Android";
                }

                SSPADDto sspadDto = new SSPADDto();
                sspadDto.setCountrie(countries);
                sspadDto.setDate(date);
                ADMaster adMaster = new ADMaster();
                adMaster.setRequestNumber(requestNumber);
                adMaster.setExposureWight(exposureWidght);
                adMaster.setId(id);
//                adMaster.setName(name);
                adMaster.setName("Facebook");
                adMaster.setExpectIncome(expectIncome);
                adMaster.setCpm(cpm);
                exposureNumber = exposureNumber.substring(0, exposureNumber.indexOf("."));
                adMaster.setExposureNumber(exposureNumber);
                clickNumber = clickNumber.substring(0, clickNumber.indexOf("."));
                adMaster.setClickNumber(clickNumber);
                adMaster.setClickWight(clickWight);

                sspadDto.setAdMaster(adMaster);
                sspadDto.setMedia(appName);
                int spoilsScale = 100;
                if (appName.contains("分动")) {
                    spoilsScale = 80;
                }

                if (appName.contains("分动") || appName.contains("V")) {
                    collectDto.setAdMaster(adMaster.getName());
                    collectDto.setApplication(appName);

                    CollectDto collectDto1 = collectDtos.get(appName);
                    String collectExpectIncome = collectDto1 == null ? "0.0" : collectDto1.getExpectIncome();
                    collectExpectIncome = collectExpectIncome == null ? "0.0" : collectExpectIncome;
                    String expectIncome1 = String.valueOf(Double.valueOf(collectExpectIncome) + Double.valueOf(expectIncome));
                    collectDto.setExpectIncome(expectIncome1);
                    Double spoilsScaleDouble = Double.valueOf(expectIncome);
                    if (spoilsScale != 100) {
                        spoilsScaleDouble = Double.valueOf(expectIncome) -
                                (Double.valueOf(expectIncome) * (spoilsScale / 100.0));
                    }

                    String collectSpoilsIncome = collectDto1 == null ? "0.0" : collectDto1.getSpoilsIncome();
                    collectSpoilsIncome = collectSpoilsIncome == null ? "0.0" : collectSpoilsIncome;
                    String spoilsIncome = String.valueOf(Double.valueOf(collectSpoilsIncome) + spoilsScaleDouble);
                    collectDto.setSpoilsIncome(String.valueOf(spoilsIncome));
                    collectDto.setSpoilsScale(String.valueOf(spoilsScale));
                    collectDtos.put(appName, collectDto);
                }

                sspadDto.setSpoilsScale(spoilsScale);
                sspadDto.setOs(os);

                List<Object> sspadDtos = new ArrayList<>();
                if (stringListMap.get(appName) != null) {
                    sspadDtos = stringListMap.get(appName);
                }
                sspadDtos.add(sspadDto);
                stringListMap.put(appName, sspadDtos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringListMap;
    }

    public  Map<String, List<Object>> getExcelSSPData(String filePath) {

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
                if (Utils.INSTANCE.isChinese(aResult[0])) {
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
                for (String key : whites) {

                    if (media.contains("原生") ||
                            media.contains("插屏") ||
                            media.toLowerCase().contains("icon")) {
                        continue;
                    }

                    if (adMasterName.contains("帕尔加特") ||
                            adMasterName.toLowerCase().contains("yingmob") ||
                            adMasterName.toLowerCase().contains("facebook") ||
                            adMasterName.toLowerCase().contains("智先锋")) {
                        continue;
                    }

                    if (media.contains(key)) {

                        adMaster.setName(adMasterName);
                        sspadDto.setAdMaster(adMaster);
                        sspadDto.setMedia(media);
                        sspadDto.setOs(os);
                        try {
                            requestNumber = requestNumber.substring(0, requestNumber.indexOf("."));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                        break;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringListMap;
    }

    public Map<String, List<Object>> getExcelGDTData(String filePath, Map<String, CollectDto> collectDtos) {

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
                        if (!Utils.INSTANCE.isChinese(newKey)) {
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

                CollectDto collectDto = new CollectDto();

                int space = 0;

                for (int i = 0; i < aResult.length; i++) {
                    String str = aResult[i];
                    if (str.isEmpty() || Utils.INSTANCE.isChinese(str)) {
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
                if (os.toLowerCase().contains("io")) {
                    os = "iOS";
                } else if (os.toLowerCase().contains("an")) {
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
                int spoilsScale = 100;
                if (media.contains("芸动汇") || media.contains("天天手环")) {
                    collectDto.setAdMaster(adMaster.getName());
                    collectDto.setApplication(media);

                    CollectDto collectDto1 = collectDtos.get(media);
                    String collectExpectIncome = collectDto1 == null ? "0.0" : collectDto1.getExpectIncome();
                    collectExpectIncome = collectExpectIncome == null ? "0.0" : collectExpectIncome;
                    String expectIncome1 = String.valueOf(Double.valueOf(collectExpectIncome) + Double.valueOf(expectIncome));
                    collectDto.setExpectIncome(expectIncome1);
                    Double spoilsScaleDouble = Double.valueOf(expectIncome);
                    if (spoilsScale != 100) {
                        spoilsScaleDouble = Double.valueOf(expectIncome) -
                                (Double.valueOf(expectIncome) * (spoilsScale / 100.0));
                    }

                    String collectSpoilsIncome = collectDto1 == null ? "0.0" : collectDto1.getSpoilsIncome();
                    collectSpoilsIncome = collectSpoilsIncome == null ? "0.0" : collectSpoilsIncome;
                    String spoilsIncome = String.valueOf(Double.valueOf(collectSpoilsIncome) + spoilsScaleDouble);
                    collectDto.setSpoilsIncome(String.valueOf(spoilsIncome));
                    collectDto.setSpoilsScale(String.valueOf(spoilsScale));
                    collectDtos.put(media, collectDto);
                }
                sspadDto.setMedia(media);
                sspadDto.setSpoilsScale(spoilsScale);
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

    public Map<String, List<DistributeDto>> getADMobCountrieData(String filePath) {

        File file = new File(filePath);

        if (!file.exists()) {
            return null;
        }

        Map<String, List<DistributeDto>> distributeMaps = new HashMap<>();
        Map<String, Integer> keyIndexMap = new HashMap<>();

        try {
            String[][] result = getData(file, 0);

            for (String keyWord : adMobCountrieKeywords) {
                for (int i = 0; i < result.length; i++) {
                    for (int j = 0; j < result[i].length; j++) {
                        String newKey = result[i][j];
                        if (!Utils.INSTANCE.isChinese(newKey)) {
                            break;
                        }

                        if (keyWord.equals(newKey) && keyIndexMap.get(keyWord) == null) {
                            keyIndexMap.put(keyWord, j);
                            continue;
                        }
                    }
                }

            }

            for (String[] aResult : result) {

                int space = 0;

                for (int i = 0; i < aResult.length; i++) {
                    String str = aResult[i];
                    if (str.isEmpty() || Utils.INSTANCE.isChinese(str)) {
                        ++space;
                    }
                }

                if (space > aResult.length - 2) {
                    continue;
                }

                DistributeDto distributeDto = new DistributeDto();
                String time = aResult[keyIndexMap.get(adMobCountrieKeywords[0])];
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(time);
                String media = aResult[keyIndexMap.get(adMobCountrieKeywords[1])];
                String os = "iOS";
                if (media.toLowerCase().contains("an")) {
                    os = "Android";
                }
                String region = aResult[keyIndexMap.get(adMobCountrieKeywords[2])];
                region = World.INSTANCE.getContinentToCountrie(region);
                String showNumber = aResult[keyIndexMap.get(adMobCountrieKeywords[14])];
                distributeDto.setData(date);
                distributeDto.setOs(os);
                distributeDto.setRegion(region);
                distributeDto.setShowNumber(showNumber);
                List<DistributeDto> distributeDtos = distributeMaps.get(os);
                if (distributeDtos == null) {
                    distributeDtos = new ArrayList<>();
                }
                distributeDtos.add(distributeDto);
                distributeMaps.put(os, distributeDtos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return distributeMaps;
    }


    public Map<String, List<Object>> getADMobData(String filePath, Map<String, CollectDto> collectDtos) {

        File file = new File(filePath);

        if (!file.exists()) {
            return null;
        }

        Map<String, List<Object>> stringListMap = new HashMap<>();
        Map<String, Integer> keyIndexMap = new HashMap<>();

        try {
            String[][] result = getData(file, 0);
            for (String keyWord : adMobKeywords) {
                for (int i = 0; i < result.length; i++) {
                    for (int j = 0; j < result[i].length; j++) {
                        String newKey = result[i][j];
                        if (!Utils.INSTANCE.isChinese(newKey)) {
                            break;
                        }

                        if (keyWord.equals(newKey) && keyIndexMap.get(keyWord) == null) {
                            keyIndexMap.put(keyWord, j);
                            continue;
                        }
                    }
                }

            }

            for (String[] aResult : result) {

                CollectDto collectDto = new CollectDto();

                int space = 0;

                for (int i = 0; i < aResult.length; i++) {
                    String str = aResult[i];
                    if (str.isEmpty() || Utils.INSTANCE.isChinese(str)) {
                        ++space;
                    }
                }

                if (space > aResult.length - 2) {
                    continue;
                }

                String dateStr = aResult[keyIndexMap.get(adMobKeywords[1])];
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                String media = aResult[keyIndexMap.get(adMobKeywords[0])];
                String id = aResult[keyIndexMap.get(adMobKeywords[2])];
                String exposureNumber = aResult[keyIndexMap.get(adMobKeywords[14])];
                String clickNumber = aResult[keyIndexMap.get(adMobKeywords[10])];
                String expectIncome = aResult[keyIndexMap.get(adMobKeywords[11])];
                String cpm = aResult[keyIndexMap.get(adMobKeywords[13])];
                String clickWight = aResult[keyIndexMap.get(adMobKeywords[12])];

                String os = media;
                if (os.toLowerCase().contains("io")) {
                    os = "iOS";
                } else if (os.toLowerCase().contains("an")) {
                    os = "Android";
                }

                if (media.contains("(")) {
                    media = media.substring(0, media.indexOf("("));
                }

                String[] strings = media.split("-");
                media = strings[0];

                SSPADDto sspadDto = new SSPADDto();
                sspadDto.setDate(date);
                ADMaster adMaster = new ADMaster();
                adMaster.setId(id);
//                adMaster.setName(name);
                adMaster.setName("Google");
                adMaster.setExpectIncome(expectIncome);
                adMaster.setCpm(cpm);
                exposureNumber = exposureNumber.substring(0, exposureNumber.indexOf("."));
                adMaster.setExposureNumber(exposureNumber);
                clickNumber = clickNumber.substring(0, clickNumber.indexOf("."));
                adMaster.setClickNumber(clickNumber);
                adMaster.setClickWight(clickWight);

                sspadDto.setAdMaster(adMaster);
                sspadDto.setMedia(media);
                int spoilsScale = 100;
                if (media.contains("分动")) {
                    spoilsScale = 80;
                }

                if (media.contains("分动") || media.contains("V")) {
                    collectDto.setAdMaster(adMaster.getName());
                    collectDto.setApplication(media);

                    CollectDto collectDto1 = collectDtos.get(media);
                    String collectExpectIncome = collectDto1 == null ? "0.0" : collectDto1.getExpectIncome();
                    collectExpectIncome = collectExpectIncome == null ? "0.0" : collectExpectIncome;
                    String expectIncome1 = String.valueOf(Double.valueOf(collectExpectIncome) + Double.valueOf(expectIncome));
                    collectDto.setExpectIncome(expectIncome1);
                    Double spoilsScaleDouble = Double.valueOf(expectIncome);
                    if (spoilsScale != 100) {
                        spoilsScaleDouble = Double.valueOf(expectIncome) -
                                (Double.valueOf(expectIncome) * (spoilsScale / 100.0));
                    }

                    String collectSpoilsIncome = collectDto1 == null ? "0.0" : collectDto1.getSpoilsIncome();
                    collectSpoilsIncome = collectSpoilsIncome == null ? "0.0" : collectSpoilsIncome;
                    String spoilsIncome = String.valueOf(Double.valueOf(collectSpoilsIncome) + spoilsScaleDouble);
                    collectDto.setSpoilsIncome(String.valueOf(spoilsIncome));
                    collectDto.setSpoilsScale(String.valueOf(spoilsScale));
                    collectDtos.put(media, collectDto);
                }

                sspadDto.setSpoilsScale(spoilsScale);
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

    public Map<String, List<Object>> synthetic(Map<String, List<Object>> sspMap,
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

                SSPADDto sspAdDto = (SSPADDto) sspadDtos.get(i);
                String adSeat = sspAdDto.getAdSeat();
                boolean haveMedia = false;
                String adMasterMediaKey = null;

                for (String media : medias) {

                    haveMedia = media.contains(mediaKey);
                    if (media.length() < mediaKey.length()) {
                        haveMedia = mediaKey.contains(media);
                    }
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
                try {
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
                        int spoilsScale = adMasterAdDto.getSpoilsScale();

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

                        } catch (Exception ignored) {
                        }
                        try {
                            exposureDifferenceInt = (int) (new BigDecimal(
                                    (sspExposureNumber - adMasterExposureNumber) / sspExposureNumber
                            ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
                        } catch (Exception ignored) {
                        }


                        String clickDifferenceStr = clickDifferenceInt + "%";
                        String exposureDifferenceStr = exposureDifferenceInt + "%";

                        sspAdDto.setClickDifference(clickDifferenceStr);
                        sspAdDto.setSpoilsScale(spoilsScale);
                        sspAdDto.setExposureDifference(exposureDifferenceStr);
                        sspAdDto.setAdMaster(adMaster);
                        sspMap.get(mediaKey).set(i, sspAdDto);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return sspMap;
    }


    public Map<String, Map> getSortData(Map<String, List<Object>> sourceData) {
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

    /**
     * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行
     *
     * @param file       读取数据的源Excel
     * @param ignoreRows 读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1
     * @return 读出的Excel中数据的内容
     * @throws FileNotFoundException
     * @throws IOException
     */
    public  String[][] getData(File file, int ignoreRows)
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
                    values[columnIndex] = Utils.INSTANCE.rightTrim(value);
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
}
