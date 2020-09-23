package edu.upm.midas.model.response.bio;

import edu.upm.midas.model.jpa.ProteinBio;
import edu.upm.midas.model.response.ResponseFather;

import java.util.List;

public class ProteinsBioResponse extends ResponseFather {
    private List<ProteinBio> proteinBioList;

    public List<ProteinBio> getProteinBioList() {
        return proteinBioList;
    }

    public void setProteinBioList(List<ProteinBio> proteinBioList) {
        this.proteinBioList = proteinBioList;
    }
}
