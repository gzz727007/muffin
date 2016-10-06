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
import org.apache.ibatis.annotations.SelectProvider;
import seedqr.model.QrCode;

/**
 *
 * @author muffin
 */
public interface QrCodeMapper {
    
    
    @InsertProvider(type = QrCodeMapperProvider.class, method = "inserAll")
    int insertQrCode(List<QrCode> list);
    
    @Select("SELECT `id`, `unit_code` unitCode, `company_code` companyCode, `ext_unit_code` extUnitCode, `seed_name` seedName, `company_name` companyName, "
            + " `tracking_url` trackingUrl, `seed_id` seedId, `status`, `create_time` createTime, `bind_time` bindTime  "
            + "FROM `qr_code`  WHERE unit_code = #{unitId} AND `status` = 1")
    List<QrCode> getQrCodeByUnitId(@Param("unitId")String unitId);
    
    @Select("SELECT `target_codes` FROM `code_mapping` WHERE `source_code` = #{src}")
    String getTargetsBySrc(@Param("src")String src);
    
    
    @SelectProvider(type = QrCodeMapperProvider.class, method = "getQrCodeByUnitIds")
    List<QrCode> getQrCodeByUnitIds(@Param("ids")String ids);
    
    @Insert("INSERT INTO `code_mapping` (`source_code`,`target_codes`) VALUES (#{src}, #{target})")
    int addQrCodeMapping(@Param("src")String src, @Param("target")String target);
    
    
    public static class QrCodeMapperProvider {

		public String inserAll(Map<String, List<QrCode>> map) {
			List<QrCode> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("INSERT INTO `qr_code` (`unit_code`, `company_code`, `ext_unit_code`, `seed_name`, `company_name`, `tracking_url`, `seed_id`, `create_time` )  values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].unitCode},#'{'list[{0}].companyCode},#'{'list[{0}].extUnitCode},#'{'list[{0}].seedName},#'{'list[{0}].companyName},#'{'list[{0}].trackingUrl},#'{'list[{0}].seedId}, now())");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new Integer[]{i}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			return stringBuilder.toString();
		}
                
                
                public String getQrCodeByUnitIds(Map<String, String> map) {
                    String ids = map.get("ids");
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("SELECT `id`, `unit_code` unitCode, `company_code` companyCode, `ext_unit_code` extUnitCode, `seed_name` seedName, `company_name` companyName, ");
                    stringBuilder.append(" `tracking_url` trackingUrl, `seed_id` seedId, `status`, `create_time` createTime, `bind_time` bindTime ");
                    stringBuilder.append("FROM `qr_code`  WHERE unit_code in (");
                    stringBuilder.append(ids);
                    stringBuilder.append(")");
                    return stringBuilder.toString();
                }

	}
}
