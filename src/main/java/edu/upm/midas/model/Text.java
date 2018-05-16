package edu.upm.midas.model;

import java.util.List;

/**
 * Created by gerardo on 21/12/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className Text
 * @see
 */
public class Text {

    private String textId;
    private Integer textOrder;
    private String text;
    private String section;
    private Integer disnetConceptsCount;
    private List<DisnetConcept> disnetConceptList;
    //Para los casos en los que se obtienen de donde fue obtenido un DISNET_CONCEPT
    private String matchedWords;
    private String positionalInfo;
    private Document document;


    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }

    public Integer getTextOrder() {
        return textOrder;
    }

    public void setTextOrder(Integer textOrder) {
        this.textOrder = textOrder;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
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

    public String getMatchedWords() {
        return matchedWords;
    }

    public void setMatchedWords(String matchedWords) {
        this.matchedWords = matchedWords;
    }

    public String getPositionalInfo() {
        return positionalInfo;
    }

    public void setPositionalInfo(String positionalInfo) {
        this.positionalInfo = positionalInfo;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
