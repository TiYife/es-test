package es.entity.jpaEntity;

import javax.persistence.*;

@Entity
@Table(name = "ori_doc", schema = "esWeb", catalog = "")
public class OriDocEntity {
    private String id;
    private String location;
    private String upTime;
    private String saveTime;
    private Byte isSave;
    private Byte isDel;
    private Integer uploader;

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
    @Column(name = "up_time")
    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    @Basic
    @Column(name = "save_time")
    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }

    @Basic
    @Column(name = "is_save")
    public Byte getIsSave() {
        return isSave;
    }

    public void setIsSave(Byte isSave) {
        this.isSave = isSave;
    }

    @Basic
    @Column(name = "is_del")
    public Byte getIsDel() {
        return isDel;
    }

    public void setIsDel(Byte isDel) {
        this.isDel = isDel;
    }

    @Basic
    @Column(name = "uploader")
    public Integer getUploader() {
        return uploader;
    }

    public void setUploader(Integer uploader) {
        this.uploader = uploader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OriDocEntity that = (OriDocEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (upTime != null ? !upTime.equals(that.upTime) : that.upTime != null) return false;
        if (saveTime != null ? !saveTime.equals(that.saveTime) : that.saveTime != null) return false;
        if (isSave != null ? !isSave.equals(that.isSave) : that.isSave != null) return false;
        if (isDel != null ? !isDel.equals(that.isDel) : that.isDel != null) return false;
        if (uploader != null ? !uploader.equals(that.uploader) : that.uploader != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (upTime != null ? upTime.hashCode() : 0);
        result = 31 * result + (saveTime != null ? saveTime.hashCode() : 0);
        result = 31 * result + (isSave != null ? isSave.hashCode() : 0);
        result = 31 * result + (isDel != null ? isDel.hashCode() : 0);
        result = 31 * result + (uploader != null ? uploader.hashCode() : 0);
        return result;
    }
}
