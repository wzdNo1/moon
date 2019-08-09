package com.mipo.db.service.impl;

import com.mipo.core.exception.RException;
import com.mipo.core.exception.ResponseCodeEnum;
import com.mipo.db.damain.entity.PartnerDisburse;
import com.mipo.db.dao.PartnerDisburseMapper;
import com.mipo.db.service.PartnerDisburseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: lyy
 * @Description:
 * @Date: 2019-08-01 14:09
 */
@Service
public class PartnerDisburseServiceImpl implements PartnerDisburseService {

    @Value("${env}")
    private String litudou;

    @Autowired
    private PartnerDisburseMapper partnerDisburseMapper;

    @Override
    public PartnerDisburse getDisburse(Long id) {
        System.out.println(litudou);
        return partnerDisburseMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<PartnerDisburse> getDisburseAll() {
        return partnerDisburseMapper.getDisburseAll();
    }
}
