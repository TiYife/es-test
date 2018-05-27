package es.entity.jpaEntity;

import javax.persistence.*;

/**
 * Created by TYF on 2018/5/27.
 */
@Entity
@Table(name = "new_word", schema = "esWeb", catalog = "")
public class NewWordEntity {
    private int id;
    private String word;
    private String createTime;
    private String createLocation;
    private String context;
    private String sepaType;
    private int processFlag;
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
    @Column(name = "word", nullable = false, length = 45)
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Basic
    @Column(name = "create_time", nullable = false, length = 45)
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "create_location", nullable = false, length = 100)
    public String getCreateLocation() {
        return createLocation;
    }

    public void setCreateLocation(String createLocation) {
        this.createLocation = createLocation;
    }

    @Basic
    @Column(name = "context", nullable = false, length = 1000)
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Basic
    @Column(name = "sepa_type", nullable = true, length = 45)
    public String getSepaType() {
        return sepaType;
    }

    public void setSepaType(String sepaType) {
        this.sepaType = sepaType;
    }

    @Basic
    @Column(name = "process_flag", nullable = false)
    public int getProcessFlag() {
        return processFlag;
    }

    public void setProcessFlag(int processFlag) {
        this.processFlag = processFlag;
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

        NewWordEntity that = (NewWordEntity) o;

        if (id != that.id) return false;
        if (processFlag != that.processFlag) return false;
        if (word != null ? !word.equals(that.word) : that.word != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (createLocation != null ? !createLocation.equals(that.createLocation) : that.createLocation != null)
            return false;
        if (context != null ? !context.equals(that.context) : that.context != null) return false;
        if (sepaType != null ? !sepaType.equals(that.sepaType) : that.sepaType != null) return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (word != null ? word.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (createLocation != null ? createLocation.hashCode() : 0);
        result = 31 * result + (context != null ? context.hashCode() : 0);
        result = 31 * result + (sepaType != null ? sepaType.hashCode() : 0);
        result = 31 * result + processFlag;
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }
}
