package edu.upm.midas.model.jpa;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;
import java.util.Objects;

/**
 * Created by gerardo on 20/07/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project demo
 * @className Configuration
 * @see
 */
@Entity
public class Configuration {
    private String confId;
    private String sourceId;
    private Date snapshot;
    private String toolId;
    private String configuration;

    @Id
    @Column(name = "conf_id", nullable = false, length = 50)
    public String getConfId() {
        return confId;
    }

    public void setConfId(String confId) {
        this.confId = confId;
    }

    @Basic
    @Column(name = "source_id", nullable = false, length = 10)
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @Basic
    @Column(name = "version", nullable = false)
    public Date getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(Date version) {
        this.snapshot = version;
    }

    @Basic
    @Column(name = "tool", nullable = false, length = 50)
    public String getToolId() {
        return toolId;
    }

    public void setToolId(String tool) {
        this.toolId = tool;
    }

    @Basic
    @Column(name = "configuration", nullable = false, length = -1)
    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return Objects.equals(confId, that.confId) &&
                Objects.equals(toolId, that.toolId) &&
                Objects.equals(configuration, that.configuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(confId, toolId, configuration);
    }
}
