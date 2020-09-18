package edu.upm.midas.model;

public class GeneBio {
    private int gen_id;
    private String gene_name;
    private String gene_symbol;

    public String getGene_symbol() {
        return gene_symbol;
    }

    public void setGene_symbol(String gene_symbol) {
        this.gene_symbol = gene_symbol;
    }

    public String getGene_name() {
        return gene_name;
    }

    public void setGene_name(String gene_name) {
        this.gene_name = gene_name;
    }

    public int getGen_id() {
        return gen_id;
    }

    public void setGen_id(int gen_id) {
        this.gen_id = gen_id;
    }
}
