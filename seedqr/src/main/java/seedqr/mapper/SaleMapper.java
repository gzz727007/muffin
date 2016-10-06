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
import seedqr.model.SaleInfo;

/**
 *
 * @author muffin
 */
public interface SaleMapper {
    
    @Select("SELECT `id`, `qr_code_id` qrCodeId, `wholesaler_id` wholesalerId, `message`, `type`, "
            + "`sale_time` saleTime FROM `seed_sales_info` WHERE qr_code_id = #{qrCodeId} AND `type` = #{type}")
    List<SaleInfo> getSaleInfoByType(@Param("qrCodeId")int qrCodeId,@Param("type")int type);
    
    @Insert("INSERT INTO `zzcx`.`seed_sales_info` ( `qr_code_id`, `wholesaler_id`, `message`, `type`) "
            + "VALUES (#{qrCodeId}, #{wholesalerId}, #{message}, #{type} ) ")
    int addSaleInfo(SaleInfo saleInfo);
}
