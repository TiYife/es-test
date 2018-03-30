package es.entity;

import javax.persistence.*;

/**
 * Created by TYF on 2018/3/30.
 */
@Entity
@Table(name = "document", schema = "esWeb", catalog = "")
public class DocumentEntity {
    private String id;
    private boolean upload;
    private boolean delete;

    @Id
    @Column(name = "id", nullable = false, length = 45)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "upload", nullable = false)
    public boolean isUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    @Basic
    @Column(name = "delete", nullable = false)
    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocumentEntity that = (DocumentEntity) o;

        if (upload != that.upload) return false;
        if (delete != that.delete) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (upload ? 1 : 0);
        result = 31 * result + (delete ? 1 : 0);
        return result;
    }
}
