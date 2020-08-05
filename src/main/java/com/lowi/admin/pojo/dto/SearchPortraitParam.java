package com.lowi.admin.pojo.dto;

import lombok.Data;

import java.util.List;

/**
 * SearchPortraitParam.java
 * ==============================================
 * Copy right 2015-2018  by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @desc :  画像 - 任务 - 接口参数
 * @author: wangr
 * @version: v2.0.0
 * @since: 2019/5/13 16:48
 */
@Data
public class SearchPortraitParam {


    private Integer page;       //页数
    private Integer pageSize;   //每页条数
    private Integer id;          //任务ID
    private Integer companyId;  //企业ID
    private Integer userId;     //用户ID
    private String taskName;    //任务名称
    private Integer isAdvanced;  //是否高级模式:0.不是;1.是
    private String remark; //备注
    private String updateDate; //更新日期
    private Integer isDel; //是否删除:0.已删除;1.正常
    private Integer taskStatus; //任务状态:1.未开始;2.进行中;3.完成;4.取消
    private Integer isTestCompany; //是否为测试企业:0.否;1.是
    private String userName; //用户名称
    private String mobile; //用户手机号
    private String copyFlag; //复制标识 1复制
    private Integer taskType; //任务类型：1.联通建模任务;2.经销商建模任务;3.人才建模任务'
    private List pronviceList; //省份集合
    private List cityList; //城市标识
    private List establishDateList; //成立年限集合
    private List companyTypeList; //企业类型集合
    private List businessScopeList; //经营范围集合
    private Integer userFlag; //前后台标识（隐藏字段用） 0.后台管理系统；1.平台用户
    private String condition; //拼接脚本
    private String searchStr; //模糊搜索关键字
    private Integer reqSource; //1web端,2手机端
    private Integer checkSqlFlag; //是否校验sql可行性 0 不校验,1 校验sql用
    private Integer isPage; //是否分页 1 不分页,2 分页
    private Integer taskId; //es使用的任务id

    private Integer isSelectAll; //是否全选：1全选，0反选
    private String ids;           //多个AI外呼记录ID，以","分割
    private String invertIds;    //反选：多个AI外呼记录ID，以","分割（即剔除的ID）
    private List<Object[]> nodes;//高级建模节点

    private String tableName;

    private Integer miniProgramType;                        //小程序类型 1、welink 2、飞书 3、企业微信 4、微信 5、钉钉

    private Integer isSort = 0;                                 //小程序是否排序 默认=0 1.排序

    private String miniProgramSort;                         //小程序正序倒序

    @Override
    public String toString() {
        return "SearchPortraitParam{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", id=" + id +
                ", companyId=" + companyId +
                ", userId=" + userId +
                ", taskName='" + taskName + '\'' +
                ", isAdvanced=" + isAdvanced +
                ", remark='" + remark + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", isDel=" + isDel +
                ", taskStatus=" + taskStatus +
                ", isTestCompany=" + isTestCompany +
                ", userName='" + userName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", copyFlag='" + copyFlag + '\'' +
                ", taskType=" + taskType +
                ", pronviceList=" + pronviceList +
                ", cityList=" + cityList +
                ", establishDateList=" + establishDateList +
                ", companyTypeList=" + companyTypeList +
                ", businessScopeList=" + businessScopeList +
                ", userFlag=" + userFlag +
                ", condition='" + condition + '\'' +
                ", searchStr='" + searchStr + '\'' +
                ", reqSource=" + reqSource +
                ", checkSqlFlag=" + checkSqlFlag +
                ", isPage=" + isPage +
                ", taskId=" + taskId +
                ", isSelectAll=" + isSelectAll +
                ", ids='" + ids + '\'' +
                ", invertIds='" + invertIds + '\'' +
                ", nodes=" + nodes +
                ", tableName='" + tableName + '\'' +
                ", miniProgramType=" + miniProgramType +
                ", isSort=" + isSort +
                ", miniProgramSort='" + miniProgramSort + '\'' +
                '}';
    }
}
