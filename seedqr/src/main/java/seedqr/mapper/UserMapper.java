/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import seedqr.model.User;

/**
 *
 * @author muffin
 */

public interface UserMapper {
    
    @Select("SELECT " +
"  `id`, `user_name` userName, `name`, `password`, `urole`, `email`, `handphone`, `create_time` createTime, `company_code` companyCode, `company_name` companyName, "
            + "`last_login_time` lastLoginTime, `type`, `parent_id` parentId, `status`, `region_id` regionId " +
"FROM " +
"  `user` ")
    List<User> getAllUser();
    
    @Select("SELECT " +
"  `id`, `user_name` userName, `name`, `password`, `urole`, `email`, `handphone`, `create_time` createTime, `company_code` companyCode, `company_name` companyName, "
            + "`last_login_time` lastLoginTime, `type`, `parent_id` parentId, `status`, `region_id` regionId " +
"FROM " +
"  `user` where user_name = #{userName}")
    User getUserByUserName(String userName);
    
    @Insert("INSERT INTO `user` (" +
"  `user_name`, `name`, `password`, `urole`, `email`, `handphone`, `create_time`, `company_code`, `company_name`, `last_login_time`, `type`, `parent_id`, `status`, `region_id`" +
") " +
"VALUES" +
"  (" +
"    #{userName}, #{name}, #{password}, 'user', #{email}, #{handphone}, NOW(), nextval('company_code'), #{companyName}, NULL, 1,0, '1', 0" +
"  )")
    int addProductUser(User user);
    
     @Insert("INSERT INTO `user` (" +
"  `user_name`, `name`, `password`, `urole`, `email`, `handphone`, `create_time`, `company_code`, `company_name`, `last_login_time`, `type`, `parent_id`, `status`, `region_id`" +
") " +
"VALUES" +
"  (" +
"    #{userName}, #{name}, #{password}, 'user', #{email}, #{handphone}, NOW(), '', #{companyName}, NULL, 2,#{parentId}, '1', #{regionId}" +
"  )")
    int addResaleUser(User user);
}
