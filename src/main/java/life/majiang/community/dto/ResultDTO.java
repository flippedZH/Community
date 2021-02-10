package life.majiang.community.dto;


import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import lombok.Data;
import org.springframework.web.servlet.ModelAndView;

@Data
public class ResultDTO <T>{
    private Integer code;
    private String message;
    private T data;

    public  static  ResultDTO errorOf(Integer code, String message){
        ResultDTO resultDTO =new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return  resultDTO;
    }

    //重载的方法：
    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        return  errorOf(errorCode.getCode(),errorCode.getMessage());
    }

    public static  ResultDTO okOf(){
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    public static  <T> ResultDTO okOf(T t){
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }

    //  e-->errorOf--->得到对象
    //继续重载：拿到抛出的异常中的消息
    public static ResultDTO errorOf(CustomizeException e) {
        return errorOf(e.getCode(),e.getMessage());
    }
}
