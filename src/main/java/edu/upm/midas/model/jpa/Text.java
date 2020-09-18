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
 * @className Text
 * @see
 */
@Entity
@Table(name = "text", catalog = "edsssdb", schema = "edsssdb")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Text.findAll", query = "SELECT t FROM Text t")
        , @NamedQuery(name = "Text.findById", query = "SELECT t FROM Text t WHERE t.textId = :textId")
        , @NamedQuery(name = "Text.findByTextId", query = "SELECT t FROM Text t WHERE t.textId = :textId")
        , @NamedQuery(name = "Text.findByContentType", query = "SELECT t FROM Text t WHERE t.contentType = :contentType")
        , @NamedQuery(name = "Text.updateById", query = "UPDATE Text t SET t.contentType = :contentType, t.text = :text WHERE t.textId = :textId")
})

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "Text.findByIdNative",
                query = "SELECT t.text_id, t.content_type, t.text "
                        + "FROM text t WHERE t.text_id = :textId",
                resultSetMapping="TextMapping"

        ),
        @NamedNativeQuery(
                name = "Text.findByIdNativeResultClass",
                query = "SELECT t.text_id, t.content_type, t.text "
                        + "FROM text t WHERE t.text_id = :textId",
                resultClass = Text.class
        ),


        @NamedNativeQuery(
                name = "Text.insertNative",
                query = "INSERT INTO text (text_id, content_type, text) "
                        + "VALUES (:textId, :contentType, :text)"
        ),
        @NamedNativeQuery(
                name = "HasText.insertNative_",
                query = "INSERT INTO has_text (document_id, date, section_id, text_id, text_order) "
                        + "VALUES (:documentId, :date, :sectionId, :textId, :textOrder)"
        ),
        @NamedNativeQuery(
                name = "TextUrl.insertNative",
                query = "INSERT INTO text_url (text_id, url_id) "
                        + "VALUES (:textId, :urlId)"
        ),


        @NamedNativeQuery(
                name = "Text.findBySourceAndVersionNative",
                query = "SELECT s.source_id, s.name, ht.document_id, ht.date, t.text_id, t.content_type, t.text " +
                        "FROM has_text ht " +
                        "LEFT JOIN text t ON t.text_id = ht.text_id " +
                        "LEFT JOIN has_source hs ON hs.document_id = ht.document_id AND hs.date = ht.date " +
                        "LEFT JOIN source s ON s.source_id = hs.source_id " +
                        "WHERE ht.date = :version " +
                        "AND s.name = :source"
        ),



        // (1) OBTIENE LOS TEXTOS Y EL NÚMERO DE TERMINOS (DISNETCONCEPTS) POR FUENTE Y VERSION ORDENADOS DE MAYOR A MENOS
        @NamedNativeQuery(
                name = "Text.findAllBySourceAndVersionAndTextCountNative",
                query = "SELECT DISTINCT ht.text_id, sec.name, ht.text_order, t.text, doc.document_id, SUBSTRING_INDEX( SUBSTRING_INDEX(ht.text_id, 'PAPERID', 2), 'PAPERID', -1) 'paper_id', COUNT(DISTINCT hsym.cui) 'disnetConceptsCount' -- getDisnetConceptsCountInAText(sce.name, doc.date, ht.text_id, true) 'disnetConceptsCount' \n" +
                        "FROM document doc  " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "-- section\n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                        "INNER JOIN section sec ON sec.section_id = hsec.section_id " +
                        "-- text\n" +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "INNER JOIN text t ON t.text_id = ht.text_id " +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = t.text_id " +
                        "WHERE sce.name = :source " +
                        "AND hs.date = :version " +
                        "AND hsym.validated = :validated " +
                        "GROUP BY ht.text_id, sec.name, ht.text_order, t.text, doc.document_id " +
                        "ORDER BY COUNT(DISTINCT hsym.cui) DESC "
        ),
        // (2) TERMINOS DE UN TEXTO VALIDADOS Y NO. SEGUN UN ID DE TEXTO
        @NamedNativeQuery(
                name = "Text.findTermsBySourceAndVersionAndDocumentAndTextIdNative",
                query = "SELECT DISTINCT sym.cui, sym.name, hsym.validated, getSemanticTypesBySymptom(sym.cui) 'semantic_types' " +
                        "FROM document doc " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "-- section \n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                        "-- texts \n" +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "-- symptom\n" +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                        "INNER JOIN symptom sym ON sym.cui = hsym.cui " +
                        "-- semantic_types\n" +
                        "INNER JOIN has_semantic_type hst ON hst.cui = sym.cui " +
                        "WHERE sce.name = :source " +
                        "AND hs.date = :version " +
                        "AND ht.text_id = :textId " +
                        "GROUP BY sym.cui, sym.name, hsym.validated " +
                        "ORDER BY hsym.validated DESC "
        ),
        // (3) INFORMACIÓN DE UNA ENFERMEDAD SEGUN UN TEXTO, FUENTE Y VERSIÓN Y DOCUMENTO
        @NamedNativeQuery(
                name = "Text.findDiseaseBySourceAndVersionAndDocumentIdNative",
                query = "SELECT d.disease_id, d.name, getDisnetConceptsCount(sce.name, doc.date, d.disease_id) 'disnetConceptCount' " +
                        "FROM document doc " +
                        "INNER JOIN has_disease hd ON hd.document_id = doc.document_id AND hd.date = doc.date " +
                        "INNER JOIN disease d ON d.disease_id = hd.disease_id " +
                        "-- source\n" +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "WHERE doc.document_id = :documentId " +
                        "AND sce.name = :source " +
                        "AND doc.date = :version "
        ),
        // (4) INFORMACIÓN DE UNA PAPER SEGÚN EL PAPER ID
        @NamedNativeQuery(
                name = "Text.findPaperByIdNative",
                query = "SELECT p.title, p.authors, p.keywords, u.url " +
                        "FROM paper p " +
                        "INNER JOIN paper_url pu ON pu.paper_id = p.paper_id " +
                        "INNER JOIN url u ON u.url_id = pu.url_id " +
                        "WHERE p.paper_id = :paperId "
        ),
        // (5) OBTIENE LOS TEXTOS DE DONDE FUE OBTENIDO EL TERMINO
        @NamedNativeQuery(
                name = "Text.findTextsBySourceAndVersionAndDocumentAndTextIdAndCuiNative",
                query = "SELECT sec.description, ht.text_order, t.text_id, t.text, hsym.matched_words, hsym.positional_info " +
                        "FROM symptom sym " +
                        "INNER JOIN has_symptom hsym ON hsym.cui = sym.cui " +
                        "INNER JOIN text t ON t.text_id = hsym.text_id " +
                        "INNER JOIN has_text ht ON ht.text_id = t.text_id " +
                        "INNER JOIN has_section hsec ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "INNER JOIN section sec ON sec.section_id = hsec.section_id " +
                        "-- source\n" +
                        "INNER JOIN document doc ON doc.document_id = hsec.document_id AND doc.date = hsec.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "WHERE sce.name = :source " +
                        "AND ht.date = :version " +
                        "AND ht.document_id = :documentId " +
                        "AND t.text_id = :textId " +
                        "AND sym.cui = :cui " +
                        "ORDER BY sec.description, ht.text_order ASC "
        )






})

@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "TextMapping",
                entities = @EntityResult(
                        entityClass = Text.class,
                        fields = {
                                @FieldResult(name = "textId", column = "text_id"),
                                @FieldResult(name = "contentType", column = "content_type"),
                                @FieldResult(name = "text", column = "text")
                        }
                )
        )
})

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="textId")
public class Text {
    private String textId;
    private String contentType;
    private String text;
    private List<HasSymptom> hasSymptomsByTextId;
    private List<HasText> hasTextsByTextId;
    private List<TextUrl> textUrlsByTextId;

    @Id
    @Column(name = "text_id", nullable = false, length = 55)
    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }

    @Basic
    @Column(name = "content_type", nullable = false, length = 10)
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Basic
    @Column(name = "text", nullable = false, length = -1)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Text text1 = (Text) o;
        return Objects.equals(textId, text1.textId) &&
                Objects.equals(contentType, text1.contentType) &&
                Objects.equals(text, text1.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textId, contentType, text);
    }

    @OneToMany(mappedBy = "textByTextId")
    public List<HasSymptom> getHasSymptomsByTextId() {
        return hasSymptomsByTextId;
    }

    public void setHasSymptomsByTextId(List<HasSymptom> hasSymptomsByTextId) {
        this.hasSymptomsByTextId = hasSymptomsByTextId;
    }

    @OneToMany(mappedBy = "textByTextId")
    public List<HasText> getHasTextsByTextId() {
        return hasTextsByTextId;
    }

    public void setHasTextsByTextId(List<HasText> hasTextsByTextId) {
        this.hasTextsByTextId = hasTextsByTextId;
    }

    @OneToMany(mappedBy = "textByTextId")
    public List<TextUrl> getTextUrlsByTextId() {
        return textUrlsByTextId;
    }

    public void setTextUrlsByTextId(List<TextUrl> textUrlsByTextId) {
        this.textUrlsByTextId = textUrlsByTextId;
    }
}
