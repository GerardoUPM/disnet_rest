package edu.upm.midas.repository.jpa;

import edu.upm.midas.model.jpa.VariantBio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VariantsBioRepository extends JpaRepository<VariantBio,String> {
    @Query(value = "SELECT v.variant_id, v.chromosome, v.chrposition, v.consequence from disnet_biolayer.variant v ;", nativeQuery = true)
    List<VariantBio> findVariants();
    @Query(value = "SELECT v.variant_id, v.chromosome, v.chrposition, v.consequence from disnet_biolayer.variant v where variant_id = :id ;", nativeQuery = true)
    VariantBio findVariantById(@Param("id") String id);
    @Query(value = "SELECT dv.disease_id from disnet_biolayer.disease_variant dv where variant_id = :id ;", nativeQuery = true)
    List<String> findDiseasesById(@Param("id") String id);
    @Query(value = "SELECT gv.gene_id from disnet_biolayer.gene_variant gv where variant_id = :id ;", nativeQuery = true)
    List<Integer> findGenesById(@Param("id") String id);
}
