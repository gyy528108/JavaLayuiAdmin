package com.lowi.admin.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lowi.admin.dao.IpDataDao;
import com.lowi.admin.dao.LoginLogDao;
import com.lowi.admin.dao.UserDao;
import com.lowi.admin.entity.IpData;
import com.lowi.admin.entity.LoginLog;
import com.lowi.admin.entity.User;
import com.lowi.admin.pojo.dto.UserDto;
import com.lowi.admin.pojo.vo.UserVo;
import com.lowi.admin.utils.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * UserService.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2019/12/19 11:16
 */
@Service
public class UserService {
    private Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private IpDataDao ipDataDao;
    @Autowired
    private LoginLogDao loginLogDao;

    public Result registerUser(UserDto userDto) {
        Result responseResult = new Result();
        String email = stringRedisTemplate.opsForValue().get(userDto.getEmail() + "_email");
        if (email == null || "".equals(email)) {
            responseResult.setCode(1);
            responseResult.setMsg("请发送验证码");
            return responseResult;
        }
        if (!email.equals(userDto.getValidaCode())) {
            responseResult.setCode(1);
            responseResult.setMsg("请输入正确的验证码");
            return responseResult;
        }

        User user = new User();
        user.setMobile(userDto.getMobile());
        User userPhone = userDao.selectOne(new QueryWrapper<>(user));
        if (userPhone != null) {
            responseResult.setCode(1);
            responseResult.setMsg("手机号已存在");
            return responseResult;
        }
        int insert = 0;
        try {
            user.setEmail(userDto.getEmail());
            user.setCreateTime(new Date());
            user.setIsDel(false);
            user.setUsername(userDto.getUserName());
            String md5Hex = Md5Utils.md5Hex(userDto.getPassword());
            user.setPassword(md5Hex);
            insert = userDao.insert(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (insert == 0) {
            responseResult.setCode(1);
            responseResult.setMsg("系统异常，请重试");
            return responseResult;
        }
        responseResult.setCode(0);
        responseResult.setMsg("注册成功");
        return responseResult;
    }

    @Transactional
    public Result loginUser(UserDto userDto) {
        Result responseResult = new Result();
        String md5Hex = Md5Utils.md5Hex(userDto.getPassword());
        User userPo = userDao.loginUser(userDto.getMobile(), md5Hex);
        if (userPo == null) {
            responseResult.setCode(1);
            responseResult.setMsg("用户不存在或密码错误");
            return responseResult;
        }
        LoginLog loginLog = new LoginLog();
        String[] split = userDto.getLoginIp().split("\\.");
        if (split.length > 0) {
            String ipAddr = "";
            StringBuilder ip = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                ip.append(ipToStr(split[i]));
            }
            Long ipNum = Long.valueOf(ip.toString());
            List<IpData> ipAddrList = ipDataDao.getIpAddr(ipNum);
            if (ipAddrList.size() > 0) {
                String province = ipAddrList.get(0).getProvince();
                String city = ipAddrList.get(0).getCity();
                if (province != null) {
                    ipAddr = province;
                }
                if (city != null && !"".equals(city)) {
                    ipAddr = ipAddr + "--" + city;
                }
            }
            loginLog.setLoginAddr(ipAddr);
            logger.info("登录地址-----------------" + ipAddr);
        }
        loginLog.setLoginTime(new Date());
        loginLog.setLoginIp(userDto.getLoginIp());
        loginLog.setUserId(userPo.getId());
        loginLogDao.insert(loginLog);

        userDto.setId(userPo.getId());
        userDto.setLastLoginIp(userPo.getLoginIp());
        userDao.updateLoginCount(userDto);
        responseResult.setCode(0);
        responseResult.setMsg("登录成功");
        long currentSeconds = DateUtil.currentSeconds();
        String token = TokenUtils.createToken(userDto.getId(), String.valueOf(currentSeconds));
        String str = JSONUtil.toJsonStr(userPo);
        stringRedisTemplate.opsForValue().set(token, str, 30, TimeUnit.MINUTES);
        stringRedisTemplate.opsForValue().set("userValida_" + userPo.getId(), String.valueOf(currentSeconds), 30, TimeUnit.MINUTES);

        Map<String, Object> map = new HashMap<>((int) (1 / 0.75) + 1);
        map.put("token", token);
        responseResult.setData(map);
        return responseResult;
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

    public Result sendEmailMsg(String email) {
        Result result = new Result();
        String s = stringRedisTemplate.opsForValue().get(email + "_increment");
        if (s != null) {
            Long increment = stringRedisTemplate.boundValueOps(email + "_increment").increment(1);
            if (increment > 4) {
                result.setCode(1);
                result.setMsg("请勿频繁发送（4/小时）");
                return result;
            }
        }
        String numbers = RandomUtil.randomNumbers(6);
        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE>" + "<div bgcolor='#f1fcfa'  style='border:1px solid #d9f4ee; font-size:14px; line-height:22px; color:#005aa0;padding-left:1px;padding-top:5px;padding-bottom:5px;text-align:center;'>"
                + "<div style='width:950px;font-family:arial;'>欢迎使用Lowi后台管理服务，您的注册码为：<br/><h2 style='color:green'>" + numbers + "</h2><br/>本邮件由系统自动发出，请勿回复。<br/>感谢您的使用。<br/>祝您生活愉快。</div>"
                + "</div>");
        boolean sendMail = SendMailUtils.sendMail(email, "Lowi注册验证", sb.toString());
        if (sendMail) {
            stringRedisTemplate.opsForValue().set(email + "_email", numbers, 15, TimeUnit.MINUTES);
            if (s == null) {
                stringRedisTemplate.opsForValue().set(email + "_increment", "1", 1, TimeUnit.HOURS);
            }
            result.setCode(0);
            return result;
        } else {
            result.setCode(1);
            result.setMsg("请检验邮箱是否正确");
            return result;
        }
    }

    public static void main(String[] args) {
        String numbers = RandomUtil.randomNumbers(6);
        System.out.println("numbers = " + numbers);

        for (int i = 0; i < 10; i++) {
            String str = RandomUtil.randomStringWithoutStr(6, "1l0Oo2zZ");
            String str2 = RandomUtil.randomString(6);
            System.out.println("str = " + str);
            System.out.println("str2 = " + str2);
        }

        String ss = "a8a02c55-9e41-465b-a4e6-5d7d9c13d0e0";
    }

    public Result getUserLoginLog(String token, Integer page, Integer limit) {
        String userInfo = stringRedisTemplate.opsForValue().get(token);
        User user = JSONUtil.toBean(userInfo, User.class);
        QueryWrapper<LoginLog> queryWrapper = new QueryWrapper<>();
        Page<LoginLog> pageInfo = new Page<>(page, limit);
        queryWrapper.select().eq("user_id", user.getId()).orderByDesc("login_time");
        IPage<LoginLog> loginLogIPage = loginLogDao.selectPage(pageInfo, queryWrapper);
        Result responseResult = new Result();
        responseResult.setCode(0);
        responseResult.setMsg("获取成功");
        responseResult.setData(loginLogIPage.getRecords());
        responseResult.setCount((int) loginLogIPage.getTotal());
        return responseResult;
    }

    public Result openAccount(UserDto userDto) {
        Result responseResult = new Result();
        String userInfo = stringRedisTemplate.opsForValue().get(userDto.getToken());
        User userVo = JSONUtil.toBean(userInfo, User.class);
        if (userVo.getParentId() > 0) {
            responseResult.setMsg("无权限");
            return responseResult;
        }
        String value = String.valueOf(System.currentTimeMillis() + 10 * 1000);
        boolean lock = LockUtil.lock(userDto.getMobile(),value);
        if(!lock){
            responseResult.setCode(1);
            responseResult.setMsg("请重试");
            return responseResult;
        }
        try {
            User user = new User();
            user.setMobile(userDto.getMobile());
            User userPhone = userDao.selectOne(new QueryWrapper<>(user));
            if (userPhone != null) {
                responseResult.setCode(1);
                responseResult.setMsg("手机号已存在");
                return responseResult;
            }
            int insert = 0;
            try {
                user.setParentId(userVo.getId());
                user.setEmail(userDto.getEmail());
                user.setCreateTime(new Date());
                user.setIsDel(false);
                user.setUsername(userDto.getUserName());
                String md5Hex = Md5Utils.md5Hex(userDto.getPassword());
                user.setPassword(md5Hex);
                insert = userDao.insert(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (insert == 0) {
                responseResult.setCode(1);
                responseResult.setMsg("系统异常，请重试");
                return responseResult;
            }
        } finally {
            LockUtil.unlock(userDto.getMobile(),value);
        }
        responseResult.setCode(0);
        responseResult.setMsg("开户成功");
        return responseResult;
    }

    public Result editPassword(UserDto userDto) {
        Result responseResult = new Result();
        String userInfo = stringRedisTemplate.opsForValue().get(userDto.getToken());
        User userVo = JSONUtil.toBean(userInfo, User.class);
        String md5Hex = Md5Utils.md5Hex(userDto.getPassword());
        if (!userVo.getPassword().equals(md5Hex)) {
            responseResult.setCode(1);
            responseResult.setMsg("原密码错误");
            return responseResult;
        }
        if (!userDto.getNewPassword().equals(userDto.getAgainPassword())) {
            responseResult.setCode(1);
            responseResult.setMsg("新密码请保持一致");
            return responseResult;
        }
        User newUser = new User();
        newUser.setId(userVo.getId());
        newUser.setPassword(Md5Utils.md5Hex(userDto.getNewPassword()));
        int update = userDao.updateById(newUser);
        if (update == 0) {
            responseResult.setCode(1);
            responseResult.setMsg("系统异常，请重试");
            return responseResult;
        }
        responseResult.setCode(0);
        responseResult.setMsg("修改成功");
        stringRedisTemplate.delete(userDto.getToken());
        stringRedisTemplate.delete("userValida_" + userVo.getId());
        return responseResult;
    }

    public Result logout(String token) {
        String userInfo = stringRedisTemplate.opsForValue().get(token);
        User userVo = JSONUtil.toBean(userInfo, User.class);
        stringRedisTemplate.delete(token);
        stringRedisTemplate.delete("userValida_" + userVo.getId());
        Result responseResult = new Result();
        responseResult.setCode(0);
        responseResult.setMsg("成功");
        return responseResult;
    }

    public Result<Map<String, Object>> pageInit(String token) {
        Result<Map<String, Object>> responseResult = new Result();
        String userInfo = stringRedisTemplate.opsForValue().get(token);
        if (userInfo == null) {
            responseResult.setCode(1);
            responseResult.setMsg("请登录");
            return responseResult;
        }
        User userVo = JSONUtil.toBean(userInfo, User.class);
        Map<String, Object> map = new HashMap<>();

        map.put("name", userVo.getUsername());
        map.put("headImg", Objects.toString(userVo.getHeadImg(), "cssjs/images/logo.png"));
        if (userVo.getParentId() == 0) {
            map.put("admin", true);
            responseResult.setCode(0);
            responseResult.setData(map);
            responseResult.setMsg("成功");
            return responseResult;
        } else {
            map.put("admin", false);
            responseResult.setCode(0);
            responseResult.setData(map);
            responseResult.setMsg("成功");
            return responseResult;
        }
    }

    public Result getUserList(String token, Integer page, Integer limit, String phone, String userName) {
        String userInfo = stringRedisTemplate.opsForValue().get(token);
        User userVo = JSONUtil.toBean(userInfo, User.class);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (phone != null) {
            queryWrapper.like("mobile", phone);
        }
        if (userName != null) {
            queryWrapper.like("username", userName);
        }
        queryWrapper.select().eq("parent_id", userVo.getId()).orderByDesc("create_time");
        Page<User> pageInfo = new Page<>(page, limit);
        IPage<User> userIPage = userDao.selectPage(pageInfo, queryWrapper);
        List<UserVo> userVos = userIPage.getRecords().stream().map(user -> UserVo.fromVo(user)).collect(Collectors.toList());
        Result responseResult = new Result();
        responseResult.setCode(0);
        responseResult.setMsg("获取成功");
        responseResult.setData(userVos);
        responseResult.setCount((int) userIPage.getTotal());
        return responseResult;
    }
}
