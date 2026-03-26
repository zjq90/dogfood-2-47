package com.mall.mapper;

import com.mall.entity.LogisticsTrace;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface LogisticsTraceMapper {
    
    List<LogisticsTrace> selectByLogisticsId(Long logisticsId);
    
    int insert(LogisticsTrace trace);
    
    int batchInsert(List<LogisticsTrace> traces);
    
    int deleteByLogisticsId(Long logisticsId);
}
