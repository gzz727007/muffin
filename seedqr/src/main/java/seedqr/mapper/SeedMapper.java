/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import seedqr.model.Seed;

/**
 *
 * @author muffin
 */
public interface SeedMapper {
    
    @Select("SELECT  id, user_id userId, seed_name seedName FROM user_seed WHERE user_id = #{userId}")
    List<Seed> getAllSeeds(@Param("userId")int userId);
    
    @Insert("INSERT INTO user_seed (user_id , seed_name) VALUES (#{userId}, #{seedName})")
    int addSeeds(Seed seed);
}
