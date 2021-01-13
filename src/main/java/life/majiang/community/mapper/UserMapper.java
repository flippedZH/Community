package life.majiang.community.mapper;

import life.majiang.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


//@Repository
//@MapperScan(basePackages = "life.majiang.community.mapper")
@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    //@Insert("insert into user (name) values(#{name})")
    void insert(User user);

    @Select("select * from  user where token=#{token}")
    User findByToken(String token);

    @Select("select * from  user where id=#{id}")
    User findByID(@Param("id") Integer id);
}
