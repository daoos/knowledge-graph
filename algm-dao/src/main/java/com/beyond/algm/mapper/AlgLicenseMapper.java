package com.beyond.algm.mapper;

import com.beyond.algm.model.AlgLicense;
import java.util.List;

public interface AlgLicenseMapper {
    int deleteByPrimaryKey(String licSn);

    int insert(AlgLicense record);

    AlgLicense selectByPrimaryKey(String licSn);

    List<AlgLicense> selectAll();

    int updateByPrimaryKey(AlgLicense record);

    AlgLicense selectLicSn(String licName);

    List<AlgLicense> selectLicName(String licSn);
}