package life.majiang.community.exception;

public class CustomizeException extends RuntimeException {

    //code的透传（到exception这一个下一层级）
    private Integer code;
    private String message;


    //构造方法传参
    public CustomizeException(ICustomizeErrorCode errorCode){
        this.message=errorCode.getMessage();
        this.code=errorCode.getCode();
    }

    //
    @Override  //@Override本身不具有实际意义，只是检查
    //不加@Override也是进行了覆写
    //覆写的目的就是异常排抛出的时候返回的是我们自己定义的消息！
    //覆写就直接调用子类方法
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
