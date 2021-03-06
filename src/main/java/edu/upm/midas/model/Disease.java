package edu.upm.midas.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

/**
 * Created by gerardo on 22/11/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className Disease
 * @see
 */
public class Disease {

    //@JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String diseaseId;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cui;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String url;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer urlsCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> urls;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer codesCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Code> codes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer disnetConceptsCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DisnetConcept> disnetConceptList;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer documentsCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Document> documentList;


    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    public Integer getUrlsCount() {
        if (urls != null){
            urlsCount = urls.size();
        }
        return urlsCount;
    }

    public void setUrlsCount(Integer urlsCount) {
        this.urlsCount = urlsCount;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getDisnetConceptsCount() {
        if (disnetConceptList != null){
            if (disnetConceptList.size() > 0)
                disnetConceptsCount = disnetConceptList.size();
        }
        return disnetConceptsCount;
    }

    public void setDisnetConceptsCount(Integer disnetConceptsCount) {
        this.disnetConceptsCount = disnetConceptsCount;
    }

    public Integer getCodesCount() {
        if (codes != null){
            //if (codes.size() > 0) codesCount = codes.size();
            codesCount = codes.size();
        }
        return codesCount;
    }

    public void setCodesCount(Integer codesCount) {
        this.codesCount = codesCount;
    }

    public List<Code> getCodes() {
        return codes;
    }

    public void setCodes(List<Code> codes) {
        this.codes = codes;
    }

    public List<DisnetConcept> getDisnetConceptList() {
        return disnetConceptList;
    }

    public void setDisnetConceptList(List<DisnetConcept> disnetConceptList) {
        this.disnetConceptList = disnetConceptList;
    }

    public Integer getDocumentsCount() {
        if (documentList != null){
            if (documentList.size() > 0)
                documentsCount = documentList.size();
        }
        return documentsCount;
    }

    public void setDocumentsCount(Integer documentsCount) {
        this.documentsCount = documentsCount;
    }

    public List<Document> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<Document> documentList) {
        this.documentList = documentList;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Disease)) return false;
        Disease disease = (Disease) o;
        return Objects.equals(getName().toLowerCase(), disease.getName().toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "Disease{" +
                "diseaseId='" + diseaseId + '\'' +
                ", name='" + name + '\'' +
                ", cui='" + cui + '\'' +
                ", url='" + url + '\'' +
                ", codesCount=" + codesCount +
                ", codes=" + codes +
                ", disnetConceptsCount=" + disnetConceptsCount +
                ", disnetConceptList=" + disnetConceptList +
                ", documentsCount=" + documentsCount +
                ", documentList=" + documentList +
                '}';
    }
}
