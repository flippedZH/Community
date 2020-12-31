package life.majiang.community.service;


import life.majiang.community.dto.PaginnationDTO;
import life.majiang.community.dto.QuestionDto;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


//服务层？？？？？？？    后端？/**/


@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginnationDTO list(Integer page, Integer size){

        Integer offset=size*(page-1);

        List<Question> questions =questionMapper.list(offset,size);//与数据库交互，从数据库中获取到指定数目的数据
        List<QuestionDto> questionDtoList =new ArrayList<>();

        PaginnationDTO paginnationDTO =new PaginnationDTO();

        for (Question question:questions){
            User user = userMapper.findByID(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            //把question对象放在questionDto里面（属性拷贝）
//            System.out.println(question.getViewCount());
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
//            System.out.println("浏览："+questionDto.getViewCount());

        }

        paginnationDTO.setQuestions(questionDtoList);//赋值
        Integer  totalCount=questionMapper.count();
        paginnationDTO.setPagination(totalCount,page,size);//为了设计逻辑函数往数据模型中传值（加值）
        return paginnationDTO;
    }
}


