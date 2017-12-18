package com.beyond.algm.mapper;

import com.beyond.algm.model.AlgModuleUsage;
import java.util.List;

public interface AlgModuleUsageMapper {
    int deleteByPrimaryKey(String usgSn);

    int insert(AlgModuleUsage record);

    AlgModuleUsage selectByPrimaryKey(String usgSn);

    List<AlgModuleUsage> selectAll();

    int updateByPrimaryKey(AlgModuleUsage record);

    //查取该算法目前人收藏
    Long selectStarCnt(String modSn);

    //收藏时，更新个数。
    int updateByModSn(AlgModuleUsage record);
}