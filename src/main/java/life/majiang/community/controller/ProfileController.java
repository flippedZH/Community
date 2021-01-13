package life.majiang.community.controller;

import life.majiang.community.dto.PaginnationDTO;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    //注入
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(
            HttpServletRequest request,
            @PathVariable(name="action" ) String action,
            @RequestParam(name="page",defaultValue = "1") Integer page,//从页面接收两个参数
            @RequestParam(name="size",defaultValue = "5") Integer size,
                          Model model){
        User user=null;
        Cookie[] cookies=request.getCookies();
        if(cookies!=null&&cookies.length!=0){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("token")){
                    String token=cookie.getValue();
                    user=userMapper.findByToken(token);
                    if(user!=null){
                        //数据给前端口
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }
        //profile页面的校验
        if(user==null){
            return "redirect:/";
        }
        if("questions".equals(action)){
                model.addAttribute("section","questions");
                model.addAttribute("sectionName","我的提问");
        }
        else if("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }
        //希望questionSevice有一个方法，能够通过用户id去传递问题
        PaginnationDTO pagination=questionService.list(user.getId(), page,size); //驱动式编程，快速修复 ALT+回车
        model.addAttribute("pagination",pagination);//通过model向前段注入数据 questions
        return "profile";
    }
}
