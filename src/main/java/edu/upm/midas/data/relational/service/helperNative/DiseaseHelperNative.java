package edu.upm.midas.data.relational.service.helperNative;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.upm.midas.data.relational.service.DiseaseService;
import edu.upm.midas.model.Finding;
import edu.upm.midas.model.response.diseases.SymptomsResponse;
import edu.upm.midas.common.util.Common;
import edu.upm.midas.common.util.UniqueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by gerardo on 13/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className DiseaseHelper
 * @see
 */
@Service
public class DiseaseHelperNative {

    @Autowired
    private DiseaseService diseaseService;
    @Autowired
    private UniqueId uniqueId;
    @Autowired
    private DocumentHelperNative documentHelperNative;
    @Autowired
    private Common common;

    private static final Logger logger = LoggerFactory.getLogger(DiseaseHelperNative.class);
    @Autowired
    ObjectMapper objectMapper;


    public SymptomsResponse getFindings(String sourceName, Date version, String diseaseName, boolean isValidated, SymptomsResponse symptomsResponse){

        List<Object[]> symptoms = diseaseService.findSymptomsBySourceAndVersionAndDiseaseNameAndIsValidated(sourceName, version, diseaseName, isValidated);
        List<Finding> findings = new ArrayList<>();

        symptomsResponse.setDisease( diseaseName );
        for (Object[] symptom: symptoms) {
            Finding finding = new Finding();
            finding.setCui( (String) symptom[0] );
            finding.setName( (String) symptom[1]);
            findings.add( finding );
        }
        findings = removeRepetedFindings( findings );
        symptomsResponse.setFindings( findings );
        symptomsResponse.setSize( findings.size() );
/*
        for (Finding finding: findings) {
            System.out.println(finding.toString());
        }
*/

        return symptomsResponse;
    }


    /**
     * @param elements
     * @return
     */
    public List<Finding> removeRepetedFindings(List<Finding> elements){
        List<Finding> resList = elements;
        Set<Finding> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.addAll(elements);
        elements.clear();
        elements.addAll(linkedHashSet);
        return resList;
    }





}
