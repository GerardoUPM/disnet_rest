package edu.upm.midas.model.jpa;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Created by gerardo on 17/12/2018.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className Tool
 * @see
 */
@Entity
public class Tool {
    private String toolId;
    private String name;
    private String description;

    @Id
    @Column(name = "tool_id", nullable = false, length = 10)
    public String getToolId() {
        return toolId;
    }

    public void setToolId(String toolId) {
        this.toolId = toolId;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 150)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tool tool = (Tool) o;
        return Objects.equals(toolId, tool.toolId) &&
                Objects.equals(name, tool.name) &&
                Objects.equals(description, tool.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(toolId, name, description);
    }
}
