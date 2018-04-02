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
    private boolean up;
    private boolean del;

    @Id
    @Column(name = "id", nullable = false, length = 45)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "location", nullable = false, length = 200)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Basic
    @Column(name = "up", nullable = false)
    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    @Basic
    @Column(name = "del", nullable = false)
    public boolean isDel() {
        return del;
    }

    public void setDel(boolean del) {
        this.del = del;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XmlEntity xmlEntity = (XmlEntity) o;

        if (up != xmlEntity.up) return false;
        if (del != xmlEntity.del) return false;
        if (id != null ? !id.equals(xmlEntity.id) : xmlEntity.id != null) return false;
        if (location != null ? !location.equals(xmlEntity.location) : xmlEntity.location != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (up ? 1 : 0);
        result = 31 * result + (del ? 1 : 0);
        return result;
    }
}
