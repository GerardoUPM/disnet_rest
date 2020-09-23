package edu.upm.midas.repository.jpa;

import edu.upm.midas.model.jpa.PathwayBio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PathwayBioRepository extends JpaRepository<PathwayBio,String> {
    @Query(value = "SELECT p.pathway_id, p.pathway_name from disnet_biolayer.track_pathway p ;", nativeQuery = true)
    List<PathwayBio> findPathways();
    @Query(value = "SELECT p.pathway_id , p.pathway_name from disnet_biolayer.pathway p where pathway_id = :id ;", nativeQuery = true)
    List<PathwayBio> findPathwayById(@Param("id") String id);
    @Query(value = "SELECT gp.gene_id from disnet_biolayer.gene_pathway gp where pathway_id = :id ;", nativeQuery = true)
    List<Integer> findGenesById(@Param("id") String id);
}
