package life.majiang.community.dto;

import lombok.Data;

@Data
public class CommentCreateDTo {
    private long parentId;
    private String content;
    private Integer type;
}
