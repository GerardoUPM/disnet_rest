package edu.upm.midas.service.jpa;

import edu.upm.midas.model.jpa.Source;
import edu.upm.midas.model.response.Configuration;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 28/04/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project ExtractionInformationDiseasesWikipedia
 * @className SourceService
 * @see
 */
public interface SourceService {

    Source findById(String sourceId);

    Source findByName(String sourceName);

    String findByNameNative(String sourceName);

    Source findLastSourceQuery();

    String findLastSourceIdQuery();

    List<String> findAllVersionsNative();

    List<Configuration> findSourceAndVersionConfigurationBySourceAndVersion(String sourceName, Date version);

    List<Source> findAll();

    List<Object[]> findAllNative();

    List<Date> findAllVersionsBySourceNative(String source);//Source name

    void save(Source source);

    int insertNative(String sourceId, String name);

    int insertNativeUrl(String sourceId, String urlId);

    boolean updateFindFull(Source source);

    boolean updateFindPartial(Source source);

    boolean deleteById(String sourceId);

}
