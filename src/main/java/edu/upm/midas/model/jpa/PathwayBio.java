package edu.upm.midas.model.jpa;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pathway", schema = "disnet_biolayer")
public class PathwayBio {
    @Id
    @JsonProperty("pathway_id")
    private String pathway_id;

    @JsonProperty("pathway_name")
    private String pathway_name;

    public String getPathway_id() {
        return pathway_id;
    }

    public void setPathway_id(String pathway_id) {
        this.pathway_id = pathway_id;
    }

    public String getPathway_name() {
        return pathway_name;
    }

    public void setPathway_name(String pathway_name) {
        this.pathway_name = pathway_name;
    }
}
