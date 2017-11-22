package edu.upm.midas.data.relational.repository.impl;

import edu.upm.midas.data.relational.entities.edsssdb.Disease;
import edu.upm.midas.data.relational.repository.AbstractDao;
import edu.upm.midas.data.relational.repository.DiseaseRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

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
    public Object[] numberDiseasesBySourceAndVersion(String sourceName, Date version) {
        Object[] numberOfDiseases = null;
        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.numberDiseaseBySourceAndVersion")
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
                .createNamedQuery("Disease.findAllBySourceAndVersion")
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
    public List<Object[]> withFewerSymptomsBySourceAndVersionAndValidated(String sourceName, Date version, boolean isValidated, int limit) {
        List<Object[]> diseases = null;
        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.withFewerSymptomsBySourceAndVersionAndValidated")
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
    public List<Object[]> withMoreSymptomsBySourceAndVersionAndValidated(String sourceName, Date version, boolean isValidated, int limit) {
        List<Object[]> diseases = null;
        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.withMoreSymptomsBySourceAndVersionAndValidated")
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
    public List<Object[]> findSymptomsBySourceAndVersionAndDiseaseNameAndValidated(String sourceName, Date version, String diseaseName, boolean isValidated) {
        List<Object[]> diseasesWithSymptoms = null;
        List<Object[]> symptomsList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findSymptomsBySourceAndVersionAndDiseaseNameAndValidated")
                .setParameter("sourceName", sourceName)
                .setParameter("version", version)
                .setParameter("diseaseName", "%" + diseaseName + "%")
                .setParameter("validated", isValidated)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(symptomsList))
            diseasesWithSymptoms = symptomsList;
        return diseasesWithSymptoms;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findSymptomsBySourceAndVersionAndDiseaseIdAndValidated(String sourceName, Date version, String diseaseId, boolean isValidated) {
        List<Object[]> diseasesWithSymptoms = null;
        List<Object[]> symptomsList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Disease.findSymptomsBySourceAndVersionAndDiseaseIdAndValidated")
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
