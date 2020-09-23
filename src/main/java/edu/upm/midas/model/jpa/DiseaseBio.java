package edu.upm.midas.model.jpa;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "disease",schema = "disnet_biolayer")
public class DiseaseBio {
    @Id
    @JsonProperty("disease_id")
    private String disease_id;
    @JsonProperty("disease_name")
    private String disease_name;


    public String getDisease_id() {
        return disease_id;
    }

    public String getDisease_name() {
        return disease_name;
    }

    public void setDisease_id(String disease_id) {
        this.disease_id = disease_id;
    }

    public void setDisease_name(String disease_name) {
        this.disease_name = disease_name;
    }
}
