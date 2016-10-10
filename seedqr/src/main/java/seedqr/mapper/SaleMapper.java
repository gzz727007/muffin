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
import seedqr.model.SaleInfo;

/**
 *
 * @author muffin
 */
public interface SaleMapper {
    
    @Select("SELECT `id`, `qr_code_id` qrCodeId, `wholesaler_id` wholesalerId, `message`, `type`, "
            + "`sale_time` saleTime FROM `seed_sales_info` WHERE qr_code_id = #{qrCodeId} AND `type` = #{type}")
    List<SaleInfo> getSaleInfoByType(@Param("qrCodeId")int qrCodeId,@Param("type")int type);
    
    @Insert("INSERT INTO `seed_sales_info` ( `qr_code_id`, `wholesaler_id`, `message`, `type`) "
            + "VALUES (#{qrCodeId}, #{wholesalerId}, #{message}, #{type} ) ")
    int addSaleInfo(SaleInfo saleInfo);
    
    
    @InsertProvider(type = SaleMapperProvider.class, method = "addSaleInfos")
    int addSaleInfos(List<SaleInfo> list);
    
    public static class SaleMapperProvider {

		public String addSaleInfos(Map<String, List<SaleInfo>> map) {
			List<SaleInfo> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("INSERT INTO `seed_sales_info` ( `qr_code_id`, `wholesaler_id`, `message`, `type`)  values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].qrCodeId},#'{'list[{0}].wholesalerId},#'{'list[{0}].message},#'{'list[{0}].type})");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new Integer[]{i}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			return stringBuilder.toString();
		}

	}
}
