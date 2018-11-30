package edu.upm.midas.service.jpa.helperNative;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.upm.midas.service.jpa.HasTextService;
import edu.upm.midas.service.jpa.TextService;
import edu.upm.midas.common.util.Common;
import edu.upm.midas.model.Disease;
import edu.upm.midas.model.DisnetConcept;
import edu.upm.midas.model.Paper;
import edu.upm.midas.model.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 14/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className TextHelper
 * @see
 */
@Service
public class TextHelperNative {

    @Autowired
    private TextService textService;
    @Autowired
    private HasTextService hasTextService;

    @Autowired
    private DocumentHelperNative documentHelperNative;
    @Autowired
    private UrlHelperNative urlHelperNative;


    @Autowired
    private Common common;

    private static final Logger logger = LoggerFactory.getLogger(TextHelperNative.class);
    @Autowired
    ObjectMapper objectMapper;


    public List<Paper> excelExport(String sourceName, Date version, int textCount){
        List<Paper> paperList = new ArrayList<>();
        List<Paper> papers = textService.findAllBySourceAndVersionAndTextCountNative(sourceName, version, true, textCount);
        if (papers!=null) {
            for (Paper paper : papers) {
                Paper existPaper = textService.findPaperByIdNative(paper.getPaperId());
                paper.setTitle(existPaper.getTitle());
                paper.setAuthors(existPaper.getAuthors());
                paper.setKeywords(existPaper.getKeywords());
                paper.setUrl(existPaper.getUrl());

                List<Disease> diseases = textService.findDiseaseBySourceAndVersionAndDocumentIdNative(sourceName, version, paper.getText().getDocument().getDocumentId());
                paper.setDiseases(diseases);

                List<DisnetConcept> disnetConcepts = textService.findTermsBySourceAndVersionAndDocumentAndTextIdNative(sourceName, version, paper.getText().getTextId());
                paper.getText().setDisnetConceptsCount(disnetConcepts.size());
                paper.getText().setDisnetConceptList(disnetConcepts);

                for (DisnetConcept disnetConcept : disnetConcepts) {
                    List<Text> texts = textService.findTextsBySourceAndVersionAndDocumentAndTextIdAndCuiNative(sourceName, version, paper.getText().getDocument().getDocumentId(), paper.getText().getTextId(), disnetConcept.getCui());
                    if (texts != null) {
                        disnetConcept.setTexts(texts);
                        disnetConcept.setTextsCount(texts.size());
                    }
                }

                paperList.add(paper);
            }
        }

        return paperList;
    }




}
