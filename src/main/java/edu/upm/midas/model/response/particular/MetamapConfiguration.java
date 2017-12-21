package edu.upm.midas.model.response.particular;
import java.util.List;

/**
 * Created by gerardo on 11/12/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className MetamapConfiguration
 * @see
 */
public class MetamapConfiguration {

    private List<String> semanticTypes;
    private List<String> sources;
    private String options;


    public List<String> getSemanticTypes() {return semanticTypes;}

    public void setSemanticTypes(List<String> semanticTypes) {
        this.semanticTypes = semanticTypes;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

}
