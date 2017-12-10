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
public class VersionsResponse extends ResponseFather {

    private Integer versionsCount;
    private List<String> versions;


    public Integer getVersionsCount() {
        return versionsCount;
    }

    public void setVersionsCount(Integer versionsCount) {
        this.versionsCount = versionsCount;
    }

    public List<String> getVersions() {
        return versions;
    }

    public void setVersions(List<String> versions) {
        this.versions = versions;
    }
}
