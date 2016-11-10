/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import seedqr.model.Region;

/**
 *
 * @author muffin
 */
public interface RegionMapper {
    
    @Select("SELECT  `id`, `name`, `level`, `parent_id` parentId FROM `region`  ORDER BY id")
    List<Region> getAllRegion();
    
    @Select("SELECT a.`name` FROM `region` a, `company` b WHERE a.`id` = b.`regionId` AND b.`id` = #{salerId}")
    String getSalerRegion(@Param("salerId")int salerId);
}
