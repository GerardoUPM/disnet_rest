package edu.upm.midas.model.response.bio;

import edu.upm.midas.model.response.ResponseFather;

import java.util.List;

public class DiseasesByIdResponse extends ResponseFather {
    private List<String> diseases_Ids;

    public List<String> getDiseases_Ids() {
        return diseases_Ids;
    }

    public void setDiseases_Ids(List<String> diseases_Ids) {
        this.diseases_Ids = diseases_Ids;
    }
}
