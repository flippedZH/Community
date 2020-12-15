package life.majiang.community.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller //允许类接收前端请求
public class HelloController {
    @GetMapping("/") //访问路径
    public String hello(){
        //@RequestParam(name = "name") String name, Model model//参数
        //model.addAttribute("name",name); //传值

        return "index"; //从模板目录里面找的（templaes）
    }
}
