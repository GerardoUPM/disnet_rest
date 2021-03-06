package edu.upm.midas.service.jpa;

import edu.upm.midas.model.jpa.HasText;
import edu.upm.midas.model.jpa.HasTextPK;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 14/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className HasTextService
 * @see
 */
public interface HasTextService {

    HasText findById(HasTextPK hasTextPK);

    HasText findByTextOrderQuery(int textOrder);

    List<HasText> findAll();

    void save(HasText hasText);

    int insertNative(String documentId, Date date, String sectionId, String textId, int textOrder);

    boolean updateFindFull(HasText hasText, HasTextPK hasTextPK);

    boolean updateFindPartial(HasText hasText, HasTextPK hasTextPK);

    boolean deleteById(HasTextPK hasTextPK);
    
}
