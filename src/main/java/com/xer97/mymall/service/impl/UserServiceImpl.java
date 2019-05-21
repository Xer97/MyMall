package com.xer97.mymall.service.impl;

import com.xer97.mymall.common.Const;
import com.xer97.mymall.common.ServerResponse;
import com.xer97.mymall.common.TokenCache;
import com.xer97.mymall.dao.UserMapper;
import com.xer97.mymall.pojo.User;
import com.xer97.mymall.service.IUserService;
import com.xer97.mymall.util.Md5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * @author xer97
 * @date 2019/3/31 12:27
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        // MD5加密
        String md5Password = Md5Util.md5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        if (StringUtils.isBlank(user.getPassword())) {
            return ServerResponse.createByErrorMessage("密码不能为空");
        }

        ServerResponse<String> vaildResponse = checkVaild(user.getUsername(), Const.USERNAME);
        if (!vaildResponse.isSuccess()) {
            return vaildResponse;
        }
        vaildResponse = checkVaild(user.getEmail(), Const.EMAIL);
        if (!vaildResponse.isSuccess()) {
            return vaildResponse;
        }

        // 设置角色
        user.setRole(Const.Role.ROLE_CUSTOMER);
        // MD5加密
        String md5Password = Md5Util.md5EncodeUtf8(user.getPassword());
        user.setPassword(md5Password);
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkVaild(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (Objects.equals(type, Const.EMAIL)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("email已存在");
                }
            }
            if (Objects.equals(type, Const.USERNAME)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        // 先检查用户名是否存在
        ServerResponse<String> response = checkVaild(username, Const.USERNAME);
        if (response.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            // 将Token放入本地内存缓存
            TokenCache.set(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("找回密码答案错误");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误，token需要传递");
        }
        ServerResponse<String> response = checkVaild(username, Const.USERNAME);
        if (response.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.get(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或过期");
        }

        if (Objects.equals(forgetToken, token)) {
            String md5Password = Md5Util.md5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, Integer userId) {
        int resultCount = userMapper.checkPassword(Md5Util.md5EncodeUtf8(passwordOld), userId);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        int rowCount = userMapper.updatePasswordByPrimaryKey(userId, Md5Util.md5EncodeUtf8(passwordNew));
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("密码修改成功");
        }
        return ServerResponse.createByErrorMessage("密码修改失败");
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        // 用户名不能更新
        // 检验email
        int resultCount = userMapper.checkEmailByUserId(user.getId(), user.getEmail());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("email已存在，请更换email");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("更新用户信息成功", user);
        }
        return ServerResponse.createByErrorMessage("更新用户信息失败");
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("未找到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    // backend

    @Override
    public ServerResponse<String> checkAdminRole(User user) {
        if (user != null && user.getRole() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
