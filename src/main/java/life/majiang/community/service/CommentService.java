package life.majiang.community.service;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.CommentMapper;
import life.majiang.community.mapper.QuestionExtMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private   QuestionMapper questionMapper;

    @Autowired
    private  QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void insert(Comment comment) {
        System.out.println("comment.getParentId():"+comment.getParentId());
        if(comment.getParentId()==null || comment.getParentId()==0){
            //怎么 把消息从内层抛出到controller呢？---通过之前自定义的Exception抛出（code码）
            System.out.println("1");
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        System.out.println("comment.getType()"+comment.getType());
        if(comment.getType()==null|| !CommentTypeEnum.isExist(comment.getType())){
            System.out.println("2");
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if(comment.getType()==CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment=commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment==null){
                System.out.println("3");
                throw  new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }
        else {
            //回复问题
            Question question =questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question==null){
                System.out.println("4");
                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
        }
    }
//    CommentExample commentExample = new CommentExample();
//        commentExample.createCriteria()
//                .andParentIdEqualTo(id)
//                .andTypeEqualTo(CommentTypeEnum.QUESTION.getType());
//        commentExample.setOrderByClause("gmt_create desc");
//    List<Comment> comments = commentMapper.selectByExample(commentExample

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                //外层id
                .andParentIdEqualTo(id)
                //里层type
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        //流式编程：list-stream-list  函数式编程：comment -> comment.getCommentator())
        // 获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList();
        userIds.addAll(commentators);

        //获取评论人并转化为map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //comment 转化为commentsDTO(单一类型转复合类型的常用方法)
        //comments是Comment对象 没有user对象
        // commentDTOS是对象列表  commentDTO 包含user对象
        //map 方法用于映射每个元素到对应的结果 类似迭代器
        List<CommentDTO> commentDTOS = comments.stream().map(
            comment -> {
            CommentDTO commentDTO = new CommentDTO();
            //属性拷贝                sources target
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
    //希望抛出异常给页面返回一个json
}
