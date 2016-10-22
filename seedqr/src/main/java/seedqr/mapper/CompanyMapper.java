package seedqr.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import seedqr.model.Company;

public interface CompanyMapper {
    @Insert("insert into company (code, `name`, abbr, email, contact, handphone, `type`, parentId, regionId, userId) "
            + "values (#{code}, #{name}, #{abbr}, #{email}, #{contact}, #{handphone}, #{type}, #{parentId}, #{regionId}, #{userId})")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id",
            before = false, resultType = int.class)
    void addCompany(Company company);
    
    @Insert("insert into company (code, `name`, abbr, email, contact, handphone, `type`, userId, regionId) "
            + "values (nextval('company_code'), #{name}, #{abbr}, #{email}, #{contact}, #{handphone}, #{type}, #{userId}, #{regionId})")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id",
            before = false, resultType = int.class)
    void addManufacturer(Company company);

    @Select("select * from company where id = #{id}")
    Company getCompany(int id);

    @Select("select * from company where type = 1")
    List<Company> getManufacturers();

    @Select("select id, `name` from company where type = 1")
    List<Company> getBriefManufacturers();

    @Select("select * from company where type = 2 and userId = #{userId}")
    List<Company> getWholesalers(int userId);

    @Update("update company set abbr = #{abbr}, contact = #{contact}, handphone = #{handphone}, email = #{email} where id = #{id}")
    void updateManufacturer(Company manufacturer);

    @Delete("delete from company where id = #{companyId}")
    void deleteCompany(int companyId);
}
