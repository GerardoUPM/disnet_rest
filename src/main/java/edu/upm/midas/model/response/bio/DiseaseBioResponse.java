package edu.upm.midas.model.response.bio;

import edu.upm.midas.model.jpa.DiseaseBio;
import edu.upm.midas.model.response.ResponseFather;

public class DiseaseBioResponse extends ResponseFather {
    private DiseaseBio disease;

    public DiseaseBio getDiseases() {
        return disease;
    }

    public void setDiseases(DiseaseBio disease) {
        this.disease = disease;
    }
}
