package edu.upm.midas.repository.jpa.impl;
import edu.upm.midas.common.util.Common;
import edu.upm.midas.model.jpa.HasSemanticType;
import edu.upm.midas.model.jpa.Symptom;
import edu.upm.midas.repository.jpa.AbstractDao;
import edu.upm.midas.repository.jpa.SymptomRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 19/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className SymptomRepositoryImpl
 * @see
 */
@Repository("SymptomRepositoryDao")
public class SymptomRepositoryImpl extends AbstractDao<String, Symptom>
                                    implements SymptomRepository {

    @Autowired
    private Common common;

    public Symptom findById(String cui) {
        Symptom symptom = getByKey(cui);
        return symptom;
    }

    @SuppressWarnings("unchecked")
    public Symptom findByIdQuery(String cui) {
        Symptom symptom = null;
        List<Symptom> symptomList = (List<Symptom>) getEntityManager()
                .createNamedQuery("Symptom.findByIdNativeResultClass")
                .setParameter("cui", cui)
                .getResultList();
        if (CollectionUtils.isNotEmpty(symptomList))
            symptom = symptomList.get(0);
        return symptom;
    }

    @SuppressWarnings("unchecked")
    public Symptom findByNameQuery(String symptomName) {
        Symptom symptom = null;
        List<Symptom> symptomList = (List<Symptom>) getEntityManager()
                .createNamedQuery("Symptom.findByName")
                .setParameter("name", symptomName)
                .getResultList();
        if (CollectionUtils.isNotEmpty(symptomList))
            symptom = symptomList.get(0);
        return symptom;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Symptom findByIdNative(String cui) {
        Symptom symptom = null;
        List<Symptom> symptomList = (List<Symptom>) getEntityManager()
                .createNamedQuery("Symptom.findByIdNativeResultClass")
                .setParameter("cui", cui)
                .getResultList();
        if (CollectionUtils.isNotEmpty(symptomList))
            symptom = symptomList.get(0);
        return symptom;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Symptom findByIdNativeResultClass(String cui) {
        Symptom symptom = null;
        List<Symptom> symptomList = (List<Symptom>) getEntityManager()
                .createNamedQuery("Symptom.findByIdNativeResultClass")
                .setParameter("cui", cui)
                .getResultList();
        if (CollectionUtils.isNotEmpty(symptomList))
            symptom = symptomList.get(0);
        return symptom;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean findHasSemanticTypeIdNative(String cui, String semanticType) {
        List<HasSemanticType> hasSemanticTypeList = (List<HasSemanticType>) getEntityManager()
                .createNamedQuery("HasSemanticType.findByIdNative")
                .setParameter("cui", cui)
                .setParameter("semanticType", semanticType)
                .getResultList();
        return hasSemanticTypeList.size() > 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findBySourceAndVersionNative(Date version, String source) {
        List<Object[]> symptoms = null;
        List<Object[]> symptomList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Symptom.findBySourceAndVersionNative")
                .setParameter("version", version)
                .setParameter("source", source)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(symptomList))
            symptoms = symptomList;
        return symptoms;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Symptom> findAllQuery() {
        return (List<Symptom>) getEntityManager()
                .createNamedQuery("Symptom.findAll")
                .setMaxResults(0)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> mostOrLessCommonBySourceAndVersionAndIsValidatedAndForceOrExludeSemanticTypes(String sourceName, Date version, boolean isValidated, int limit, boolean mostSymptoms, boolean forceSemanticTypes, List<String> semanticTypes) {
        String mostOrLessQuery;
        String semanticTypesQuery = "";
        if (mostSymptoms) mostOrLessQuery = " ORDER BY COUNT(hsym.cui) DESC ";
        else mostOrLessQuery = " ORDER BY COUNT(hsym.cui) ASC ";
        if (semanticTypes.size() > 0) {
            if (forceSemanticTypes) semanticTypesQuery = common.createForceSemanticTypesQuery(semanticTypes);
            else semanticTypesQuery = common.createExcludeSemanticTypesQuery(semanticTypes);
        }
        Query query = getEntityManager().createNativeQuery(
                "SELECT DISTINCT sym.cui, sym.name, COUNT(hsym.cui) 'common', getSemanticTypesBySymptom(sym.cui) 'semantic_types' " +
                        "FROM disease d " +
                        "INNER JOIN has_disease hd ON hd.disease_id = d.disease_id " +
                        "INNER JOIN document doc ON doc.document_id = hd.document_id AND doc.date = hd.date " +
                        "INNER JOIN has_source hs ON hs.document_id = doc.document_id AND hs.date = doc.date " +
                        "INNER JOIN source sce ON sce.source_id = hs.source_id " +
                        "-- symptom \n" +
                        "INNER JOIN has_section hsec ON hsec.document_id = doc.document_id AND hsec.date = doc.date " +
                        "INNER JOIN has_text ht ON ht.document_id = hsec.document_id AND ht.date = hsec.date AND ht.section_id = hsec.section_id " +
                        "INNER JOIN has_symptom hsym ON hsym.text_id = ht.text_id " +
                        "INNER JOIN symptom sym ON sym.cui = hsym.cui " +
                        "-- semantic_types\n" +
                        "INNER JOIN has_semantic_type hst ON hst.cui = sym.cui " +
                        "WHERE sce.name = :source " +
                        "AND hs.date = :version " +
                        "AND hsym.validated = :validated " +
                        "AND d.relevant = true " +
                        semanticTypesQuery +
                        "-- AND getSemanticTypesBySymptom(sym.cui) = 'dsyn'\n" +
                        "-- AND hst.semantic_type = 'dsyn'\n" +
                        "GROUP BY sym.cui, sym.name " + mostOrLessQuery);

        List<Object[]> symptoms = null;
        List<Object[]> symptomList = (List<Object[]>) query
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("validated", isValidated)
                .setMaxResults(limit)
                .getResultList();
        if (CollectionUtils.isNotEmpty(symptomList))
            symptoms = symptomList;
        return symptoms;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> lessCommonBySourceAndVersionAndValidated(String sourceName, Date version, boolean isValidated, int limit) {
        List<Object[]> symptoms = null;
        List<Object[]> symptomList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Symptom.lessCommonBySourceAndVersionAndValidated")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("validated", isValidated)
                .setMaxResults(limit)
                .getResultList();
        if (CollectionUtils.isNotEmpty(symptomList))
            symptoms = symptomList;
        return symptoms;
    }

    @Override
    public void persist(Symptom symptom) {
        super.persist(symptom);
    }

    @Override
    public int insertNative(String cui, String name) {
        return getEntityManager()
                .createNamedQuery("Symptom.insertNative")
                .setParameter("cui", cui)
                .setParameter("name", name)
                .executeUpdate();
    }

    @Override
    public boolean deleteById(String cui) {
        Symptom symptom = findById( cui );
        if(symptom ==null)
            return false;
        super.delete(symptom);
        return true;
    }

    @Override
    public void delete(Symptom symptom) {
        super.delete(symptom);
    }

    @Override
    public Symptom update(Symptom symptom) {
        return super.update(symptom);
    }

    @Override
    public int updateByIdQuery(Symptom symptom) {
        return 0;
    }
}
