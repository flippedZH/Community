package life.majiang.community.dto;
import life.majiang.community.model.User;
import lombok.Data;


//向前端输送的内容
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    //多出来的
    private User user;

    private Integer commentCount;






}