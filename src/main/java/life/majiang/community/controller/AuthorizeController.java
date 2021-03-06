package life.majiang.community.controller;
import life.majiang.community.dto.AccessTokenDTo;
import life.majiang.community.dto.GithubUser;
import life.majiang.community.model.User;
import life.majiang.community.provider.GithubProvider;
import life.majiang.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import life.majiang.community.mapper.UserMapper;


import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

//配置路由

@Controller
public class AuthorizeController {

    //重要！！！！！！
    //JSON.parseObject,把string自动转账为java类对象
    @Autowired //自动加载容器中实例化对象加载到当前上下文中，是不是与前面的Component对应---是的
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private  String clientId;
    @Value("${github.client.secret}")
    private  String clientSecret;
    @Value("${github.redirect.uri}")
    private  String redirectUri;

    @Autowired
    private UserService userService;
//    //@Resource
//    @Autowired
//    private UserMapper userMapper;

    //参数的接收
    @GetMapping("/callback")
    public String callback (@RequestParam(name = "code") String code,//@RequestParam 向服务器发送请求(发送两个参数？)
                            @RequestParam(name = "state") String state,
                            HttpServletRequest request,
                            HttpServletResponse response){
        //调用github access tocken接口

        //设置五个参数
        AccessTokenDTo accessTokenDTo = new AccessTokenDTo();
        accessTokenDTo.setClient_id(clientId);
        accessTokenDTo.setClient_secret(clientSecret);
        System.out.println("code:"+code);
        accessTokenDTo.setCode(code);
        accessTokenDTo.setRedirect_uri(redirectUri);
        accessTokenDTo.setState(state);

//        githubProvider里面有两个类：一是返回Token,另一个返回githubUsers对象
        String accessToken=githubProvider.getAccessToken(accessTokenDTo);
        GithubUser githubUser=githubProvider.getUser(accessToken);

        if(githubUser!=null && githubUser.getId()!=null){
            //登录成功 写cookies 和 session
            System.out.println("name:"+githubUser.getName());
            User user = new User();
            String token = UUID.randomUUID().toString();
            //不管数据库是否存在这些值，都需要重新更新，所以放在外面
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatar_url());//GithubProvider
            userService.createOrUpdatae(user);
//            userMapper.insert(user);//写入数据库

            //响应写入cookie
            response.addCookie(new Cookie("token",token));
            return "redirect:/";

        }else {
            //登录失败，重新登录
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        request.getSession().removeAttribute("user"); //对应 SessionInterceptor里面的添加 user
        Cookie cookie=new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/"; //重定向到首页
    }
}
