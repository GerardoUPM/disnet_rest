package edu.upm.midas.model.jpa;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


public class GeneBioWithoutId {

    @JsonProperty("gene_name")
    private String gene_name;

    @JsonProperty("gene_symbol")
    private String gene_symbol;

    public String getGene_name() {
        return gene_name;
    }

    public void setGene_name(String gene_name) {
        this.gene_name = gene_name;
    }

    public String getGene_symbol() {
        return gene_symbol;
    }

    public void setGene_symbol(String gene_symbol) {
        this.gene_symbol = gene_symbol;
    }
    public GeneBioWithoutId( String gene_name, String gene_symbol){
        this.gene_name = gene_name;
        this.gene_symbol = gene_symbol;
    }
}
