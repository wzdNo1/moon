package com.mipo.db.dao;

import com.mipo.db.damain.entity.PartnerDisburse;

import java.util.List;

public interface PartnerDisburseMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(PartnerDisburse record);

    PartnerDisburse selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PartnerDisburse record);

    int updateByPrimaryKey(PartnerDisburse record);

    List<PartnerDisburse> getDisburseAll();
}