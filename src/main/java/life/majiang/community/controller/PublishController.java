package life.majiang.community.controller;

import life.majiang.community.dto.QuestionDto;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("publish/{id}")
    public  String edit(@PathVariable(name="id") Long id,
                        Model model){
        //从数据库中查询对应的问题肯定是调对应的Mapper
        QuestionDto question=questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());//用于唯一标识问题，去判断是更新还是创建问题
        return "publish";
    }

    @GetMapping("/publish")  //Get返回页面
    public String publish() {
        return "publish";
    }


    //按钮点击一次执行一次 执行一次 进行一次数据库插入
    @PostMapping("/publish")  //Post执行请求
    public String doPublish(
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "tag",required = false) String tag,
            @RequestParam(value = "id",required = false)  Long id,
            HttpServletRequest request,
            Model model) {  //model:从服务端向页面传递参数

        model.addAttribute("title",title);
        model.addAttribute("description" ,description);
        model.addAttribute("tag",tag);

        if(title==null||title==""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description==null||description==""){
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }
        if(tag==null||tag==""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }

        //?
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            //这部分与前端相关
            model.addAttribute( "error", "用户未登陆");
            return "publish";
        }
        //需要传递的值
        Question question =new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());

        question.setId(id);//id是可空的
        questionService.createOrUpdate(question);

        return "redirect:/"; //回首页，展示刚刚发布的内容。
    }
}

