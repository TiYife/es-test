package es.entity.jpaEntity;

import javax.persistence.*;

@Entity
@Table(name = "xml", schema = "esWeb", catalog = "")
public class XmlEntity {
    private String id;
    private String location;
    private byte up;
    private byte del;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Basic
    @Column(name = "up")
    public byte getUp() {
        return up;
    }

    public void setUp(byte up) {
        this.up = up;
    }

    @Basic
    @Column(name = "del")
    public byte getDel() {
        return del;
    }

    public void setDel(byte del) {
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
        result = 31 * result + (int) up;
        result = 31 * result + (int) del;
        return result;
    }
}
