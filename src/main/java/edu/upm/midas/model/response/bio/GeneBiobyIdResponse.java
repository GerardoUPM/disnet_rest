package edu.upm.midas.model.response.bio;

import edu.upm.midas.model.response.ResponseFather;

import java.util.List;

public class GeneBiobyIdResponse extends ResponseFather {
    private List<Integer> genes_Id;

    public List<Integer> getGenes_Id() {
        return genes_Id;
    }

    public void setGenes_Id(List<Integer> genes_Id) {
        this.genes_Id = genes_Id;
    }
}
