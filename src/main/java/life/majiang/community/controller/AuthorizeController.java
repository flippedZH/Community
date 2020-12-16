package life.majiang.community.controller;
<<<<<<< HEAD
import life.majiang.community.dto.AccessTokenDTo;
import life.majiang.community.dto.GithubUser;
import life.majiang.community.model.User;
=======

import life.majiang.community.dto.AccessTokenDTo;
import life.majiang.community.dto.GithubUser;
>>>>>>> 4ba834baa28fd1150e9063787fd239eab911e2da
import life.majiang.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
<<<<<<< HEAD

import org.springframework.web.bind.annotation.RequestParam;
import life.majiang.community.mapper.UserMapper;


import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

//配置路由

=======
import org.springframework.web.bind.annotation.RequestParam;

//配置路由


>>>>>>> 4ba834baa28fd1150e9063787fd239eab911e2da
@Controller
public class AuthorizeController {

    @Autowired //自动加载容器中实例化对象加载到当前上下文中，是不是与前面的Component对应---是的
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private  String clientId;
    @Value("${github.client.secret}")
    private  String clientSecret;
    @Value("${github.redirect.uri}")
    private  String redirectUri;

<<<<<<< HEAD
    @Autowired
    private UserMapper userMapper;

    //参数的接收
    @GetMapping("/callback")
    public String callback (@RequestParam(name = "code") String code,//@RequestParam 向服务器发送请求(发送两个参数？)
                            @RequestParam(name = "state") String state,
                            HttpServletRequest request){
=======
    //参数的接收
    @GetMapping("/callback")
    public String callback (@RequestParam(name = "code") String code,//@RequestParam 向服务器发送请求(发送两个参数？)
                            @RequestParam(name = "state") String state){
>>>>>>> 4ba834baa28fd1150e9063787fd239eab911e2da
        //调用github access tocken接口
        System.out.println("code:"+code);
        System.out.println("state"+state);

        //设置五个参数
        AccessTokenDTo accessTokenDTo = new AccessTokenDTo();
        accessTokenDTo.setClient_id(clientId);
        accessTokenDTo.setClient_secret(clientSecret);
        System.out.println("code:"+code);
        accessTokenDTo.setCode(code);
        accessTokenDTo.setRedirect_uri(redirectUri);
        accessTokenDTo.setState(state);

        String accessToken=githubProvider.getAccessToken(accessTokenDTo);
<<<<<<< HEAD
        GithubUser githubUser=githubProvider.getUser(accessToken);
        if(githubUser!=null){
            //登录成功 写cookies 和 session

            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtModified());
            userMapper.insert(user);


            request.getSession().setAttribute("user",githubUser);
            return "redirect:/";


        }else {
            //登录失败，重新登录
            return "redirect:/";
        }
=======
        GithubUser user=githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        System.out.println(user.getId());
        return "index";
>>>>>>> 4ba834baa28fd1150e9063787fd239eab911e2da
    }
}
