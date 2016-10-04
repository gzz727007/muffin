/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.dao;

import org.apache.ibatis.session.SqlSession;
import seedqr.mapper.UserMapper;
import seedqr.model.User;
import seedqr.util.MybatisUtil;

/**
 *
 * @author muffin
 */
public class UserDao {
    
    public User getUserByUserName(String userName){
        SqlSession  session = MybatisUtil.getInstance().openSession();
        UserMapper  userMapper =  session.getMapper(UserMapper.class);
        return userMapper.getUserByUserName(userName);
    }
    
    public int addUser(User user) {
        int result = 0;
        SqlSession  session = MybatisUtil.getInstance().openSession(false);
        UserMapper  userMapper =  session.getMapper(UserMapper.class);
        if (user.getParentId() > 0) {
            result = userMapper.addResaleUser(user);
        } else {
           result =  userMapper.addProductUser(user);
        }
        session.commit();
        return result;
    }
}
