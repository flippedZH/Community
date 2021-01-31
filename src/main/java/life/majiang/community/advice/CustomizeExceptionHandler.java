package life.majiang.community.advice;


import com.alibaba.fastjson.JSON;
import life.majiang.community.dto.ResultDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice //扫描所有
public class CustomizeExceptionHandler {


    //Exception.class表示处理所有ExceptioN
    @ExceptionHandler(Exception.class)
    ModelAndView handle( Throwable e, Model model,
                HttpServletRequest request,
                   HttpServletResponse response
    ) {
        System.out.println("调试："+e);
        //ModelAndView是页面元素？
        String contentType=request.getContentType();

        //不能 同时用 Object去接收 ResultDTO与ModelAndView
        if("application/json".equals(contentType)){
            ResultDTO resultDTO;
            //返回json

            if (e instanceof CustomizeException){
                resultDTO= ResultDTO.errorOf((CustomizeException) e);

            }else {
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);//(ResultDTO)

            }
        try{
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer=response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            }catch(IOException ioe){}
        return null;


        }else {
            //错误页面跳转
            if(e instanceof CustomizeException){
                model.addAttribute("message",e.getMessage());
            }else {
                model.addAttribute("message",CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");//返回数据与页面
        }
    }
}
