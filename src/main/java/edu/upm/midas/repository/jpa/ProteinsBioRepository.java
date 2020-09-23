package edu.upm.midas.repository.jpa;

import edu.upm.midas.model.jpa.PathwayBio;
import edu.upm.midas.model.jpa.ProteinBio;
import edu.upm.midas.model.jpa.VariantBio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProteinsBioRepository extends JpaRepository<ProteinBio,String>  {


    @Query(value = "SELECT p.protein_id from disnet_biolayer.protein p ;", nativeQuery = true)
    List<ProteinBio> findProteins();
    @Query(value = "SELECT e.gene_id from disnet_biolayer.encodes e where protein_id = :id ;", nativeQuery = true)
    List<Integer> findGenesById(@Param("id") String id);
}
