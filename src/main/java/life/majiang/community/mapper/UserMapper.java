package life.majiang.community.mapper;

import life.majiang.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

//@Repository
//@MapperScan(basePackages = "life.majiang.community.mapper")
@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_creat,gmt_modified) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    //@Insert("insert into user (name) values(#{name})")
    void insert(User user);
}
