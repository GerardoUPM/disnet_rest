package edu.upm.midas.model.response.bio;

import edu.upm.midas.model.jpa.GeneBio;
import edu.upm.midas.model.response.ResponseFather;

import java.util.List;

public class GenesBioResponse extends ResponseFather {
    private List<GeneBio> genes;

    public List<GeneBio> getGenes() {
        return genes;
    }

    public void setGenes(List<GeneBio> genes) {
        this.genes = genes;
    }
}
