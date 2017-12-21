package edu.upm.midas.data.relational.service.impl;
import edu.upm.midas.data.relational.entities.edsssdb.Symptom;
import edu.upm.midas.data.relational.repository.SymptomRepository;
import edu.upm.midas.data.relational.service.SymptomService;
import edu.upm.midas.model.DisnetConcept;
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
 * Created by gerardo on 19/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className SymptomServiceImpl
 * @see
 */
@Service
public class SymptomServiceImpl implements SymptomService {

    @Autowired
    private SymptomRepository daoSymptom;

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Symptom findById(String cui) {
        Symptom symptom = daoSymptom.findById(cui);
        //if(source!=null)
        //Hibernate.initialize(source.getDiseasesBySidsource());
        return symptom;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Symptom findByName(String symptomName) {
        return daoSymptom.findByNameQuery(symptomName);
    }

    @Override
    public boolean findHasSemanticTypeIdNative(String cui, String semanticType) {
        return daoSymptom.findHasSemanticTypeIdNative(cui, semanticType);
    }

    @Override
    public List<Object[]> findBySourceAndVersionNative(Date version, String source) {
        return daoSymptom.findBySourceAndVersionNative( version, source );
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Symptom> findAll() {
        List<Symptom> symptomList = daoSymptom.findAllQuery();
        return symptomList;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<DisnetConcept> mostOrLessCommonBySourceAndVersionAndIsValidatedAndForceOrExludeSemanticTypes(String sourceName, Date version, boolean isValidated, int limit, boolean mostSymptoms, boolean forceSemanticTypes, List<String> semanticTypes) {
        List<Object[]> symptoms = daoSymptom.mostOrLessCommonBySourceAndVersionAndIsValidatedAndForceOrExludeSemanticTypes(sourceName, version, isValidated, limit, mostSymptoms, forceSemanticTypes, semanticTypes);
        return getSymptomWithCountList(symptoms);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<DisnetConcept> lessCommonBySourceAndVersionAndValidated(String sourceName, Date version, boolean isValidated, int limit) {
        List<Object[]> symptoms = daoSymptom.lessCommonBySourceAndVersionAndValidated(sourceName, version, isValidated, limit);
        return getSymptomWithCountList(symptoms);
    }

    private List<DisnetConcept> getSymptomWithCountList(List<Object[]> symptoms){
        List<DisnetConcept> symptomList = null;
        if (symptoms != null) {
            symptomList = new ArrayList<>();
            for (Object[] sym : symptoms) {
                DisnetConcept symptom = new DisnetConcept();
                symptom.setCui((String) sym[0]);
                symptom.setName((String) sym[1]);
                BigInteger count = (BigInteger) sym[2];
                symptom.setCommon((Integer) count.intValue());
                symptom.setSemanticTypes(setSemanticTypes((String) sym[3]));
                symptomList.add(symptom);
            }
        }
        return symptomList;
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
    public void save(Symptom symptom) {
        daoSymptom.persist(symptom);
    }

    @Override
    public int insertNative(String cui, String name) {
        return daoSymptom.insertNative( cui, name );
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindFull(Symptom symptom) {
        Symptom sy = daoSymptom.findById(symptom.getCui());
        if(sy!=null){
                sy.setCui( symptom.getCui() );
                sy.setName( symptom.getName() );
/*
            if(StringUtils.isNotBlank(code.getCodePK()))
                cod.setSourceId(code.getSourceId());
            if(StringUtils.isNotBlank(code.getDisease()))
                cod.setDisease(code.getDisease());
*/
            //if(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource()))
            //    sour.setDiseasesBySidsource(source.getDiseasesBySidsource());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindPartial(Symptom symptom) {
        Symptom sy = daoSymptom.findById(symptom.getCui());
        if(sy!=null){
            if(StringUtils.isNotBlank(symptom.getCui()))
                sy.setCui( symptom.getCui() );
            if(StringUtils.isNotBlank(symptom.getName()))
                sy.setName( symptom.getName() );
/*
            if(StringUtils.isNotBlank(code.getCodePK()))
                cod.setSourceId(code.getSourceId());
            if(StringUtils.isNotBlank(code.getDisease()))
                cod.setDisease(code.getDisease());
*/
            //if(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource()))
            //    sour.setDiseasesBySidsource(source.getDiseasesBySidsource());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean deleteById(String cui) {
        Symptom symptom = daoSymptom.findById(cui);
        if(symptom !=null)
            daoSymptom.delete(symptom);
        else
            return false;
        return true;
    }
}
