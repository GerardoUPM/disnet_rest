package edu.upm.midas.data.relational.service.impl;
import edu.upm.midas.data.relational.entities.edsssdb.Text;
import edu.upm.midas.data.relational.repository.TextRepository;
import edu.upm.midas.data.relational.service.TextService;
import edu.upm.midas.model.Disease;
import edu.upm.midas.model.DisnetConcept;
import edu.upm.midas.model.Document;
import edu.upm.midas.model.Paper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 14/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className TextServiceImpl
 * @see
 */
@Service("textService")
public class TextServiceImpl implements TextService {

    @Autowired
    private TextRepository daoText;

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Text findById(String textId) {
        Text text = daoText.findById(textId);
        //if(source!=null)
        //Hibernate.initialize(source.getDiseasesBySidsource());
        return text;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Text findByContentTypeQuery(String contentType) {
        Text txt = daoText.findByContentTypeQuery(contentType);
/*
        if(source!=null)
            Hibernate.initialize(source.getVersionList());
*/
        return txt;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Text findByTextQuery(String text) {
        Text txt = daoText.findByTextQuery(text);
/*
        if(source!=null)
            Hibernate.initialize(source.getVersionList());
*/
        return txt;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Paper> findAllBySourceAndVersionAndTextCountNative(String sourceName, Date version, boolean validated, int textCount) {
        List<Paper> paperList = null;
        List<Object[]> papers = daoText.findAllBySourceAndVersionAndTextCountNative(sourceName, version, validated, textCount);
        if(papers!=null){
            paperList = new ArrayList<>();
            for (Object[] paperObject: papers) {
                Paper paper = new Paper((String) paperObject[5]);

                Document document = new Document((String) paperObject[4], version);
                edu.upm.midas.model.Text text = new edu.upm.midas.model.Text();
                text.setTextId((String) paperObject[0]);
                text.setSection((String) paperObject[1]);
                text.setText((String) paperObject[3]);
                text.setTextOrder((Integer) paperObject[2]);
                try {
                    text.setDisnetConceptsCount((Integer) paperObject[6]);
                } catch (Exception e){
                    BigInteger count = (BigInteger) paperObject[6];
                    text.setDisnetConceptsCount(count.intValue());
                }
                text.setDocument(document);
                paper.setText(text);
                paperList.add(paper);
            }
        }
        return paperList;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<DisnetConcept> findTermsBySourceAndVersionAndDocumentAndTextIdNative(String sourceName, Date version, String textId) {
        List<Object[]> terms = daoText.findTermsBySourceAndVersionAndDocumentAndTextIdNative(sourceName, version, textId);
        return createDisnetConceptList(terms, true);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Disease> findDiseaseBySourceAndVersionAndDocumentIdNative(String sourceName, Date version, String documentId) {
        List<Object[]> diseases = daoText.findDiseaseBySourceAndVersionAndDocumentIdNative(sourceName, version, documentId);
        return createDiseaseList(diseases, version);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Paper findPaperByIdNative(String paperId) {
        Object[] paper = daoText.findPaperByIdNative(paperId);
        return createPaper(paper);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<edu.upm.midas.model.Text> findTextsBySourceAndVersionAndDocumentAndTextIdAndCuiNative(String sourceName, Date version, String documentId, String textId, String cui) {
        List<Object[]> texts = daoText.findTextsBySourceAndVersionAndDocumentAndTextIdAndCuiNative(sourceName, version, documentId, textId, cui);
        return createTextList(texts, true);
    }

    public List<edu.upm.midas.model.Text> createTextList(List<Object[]> texts, boolean positionalInfo){
        List<edu.upm.midas.model.Text> textList = null;
        if (texts != null) {
            textList = new ArrayList<>();
            for (Object[] txt : texts) {
                edu.upm.midas.model.Text text = new edu.upm.midas.model.Text();
                text.setSection((String) txt[0]);
                text.setTextId((String) txt[2]);
                text.setText((String) txt[3]);
                try {
                    text.setTextOrder((Integer) txt[1]);
                } catch (Exception e){
                    BigInteger count = (BigInteger) txt[1];
                    text.setTextOrder(count.intValue());
                }
                if (positionalInfo){
                    text.setMatchedWords((String) txt[4]);
                    text.setPositionalInfo((String) txt[5]);
                }
                textList.add(text);
            }
        }
        return textList;
    }

    public Paper createPaper(Object[] paperObject){
        Paper paper = null;
        if(paperObject!=null){
            paper = new Paper();
            paper.setTitle((String) paperObject[0]);
            paper.setAuthors((String) paperObject[1]);
            paper.setKeywords((String) paperObject[2]);
            paper.setUrl((String) paperObject[3]);
        }
        return paper;
    }

    public List<DisnetConcept> createDisnetConceptList(List<Object[]> symptoms, boolean setValidatedParam){
        List<DisnetConcept> disnetConcepts = new ArrayList<>();
        if (symptoms != null) {
            for (Object[] symptom : symptoms) {
                DisnetConcept disnetConcept = new DisnetConcept();
                disnetConcept.setCui((String) symptom[0]);
                disnetConcept.setName((String) symptom[1]);
                if (setValidatedParam){
                    disnetConcept.setValidated((boolean) symptom[2]);
                    disnetConcept.setSemanticTypes(setSemanticTypes((String) symptom[3]));
                }else disnetConcept.setSemanticTypes(setSemanticTypes((String) symptom[5]));

                disnetConcepts.add(disnetConcept);
            }
        }
        return disnetConcepts;
    }

    public List<Disease> createDiseaseList(List<Object[]> diseases, Date version){
        List<Disease> diseaseList = null;
        if (diseases != null) {
            diseaseList = new ArrayList<>();
            for (Object[] dis : diseases) {
                Disease disease = new Disease();
                disease.setDiseaseId((String) dis[0]);
                disease.setName((String) dis[1]);
                disease.setDisnetConceptsCount((Integer) dis[2]);
                diseaseList.add(disease);
            }
        }
        return diseaseList;
    }

    public List<String> setSemanticTypes(String semanticTypes){
        List<String> semanticTypesList = new ArrayList<>();
        String[] parts = semanticTypes.split(",");
        for (String semanticType: parts) {
            semanticTypesList.add(semanticType);
        }
        return semanticTypesList;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Text> findAll() {
        List<Text> listTEXTEntities = daoText.findAllQuery();
        return listTEXTEntities;
    }

    @Override
    public List<Object[]> findBySourceAndVersionNative(Date version, String source) {
        return daoText.findBySourceAndVersionNative(version, source);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public void save(Text text) {
        daoText.persist(text);
    }

    @Override
    public int insertNative(String textId, String contentType, String text) {
        return daoText.insertNative( textId, contentType, text );
    }

    @Override
    public int insertNativeUrl(String textId, String urlId) {
        return daoText.insertNativeUrl( textId, urlId );
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindFull(Text text) {
        Text txt = daoText.findById(text.getTextId());
        if(txt!=null){
            if(StringUtils.isNotBlank(text.getTextId()))
                txt.setTextId(text.getTextId());
            if(StringUtils.isNotBlank(text.getText()))
                txt.setText(text.getText());
            //if(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource()))
            //    sour.setDiseasesBySidsource(source.getDiseasesBySidsource());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindPartial(Text text) {
        Text txt = daoText.findById(text.getTextId());
        if(txt!=null){
            if(StringUtils.isNotBlank(text.getTextId()))
                txt.setTextId(text.getTextId());
            if(StringUtils.isNotBlank(text.getText()))
                txt.setText(text.getText());
            //if(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource()))
            //    sour.setDiseasesBySidsource(source.getDiseasesBySidsource());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean deleteById(String textId) {
        Text text = daoText.findById(textId);
        if(text !=null)
            daoText.delete(text);
        else
            return false;
        return true;
    }
}
