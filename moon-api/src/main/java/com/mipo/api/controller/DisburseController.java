package com.mipo.api.controller;

import com.github.pagehelper.PageHelper;
import com.mipo.core.annotation.NonHandle;
import com.mipo.core.exception.RException;
import com.mipo.core.exception.ResponseCodeEnum;
import com.mipo.db.config.DsType;
import com.mipo.db.config.DsTypeEnum;
import com.mipo.db.damain.entity.PartnerDisburse;
import com.mipo.db.service.PartnerDisburseService;
import com.mipo.db.util.PageBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: lyy
 * @Description:
 * @Date: 2019-08-01 14:11
 */
@Api("转账相关接口")
@RestController
public class DisburseController {

    @Autowired
    private PartnerDisburseService partnerDisburseService;

    @NonHandle(response = false)
    @GetMapping(value = "getOne", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "获取转账数据", notes = "根据ID获取单个转账数据")
    @ApiImplicitParam(name = "id", value = "放款Id", dataType = "Integer", paramType = "query")
    public PartnerDisburse getDisburse(Long id) {
        System.err.println(id);
        return partnerDisburseService.getDisburse(id);
    }

    @NonHandle(response = false)
    @GetMapping(value = "getAll")
    @DsType(type = DsTypeEnum.READ)
    @ApiOperation(value = "获取转账数据", notes = "获取所有转账数据带分页的")
    public PageBean getDisburseAll() {
        PageHelper.startPage(1,3);
        System.out.println();
        List<PartnerDisburse> disburseAll = partnerDisburseService.getDisburseAll();
        //PageInfo<PartnerDisburse> partnerDisbursePageInfo = new PageInfo<>(disburseAll);
        return new PageBean(disburseAll);
    }
    @NonHandle(response = false)
    @GetMapping(value = "index")
    @ApiOperation(value = "模拟异常", notes = "模拟异常返回")
    public String index() {
        throw new RException(ResponseCodeEnum.PARAMTER_ERROR);

    }

}
