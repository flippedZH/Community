package life.majiang.community.interceptor;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Notification;
import life.majiang.community.model.User;
import life.majiang.community.model.UserExample;
import life.majiang.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


//将userMapper注入进来
//因为SessionInterceptor不是spring接管的bean
@Service
public class SessionInterceptor implements HandlerInterceptor {

    //注入
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies=request.getCookies();
        if(cookies!=null&&cookies.length!=0){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("token")){
                    String token=cookie.getValue();
                    UserExample userExample = new UserExample();
                    userExample.createCriteria().
                            andTokenEqualTo(token);
                   List<User> users =userMapper.selectByExample(userExample);
                    if(users.size()!=0){
                        //数据（List中的第一个元素）给前端口
                        request.getSession().setAttribute("user",users.get(0));

                        Long unreadCount = notificationService.unreadCount(users.get(0).getId());
                        //注入前端
                        request.getSession().setAttribute("unreadCount", unreadCount);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
