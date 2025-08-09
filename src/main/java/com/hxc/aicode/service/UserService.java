package com.hxc.aicode.service;

import com.hxc.aicode.model.dto.UserQueryRequest;
import com.hxc.aicode.model.vo.LoginUserVO;
import com.hxc.aicode.model.vo.UserVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.hxc.aicode.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 用户 服务层。
 *
 * @author hxc
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);


    /**
     * 获取脱敏的用户登录信息
     * @param user
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息（比如一个用户查看其他用户信息，能看到的信息会更少）
     * @param user
     * @return 用户信息
     */
    UserVO getUserVO(User user);


    /**
     * 获取脱敏的用户信息列表（
     * @param userList
     * @return 用户信息列表
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);


    /**
     * 用户注销
     */
    Boolean userLogout(HttpServletRequest request);


    /**
     * 根据查询条件构造数据查询参数
     * @param userQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest);


    /**
     * 根据传入的密码，返回加密后的密码
      */

    String getEncryptPassword(String password);



}
