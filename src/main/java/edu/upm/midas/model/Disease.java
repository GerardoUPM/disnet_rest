package edu.upm.midas.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upm.midas.model.response.Code;

import java.util.List;

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String diseaseId;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cui;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String url;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer disnetConceptsCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Code> codes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DisnetConcept> disnetConceptList;


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
        this.name = name;
    }

    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
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
}
