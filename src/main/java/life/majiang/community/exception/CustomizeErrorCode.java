package life.majiang.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{

    //2001--2开头表示系统级别的错误

    //Question
    QUESTION_NOT_FOUND(2001,"你找的问题不在了"),
    //Comment
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGIN(2003,"当前操作需要登录，请登录后重试"),
    SYS_ERROR(2004,"服务器故障"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"你回复的评论不存在"),
    CONTENT_IS_EMPTY(2007,"输入内容不能为空");



    @Override
    public  String getMessage(){
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    //属性与方法都是与上面的常量语句中的参数是一一对应的！！
    private  String message;
    private Integer code;

    //Ctrl+F6全局依赖更改
    CustomizeErrorCode(Integer code,String message){
        this.message =message;
        this.code=code;
    }
}

