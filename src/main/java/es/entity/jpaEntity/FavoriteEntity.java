package es.entity.jpaEntity;

import javax.persistence.*;

/**
 * Created by TYF on 2018/5/21.
 */
@Entity
@Table(name = "favorite", schema = "esWeb", catalog = "")
public class FavoriteEntity {
    private int id;
    private Integer userId;
    private String docId;
    private String docName;
    private String favorTime;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_id", nullable = true)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "doc_id", nullable = true, length = 255)
    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    @Basic
    @Column(name = "doc_name", nullable = true, length = 255)
    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    @Basic
    @Column(name = "favor_time", nullable = true, length = 100)
    public String getFavorTime() {
        return favorTime;
    }

    public void setFavorTime(String favorTime) {
        this.favorTime = favorTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FavoriteEntity that = (FavoriteEntity) o;

        if (id != that.id) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (docId != null ? !docId.equals(that.docId) : that.docId != null) return false;
        if (docName != null ? !docName.equals(that.docName) : that.docName != null) return false;
        if (favorTime != null ? !favorTime.equals(that.favorTime) : that.favorTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (docId != null ? docId.hashCode() : 0);
        result = 31 * result + (docName != null ? docName.hashCode() : 0);
        result = 31 * result + (favorTime != null ? favorTime.hashCode() : 0);
        return result;
    }
}
