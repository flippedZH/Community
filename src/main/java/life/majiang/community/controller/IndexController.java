package life.majiang.community.controller;

import life.majiang.community.dto.PaginnationDTO;
import life.majiang.community.dto.QuestionDto;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

//与页面数据交互


@Controller//允许类接收前端请求
public class IndexController {

    //注入
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/") //访问路径
    //@RequestParam(name = "name") String name, Model model//参数
    //model.addAttribute("name",name); //传值
    public  String index(HttpServletRequest request,
                         Model model,
                         @RequestParam(name="page",defaultValue = "1") Integer page,//从页面接收两个参数
                         @RequestParam(name="size",defaultValue = "3") Integer size
    ){
        Cookie[] cookies=request.getCookies();
        if(cookies!=null&&cookies.length!=0){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("token")){
                    String token=cookie.getValue();
                    User user=userMapper.findByToken(token);
                    if(user!=null){
                        //数据给前端口
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }

        PaginnationDTO paginnation=questionService.list(page,size); //驱动式编程，快速修复 ALT+回车
        model.addAttribute("paginnation",paginnation);//通过model向前段注入数据 questions
        return "index";//从模板目录里面找的（templaes）
    }
}
