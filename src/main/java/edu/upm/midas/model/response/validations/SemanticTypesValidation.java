package edu.upm.midas.model.response.validations;
import java.util.List;

/**
 * Created by gerardo on 05/12/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className SemanticTypesValidation
 * @see
 */
public class SemanticTypesValidation extends Validation {

    private List<String> validSemanticTypes;


    public List<String> getValidSemanticTypes() {
        return validSemanticTypes;
    }

    public void setValidSemanticTypes(List<String> validSemanticTypes) {
        this.validSemanticTypes = validSemanticTypes;
    }
}
