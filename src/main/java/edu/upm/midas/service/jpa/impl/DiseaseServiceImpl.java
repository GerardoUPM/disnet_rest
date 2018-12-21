package edu.upm.midas.service.jpa.impl;
import edu.upm.midas.constants.Constants;
import edu.upm.midas.model.jpa.Disease;
import edu.upm.midas.repository.jpa.DiseaseRepository;
import edu.upm.midas.service.jpa.DiseaseService;
import edu.upm.midas.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 12/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className DiseaseServiceImpl
 * @see
 */
@Service("diseaseService")
public class DiseaseServiceImpl implements DiseaseService {

    @Autowired
    private DiseaseRepository daoDisease;


    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Disease findById(String diseaseId) {
        Disease disease = daoDisease.findById(diseaseId);
        //if(source!=null)
        //Hibernate.initialize(source.getDiseasesBySidsource());
        return disease;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Disease findByName(String diseaseName) {
        Disease disease = daoDisease.findByNameQuery(diseaseName);
/*
        if(source!=null)
            Hibernate.initialize(source.getVersionList());
*/
        return disease;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Disease findByCui(String cui) {
        Disease disease = daoDisease.findByCuiQuery(cui);
/*
        if(source!=null)
            Hibernate.initialize(source.getVersionList());
*/
        return disease;
    }

    @Override
    public Disease findLastDiseaseQuery() {
        return daoDisease.findLastDiseaseQuery();
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Disease> findAll() {
        List<Disease> listDiseaseEntities = daoDisease.findAllQuery();
        return listDiseaseEntities;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Integer numberDiseasesBySourceAndVersion(String sourceName, Date version) {
        Integer count = 0;
        BigInteger disease = daoDisease.numberDiseasesBySourceAndVersion(sourceName, version);
        if (disease != null) {
            count = disease.intValue();
        }
        return count;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<edu.upm.midas.model.Disease> findAllBySourceAndVersion(String sourceName, Date version) {
        List<Object[]> diseases = daoDisease.findAllBySourceAndVersion(sourceName, version);
        return createDiseaseList(diseases);
    }

    @Override
    public List<Code> findCodesBySourceAndVersionAndDiseaseIdNative(String sourceName, Date version, String diseaseId) {
        List<Code> codeList = null;
        List<Object[]> codes = daoDisease.findCodesBySourceAndVersionAndDiseaseIdNative(sourceName, version, diseaseId);
        if (codes != null) {
            codeList = new ArrayList<>();
            for (Object[] code : codes) {
                Code cod = new Code();
                cod.setCode((String) code[0]);
                cod.setTypeCode((String) code[1]);
                codeList.add(cod);
            }
        }
        return codeList;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<edu.upm.midas.model.Disease> findAllWithUrlAndSymptomsCountBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated) {
        List<edu.upm.midas.model.Disease> diseaseList = null;
        List<Object[]> diseases = daoDisease.findAllWithUrlAndSymptomsCountBySourceAndVersionAndIsValidated(sourceName, version, isValidated);
        if (diseases != null) {
            diseaseList = new ArrayList<>();
            for (Object[] dis : diseases) {
                edu.upm.midas.model.Disease disease = new edu.upm.midas.model.Disease();
                disease.setDiseaseId((String) dis[0]);
                disease.setName((String) dis[1]);
                disease.setUrl((String) dis[2]);
                BigInteger count = (BigInteger) dis[3];
                disease.setDisnetConceptsCount(count.intValue());
                diseaseList.add(disease);
            }
        }
        return diseaseList;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<DiseaseDisnetConcepts> withFewerSymptomsBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated, int limit) {
        List<Object[]> diseases = daoDisease.withFewerSymptomsBySourceAndVersionAndIsValidated(sourceName, version, isValidated, limit);
        return getDiseaseSymptomsList(diseases);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<edu.upm.midas.model.Disease> withMoreOrFewerSymptomsBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated, int limit, boolean moreSymptoms) {
        List<Object[]> diseases = daoDisease.withMoreOrFewerSymptomsBySourceAndVersionAndIsValidated(sourceName, version, isValidated, limit, moreSymptoms);
        return createDiseaseList(diseases);
    }

    public List<DiseaseDisnetConcepts> getDiseaseSymptomsList(List<Object[]> diseases){
        List<DiseaseDisnetConcepts> diseaseList = null;
        if (diseases != null) {
            diseaseList = new ArrayList<>();
            for (Object[] dis : diseases) {
                DiseaseDisnetConcepts disease = new DiseaseDisnetConcepts();
                disease.setDiseaseId((String) dis[0]);
                disease.setName((String) dis[1]);
                BigInteger count = (BigInteger) dis[2];//System.out.println(count+" - " +count + " - " + count.intValue() + " - "+count.intValueExact());
                disease.setDisnetConceptCount(count.intValue());
                diseaseList.add(disease);
            }
        }
        return diseaseList;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<DisnetConcept> findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidated(String sourceName, Date version, String diseaseId, boolean isValidated) {
        List<Object[]> symptoms = daoDisease.findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidated(sourceName, version, diseaseId, isValidated);
        //System.out.println(sourceName+" - "+version+" - "+diseaseId +" - "+isValidated);
        return createDisnetConceptList(symptoms, false);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<DisnetConcept> findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidatedAndForceOrExludeSemanticTypes(String sourceName, Date version, String diseaseId, boolean isValidated, boolean forceSemanticTypes, List<String> semanticTypes) {
        List<Object[]> symptoms = daoDisease.findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidatedAndForceOrExludeSemanticTypes(sourceName, version, diseaseId, isValidated, forceSemanticTypes, semanticTypes);
        //System.out.println(sourceName+" - "+version+" - "+diseaseId +" - "+isValidated);
        return createDisnetConceptList(symptoms, false);
    }

    public List<DisnetConcept> createDisnetConceptList(List<Object[]> symptoms, boolean setValidatedParam){
        List<DisnetConcept> disnetConcepts = new ArrayList<>();
        if (symptoms != null) {
            //disnetConcepts = new ArrayList<>();
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

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public boolean existDiseaseBySourceAndVersionAndMatchExactNameTrueNative(String sourceName, Date version, String diseaseName) {
        Object[] disease = daoDisease.findBySourceAndVersionAndMatchExactNameTrueNative(sourceName, version, diseaseName);
        if (disease != null)
            if (disease.length > 0)
                return true;
            else return false;
        else return false;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public boolean existDiseaseBySourceAndVersionAndMatchExactNameFalseNative(String sourceName, Date version, String diseaseName) {
        List<Object[]> diseases = daoDisease.findBySourceAndVersionAndMatchExactNameFalseNative(sourceName, version, diseaseName);
        if (diseases != null)
            if (diseases.size() > 0)
                return true;
            else return false;
        else return false;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<edu.upm.midas.model.Disease> findBySourceAndVersionAndMatchExactNameFalseNative(String sourceName, Date version, String diseaseName) {
        List<Object[]> diseases = daoDisease.findBySourceAndVersionAndMatchExactNameFalseNative(sourceName, version, diseaseName);
        return createDiseaseList(diseases);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<edu.upm.midas.model.Disease> findBySourceAndVersionAndMatchExactNameTrueNative(String sourceName, Date version, String diseaseName) {
        List<edu.upm.midas.model.Disease> diseaseList = null;
        Object[] disease = daoDisease.findBySourceAndVersionAndMatchExactNameTrueNative(sourceName, version, diseaseName);
        if (disease != null) {
            diseaseList = new ArrayList<>();
            diseaseList.add(createDisease(disease));
        }
        return diseaseList;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<edu.upm.midas.model.Disease> findBySourceAndVersionAndCodeAndTypeCodeNative(String sourceName, Date version, String code, String typeCode) {
        List<Object[]> diseases = daoDisease.findBySourceAndVersionAndCodeAndTypeCodeNative(sourceName, version, code, typeCode);
        return createDiseaseList(diseases);
    }

    public List<edu.upm.midas.model.Disease> createDiseaseList(List<Object[]> diseases){
        List<edu.upm.midas.model.Disease> diseaseList = null;
        if (diseases != null) {
            diseaseList = new ArrayList<>();
            for (Object[] dis : diseases) {
                edu.upm.midas.model.Disease disease = new edu.upm.midas.model.Disease();
                disease.setDiseaseId((String) dis[0]);
                disease.setName((String) dis[1]);
                disease.setUrl((String) dis[3]);
                try {
                    disease.setDisnetConceptsCount((Integer) dis[4]);
                } catch (Exception e){
                    BigInteger count = (BigInteger) dis[4];
                    disease.setDisnetConceptsCount(count.intValue());
                }
                diseaseList.add(disease);
            }
        }
        return diseaseList;
    }

    public edu.upm.midas.model.Disease createDisease(Object[] dis){
        edu.upm.midas.model.Disease disease = new edu.upm.midas.model.Disease();
        disease.setDiseaseId((String) dis[0]);
        disease.setName((String) dis[1]);
        disease.setUrl((String) dis[3]);
        disease.setDisnetConceptsCount((Integer) dis[4]);
        return disease;
    }


    public List<edu.upm.midas.model.Disease> createDiseaseWithDocInfoList(List<Object[]> diseases, Date version){
        List<edu.upm.midas.model.Disease> diseaseList = null;
        if (diseases != null) {
            diseaseList = new ArrayList<>();
            for (Object[] dis : diseases) {//System.out.println("RES: "+(String) dis[4]);
                edu.upm.midas.model.Disease disease = new edu.upm.midas.model.Disease();
                disease.setDiseaseId((String) dis[1]);
                disease.setName((String) dis[0]);
                disease.setDisnetConceptsCount((Integer) dis[3]);
                disease.setDocumentList( getDocument((String) dis[4], version) );//Obtiene el documento del
                diseaseList.add(disease);
            }
        }
        return diseaseList;
    }


    public List<Document> getDocument(String documentInfo, Date version){
        List<Document> documentList = new ArrayList<>();
        String[] info = documentInfo.split(";");
        if (info != null){
            for (String doc: info) {
                String[] docment = doc.split("-");
                Document document = new Document();
                document.setDocumentId(docment[0].trim());
                document.setVersion(version);
                document.setUrl(docment[1].trim());
                documentList.add(document);
            }
        }
        return documentList;
    }


    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<edu.upm.midas.model.Disease> findCodesBySourceAndVersionAndDiseaseNameNative(String sourceName, Date version, String diseaseName, int limit) {
        List<edu.upm.midas.model.Disease> diseaseList = null;
        List<Object[]> symptoms = daoDisease.findCodesBySourceAndVersionAndDiseaseNameNative( sourceName, version, diseaseName, limit );
        String diseaseId = "";
        List<Code> codes = new ArrayList<>();
        if (symptoms != null) {
            diseaseList = new ArrayList<>();
            for (Object[] symptom : symptoms) {
                if (!diseaseId.equals((String) symptom[0])){
                    edu.upm.midas.model.Disease disease = new edu.upm.midas.model.Disease();
                    diseaseId = (String) symptom[0];
                    disease.setName((String) symptom[1]);
                    disease.setUrl((String) symptom[2]);
                    //System.out.println("size for: "+concepts.size());
                    if (codes.size() > 0 ) {
                        codes = new ArrayList<>();//concepts.clear();
                    }
                    disease.setCodes(codes);
                    diseaseList.add(disease);
                }
                Code code = new Code();
                code.setCode((String) symptom[3]);
                code.setTypeCode((String) symptom[4]);
                codes.add(code);
                //disnetConcepts.add(DisnetConcept);
            }
        }
        return diseaseList;


    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndDiseaseNameAndValidatedAndForceSemanticTypesNative(String sourceName, Date version, String diseaseName, boolean isValidated, List<String> semanticTypes) {
        List<Object[]> symptoms = daoDisease.findSymptomsBySourceAndVersionAndDiseaseNameAndValidatedAndForceSemanticTypesNative( sourceName, version, diseaseName, isValidated, semanticTypes );
        //System.out.println(sourceName+" | "+version+" | "+diseaseName+" | "+isValidated+" | "+symptoms.size() );
        return createDiseasesWithDisnetConcept(symptoms);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndDiseaseNameAndValidatedAndExcludeSemanticTypesNative(String sourceName, Date version, String diseaseName, boolean isValidated, List<String> semanticTypes) {
        List<Object[]> symptoms = daoDisease.findSymptomsBySourceAndVersionAndDiseaseNameAndValidatedAndExcludeSemanticTypesNative( sourceName, version, diseaseName, isValidated, semanticTypes );
        //System.out.println(sourceName+" | "+version+" | "+diseaseName+" | "+isValidated+" | "+symptoms.size() );
        return createDiseasesWithDisnetConcept(symptoms);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndValidatedAndForceSemanticTypesNative(String sourceName, Date version, String code, String typeCode, boolean isValidated, List<String> semanticTypes) {
        List<Object[]> symptoms = daoDisease.findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndValidatedAndForceSemanticTypesNative( sourceName, version, code, typeCode, isValidated, semanticTypes );
        //System.out.println(sourceName+" | "+version+" | "+diseaseName+" | "+isValidated+" | "+symptoms.size() );
        return createDiseasesWithDisnetConcept(symptoms);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndValidatedAndExcludeSemanticTypesNative(String sourceName, Date version, String code, String typeCode, boolean isValidated, List<String> semanticTypes) {
        List<Object[]> symptoms = daoDisease.findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndValidatedAndExcludeSemanticTypesNative( sourceName, version, code, typeCode, isValidated, semanticTypes );
        //System.out.println(sourceName+" | "+version+" | "+diseaseName+" | "+isValidated+" | "+symptoms.size() );
        return createDiseasesWithDisnetConcept(symptoms);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<edu.upm.midas.model.Disease> findAllBySourceAndVersionAndSymptomsCountNative(String sourceName, Date version, int symptoms) {
        List<Object[]> diseases = daoDisease.findAllBySourceAndVersionAndSymptomsCountNative( sourceName, version, symptoms );
        //System.out.println(sourceName+" | "+version+" | "+diseaseName+" | "+isValidated+" | "+symptoms.size() );
        return createDiseaseWithDocInfoList(diseases, version);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<DisnetConcept> findTermsBySourceAndVersionAndDocumentAndDiseaseIdNative(String sourceName, Date version, String documentId, String diseaseId) {
        List<Object[]> terms = daoDisease.findTermsBySourceAndVersionAndDocumentAndDiseaseIdNative(sourceName, version, documentId, diseaseId);
        //System.out.println(sourceName+" - "+version+" - "+diseaseId +" - "+isValidated);
        return createDisnetConceptList(terms, true);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Text> findTextsBySourceAndVersionAndDocumentAndDiseaseIdAndCuiNative(String sourceName, Date version, String documentId, String diseaseId, String cui) {
        List<Object[]> texts = daoDisease.findTextsBySourceAndVersionAndDocumentAndDiseaseIdAndCuiNative(sourceName, version, documentId, diseaseId, cui);
        //System.out.println(sourceName+" - "+version+" - "+diseaseId +" - "+isValidated);
        return createTextList(texts, true);
    }

    @Override
    public List<Text> findTextsBySourceAndVersionAndDocumentAndDiseaseIdNative(String sourceName, Date version, String documentId, String diseaseId) {
        List<Object[]> texts = daoDisease.findTextsBySourceAndVersionAndDocumentAndDiseaseIdNative(sourceName, version, documentId, diseaseId);
        //System.out.println(sourceName+" - "+version+" - "+diseaseId +" - "+isValidated);
        return createTextList(texts, false);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public String findDocumentIdBySourceAndVersionAndDiseaseIdNative(String sourceName, Date version, String diseaseId) {
        return daoDisease.findDocumentIdBySourceAndVersionAndDiseaseIdNative(sourceName, version, diseaseId);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public DetectionInformation findDetectionInformationBySourceAndVersionAndDocumentIdAndDiseaseIdAndCuiAndValidatedToDisnetConceptNative(String sourceName, Date version, String documentId, String diseaseId, String cui, boolean isValidated) {
        List<Object[]> detectionInformationList =daoDisease.findDetectionInformationBySourceAndVersionAndDocumentIdAndDiseaseIdAndCuiAndValidatedNative(sourceName,version, documentId, diseaseId, cui, isValidated);
        return createDetectionInformation(detectionInformationList);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<String> findPaperUrlsBySourceAndVersionAndDiseaseIdNative(String sourceName, Date version, String diseaseId) {
        List<String> urls = new ArrayList<>();
        List<Object[]> paperUrls = daoDisease.findPaperUrlsBySourceAndVersionAndDiseaseIdNative(sourceName, version, diseaseId);
        if (paperUrls!=null){
            for (Object[] paperUrl: paperUrls) {
                urls.add((String) paperUrl[1]);
            }
        }
        return urls;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public int getExtractedDiseasesTotBySourceAndSnapshotNative(String sourceName, Date snapshot) {
        return daoDisease.getExtractedDiseasesTotBySourceAndSnapshotNative(sourceName, snapshot).intValue();
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public int getDiseasesNumberWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative(String sourceName, Date snapshot, boolean isValidated) {
        return daoDisease.getDiseasesNumberWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative(sourceName, snapshot, isValidated).intValue();
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public int getDiseasesNumberWithALeastOneValidatedMedicalTermsBySourceNative(String sourceName, boolean isValidated) {
        return daoDisease.getDiseasesNumberWithALeastOneValidatedMedicalTermsBySourceNative(sourceName, isValidated).intValue();
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public int getTotalValidatedMedicalTermsNumberBySourceAndSnapshotNative(String sourceName, Date snapshot) {
        return daoDisease.getTotalValidatedMedicalTermsNumberBySourceAndSnapshotNative(sourceName, snapshot).intValue();
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public int getValidatedMedicalTermsNumberBySourceAndSnapshotNative(String sourceName, Date snapshot, boolean isValidated) {
        return daoDisease.getValidatedMedicalTermsNumberBySourceAndSnapshotNative(sourceName, snapshot, isValidated).intValue();
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public int getTotalTextsBySourceAndSnapshotNative(String sourceName, Date snapshot) {
        return daoDisease.getTotalTextsBySourceAndSnapshotNative(sourceName, snapshot).intValue();
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public int getTextsNumberWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative(String sourceName, Date snapshot, boolean isValidated) {
        return daoDisease.getTextsNumberWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative(sourceName, snapshot, isValidated).intValue();
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public int getDiseasesNumberWithCodesAndWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative(String sourceName, Date snapshot, boolean isValidated) {
        return daoDisease.getDiseasesNumberWithCodesAndWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative(sourceName, snapshot, isValidated).intValue();
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public int getDiseasesCodesNumberBySourceAndSnapshotNative(String sourceName, Date snapshot) {
        return daoDisease.getDiseasesCodesNumberBySourceAndSnapshotNative(sourceName, snapshot).intValue();
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<String> getWikipediaMedicalVocabulariesNative() {
        List<String> medicalVocabularies = daoDisease.getWikipediaMedicalVocabulariesNative();
        if (medicalVocabularies != null)
            return medicalVocabularies;
        else
            return new ArrayList<>();
    }


    public DetectionInformation createDetectionInformation(List<Object[]> detectionInformationList){
        DetectionInformation detectionInformation = null;
        Integer timesFoundInTexts = 0;
        for (Object[] detectInfo: detectionInformationList) {
            String matchedWords = (String) detectInfo[1];
            String[] matchedWordList = matchedWords.split(Constants.AMPERSAND);
            timesFoundInTexts = timesFoundInTexts + matchedWordList.length;
        }
        if (timesFoundInTexts > 0){
            detectionInformation = new DetectionInformation();
            detectionInformation.setTimesFoundInTexts(timesFoundInTexts);}
        return detectionInformation;
    }


    public List<Text> createTextList(List<Object[]> texts, boolean positionalInfo){
        List<Text> textList = null;
        if (texts != null) {
            textList = new ArrayList<>();
            for (Object[] txt : texts) {
                Text text = new Text();
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


    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndDiseaseNameAndIsValidated(String sourceName, Date version, String diseaseName, boolean isValidated) {
        List<Object[]> symptoms = daoDisease.findSymptomsBySourceAndVersionAndDiseaseNameAndIsValidated( sourceName, version, diseaseName, isValidated );
        //System.out.println(sourceName+" | "+version+" | "+diseaseName+" | "+isValidated+" | "+symptoms.size() );
        return createDiseasesWithDisnetConcept(symptoms);
    }

    @Override
    public List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndIsValidatedNative(String sourceName, Date version, String code, String resourceName, boolean isValidated) {
        List<Object[]> symptoms = daoDisease.findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndIsValidatedNative( sourceName, version, code, resourceName, isValidated );
        //System.out.println("HOLA: "+sourceName+" | "+version+" | "+code+" | "+resourceName+" | "+isValidated+" | "+symptoms.size() );
        return createDiseasesWithDisnetConcept(symptoms);
    }


    public List<edu.upm.midas.model.Disease> createDiseasesWithDisnetConcept(List<Object[]> symptoms){
        List<edu.upm.midas.model.Disease> diseaseList = null;
        String diseaseId = "";
        List<DisnetConcept> concepts = new ArrayList<>();
        if (symptoms != null) {
            diseaseList = new ArrayList<>();
            for (Object[] symptom : symptoms) {
                //if (count == 1) diseaseId = (String) symptom[0];
                if (!diseaseId.equals((String) symptom[3])){
                    edu.upm.midas.model.Disease disease = new edu.upm.midas.model.Disease();
                    diseaseId = (String) symptom[3];
                    disease.setName((String) symptom[4]);
                    disease.setUrl((String) symptom[5]);
                    //System.out.println("size for: "+concepts.size());
                    if (concepts.size() > 0 ) {
                        concepts = new ArrayList<>();//concepts.clear();
                    }
                    disease.setDisnetConceptList(concepts);
                    diseaseList.add(disease);
                }
                DisnetConcept DisnetConcept = new DisnetConcept();
                DisnetConcept.setCui((String) symptom[0]);
                DisnetConcept.setName((String) symptom[1]);
                //DisnetConcept.setUrl((String) symptom[5]);
                DisnetConcept.setSemanticTypes(setSemanticTypes((String) symptom[6]));
                concepts.add(DisnetConcept);
                //disnetConcepts.add(DisnetConcept);
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

    @Transactional(propagation= Propagation.REQUIRED)
    public void save(Disease disease) {
        daoDisease.persist(disease);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public int insertNative(String diseaseId, String name, String cui) {
        return daoDisease.insertNative( diseaseId, name, cui);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public int insertNativeHasDisease(String documentId, Date date, String diseaseId) {
        return daoDisease.insertNativeHasDisease( documentId, date, diseaseId );
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindFull(Disease disease) {
        Disease dis = daoDisease.findById(disease.getDiseaseId());
        if(dis!=null){
            dis.setDiseaseId(disease.getDiseaseId());
            dis.setName(disease.getName());
            dis.setCui(disease.getCui());
            //sour.getDiseasesBySidsource().clear();
            //sour.getDiseasesBySidsource().addAll(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource())?source.getDiseasesBySidsource():new ArrayList<DisnetConceptsResponse>());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindPartial(Disease disease) {
        Disease dis = daoDisease.findByNameQuery(disease.getName());
        if(dis!=null){
/*
            if(StringUtils.isNotBlank(disease.getDocumentId()))
                dis.setDocumentId(disease.getDocumentId());
            if(StringUtils.isNotBlank(disease.getDiseases()))
                dis.setDiseases(disease.getDiseases());
*/

            //dis.setDocumentList( disease.getDocumentList() );
/*
            if(StringUtils.isNotBlank(disease.getUrl()))
                dis.setUrl(disease.getUrl());
*/
            //if(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource()))
            //    sour.setDiseasesBySidsource(source.getDiseasesBySidsource());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean deleteById(String diseaseId) {
        Disease disease = daoDisease.findById(diseaseId);
        if(disease !=null)
            daoDisease.delete(disease);
        else
            return false;
        return true;
    }
}
