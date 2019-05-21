package com.xer97.mymall.service;

import com.xer97.mymall.common.ServerResponse;
import com.xer97.mymall.pojo.User;

/**
 * @author xer97
 * @date 2019/3/31 12:25
 */
public interface IUserService {
    /**
     * 登录服务
     *
     * @param username 用户名
     * @param password 用户密码
     * @return 服务响应 附带登录用户信息
     */
    ServerResponse<User> login(String username, String password);

    /**
     * 注册服务
     *
     * @param user 注册用户信息
     * @return 服务响应
     */
    ServerResponse<String> register(User user);

    /**
     * 校验服务
     *
     * @param str  校验的字符串
     * @param type 校验类型 username 或 email
     * @return 服务响应
     */
    ServerResponse<String> checkVaild(String str, String type);

    /**
     * 获取找回密码问题
     *
     * @param username 用户名
     * @return 服务响应 附带用户找回密码问题
     */
    ServerResponse<String> selectQuestion(String username);

    /**
     * 验证找回密码答案
     *
     * @param username 用户名
     * @param question 找回密码问题
     * @param answer   找回密码答案
     * @return 服务响应 附带Token
     */
    ServerResponse<String> checkAnswer(String username, String question, String answer);

    /**
     * 忘记密码下修改用户密码
     *
     * @param username    用户名
     * @param passwordNew 用户新密码
     * @param forgetToken 验证找回密码答案时生成的Token
     * @return 服务响应
     */
    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    /**
     * 登录状态下修改密码
     *
     * @param passwordOld 用户旧密码
     * @param passwordNew 用户新密码
     * @param userId      用户id
     * @return 服务响应
     */
    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, Integer userId);

    /**
     * 更新用户信息
     *
     * @param user 用户新信息
     * @return 服务响应 附带更新后的用户对象
     */
    ServerResponse<User> updateInformation(User user);

    /**
     * 根据id获取当前用户信息
     *
     * @param userId 用户id
     * @return 服务响应 附带用户信息
     */
    ServerResponse<User> getInformation(Integer userId);

    /**
     * 检验用户是否为管理员
     *
     * @param user 用户信息
     * @return 服务响应
     */
    ServerResponse<String> checkAdminRole(User user);
}
