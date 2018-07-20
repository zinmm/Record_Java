package com.zin.record.service;

import com.zin.record.dto.RecordDto;
import com.zin.record.vo.RecordVo;

import java.util.List;

/**
 * Created by zhujinming on 2017/11/27.
 */
public interface RecordService {

    /**
     * insert a record data
     * @param recordVo object
     * @return record id
     */
    String insertRecord(RecordVo recordVo);

    /**
     * delete a record data
     * @param id record id
     * @return delete successfully
     */
    boolean deleteRecord(String id);

    /**
     * query all record data
     * @return all record data
     */
    List<RecordDto> queryRecord();

    /**
     * update a record data
     * @param recordVo record vo
     * @return update successfully
     */
    boolean updateRecord(RecordVo recordVo);
}
