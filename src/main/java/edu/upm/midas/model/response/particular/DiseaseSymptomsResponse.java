package edu.upm.midas.model.response.particular;
import edu.upm.midas.model.DiseaseSymptoms;
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

    private int size;
    private List<DiseaseSymptoms> diseaseList;


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<DiseaseSymptoms> getDiseaseList() {
        return diseaseList;
    }

    public void setDiseaseList(List<DiseaseSymptoms> diseaseList) {
        this.diseaseList = diseaseList;
    }
}
