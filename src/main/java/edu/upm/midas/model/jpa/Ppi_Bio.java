package edu.upm.midas.model.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ppi", schema = "disnet_biolayer")
public class Ppi_Bio {
    @Id
    private String protein1_id;
    private String protein2_id;

    public String getProtein1_id() {
        return protein1_id;
    }

    public void setProtein1_id(String protein1_id) {
        this.protein1_id = protein1_id;
    }

    public String getProtein2_id() {
        return protein2_id;
    }

    public void setProtein2_id(String protein2_id) {
        this.protein2_id = protein2_id;
    }


}
