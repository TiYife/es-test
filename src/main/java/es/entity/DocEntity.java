package es.entity;

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
    private String docId;
    private String caseName;
    private String courtName;
    private String courtLevel;
    private String courtProvince;
    private String courtCity;
    private String courtCountry;
    private String courtRegion;
    private boolean isExpUnit;//是否是试点法院
    private String courtType;
    private String caseType;
    private String trialProcedure;//审批程序
    private String docType;
    private String caseNo;
    private String caseCause;
    private String client;
    private String judge;
    private String clerk;
    private String trialDate;
    private String trialMonth;
    private String trialYear;
    private String courtLevelOne;
    private String courtLevelTwo;
    private String courtLevelThree;
    private String courtLevelFour;
    private String courtSuperior;
    private String content;
    private String courtId;

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

    public String getCourtCity() {
        return courtCity;
    }

    public void setCourtCity(String courtCity) {
        this.courtCity = courtCity;
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

    public boolean isExpUnit() {
        return isExpUnit;
    }

    public void setExpUnit(boolean expUnit) {
        isExpUnit = expUnit;
    }

    public String getCourtType() {
        return courtType;
    }

    public void setCourtType(String courtType) {
        this.courtType = courtType;
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

    public String getCourtLevelOne() {
        return courtLevelOne;
    }

    public void setCourtLevelOne(String courtLevelOne) {
        this.courtLevelOne = courtLevelOne;
    }

    public String getCourtLevelTwo() {
        return courtLevelTwo;
    }

    public void setCourtLevelTwo(String courtLevelTwo) {
        this.courtLevelTwo = courtLevelTwo;
    }

    public String getCourtLevelThree() {
        return courtLevelThree;
    }

    public void setCourtLevelThree(String courtLevelThree) {
        this.courtLevelThree = courtLevelThree;
    }

    public String getCourtLevelFour() {
        return courtLevelFour;
    }

    public void setCourtLevelFour(String courtLevelFour) {
        this.courtLevelFour = courtLevelFour;
    }

    public String getCourtSuperior() {
        return courtSuperior;
    }

    public void setCourtSuperior(String courtSuperior) {
        this.courtSuperior = courtSuperior;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCourtLevel() {
        return courtLevel;
    }

    public void setCourtLevel(String courtLevel) {
        this.courtLevel = courtLevel;
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

    public String getCourtId() {
        return courtId;
    }

    public void setCourtId(String courtId) {
        this.courtId = courtId;
    }
}
