package seedqr.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import seedqr.model.Company;

public interface CompanyMapper {
    @Select("select * from company where id=#{id}")
    Company getCompany(int id);

    @Select("select * from company where type=2")
    List<Company> getWholesalers();

    @Insert("insert into company (code, `name`, abbr, email, contact, handphone, `type`, parentId, regionId) "
            + "values (#{code}, #{name}, #{abbr}, #{email}, #{contact}, #{handphone}, #{type}, #{parentId}, #{regionId})")
    void addCompany(Company company);

    @Delete("delete from company where id=#{companyId}")
    void deleteCompany(int companyId);
}
