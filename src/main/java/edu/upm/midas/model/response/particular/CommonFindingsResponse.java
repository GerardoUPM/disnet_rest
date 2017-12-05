package edu.upm.midas.model.response.particular;
import edu.upm.midas.model.DisnetConcept;
import edu.upm.midas.model.response.ResponseFather;

import java.util.List;

/**
 * Created by gerardo on 22/11/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className CommonFindingsResponse
 * @see
 */
public class CommonFindingsResponse extends ResponseFather {

    private int disnetConceptsCount;
    private List<DisnetConcept> disnetConcepts;


    public int getDisnetConceptsCount() {
        return disnetConceptsCount;
    }

    public void setDisnetConceptsCount(int disnetConceptsCount) {
        this.disnetConceptsCount = disnetConceptsCount;
    }

    public List<DisnetConcept> getDisnetConcepts() {
        return disnetConcepts;
    }

    public void setDisnetConcepts(List<DisnetConcept> disnetConcepts) {
        this.disnetConcepts = disnetConcepts;
    }
}
