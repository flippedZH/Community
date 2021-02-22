package life.majiang.community.service;

import life.majiang.community.dto.NotificationDTO;
import life.majiang.community.dto.PaginnationDTO;
import life.majiang.community.dto.QuestionDto;
import life.majiang.community.enums.NotificationStatusEnum;
import life.majiang.community.enums.NotificationTypeEnum;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.NotificationMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    //通知的分页函数
    public PaginnationDTO list(Long userId, Integer page, Integer size) {

        PaginnationDTO<NotificationDTO> paginnationDTO =new PaginnationDTO();

        Integer totalPage;

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer  totalCount=(int)notificationMapper.countByExample(notificationExample);

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

        NotificationExample example = new NotificationExample();
        //List<Question> questions =questionMapper.listByUserId(userId,offset,size);//与数据库交互，从数据库中获取到指定数目的数据
        example.createCriteria().andReceiverEqualTo(userId);
        List<Notification> notifications=notificationMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));//与数据库交互，从数据库中获取到指定数目的数据

        if(notifications.size()==0){
            return  paginnationDTO;
        }

//        Set<Long> disUserIds=notifications.stream().map(notify-> notify.getNotifier()).collect(Collectors.toSet());
//        List<Long> userIds=new ArrayList<>(disUserIds);
//        UserExample userExample=new UserExample();
//        userExample.createCriteria().andIdIn(userIds);
//        List<User> users=userMapper.selectByExample(userExample);
//        Map<Long,User> userMap=users.stream().collect(Collectors.toMap(u->u.getId(), u->u));

        List<NotificationDTO> notificationDTOS =new ArrayList<>();

        for (Notification notification : notifications) {
            System.out.println("notifications:"+notification);
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);

            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }

        paginnationDTO.setData(notificationDTOS);
        return paginnationDTO;
    }

    public Long unreadCount(Long userId) {
        NotificationExample notificationExample =new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!notification.getReceiver().equals(user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;

    }
}


    //通过用户id查找到的问题的数据模型:实现我的问题功能
