package edu.upm.midas.model.response.bio;

import edu.upm.midas.model.response.ResponseFather;

import java.util.List;

public class VariantBiobyIdResponse extends ResponseFather {
    private List<String> variant_Id;

    public List<String> getVariant_Id() {
        return variant_Id;
    }

    public void setVariant_Id(List<String> variant_id) {
        this.variant_Id = variant_id;
    }
}
