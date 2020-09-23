package edu.upm.midas.repository.jpa;

import edu.upm.midas.model.jpa.GeneBio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GenesBioRepository extends JpaRepository<GeneBio,String> {
    @Query(value = "SELECT g.gene_id, g.gene_name, g.gene_symbol from disnet_biolayer.gene g ;", nativeQuery = true)
    List<GeneBio> findGenes();
    @Query(value = "SELECT g.gene_id, g.gene_name, g.gene_symbol from disnet_biolayer.gene g where g.gene_id = :id ;", nativeQuery = true)
    GeneBio findGeneById(@Param("id") String id);
    @Query(value = "SELECT dg.disease_id from disnet_biolayer.disease_gene dg where dg.gene_id = :id ;", nativeQuery = true)
    List<String> findDiseseasesByGenes(@Param("id") String id);
    @Query(value = "SELECT e.protein_id from disnet_biolayer.encodes e where e.gene_id = :id ;", nativeQuery = true)
    List<String> findProteinsByGenes(@Param("id") String id);
    @Query(value = "SELECT gp.pathway_id from disnet_biolayer.gene_pathway gp where gp.gene_id = :id ;", nativeQuery = true)
    List<String> findPathwayByGenes(@Param("id") String id);
    @Query(value = "SELECT gv.variant_id from disnet_biolayer.gene_variant gv where gv.gene_id = :id ;", nativeQuery = true)
    List<String> findVariantsByGenes(@Param("id") String id);
}
