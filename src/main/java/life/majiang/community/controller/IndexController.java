package life.majiang.community.controller;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


@Controller//允许类接收前端请求
public class IndexController {

    //注入
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/") //访问路径
    //@RequestParam(name = "name") String name, Model model//参数
    //model.addAttribute("name",name); //传值
    public  String index(HttpServletRequest request){
        Cookie[] cookies=request.getCookies();
        for(Cookie cookie:cookies){
            String token=cookie.getValue();
            User user=userMapper.findByToken(token);
            if(user!=null){
                request.getSession().setAttribute("user",user);
            }
            break;
        }
        return "index";//从模板目录里面找的（templaes）
    }
}
