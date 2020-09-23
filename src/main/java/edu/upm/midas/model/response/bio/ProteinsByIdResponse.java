package edu.upm.midas.model.response.bio;

import edu.upm.midas.model.response.ResponseFather;

import java.util.List;

public class ProteinsByIdResponse extends ResponseFather {
    private List<String> proteins_Ids;

    public List<String> getProteins_Ids() {
        return proteins_Ids;
    }

    public void setProteins_Ids(List<String> proteins_Ids) {
        this.proteins_Ids = proteins_Ids;
    }
}
