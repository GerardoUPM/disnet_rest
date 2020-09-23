package edu.upm.midas.model.response.bio;

import edu.upm.midas.model.jpa.PathwayBio;
import edu.upm.midas.model.response.ResponseFather;

import java.util.List;

public class PathwayResponse extends ResponseFather {
    private List<PathwayBio>  pathwayBios;

    public List<PathwayBio> getPathwayBios() {
        return pathwayBios;
    }

    public void setPathwayBios(List<PathwayBio> pathwayBios) {
        this.pathwayBios = pathwayBios;
    }
}
