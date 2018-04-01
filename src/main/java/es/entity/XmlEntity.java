package es.entity;

import javax.persistence.*;

/**
 * Created by TYF on 2018/4/1.
 */
@Entity
@Table(name = "xml", schema = "esWeb", catalog = "")
public class XmlEntity {
    private String id;
    private String location;
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
    @Column(name = "location", nullable = false, length = 45)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

        XmlEntity xmlEntity = (XmlEntity) o;

        if (upload != xmlEntity.upload) return false;
        if (delete != xmlEntity.delete) return false;
        if (id != null ? !id.equals(xmlEntity.id) : xmlEntity.id != null) return false;
        if (location != null ? !location.equals(xmlEntity.location) : xmlEntity.location != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (upload ? 1 : 0);
        result = 31 * result + (delete ? 1 : 0);
        return result;
    }
}
