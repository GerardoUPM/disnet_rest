package edu.upm.midas.data.relational.repository;

import edu.upm.midas.data.relational.entities.edsssdb.Disease;

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

    Disease findById(String diseaseId);

    Disease findByIdQuery(String diseaseId);

    Disease findByNameQuery(String diseaseName);

    Disease findByCuiQuery(String cui);

    Disease findLastDiseaseQuery();

    Disease findByIdNative(String diseaseId);

    Disease findByIdNativeResultClass(String diseaseId);

    BigInteger numberDiseasesBySourceAndVersion(String sourceName, Date version);

    List<Object[]> findAllBySourceAndVersion(String sourceName, Date version);

    List<Object[]> findAllWithUrlAndSymptomsCountBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated);

    List<Object[]> withFewerSymptomsBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated, int limit);

    List<Object[]> withMoreSymptomsBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated, int limit);

    List<Object[]> findSymptomsBySourceAndVersionAndDiseaseNameAndIsValidated(String sourceName, Date version, String diseaseName, boolean isValidated);

    List<Object[]> findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidated(String sourceName, Date version, String diseaseId, boolean isValidated);

    List<Object[]> findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndValidatedNative(String sourceName, Date version, String code, String resource, boolean isValidated);

    Object[] findByExactNameAndSourceAndVersionNative(String sourceName, Date version, String diseaseName);

    List<Object[]> findByLikeNameAndSourceAndVersionNative(String sourceName, Date version, String diseaseName);

    List<Disease> findAllQuery();

    void persist(Disease disease);

    int insertNative(String diseaseId, String name, String cui);

    int insertNativeHasDisease(String documentId, Date date, String diseaseId);

    boolean deleteById(String diseaseId);

    void delete(Disease disease);

    Disease update(Disease disease);

    int updateByIdQuery(Disease disease);
    
}
