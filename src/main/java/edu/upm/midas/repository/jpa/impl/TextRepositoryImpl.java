package edu.upm.midas.repository.jpa.impl;

import edu.upm.midas.model.jpa.Text;
import edu.upm.midas.repository.jpa.AbstractDao;
import edu.upm.midas.repository.jpa.TextRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 14/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className TextRepositoryImpl
 * @see
 */
@Repository("TextRepositoryDao")
public class TextRepositoryImpl extends AbstractDao<String, Text>
                                implements TextRepository {

    public Text findById(String textId) {
        Text text = getByKey(textId);
        return text;
    }

    @SuppressWarnings("unchecked")
    public Text findByIdQuery(String textId) {
        Text text = null;
        List<Text> textList = (List<Text>) getEntityManager()
                .createNamedQuery("Text.findById")
                .setParameter("textId", textId)
                .getResultList();
        if (CollectionUtils.isNotEmpty(textList))
            text = textList.get(0);
        return text;
    }

    @SuppressWarnings("unchecked")
    public Text findByContentTypeQuery(String contentType) {
        Text text = null;
        List<Text> textList = (List<Text>) getEntityManager()
                .createNamedQuery("Text.findByContentType")
                .setParameter("contentType", contentType)
                .getResultList();
        if (CollectionUtils.isNotEmpty(textList))
            text = textList.get(0);
        return text;
    }

    @SuppressWarnings("unchecked")
    public Text findByTextQuery(String text) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Text findByIdNative(String textId) {
        Text text = null;
        List<Text> textList = (List<Text>) getEntityManager()
                .createNamedQuery("Text.findByIdNative")
                .setParameter("textId", textId)
                .getResultList();
        if (CollectionUtils.isNotEmpty(textList))
            text = textList.get(0);
        return text;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Text findByIdNativeResultClass(String textId) {
        Text text = null;
        List<Text> textList = (List<Text>) getEntityManager()
                .createNamedQuery("Text.findByIdNativeResultClass")
                .setParameter("textId", textId)
                .getResultList();
        if (CollectionUtils.isNotEmpty(textList))
            text = textList.get(0);
        return text;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findBySourceAndVersionNative(Date version, String source) {
        List<Object[]> texts = null;
        List<Object[]> textList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Text.findBySourceAndVersionNative")
                .setParameter("version", version)
                .setParameter("source", source)
                .getResultList();
        if (CollectionUtils.isNotEmpty(textList))
            texts = textList;
        return texts;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findAllBySourceAndVersionAndTextCountNative(String sourceName, Date version, boolean validated, int textCount) {
        List<Object[]> texts = null;
        List<Object[]> textList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Text.findAllBySourceAndVersionAndTextCountNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("validated", validated)
                .setMaxResults(textCount)
                .getResultList();
        if (CollectionUtils.isNotEmpty(textList))
            texts = textList;
        return texts;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findTermsBySourceAndVersionAndDocumentAndTextIdNative(String sourceName, Date version, String textId) {
        List<Object[]> terms = null;
        List<Object[]> termList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Text.findTermsBySourceAndVersionAndDocumentAndTextIdNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("textId", textId)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(termList))
            terms = termList;
        return terms;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findDiseaseBySourceAndVersionAndDocumentIdNative(String sourceName, Date version, String documentId) {
        List<Object[]> diseases = null;
        List<Object[]> diseaseList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Text.findDiseaseBySourceAndVersionAndDocumentIdNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("documentId", documentId)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(diseaseList))
            diseases = diseaseList;
        return diseases;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findTextsBySourceAndVersionAndDocumentAndTextIdAndCuiNative(String sourceName, Date version, String documentId, String textId, String cui) {
        List<Object[]> texts = null;
        List<Object[]> textList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Text.findTextsBySourceAndVersionAndDocumentAndTextIdAndCuiNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .setParameter("documentId", documentId)
                .setParameter("textId", textId)
                .setParameter("cui", cui)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(textList))
            texts = textList;
        return texts;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object[] findPaperByIdNative(String paperId) {
        Object[] paper = null;
        List<Object[]> paperList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Text.findPaperByIdNative")
                .setParameter("paperId", paperId)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(paperList))
            paper = paperList.get(0);
        return paper;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Text> findAllQuery() {
        return (List<Text>) getEntityManager()
                .createNamedQuery("Text.findAll")
                .setMaxResults(0)
                .getResultList();
    }


    public void persist(Text text) {
        super.persist(text);
    }

    @Override
    public int insertNative(String textId, String contentType, String text) {
        return getEntityManager()
                .createNamedQuery("Text.insertNative")
                .setParameter("textId", textId)
                .setParameter("contentType", contentType)
                .setParameter("text", text)
                .executeUpdate();
    }

    @Override
    public int insertNativeUrl(String textId, String urlId) {
        return getEntityManager()
                .createNamedQuery("TextUrl.insertNative")
                .setParameter("textId", textId)
                .setParameter("urlId", urlId)
                .executeUpdate();
    }

    public boolean deleteById(String textId) {
        Text text = findById( textId );
        if(text ==null)
            return false;
        super.delete(text);
        return true;
    }

    public void delete(Text text) {
        super.delete(text);
    }

    public Text update(Text text) {
        return super.update(text);
    }

    public int updateByIdQuery(Text text) {
        return getEntityManager()
                .createNamedQuery("Text.updateById")
                .setParameter("textId", text.getTextId())
                .setParameter("contentType", text.getContentType())
                .setParameter("text", text.getText())
                .executeUpdate();
    }
}
