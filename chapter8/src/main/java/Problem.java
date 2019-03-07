import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("problems")
class Problem {

    @Column private String id;

    @Column private int number;

    @Column private String url;

    @Column private int point;

    @Column private String contestId;

    @Column private Date createTime;

    @Column private Date updateTime;

    Problem(String id, int number, String url, int point,
            String contestId, Date createTime, Date updateTime) {
        this.id = id;
        this.number = number;
        this.url = url;
        this.point = point;
        this.contestId = contestId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override public String toString() {
        return "Problem{" +
            "id='" + id + '\'' +
            ", number=" + number +
            ", url='" + url + '\'' +
            ", point=" + point +
            ", contestId='" + contestId + '\'' +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            '}';
    }

}
