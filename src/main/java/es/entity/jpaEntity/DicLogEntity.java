package es.entity.jpaEntity;

import javax.persistence.*;

/**
 * Created by TYF on 2018/5/27.
 */
@Entity
@Table(name = "dic_log", schema = "esWeb", catalog = "")
public class DicLogEntity {
    private int id;
    private String operateType;
    private String operateTarget;
    private String operateTime;
    private int operateUserId;
    private String operateDetail;
    private int operateFlag;
    private String remark;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "operate_type", nullable = false, length = 45)
    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    @Basic
    @Column(name = "operate_target", nullable = false, length = 45)
    public String getOperateTarget() {
        return operateTarget;
    }

    public void setOperateTarget(String operateTarget) {
        this.operateTarget = operateTarget;
    }

    @Basic
    @Column(name = "operate_time", nullable = false, length = 45)
    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    @Basic
    @Column(name = "operate_user_id", nullable = false)
    public int getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(int operateUserId) {
        this.operateUserId = operateUserId;
    }

    @Basic
    @Column(name = "operate_detail", nullable = true, length = 100)
    public String getOperateDetail() {
        return operateDetail;
    }

    public void setOperateDetail(String operateDetail) {
        this.operateDetail = operateDetail;
    }

    @Basic
    @Column(name = "operate_flag", nullable = false)
    public int getOperateFlag() {
        return operateFlag;
    }

    public void setOperateFlag(int operateFlag) {
        this.operateFlag = operateFlag;
    }

    @Basic
    @Column(name = "remark", nullable = true, length = 300)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DicLogEntity that = (DicLogEntity) o;

        if (id != that.id) return false;
        if (operateUserId != that.operateUserId) return false;
        if (operateFlag != that.operateFlag) return false;
        if (operateType != null ? !operateType.equals(that.operateType) : that.operateType != null) return false;
        if (operateTarget != null ? !operateTarget.equals(that.operateTarget) : that.operateTarget != null)
            return false;
        if (operateTime != null ? !operateTime.equals(that.operateTime) : that.operateTime != null) return false;
        if (operateDetail != null ? !operateDetail.equals(that.operateDetail) : that.operateDetail != null)
            return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (operateType != null ? operateType.hashCode() : 0);
        result = 31 * result + (operateTarget != null ? operateTarget.hashCode() : 0);
        result = 31 * result + (operateTime != null ? operateTime.hashCode() : 0);
        result = 31 * result + operateUserId;
        result = 31 * result + (operateDetail != null ? operateDetail.hashCode() : 0);
        result = 31 * result + operateFlag;
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }
}
