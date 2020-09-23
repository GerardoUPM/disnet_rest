package edu.upm.midas.model.response.bio;

import edu.upm.midas.model.jpa.ProteinBio;
import edu.upm.midas.model.response.ResponseFather;

import java.util.List;

public class ProteinBioResponse extends ResponseFather {
    public ProteinBio getProteinBioList() {
        return proteinBioList;
    }

    public void setProteinBioList(ProteinBio proteinBioList) {
        this.proteinBioList = proteinBioList;
    }

    private ProteinBio proteinBioList;


}
