package life.majiang.community.controller;

import life.majiang.community.dto.PaginnationDTO;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//与页面数据交互

@Controller//允许类接收前端请求
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/") //访问路径
    //@RequestParam(name = "name") String name, Model model//参数
    //model.addAttribute("name",name); //传值
    public  String index(
                         Model model,
                         @RequestParam(name="page",defaultValue = "1") Integer page,//从页面接收两个参数
                         @RequestParam(name="size",defaultValue = "5") Integer size
    ){
        PaginnationDTO pagination=questionService.list(page,size); //驱动式编程，快速修复 ALT+回车
        System.out.println("问题："+pagination.getQuestions());
        model.addAttribute("pagination",pagination);//通过model向前段注入数据 questions
        return "index";//从模板目录里面找的（templaes）
    }
}
