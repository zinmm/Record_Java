package com.zin.record.dto;

import java.io.Serializable;

/**
 * Created by zhujinming on 2018/7/10.
 */
public class ADMaster implements Serializable {

    private String id;
    // 请求数
    private String requestNumber;
    // 广告主
    private String name;
    // 曝光（广告主）
    private String exposureNumber;
    // 点击（广告主）
    private String clickNumber;
    // 预计收入（广告主）
    private String expectIncome;
    // 千次展示收入（广告主）
    private String cpm;
    // 点击率 (广告主)
    private String clickWight;
    // 曝光率
    private String exposureWight;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getExpectIncome() {
        return expectIncome;
    }

    public void setExpectIncome(String expectIncome) {
        this.expectIncome = expectIncome;
    }

    public String getCpm() {
        return cpm;
    }

    public void setCpm(String cpm) {
        this.cpm = cpm;
    }

    public String getClickWight() {
        return clickWight;
    }

    public void setClickWight(String clickWight) {
        this.clickWight = clickWight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
    }

    public String getExposureWight() {
        return exposureWight;
    }

    public void setExposureWight(String exposureWight) {
        this.exposureWight = exposureWight;
    }
}
