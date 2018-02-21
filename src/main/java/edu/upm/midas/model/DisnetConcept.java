package edu.upm.midas.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

/**
 * Created by gerardo on 30/08/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className DisnetConcept
 * @see
 */
public class DisnetConcept {

    private String cui;
    private String name;
    @JsonIgnore
    private Boolean validated;
    @JsonIgnore
    private String url;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer common;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> semanticTypes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DetectionInformation detectionInformation;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer textsCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Text> texts;


    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getCommon() {
        return common;
    }

    public void setCommon(Integer common) {
        this.common = common;
    }

    public List<String> getSemanticTypes() {
        return semanticTypes;
    }

    public void setSemanticTypes(List<String> semanticTypes) {
        this.semanticTypes = semanticTypes;
    }

    public DetectionInformation getDetectionInformation() {
        return detectionInformation;
    }

    public void setDetectionInformation(DetectionInformation detectionInformation) {
        this.detectionInformation = detectionInformation;
    }

    public Integer getTextsCount() {
        return textsCount;
    }

    public void setTextsCount(Integer textsCount) {
        this.textsCount = textsCount;
    }

    public List<Text> getTexts() {
        return texts;
    }

    public void setTexts(List<Text> texts) {
        this.texts = texts;
    }



    @Override
    public String toString() {
        return "DisnetConcept{" +
                "cui='" + cui + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DisnetConcept)) return false;
        DisnetConcept DisnetConcept = (DisnetConcept) o;
        return Objects.equals(getCui(), DisnetConcept.getCui());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCui());
    }
}
