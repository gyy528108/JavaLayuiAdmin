package com.lowi.admin.enums;

import lombok.Getter;

/**
 * AgencyEsSortEnum.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/7/14 15:47
 */
@Getter
public enum AgencyEsSortEnum {
    AGENCY_ES_SORT_ENUM_1(1, "id"),
    AGENCY_ES_SORT_ENUM_2(2, "mobile"),
    AGENCY_ES_SORT_ENUM_3(3, "lar_name"),
    AGENCY_ES_SORT_ENUM_4(4, "city_name"),
    AGENCY_ES_SORT_ENUM_5(5, "create_time"),
    AGENCY_ES_SORT_ENUM_6(6, "company_type"),
    AGENCY_ES_SORT_ENUM_7(7, "uscc"),
    AGENCY_ES_SORT_ENUM_8(8, "business_scope"),
    AGENCY_ES_SORT_ENUM_9(9, "address"),
    ;

    AgencyEsSortEnum(int type, String param) {
        this.type = type;
        this.param = param;
    }

    private int type;
    private String param;

    public static AgencyEsSortEnum getByType(int type) {
        for (AgencyEsSortEnum agencyEsSortEnum : values()) {
            if (type == agencyEsSortEnum.getType()) {
                return agencyEsSortEnum;
            }
        }
        return null;
    }
}
