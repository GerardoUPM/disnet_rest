package edu.upm.midas.model.response.particular;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gerardo on 22/11/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project eidw
 * @className TvpConfiguration
 * @see
 */
public class TvpConfiguration {

    @JsonProperty("numDisnetConcepts")
    private int termsFound;
    @JsonProperty("numNonRepeatedDisnetConcepts")
    private int nonRepetedTerms;
    @JsonProperty("numValidatedNonRepeatedDisnetConcepts")
    private int validatedNonRepetedTerms;


    public int getTermsFound() {
        return termsFound;
    }

    public void setTermsFound(int termsFound) {
        this.termsFound = termsFound;
    }

    public int getNonRepetedTerms() {
        return nonRepetedTerms;
    }

    public void setNonRepetedTerms(int nonRepetedTerms) {
        this.nonRepetedTerms = nonRepetedTerms;
    }

    public int getValidatedNonRepetedTerms() {
        return validatedNonRepetedTerms;
    }

    public void setValidatedNonRepetedTerms(int validatedNonRepetedTerms) {
        this.validatedNonRepetedTerms = validatedNonRepetedTerms;
    }
}
