package es.entity;

import javax.persistence.*;

/**
 * Created by TYF on 2018/3/2.
 */
@Entity
@Table(name = "documents", schema = "es", catalog = "")
public class DocumentsEntity {
    private String title;
    private boolean uploaded;
    private boolean deleted;

    @Id
    @Column(name = "title", nullable = false, length = 150)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "uploaded", nullable = false)
    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    @Basic
    @Column(name = "deleted", nullable = false)
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocumentsEntity that = (DocumentsEntity) o;

        if (uploaded != that.uploaded) return false;
        if (deleted != that.deleted) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (uploaded ? 1 : 0);
        result = 31 * result + (deleted ? 1 : 0);
        return result;
    }
}
