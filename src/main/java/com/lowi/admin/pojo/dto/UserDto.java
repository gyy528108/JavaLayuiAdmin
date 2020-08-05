package com.lowi.admin.pojo.dto;

import com.lowi.admin.validation.group.UserGroup;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * UserDto.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2019/12/19 10:52
 */
@Data
public class UserDto {
    @NotNull(groups = {UserGroup.UserRegister.class, UserGroup.UserOpenAccount.class}, message = "用户名称不能为空")
    @Size(groups = {UserGroup.UserRegister.class, UserGroup.UserOpenAccount.class}, min = 1, message = "用户名称1-6个字符")
    @Size(groups = {UserGroup.UserRegister.class, UserGroup.UserOpenAccount.class}, max = 6, message = "用户名称1-6个字符")
    private String userName;
    @NotNull(groups = {UserGroup.UserRegister.class, UserGroup.UserLogin.class, UserGroup.UserOpenAccount.class, UserGroup.EditPassword.class}, message = "用户密码不能为空")
    @Size(groups = {UserGroup.UserRegister.class, UserGroup.UserLogin.class, UserGroup.UserOpenAccount.class, UserGroup.EditPassword.class}, min = 6, message = "用户密码6-16个字符")
    @Size(groups = {UserGroup.UserRegister.class, UserGroup.UserLogin.class, UserGroup.UserOpenAccount.class, UserGroup.EditPassword.class}, max = 16, message = "用户密码6-16个字符")
    private String password;
    @NotNull(groups = {UserGroup.UserRegister.class, UserGroup.UserLogin.class, UserGroup.UserOpenAccount.class}, message = "手机号不能为空")
    @Pattern(groups = {UserGroup.UserRegister.class, UserGroup.UserLogin.class, UserGroup.UserOpenAccount.class}, regexp = "1[0-9]{10,}", message = "手机号格式错误")
    private String mobile;
    @NotNull(groups = {UserGroup.UserRegister.class}, message = "验证码不能为空")
    private String validaCode;
    private String email;
    private String loginIp;
    private String lastLoginIp;
    private Integer id;
    private Integer page;
    private Integer limit;
    @NotNull(groups = {UserGroup.UserOpenAccount.class}, message = "token不能为空")
    private String token;
    @NotNull(groups = {UserGroup.EditPassword.class}, message = "新密码不能为空")
    @Size(groups = {UserGroup.EditPassword.class}, min = 6, message = "用户密码6-16个字符")
    @Size(groups = {UserGroup.EditPassword.class}, max = 16, message = "用户密码6-16个字符")
    private String newPassword;
    @Size(groups = {UserGroup.EditPassword.class}, min = 6, message = "用户密码6-16个字符")
    @Size(groups = {UserGroup.EditPassword.class}, max = 16, message = "用户密码6-16个字符")
    @NotNull(groups = {UserGroup.EditPassword.class}, message = "确认新密码不能为空")
    private String againPassword;
    @Min(groups = {UserGroup.UserOpenAccount.class}, message = "性别请按规范输入", value = 1)
    @Min(groups = {UserGroup.UserOpenAccount.class}, message = "性别请按规范输入", value = 0)
    private Integer sex;

    @Override
    public String toString() {
        return "UserDto{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", mobile='" + mobile + '\'' +
                ", validaCode='" + validaCode + '\'' +
                ", email='" + email + '\'' +
                ", loginIp='" + loginIp + '\'' +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", id=" + id +
                ", page=" + page +
                ", limit=" + limit +
                ", token='" + token + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", againPassword='" + againPassword + '\'' +
                ", sex=" + sex +
                '}';
    }
}
