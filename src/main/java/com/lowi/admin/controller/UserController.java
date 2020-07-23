package com.lowi.admin.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.lowi.admin.dao.IpDataDao;
import com.lowi.admin.entity.IpData;
import com.lowi.admin.entity.User;
import com.lowi.admin.pojo.dto.UserDto;
import com.lowi.admin.service.UserService;
import com.lowi.admin.utils.LoginValidaUtils;
import com.lowi.admin.utils.Result;
import com.lowi.admin.validation.group.UserGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * UserController.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2019/12/19 10:48
 */
@RestController
@RequestMapping("user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;
    @Autowired
    IpDataDao ipDataDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/register")
    public Result register(@RequestBody @Validated(UserGroup.UserRegister.class) UserDto userDto, Errors errors) {
        if (errors.hasErrors()) {
            return Result.getError(errors);
        }
        return userService.registerUser(userDto);
    }

    @RequestMapping("/openAccount")
    public Result openAccount(@RequestBody @Validated(UserGroup.UserOpenAccount.class) UserDto userDto, Errors errors) {
        if (errors.hasErrors()) {
            return Result.getError(errors);
        }
        return userService.openAccount(userDto);
    }

    @RequestMapping("/getUserList")
    public Result getUserList(String token, Integer page, Integer limit, String phone, String userName) {
        Result responseResult = new Result();
        if (token == null) {
            responseResult.setCode(1);
            responseResult.setMsg("请登录");
            return responseResult;
        }
        return userService.getUserList(token, page, limit, phone, userName);
    }

    @RequestMapping("/editPassword")
    public Result editPassword(@RequestBody @Validated(UserGroup.editPassword.class) UserDto userDto, Errors errors) {
        if (errors.hasErrors()) {
            return Result.getError(errors);
        }
        return userService.editPassword(userDto);
    }

    @RequestMapping("/login")
    public Result<User> login(HttpServletRequest request, @RequestBody @Validated(UserGroup.UserLogin.class) UserDto userDto, Errors errors, HttpSession session) {
        if (errors.hasErrors()) {
            return Result.getError(errors);
        }
        String userIpAddr = request.getRemoteAddr();
        String ipAddr = getIpAddr(request);
        logger.info("ipAddr = " + ipAddr);
        logger.info("userIpAddr = " + userIpAddr);
        userDto.setLoginIp(userIpAddr);
        return userService.loginUser(userDto);
    }

    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.split(",").length > 0) {
            return ip.split(",")[0];
        }
        return ip;
    }

    @RequestMapping("/getUserInfo")
    public Result getUserInfo(@RequestBody UserDto userDto) {
        Result<UserDto> responseResult = new Result<UserDto>();
        if (userDto.getToken() == null) {
            responseResult.setCode(1);
            responseResult.setMsg("请登录");
            return responseResult;
        }
        boolean loginValida = LoginValidaUtils.loginValida(userDto.getToken());
        if (!loginValida) {
            responseResult.setCode(1);
            responseResult.setMsg("请登录");
            return responseResult;
        } else {
            responseResult.setCode(0);
            responseResult.setMsg("登录成功");
            return responseResult;
        }
    }

    @RequestMapping("/getUserLoginLog")
    public Result getUserLoginLog(String token, Integer page, Integer limit) {
        Result responseResult = new Result();
        if (token == null) {
            responseResult.setCode(1);
            responseResult.setMsg("请登录");
            return responseResult;
        }
        return userService.getUserLoginLog(token, page, limit);
    }

    @RequestMapping("/pageInit")
    public Result<Map<String, Object>> pageInit(@RequestBody UserDto userDto) {
        Result<Map<String, Object>> responseResult = new Result<Map<String, Object>>();
        if (userDto.getToken() == null) {
            responseResult.setCode(1);
            responseResult.setMsg("请登录");
            return responseResult;
        }
        return userService.pageInit(userDto.getToken());
    }


    @RequestMapping("/getInfo")
    public Result getInfo(Integer page, Integer limit, Integer sex, String city, String type) {
        System.out.println("page = " + page);
        System.out.println("limit = " + limit);
        System.out.println("sex = " + sex);
        System.out.println("city = " + city);
        System.out.println("type = " + type);
        JSONArray objects = JSONUtil.parseArray("[\n" +
                "    {\n" +
                "      \"id\": 10000,\n" +
                "      \"username\": \"user-0\",\n" +
                "      \"sex\": \"女\",\n" +
                "      \"city\": \"城市-0\",\n" +
                "      \"sign\": \"签名-0\",\n" +
                "      \"experience\": 255,\n" +
                "      \"logins\": 24,\n" +
                "      \"wealth\": 82830700,\n" +
                "      \"classify\": \"作家\",\n" +
                "      \"score\": 57\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 10001,\n" +
                "      \"username\": \"user-1\",\n" +
                "      \"sex\": \"男\",\n" +
                "      \"city\": \"城市-1\",\n" +
                "      \"sign\": \"签名-1\",\n" +
                "      \"experience\": 884,\n" +
                "      \"logins\": 58,\n" +
                "      \"wealth\": 64928690,\n" +
                "      \"classify\": \"词人\",\n" +
                "      \"score\": 27\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 10002,\n" +
                "      \"username\": \"user-2\",\n" +
                "      \"sex\": \"女\",\n" +
                "      \"city\": \"城市-2\",\n" +
                "      \"sign\": \"签名-2\",\n" +
                "      \"experience\": 650,\n" +
                "      \"logins\": 77,\n" +
                "      \"wealth\": 6298078,\n" +
                "      \"classify\": \"酱油\",\n" +
                "      \"score\": 31\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 10003,\n" +
                "      \"username\": \"user-3\",\n" +
                "      \"sex\": \"女\",\n" +
                "      \"city\": \"城市-3\",\n" +
                "      \"sign\": \"签名-3\",\n" +
                "      \"experience\": 362,\n" +
                "      \"logins\": 157,\n" +
                "      \"wealth\": 37117017,\n" +
                "      \"classify\": \"诗人\",\n" +
                "      \"score\": 68\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 10004,\n" +
                "      \"username\": \"user-4\",\n" +
                "      \"sex\": \"男\",\n" +
                "      \"city\": \"城市-4\",\n" +
                "      \"sign\": \"签名-4\",\n" +
                "      \"experience\": 807,\n" +
                "      \"logins\": 51,\n" +
                "      \"wealth\": 76263262,\n" +
                "      \"classify\": \"作家\",\n" +
                "      \"score\": 6\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 10005,\n" +
                "      \"username\": \"user-5\",\n" +
                "      \"sex\": \"女\",\n" +
                "      \"city\": \"城市-5\",\n" +
                "      \"sign\": \"签名-5\",\n" +
                "      \"experience\": 173,\n" +
                "      \"logins\": 68,\n" +
                "      \"wealth\": 60344147,\n" +
                "      \"classify\": \"作家\",\n" +
                "      \"score\": 87\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 10006,\n" +
                "      \"username\": \"user-6\",\n" +
                "      \"sex\": \"女\",\n" +
                "      \"city\": \"城市-6\",\n" +
                "      \"sign\": \"签名-6\",\n" +
                "      \"experience\": 982,\n" +
                "      \"logins\": 37,\n" +
                "      \"wealth\": 57768166,\n" +
                "      \"classify\": \"作家\",\n" +
                "      \"score\": 34\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 10007,\n" +
                "      \"username\": \"user-7\",\n" +
                "      \"sex\": \"男\",\n" +
                "      \"city\": \"城市-7\",\n" +
                "      \"sign\": \"签名-7\",\n" +
                "      \"experience\": 727,\n" +
                "      \"logins\": 150,\n" +
                "      \"wealth\": 82030578,\n" +
                "      \"classify\": \"作家\",\n" +
                "      \"score\": 28\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 10008,\n" +
                "      \"username\": \"user-8\",\n" +
                "      \"sex\": \"男\",\n" +
                "      \"city\": \"城市-8\",\n" +
                "      \"sign\": \"签名-8\",\n" +
                "      \"experience\": 951,\n" +
                "      \"logins\": 133,\n" +
                "      \"wealth\": 16503371,\n" +
                "      \"classify\": \"词人\",\n" +
                "      \"score\": 14\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 10009,\n" +
                "      \"username\": \"user-9\",\n" +
                "      \"sex\": \"女\",\n" +
                "      \"city\": \"城市-9\",\n" +
                "      \"sign\": \"签名-9\",\n" +
                "      \"experience\": 484,\n" +
                "      \"logins\": 25,\n" +
                "      \"wealth\": 86801934,\n" +
                "      \"classify\": \"词人\",\n" +
                "      \"score\": 75\n" +
                "    }\n" +
                "  ]");
        Result result = new Result();
        result.setCode(0);
        result.setMsg("成功");
        result.setCount(9);
        result.setData(objects);
        return result;
    }

    @RequestMapping("/logout")
    public Result logout(@RequestBody UserDto userDto) {
        return userService.logout(userDto.getToken());
    }

    @RequestMapping("/sendEmailMsg")
    public Result sendEmailMsg(String email) {
        return userService.sendEmailMsg(email);
    }

    public static void main(String[] args) {
        File file = new File("E:/11.txt");
    }


    //    @RequestMapping("/insertIp")
    public void insertIp() {
        File file = new File("D:/GoogleDown/ip.txt");
        txt2String(file);
    }

    static int count = 0;
    static int insert = 0;

    @Transactional
    public void txt2String(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            List<IpData> ipDataList = new ArrayList<>();
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                String[] split = s.split(",");
                count++;

                System.out.println("省份：" + split[3] + " 城市：" + split[4] + " 运营商：" + split[5] + " ip ：" + split[6] + "-" + split[7] + "   count=" + count);

                IpData ipData = new IpData();
                for (int i = 0; i < split.length; i++) {
                    ipData.setProvince(split[3]);
                    ipData.setCity(split[4]);
                    ipData.setOperator(split[5]);
                    ipData.setIpStart(split[6]);
                    ipData.setIpEnd(split[7]);
                    if (split[6] != null) {
                        String[] ip = split[6].split("\\.");
                        String s1 = ipToStr(ip[0]);
                        String s2 = ipToStr(ip[1]);
                        String s3 = ipToStr(ip[2]);
                        String s4 = ipToStr(ip[3]);
                        Long aLong = Long.valueOf(s1 + s2 + s3 + s4);
                        ipData.setIpScopeStart(aLong);
                    }
                    if (split[7] != null) {
                        String[] ip = split[7].split("\\.");
                        String s1 = ipToStr(ip[0]);
                        String s2 = ipToStr(ip[1]);
                        String s3 = ipToStr(ip[2]);
                        String s4 = ipToStr(ip[3]);
                        Long aLong = Long.valueOf(s1 + s2 + s3 + s4);
                        ipData.setIpScopeEnd(aLong);
                    }
                    ipData.setCreateTime(new Date());
                }
                ipDataList.add(ipData);
                if (ipDataList.size() >= 2000) {
                    insert++;
                    ipDataDao.batchInert(ipDataList);
                    ipDataList = new ArrayList<>();
                    System.out.println("insert = " + insert + "  list=" + ipDataList);
                }
            }
            insert++;
            ipDataDao.batchInert(ipDataList);
            System.out.println("insert = " + insert + "  list=" + ipDataList);
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String ipToStr(String num) {
        if (num.length() == 1) {
            return "00" + num;
        }
        if (num.length() == 2) {
            return "0" + num;
        }
        return num;
    }
}
