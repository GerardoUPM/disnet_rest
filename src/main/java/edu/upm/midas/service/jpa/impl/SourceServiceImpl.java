package edu.upm.midas.service.jpa.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import edu.upm.midas.constants.Constants;
import edu.upm.midas.model.jpa.Source;
import edu.upm.midas.repository.jpa.SourceRepository;
import edu.upm.midas.service.jpa.SourceService;
import edu.upm.midas.model.response.Configuration;
import edu.upm.midas.model.response.particular.DiseaseAlbumConfiguration;
import edu.upm.midas.model.response.particular.MetamapConfiguration;
import edu.upm.midas.model.response.particular.TvpConfiguration;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 03/05/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project ExtractionInformationDiseasesWikipedia
 * @className SourceServiceImpl
 * @see
 */
@Service("sourceService")
public class SourceServiceImpl implements SourceService {

    @Autowired
    private SourceRepository daoSource;

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Source findById(String sourceId) {
        Source source = daoSource.findById((String) sourceId);
        //if(source!=null)
            //Hibernate.initialize(source.getDiseasesBySidsource());
        return source;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Source findByName(String sourceName) {
        Source source = daoSource.findByNameQuery(sourceName);
/*
        if(source!=null)
            Hibernate.initialize(source.getVersionList());
*/
        return source;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public String findByNameNative(String sourceName) {
        return daoSource.findByNameNative( sourceName );
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public Source findLastSourceQuery() {
        return daoSource.findLastSourceQuery();
    }

    @Override
    public String findLastSourceIdQuery() {
        return daoSource.findLastSourceIdQuery();
    }

    @Override
    public List<String> findAllVersionsNative() {
        List<String> versionList = null;
        List<Object[]> versions = daoSource.findAllVersionsNative();
        if (versions != null) {
            versionList = new ArrayList<>();
            for (Object[] version : versions) {
                versionList.add((String) version[0]);
            }
        }
        return null;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    @Override
    public List<Configuration> findSourceAndVersionConfigurationBySourceAndVersion(String sourceName, Date version) {
        List<Configuration> configurationList = null;
        List<Object[]> configurations = daoSource.findSourceAndVersionConfigurationBySourceAndVersion(sourceName, version);
        if (configurations != null){
            configurationList = new ArrayList<>();
            for (Object[] config: configurations) {
                Configuration configuration = new Configuration();
                configuration.setConfigurationId((String) config[0]);
                configuration.setTool((String) config[1]);
                configuration.setConfiguration((String) config[2]);

                Gson gson = new Gson();
                if (configuration.getTool().contains(Constants.METAMAP_API_REST_CODE)){
                    Type metamapConfigType = new TypeToken<MetamapConfiguration>(){}.getType();
                    MetamapConfiguration metamapConfiguration = gson.fromJson(configuration.getConfiguration(), metamapConfigType);
                    configuration.setMetamapConfiguration(metamapConfiguration);
                }
                if (configuration.getTool().contains(Constants.TVP_API_REST_CODE)){
                    Type tvpConfigType = new TypeToken<TvpConfiguration>(){}.getType();
                    TvpConfiguration tvpConfiguration = gson.fromJson(configuration.getConfiguration(), tvpConfigType);
                    configuration.setTvpConfiguration(tvpConfiguration);
                }
                if (configuration.getTool().contains(Constants.DIS_ALBUM_API_REST_CODE)){
                    Type diseaseAlbumConfigType = new TypeToken<DiseaseAlbumConfiguration>(){}.getType();
                    DiseaseAlbumConfiguration diseaseAlbumConfiguration = gson.fromJson(configuration.getConfiguration(), diseaseAlbumConfigType);
                    configuration.setDiseaseAlbumConfiguration(diseaseAlbumConfiguration);
                }
                configurationList.add(configuration);
            }
        }
        return configurationList;
    }

    @Transactional(propagation= Propagation.REQUIRED,readOnly=true)
    public List<Source> findAll() {
        List<Source> listSourceEntities = daoSource.findAllQuery();
        return listSourceEntities;
    }

    @Override
    public List<Object[]> findAllNative() {
        return daoSource.findAllNative();
    }

    @Override
    public List<Date> findAllVersionsBySourceNative(String source) {
        return daoSource.findAllVersionsBySourceNative( source );
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public void save(Source source) {
        daoSource.persist(source);
    }

    @Override
    public int insertNative(String sourceId, String name) {
        return daoSource.insertNative( sourceId, name );
    }

    @Override
    public int insertNativeUrl(String sourceId, String urlId) {
        return daoSource.insertNativeUrl( sourceId, urlId );
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindFull(Source source) {
        Source sour = daoSource.findById(source.getSourceId());
        if(sour!=null){
            sour.setSourceId(source.getSourceId());
            sour.setName(source.getName());
            //sour.getDiseasesBySidsource().clear();
            //sour.getDiseasesBySidsource().addAll(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource())?source.getDiseasesBySidsource():new ArrayList<DisnetConceptsResponse>());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean updateFindPartial(Source source) {
        Source sour = daoSource.findById(source.getSourceId());
        if(sour!=null){
            if(StringUtils.isNotBlank(source.getSourceId()))
                sour.setSourceId(source.getSourceId());
            if(StringUtils.isNotBlank(source.getName()))
                sour.setName(source.getName());
            //if(CollectionUtils.isNotEmpty(source.getDiseasesBySidsource()))
            //    sour.setDiseasesBySidsource(source.getDiseasesBySidsource());
        }else
            return false;
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean deleteById(String sourceId) {
        Source source = daoSource.findById(sourceId);
        if(source !=null)
            daoSource.delete(source);
        else
            return false;
        return true;
    }
}
