package life.majiang.community.service;


import life.majiang.community.dto.PaginnationDTO;
import life.majiang.community.dto.QuestionDto;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

    //包含所有问题的数据模型
    public PaginnationDTO list(Integer page, Integer size){

        PaginnationDTO paginnationDTO =new PaginnationDTO();

        //这里主要就只有两个变量：totalPage，totalCount
        //totalPage由totalCount得到，totalCount来自数据库
        Integer  totalPage;
        Integer  totalCount=questionMapper.count();

        if(totalCount % size==0){
            totalPage=totalCount/size;//0
        }else{
            totalPage=totalCount/size+1;
        }
        //判断逻辑：判断是否是页面数据是否正确，避免类似情况：直接在浏览器修改page=-1
        if(page<1){
            page=1;
        }
        if(page>totalPage){
            page=totalPage;
        }
        paginnationDTO.setPagination(totalPage,page);//为了设计逻辑函数往数据模型中传值（加值）
        Integer offset=size*(page-1);
        List<Question> questions =questionMapper.list(offset,size);//与数据库交互，从数据库中获取到指定数目的数据
        List<QuestionDto> questionDtoList =new ArrayList<>();

        for (Question question:questions){
            User user = userMapper.findByID(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            //把question对象放在questionDto里面（属性拷贝）
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginnationDTO.setQuestions(questionDtoList);//赋值
        return paginnationDTO;
    }

    //通过用户id查找到的问题的数据模型
    public PaginnationDTO list(Integer userId, Integer page, Integer size) {
        
        PaginnationDTO paginnationDTO =new PaginnationDTO();

        Integer totalPage;

        Integer  totalCount=questionMapper.countByUserId(userId);

        if(totalCount % size==0){
            totalPage=totalCount/size;//0
        }else{
            totalPage=totalCount/size+1;
        }

        //判断逻辑：判断是否是页面数据是否正确，避免类似情况：直接在浏览器修改page=-1
        if(page<1){
            page=1;
        }
        if(page>totalPage){
            page=totalPage;
        }

        paginnationDTO.setPagination(totalPage,page);//为了设计逻辑函数往数据模型中传值（加值）
        
        Integer offset=size*(page-1);
        List<Question> questions =questionMapper.listByUserId(userId,offset,size);//与数据库交互，从数据库中获取到指定数目的数据
        List<QuestionDto> questionDtoList =new ArrayList<>();

        for (Question question:questions){
            User user = userMapper.findByID(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            //把question对象放在questionDto里面（属性拷贝）
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginnationDTO.setQuestions(questionDtoList);//赋值
        return paginnationDTO;
    }

    public QuestionDto getById(Integer id) {
        Question question =questionMapper.getById(id);

        QuestionDto questionDto=new QuestionDto();
        BeanUtils.copyProperties(question,questionDto);

        User user = userMapper.findByID(question.getCreator());
        questionDto.setUser(user);

        return  questionDto;
    }

    public void createOrUpdate(Question question) {
         if(question.getId()==null){
            //创建
             //gmt_create表示记录创建时间，gmt_modified表示最近修改时间
             //因为参数这两个需要在一起
             question.setGmtCreate(System.currentTimeMillis());
             question.setGmtModified(question.getGmtCreate());
             questionMapper.create(question);
         }else{
             //更新
             question.setGmtModified(question.getGmtCreate());
            questionMapper.update(question);
         }
    }
}


