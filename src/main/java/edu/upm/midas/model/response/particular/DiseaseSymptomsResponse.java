package edu.upm.midas.model.response.particular;
import edu.upm.midas.model.DiseaseDisnetConcepts;
import edu.upm.midas.model.response.ResponseFather;

import java.util.List;

/**
 * Created by gerardo on 22/11/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className DiseaseSymptomsResponse
 * @see
 */
public class DiseaseSymptomsResponse extends ResponseFather {

    private int diseaseCount;
    private List<DiseaseDisnetConcepts> diseaseList;


    public int getDiseaseCount() {
        return diseaseCount;
    }

    public void setDiseaseCount(int diseaseCount) {
        this.diseaseCount = diseaseCount;
    }

    public List<DiseaseDisnetConcepts> getDiseaseList() {
        return diseaseList;
    }

    public void setDiseaseList(List<DiseaseDisnetConcepts> diseaseList) {
        this.diseaseList = diseaseList;
    }
}
