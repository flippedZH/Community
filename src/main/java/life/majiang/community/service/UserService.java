package life.majiang.community.service;

import life.majiang.community.dto.GithubUser;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper; //idea无法自动加载上下午文


    public void createOrUpdatae(User user) {
        //网页传一个user  从数据库查一个user
        //通过用户id查询，不是主键id
        User dbUser=userMapper.findByAccountId(user.getAccountId());
        //通过数据库查询结果判断
        if(dbUser==null){
            //插入用户、创建用户
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtModified());
            userMapper.insert(user);
        }
        else {
            //更新用户
            dbUser.setGmtCreate(System.currentTimeMillis());
            dbUser.setAvatarUrl(user.getAvatarUrl());//GithubProvider
            dbUser.setName(user.getName());
            dbUser.setToken(user.getToken());
            userMapper.update(dbUser);
        }
    }
}
