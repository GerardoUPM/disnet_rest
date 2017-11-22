package edu.upm.midas.data.relational.repository;

import edu.upm.midas.data.relational.entities.edsssdb.Disease;

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

    Object[] numberDiseasesBySourceAndVersion(String sourceName, Date version);

    List<Object[]> findAllBySourceAndVersion(String sourceName, Date version);

    List<Object[]> withFewerSymptomsBySourceAndVersionAndValidated(String sourceName, Date version, boolean isValidated, int limit);

    List<Object[]> withMoreSymptomsBySourceAndVersionAndValidated(String sourceName, Date version, boolean isValidated, int limit);

    List<Disease> findAllQuery();

    List<Object[]> findSymptomsBySourceAndVersionAndDiseaseNameAndValidated(String sourceName, Date version, String diseaseName, boolean isValidated);

    List<Object[]> findSymptomsBySourceAndVersionAndDiseaseIdAndValidated(String sourceName, Date version, String diseaseId, boolean isValidated);

    void persist(Disease disease);

    int insertNative(String diseaseId, String name, String cui);

    int insertNativeHasDisease(String documentId, Date date, String diseaseId);

    boolean deleteById(String diseaseId);

    void delete(Disease disease);

    Disease update(Disease disease);

    int updateByIdQuery(Disease disease);
    
}
