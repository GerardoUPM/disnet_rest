package edu.upm.midas.model.response.particular;
import edu.upm.midas.model.SymptomWithCount;
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

    private List<SymptomWithCount> symptomList;


    public List<SymptomWithCount> getSymptomList() {
        return symptomList;
    }

    public void setSymptomList(List<SymptomWithCount> symptomList) {
        this.symptomList = symptomList;
    }
}