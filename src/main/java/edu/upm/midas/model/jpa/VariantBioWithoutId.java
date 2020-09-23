package edu.upm.midas.model.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

public class VariantBioWithoutId {
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

    public VariantBioWithoutId(String chromosome, String chrposition, String consequence) {
        this.chromosome = chromosome;
        this.chrposition = chrposition;
        this.consequence = consequence;
    }
}
