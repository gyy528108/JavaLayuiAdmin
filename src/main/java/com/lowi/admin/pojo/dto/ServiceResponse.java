package com.lowi.admin.pojo.dto;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * com.lyx.ucenter.controller.api.ServiceResponse.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : hejw
 * @version : v1.0.0
 * @desc : 服务返回消息体包装类
 * @since : 2017/4/19 15:16
 */
public class ServiceResponse implements Serializable {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    public String version = "1";  //接口版本
    public Meta meta;             //返回消息体Mate信息
    public Data data;             //返回消息体Data信息


    /**
     * 接口返回消息体
     * @param version 接口版本
     * @param status 接口返回状态
     * @param server_time 服务器时间
     * @param account_id
     * @param cost 接口请求处理时长
     * @param errdata 接口错误时返回数据
     * @param errmsg 返回状态码对应信息
     * @param req_code 请求代码
     * @param has_more 是否有更多记录
     * @param num_items 总记录数
     * @param items 成功返回数据
     * @param alert_msg 自定义提示信息
     * @author hejw
     * @since 2017/4/19 15:16
     * @return ServiceResponse 服务返回消息体
     */
    public static ServiceResponse getInstance(String version, String status, String server_time, String account_id, String cost, String errdata, String errmsg, String req_code, String has_more, String num_items, Object items, String alert_msg) {
        ServiceResponse serviceResponse = new ServiceResponse();
        Meta meta = new Meta(status, server_time, account_id, cost, errdata, errmsg, req_code);
        Data data = new Data(has_more, num_items, items, alert_msg);
        serviceResponse.setVersion(version);
        serviceResponse.setMeta(meta);
        serviceResponse.setData(data);
        return serviceResponse;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    /**
     * 返回消息体Mate信息
     * @author : hejw
     * @since : 2017/4/19 15:16
     */
    public static class Meta {
        private String status = "";       //接口返回状态
        private String server_time = "";  //服务器时间
        private String account_id = "0";  //
        private String cost = "";         //接口请求处理时长
        private String errdata = "";      //接口错误时返回数据
        private String errmsg = "";       //返回状态码对应信息
        private String req_code = "";     //请求代码

        public Meta() {}

        public Meta(String status, String server_time, String account_id, String cost, String errdata, String errmsg, String req_code) {
            this.status = status;
            this.server_time = server_time;
            this.account_id = account_id;
            this.cost = cost;
            this.errdata = errdata;
            this.errmsg = errmsg;
            this.req_code = req_code;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getServer_time() {
            return server_time;
        }

        public void setServer_time(String server_time) {
            this.server_time = server_time;
        }

        public String getAccount_id() {
            return account_id;
        }

        public void setAccount_id(String account_id) {
            this.account_id = account_id;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public String getErrdata() {
            return errdata;
        }

        public void setErrdata(String errdata) {
            this.errdata = errdata;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        public String getReq_code() {
            return req_code;
        }

        public void setReq_code(String req_code) {
            this.req_code = req_code;
        }
    }

    /**
     * 返回消息体Data信息，封装服务端返回的数据
     * @author : hejw
     * @since : 2017/4/19 15:16
     */
    public static class Data {
        private String has_more = "";   //是否有更多记录
        private String num_items = "";  //总记录数
        private Object items = "";      //成功返回数据
        private String alert_msg = "";  //自定义提示信息

        public Data() {}

        public Data(String has_more, String num_items, Object items, String alert_msg) {
            this.has_more = has_more;
            this.num_items = num_items;
            this.items = items;
            this.alert_msg = alert_msg;
        }

        public String getHas_more() {
            return has_more;
        }

        public void setHas_more(String has_more) {
            this.has_more = has_more;
        }

        public String getNum_items() {
            return num_items;
        }

        public void setNum_items(String num_items) {
            this.num_items = num_items;
        }

        public Object getItems() {
            return items;
        }

        public void setItems(Object items) {
            this.items = items;
        }

        public String getAlert_msg() {
            return alert_msg;
        }

        public void setAlert_msg(String alert_msg) {
            this.alert_msg = alert_msg;
        }
    }
}
