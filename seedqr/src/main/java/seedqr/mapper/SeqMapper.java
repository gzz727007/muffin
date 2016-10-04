/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author muffin
 */
public interface SeqMapper {
    
    @Select("SELECT nextvaln(#{key}, #{size})")
    int nextVal(@Param("key")String key, @Param("size")int size);
    
}
