package edu.upm.midas.data.relational.service;

import edu.upm.midas.data.relational.entities.edsssdb.Symptom;
import edu.upm.midas.model.DisnetConcept;
import edu.upm.midas.model.SymptomWithCount;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 19/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className SymptomService
 * @see
 */
public interface SymptomService {

    Symptom findById(String cui);

    Symptom findByName(String symptomName);

    boolean findHasSemanticTypeIdNative(String cui, String semanticType);

    List<Object[]> findBySourceAndVersionNative(Date version, String source);

    List<Symptom> findAll();

    List<DisnetConcept> mostCommonBySourceAndVersionAndValidated(String sourceName, Date version, boolean isValidated, int limit);

    List<DisnetConcept> lessCommonBySourceAndVersionAndValidated(String sourceName, Date version, boolean isValidated, int limit);


    void save(Symptom symptom);

    int insertNative(String cui, String name);

    boolean updateFindFull(Symptom symptom);

    boolean updateFindPartial(Symptom symptom);

    boolean deleteById(String cui);
}
