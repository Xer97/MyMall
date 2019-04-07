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
     * @param username
     * @param password
     * @return
     */
    ServerResponse<User> login(String username, String password);

    /**
     * 注册服务
     *
     * @param user
     * @return
     */
    ServerResponse<String> register(User user);

    /**
     * 校验服务
     *
     * @param str
     * @param type
     * @return
     */
    ServerResponse<String> checkVaild(String str, String type);

    /**
     * 获取找回密码问题
     *
     * @param username
     * @return
     */
    ServerResponse<String> selectQuestion(String username);

    /**
     * 验证找回密码答案
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    ServerResponse<String> checkAnswer(String username, String question, String answer);

    /**
     * 忘记密码下修改用户密码
     *
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    /**
     * 登录状态下修改密码
     *
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    ServerResponse<User> updateInformation(User user);

    ServerResponse<User> getInformation(Integer userId);
}
