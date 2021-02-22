package life.majiang.community.dto;

import life.majiang.community.model.User;
import lombok.Data;

import java.util.List;

@Data
public class NotificationDTO {

    private Long id;
    private Long gmtCreate;
    private Integer status;
    private User user;
    private Long notifier;
    private Long outerid;
    private String notifierName;
    private  String outerTitle;
    private String typeName;
    private Integer type;
}

