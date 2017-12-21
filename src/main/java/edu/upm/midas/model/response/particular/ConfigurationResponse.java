package edu.upm.midas.model.response.particular;
import edu.upm.midas.model.response.Configuration;
import edu.upm.midas.model.response.ResponseFather;

import java.util.List;

/**
 * Created by gerardo on 11/12/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className ConfigurationResponse
 * @see
 */
public class ConfigurationResponse extends ResponseFather {

    private Integer configurationsCount;
    private List<Configuration> configurationList;


    public Integer getConfigurationsCount() {
        return configurationsCount;
    }

    public void setConfigurationsCount(Integer configurationsCount) {
        this.configurationsCount = configurationsCount;
    }

    public List<Configuration> getConfigurationList() {
        return configurationList;
    }

    public void setConfigurationList(List<Configuration> configurationList) {
        this.configurationList = configurationList;
    }
}
