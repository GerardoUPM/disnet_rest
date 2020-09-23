package edu.upm.midas.model.response.bio;

import edu.upm.midas.model.jpa.Ppi_Bio;
import edu.upm.midas.model.response.ResponseFather;

import java.util.List;

public class PpissBioResponse extends ResponseFather {
    public List<Ppi_Bio> getPpisBioList() {
        return ppisBioList;
    }

    public void setPpisBioList(List<Ppi_Bio> ppisBioList) {
        this.ppisBioList = ppisBioList;
    }

    private List<Ppi_Bio> ppisBioList;


}
