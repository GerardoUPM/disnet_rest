package edu.upm.midas.data.relational.service.model.response.diseases;
import java.util.Objects;

/**
 * Created by gerardo on 30/08/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className Finding
 * @see
 */
public class Finding {

    private String cui;
    private String name;


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

    @Override
    public String toString() {
        return "Finding{" +
                "cui='" + cui + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Finding)) return false;
        Finding finding = (Finding) o;
        return Objects.equals(getCui(), finding.getCui());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCui());
    }
}
