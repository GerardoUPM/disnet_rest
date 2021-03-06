package edu.upm.midas.repository.jpa.impl;

import edu.upm.midas.model.jpa.Source;
import edu.upm.midas.repository.jpa.AbstractDao;
import edu.upm.midas.repository.jpa.SourceRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 28/04/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project ExtractionInformationDiseasesWikipedia
 * @className SourceRepositoryImpl
 * @see
 */
@Repository("SourceRepositoryDao")
public class SourceRepositoryImpl extends AbstractDao<String, Source> implements SourceRepository {


    public Source findById(String sourceId) {
        Source source = getByKey(sourceId);
        return source;
    }

    @SuppressWarnings("unchecked")
    public Source findByIdQuery(String sourceId) {
        Source source = null;
        List<Source> listSource = (List<Source>) getEntityManager()
                .createNamedQuery("Source.findById")
                .setParameter("sourceId", sourceId)
                .getResultList();
        if (CollectionUtils.isNotEmpty(listSource))
            source = listSource.get(0);
        return source;
    }

    @SuppressWarnings("unchecked")
    public Source findByNameQuery(String sourceName) {
        Source source = null;
        List<Source> listSource = (List<Source>) getEntityManager()
                .createNamedQuery("Source.findByName")
                .setParameter("name", sourceName)
                .getResultList();
        if (CollectionUtils.isNotEmpty(listSource))
            source = listSource.get(0);
        return source;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String findByNameNative(String sourceName) {
        String sourceId = null;
        List<String> sources = (List<String>) getEntityManager()
                .createNamedQuery("Source.findByNameNative")
                .setParameter("name", sourceName)
                .setMaxResults(1)
                .getResultList();
        if (CollectionUtils.isNotEmpty(sources))
            sourceId = sources.get(0);
        return sourceId;
    }

    @Override
    public Source findLastSourceQuery() {
        Source source = null;
        source = (Source) getEntityManager()
                .createNamedQuery("Source.findLastSourceNativeResultClass")
                .setMaxResults(1)
                .getResultList();
        return source;
    }

    @Override
    public String findLastSourceIdQuery() {
        String sourceId = (String) getEntityManager()
                .createNamedQuery("Source.findLastIdNative")
                .setMaxResults(1)
                .getSingleResult();
        return sourceId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Source findByIdNative(String sourceId) {
        Source source = null;
        List<Source> listSource = (List<Source>) getEntityManager()
                .createNamedQuery("Source.findByIdNativeMapping")
                .setParameter("sourceId", sourceId)
                .getResultList();
        if (CollectionUtils.isNotEmpty(listSource))
            source = listSource.get(0);
        return source;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Source findByIdNativeResultClass(String sourceId) {
        Source source = null;
        List<Source> listSource = (List<Source>) getEntityManager()
                .createNamedQuery("Source.findByIdNativeResultClass")
                .setParameter("sourceId", sourceId)
                .getResultList();
        if (CollectionUtils.isNotEmpty(listSource))
            source = listSource.get(0);
        return source;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findAllVersionsNative() {
        List<Object[]> versions = null;
        List<Object[]> versionList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Source.findAllVersionsNative")
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(versionList))
            versions = versionList;
        return versions;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findSourceAndVersionConfigurationBySourceAndVersion(String sourceName, Date version) {
        List<Object[]> configurations = null;
        List<Object[]> configurationList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Source.findSourceAndVersionConfigurationBySourceAndVersionNative")
                .setParameter("source", sourceName)
                .setParameter("version", version)
                .getResultList();
        if (CollectionUtils.isNotEmpty(configurationList))
            configurations = configurationList;
        return configurations;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Source> findAllQuery() {
        /*List_<Source> sources = getEntityManager()
                .createNamedQuery("Source.findAll")
                .setMaxResults(10)
                .getResultList();
        return sources;*/
        return (List<Source>) getEntityManager()
                .createNamedQuery("Source.findAll")
                .setMaxResults(0)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findAllNative() {
        List<Object[]> sources = null;
        List<Object[]> sourceList = (List<Object[]>) getEntityManager()
                .createNamedQuery("Source.findAllNative")
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(sourceList))
            sources = sourceList;
        return sources;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Date> findAllVersionsBySourceNative(String source) {
        List<Date> versions = null;
        List<Date> versionList = (List<Date>) getEntityManager()
                .createNamedQuery("Source.findAllVersionsBySourceNative")
                .setParameter("name", source)
                //.setMaxResults(100)
                .getResultList();
        if (CollectionUtils.isNotEmpty(versionList))
            versions = versionList;
        return versions;
    }

    @Override
    public void persist(Source source) {
        super.persist(source);
    }

    @Override
    public int insertNative(String sourceId, String name) {
        return getEntityManager()
                .createNamedQuery("Source.insertNative")
                .setParameter("sourceId", sourceId)
                .setParameter("name", name)
                .executeUpdate();
    }

    @Override
    public int insertNativeUrl(String sourceId, String urlId) {
        return getEntityManager()
                .createNamedQuery("SourceUrl.insertNative")
                .setParameter("sourceId", sourceId)
                .setParameter("urlId", urlId)
                .executeUpdate();
    }

    @Override
    public boolean deleteById(String sourceId) {
        Source source = findById( sourceId );
        if(source ==null)
            return false;
        super.delete(source);
        return true;
    }

    @Override
    public void delete(Source source) {
        super.delete(source);
    }

    @Override
    public Source update(Source source) {
        return super.update(source);
    }

    @Override
    public int updateByIdQuery(Source source) {
        /*int rows = getEntityManager()
                .createNamedQuery("Source.updateById")
                .setParameter("sourceId", source.getSourceEntityId())
                .setParameter("lastUpdate", source.getLastUpdate())
                .executeUpdate();
        return rows;*/
        return getEntityManager()
                .createNamedQuery("Source.updateById")
                .setParameter("sourceId", source.getSourceId())
                .executeUpdate();
    }
}
