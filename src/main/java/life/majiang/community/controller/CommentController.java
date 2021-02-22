package life.majiang.community.controller;

import life.majiang.community.dto.CommentCreateDTo;
import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.ResultDTO;
import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.model.Comment;
import life.majiang.community.model.User;
import life.majiang.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTo commentCreateDTo,
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

        if(commentCreateDTo==null || StringUtils.isBlank(commentCreateDTo.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        //里面的校验：比如parrentId是否存在？
        Comment comment = new Comment();//model已经自动生成了相应的数据模型：comment
        comment.setParentId(commentCreateDTo.getParentId());
        comment.setContent(commentCreateDTo.getContent());
        comment.setType(commentCreateDTo.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId()); //这里注意一下！核对一下源代码
        comment.setLikeCount(0L);
        commentService.insert(comment,user);
//        Map<Object,Object> objectObjectHashMap =new HashMap<>();
//        objectObjectHashMap.put("messsage","成功");
        return  ResultDTO.okOf();//直接返回对象
    }


    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public  ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id")Long id) {
        List<CommentDTO> commentDTOS=commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
        }
}

