/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.mapper;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.InsertProvider;
import seedqr.model.QrCode;

/**
 *
 * @author muffin
 */
public interface QrCodeMapper {
    
    
    @InsertProvider(type = QrCodeMapperProvider.class, method = "inserAll")
    int insertQrCode(List<QrCode> list);
    
    
    
    public static class QrCodeMapperProvider {

		public String inserAll(Map<String, List<QrCode>> map) {
			List<QrCode> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("INSERT INTO `qr_code` (`unit_code`, `company_code`, `ext_unit_code`, `seed_name`, `company_name`, `tracking_url`, `seed_id`, `create_time` )  values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].loginName},#'{'list[{0}].name},#'{'list[{0}].password},#'{'list[{0}].salt},#'{'list[{0}].email},#'{'list[{0}].status},#'{'list[{0}].teamId})");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new Integer[]{i}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			return stringBuilder.toString();
		}

	}
}
