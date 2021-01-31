package life.majiang.community.controller;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.ResultDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.model.Comment;
import life.majiang.community.model.User;
import life.majiang.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request
    ) {
        //未登录不能评论
        User user =(User)request.getSession().getAttribute("user");
        if(user==null)
        {
            //返回到哪里去了？----@ResponseBody
//            return ResultDTO.errorOf(2002,"未登录不能评论，请先登录！");
            //throw new?
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        //里面的校验：比如parrentId是否存在？
        Comment comment = new Comment();//model已经自动生成了相应的数据模型：comment
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId()); //这里注意一下！核对一下源代码
        comment.setLikeCount(0L);

        commentService.insert(comment);
//        Map<Object,Object> objectObjectHashMap =new HashMap<>();
//        objectObjectHashMap.put("messsage","成功");
        return  ResultDTO.okOf();//直接返回对象
    }
}

