package edu.upm.midas.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

public class Document {

    private String documentId;
    private Date version;
    private String url;
    //Preparado para dos lógicas
    //Que un documento tenga conceptos que pertenecen a textos
    //O que tenga textos y ellos tienen conceptos
    private Integer disnetConceptsCount;
    private List<DisnetConcept> disnetConceptList;

    private Integer textsCount;
    private List<Text> textList;


    public Document() {
    }

    public Document(String documentId, Date version) {
        this.documentId = documentId;
        this.version = version;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Date getVersion() {
        return version;
    }

    public void setVersion(Date version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getDisnetConceptsCount() {
        return disnetConceptsCount;
    }

    public void setDisnetConceptsCount(Integer disnetConceptsCount) {
        this.disnetConceptsCount = disnetConceptsCount;
    }

    public List<DisnetConcept> getDisnetConceptList() {
        return disnetConceptList;
    }

    public void setDisnetConceptList(List<DisnetConcept> disnetConceptList) {
        this.disnetConceptList = disnetConceptList;
    }

    public Integer getTextsCount() {
        return textsCount;
    }

    public void setTextsCount(Integer textsCount) {
        this.textsCount = textsCount;
    }

    public List<Text> getTextList() {
        return textList;
    }

    public void setTextList(List<Text> textList) {
        this.textList = textList;
    }
}
