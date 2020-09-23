package edu.upm.midas.model.response.bio;

import edu.upm.midas.model.jpa.VariantBioWithoutId;
import edu.upm.midas.model.response.ResponseFather;

public class VariantBioResponse extends ResponseFather {
    public VariantBioWithoutId getVariantBio() {
        return variantBio;
    }

    public void setVariantBio(VariantBioWithoutId variantBio) {
        this.variantBio = variantBio;
    }

    private VariantBioWithoutId variantBio;
}
