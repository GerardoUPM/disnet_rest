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

    private int size;
    private List<DisnetConcept> disnetConcepts;


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<DisnetConcept> getDisnetConcepts() {
        return disnetConcepts;
    }

    public void setDisnetConcepts(List<DisnetConcept> disnetConcepts) {
        this.disnetConcepts = disnetConcepts;
    }
}
