import java.util.Date;

class ActionLog {

    private String id;

    private int status;

    private Date createTime, updateTime;

    ActionLog(String id, int status, Date createTime, Date updateTime) {
        this.id = id;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override public String toString() {
        return "ActionLog{" +
            "id='" + id + '\'' +
            ", status=" + status +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            '}';
    }
}
