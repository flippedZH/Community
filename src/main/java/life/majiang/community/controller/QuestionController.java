package life.majiang.community.controller;

import life.majiang.community.dto.QuestionDto;


import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.model.Question;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class QuestionController {

    //后续探究一下Autowired 与 new的区别
    @Autowired //通过 @Autowired的使用来消除 set ，get方法。
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(
            @PathVariable(name = "id") Long id,
                           Model model){
        System.out.println("test:"+id);
        QuestionDto questionDTO=questionService.getById(id);

        //累加阅读数
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        return "question";
    }
}
