package com.mipo.db.service;

import com.mipo.db.damain.entity.PartnerDisburse;

import java.util.List;

/**
 * @Author: lyy
 * @Description:
 * @Date: 2019-08-01 14:08
 */
public interface PartnerDisburseService {

    PartnerDisburse getDisburse(Long id);

    List<PartnerDisburse> getDisburseAll();
}
