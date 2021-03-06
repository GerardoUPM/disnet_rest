package edu.upm.midas.repository.jpa.impl;
import edu.upm.midas.model.jpa.Resource;
import edu.upm.midas.repository.jpa.AbstractDao;
import edu.upm.midas.repository.jpa.ResourceRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gerardo on 09/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edsssdb
 * @className ResourceRepositoryImpl
 * @see
 */
@Repository("ResourceRepositoryDao")
public class ResourceRepositoryImpl extends AbstractDao<Integer, Resource>
                                    implements ResourceRepository {

    @Override
    public Resource findById(Integer resourceId) {
        Resource resource = getByKey(resourceId);
        return resource;
    }

    @SuppressWarnings("unchecked")
    public Resource findByIdQuery(Integer resourceId) {
        Resource resource = null;
        List<Resource> resourceList = (List<Resource>) getEntityManager()
                .createNamedQuery("Resource.findById")
                .setParameter("resourceId", resourceId)
                .getResultList();
        if (CollectionUtils.isNotEmpty(resourceList))
            resource = resourceList.get(0);
        return resource;
    }

    @SuppressWarnings("unchecked")
    public Resource findByNameQuery(String resourceName) {
        Resource resource = null;
        List<Resource> resourceList = (List<Resource>) getEntityManager()
                .createNamedQuery("Resource.findByNameNative")//BUSCA APLICANDO SENSIBILIDAD ENTRE MAYÚSCULAS Y MINÚSCULAS
                .setParameter("name", resourceName)
                .getResultList();
        if (CollectionUtils.isNotEmpty(resourceList))
            resource = resourceList.get(0);
        return resource;
    }

    @Override
    public int findIdByNameQuery(String resourceName) {
        //EntityManager em = getEntityManager();
        //em.flush();
        //em.close();
        int resourceId = (int) getEntityManager()
                .createNamedQuery("Resource.findIdByNameNative")
                .setParameter("name", resourceName)
                .setMaxResults(1)
                .getSingleResult();
        return resourceId;
    }


    @SuppressWarnings("unchecked")
    @Override
    public Resource findByIdNative(Integer resourceId) {
        Resource resource = null;
        List<Resource> resourceList = (List<Resource>) getEntityManager()
                .createNamedQuery("Resource.findByIdNative")
                .setParameter("resourceId", resourceId)
                .getResultList();
        if (CollectionUtils.isNotEmpty(resourceList))
            resource = resourceList.get(0);
        return resource;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Resource findByIdNativeResultClass(Integer resourceId) {
        Resource resource = null;
        List<Resource> resourceList = (List<Resource>) getEntityManager()
                .createNamedQuery("Resource.findByIdNativeResultClass")
                .setParameter("resourceId", resourceId)
                .getResultList();
        if (CollectionUtils.isNotEmpty(resourceList))
            resource = resourceList.get(0);
        return resource;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Resource> findAllQuery() {
        List<Resource> resources = getEntityManager()
                .createNamedQuery("Resource.findAll")
                .setMaxResults(0)
                .getResultList();
        return resources;
    }

    @Override
    public void persist(Resource resource) {
        super.persist(resource);
    }

    @Override
    public int insertNative(int resourceId, String name) {
        return getEntityManager()
                .createNamedQuery("Resource.insertNative")
                .setParameter("resourceId", resourceId)
                .setParameter("name", name)
                .executeUpdate();
    }

    @Override
    public boolean deleteById(Integer resourceId) {
        Resource resource = findById( resourceId );
        if(resource ==null)
            return false;
        super.delete(resource);
        return true;
    }

    @Override
    public void delete(Resource resource) {
        super.delete(resource);
    }

    @Override
    public Resource update(Resource resource) {
        return super.update(resource);
    }

    @Override
    public Integer updateByIdQuery(Resource resource) {
        int rows = getEntityManager()
                .createNamedQuery("Resource.updateById")
                .setParameter("resourceId", resource.getResourceId())
                .setParameter("name", resource.getName())
                .executeUpdate();
        return rows;
    }
}
