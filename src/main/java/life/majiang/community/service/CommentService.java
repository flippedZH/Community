package life.majiang.community.service;

import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.CommentMapper;
import life.majiang.community.mapper.QuestionExtMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.model.Comment;
import life.majiang.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private   QuestionMapper questionMapper;

    @Autowired
    private  QuestionExtMapper questionExtMapper;

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
    //希望抛出异常给页面返回一个json
}
