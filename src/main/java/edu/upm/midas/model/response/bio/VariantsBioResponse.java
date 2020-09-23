package edu.upm.midas.model.response.bio;

import edu.upm.midas.model.jpa.VariantBio;
import edu.upm.midas.model.response.ResponseFather;

import java.util.List;

public class VariantsBioResponse extends ResponseFather {
    public List<VariantBio> getVariantBio() {
        return variantBio;
    }

    public void setVariantBio(List<VariantBio> variantBio) {
        this.variantBio = variantBio;
    }

    private List<VariantBio> variantBio;
}
