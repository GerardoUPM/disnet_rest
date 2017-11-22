package edu.upm.midas.data.relational.service.impl;
import edu.upm.midas.data.relational.entities.edsssdb.Disease;
import edu.upm.midas.data.relational.repository.DiseaseRepository;
import edu.upm.midas.data.relational.service.DiseaseService;
import edu.upm.midas.model.DiseaseSymptoms;
import edu.upm.midas.model.Finding;
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
        List<Object[]> diseases = daoDisease.findAllBySourceAndVersion(sourceName, version);
        List<edu.upm.midas.model.Disease> diseaseList = null;
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
    public List<DiseaseSymptoms> withFewerSymptomsBySourceAndVersionAndValidated(String sourceName, Date version, boolean isValidated, int limit) {
        List<Object[]> diseases = daoDisease.withFewerSymptomsBySourceAndVersionAndValidated(sourceName, version, isValidated, limit);
        return getDiseaseSymptomsList(diseases);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<DiseaseSymptoms> withMoreSymptomsBySourceAndVersionAndValidated(String sourceName, Date version, boolean isValidated, int limit) {
        List<Object[]> diseases = daoDisease.withMoreSymptomsBySourceAndVersionAndValidated(sourceName, version, isValidated, limit);
        return getDiseaseSymptomsList(diseases);
    }

    public List<DiseaseSymptoms> getDiseaseSymptomsList(List<Object[]> diseases){
        List<DiseaseSymptoms> diseaseList = null;
        if (diseases != null) {
            diseaseList = new ArrayList<>();
            for (Object[] dis : diseases) {
                DiseaseSymptoms disease = new DiseaseSymptoms();
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
    public List<Finding> findSymptomsBySourceAndVersionAndDiseaseIdAndValidated(String sourceName, Date version, String diseaseId, boolean isValidated) {
        List<Object[]> symptoms = daoDisease.findSymptomsBySourceAndVersionAndDiseaseIdAndValidated(sourceName, version, diseaseId, isValidated);
        //System.out.println(sourceName+" - "+version+" - "+diseaseId +" - "+isValidated);
        List<Finding> findings = null;

        if (symptoms != null) {
            findings = new ArrayList<>();
            for (Object[] symptom : symptoms) {
                Finding finding = new Finding();
                finding.setCui((String) symptom[0]);
                finding.setName((String) symptom[1]);
                findings.add(finding);
            }
        }
        return findings;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Finding> findSymptomsBySourceAndVersionAndDiseaseNameAndValidated(String sourceName, Date version, String diseaseName, boolean isValidated) {
        List<Object[]> symptoms = daoDisease.findSymptomsBySourceAndVersionAndDiseaseNameAndValidated( sourceName, version, diseaseName, isValidated );
        List<Finding> findings = null;

        if (symptoms != null) {
            findings = new ArrayList<>();
            for (Object[] symptom : symptoms) {
                Finding finding = new Finding();
                finding.setCui((String) symptom[0]);
                finding.setName((String) symptom[1]);
                findings.add(finding);
            }
        }
        return findings;
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
            //sour.getDiseasesBySidsource().addAll(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource())?source.getDiseasesBySidsource():new ArrayList<SymptomsResponse>());
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
            if(StringUtils.isNotBlank(disease.getCui()))
                dis.setCui(disease.getCui());
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
