package edu.upm.midas.model;

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
//    @JsonProperty("source_id")
//    private int source_id;
//    @JsonProperty("ddf_type")
//    private String ddf_type;

//    public int getSource_id() {
//        return source_id;
//    }
//
//    public void setSource_id(int source_id) {
//        this.source_id = source_id;
//    }
//
//    public String getDdf_type() {
//        return ddf_type;
//    }
//
//    public void setDdf_type(String ddf_type) {
//        this.ddf_type = ddf_type;
//    }

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
