package edu.upm.midas.data.relational.service;

import edu.upm.midas.data.relational.entities.edsssdb.Disease;
import edu.upm.midas.model.DiseaseDisnetConcepts;
import edu.upm.midas.model.DisnetConcept;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 12/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className DiseaseService
 * @see
 */
public interface DiseaseService {

    Disease findById(String diseaseId);

    Disease findByName(String diseaseName);

    Disease findByCui(String cui);

    Disease findLastDiseaseQuery();

    BigInteger numberDiseasesBySourceAndVersion(String sourceName, Date version);

    List<edu.upm.midas.model.Disease> findAllBySourceAndVersion(String sourceName, Date version);

    List<edu.upm.midas.model.Disease> findAllWithUrlAndSymptomsCountBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated);

    List<DiseaseDisnetConcepts> withFewerSymptomsBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated, int limit);

    List<DiseaseDisnetConcepts> withMoreSymptomsBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated, int limit);

    List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndDiseaseNameAndIsValidated(String sourceName, Date version, String diseaseName, boolean isValidated);

    List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndIsValidatedNative(String sourceName, Date version, String code, String resourceName, boolean isValidated);

    List<DisnetConcept> findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidated(String sourceName, Date version, String diseaseId, boolean isValidated);

    boolean existDiseaseByExactNameAndSourceAndVersionNative(String sourceName, Date version, String diseaseName);

    boolean existDiseaseByLikeNameAndSourceAndVersionNative(String sourceName, Date version, String diseaseName);

    List<Object[]> findByLikeNameAndSourceAndVersionNative(String sourceName, Date version, String diseaseName);

    List<edu.upm.midas.model.Disease> findCodesBySourceAndVersionAndDiseaseNameNative(String sourceName, Date version, String diseaseName, int limit);

    List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndDiseaseNameAndValidatedAndSemanticTypesNative(String sourceName, Date version, String diseaseName, boolean isValidated, List<String> semanticTypes);

    List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndDiseaseNameAndValidatedAndExcludeSemanticTypesNative(String sourceName, Date version, String diseaseName, boolean isValidated, List<String> semanticTypes);

    List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndValidatedAndForceSemanticTypesNative(String sourceName, Date version, String code, String typeCode, boolean isValidated, List<String> semanticTypes);

    List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndValidatedAndExcludeSemanticTypesNative(String sourceName, Date version, String code, String typeCode, boolean isValidated, List<String> semanticTypes);

    List<Disease> findAll();

    void save(Disease disease);

    int insertNative(String diseaseId, String name, String cui);

    int insertNativeHasDisease(String documentId, Date date, String diseaseId);

    boolean updateFindFull(Disease disease);

    boolean updateFindPartial(Disease disease);

    boolean deleteById(String diseaseId);
    
}
