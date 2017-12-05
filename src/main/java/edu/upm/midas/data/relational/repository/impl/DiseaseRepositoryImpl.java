package edu.upm.midas.data.relational.repository.impl;

import edu.upm.midas.data.relational.entities.edsssdb.Disease;
import edu.upm.midas.data.relational.repository.AbstractDao;
import edu.upm.midas.data.relational.repository.DiseaseRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 12/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className DiseaseRepositoryImpl
 * @see
 */
@Repository("DiseaseRepositoryDao")
public class DiseaseRepositoryImpl extends AbstractDao<String, Disease>
                                    implements DiseaseRepository {


    public Disease findById(String diseaseId) {
        Disease disease = getByKey(diseaseId);
        return disease;
    }

    @SuppressWarnings("unchecked")
    public Disease findByIdQuery(String diseaseId) {
        Disease disease = null;
        List<Disease> diseaseList = (List<Disease>) getEntityManager()
                .createNamedQuery("Disease.findById")
                .setParameter("diseaseId", diseaseId)
                .getResultList();
        if (CollectionUtils.isNotEmpty(diseaseList))
            disease = diseaseList.get(0);
        return disease;
    }

    @SuppressWarnings("unchecked")
    public Disease findByNameQuery(String diseaseName) {
        Disease disease = null;
        List<Disease> diseaseList = (List<Disease>) getEntityManager()
                .createNamedQuery("Disease.findByNameNativeResultClass")
                .setParameter("name", diseaseName)
                .setMaxResults(1)
                .getResultList();
        if (CollectionUtils.isNotEmpty(diseaseList))
            disease = diseaseList.get(0);
        return disease;
    }

    @SuppressWarnings("unchecked")
    public Disease findByCuiQuery(String cui) {
        Disease disease = null;
        List<Disease> diseaseList = (List<Disease>) getEntityManager()
                .createNamedQuery("Disease.findByCui")
                .setParameter("cui", cui)
                .getResultList();
        if (CollectionUtils.isNotEmpty(diseaseList))
            disease = diseaseList.get(0);
        return disease;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Disease findLastDiseaseQuery() {
        Disease disease = null;
        List<Disease> diseaseList = (List<Disease>) getEntityManager()
                .createNamedQuery("Disease.findLastDisease")
                .setMaxResults(1)
                .getResultList();
        if (CollectionUtils.isNotEmpty(diseaseList))
            disease = diseaseList.get(0);
        return disease;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Disease findByIdNative(String diseaseId) {
        Disease source = null;
        List<Disease> listSource = (List<Disease>) getEntityManager()
                .createNamedQuery("Disease.findByIdNative")
                .setParameter("diseaseId", diseaseId)
                .getResultList();
        if (CollectionUtils.isNotEmpty(listSource))
            source = listSource.get(0);
        return source;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Disease findByIdNativeResultClass(String diseaseId) {
        Disease disease = null;
        List<Disease> diseaseList = (List<Disease>) getEntityManager()
                .createNamedQuery("Disease.findByIdNativeResultClass")
                .setParameter("diseaseId", diseaseId)
                .getResultList();
        if (CollectionUtils.isNotEmpty(diseaseList))
            disease = diseaseList.get(0);
        return disease;
    }

    @SuppressWarnings("unchecked")
    @Override
    public BigInteger numberDiseasesBySourceAndVersion(String sourceName, Date version) {
        BigInteger numberOfDiseases = null;
        List<BigInteger> diseaseList = (List<BigInteger>) getEntityManager()
                .createNamedQuery("Disease.numberDiseaseBySourceAndVersionNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .getResultList();
        if (CollectionUtils.isNotEmpty(diseaseList))
            numberOfDiseases = diseaseList.get(0);
        return numberOfDiseases;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findAllBySourceAndVersion(String sourceName, Date version) {
        List<Object[]> diseases = null;
        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findAllBySourceAndVersionNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(diseaseList))
            diseases = diseaseList;
        return diseases;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findAllWithUrlAndSymptomsCountBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated) {
        List<Object[]> diseases = null;
        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findAllWithUrlAndSymptomsCountBySourceAndVersionNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("validated", isValidated)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(diseaseList))
            diseases = diseaseList;
        return diseases;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> withFewerSymptomsBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated, int limit) {
        List<Object[]> diseases = null;
        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.withFewerSymptomsBySourceAndVersionAndValidatedNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("validated", isValidated)
                .setMaxResults(limit)
                .getResultList();
        if (CollectionUtils.isNotEmpty(diseaseList))
            diseases = diseaseList;
        return diseases;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> withMoreSymptomsBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated, int limit) {
        List<Object[]> diseases = null;//System.out.println(sourceName+" - "+ version+" - "+ isValidated+" - "+ limit);
        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.withMoreSymptomsBySourceAndVersionAndValidatedNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("validated", isValidated)
                .setMaxResults(limit)
                .getResultList();
        if (CollectionUtils.isNotEmpty(diseaseList))
            diseases = diseaseList;
        return diseases;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Disease> findAllQuery() {
        return (List<Disease>) getEntityManager()
                .createNamedQuery("Disease.findAll")
                .setMaxResults(0)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findSymptomsBySourceAndVersionAndDiseaseNameAndIsValidated(String sourceName, Date version, String diseaseName, boolean isValidated) {
        List<Object[]> diseasesWithSymptoms = null;
        List<Object[]> symptomsList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findSymptomsBySourceAndVersionAndDiseaseNameAndValidatedNative")
                .setParameter("sourceName", sourceName)
                .setParameter("version", version)
                .setParameter("diseaseName", diseaseName)
                //.setParameter("diseaseName", "%" + diseaseName + "%")
                .setParameter("validated", isValidated)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(symptomsList))
            diseasesWithSymptoms = symptomsList;
        return diseasesWithSymptoms;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidated(String sourceName, Date version, String diseaseId, boolean isValidated) {
        List<Object[]> diseasesWithSymptoms = null;
        List<Object[]> symptomsList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findSymptomsBySourceAndVersionAndDiseaseIdAndValidatedNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("diseaseId",  diseaseId)
                .setParameter("validated", isValidated)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(symptomsList))
            diseasesWithSymptoms = symptomsList;
        return diseasesWithSymptoms;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndIsValidatedNative(String sourceName, Date version, String code, String resource, boolean isValidated) {
        List<Object[]> diseasesWithSymptoms = null;
        List<Object[]> symptomsList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndValidatedNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("code",  code)
                .setParameter("resource",  resource)
                .setParameter("validated", isValidated)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(symptomsList))
            diseasesWithSymptoms = symptomsList;
        return diseasesWithSymptoms;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object[] findByExactNameAndSourceAndVersionNative(String sourceName, Date version, String diseaseName) {
        Object[] disease = null;
        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findByExactNameAndSourceAndVersionNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("disease", diseaseName)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(diseaseList))
            disease = diseaseList.get(0);
        return disease;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findByLikeNameAndSourceAndVersionNative(String sourceName, Date version, String diseaseName) {
        List<Object[]> diseases = null;
        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findByLikeNameAndSourceAndVersionNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("disease", "%" + diseaseName + "%")
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(diseaseList))
            diseases = diseaseList;
        return diseases;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findCodesBySourceAndVersionAndDiseaseNameNative(String sourceName, Date version, String diseaseName, int limit) {
        List<Object[]> diseases = null;
        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findCodesBySourceAndVersionAndDiseaseNameNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("disease", "%" + diseaseName + "%")
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(diseaseList))
            diseases = diseaseList;
        return diseases;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findSymptomsBySourceAndVersionAndDiseaseNameAndValidatedAndForceSemanticTypesNative(String sourceName, Date version, String diseaseName, boolean isValidated, List<String> semanticTypes) {
        String semanticTypesQuery = createForceSemanticTypesQuery(semanticTypes);
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT sym.cui 'symptom', sym.name 'symptomName', hsym.validated, d.disease_id 'diseaseCode', d.name 'diseaseName', u.url, getSemanticTypesBySymptom(sym.cui) 'semantic_types' " +
                "FROM disease d " +
                "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                "-- url\n" +
                "INNER JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date " +
                "INNER JOIN url u ON u.url_id = docu.url_id " +
                "-- symptoms\n" +
                "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                "INNER JOIN symptom sym ON sym.cui = hsym.cui " +
                "INNER JOIN has_semantic_type hst ON hst.cui = sym.cui " +
                "WHERE sce.name = :source " +
                "AND hs.date = :version " +
                "AND d.name COLLATE utf8_bin = :disease " +
                "AND hsym.validated = :validated " + semanticTypesQuery);

        List<Object[]> diseases = null;
        List<Object[]> diseaseList = (List<Object[]>) query.setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("disease", diseaseName)
                .setParameter("validated", isValidated)
                .getResultList();

        if (CollectionUtils.isNotEmpty(diseaseList))
            diseases = diseaseList;
        return diseases;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findSymptomsBySourceAndVersionAndDiseaseNameAndValidatedAndExcludeSemanticTypesNative(String sourceName, Date version, String diseaseName, boolean isValidated, List<String> semanticTypes) {
        String semanticTypesQuery = createExcludeSemanticTypesQuery(semanticTypes);
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT sym.cui 'symptom', sym.name 'symptomName', hsym.validated, d.disease_id 'diseaseCode', d.name 'diseaseName', u.url, getSemanticTypesBySymptom(sym.cui) 'semantic_types' " +
                "FROM disease d " +
                "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                "-- url\n" +
                "INNER JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date " +
                "INNER JOIN url u ON u.url_id = docu.url_id " +
                "-- symptoms\n" +
                "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                "INNER JOIN symptom sym ON sym.cui = hsym.cui " +
                "INNER JOIN has_semantic_type hst ON hst.cui = sym.cui " +
                "WHERE sce.name = :source " +
                "AND hs.date = :version " +
                "AND d.name COLLATE utf8_bin = :disease " +
                "AND hsym.validated = :validated " + semanticTypesQuery);

        List<Object[]> diseases = null;
        List<Object[]> diseaseList = (List<Object[]>) query.setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("disease", diseaseName)
                .setParameter("validated", isValidated)
                .getResultList();

        if (CollectionUtils.isNotEmpty(diseaseList))
            diseases = diseaseList;
        return diseases;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndValidatedAndForceSemanticTypesNative(String sourceName, Date version, String code, String typeCode, boolean isValidated, List<String> semanticTypes) {
        String semanticTypesQuery = createForceSemanticTypesQuery(semanticTypes);
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT sym.cui 'symptom', sym.name 'symptomName', hsym.validated, d.disease_id 'diseaseCode', u.url, d.name 'diseaseName', getSemanticTypesBySymptom(sym.cui) 'semantic_types'-- ht.text_id \n" +
                "FROM disease d \n" +
                "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id \n" +
                "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date \n" +
                "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date \n" +
                "INNER JOIN source sce ON sce.source_id = hs.source_id \n" +
                "-- url\n" +
                "INNER JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date\n" +
                "INNER JOIN url u ON u.url_id = docu.url_id\n" +
                "-- symptoms\n" +
                "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date \n" +
                "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id \n" +
                "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id \n" +
                "INNER JOIN symptom sym ON sym.cui = hsym.cui \n" +
                "-- semanticTypes\n" +
                "INNER JOIN has_semantic_type hst ON hst.cui = sym.cui\n" +
                "-- code\n" +
                "INNER JOIN has_code hc ON hc.document_id = doc.document_id AND hc.date = doc.date\n" +
                "INNER JOIN code c ON c.code = hc.code AND c.resource_id = hc.resource_id\n" +
                "INNER JOIN resource r ON r.resource_id = c.resource_id\n" +
                "WHERE sce.name = :source \n" +
                "AND doc.date = :version \n" +
                "AND c.code = :code \n" +
                "AND r.name = :resource \n" +
                "AND hsym.validated = :validated \n" +
                semanticTypesQuery +
                "ORDER BY d.disease_id ASC ");

        List<Object[]> diseases = null;
        List<Object[]> diseaseList = (List<Object[]>) query.setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("code", code)
                .setParameter("resource", typeCode)
                .setParameter("validated", isValidated)
                .getResultList();

        if (CollectionUtils.isNotEmpty(diseaseList))
            diseases = diseaseList;
        return diseases;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndValidatedAndExcludeSemanticTypesNative(String sourceName, Date version, String code, String typeCode, boolean isValidated, List<String> semanticTypes) {
        String semanticTypesQuery = createExcludeSemanticTypesQuery(semanticTypes);
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT sym.cui 'symptom', sym.name 'symptomName', hsym.validated, d.disease_id 'diseaseCode', u.url, d.name 'diseaseName', getSemanticTypesBySymptom(sym.cui) 'semantic_types'-- ht.text_id \n" +
                "FROM disease d \n" +
                "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id \n" +
                "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date \n" +
                "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date \n" +
                "INNER JOIN source sce ON sce.source_id = hs.source_id \n" +
                "-- url\n" +
                "INNER JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date\n" +
                "INNER JOIN url u ON u.url_id = docu.url_id\n" +
                "-- symptoms\n" +
                "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date \n" +
                "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id \n" +
                "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id \n" +
                "INNER JOIN symptom sym ON sym.cui = hsym.cui \n" +
                "-- semanticTypes\n" +
                "INNER JOIN has_semantic_type hst ON hst.cui = sym.cui\n" +
                "-- code\n" +
                "INNER JOIN has_code hc ON hc.document_id = doc.document_id AND hc.date = doc.date\n" +
                "INNER JOIN code c ON c.code = hc.code AND c.resource_id = hc.resource_id\n" +
                "INNER JOIN resource r ON r.resource_id = c.resource_id\n" +
                "WHERE sce.name = :source \n" +
                "AND doc.date = :version \n" +
                "AND c.code = :code \n" +
                "AND r.name = :resource \n" +
                "AND hsym.validated = :validated \n" +
                semanticTypesQuery +
                "ORDER BY d.disease_id ASC ");

        List<Object[]> diseases = null;
        List<Object[]> diseaseList = (List<Object[]>) query.setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("code", code)
                .setParameter("resource", typeCode)
                .setParameter("validated", isValidated)
                .getResultList();

        if (CollectionUtils.isNotEmpty(diseaseList))
            diseases = diseaseList;
        return diseases;
    }

    public String createForceSemanticTypesQuery(List<String> semanticTypes){
        String query = "";
        for (String semanticType: semanticTypes) {
            query += " AND hst.semantic_type = '" + semanticType +"' ";
        }
        return query;
    }


    public String createExcludeSemanticTypesQuery(List<String> semanticTypes){
        String query = "";
        for (String semanticType: semanticTypes) {
            query += " AND hst.semantic_type != '" + semanticType +"' ";
        }
        return query;
    }

    public void persist(Disease disease) {
        super.persist(disease);
    }

    @Override
    public int insertNative(String diseaseId, String name, String cui) {
        return getEntityManager()
                .createNamedQuery("Disease.insertNative")
                .setParameter("diseaseId", diseaseId)
                .setParameter("name", name)
                .setParameter("cui", cui)
                .executeUpdate();
    }

    @Override
    public int insertNativeHasDisease(String documentId, Date date, String diseaseId) {
        return getEntityManager()
                .createNamedQuery("HasDisease.insertNative")
                .setParameter("documentId", documentId)
                .setParameter("date", date)
                .setParameter("diseaseId", diseaseId)
                .executeUpdate();
    }

    public boolean deleteById(String diseaseId) {
        Disease disease = findById( diseaseId );
        if(disease ==null)
            return false;
        super.delete(disease);
        return true;
    }

    public void delete(Disease disease) {
        super.delete(disease);
    }

    public Disease update(Disease disease) {
        return super.update(disease);
    }

    @Override
    public int updateByIdQuery(Disease disease) {
        return getEntityManager()
                .createNamedQuery("Disease.updateById")
                .setParameter("diseaseId", disease.getDiseaseId())
                .setParameter("name", disease.getName())
                .setParameter("cui", disease.getCui())
                .executeUpdate();
    }
}
