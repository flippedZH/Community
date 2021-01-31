package life.majiang.community.service;

import life.majiang.community.dto.GithubUser;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper; //idea无法自动加载上下午文
    
    public void createOrUpdatae(User user) {
        //网页传一个user  从数据库查一个user
        //通过用户id查询，不是主键id
//        User dbUser=userMapper.findByAccountId(user.getAccountId());
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users=userMapper.selectByExample(userExample);
        //通过数据库查询结果判断
        if(users.size()==0){
            //插入用户、创建用户
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtModified());
            userMapper.insert(user);
        }
        else {
            //更新用户
            User dbUser=users.get(0);
            User updateUser = new User();
            updateUser.setGmtCreate(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());//GithubProvider
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            //为了不更新创建时间
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
//            userMapper.update(dbUser);
        }
    }
}
