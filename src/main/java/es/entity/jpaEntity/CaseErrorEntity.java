package es.entity.jpaEntity;

import javax.persistence.*;

/**
 * Created by TYF on 2018/5/27.
 */
@Entity
@Table(name = "case_error", schema = "esWeb", catalog = "")
public class CaseErrorEntity {
    private int id;
    private String errorType;
    private String errorMessage;
    private String errorLocation;
    private String errorTime;
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
    @Column(name = "error_type", nullable = false, length = 20)
    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    @Basic
    @Column(name = "error_message", nullable = false, length = 100)
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Basic
    @Column(name = "error_location", nullable = false, length = 100)
    public String getErrorLocation() {
        return errorLocation;
    }

    public void setErrorLocation(String errorLocation) {
        this.errorLocation = errorLocation;
    }

    @Basic
    @Column(name = "error_time", nullable = false, length = 45)
    public String getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(String errorTime) {
        this.errorTime = errorTime;
    }

    @Basic
    @Column(name = "remark", nullable = true, length = 200)
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

        CaseErrorEntity that = (CaseErrorEntity) o;

        if (id != that.id) return false;
        if (errorType != null ? !errorType.equals(that.errorType) : that.errorType != null) return false;
        if (errorMessage != null ? !errorMessage.equals(that.errorMessage) : that.errorMessage != null) return false;
        if (errorLocation != null ? !errorLocation.equals(that.errorLocation) : that.errorLocation != null)
            return false;
        if (errorTime != null ? !errorTime.equals(that.errorTime) : that.errorTime != null) return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (errorType != null ? errorType.hashCode() : 0);
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        result = 31 * result + (errorLocation != null ? errorLocation.hashCode() : 0);
        result = 31 * result + (errorTime != null ? errorTime.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }
}
