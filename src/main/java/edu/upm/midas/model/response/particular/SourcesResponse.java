package edu.upm.midas.model.response.particular;
import edu.upm.midas.model.response.ResponseFather;

import java.util.List;

/**
 * Created by gerardo on 27/09/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className SourcesResponse
 * @see
 */
public class SourcesResponse extends ResponseFather {

    private Integer sourceCount;
    private List<String> sources;


    public Integer getSourceCount() {
        return sourceCount;
    }

    public void setSourceCount(Integer sourceCount) {
        this.sourceCount = sourceCount;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }
}
