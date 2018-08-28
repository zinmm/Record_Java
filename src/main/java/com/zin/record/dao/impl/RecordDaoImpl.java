package com.zin.record.dao.impl;

import com.zin.record.dao.RecordDao;
import com.zin.record.dto.RecordDto;
import com.zin.record.vo.RecordVo;

import java.util.List;

/**
 * record dao impl
 * Created by zhujinming on 2017/11/27.
 */
public class RecordDaoImpl implements RecordDao {

    @Override
    public String insertRecord(RecordVo recordVo) {
        return null;
    }

    @Override
    public boolean deleteRecord(String id) {
        return false;
    }

    @Override
    public List<RecordDto> queryRecord() {
        return null;
    }

    @Override
    public boolean updateRecord(RecordVo recordVo) {
        return false;
    }
}
