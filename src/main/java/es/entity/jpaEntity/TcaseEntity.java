package es.entity.jpaEntity;

import javax.persistence.*;

/**
 * Created by TYF on 2018/6/7.
 */
@Entity
@Table(name = "tcase", schema = "esWeb", catalog = "")
public class TcaseEntity {
    private int docId;
    private String caseType;
    private String caseName;
    private String courtName;
    private String docType;
    private String caseNo;
    private String client;
    private String clerk;
    private String trailDate;
    private String judge;
    private String content;
    private String courtId;
    private Integer trailYear;
    private String courtProvince;
    private String caseCause;
    private String trailProcedure;

    @Id
    @Column(name = "doc_id", nullable = false)
    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    @Basic
    @Column(name = "case_type", nullable = true, length = 32)
    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    @Basic
    @Column(name = "case_name", nullable = true, length = 3024)
    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    @Basic
    @Column(name = "court_name", nullable = true, length = 3024)
    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    @Basic
    @Column(name = "doc_type", nullable = true, length = 3024)
    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    @Basic
    @Column(name = "case_no", nullable = true, length = 3024)
    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    @Basic
    @Column(name = "client", nullable = true, length = -1)
    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @Basic
    @Column(name = "CLERK", nullable = true, length = 512)
    public String getClerk() {
        return clerk;
    }

    public void setClerk(String clerk) {
        this.clerk = clerk;
    }

    @Basic
    @Column(name = "trail_date", nullable = true, length = 3024)
    public String getTrailDate() {
        return trailDate;
    }

    public void setTrailDate(String trailDate) {
        this.trailDate = trailDate;
    }

    @Basic
    @Column(name = "judge", nullable = true, length = 5014)
    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    @Basic
    @Column(name = "content", nullable = true, length = -1)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "court_id", nullable = true, length = 128)
    public String getCourtId() {
        return courtId;
    }

    public void setCourtId(String courtId) {
        this.courtId = courtId;
    }

    @Basic
    @Column(name = "trail_year", nullable = true)
    public Integer getTrailYear() {
        return trailYear;
    }

    public void setTrailYear(Integer trailYear) {
        this.trailYear = trailYear;
    }

    @Basic
    @Column(name = "court_province", nullable = true, length = 16)
    public String getCourtProvince() {
        return courtProvince;
    }

    public void setCourtProvince(String courtProvince) {
        this.courtProvince = courtProvince;
    }

    @Basic
    @Column(name = "case_cause", nullable = true, length = 8)
    public String getCaseCause() {
        return caseCause;
    }

    public void setCaseCause(String caseCause) {
        this.caseCause = caseCause;
    }

    @Basic
    @Column(name = "trail_procedure", nullable = true, length = 8)
    public String getTrailProcedure() {
        return trailProcedure;
    }

    public void setTrailProcedure(String trailProcedure) {
        this.trailProcedure = trailProcedure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TcaseEntity that = (TcaseEntity) o;

        if (docId != that.docId) return false;
        if (caseType != null ? !caseType.equals(that.caseType) : that.caseType != null) return false;
        if (caseName != null ? !caseName.equals(that.caseName) : that.caseName != null) return false;
        if (courtName != null ? !courtName.equals(that.courtName) : that.courtName != null) return false;
        if (docType != null ? !docType.equals(that.docType) : that.docType != null) return false;
        if (caseNo != null ? !caseNo.equals(that.caseNo) : that.caseNo != null) return false;
        if (client != null ? !client.equals(that.client) : that.client != null) return false;
        if (clerk != null ? !clerk.equals(that.clerk) : that.clerk != null) return false;
        if (trailDate != null ? !trailDate.equals(that.trailDate) : that.trailDate != null) return false;
        if (judge != null ? !judge.equals(that.judge) : that.judge != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (courtId != null ? !courtId.equals(that.courtId) : that.courtId != null) return false;
        if (trailYear != null ? !trailYear.equals(that.trailYear) : that.trailYear != null) return false;
        if (courtProvince != null ? !courtProvince.equals(that.courtProvince) : that.courtProvince != null)
            return false;
        if (caseCause != null ? !caseCause.equals(that.caseCause) : that.caseCause != null) return false;
        if (trailProcedure != null ? !trailProcedure.equals(that.trailProcedure) : that.trailProcedure != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = docId;
        result = 31 * result + (caseType != null ? caseType.hashCode() : 0);
        result = 31 * result + (caseName != null ? caseName.hashCode() : 0);
        result = 31 * result + (courtName != null ? courtName.hashCode() : 0);
        result = 31 * result + (docType != null ? docType.hashCode() : 0);
        result = 31 * result + (caseNo != null ? caseNo.hashCode() : 0);
        result = 31 * result + (client != null ? client.hashCode() : 0);
        result = 31 * result + (clerk != null ? clerk.hashCode() : 0);
        result = 31 * result + (trailDate != null ? trailDate.hashCode() : 0);
        result = 31 * result + (judge != null ? judge.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (courtId != null ? courtId.hashCode() : 0);
        result = 31 * result + (trailYear != null ? trailYear.hashCode() : 0);
        result = 31 * result + (courtProvince != null ? courtProvince.hashCode() : 0);
        result = 31 * result + (caseCause != null ? caseCause.hashCode() : 0);
        result = 31 * result + (trailProcedure != null ? trailProcedure.hashCode() : 0);
        return result;
    }
}
