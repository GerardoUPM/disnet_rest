package edu.upm.midas.service.jpa.impl;
import edu.upm.midas.model.jpa.Code;
import edu.upm.midas.model.jpa.CodePK;
import edu.upm.midas.repository.jpa.CodeRepository;
import edu.upm.midas.service.jpa.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 13/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className CodeServiceImpl
 * @see
 */
@Service("codeService")
public class CodeServiceImpl implements CodeService {

    @Autowired
    private CodeRepository daoCode;


    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Code findById(CodePK codePK) {
        Code code = daoCode.findById(codePK);
        //if(source!=null)
        //Hibernate.initialize(source.getDiseasesBySidsource());
        return code;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Code findByCodeQuery(String code) {
        return daoCode.findByCodeQuery(code);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Code findByResourceIdQuery(int resourceId) {
        return daoCode.findByResourceIdQuery(resourceId);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Object[] findByIdNative(String code, int resourceId) {
        return daoCode.findByIdNative(code, resourceId);
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public boolean existCodeByCodeAndResourceNameAndSourceAndVersionNative(String sourceName, Date version, String code, String resourceName) {
        List<Object[]> codeList = daoCode.findByCodeAndResourceNameAndSourceAndVersionNative(sourceName, version, code, resourceName);
        if (codeList != null){
            if (codeList.size() > 0)
                return true;
            else return false;
        }else return false;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Code> findAll() {
        List<Code> listCodeEntities = daoCode.findAllQuery();
        return listCodeEntities;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public void save(Code code) {
        daoCode.persist(code);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public int insertNative(String code, int resourceId) {
        return daoCode.insertNative( code, resourceId );
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public int insertNativeUrl(String code, int resourceId, String urlId) {
        return daoCode.insertNativeUrl( code, resourceId, urlId );
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public int insertNativeHasCode(String documentId, Date date, String code, int resourceId) {
        return daoCode.insertNativeHasCode( documentId, date, code, resourceId );
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindFull(Code code, CodePK codePK) {
        Code cod = daoCode.findById(codePK);
        if(cod!=null){
            //==>cod.setCodePK(code.getCodePK());
//            sour.set(source.getDisease());
            //sour.getDiseasesBySidsource().clear();
            //sour.getDiseasesBySidsource().addAll(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource())?source.getDiseasesBySidsource():new ArrayList<DisnetConceptsResponse>());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindPartial(Code code, CodePK codePK) {
        Code cod = daoCode.findById(codePK);
        if(cod!=null){
            //cod.set.setDocumentList( code.getDocumentList() );
/*
            if(StringUtils.isNotBlank(code.getCodePK()))
                cod.setSourceId(code.getSourceId());
            if(StringUtils.isNotBlank(code.getDiseases()))
                cod.setDiseases(code.getDiseases());
*/
            //if(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource()))
            //    sour.setDiseasesBySidsource(source.getDiseasesBySidsource());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean deleteById(CodePK codePK) {
        Code code = daoCode.findById(codePK);
        if(code !=null)
            daoCode.delete(code);
        else
            return false;
        return true;
    }
}
