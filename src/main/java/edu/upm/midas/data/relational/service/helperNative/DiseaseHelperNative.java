package edu.upm.midas.data.relational.service.helperNative;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.upm.midas.data.relational.entities.edsssdb.Disease;
import edu.upm.midas.data.relational.service.DiseaseService;
import edu.upm.midas.data.relational.service.SymptomService;
import edu.upm.midas.model.DiseaseSymptoms;
import edu.upm.midas.model.Finding;
import edu.upm.midas.common.util.Common;
import edu.upm.midas.common.util.UniqueId;
import edu.upm.midas.model.SymptomWithCount;
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
    private SymptomService symptomService;
    @Autowired
    private UniqueId uniqueId;
    @Autowired
    private DocumentHelperNative documentHelperNative;
    @Autowired
    private Common common;

    private static final Logger logger = LoggerFactory.getLogger(DiseaseHelperNative.class);
    @Autowired
    ObjectMapper objectMapper;


    public List<Finding> getFindings(String sourceName, Date version, String diseaseName, boolean isValidated){
        List<Finding> findings = diseaseService.findSymptomsBySourceAndVersionAndDiseaseNameAndValidated(sourceName, version, diseaseName, isValidated);
        if (findings != null) {
            findings = removeRepetedFindings(findings);
        }
/*
        for (Finding finding: findings) {
            System.out.println(finding.toString());
        }
*/
        return findings;
    }


    public int getNumberDiseases(String sourceName, Date version){
        return diseaseService.numberDiseasesBySourceAndVersion(sourceName, version);
    }


    public List<edu.upm.midas.model.Disease> diseaseList(String sourceName, Date version){
        return diseaseService.findAllBySourceAndVersion(sourceName, version);
    }


    public List<DiseaseSymptoms> getDiseasesWithFewerFindings(String sourceName, Date version, boolean isValidated, int limit){
        List<DiseaseSymptoms> diseases = diseaseService.withFewerSymptomsBySourceAndVersionAndValidated(sourceName, version, isValidated, limit);
        //REGRESA LA MISMA LISTA PERO CON INFORMACIÓN DE SUS SINTOMAS... QUE CON LA PRIMER CONSULTA NO SE CONSIGUE
        List<DiseaseSymptoms> diseaseList = getDiseaseWithCountList(sourceName, version, isValidated, limit, diseases);
        return diseaseList;
    }


    public List<DiseaseSymptoms> getDiseasesWithMoreFindings(String sourceName, Date version, boolean isValidated, int limit){
        List<DiseaseSymptoms> diseases = diseaseService.withMoreSymptomsBySourceAndVersionAndValidated(sourceName, version, isValidated, limit);
        //REGRESA LA MISMA LISTA PERO CON INFORMACIÓN DE SUS SINTOMAS... QUE CON LA PRIMER CONSULTA NO SE CONSIGUE
        List<DiseaseSymptoms> diseaseList = getDiseaseWithCountList(sourceName, version, isValidated, limit, diseases);
        return diseaseList;
    }


    public List<DiseaseSymptoms> getDiseaseWithCountList(String sourceName, Date version, boolean isValidated, int limit, List<DiseaseSymptoms> diseases){
        List<DiseaseSymptoms> diseaseList = null;
        for (DiseaseSymptoms diseaseSymptoms : diseases){
            DiseaseSymptoms disease = null;
            //SE OBTIENEN LOS SINTOMAS POR CADA ENFERMEDAD ENCONTRADA
            List<Finding> findings = diseaseService.findSymptomsBySourceAndVersionAndDiseaseIdAndValidated(sourceName, version, diseaseSymptoms.getDiseaseId(), isValidated);
            if (findings != null) {
                disease.setFindingList(findings);
                disease.setDiseaseId(diseaseSymptoms.getDiseaseId());
                disease.setName(diseaseSymptoms.getName());
                disease.setCount(diseaseSymptoms.getCount());
                diseaseList.add(disease);
            }
        }
        return diseaseList;
    }


    public List<SymptomWithCount> getMostCommonSymptoms(String sourceName, Date version, boolean isValidated, int limit){
        return symptomService.mostCommonBySourceAndVersionAndValidated(sourceName, version, isValidated, limit);
    }


    public List<SymptomWithCount> getLessCommonSymptoms(String sourceName, Date version, boolean isValidated, int limit){
        return symptomService.lessCommonBySourceAndVersionAndValidated(sourceName, version, isValidated, limit);
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
