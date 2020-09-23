package edu.upm.midas.model.response.bio;

import edu.upm.midas.model.response.ResponseFather;

import java.util.List;

public class PathwayByIdResponse extends ResponseFather {
    private List<String> pathways_Ids;

    public List<String> getPathways_Ids() {
        return pathways_Ids;
    }

    public void setPathways_Ids(List<String> pathways_Ids) {
        this.pathways_Ids = pathways_Ids;
    }
}
