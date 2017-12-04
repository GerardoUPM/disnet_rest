package edu.upm.midas.model.response.validations;
/**
 * Created by gerardo on 04/12/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className TypeSearchValidation
 * @see
 */
public class TypeSearchValidation {

    private String typeSearch;
    private boolean errors;



    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public boolean isErrors() {
        return errors;
    }

    public void setErrors(boolean errors) {
        this.errors = errors;
    }
}
