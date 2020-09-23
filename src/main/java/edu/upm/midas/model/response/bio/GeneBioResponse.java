package edu.upm.midas.model.response.bio;

import edu.upm.midas.model.jpa.GeneBioWithoutId;
import edu.upm.midas.model.response.ResponseFather;

public class GeneBioResponse extends ResponseFather {
    private GeneBioWithoutId geneWithIdBio;

    public GeneBioWithoutId getGeneBio() {
        return geneWithIdBio;
    }

    public void setGeneBio(GeneBioWithoutId geneWithIdBio) {
        this.geneWithIdBio = geneWithIdBio;
    }
}
