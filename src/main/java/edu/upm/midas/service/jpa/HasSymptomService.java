package edu.upm.midas.service.jpa;

import edu.upm.midas.model.jpa.HasSymptom;
import edu.upm.midas.model.jpa.HasSymptomPK;

import java.util.List;

/**
 * Created by gerardo on 19/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className HasSymptomService
 * @see
 */
public interface HasSymptomService {

    HasSymptom findById(HasSymptomPK hasSymptomPK);

    List<HasSymptom> findAll();

    void save(HasSymptom hasSymptom);

    int insertNative(String textId, String cui, boolean validated, String matchedWords, String positionalInfo);

    boolean updateFindFull(HasSymptom hasSymptom, HasSymptomPK hasSymptomPK);

    boolean updateFindPartial(HasSymptom hasSymptom, HasSymptomPK hasSymptomPK);

    boolean deleteById(HasSymptomPK hasSymptomPK);

    int updateValidatedNative(String version, String sourceId, String cui, boolean validated);
}
