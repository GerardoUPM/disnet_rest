package edu.upm.midas.model.jpa;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

/**
 * Created by gerardo on 20/07/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project demo
 * @className Symptom
 * @see
 */
@Entity
@Table(name = "symptom", catalog = "edsssdb", schema = "edsssdb")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Symptom.findAll", query = "SELECT s FROM Symptom s")
        , @NamedQuery(name = "Symptom.findById", query = "SELECT s FROM Symptom s WHERE s.cui = :cui")
        , @NamedQuery(name = "Symptom.findByCui", query = "SELECT s FROM Symptom s WHERE s.cui = :cui")
        , @NamedQuery(name = "Symptom.findByName", query = "SELECT s FROM Symptom s WHERE s.name = :name")

})

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "Symptom.findByIdNativeMapping",
                query = "SELECT s.cui, s.name "
                        + "FROM symptom s WHERE s.cui = :cui",
                resultSetMapping="SymptomMapping"

        ),
        @NamedNativeQuery(
                name = "Symptom.findByIdNativeResultClass",
                query = "SELECT s.cui, s.name "
                        + "FROM symptom s WHERE s.cui = :cui",
                resultClass = Symptom.class
        ),
        @NamedNativeQuery(
                name = "HasSemanticType.findByIdNative",
                query = "SELECT h.cui, h.semantic_type "
                        + "FROM has_semantic_type h WHERE h.cui = :cui AND h.semantic_type = :semanticType"
        ),


        @NamedNativeQuery(
                name = "Symptom.findByIdNative",
                query = "SELECT s.cui, s.name "
                        + "FROM symptom s WHERE s.cui = :cui"
        ),
        @NamedNativeQuery(
                name = "Symptom.UpdateByIdNative",
                query = "SELECT s.cui, s.name "
                        + "FROM symptom s WHERE s.cui = :cui"
        ),


        @NamedNativeQuery(
                name = "Symptom.insertNative",
                query = "INSERT INTO symptom (cui, name) "
                        + "VALUES (:cui, :name)"
        ),
        @NamedNativeQuery(
                name = "Symptom.insertNativeHasSemanticType",
                query = "INSERT INTO has_semantic_type (cui, semantic_type) "
                        + "VALUES (:cui, :semanticType)"
        ),


        @NamedNativeQuery(
                name = "Symptom.findBySourceAndVersionNative",
                query = "SELECT s.source_id, s.name, ht.document_id, ht.date, t.text_id, sy.cui, sy.name AS nameSy, hsy.validated " +
                        "FROM has_text ht " +
                        "LEFT JOIN text t ON t.text_id = ht.text_id " +
                        "LEFT JOIN has_source hs ON hs.document_id = ht.document_id AND hs.date = ht.date " +
                        "LEFT JOIN source s ON s.source_id = hs.source_id " +
                        "LEFT JOIN has_symptom hsy ON hsy.text_id = t.text_id " +
                        "INNER JOIN symptom sy ON sy.cui = hsy.cui " +
                        "WHERE ht.date = :version " +
                        "AND s.name = :source"
        ),



        //-- "YA NO SE USARÁ AQUÍ" <<<mostCommonSymptoms>>> SINTOMAS MAS COMUNES
        @NamedNativeQuery(
                name = "Symptom.mostCommonBySourceAndVersionAndValidated",
                query = "SELECT DISTINCT sym.cui, sym.name, COUNT(hsym.cui) 'common', getSemanticTypesBySymptom(sym.cui) 'semantic_types' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id \n" +
                        "-- source and version\n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                        "INNER JOIN symptom sym ON sym.cui = hsym.cui " +
                        "WHERE sce.name = :source " +
                        "AND hs.date = :version " +
                        "AND hsym.validated = :validated " +
                        "GROUP BY sym.cui, sym.name " +
                        "ORDER BY COUNT(hsym.cui) DESC "
        ),
        //-- "YA NO SE USARÁ AQUÍ" <<<lessCommonSymptoms>>> SINTOMAS MENOS COMUNES
        @NamedNativeQuery(
                name = "Symptom.lessCommonBySourceAndVersionAndValidated",
                query = "SELECT DISTINCT sym.cui, sym.name, COUNT(hsym.cui) 'common', getSemanticTypesBySymptom(sym.cui) 'semantic_types' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id \n" +
                        "-- source and version\n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                        "INNER JOIN symptom sym ON sym.cui = hsym.cui " +
                        "WHERE sce.name = :source " +
                        "AND hs.date = :version " +
                        "AND hsym.validated = :validated " +
                        "GROUP BY sym.cui, sym.name " +
                        "ORDER BY COUNT(hsym.cui) ASC "
        )



})

@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "SymptomMapping",
                entities = @EntityResult(
                        entityClass = Symptom.class,
                        fields = {
                                @FieldResult(name = "cui", column = "cui"),
                                @FieldResult(name = "name", column = "name")
                        }
                )
        )
})

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="cui")
public class Symptom {
    private String cui;
    private String name;
    private List<HasSemanticType> hasSemanticTypesByCui;
    private List<HasSymptom> hasSymptomsByCui;

    @Id
    @Column(name = "cui", nullable = false, length = 8)
    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 250)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symptom symptom = (Symptom) o;
        return Objects.equals(cui, symptom.cui) &&
                Objects.equals(name, symptom.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cui, name);
    }

    @OneToMany(mappedBy = "symptomByCui")
    public List<HasSemanticType> getHasSemanticTypesByCui() {
        return hasSemanticTypesByCui;
    }

    public void setHasSemanticTypesByCui(List<HasSemanticType> hasSemanticTypesByCui) {
        this.hasSemanticTypesByCui = hasSemanticTypesByCui;
    }

    @OneToMany(mappedBy = "symptomByCui")
    public List<HasSymptom> getHasSymptomsByCui() {
        return hasSymptomsByCui;
    }

    public void setHasSymptomsByCui(List<HasSymptom> hasSymptomsByCui) {
        this.hasSymptomsByCui = hasSymptomsByCui;
    }
}
