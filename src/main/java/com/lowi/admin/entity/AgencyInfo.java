package com.lowi.admin.entity;

import lombok.Data;

import java.util.Date;

@Data
public class AgencyInfo {
    private Integer id;    //
    private Integer phpAgencyId;    //
    private String name;
    private String mobile;
    private String provinceName;
    private String cityName;
    private String areaName;
    private String uscc;
    private String larName;
    private String originalCompanyType;
    private String companyType;
    private String companyEstablishDate;
    private String originalRegisteredCapital;
    private String registeredCapital;
    private String address;
    private String email;
    private String businessScope;
    private String website;
    private String otherMobile;
    private Integer isDel;
    private Date createTime;
    private String companyIds;
    private Integer versionNo;
    private String industry;
    private String brands;
    private Integer salemanNum;
    private Double annualTurnover;
}
