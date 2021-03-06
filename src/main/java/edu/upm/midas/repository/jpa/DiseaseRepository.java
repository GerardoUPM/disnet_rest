package edu.upm.midas.repository.jpa;

import edu.upm.midas.model.jpa.Disease;
import edu.upm.midas.model.response.particular.DiseaseListPageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 12/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className DiseaseRepository
 * @see
 */
public interface DiseaseRepository {

    Page<Object[]> findBySourceAndVersion(String sourceName, Date version, Pageable pageable);

    DiseaseListPageResponse findBySourceAndVersionNew(String sourceName, Date version, Pageable pageable);

    Disease findById(String diseaseId);

    Disease findByIdQuery(String diseaseId);

    Disease findByNameQuery(String diseaseName);

    Disease findByCuiQuery(String cui);

    Disease findLastDiseaseQuery();

    Disease findByIdNative(String diseaseId);

    Disease findByIdNativeResultClass(String diseaseId);

    BigInteger numberDiseasesBySourceAndVersion(String sourceName, Date version);

    List<Object[]> findAllBySourceAndVersion(String sourceName, Date version);

    List<Object[]> findCodesBySourceAndVersionAndDiseaseIdNative(String sourceName, Date version, String diseaseId);

    List<Object[]> findAllWithUrlAndSymptomsCountBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated);

    List<Object[]> withFewerSymptomsBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated, int limit);

    List<Object[]> withMoreOrFewerSymptomsBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated, int limit, boolean moreSymptoms);

    List<Object[]> findSymptomsBySourceAndVersionAndDiseaseNameAndIsValidated(String sourceName, Date version, String diseaseName, boolean isValidated);



    List<Object[]> findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidated(String sourceName, Date version, String diseaseId, boolean isValidated);

    List<Object[]> findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidatedAndForceOrExludeSemanticTypes(String sourceName, Date version, String diseaseId, boolean isValidated, boolean forceSemanticTypes,List<String> semanticTypes);


    List<Object[]> findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndIsValidatedNative(String sourceName, Date version, String code, String resource, boolean isValidated);



    Object[] findBySourceAndVersionAndMatchExactNameTrueNative(String sourceName, Date version, String diseaseName);

    List<Object[]> findBySourceAndVersionAndMatchExactNameFalseNative(String sourceName, Date version, String diseaseName);

    List<Object[]> findBySourceAndVersionAndCodeAndTypeCodeNative(String sourceName, Date version, String code, String typeCode);



    List<Object[]> findCodesBySourceAndVersionAndDiseaseNameNative(String sourceName, Date version, String diseaseName, int limit);



    List<Object[]> findSymptomsBySourceAndVersionAndDiseaseNameAndValidatedAndForceSemanticTypesNative(String sourceName, Date version, String diseaseName, boolean isValidated, List<String> semanticTypes);

    List<Object[]> findSymptomsBySourceAndVersionAndDiseaseNameAndValidatedAndExcludeSemanticTypesNative(String sourceName, Date version, String diseaseName, boolean isValidated, List<String> semanticTypes);

    List<Object[]> findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndValidatedAndForceSemanticTypesNative(String sourceName, Date version, String code, String typeCode, boolean isValidated, List<String> semanticTypes);

    List<Object[]> findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndValidatedAndExcludeSemanticTypesNative(String sourceName, Date version, String code, String typeCode, boolean isValidated, List<String> semanticTypes);


    List<Object[]> findAllBySourceAndVersionAndSymptomsCountNative(String sourceName, Date version, int symptoms);

    List<Object[]> findTermsBySourceAndVersionAndDocumentAndDiseaseIdNative(String sourceName, Date version, String documentId, String diseaseId);

    List<Object[]> findTextsBySourceAndVersionAndDocumentAndDiseaseIdAndCuiNative(String sourceName, Date version, String documentId, String diseaseId, String cui);

    List<Object[]> findTextsBySourceAndVersionAndDocumentAndDiseaseIdNative(String sourceName, Date version, String documentId, String diseaseId);

    String findDocumentIdBySourceAndVersionAndDiseaseIdNative(String sourceName, Date version, String diseaseId);

    List<Object[]> findDetectionInformationBySourceAndVersionAndDocumentIdAndDiseaseIdAndCuiAndValidatedNative(String sourceName, Date version, String documentId, String diseaseId, String cui, boolean isValidated);

    List<Object[]> findPaperUrlsBySourceAndVersionAndDiseaseIdNative(String sourceName, Date version, String diseaseId);

    //ANALYSIS
    BigInteger getExtractedDiseasesTotBySourceAndSnapshotNative(String sourceName, Date snapshot);
    BigInteger getDiseasesNumberWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative(String sourceName, Date snapshot, boolean isValidated);
    BigInteger getRelevantDiseasesNumberWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative(String sourceName, Date snapshot, boolean isValidated, boolean isRelevant);

    BigInteger getDiseasesNumberWithALeastOneValidatedMedicalTermsBySourceNative(String sourceName, boolean isValidated);

    BigInteger getTotalValidatedMedicalTermsNumberBySourceAndSnapshotNative(String sourceName, Date snapshot);
    BigInteger getValidatedMedicalTermsNumberBySourceAndSnapshotNative(String sourceName, Date snapshot, boolean isValidated);

    BigInteger getTotalTextsBySourceAndSnapshotNative(String sourceName, Date snapshot);
    BigInteger getTextsNumberWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative(String sourceName, Date snapshot, boolean isValidated);

    BigInteger getDiseasesNumberWithCodesAndWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative(String sourceName, Date snapshot, boolean isValidated);

    BigInteger getDiseasesCodesNumberBySourceAndSnapshotNative(String sourceName, Date snapshot);

    List<String> getWikipediaMedicalVocabulariesNative();


    List<Disease> findAllQuery();

    void persist(Disease disease);

    int insertNative(String diseaseId, String name, String cui);

    int insertNativeHasDisease(String documentId, Date date, String diseaseId);

    boolean deleteById(String diseaseId);

    void delete(Disease disease);

    Disease update(Disease disease);

    int updateByIdQuery(Disease disease);
    
}
