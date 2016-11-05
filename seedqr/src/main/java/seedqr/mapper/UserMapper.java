package seedqr.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import seedqr.model.User;

public interface UserMapper {
    @Insert("insert into `user` (user_name, password, urole, company_id, create_time) "
            + "values (#{userName}, #{password}, #{role}, #{companyId}, NOW())")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id",
            before = false, resultType = int.class)
    void addUser(User user);

    @Select("select id, user_name userName, password, urole role, company_id companyId from `user`")
    List<User> getAllUsers();

    @Select("select id, user_name userName, password, urole role, company_id companyId from `user` where user_name = #{userName}")
    User getUserByUserName(String userName);

    @Update("update `user` set password = #{password}, urole = #{role} where id = #{id}")
    void updateUser(User user);
    
    @Update("update `user` set `last_login_time` = now() where id = #{id}")
    void updateUserOnlineStatus(User user);
}
