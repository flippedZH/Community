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

    //通知的分页函数 返回分页后的通知DTO  目的是列表化展示我的通知
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
        //setPagination为自己写的分页逻辑函数
        paginnationDTO.setPagination(totalPage,page);//为了设计逻辑函数往数据模型中传值（加值）
        Integer offset=size*(page-1);
        //数据库查询
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        //按照时间倒序排序
        example.setOrderByClause("gmt_create desc");
        //与数据库交互，从数据库中获取到指定数目的数据 完成分页
        //org.mybatis.generator.plugins.RowBoundsPlugin分页插件
        List<Notification> notifications=notificationMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));
        if(notifications.size()==0){
            return  paginnationDTO;
        }

        //创建通知列表
        //属性copy并设置typeName
        List<NotificationDTO> notificationDTOS =new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            //就是给type定义了一个中文名字，就是用枚举赋值
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }
        paginnationDTO.setData(notificationDTOS);

        return paginnationDTO;
    }


    public Long unreadCount(Long userId) {
        NotificationExample notificationExample =new NotificationExample();
        //通过拼接，连接数据库查询条件
        notificationExample.createCriteria().andReceiverEqualTo(userId).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        //countByExample表示通过条件计数？
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
