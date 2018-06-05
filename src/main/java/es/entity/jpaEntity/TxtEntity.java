package es.entity.jpaEntity;

import javax.persistence.*;

/**
 * Created by TYF on 2018/6/4.
 */
@Entity
@Table(name = "txt", schema = "esWeb", catalog = "")
public class TxtEntity {
    private String id;
    private String name;
    private String upLog;
    private String location;
    private String xmlLocation;
    private String upTime;
    private String saveTime;
    private Integer status;
    private Integer uploader;

    @Id
    @Column(name = "id", nullable = false, length = 100)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "up_log", nullable = false, length = 45)
    public String getUpLog() {
        return upLog;
    }

    public void setUpLog(String upLog) {
        this.upLog = upLog;
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
    @Column(name = "xml_location", nullable = true, length = 200)
    public String getXmlLocation() {
        return xmlLocation;
    }

    public void setXmlLocation(String xmlLocation) {
        this.xmlLocation = xmlLocation;
    }

    @Basic
    @Column(name = "up_time", nullable = false, length = 45)
    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    @Basic
    @Column(name = "save_time", nullable = true, length = 45)
    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }

    @Basic
    @Column(name = "status", nullable = true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "uploader", nullable = true)
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

        TxtEntity txtEntity = (TxtEntity) o;

        if (id != null ? !id.equals(txtEntity.id) : txtEntity.id != null) return false;
        if (name != null ? !name.equals(txtEntity.name) : txtEntity.name != null) return false;
        if (upLog != null ? !upLog.equals(txtEntity.upLog) : txtEntity.upLog != null) return false;
        if (location != null ? !location.equals(txtEntity.location) : txtEntity.location != null) return false;
        if (xmlLocation != null ? !xmlLocation.equals(txtEntity.xmlLocation) : txtEntity.xmlLocation != null)
            return false;
        if (upTime != null ? !upTime.equals(txtEntity.upTime) : txtEntity.upTime != null) return false;
        if (saveTime != null ? !saveTime.equals(txtEntity.saveTime) : txtEntity.saveTime != null) return false;
        if (status != null ? !status.equals(txtEntity.status) : txtEntity.status != null) return false;
        if (uploader != null ? !uploader.equals(txtEntity.uploader) : txtEntity.uploader != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (upLog != null ? upLog.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (xmlLocation != null ? xmlLocation.hashCode() : 0);
        result = 31 * result + (upTime != null ? upTime.hashCode() : 0);
        result = 31 * result + (saveTime != null ? saveTime.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (uploader != null ? uploader.hashCode() : 0);
        return result;
    }
}
