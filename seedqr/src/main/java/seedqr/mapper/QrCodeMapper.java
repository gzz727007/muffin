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
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import seedqr.model.QrCode;
import seedqr.model.QrCodeRequest;

/**
 *
 * @author muffin
 */
public interface QrCodeMapper {
    
    
    @InsertProvider(type = QrCodeMapperProvider.class, method = "inserAll")
    int insertQrCode(List<QrCode> list);
    
    @Select("SELECT `id`, `unit_code` unitCode, `company_code` companyCode, `ext_unit_code` extUnitCode, `seed_name` seedName, `company_name` companyName, "
            + " `tracking_url` trackingUrl, `seed_id` seedId, `status`, `create_time` createTime, `bind_time` bindTime, request_id requestId  "
            + "FROM `qr_code`  WHERE unit_code = #{unitId} AND `status` = 1")
    List<QrCode> getQrCodeByUnitId(@Param("unitId")String unitId);
    
    @Select("SELECT `target_codes` FROM `code_mapping` WHERE `source_code` = #{src}")
    String getTargetsBySrc(@Param("src")String src);
    
    
    @SelectProvider(type = QrCodeMapperProvider.class, method = "getQrCodeByUnitIds")
    List<QrCode> getQrCodeByUnitIds(@Param("ids")String ids);
    
    @Insert("INSERT INTO `code_mapping` (`source_code`,`target_codes`) VALUES (#{src}, #{target})")
    int addQrCodeMapping(@Param("src")String src, @Param("target")String target);
    
    @Insert("INSERT INTO `qrcode_request` (" +
"  `user_id`, `seed_id`, `seed_name`, `company_name`, `amount`, `create_time`, `progress`, `file_name` " +
") VALUES (" + "#{userId}, #{seedId}, #{seedName}, #{companyName}, #{amount}, now(), #{progress}, #{fileName} )")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=int.class) 
    int addQrCodeRequest(QrCodeRequest request);
    
    @Select("SELECT  `id`, `user_id` userId, `seed_id` seedId, `seed_name` seedName, `company_name` companyName, `amount`, `create_time` createTime, `progress`, `file_name` fileName "
            + "FROM `qrcode_request`  WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<QrCodeRequest> getAllRequest(@Param("userId")int userId);
    
    @Update("UPDATE qrcode_request SET progress =#{progress} WHERE id = #{id}")
    int updateRequestProgress(@Param("id")int id, @Param("progress")int progress);
    
    @Update("UPDATE qrcode_request SET file_name =#{fileName} WHERE id = #{id}")
    int updateRequestFileName(@Param("id")int id, @Param("fileName")String fileName);
    
    @UpdateProvider(type = QrCodeMapperProvider.class, method = "updateQrCodeSeedId")
    int updateQrCodeSeedId(@Param("qrcodes")String qrcodes, @Param("seedId")Integer seedId);
    
    public static class QrCodeMapperProvider {

		public String inserAll(Map<String, List<QrCode>> map) {
			List<QrCode> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("INSERT INTO `qr_code` (`unit_code`, `company_code`, `ext_unit_code`, `seed_name`, `company_name`, `tracking_url`, `seed_id`, `create_time`,request_id )  values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].unitCode},#'{'list[{0}].companyCode},#'{'list[{0}].extUnitCode},#'{'list[{0}].seedName},"
                                + "#'{'list[{0}].companyName},#'{'list[{0}].trackingUrl},#'{'list[{0}].seedId}, now(),#'{'list[{0}].requestId})");
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

                
                public String updateQrCodeSeedId(Map<String, Object> map) {
                    String qrcodes = map.get("qrcodes").toString();
                    Integer seedId = (Integer) map.get("seedId");
                    
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("UPDATE `qr_code` SET seed_id = ");
                    stringBuilder.append(seedId);
                    stringBuilder.append(",bind_time =now() ,status = 1  WHERE unit_code IN ( ");
                    stringBuilder.append(qrcodes);
                    stringBuilder.append(")");
                    return stringBuilder.toString();
                }
	}
}
