package edu.upm.midas.model.response.validations;
import java.util.List;

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
    private String typeSemanticTypesSearch;
    private List<String> excludeSemanticTypes;
    private List<String> forceSemanticTypes;


    public TypeSearchValidation() {
        this.typeSemanticTypesSearch = "";
    }

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

    public String getTypeSemanticTypesSearch() {
        return typeSemanticTypesSearch;
    }

    public void setTypeSemanticTypesSearch(String typeSemanticTypesSearch) {
        this.typeSemanticTypesSearch = typeSemanticTypesSearch;
    }

    public List<String> getExcludeSemanticTypes() {
        return excludeSemanticTypes;
    }

    public void setExcludeSemanticTypes(List<String> excludeSemanticTypes) {
        this.excludeSemanticTypes = excludeSemanticTypes;
    }

    public List<String> getForceSemanticTypes() {
        return forceSemanticTypes;
    }

    public void setForceSemanticTypes(List<String> forceSemanticTypes) {
        this.forceSemanticTypes = forceSemanticTypes;
    }


    @Override
    public String toString() {
        return "TypeSearchValidation{" +
                "typeSearch='" + typeSearch + '\'' +
                ", errors=" + errors +
                ", typeSemanticTypesSearch='" + typeSemanticTypesSearch + '\'' +
                ", excludeSemanticTypes=" + excludeSemanticTypes +
                ", forceSemanticTypes=" + forceSemanticTypes +
                '}';
    }
}
