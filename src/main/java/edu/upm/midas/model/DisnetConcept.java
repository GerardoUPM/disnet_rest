package edu.upm.midas.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

/**
 * Created by gerardo on 30/08/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className DisnetConcept
 * @see
 */
public class DisnetConcept {

    private String cui;
    private String name;
    @JsonIgnore
    private String url;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> semanticTypes;


    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getSemanticTypes() {
        return semanticTypes;
    }

    public void setSemanticTypes(List<String> semanticTypes) {
        this.semanticTypes = semanticTypes;
    }

    @Override
    public String toString() {
        return "DisnetConcept{" +
                "cui='" + cui + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DisnetConcept)) return false;
        DisnetConcept DisnetConcept = (DisnetConcept) o;
        return Objects.equals(getCui(), DisnetConcept.getCui());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCui());
    }
}
