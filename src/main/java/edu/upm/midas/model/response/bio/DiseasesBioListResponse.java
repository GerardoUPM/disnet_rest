package edu.upm.midas.model.response.bio;

import edu.upm.midas.model.jpa.DiseaseBio;
import edu.upm.midas.model.response.ResponseFather;

import java.util.List;

public class DiseasesBioListResponse extends ResponseFather {
    private List<DiseaseBio> diseases;

    public List<DiseaseBio> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<DiseaseBio> diseases) {
        this.diseases = diseases;
    }
}
