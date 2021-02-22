package life.majiang.community.enums;

//为什么要创建枚举：为了让代码意义清晰
public enum NotificationStatusEnum {
    UNREAD(0),
    READ(1);

    private  int status;

    public int getStatus() {
        return status;
    }

    NotificationStatusEnum(int status) {
        this.status = status;
    }
}
