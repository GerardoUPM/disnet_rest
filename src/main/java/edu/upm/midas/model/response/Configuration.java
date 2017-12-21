package edu.upm.midas.model.response;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upm.midas.model.response.particular.DiseaseAlbumConfiguration;
import edu.upm.midas.model.response.particular.MetamapConfiguration;
import edu.upm.midas.model.response.particular.TvpConfiguration;

/**
 * Created by gerardo on 11/12/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className Configuration
 * @see
 */
public class Configuration {

    private String configurationId;
    private String tool;
    @JsonIgnore
    private String configuration;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DiseaseAlbumConfiguration diseaseAlbumConfiguration;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MetamapConfiguration metamapConfiguration;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TvpConfiguration tvpConfiguration;


    public String getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(String configurationId) {
        this.configurationId = configurationId;
    }

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public DiseaseAlbumConfiguration getDiseaseAlbumConfiguration() {
        return diseaseAlbumConfiguration;
    }

    public void setDiseaseAlbumConfiguration(DiseaseAlbumConfiguration diseaseAlbumConfiguration) {
        this.diseaseAlbumConfiguration = diseaseAlbumConfiguration;
    }

    public MetamapConfiguration getMetamapConfiguration() {
        return metamapConfiguration;
    }

    public void setMetamapConfiguration(MetamapConfiguration metamapConfiguration) {
        this.metamapConfiguration = metamapConfiguration;
    }

    public TvpConfiguration getTvpConfiguration() {
        return tvpConfiguration;
    }

    public void setTvpConfiguration(TvpConfiguration tvpConfiguration) {
        this.tvpConfiguration = tvpConfiguration;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "configurationId='" + configurationId + '\'' +
                ", tool='" + tool + '\'' +
                ", configuration='" + configuration + '\'' +
                '}';
    }
}
