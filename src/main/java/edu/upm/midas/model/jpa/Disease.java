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
 * @className DisnetConceptsResponse
 * @see
 */
@Entity
@Table(name = "disease", catalog = "edsssdb", schema = "edsssdb")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Disease.findAll", query = "SELECT d FROM Disease d")
        , @NamedQuery(name = "Disease.findById", query = "SELECT d FROM Disease d WHERE d.diseaseId = :diseaseId")
        , @NamedQuery(name = "Disease.findByDiseaseId", query = "SELECT d FROM Disease d WHERE d.diseaseId = :diseaseId")
        , @NamedQuery(name = "Disease.findByName", query = "SELECT d FROM Disease d WHERE d.name = :name")
        , @NamedQuery(name = "Disease.findByCui", query = "SELECT d FROM Disease d WHERE d.cui = :cui")
        , @NamedQuery(name = "Disease.updateById", query = "UPDATE Disease d SET d.name = :name, d.cui = :cui  WHERE d.diseaseId = :diseaseId")
        , @NamedQuery(name = "Disease.findLastDisease", query = "SELECT d FROM Disease d ORDER BY d.diseaseId DESC")
})

@NamedNativeQueries({
        //PAGEABLE

        /*
        * ESTA CONSULTA LA ELIMINO PORQUE RETORNA TODAS LOS ARTÍCULOS QUE SE CREEN QUE SON ENFERMEDADES, PERO REALMENTE
        * NO LO SON.
        *
        * EL FILTRO NUEVO ES PRIMERO, UNA VERIFICACIÓN MANUAL (relevant field) ENCONTRADA EN LA TABLA DISEASE,
        * SEGUNDO, EN EL WHERE, PARA SOLO CONSIDERAR AQUELLOS ARTÍCULOS QUE AL MENOS TENGAN UN CÓDIGO O MÁS Ó
        * AL MENOS TENGAN UNO O MÁS TEXTOS, ESTO QUIERE DECIR QUE AL MENOS TIENE CONTENIDO EN LAS SECCIONES RELEVANTES
        * AL DIAGNÓSTICO DE LA ENFERMEDAD
        * */
        @NamedNativeQuery(
                name = "Disease.findBySourceAndVersionNative",
                query = "SELECT DISTINCT d.disease_id, d.name, d.cui, getDocumentUrl(s.name, doc.date, d.disease_id) 'url', getDisnetConceptsCount(s.name, doc.date, d.disease_id) 'disnetConceptCount' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source s ON s.source_id = hs.source_id " +
                        "-- url\n" +
                        "-- INNER JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date " +
                        "-- INNER JOIN url u ON u.url_id = docu.url_id\n " +
                        "WHERE s.name COLLATE utf8_bin = :source " +
                        "AND doc.date = :version " +
                        "ORDER BY d.name ASC"
        ),
        @NamedNativeQuery(
                name = "Disease.findBySourceAndVersionNative.count",
                query = "SELECT count(DISTINCT d.disease_id) " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source s ON s.source_id = hs.source_id " +
                        "-- url\n" +
                        "-- INNER JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date " +
                        "-- INNER JOIN url u ON u.url_id = docu.url_id\n " +
                        "WHERE s.name COLLATE utf8_bin = :source " +
                        "AND doc.date = :version "
        ),
        //--------------------------- PAGES


        @NamedNativeQuery(
                name = "Disease.findBySourceAndVersionNative_pages",
                query = "SELECT dsyn.disease_id, dsyn.name, dsyn.cui, dsyn.url, dsyn.term_count, dsyn.code_list, dsyn.text_count " +
                        "FROM ( " +
                        "       SELECT DISTINCT d.disease_id, " +
                        "                       d.name, " +
                        "                       d.cui, " +
                        "                       getDocumentUrl(s.name, doc.date, d.disease_id) 'url', " +
                        "                       getFiltredDisnetConceptsCount(s.name, doc.date, d.disease_id, true, false) 'term_count', " +
                        "                       getCodesListBySourceAndDiseaseIdAndSnapshot(s.name, doc.date, d.disease_id)  'code_list', " +
                        "                       getRelevantRealTextCountDisease(s.name, doc.date, d.disease_id, true, false) 'text_count' " +
                        "       FROM disease d " +
                        "              INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "              INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "              INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "              INNER JOIN source s ON s.source_id = hs.source_id " +
                        "       WHERE s.name COLLATE utf8_bin = :source " +
                        "         AND doc.date = :version " +
                        "         AND d.relevant = true " +
                        "       ORDER BY d.name ASC " +
                        "     ) dsyn " +
                        "WHERE ( dsyn.text_count > 0 OR LENGTH(dsyn.code_list) > 0 )"
        ),
        @NamedNativeQuery(
                name = "Disease.findBySourceAndVersionNative.count_pages",
                query = "SELECT COUNT(*) " +
                        "FROM ( " +
                        "       SELECT DISTINCT d.disease_id, " +
                        "                       d.name, " +
                        "                       d.cui, " +
                        "                       getDocumentUrl(s.name, doc.date, d.disease_id)                               url, " +
                        "                       getFiltredDisnetConceptsCount(s.name, doc.date, d.disease_id, true, false)   term_count, " +
                        "                       getCodesListBySourceAndDiseaseIdAndSnapshot(s.name, doc.date, d.disease_id)  code_list, " +
                        "                       getRelevantRealTextCountDisease(s.name, doc.date, d.disease_id, true, false) text_count " +
                        "       FROM disease d " +
                        "              INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "              INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "              INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "              INNER JOIN source s ON s.source_id = hs.source_id " +
                        "       WHERE s.name COLLATE utf8_bin = :source " +
                        "         AND doc.date = :version " +
                        "         AND d.relevant = true " +
                        "       ORDER BY d.name ASC " +
                        "     ) dsyn " +
                        "WHERE ( dsyn.text_count > 0 OR LENGTH(dsyn.code_list) > 0 )"
        ),


        //----------------------------


        @NamedNativeQuery(
                name = "Disease.findByIdNative",
                query = "SELECT d.disease_id, d.name, d.cui "
                        + "FROM disease d WHERE d.disease_id COLLATE utf8_bin = :diseaseId",
                resultSetMapping="DiseaseMapping"

        ),
        @NamedNativeQuery(
                name = "Disease.findByIdNativeResultClass",
                query = "SELECT d.disease_id, d.name, d.cui "
                        + "FROM disease d WHERE d.disease_id COLLATE utf8_bin = :diseaseId",
                resultClass = Disease.class

        ),
        @NamedNativeQuery(
                name = "Disease.findByMatchExactNameTrueNativeResultClass",
                query = "SELECT d.disease_id, d.name, d.cui "
                        + "FROM disease d WHERE d.name COLLATE utf8_bin = :name",
                resultClass = Disease.class
        ),



        @NamedNativeQuery(
                name = "Disease.findBySourceAndVersionAndMatchExactNameTrueNative",
                query = "SELECT DISTINCT d.disease_id 'diseaseCode', d.name 'diseaseName', d.cui, getDocumentUrl(sce.name, doc.date, d.disease_id) 'url', getDisnetConceptsCount(sce.name, doc.date, d.disease_id) 'disnetConceptCount' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "-- url \n" +
                        "-- INNER JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date " +
                        "-- INNER JOIN url u ON u.url_id = docu.url_id \n " +
                        "WHERE sce.name = :source " +
                        "AND hs.date = :version " +
                        "AND d.relevant = true " +
                        "AND d.name COLLATE utf8_bin = :disease "
        ),
        @NamedNativeQuery(
                name = "Disease.findBySourceAndVersionAndMatchExactNameFalseNative",
                query = " SELECT DISTINCT d.disease_id 'diseaseCode', d.name 'diseaseName', d.cui, getDocumentUrl(sce.name, doc.date, d.disease_id) 'url', getDisnetConceptsCount(sce.name, doc.date, d.disease_id) 'disnetConceptCount' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "-- url \n" +
                        "-- INNER JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date " +
                        "-- INNER JOIN url u ON u.url_id = docu.url_id \n " +
                        "WHERE sce.name = :source " +
                        "AND hs.date = :version " +
                        "AND d.relevant = true " +
                        "AND d.name LIKE :disease "
        ),


        @NamedNativeQuery(
                name = "Disease.insertNative",
                query = "INSERT INTO disease (disease_id, name, cui) "
                        + "VALUES (:diseaseId, :name, :cui)"
        ),
        @NamedNativeQuery(
                name = "HasDisease.insertNative",
                query = "INSERT INTO has_disease (document_id, date, disease_id) "
                        + "VALUES (:documentId, :date, :diseaseId)"
        ),

        // DiseaseHelperNative getDiseasesAndTheirDisnetConcepts <SIN USAR>
        // -- <<<findingsList>>> (POR NOMBRE DE ENFERMEDAD) CUALES SITOMAS CON SUS SEMANTIC TYPES TIENE UNA ENFERMEDAD "nombre exacto" POR FUENTE Y VERSION Y SI ESTAN VALIDADOS
        @NamedNativeQuery(
                name = "Disease.findSymptomsBySourceAndVersionAndMatchExactNameTrueAndValidatedNative",
                query = "SELECT DISTINCT hsym.cui 'symptom', sym.name 'symptomName', hsym.validated, d.disease_id 'diseaseCode', d.name 'diseaseName', getDocumentUrl(sce.name, doc.date, d.disease_id) 'url', getSemanticTypesBySymptom(sym.cui) 'semantic_types'-- , ht.text_id \n" +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "-- url\n" +
                        "-- INNER JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date " +
                        "-- INNER JOIN url u ON u.url_id = docu.url_id \n " +
                        "-- symptoms\n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date  " +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                        "INNER JOIN symptom sym ON sym.cui = hsym.cui " +
                        "WHERE sce.name = :sourceName " +
                        "AND hs.date = :version " +
                        "AND d.name COLLATE utf8_bin = :diseaseName " +
                        "AND hsym.validated = :validated -- filtering "
        ),
        // -- <<<findingsList>>> (POR ID DE ENFERMEDAD) CUALES SITOMAS CON SUS SEMANTIC TYPES TIENE UNA ENFERMEDAD "id de la enfermedad" POR FUENTE Y VERSION Y SI ESTAN VALIDADOS
        @NamedNativeQuery(
                name = "Disease.findSymptomsBySourceAndVersionAndDiseaseIdAndValidatedNative",
                query = "SELECT DISTINCT hsym.cui 'symptom', sym.name 'symptomName', hsym.validated, d.disease_id 'diseaseCode', d.name 'diseaseName', getSemanticTypesBySymptom(sym.cui) 'semantic_types' \n" +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        //-- symptoms
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                        "INNER JOIN symptom sym ON sym.cui = hsym.cui " +
                        "WHERE sce.name = :source " +
                        "AND hs.date = :version " +
                        "AND d.disease_id = :diseaseId " +
                        "AND hsym.validated = :validated -- filtering "
        ),

        // DiseaseHelperNative getDiseasesAndTheirDisnetConcepts <SIN USAR>
        // -- <<<findingsList>>> (POR CODIGOS DE ENFERMEDAD) CUALES SITOMAS CON SUS SEMANTIC TYPES TIENE UNA ENFERMEDAD "code y typeCode" POR FUENTE Y VERSION Y SI ESTAN VALIDADOS
        @NamedNativeQuery(
                name = "Disease.findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndValidatedNative",
                query = "SELECT DISTINCT sym.cui 'symptom', sym.name 'symptomName', hsym.validated, d.disease_id 'diseaseCode', d.name 'diseaseName', getDocumentUrl(sce.name, doc.date, d.disease_id) 'url', getSemanticTypesBySymptom(sym.cui) 'semantic_types' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "-- url\n" +
                        "-- INNER JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date " +
                        "-- INNER JOIN url u ON u.url_id = docu.url_id " +
                        "-- symptoms\n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                        "INNER JOIN symptom sym ON sym.cui = hsym.cui " +
                        "-- code\n" +
                        "INNER JOIN has_code hc ON hc.document_id = doc.document_id AND hc.date = doc.date " +
                        "INNER JOIN code c ON c.code = hc.code AND c.resource_id = hc.resource_id " +
                        "INNER JOIN resource r ON r.resource_id = c.resource_id " +
                        "WHERE sce.name = :source " +
                        "AND doc.date = :version " +
                        "AND c.code = :code " +
                        "AND r.name = :resource " +
                        "AND hsym.validated = :validated " +
                        "ORDER BY d.disease_id ASC "
        ),
        // AQUI NO SE USARÁ. SINO EN EL REPOSITORY-- <<<findingsList>>> (POR CODIGOS DE ENFERMEDAD) CUALES SITOMAS CON SUS SEMANTIC TYPES TIENE UNA ENFERMEDAD "code y typeCode" POR FUENTE Y VERSION Y SI ESTAN VALIDADOS Y POR SEMANTICS TYPES
        @NamedNativeQuery(
                name = "Disease.findSymptomsBySourceAndVersionAndCodeAndDiseaseNameAndValidatedAndSemanticTypesNative",
                query = "SELECT DISTINCT sym.cui 'symptom', sym.name 'symptomName', hsym.validated, d.disease_id 'diseaseCode', d.name 'diseaseName', getDocumentUrl(sce.name, doc.date, d.disease_id) 'url', getSemanticTypesBySymptom(sym.cui) 'semantic_types' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "-- url\n" +
                        "-- INNER JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date " +
                        "-- INNER JOIN url u ON u.url_id = docu.url_id \n " +
                        "-- symptoms\n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                        "INNER JOIN symptom sym ON sym.cui = hsym.cui " +
                        "INNER JOIN has_semantic_type hst ON hst.cui = sym.cui " +
                        "WHERE sce.name = :source " +
                        "AND hs.date = :version " +
                        "AND d.name COLLATE utf8_bin = :disease " +
                        "AND hsym.validated = :validated " +
                        "AND hst.semantic_type = 'dsyn' "
        ),


        @NamedNativeQuery(
                name = "Disease.findAllBySourceAndVersionNative",
                query = "SELECT DISTINCT d.disease_id, d.name, d.cui, getDocumentUrl(s.name, doc.date, d.disease_id) 'url', getDisnetConceptsCount(s.name, doc.date, d.disease_id) 'disnetConceptCount' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source s ON s.source_id = hs.source_id " +
                        "-- url\n" +
                        "-- INNER JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date " +
                        "-- INNER JOIN url u ON u.url_id = docu.url_id\n " +
                        "WHERE s.name COLLATE utf8_bin = :source " +
                        "AND doc.date = :version " +
                        "ORDER BY d.name ASC"
        ),
        // (1) OBTIENE ENFERMEDADES CON MAS DE 20 SINTOMAS (SIN REPETIR ENFERMEDAD) CON EL ANTERIOR DE REPITE PORQUE LA URL ES DIFERENTE
        @NamedNativeQuery(
                name = "Disease.findAllBySourceAndVersionAndSymptomsCountNative",
                query = "SELECT DISTINCT d.name 'diseaseName', d.disease_id 'diseaseCode', d.cui, getDisnetConceptsCount(sce.name, doc.date, d.disease_id) 'disnetConceptCount', getDocumentInfo(sce.name, doc.date, d.disease_id) 'docInfo' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "WHERE sce.name = :source " +
                        "AND hs.date = :version  " +
                        "AND d.relevant = true " +
                        "AND getDisnetConceptsCount(sce.name, doc.date, d.disease_id) > :symptoms " +
                        "ORDER BY getDisnetConceptsCount(sce.name, doc.date, d.disease_id) DESC "
        ),
        // (2) TERMINOS DE UNA ENFERMEDAD VALIDADOS Y NO. SEGUN UN DOCUMENTO, PORQUE HAY DOCUMENTOS DE LOS QUE SE HABLA DE UNA MISMA ENFERMEDAD
        @NamedNativeQuery(
                name = "Disease.findTermsBySourceAndVersionAndDocumentAndDiseaseIdNative",
                query = "SELECT DISTINCT sym.cui, sym.name, hsym.validated, getSemanticTypesBySymptom(sym.cui) 'semantic_types' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
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
                        "AND doc.document_id = :documentId " +
                        "AND d.disease_id = :diseaseId " +
                        "GROUP BY sym.cui, sym.name, hsym.validated " +
                        "ORDER BY hsym.validated DESC "
        ),
        // (3) OBTIENE LOS TEXTOS DE DONDE FUE OBTENIDO EL TERMINO
        @NamedNativeQuery(
                name = "Disease.findTextsBySourceAndVersionAndDocumentAndDiseaseIdAndCuiNative",
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
                        "-- disease \n" +
                        "INNER JOIN has_disease hd ON hd.document_id = doc.document_id AND hd.date = doc.date " +
                        "INNER JOIN disease d ON d.disease_id = hd.disease_id " +
                        "WHERE sce.name = :source " +
                        "AND ht.date = :version " +
                        "AND ht.document_id = :documentId " +
                        "AND d.disease_id = :diseaseId " +
                        "AND sym.cui = :cui " +
                        "ORDER BY sec.description, ht.text_order ASC "
        ),
        // (4) OBTIENE TODOS TEXTOS DE UN DOCUMENTO Y ENFERMEDAD
        @NamedNativeQuery(
                name = "Disease.findTextsBySourceAndVersionAndDocumentAndDiseaseIdNative",
                query = "SELECT sec.description, ht.text_order, t.text_id, t.text " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "-- section\n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                        "INNER JOIN section sec ON sec.section_id = hsec.section_id " +
                        "-- texts\n" +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "INNER JOIN text t ON t.text_id = ht.text_id " +
                        "WHERE sce.name = :source " +
                        "AND ht.date = :version " +
                        "AND ht.document_id = :documentId " +
                        "AND d.disease_id = :diseaseId " +
                        "ORDER BY sec.description, ht.text_order ASC "
        ),
        @NamedNativeQuery(
                name = "Disease.findByIdAndSourceAndVersionNative",
                query = "SELECT d.disease_id, d.name, d.cui " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source s ON s.source_id = hs.source_id " +
                        "WHERE s.name COLLATE utf8_bin = :sourceName " +
                        "AND doc.date = :version " +
                        "AND d.disease_id = :diseaseId "
        ),
        @NamedNativeQuery(
                name = "Disease.findByCuiAndSourceAndVersionNative",
                query = "SELECT d.disease_id, d.name, d.cui " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source s ON s.source_id = hs.source_id " +
                        "WHERE s.name COLLATE utf8_bin = :sourceName " +
                        "AND doc.date = :version " +
                        "AND d.cui = :cui "
        ),
        @NamedNativeQuery(
                name = "Disease.findBySourceAndVersionAndMatchExactNameFalseNative_enable",
                query = "SELECT d.disease_id, d.name, d.cui " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source s ON s.source_id = hs.source_id " +
                        "WHERE s.name COLLATE utf8_bin = :sourceName " +
                        "AND doc.date = :version " +
                        "AND d.name LIKE :diseaseName "
        ),
        @NamedNativeQuery(
                name = "Disease.updateCuiByIdAndSourceAndVersionNative",
                query = " UPDATE disease d " +
                        " INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        " INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        " INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        " INNER JOIN source s ON s.source_id = hs.source_id " +
                        " SET d.cui = :cui " +
                        " WHERE s.name COLLATE utf8_bin = :sourceName " +
                        " AND doc.date = :version " +
                        " AND d.disease_id = :diseaseId "
        ),



        //-- <<<numberDisease>>> NUMERO ENFERMEDADES
        @NamedNativeQuery(
                name = "Disease.numberDiseaseBySourceAndVersionNative",
                query = "SELECT COUNT(d.name) " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "WHERE sce.name = :source " +
                        "AND d.relevant = true " +
                        "AND hs.date = :version "
        ),
        //-- <<<diseaseList>>> LISTA DE ENFERMEDADES
        @NamedNativeQuery(
                name = "Disease.findAllBySourceAndVersionNative_",
                query = " SELECT d.disease_id, d.name " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "WHERE sce.name = :source " +
                        "AND hs.date = :version "
        ),
        //-- <<<diseaseList>>> LISTA DE ENFERMEDADES, SUS URLS Y EL NUMERO DE SINTOMAS
        @NamedNativeQuery(
                name = "Disease.findAllWithUrlAndSymptomsCountBySourceAndVersionNative",
                query = "SELECT d.disease_id, d.name, getDocumentUrl(sce.name, doc.date, d.disease_id) 'url', COUNT(DISTINCT hsym.cui) 'symptoms' " +
                        "FROM disease d  " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id" +
                        "-- url  " +
                        "INNER JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date " +
                        "INNER JOIN url u ON u.url_id = docu.url_id \n " +
                        "-- symptoms\n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                        "INNER JOIN symptom sym ON sym.cui = hsym.cui " +
                        "WHERE sce.name = :source " +
                        "AND doc.date = :version " +
                        "AND hsym.validated = :validated " +
                        "GROUP BY d.disease_id, d.name, u.url " +
                        "ORDER BY d.name ASC "
        ),
        //-- <<<diseaseWithFewerSymptoms>>> 10 ENFERMEDADES CON MENOS SINTOMAS
        @NamedNativeQuery(
                name = "Disease.withFewerSymptomsBySourceAndVersionAndValidatedNative",
                query = "SELECT DISTINCT d.disease_id, d.name,  COUNT(DISTINCT hsym.cui) 'findings' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id \n" +
                        "-- symptoms\n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                        "INNER JOIN symptom sym ON sym.cui = hsym.cui " +
                        "WHERE sce.name = :source " +
                        "AND hs.date = :version \n" +
                        "-- AND d.name LIKE 'Gastroenteritis' \n" +
                        "AND hsym.validated = :validated " +
                        "GROUP BY d.disease_id, d.name " +
                        "ORDER BY COUNT(DISTINCT hsym.cui) ASC "
        ),
        //-- <<<diseaseWithMoreSymptoms>>> 10 ENFERMEDADES CON MAS SINTOMAS "NO SE USARÁ AQUÍ"
        @NamedNativeQuery(
                name = "Disease.withMoreSymptomsBySourceAndVersionAndValidatedNative",
                query = "SELECT DISTINCT d.disease_id 'diseaseCode', d.name 'diseaseName', d.cui, getDocumentUrl(sce.name, doc.date, d.disease_id) 'url', COUNT(DISTINCT hsym.cui) 'disnetConceptCount' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "-- url\n" +
                        "-- INNER JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date " +
                        "-- INNER JOIN url u ON u.url_id = docu.url_id \n " +
                        "-- symptoms--\n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                        "INNER JOIN symptom sym ON sym.cui = hsym.cui " +
                        "WHERE sce.name = :source " +
                        "AND hs.date = :version " +
                        "-- AND d.name LIKE 'Gastroenteritis' \n" +
                        "AND hsym.validated = :validated " +
                        "GROUP BY d.disease_id, d.name, d.cui, u.url " +
                        "ORDER BY COUNT(DISTINCT hsym.cui) DESC "
        ),
        //-- <<<diseaseList>>> LISTA DE ENFERMEDADES, SUS URLS Y SUS CODIGOS
        @NamedNativeQuery(
                name = "Disease.findCodesBySourceAndVersionAndDiseaseNameNative",
                query = "SELECT d.disease_id, d.name, u.url, hc.code , r.name 'resource' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "-- url\n" +
                        "INNER JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date " +
                        "INNER JOIN url u ON u.url_id = docu.url_id " +
                        "-- codes\n" +
                        "INNER JOIN has_code hc ON hc.document_id = doc.document_id AND hc.date = doc.date " +
                        "INNER JOIN code c ON c.code = hc.code AND c.resource_id = hc.resource_id " +
                        "INNER JOIN resource r ON r.resource_id = c.resource_id " +
                        "WHERE sce.name = :source " +
                        "AND doc.date = :version " +
                        "AND d.name LIKE :disease " +
                        "ORDER BY d.name ASC "
        ),
        //-- <<<diseaseList>>> LISTA DE ENFERMEDADES SEGUN SUS CODIGOS
        @NamedNativeQuery(
                name = "Disease.findBySourceAndVersionAndCodeAndTypeCodeNative",
                query = "SELECT DISTINCT d.disease_id 'diseaseCode', d.name 'diseaseName', d.cui, getDocumentUrl(sce.name, doc.date, d.disease_id) 'url', getDisnetConceptsCount(sce.name, doc.date, d.disease_id) 'disnetConceptCount', c.code, r.name " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "-- url \n" +
                        "-- INNER JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date " +
                        "-- INNER JOIN url u ON u.url_id = docu.url_id \n " +
                        "-- code \n" +
                        "INNER JOIN has_code hc ON hc.document_id = doc.document_id AND hc.date = doc.date " +
                        "INNER JOIN code c ON c.code = hc.code AND c.resource_id = hc.resource_id " +
                        "INNER JOIN resource r ON r.resource_id = c.resource_id " +
                        "WHERE sce.name = :source " +
                        "AND doc.date = :version " +
                        "AND c.code = :code " +
                        "AND r.name = :resource " +
                        "AND d.relevant = true " +
                        "ORDER BY d.name ASC "
        )


        ,
        //-- <<<codeListByDiseaseId>>> CODIGOS DE UNA ENFERMEDAD
        @NamedNativeQuery(
                name = "Disease.findCodesBySourceAndVersionAndDiseaseIdNative",
                query = "SELECT hc.code , r.name " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "-- codes \n" +
                        "INNER JOIN has_code hc ON hc.document_id = doc.document_id AND hc.date = doc.date " +
                        "INNER JOIN code c ON c.code = hc.code AND c.resource_id = hc.resource_id " +
                        "INNER JOIN resource r ON r.resource_id = c.resource_id " +
                        "WHERE sce.name = :source " +
                        "AND doc.date = :version " +
                        "AND d.disease_id = :diseaseId "
        )


        ,
        //-- <<<getDocumentId>>> ID DE DOCUMENTO DE UNA ENFERMEDAD, RETORNA EL PRIMER DOCUMENTO EN CASO
        // DE QUE LA ENFERMEDAD TENGA UN REDIRECCIONAMIENTO (DOS DOCUMENTOS ASOCIADOS)
        @NamedNativeQuery(
                name = "Disease.findDocumentIdBySourceAndVersionAndDiseaseIdNative",
                query = "SELECT doc.document_id " +
                        "FROM disease dis " +
                        "INNER JOIN has_disease hd ON hd.disease_id = dis.disease_id  " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "-- source\n" +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date  " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "WHERE sce.name = :source " +
                        "AND doc.date = :version " +
                        "AND dis.disease_id = :diseaseId " +
                        "ORDER BY CAST( SUBSTRING( doc.document_id , 9) AS UNSIGNED) ASC LIMIT 1 "
        )


        ,
        //-- <<<getDetectionInformationTheDISNETConcept>>> Obtiene la información de por cuales palabras
        // fue encontrado el concepto y en que posiciones se encuentra
        @NamedNativeQuery(
                name = "Disease.findDetectionInformationBySourceAndVersionAndDocumentIdAndDiseaseIdAndCuiAndValidatedNative",
                query = "SELECT hsym.text_id, hsym.matched_words, hsym.positional_info  " +
                        "FROM disease d  " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id  " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date  " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id  " +
                        "-- section  \n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date  " +
                        "-- texts \\n \n" +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "-- symptom\\n \n" +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                        "WHERE sce.name = :source  " +
                        "AND hs.date = :version  " +
                        "AND doc.document_id = :documentId " +
                        "AND d.disease_id = :diseaseId " +
                        "AND hsym.cui = :cui " +
                        "AND hsym.validated = :validated "
        ),
        //Busca las urls de todos los papers pertenecientes a una enfermedad, de una fuente, de una version
        @NamedNativeQuery(
                name = "Disease.findPaperUrlsBySourceAndVersionAndDiseaseIdNative",
                query = "SELECT docset.paper_id, u.url " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "-- source\n" +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source s ON s.source_id = hs.source_id " +
                        "-- papers\n" +
                        "INNER JOIN document_set docset ON docset.document_id = doc.document_id AND docset.date = doc.date " +
                        "INNER JOIN paper_url pu ON pu.paper_id = docset.paper_id " +
                        "INNER JOIN url u ON u.url_id = pu.url_id " +
                        "WHERE s.name COLLATE utf8_bin = :source " +
                        "AND doc.date = :version " +
                        "AND d.disease_id = :diseaseId "
        ),




        //Obtener el total de artículos de enfermedades extraidas
        @NamedNativeQuery(
                name = "Disease.getExtractedDiseasesTotBySourceAndSnapshotNative",
                query = "SELECT COUNT(distinct d.name) 'diseases_count' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "-- source \n" +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "WHERE (sce.name COLLATE utf8_bin = :source AND doc.date = :snapshot) "
        ),
        //Obtener el número de enfermedades con al menos un termino médico validado
        @NamedNativeQuery(
                name = "Disease.getDiseasesNumberWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative",
                query = "SELECT COUNT(distinct d.name) 'diseases_count' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "-- source \n" +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                        "WHERE hsym.validated = :validated " +
                        "AND (sce.name COLLATE utf8_bin = :source AND doc.date = :snapshot) "
        ),
        //Obtener el número de enfermedades relevantes (relevant = true) con al menos un termino médico validado
        @NamedNativeQuery(
                name = "Disease.getRelevantDiseasesNumberWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative",
                query = "SELECT COUNT(distinct d.name) 'diseases_count' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "-- source \n" +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                        "WHERE hsym.validated = :validated " +
                        "AND d.relevant = :relevant " +
                        "AND (sce.name COLLATE utf8_bin = :source AND doc.date = :snapshot) "
        ),



        //Obtener el número total de enfermedades con al menos un termino médico validado de una fuente
        @NamedNativeQuery(
                name = "Disease.getDiseasesNumberWithALeastOneValidatedMedicalTermsBySourceNative",
                query = "SELECT COUNT(distinct d.name) 'diseases_count' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "-- source \n" +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                        "WHERE hsym.validated = :validated " +
                        "AND sce.name COLLATE utf8_bin = :source "
        ),



        //Obtener el número total de términos médicos validados de una fuente y una snapshot
        @NamedNativeQuery(
                name = "Disease.getTotalValidatedMedicalTermsNumberBySourceAndSnapshotNative",
                query = "SELECT COUNT(distinct hsym.cui) 'medical_temr_count'\n" +
                        "FROM disease d \n" +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id \n" +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date \n" +
                        "-- source \n" +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date \n" +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id \n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date \n" +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id \n" +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id \n" +
                        "-- INNER JOIN symptom sym ON sym.cui = hsym.cui\n" +
                        "WHERE sce.name COLLATE utf8_bin = :source " +
                        "AND doc.date = :snapshot "
        ),
        //Obtener el número de términos médicos validados de una fuente y una snapshot
        @NamedNativeQuery(
                name = "Disease.getValidatedMedicalTermsNumberBySourceAndSnapshotNative",
                query = "SELECT COUNT(distinct hsym.cui) 'medical_temr_count'\n" +
                        "FROM disease d \n" +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id \n" +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date \n" +
                        "-- source \n" +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date \n" +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id \n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date \n" +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id \n" +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id \n" +
                        "-- INNER JOIN symptom sym ON sym.cui = hsym.cui\n" +
                        "WHERE sce.name COLLATE utf8_bin = :source " +
                        "AND doc.date = :snapshot " +
                        "AND hsym.validated = :validated "
        ),




        //Obtener el total de textos de una fuente y snapshot
        @NamedNativeQuery(
                name = "Disease.getTotalTextsBySourceAndSnapshotNative",
                query = "SELECT COUNT(distinct ht.text_id) 'texts_count'\n" +
                        "FROM disease d \n" +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id \n" +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date \n" +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date \n" +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id \n" +
                        "-- source and version\n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date \n" +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id  " +
                        "WHERE (sce.name COLLATE utf8_bin = :source AND doc.date = :snapshot) "
        ),
        //Obtener el número de enfermedades con al menos un termino médico validado
        @NamedNativeQuery(
                name = "Disease.getTextsNumberWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative",
                query = "SELECT COUNT(distinct ht.text_id) 'texts_count'\n" +
                        "FROM disease d \n" +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id \n" +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date \n" +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date \n" +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id \n" +
                        "-- source and version\n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date \n" +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id \n" +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id  " +
                        "WHERE hsym.validated = :validated " +
                        "AND (sce.name COLLATE utf8_bin = :source AND doc.date = :snapshot) "
        ),




        //Obtener el total de artículos de enfermedad con al menos un término médico validado y con al menos un código de enfermedad
        @NamedNativeQuery(
                name = "Disease.getDiseasesNumberWithCodesAndWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative",
                query = "SELECT COUNT(distinct d.name) 'diseases_count'-- sce.name, COUNT(distinct d.name) 'diseases_count' -- distinct d.name, sce.name -- , u.url-- COUNT(distinct d.name) 'diseases_count' \n" +
                        "FROM disease d \n" +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id \n" +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date \n" +
                        "-- source \n" +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date \n" +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id \n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date \n" +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id \n" +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id \n" +
                        "-- codes \n" +
                        "INNER JOIN has_code hc ON hc.document_id = doc.document_id AND hc.date = doc.date\n" +
                        "WHERE hsym.validated = :validated " +
                        "AND (sce.name COLLATE utf8_bin = :source AND doc.date = :snapshot) "
        ),


        //Obtener los códigos de una fuente y una snapshot
        @NamedNativeQuery(
                name = "Disease.getDiseasesCodesNumberBySourceAndSnapshotNative",
                query = "SELECT COUNT(distinct hc.code, hc.resource_id) 'codes_count'\n" +
                        "FROM disease d \n" +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id \n" +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date \n" +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date \n" +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id \n" +
                        "-- codes \n" +
                        "INNER JOIN has_code hc ON hc.document_id = doc.document_id AND hc.date = doc.date  " +
                        "WHERE (sce.name COLLATE utf8_bin = :source AND doc.date = :snapshot) "
        ),



        //Obtener los códigos de una fuente y una snapshot
        @NamedNativeQuery(
                name = "Disease.getWikipediaMedicalVocabulariesNative",
                query = "SELECT DISTINCT res.name \n" +
                        "FROM resource res\n" +
                        "INNER JOIN code c ON c.resource_id = res.resource_id\n" +
                        "INNER JOIN has_code hc ON hc.resource_id = c.resource_id " +
                        "ORDER BY res.name ASC "
        )





})

@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "DiseaseMapping",
                entities = @EntityResult(
                        entityClass = Disease.class,
                        fields = {
                                @FieldResult(name = "diseaseId", column = "disease_id"),
                                @FieldResult(name = "name", column = "name"),
                                @FieldResult(name = "cui", column = "cui"),
                                @FieldResult(name = "relevant", column = "relevant")
                        }
                )
        )
})

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="diseaseId")
public class Disease {
    private String diseaseId;
    private String name;
    private String cui;
    private Byte relevant;
    private List<HasDisease> hasDiseasesByDiseaseId;

    @Id
    @Column(name = "disease_id", nullable = false, length = 150)
    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 150)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "cui", nullable = true, length = 8)
    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    @Basic
    @Column(name = "relevant", nullable = true)
    public Byte getRelevant() {
            return relevant;
    }

    public void setRelevant(Byte relevant) {
            this.relevant = relevant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disease disease = (Disease) o;
        return Objects.equals(diseaseId, disease.diseaseId) &&
                Objects.equals(name, disease.name) &&
                Objects.equals(cui, disease.cui);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diseaseId, name, cui);
    }

    @OneToMany(mappedBy = "diseaseByDiseaseId")
    public List<HasDisease> getHasDiseasesByDiseaseId() {
        return hasDiseasesByDiseaseId;
    }

    public void setHasDiseasesByDiseaseId(List<HasDisease> hasDiseasesByDiseaseId) {
        this.hasDiseasesByDiseaseId = hasDiseasesByDiseaseId;
    }
}
