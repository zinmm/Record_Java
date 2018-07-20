package com.zin.record.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhujinming on 2018/7/10.
 */
public class SSPADDto implements Serializable {

    // 时间
    private Date date;
    // 广告主
    private ADMaster adMaster;
    // 广告位
    private String adSeat;
    // 媒体位
    private String media;
    // 操作系统
    private String os;
    // 分成后收入
    private String spoilsIncome;
    // 请求数
    private String requestNumber;
    // 曝光数
    private String exposureNumber;
    // 点击数
    private String clickNumber;
    // 点击率
    private String clickWight;
    // 填充率
    private String fillWight;
    // 曝光差异
    private String exposureDifference;
    // 点击差异
    private String clickDifference;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ADMaster getAdMaster() {
        return adMaster;
    }

    public void setAdMaster(ADMaster adMaster) {
        this.adMaster = adMaster;
    }

    public String getAdSeat() {
        return adSeat;
    }

    public void setAdSeat(String adSeat) {
        this.adSeat = adSeat;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getSpoilsIncome() {
        return spoilsIncome;
    }

    public void setSpoilsIncome(String spoilsIncome) {
        this.spoilsIncome = spoilsIncome;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
    }

    public String getExposureNumber() {
        return exposureNumber;
    }

    public void setExposureNumber(String exposureNumber) {
        this.exposureNumber = exposureNumber;
    }

    public String getClickNumber() {
        return clickNumber;
    }

    public void setClickNumber(String clickNumber) {
        this.clickNumber = clickNumber;
    }

    public String getClickWight() {
        return clickWight;
    }

    public void setClickWight(String clickWight) {
        this.clickWight = clickWight;
    }

    public String getFillWight() {
        return fillWight;
    }

    public void setFillWight(String fillWight) {
        this.fillWight = fillWight;
    }

    public String getExposureDifference() {
        return exposureDifference;
    }

    public void setExposureDifference(String exposureDifference) {
        this.exposureDifference = exposureDifference;
    }

    public String getClickDifference() {
        return clickDifference;
    }

    public void setClickDifference(String clickDifference) {
        this.clickDifference = clickDifference;
    }
}
