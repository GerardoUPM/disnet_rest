package edu.upm.midas.model.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "protein", schema = "disnet_biolayer")
public class ProteinBio {
    @Id
    private String protein_id;

    public String getProtein_id() {
        return protein_id;
    }

    public void setProtein_id(String protein_id) {
        this.protein_id = protein_id;
    }
}
