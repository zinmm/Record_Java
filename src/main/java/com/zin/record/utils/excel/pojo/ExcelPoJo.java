package com.zin.record.utils.excel.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhujinming on 2018/7/10.
 */
public class ExcelPoJo implements Serializable {

    private String title;
    private String[] rowName;
    private List<Object[]> dataList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getRowName() {
        return rowName;
    }

    public void setRowName(String[] rowName) {
        this.rowName = rowName;
    }

    public List<Object[]> getDataList() {
        return dataList;
    }

    public void setDataList(List<Object[]> dataList) {
        this.dataList = dataList;
    }
}
