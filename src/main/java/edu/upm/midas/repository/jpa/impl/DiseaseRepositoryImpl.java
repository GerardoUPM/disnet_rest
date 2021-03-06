package edu.upm.midas.repository.jpa.impl;

import edu.upm.midas.common.util.Common;
import edu.upm.midas.model.jpa.Disease;
import edu.upm.midas.model.response.particular.DiseaseListPageResponse;
import edu.upm.midas.repository.jpa.AbstractDao;
import edu.upm.midas.repository.jpa.DiseaseRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private Common common;

    @Override
    @SuppressWarnings("unchecked")
    public Page<Object[]> findBySourceAndVersion(String sourceName, Date version, Pageable pageable) {
        BigInteger numberOfDiseases = null;
        List<BigInteger> countList = (List<BigInteger>) getEntityManager()
                .createNamedQuery("Disease.findBySourceAndVersionNative.count")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .getResultList();
        if (CollectionUtils.isNotEmpty(countList))
            numberOfDiseases = countList.get(0);
        else
            numberOfDiseases = BigInteger.ZERO;

        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findBySourceAndVersionNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setFirstResult(pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(diseaseList, pageable, numberOfDiseases.intValue());
    }

    @Override
    @SuppressWarnings("unchecked")
    public DiseaseListPageResponse findBySourceAndVersionNew(String sourceName, Date version, Pageable pageable) {
        BigInteger numberOfDiseases = null;
        List<BigInteger> countList = (List<BigInteger>) getEntityManager()
                .createNamedQuery("Disease.findBySourceAndVersionNative.count_pages")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .getResultList();
        if (CollectionUtils.isNotEmpty(countList))
            numberOfDiseases = countList.get(0);
        else
            numberOfDiseases = BigInteger.ZERO;

        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findBySourceAndVersionNative_pages")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setFirstResult(pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new DiseaseListPageResponse(createDiseaseList(diseaseList), pageable, numberOfDiseases.intValue());
    }

    public List<edu.upm.midas.model.Disease> createDiseaseList(List<Object[]> diseases){
        return diseases.stream().map(dis -> {
            edu.upm.midas.model.Disease diseaseDto = new edu.upm.midas.model.Disease();

            diseaseDto.setDiseaseId((String) dis[0]);
            diseaseDto.setName((String) dis[1]);
            diseaseDto.setUrl((String) dis[3]);

            try {
                diseaseDto.setDisnetConceptsCount((Integer) dis[4]);
            } catch (Exception e){
                BigInteger count = (BigInteger) dis[4];

                diseaseDto.setDisnetConceptsCount(count.intValue());
            }

            return diseaseDto;
        }).collect(Collectors.toList());
    }

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
                .createNamedQuery("Disease.findByMatchExactNameTrueNativeResultClass")
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
    public List<Object[]> findCodesBySourceAndVersionAndDiseaseIdNative(String sourceName, Date version, String diseaseId) {
        List<Object[]> diseases = null;
        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findCodesBySourceAndVersionAndDiseaseIdNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("diseaseId", diseaseId)
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
    public List<Object[]> withMoreOrFewerSymptomsBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated, int limit, boolean moreSymptoms) {
        String moreOrFewerQuery;
        if (moreSymptoms) moreOrFewerQuery = " ORDER BY COUNT(DISTINCT hsym.cui) DESC ";
        else moreOrFewerQuery = " ORDER BY COUNT(DISTINCT hsym.cui) ASC ";
        Query query = getEntityManager().createNativeQuery(
                "SELECT DISTINCT d.disease_id 'diseaseCode', d.name 'diseaseName', d.cui, u.url, COUNT(DISTINCT hsym.cui) 'disnetConceptCount' -- getDocumentUrl(sce.name, doc.date, d.disease_id) 'url' \n" +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "-- url\n" +
                        "LEFT JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date " +
                        "LEFT JOIN url u ON u.url_id = docu.url_id \n " +
                        "-- symptoms--\n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                        "INNER JOIN symptom sym ON sym.cui = hsym.cui " +
                        "WHERE sce.name = :source " +
                        "AND hs.date = :version " +
                        "-- AND d.name LIKE 'Gastroenteritis' \n" +
                        "AND hsym.validated = :validated " +
                        "AND d.relevant = true " +
                        "GROUP BY d.disease_id, d.name, d.cui, u.url " + moreOrFewerQuery);

        List<Object[]> diseases = null;
        List<Object[]> diseaseList = (List<Object[]>) query
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
        List<Object[]> symptoms = null;
        List<Object[]> symptomsList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findSymptomsBySourceAndVersionAndMatchExactNameTrueAndValidatedNative")
                .setParameter("sourceName", sourceName)
                .setParameter("version", version)
                .setParameter("diseaseName", diseaseName)
                //.setParameter("diseaseName", "%" + diseaseName + "%")
                .setParameter("validated", isValidated)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(symptomsList))
            symptoms = symptomsList;
        return symptoms;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidated(String sourceName, Date version, String diseaseId, boolean isValidated) {
        List<Object[]> symptoms = null;
        List<Object[]> symptomsList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findSymptomsBySourceAndVersionAndDiseaseIdAndValidatedNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("diseaseId",  diseaseId)
                .setParameter("validated", isValidated)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(symptomsList))
            symptoms = symptomsList;
        return symptoms;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidatedAndForceOrExludeSemanticTypes(String sourceName, Date version, String diseaseId, boolean isValidated, boolean forceSemanticTypes, List<String> semanticTypes) {
        String semanticTypesQuery = "";
        if (semanticTypes.size() > 0) {
            if (forceSemanticTypes) semanticTypesQuery = common.createForceSemanticTypesQuery(semanticTypes);
            else semanticTypesQuery = common.createExcludeSemanticTypesQuery(semanticTypes);
        }
        Query query = getEntityManager().createNativeQuery(
                "SELECT DISTINCT hsym.cui 'symptom', sym.name 'symptomName', hsym.validated, d.disease_id 'diseaseCode', d.name 'diseaseName', getSemanticTypesBySymptom(sym.cui) 'semantic_types' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "-- symptoms\n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                        "INNER JOIN symptom sym ON sym.cui = hsym.cui " +
                        "-- semantic_types\n" +
                        "INNER JOIN has_semantic_type hst ON hst.cui = sym.cui " +
                        "WHERE sce.name = :source " +
                        "AND hs.date = :version " +
                        "AND d.disease_id = :diseaseId " +
                        "AND hsym.validated = :validated " + semanticTypesQuery);

        List<Object[]> symptoms = null;
        List<Object[]> symptomsList = (List<Object[]>) query
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("diseaseId",  diseaseId)
                .setParameter("validated", isValidated)
                //.setMaxResults(100)
                .getResultList();

        if (CollectionUtils.isNotEmpty(symptomsList))
            symptoms = symptomsList;
        return symptoms;
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
    public Object[] findBySourceAndVersionAndMatchExactNameTrueNative(String sourceName, Date version, String diseaseName) {
        Object[] disease = null;
        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findBySourceAndVersionAndMatchExactNameTrueNative")
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
    public List<Object[]> findBySourceAndVersionAndMatchExactNameFalseNative(String sourceName, Date version, String diseaseName) {
        List<Object[]> diseases = null;
        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findBySourceAndVersionAndMatchExactNameFalseNative")
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
    public List<Object[]> findBySourceAndVersionAndCodeAndTypeCodeNative(String sourceName, Date version, String code, String typeCode) {
        List<Object[]> diseases = null;
        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findBySourceAndVersionAndCodeAndTypeCodeNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("code", code)
                .setParameter("resource", typeCode)
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
        String semanticTypesQuery = common.createForceSemanticTypesQuery(semanticTypes);
        Query query = getEntityManager().createNativeQuery(
                "SELECT DISTINCT sym.cui 'symptom', sym.name 'symptomName', hsym.validated, d.disease_id 'diseaseCode', d.name 'diseaseName', u.url, getSemanticTypesBySymptom(sym.cui) 'semantic_types' " +
                "FROM disease d " +
                "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                "-- url\n" +
                "LEFT JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date " +
                "LEFT JOIN url u ON u.url_id = docu.url_id " +
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
        String semanticTypesQuery = common.createExcludeSemanticTypesQuery(semanticTypes);
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT sym.cui 'symptom', sym.name 'symptomName', hsym.validated, d.disease_id 'diseaseCode', d.name 'diseaseName', u.url, getSemanticTypesBySymptom(sym.cui) 'semantic_types' " +
                "FROM disease d " +
                "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                "-- url\n" +
                "LEFT JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date " +
                "LEFT JOIN url u ON u.url_id = docu.url_id " +
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
        String semanticTypesQuery = common.createForceSemanticTypesQuery(semanticTypes);
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT sym.cui 'symptom', sym.name 'symptomName', hsym.validated, d.disease_id 'diseaseCode', u.url, d.name 'diseaseName', getSemanticTypesBySymptom(sym.cui) 'semantic_types'-- ht.text_id \n" +
                "FROM disease d \n" +
                "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id \n" +
                "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date \n" +
                "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date \n" +
                "INNER JOIN source sce ON sce.source_id = hs.source_id \n" +
                "-- url\n" +
                "LEFT JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date\n" +
                "LEFT JOIN url u ON u.url_id = docu.url_id\n" +
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
        String semanticTypesQuery = common.createExcludeSemanticTypesQuery(semanticTypes);
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT sym.cui 'symptom', sym.name 'symptomName', hsym.validated, d.disease_id 'diseaseCode', u.url, d.name 'diseaseName', getSemanticTypesBySymptom(sym.cui) 'semantic_types'-- ht.text_id \n" +
                "FROM disease d \n" +
                "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id \n" +
                "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date \n" +
                "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date \n" +
                "INNER JOIN source sce ON sce.source_id = hs.source_id \n" +
                "-- url\n" +
                "LEFT JOIN document_url docu ON docu.document_id = doc.document_id AND docu.date = doc.date\n" +
                "LEFT JOIN url u ON u.url_id = docu.url_id\n" +
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
    public List<Object[]> findAllBySourceAndVersionAndSymptomsCountNative(String sourceName, Date version, int symptoms) {
        List<Object[]> diseases = null;
        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findAllBySourceAndVersionAndSymptomsCountNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("symptoms", symptoms)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(diseaseList))
            diseases = diseaseList;
        return diseases;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findTermsBySourceAndVersionAndDocumentAndDiseaseIdNative(String sourceName, Date version, String documentId, String diseaseId) {
        List<Object[]> terms = null;
        List<Object[]> termList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findTermsBySourceAndVersionAndDocumentAndDiseaseIdNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("documentId", documentId)
                .setParameter("diseaseId", diseaseId)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(termList))
            terms = termList;
        return terms;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findTextsBySourceAndVersionAndDocumentAndDiseaseIdAndCuiNative(String sourceName, Date version, String documentId, String diseaseId, String cui) {
        List<Object[]> texts = null;
        List<Object[]> textList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findTextsBySourceAndVersionAndDocumentAndDiseaseIdAndCuiNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("documentId", documentId)
                .setParameter("diseaseId", diseaseId)
                .setParameter("cui", cui)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(textList))
            texts = textList;
        return texts;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findTextsBySourceAndVersionAndDocumentAndDiseaseIdNative(String sourceName, Date version, String documentId, String diseaseId) {
        List<Object[]> texts = null;
        List<Object[]> textList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findTextsBySourceAndVersionAndDocumentAndDiseaseIdNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("documentId", documentId)
                .setParameter("diseaseId", diseaseId)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(textList))
            texts = textList;
        return texts;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String findDocumentIdBySourceAndVersionAndDiseaseIdNative(String sourceName, Date version, String diseaseId) {
        String documentId = null;
        List<String> documentList = (List<String>) getEntityManager()
                .createNamedQuery("Disease.findDocumentIdBySourceAndVersionAndDiseaseIdNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("diseaseId", diseaseId)
                .getResultList();
        if (CollectionUtils.isNotEmpty(documentList))
            documentId = documentList.get(0);
        return documentId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findDetectionInformationBySourceAndVersionAndDocumentIdAndDiseaseIdAndCuiAndValidatedNative(String sourceName, Date version, String documentId, String diseaseId, String cui, boolean isValidated) {
        List<Object[]> detectionInformation = null;
        List<Object[]> detectionInformationList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findDetectionInformationBySourceAndVersionAndDocumentIdAndDiseaseIdAndCuiAndValidatedNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("documentId", documentId)
                .setParameter("diseaseId", diseaseId)
                .setParameter("cui", cui)
                .setParameter("validated", isValidated)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(detectionInformationList))
            detectionInformation = detectionInformationList;
        return detectionInformation;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findPaperUrlsBySourceAndVersionAndDiseaseIdNative(String sourceName, Date version, String diseaseId) {
        List<Object[]> paperUrls = null;
        List<Object[]> paperUrlList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findPaperUrlsBySourceAndVersionAndDiseaseIdNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("diseaseId", diseaseId)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(paperUrlList))
            paperUrls = paperUrlList;
        return paperUrls;
    }

    //ANALYSIS
    @SuppressWarnings("unchecked")
    @Override
    public BigInteger getExtractedDiseasesTotBySourceAndSnapshotNative(String sourceName, Date snapshot) {
        return (BigInteger) getEntityManager()
                .createNamedQuery("Disease.getExtractedDiseasesTotBySourceAndSnapshotNative")
                .setParameter("source", sourceName)
                .setParameter("snapshot", snapshot)
                //.setMaxResults(1)
                .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public BigInteger getDiseasesNumberWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative(String sourceName, Date snapshot, boolean isValidated) {
        return (BigInteger) getEntityManager()
                .createNamedQuery("Disease.getDiseasesNumberWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative")
                .setParameter("source", sourceName)
                .setParameter("snapshot", snapshot)
                .setParameter("validated", isValidated)
                //.setMaxResults(1)
                .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public BigInteger getRelevantDiseasesNumberWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative(String sourceName, Date snapshot, boolean isValidated, boolean isRelevant) {
        return (BigInteger) getEntityManager()
                .createNamedQuery("Disease.getRelevantDiseasesNumberWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative")
                .setParameter("source", sourceName)
                .setParameter("snapshot", snapshot)
                .setParameter("validated", isValidated)
                .setParameter("relevant", isRelevant)
                //.setMaxResults(1)
                .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public BigInteger getDiseasesNumberWithALeastOneValidatedMedicalTermsBySourceNative(String sourceName, boolean isValidated) {
        return (BigInteger) getEntityManager()
                .createNamedQuery("Disease.getDiseasesNumberWithALeastOneValidatedMedicalTermsBySourceNative")
                .setParameter("source", sourceName)
                .setParameter("validated", isValidated)
                //.setMaxResults(1)
                .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public BigInteger getTotalValidatedMedicalTermsNumberBySourceAndSnapshotNative(String sourceName, Date snapshot) {
        return (BigInteger) getEntityManager()
                .createNamedQuery("Disease.getTotalValidatedMedicalTermsNumberBySourceAndSnapshotNative")
                .setParameter("source", sourceName)
                .setParameter("snapshot", snapshot)
                //.setMaxResults(1)
                .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public BigInteger getValidatedMedicalTermsNumberBySourceAndSnapshotNative(String sourceName, Date snapshot, boolean isValidated) {
        return (BigInteger) getEntityManager()
                .createNamedQuery("Disease.getValidatedMedicalTermsNumberBySourceAndSnapshotNative")
                .setParameter("source", sourceName)
                .setParameter("snapshot", snapshot)
                .setParameter("validated", isValidated)
                //.setMaxResults(1)
                .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public BigInteger getTotalTextsBySourceAndSnapshotNative(String sourceName, Date snapshot) {
        return (BigInteger) getEntityManager()
                .createNamedQuery("Disease.getTotalTextsBySourceAndSnapshotNative")
                .setParameter("source", sourceName)
                .setParameter("snapshot", snapshot)
                //.setMaxResults(1)
                .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public BigInteger getTextsNumberWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative(String sourceName, Date snapshot, boolean isValidated) {
        return (BigInteger) getEntityManager()
                .createNamedQuery("Disease.getTextsNumberWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative")
                .setParameter("source", sourceName)
                .setParameter("snapshot", snapshot)
                .setParameter("validated", isValidated)
                //.setMaxResults(1)
                .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public BigInteger getDiseasesNumberWithCodesAndWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative(String sourceName, Date snapshot, boolean isValidated) {
        return (BigInteger) getEntityManager()
                .createNamedQuery("Disease.getDiseasesNumberWithCodesAndWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative")
                .setParameter("source", sourceName)
                .setParameter("snapshot", snapshot)
                .setParameter("validated", isValidated)
                //.setMaxResults(1)
                .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public BigInteger getDiseasesCodesNumberBySourceAndSnapshotNative(String sourceName, Date snapshot) {
        return (BigInteger) getEntityManager()
                .createNamedQuery("Disease.getDiseasesCodesNumberBySourceAndSnapshotNative")
                .setParameter("source", sourceName)
                .setParameter("snapshot", snapshot)
                //.setMaxResults(1)
                .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getWikipediaMedicalVocabulariesNative() {
        List<String> resourceList = null;
        List<String> list = (List<String>) getEntityManager()
                .createNamedQuery("Disease.getWikipediaMedicalVocabulariesNative")
                .getResultList();
        if (CollectionUtils.isNotEmpty(list))
            resourceList = list;
        return resourceList;
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
