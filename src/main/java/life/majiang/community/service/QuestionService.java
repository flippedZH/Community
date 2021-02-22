package life.majiang.community.service;


import life.majiang.community.dto.PaginnationDTO;
import life.majiang.community.dto.QuestionDto;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;

import life.majiang.community.mapper.QuestionExtMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.QuestionExample;
import life.majiang.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;


//服务层？？？？？？？    后端？/**/

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    //包含所有问题的数据模型：实现分页展示所有问题功能
    public PaginnationDTO list(Integer page, Integer size){

        PaginnationDTO paginnationDTO =new PaginnationDTO();

        //这里主要就只有两个变量：totalPage，totalCount
        //totalPage由totalCount得到，totalCount来自数据库
        Integer  totalPage;
        Integer  totalCount=(int)questionMapper.countByExample(new QuestionExample());

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
        //问题倒序展示：
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        List<Question> questions=questionMapper.selectByExampleWithRowbounds(questionExample,new RowBounds(offset,size));//与数据库交互，从数据库中获取到指定数目的数据
        List<QuestionDto> questionDtoList =new ArrayList<>();

        for (Question question:questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            //把question对象放在questionDto里面（属性拷贝）
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginnationDTO.setData(questionDtoList);//赋值
        return paginnationDTO;
    }

    //通过用户id查找到的问题的数据模型:实现我的问题功能
    public PaginnationDTO list(Long userId, Integer page, Integer size) {
        
        PaginnationDTO paginnationDTO =new PaginnationDTO();

        Integer totalPage;

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer  totalCount=(int)questionMapper.countByExample(questionExample);

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

        QuestionExample example = new QuestionExample();
        //List<Question> questions =questionMapper.listByUserId(userId,offset,size);//与数据库交互，从数据库中获取到指定数目的数据
        example.createCriteria().andCreatorEqualTo(userId);

        List<Question> questions=questionMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));//与数据库交互，从数据库中获取到指定数目的数据
        List<QuestionDto> questionDtoList =new ArrayList<>();

        for (Question question:questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            //把question对象放在questionDto里面（属性拷贝）
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginnationDTO.setData(questionDtoList);//赋值
        return paginnationDTO;
    }

    public QuestionDto getById(Long id) {
        Question question =questionMapper.selectByPrimaryKey(id);
        if(question==null){
//            throw new CustomizeException("你找的问题不存在");//message="你找的问题不存在"
            throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDto questionDto=new QuestionDto();
        BeanUtils.copyProperties(question,questionDto);

        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDto.setUser(user);

        return  questionDto;
    }

    //解决异常：同一用户每次重新登录，在数据库中的用户id(ACCOUNT_ID)不一样。
    public void createOrUpdate(Question question) {
         if(question.getId()==null){
            //创建
             //gmt_create表示记录创建时间，gmt_modified表示最近修改时间
             //因为参数这两个需要在一起
             question.setGmtCreate(System.currentTimeMillis());
             question.setGmtModified(question.getGmtCreate());
             //创建时设置默认初始值
             question.setViewCount(0);
             question.setLikeCount(0);
             question.setCommentCount(0);

             //questionMapper.create(question);
             questionMapper.insert(question);
         }else{
             //更新
             //问题编辑功能的实现
             Question updateQuestion = new Question();
             updateQuestion.setGmtModified(System.currentTimeMillis());
             updateQuestion.setTitle(question.getTitle());
             updateQuestion.setDescription(question.getDescription());
             updateQuestion.setTag(question.getTag());
             QuestionExample example = new QuestionExample();
             example.createCriteria()
                     .andIdEqualTo(question.getId());
             int updated=questionMapper.updateByExampleSelective(updateQuestion, example);
             if(updated!=1){
                 throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
             }
         }
    }

    //累加阅读数
    public void incView(Long id) {
        ///按主键查询问题
        Question question=new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);

//        /按主键查询问题
//        Question question=questionMapper.selectByPrimaryKey(id);
//        Question updateQuestion = new Question();
////对阅读数进行更新
//        updateQuestion.setViewCount(question.getViewCount()+1);
////生成SQl语句，设置条件？  这里需要看看SQL知识，增加理解
//        QuestionExample questionExample =new QuestionExample();
//        questionExample.createCriteria().andIdEqualTo(id);
////参数:要修改的部分值组成的对象  , 对应的查询条件的类
//        questionMapper.updateByExampleSelective(updateQuestion,questionExample);


    }

    //直接传入参数DTO 需要获取id、tag等
    public List<QuestionDto> selectRelated(QuestionDto queryDTO) {
        //用StringUtils判断是否为空
        if(StringUtils.isBlank((queryDTO.getTag()))){
            return  new ArrayList<>();
        }
        //数组接收分割之后的标签
        String[] tags =StringUtils.split(queryDTO.getTag(),',');
        //字符拼接成正则模式
        String  regexpTag= Arrays.stream(tags).collect(Collectors.joining("|"));
        //局部特殊question对象
        Question question =new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);

        //stream用于集合迭代器的增强

        //利用准备好的question对象从数据库中查找结果
        List<Question> questions =questionExtMapper.selectRelated(question);

        //map类似迭代器
        //流操作，完成通用question对象的赋值，并封装成对象列表
        List<QuestionDto> questionDTOS= questions.stream().map(q->
        {   QuestionDto questionDto=new QuestionDto();
            BeanUtils.copyProperties(q,questionDto);
            return  questionDto;
        }).collect(Collectors.toList());

        return  questionDTOS;
    }
}


