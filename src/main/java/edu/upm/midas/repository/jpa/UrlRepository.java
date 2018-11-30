package edu.upm.midas.repository.jpa;

import edu.upm.midas.model.jpa.Url;

import java.util.List;

/**
 * Created by gerardo on 13/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className UrlRepository
 * @see
 */
public interface UrlRepository {

    Url findById(String urlId);

    Url findByIdQuery(String urlId);

    Url findByNameQuery(String urlName);

    Url findLastUrlQuery();

    Url findByIdNative(String urlId);

    Url findByIdNativeResultClass(String urlId);

    List<Url> findAllQuery();

    void persist(Url url);

    int insertNative(String urlId, String url);

    boolean deleteById(String urlId);

    void delete(Url url);

    Url update(Url url);

    int updateByIdQuery(Url url);
    
}
