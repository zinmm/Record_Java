package com.zin.record.dto;

import java.util.Date;

/**
 * Created by zhujinming on 2018/7/30.
 */
public class DistributeDto {

    private Date data;
    private String os;

    private String region;

    private String showNumber;

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getShowNumber() {
        return showNumber;
    }

    public void setShowNumber(String showNumber) {
        this.showNumber = showNumber;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
