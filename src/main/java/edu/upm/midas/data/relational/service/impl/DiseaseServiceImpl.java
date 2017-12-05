package edu.upm.midas.data.relational.service.impl;
import edu.upm.midas.data.relational.entities.edsssdb.Disease;
import edu.upm.midas.data.relational.repository.DiseaseRepository;
import edu.upm.midas.data.relational.service.DiseaseService;
import edu.upm.midas.model.DisnetConcept;
import edu.upm.midas.model.DiseaseDisnetConcepts;
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
    public BigInteger numberDiseasesBySourceAndVersion(String sourceName, Date version) {
        BigInteger disease = daoDisease.numberDiseasesBySourceAndVersion(sourceName, version);
        if (disease != null) {
            return disease;
        }else{
            return BigInteger.valueOf(0);
        }
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<edu.upm.midas.model.Disease> findAllBySourceAndVersion(String sourceName, Date version) {
        List<edu.upm.midas.model.Disease> diseaseList = null;
        List<Object[]> diseases = daoDisease.findAllBySourceAndVersion(sourceName, version);
        if (diseases != null) {
            diseaseList = new ArrayList<>();
            for (Object[] dis : diseases) {
                edu.upm.midas.model.Disease disease = new edu.upm.midas.model.Disease();
                disease.setDiseaseId((String) dis[0]);
                disease.setName((String) dis[1]);
                diseaseList.add(disease);
            }
        }
        return diseaseList;
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
    public List<DiseaseDisnetConcepts> withMoreSymptomsBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated, int limit) {
        List<Object[]> diseases = daoDisease.withMoreSymptomsBySourceAndVersionAndIsValidated(sourceName, version, isValidated, limit);
        return getDiseaseSymptomsList(diseases);
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
                disease.setCount(count.intValue());
                diseaseList.add(disease);
            }
        }
        return diseaseList;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<DisnetConcept> findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidated(String sourceName, Date version, String diseaseId, boolean isValidated) {
        List<Object[]> symptoms = daoDisease.findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidated(sourceName, version, diseaseId, isValidated);
        //System.out.println(sourceName+" - "+version+" - "+diseaseId +" - "+isValidated);
        List<DisnetConcept> DisnetConcepts = null;

        if (symptoms != null) {
            DisnetConcepts = new ArrayList<>();
            for (Object[] symptom : symptoms) {
                DisnetConcept DisnetConcept = new DisnetConcept();
                DisnetConcept.setCui((String) symptom[0]);
                DisnetConcept.setName((String) symptom[1]);
                DisnetConcepts.add(DisnetConcept);
            }
        }
        return DisnetConcepts;
    }

    @Override
    public boolean existDiseaseByExactNameAndSourceAndVersionNative(String sourceName, Date version, String diseaseName) {
        Object[] disease = daoDisease.findByExactNameAndSourceAndVersionNative(sourceName, version, diseaseName);
        if (disease != null)
            if (disease.length > 0)
                return true;
            else return false;
        else return false;
    }

    @Override
    public List<Object[]> findByLikeNameAndSourceAndVersionNative(String sourceName, Date version, String diseaseName) {
        return null;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndDiseaseNameAndIsValidated(String sourceName, Date version, String diseaseName, boolean isValidated) {
        List<Object[]> symptoms = daoDisease.findSymptomsBySourceAndVersionAndDiseaseNameAndIsValidated( sourceName, version, diseaseName, isValidated );
        //System.out.println(sourceName+" | "+version+" | "+diseaseName+" | "+isValidated+" | "+symptoms.size() );
        return createDiseaseList(symptoms);
    }

    @Override
    public List<edu.upm.midas.model.Disease> findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndIsValidatedNative(String sourceName, Date version, String code, String resourceName, boolean isValidated) {
        List<Object[]> symptoms = daoDisease.findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndIsValidatedNative( sourceName, version, code, resourceName, isValidated );
        //System.out.println("HOLA: "+sourceName+" | "+version+" | "+code+" | "+resourceName+" | "+isValidated+" | "+symptoms.size() );
        return createDiseaseList(symptoms);
    }


    public List<edu.upm.midas.model.Disease> createDiseaseList(List<Object[]> symptoms){
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
            if(StringUtils.isNotBlank(disease.getDisease()))
                dis.setDisease(disease.getDisease());
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
