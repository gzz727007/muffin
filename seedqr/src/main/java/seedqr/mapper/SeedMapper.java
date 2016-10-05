/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.mapper;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import seedqr.model.QrCode;
import seedqr.model.Seed;
import seedqr.model.SeedConfig;

/**
 *
 * @author muffin
 */
public interface SeedMapper {
    
    @Select("SELECT  id, user_id userId, seed_name seedName , seed_ui_display seedUiDisplay FROM user_seed WHERE user_id = #{userId}")
    List<Seed> getAllSeeds(@Param("userId")int userId);
    
    @Insert("INSERT INTO user_seed (user_id , seed_name, seed_ui_display) VALUES (#{userId}, #{seedName}, #{seedUiDisplay})")
    int addSeeds(Seed seed);
    
    @InsertProvider(type = SeedMapperProvider.class, method = "inserAll")
    int insertQrCode(List<SeedConfig> list);
    
    
    public static class SeedMapperProvider {

		public String inserAll(Map<String, List<SeedConfig>> map) {
			List<SeedConfig> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("INSERT INTO `seed_config` (`seed_id`, `para_name`, `para_value`, `type` )  values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].seedId},#'{'list[{0}].paraName},#'{'list[{0}].paraValue},#'{'list[{0}].type})");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new Integer[]{i}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			return stringBuilder.toString();
		}

	}
}
