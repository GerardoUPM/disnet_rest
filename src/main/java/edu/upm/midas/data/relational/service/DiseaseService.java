package edu.upm.midas.data.relational.service;

import edu.upm.midas.data.relational.entities.edsssdb.Disease;
import edu.upm.midas.model.DiseaseSymptoms;
import edu.upm.midas.model.Finding;

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

    List<Disease> findAll();

    BigInteger numberDiseasesBySourceAndVersion(String sourceName, Date version);

    List<edu.upm.midas.model.Disease> findAllBySourceAndVersion(String sourceName, Date version);

    List<DiseaseSymptoms> withFewerSymptomsBySourceAndVersionAndValidated(String sourceName, Date version, boolean isValidated, int limit);

    List<DiseaseSymptoms> withMoreSymptomsBySourceAndVersionAndValidated(String sourceName, Date version, boolean isValidated, int limit);

    List<Finding> findSymptomsBySourceAndVersionAndDiseaseNameAndValidated(String sourceName, Date version, String diseaseName, boolean isValidated);

    List<Finding> findSymptomsBySourceAndVersionAndDiseaseIdAndValidated(String sourceName, Date version, String diseaseId, boolean isValidated);

    void save(Disease disease);

    int insertNative(String diseaseId, String name, String cui);

    int insertNativeHasDisease(String documentId, Date date, String diseaseId);

    boolean updateFindFull(Disease disease);

    boolean updateFindPartial(Disease disease);

    boolean deleteById(String diseaseId);
    
}
