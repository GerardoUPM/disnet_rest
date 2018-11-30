package edu.upm.midas.service.jpa;

import edu.upm.midas.model.jpa.Text;
import edu.upm.midas.model.Disease;
import edu.upm.midas.model.DisnetConcept;
import edu.upm.midas.model.Paper;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 14/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className TextService
 * @see
 */
public interface TextService {

    Text findById(String textId);

    Text findByContentTypeQuery(String contentType);

    Text findByTextQuery(String text);

    List<Paper> findAllBySourceAndVersionAndTextCountNative(String sourceName, Date version, boolean validated, int symptoms);

    List<DisnetConcept> findTermsBySourceAndVersionAndDocumentAndTextIdNative(String sourceName, Date version, String textId);

    List<Disease> findDiseaseBySourceAndVersionAndDocumentIdNative(String sourceName, Date version, String documentId);

    Paper findPaperByIdNative(String paperId);

    List<edu.upm.midas.model.Text> findTextsBySourceAndVersionAndDocumentAndTextIdAndCuiNative(String sourceName, Date version, String documentId, String textId, String cui);

    List<Text> findAll();

    List<Object[]> findBySourceAndVersionNative(Date version, String source);

    void save(Text text);

    int insertNative(String textId, String contentType, String text);

    int insertNativeUrl(String textId, String urlId);

    boolean updateFindFull(Text text);

    boolean updateFindPartial(Text text);

    boolean deleteById(String textId);

}
