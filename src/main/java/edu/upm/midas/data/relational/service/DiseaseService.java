package edu.upm.midas.data.relational.service;

import edu.upm.midas.data.relational.entities.edsssdb.Disease;
import edu.upm.midas.model.DiseaseDisnetConcepts;
import edu.upm.midas.model.DisnetConcept;
import edu.upm.midas.model.response.Code;
import edu.upm.midas.model.response.Text;

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

    Integer numberDiseasesBySourceAndVersion(String sourceName, Date version);

    List<edu.upm.midas.model.Disease> findAllBySourceAndVersion(String sourceName, Date version);

    List<Code> findCodesBySourceAndVersionAndDiseaseIdNative(String sourceName, Date version, String diseaseId);

    List<edu.upm.midas.model.Disease> findAllWithUrlAndSymptomsCountBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated);

    List<DiseaseDisnetConcepts> withFewerSymptomsBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated, int limit);

    List<edu.upm.midas.model.Disease> withMoreOrFewerSymptomsBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated, int limit, boolean moreSymptoms);

    List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndDiseaseNameAndIsValidated(String sourceName, Date version, String diseaseName, boolean isValidated);

    List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndIsValidatedNative(String sourceName, Date version, String code, String resourceName, boolean isValidated);



    List<DisnetConcept> findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidated(String sourceName, Date version, String diseaseId, boolean isValidated);

    List<DisnetConcept> findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidatedAndForceOrExludeSemanticTypes(String sourceName, Date version, String diseaseId, boolean isValidated, boolean forceSemanticTypes, List<String> semanticTypes);



    boolean existDiseaseBySourceAndVersionAndMatchExactNameTrueNative(String sourceName, Date version, String diseaseName);

    boolean existDiseaseBySourceAndVersionAndMatchExactNameFalseNative(String sourceName, Date version, String diseaseName);



    List<edu.upm.midas.model.Disease> findBySourceAndVersionAndMatchExactNameFalseNative(String sourceName, Date version, String diseaseName);

    List<edu.upm.midas.model.Disease> findBySourceAndVersionAndMatchExactNameTrueNative(String sourceName, Date version, String diseaseName);

    List<edu.upm.midas.model.Disease> findBySourceAndVersionAndCodeAndTypeCodeNative(String sourceName, Date version, String code, String typeCode);



    List<edu.upm.midas.model.Disease> findCodesBySourceAndVersionAndDiseaseNameNative(String sourceName, Date version, String diseaseName, int limit);

    List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndDiseaseNameAndValidatedAndForceSemanticTypesNative(String sourceName, Date version, String diseaseName, boolean isValidated, List<String> semanticTypes);

    List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndDiseaseNameAndValidatedAndExcludeSemanticTypesNative(String sourceName, Date version, String diseaseName, boolean isValidated, List<String> semanticTypes);

    List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndValidatedAndForceSemanticTypesNative(String sourceName, Date version, String code, String typeCode, boolean isValidated, List<String> semanticTypes);

    List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndValidatedAndExcludeSemanticTypesNative(String sourceName, Date version, String code, String typeCode, boolean isValidated, List<String> semanticTypes);


    List<edu.upm.midas.model.Disease> findAllBySourceAndVersionAndSymptomsCountNative(String sourceName, Date version, int numberSymptom);

    List<DisnetConcept> findTermsBySourceAndVersionAndDocumentAndDiseaseNative(String sourceName, Date version, String documentId, String diseaseId);

    List<Text> findTextsBySourceAndVersionAndDocumentAndDiseaseNative(String sourceName, Date version, String documentId, String diseaseId, String cui);


    List<Disease> findAll();

    void save(Disease disease);

    int insertNative(String diseaseId, String name, String cui);

    int insertNativeHasDisease(String documentId, Date date, String diseaseId);

    boolean updateFindFull(Disease disease);

    boolean updateFindPartial(Disease disease);

    boolean deleteById(String diseaseId);
    
}
