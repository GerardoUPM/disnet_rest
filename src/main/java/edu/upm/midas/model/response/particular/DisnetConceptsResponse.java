package edu.upm.midas.model.response.particular;
import edu.upm.midas.model.Disease;
import edu.upm.midas.model.DisnetConcept;
import edu.upm.midas.model.response.ResponseFather;

import java.util.List;

/**
 * Created by gerardo on 30/08/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className DisnetConceptsResponse
 * @see
 */
public class DisnetConceptsResponse extends ResponseFather {

    private int diseaseCount;
    private List<Disease> diseaseList;



    public int getDiseaseCount() {
        return diseaseCount;
    }

    public void setDiseaseCount(int diseaseCount) {
        this.diseaseCount = diseaseCount;
    }

    public List<Disease> getDiseaseList() {
        return diseaseList;
    }

    public void setDiseaseList(List<Disease> diseaseList) {
        this.diseaseList = diseaseList;
    }
}
