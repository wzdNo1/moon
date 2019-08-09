package com.mipo.db.damain.entity;

import java.io.Serializable;
import java.util.Date;

public class PartnerDisburse implements Serializable {
    private Long id;

    private Long partnerId;

    private String partnerNo;

    private String partnerAppNo;

    private String partnerUniqueId;

    private String thirdUniqueId;

    private String bankCode;

    private String bankAccountNumber;

    private String bankAccountHolderName;

    private String disburseDescription;

    private Long disburseMoney;

    private String customerMobileno;

    private String customerEmail;

    private Integer disburseType;

    private String channelType;

    private Integer status;

    private Integer statusNotify;

    private String remark;

    private String bizRemark;

    private String channelRemark;

    private Byte type;

    private Date createTime;

    private Date updateTime;

    private Date resultTime;

    private Byte manualHandler;

    private String statusMessage;

    private Long batchId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerNo() {
        return partnerNo;
    }

    public void setPartnerNo(String partnerNo) {
        this.partnerNo = partnerNo == null ? null : partnerNo.trim();
    }

    public String getPartnerAppNo() {
        return partnerAppNo;
    }

    public void setPartnerAppNo(String partnerAppNo) {
        this.partnerAppNo = partnerAppNo == null ? null : partnerAppNo.trim();
    }

    public String getPartnerUniqueId() {
        return partnerUniqueId;
    }

    public void setPartnerUniqueId(String partnerUniqueId) {
        this.partnerUniqueId = partnerUniqueId == null ? null : partnerUniqueId.trim();
    }

    public String getThirdUniqueId() {
        return thirdUniqueId;
    }

    public void setThirdUniqueId(String thirdUniqueId) {
        this.thirdUniqueId = thirdUniqueId == null ? null : thirdUniqueId.trim();
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber == null ? null : bankAccountNumber.trim();
    }

    public String getBankAccountHolderName() {
        return bankAccountHolderName;
    }

    public void setBankAccountHolderName(String bankAccountHolderName) {
        this.bankAccountHolderName = bankAccountHolderName == null ? null : bankAccountHolderName.trim();
    }

    public String getDisburseDescription() {
        return disburseDescription;
    }

    public void setDisburseDescription(String disburseDescription) {
        this.disburseDescription = disburseDescription == null ? null : disburseDescription.trim();
    }

    public Long getDisburseMoney() {
        return disburseMoney;
    }

    public void setDisburseMoney(Long disburseMoney) {
        this.disburseMoney = disburseMoney;
    }

    public String getCustomerMobileno() {
        return customerMobileno;
    }

    public void setCustomerMobileno(String customerMobileno) {
        this.customerMobileno = customerMobileno == null ? null : customerMobileno.trim();
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail == null ? null : customerEmail.trim();
    }

    public Integer getDisburseType() {
        return disburseType;
    }

    public void setDisburseType(Integer disburseType) {
        this.disburseType = disburseType;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType == null ? null : channelType.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatusNotify() {
        return statusNotify;
    }

    public void setStatusNotify(Integer statusNotify) {
        this.statusNotify = statusNotify;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getBizRemark() {
        return bizRemark;
    }

    public void setBizRemark(String bizRemark) {
        this.bizRemark = bizRemark == null ? null : bizRemark.trim();
    }

    public String getChannelRemark() {
        return channelRemark;
    }

    public void setChannelRemark(String channelRemark) {
        this.channelRemark = channelRemark == null ? null : channelRemark.trim();
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getResultTime() {
        return resultTime;
    }

    public void setResultTime(Date resultTime) {
        this.resultTime = resultTime;
    }

    public Byte getManualHandler() {
        return manualHandler;
    }

    public void setManualHandler(Byte manualHandler) {
        this.manualHandler = manualHandler;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage == null ? null : statusMessage.trim();
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }
}