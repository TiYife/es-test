package es.entity.jpaEntity;

import javax.persistence.*;

/**
 * Created by TYF on 2018/6/3.
 */
@Entity
@Table(name = "up_log", schema = "esWeb", catalog = "")
public class UpLogEntity {
    private String id;
    private String fileName;
    private String newName;
    private int uploader;
    private String upTime;
    private String location;
    private Integer isSave;
    private Integer isDel;
    private String saveTime;
    private String delTime;

    @Id
    @Column(name = "id", nullable = false, length = 50)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "file_name", nullable = false, length = 100)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Basic
    @Column(name = "new_name", nullable = false, length = 200)
    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    @Basic
    @Column(name = "uploader", nullable = false)
    public int getUploader() {
        return uploader;
    }

    public void setUploader(int uploader) {
        this.uploader = uploader;
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
    @Column(name = "location", nullable = false, length = 200)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Basic
    @Column(name = "is_save", nullable = true)
    public Integer getIsSave() {
        return isSave;
    }

    public void setIsSave(Integer isSave) {
        this.isSave = isSave;
    }

    @Basic
    @Column(name = "is_del", nullable = true)
    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
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
    @Column(name = "del_time", nullable = true, length = 45)
    public String getDelTime() {
        return delTime;
    }

    public void setDelTime(String delTime) {
        this.delTime = delTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpLogEntity entity = (UpLogEntity) o;

        if (uploader != entity.uploader) return false;
        if (id != null ? !id.equals(entity.id) : entity.id != null) return false;
        if (fileName != null ? !fileName.equals(entity.fileName) : entity.fileName != null) return false;
        if (newName != null ? !newName.equals(entity.newName) : entity.newName != null) return false;
        if (upTime != null ? !upTime.equals(entity.upTime) : entity.upTime != null) return false;
        if (location != null ? !location.equals(entity.location) : entity.location != null) return false;
        if (isSave != null ? !isSave.equals(entity.isSave) : entity.isSave != null) return false;
        if (isDel != null ? !isDel.equals(entity.isDel) : entity.isDel != null) return false;
        if (saveTime != null ? !saveTime.equals(entity.saveTime) : entity.saveTime != null) return false;
        if (delTime != null ? !delTime.equals(entity.delTime) : entity.delTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (newName != null ? newName.hashCode() : 0);
        result = 31 * result + uploader;
        result = 31 * result + (upTime != null ? upTime.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (isSave != null ? isSave.hashCode() : 0);
        result = 31 * result + (isDel != null ? isDel.hashCode() : 0);
        result = 31 * result + (saveTime != null ? saveTime.hashCode() : 0);
        result = 31 * result + (delTime != null ? delTime.hashCode() : 0);
        return result;
    }
}
