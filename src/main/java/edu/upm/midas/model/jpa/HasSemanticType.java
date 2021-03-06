package edu.upm.midas.model.jpa;
import javax.persistence.*;
import java.util.Objects;

/**
 * Created by gerardo on 20/07/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project demo
 * @className HasSemanticType
 * @see
 */
@Entity
@Table(name = "has_semantic_type", schema = "edsssdb", catalog = "")
@IdClass(HasSemanticTypePK.class)
public class HasSemanticType {
    private String cui;
    private String semanticType;
    private Symptom symptomByCui;
    private SemanticType semanticTypeBySemanticType;

    @Id
    @Column(name = "cui", nullable = false, length = 8)
    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    @Id
    @Column(name = "semantic_type", nullable = false, length = 8)
    public String getSemanticType() {
        return semanticType;
    }

    public void setSemanticType(String semanticType) {
        this.semanticType = semanticType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HasSemanticType that = (HasSemanticType) o;
        return Objects.equals(cui, that.cui) &&
                Objects.equals(semanticType, that.semanticType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cui, semanticType);
    }

    @ManyToOne
    @JoinColumn(name = "cui", referencedColumnName = "cui", nullable = false, insertable = false, updatable = false)
    public Symptom getSymptomByCui() {
        return symptomByCui;
    }

    public void setSymptomByCui(Symptom symptomByCui) {
        this.symptomByCui = symptomByCui;
    }

    @ManyToOne
    @JoinColumn(name = "semantic_type", referencedColumnName = "semantic_type", nullable = false, insertable = false, updatable = false)
    public SemanticType getSemanticTypeBySemanticType() {
        return semanticTypeBySemanticType;
    }

    public void setSemanticTypeBySemanticType(SemanticType semanticTypeBySemanticType) {
        this.semanticTypeBySemanticType = semanticTypeBySemanticType;
    }
}
