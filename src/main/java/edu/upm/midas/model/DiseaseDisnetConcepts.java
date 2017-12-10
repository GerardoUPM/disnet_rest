package edu.upm.midas.model;
import java.util.List;

/**
 * Created by gerardo on 22/11/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className DiseaseDisnetConcepts
 * @see
 */
public class DiseaseDisnetConcepts {


    private String diseaseId;
    private String name;
    private int disnetConceptCount;
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

    public int getDisnetConceptCount() {
        return disnetConceptCount;
    }

    public void setDisnetConceptCount(int disnetConceptCount) {
        this.disnetConceptCount = disnetConceptCount;
    }

    public List<DisnetConcept> getDisnetConceptList() {
        return disnetConceptList;
    }

    public void setDisnetConceptList(List<DisnetConcept> disnetConceptList) {
        this.disnetConceptList = disnetConceptList;
    }
}
