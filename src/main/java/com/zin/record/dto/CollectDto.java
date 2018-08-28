package com.zin.record.dto;

/**
 * Created by zhujinming on 2018/7/26.
 */
public class CollectDto {

    private String application;
    private String adMaster;
    private String spoilsScale;
    private String expectIncome;
    private String spoilsIncome;

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getAdMaster() {
        return adMaster;
    }

    public void setAdMaster(String adMaster) {
        this.adMaster = adMaster;
    }

    public String getExpectIncome() {
        return expectIncome;
    }

    public void setExpectIncome(String expectIncome) {
        this.expectIncome = expectIncome;
    }

    public String getSpoilsIncome() {
        return spoilsIncome;
    }

    public void setSpoilsIncome(String spoilsIncome) {
        this.spoilsIncome = spoilsIncome;
    }

    public String getSpoilsScale() {
        return spoilsScale;
    }

    public void setSpoilsScale(String spoilsScale) {
        this.spoilsScale = spoilsScale;
    }


}
