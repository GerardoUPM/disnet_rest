package edu.upm.midas.model;
import java.util.List;

/**
 * Created by gerardo on 22/11/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className DiseaseSymptoms
 * @see
 */
public class DiseaseSymptoms {

    private String diseaseId;
    private String name;
    private int count;
    private List<Finding> findingList;


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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Finding> getFindingList() {
        return findingList;
    }

    public void setFindingList(List<Finding> findingList) {
        this.findingList = findingList;
    }
}
