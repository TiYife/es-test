package es.entity.jpaEntity;

import javax.persistence.*;

/**
 * Created by TYF on 2018/5/24.
 */
@Entity
@Table(name = "ori_doc", schema = "esWeb", catalog = "")
public class OriDocEntity {
    private String id;
    private String name;
    private String location;
    private String upTime;
    private String saveTime;
    private Boolean isSave;
    private Boolean isDel;
    private Integer uploader;
    private String oriDoccol;

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
    @Column(name = "location", nullable = false, length = 200)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
    @Column(name = "is_save", nullable = true)
    public Boolean getIsSave() {
        return isSave;
    }

    public void setIsSave(Boolean save) {
        isSave = save;
    }

    @Basic
    @Column(name = "is_del", nullable = true)
    public Boolean getIsDel() {
        return isDel;
    }

    public void setIsDel(Boolean del) {
        isDel = del;
    }

    @Basic
    @Column(name = "uploader", nullable = true)
    public Integer getUploader() {
        return uploader;
    }

    public void setUploader(Integer uploader) {
        this.uploader = uploader;
    }

    @Basic
    @Column(name = "ori_doccol", nullable = true, length = 45)
    public String getOriDoccol() {
        return oriDoccol;
    }

    public void setOriDoccol(String oriDoccol) {
        this.oriDoccol = oriDoccol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OriDocEntity entity = (OriDocEntity) o;

        if (id != null ? !id.equals(entity.id) : entity.id != null) return false;
        if (name != null ? !name.equals(entity.name) : entity.name != null) return false;
        if (location != null ? !location.equals(entity.location) : entity.location != null) return false;
        if (upTime != null ? !upTime.equals(entity.upTime) : entity.upTime != null) return false;
        if (saveTime != null ? !saveTime.equals(entity.saveTime) : entity.saveTime != null) return false;
        if (isSave != null ? !isSave.equals(entity.isSave) : entity.isSave != null) return false;
        if (isDel != null ? !isDel.equals(entity.isDel) : entity.isDel != null) return false;
        if (uploader != null ? !uploader.equals(entity.uploader) : entity.uploader != null) return false;
        if (oriDoccol != null ? !oriDoccol.equals(entity.oriDoccol) : entity.oriDoccol != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (upTime != null ? upTime.hashCode() : 0);
        result = 31 * result + (saveTime != null ? saveTime.hashCode() : 0);
        result = 31 * result + (isSave != null ? isSave.hashCode() : 0);
        result = 31 * result + (isDel != null ? isDel.hashCode() : 0);
        result = 31 * result + (uploader != null ? uploader.hashCode() : 0);
        result = 31 * result + (oriDoccol != null ? oriDoccol.hashCode() : 0);
        return result;
    }
}
