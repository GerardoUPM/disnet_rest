package edu.upm.midas.model.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "variant", schema = "disnet_biolayer")
public class VariantBio {
    @Id
    private String variant_id;
    private String chromosome;
    private String chrposition;
    private String consequence;

    public String getChromosome() {
        return chromosome;
    }

    public String getChrposition() {
        return chrposition;
    }

    public String getConsequence() {
        return consequence;
    }

    public String getVariant_id() {
        return variant_id;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public void setChrposition(String chrposition) {
        this.chrposition = chrposition;
    }

    public void setConsequence(String consequence) {
        this.consequence = consequence;
    }

    public void setVariant_id(String variant_id) {
        this.variant_id = variant_id;
    }
}
