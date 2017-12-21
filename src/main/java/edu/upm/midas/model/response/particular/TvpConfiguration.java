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

    @JsonProperty("numDisnetConceptsFound")
    private int numSymptomsFound;
    @JsonProperty("numDisnetConceptsFoundValidated")
    private int numSymptomsFoundValidated;


    public int getNumSymptomsFound() {
        return numSymptomsFound;
    }

    public void setNumSymptomsFound(int numSymptomsFound) {
        this.numSymptomsFound = numSymptomsFound;
    }

    public int getNumSymptomsFoundValidated() {
        return numSymptomsFoundValidated;
    }

    public void setNumSymptomsFoundValidated(int numSymptomsFoundValidated) {
        this.numSymptomsFoundValidated = numSymptomsFoundValidated;
    }
}
