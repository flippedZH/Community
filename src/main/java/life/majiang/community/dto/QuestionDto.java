package life.majiang.community.dto;

import life.majiang.community.model.User;
import lombok.Data;

//传输数据模型
@Data
public class QuestionDto {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
