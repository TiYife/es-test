package es.entity.jpaEntity;

import javax.persistence.*;

/**
 * Created by TYF on 2018/5/27.
 */
@Entity
@Table(name = "dic", schema = "esWeb", catalog = "")
public class DicEntity {
    private int id;
    private String word;
    private String type;
    private String sepaType;
    private Integer parentId;
    private String createTime;
    private int createUserId;
    private int createId;
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
    @Column(name = "word", nullable = false, length = 30)
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Basic
    @Column(name = "type", nullable = false, length = 20)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "sepa_type", nullable = false, length = 45)
    public String getSepaType() {
        return sepaType;
    }

    public void setSepaType(String sepaType) {
        this.sepaType = sepaType;
    }

    @Basic
    @Column(name = "parent_id", nullable = true)
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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
    @Column(name = "create_user_id", nullable = false)
    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    @Basic
    @Column(name = "create_id", nullable = false)
    public int getCreateId() {
        return createId;
    }

    public void setCreateId(int createId) {
        this.createId = createId;
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

        DicEntity dicEntity = (DicEntity) o;

        if (id != dicEntity.id) return false;
        if (createUserId != dicEntity.createUserId) return false;
        if (createId != dicEntity.createId) return false;
        if (word != null ? !word.equals(dicEntity.word) : dicEntity.word != null) return false;
        if (type != null ? !type.equals(dicEntity.type) : dicEntity.type != null) return false;
        if (sepaType != null ? !sepaType.equals(dicEntity.sepaType) : dicEntity.sepaType != null) return false;
        if (parentId != null ? !parentId.equals(dicEntity.parentId) : dicEntity.parentId != null) return false;
        if (createTime != null ? !createTime.equals(dicEntity.createTime) : dicEntity.createTime != null) return false;
        if (remark != null ? !remark.equals(dicEntity.remark) : dicEntity.remark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (word != null ? word.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (sepaType != null ? sepaType.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + createUserId;
        result = 31 * result + createId;
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }
}
