package es.entity.esEntity;

import es.Constant;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * Created by TYF on 2018/1/25.
 */
@Document(indexName = Constant.INDEX_NAME, type = "law")
public class DocEntity implements Serializable{
    @Id
    public String docId;
    public String caseName;
    public String courtName;
    public String courtProvince;
    public String courtCountry;
    public String courtRegion;
    public String caseType;
    public String trialProcedure;//审批程序
    public String docType;
    public String caseNo;
    public String caseCause;
    public String client;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public String getCourtProvince() {
        return courtProvince;
    }

    public void setCourtProvince(String courtProvince) {
        this.courtProvince = courtProvince;
    }

    public String getCourtCountry() {
        return courtCountry;
    }

    public void setCourtCountry(String courtCountry) {
        this.courtCountry = courtCountry;
    }

    public String getCourtRegion() {
        return courtRegion;
    }

    public void setCourtRegion(String courtRegion) {
        this.courtRegion = courtRegion;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getTrialProcedure() {
        return trialProcedure;
    }

    public void setTrialProcedure(String trialProcedure) {
        this.trialProcedure = trialProcedure;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getCaseCause() {
        return caseCause;
    }

    public void setCaseCause(String caseCause) {
        this.caseCause = caseCause;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public String getClerk() {
        return clerk;
    }

    public void setClerk(String clerk) {
        this.clerk = clerk;
    }

    public String getTrialDate() {
        return trialDate;
    }

    public void setTrialDate(String trialDate) {
        this.trialDate = trialDate;
    }

    public String getTrialMonth() {
        return trialMonth;
    }

    public void setTrialMonth(String trialMonth) {
        this.trialMonth = trialMonth;
    }

    public String getTrialYear() {
        return trialYear;
    }

    public void setTrialYear(String trialYear) {
        this.trialYear = trialYear;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCourtId() {
        return courtId;
    }

    public void setCourtId(String courtId) {
        this.courtId = courtId;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String judge;
    public String clerk;
    public String trialDate;
    public String trialMonth;
    public String trialYear;
    public String content;
    public String courtId;
    public String keyWord;

}
