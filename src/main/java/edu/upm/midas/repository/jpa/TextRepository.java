package edu.upm.midas.repository.jpa;

import edu.upm.midas.model.jpa.Text;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 14/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className TextRepository
 * @see
 */
public interface TextRepository {

    Text findById(String textId);

    Text findByIdQuery(String textId);

    Text findByContentTypeQuery(String contentType);

    Text findByTextQuery(String text);

    Text findByIdNative(String textId);

    Text findByIdNativeResultClass(String textId);

    List<Object[]> findBySourceAndVersionNative(Date version, String source);

    List<Object[]> findAllBySourceAndVersionAndTextCountNative(String sourceName, Date version, boolean validated, int textCount);

    List<Object[]> findTermsBySourceAndVersionAndDocumentAndTextIdNative(String sourceName, Date version, String textId);

    List<Object[]> findDiseaseBySourceAndVersionAndDocumentIdNative(String sourceName, Date version, String documentId);

    List<Object[]> findTextsBySourceAndVersionAndDocumentAndTextIdAndCuiNative(String sourceName, Date version, String documentId, String textId, String cui);

    Object[] findPaperByIdNative(String paperId);

    List<Text> findAllQuery();

    void persist(Text text);

    int insertNative(String textId, String contentType, String text);

    int insertNativeUrl(String textId, String urlId);

    boolean deleteById(String textId);

    void delete(Text text);

    Text update(Text text);

    int updateByIdQuery(Text text);
    
}
