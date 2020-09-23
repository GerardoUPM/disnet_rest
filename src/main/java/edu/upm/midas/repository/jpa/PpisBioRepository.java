package edu.upm.midas.repository.jpa;

import edu.upm.midas.model.jpa.Ppi_Bio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PpisBioRepository extends JpaRepository<Ppi_Bio,String>  {


    @Query(value = "SELECT ppi.protein1_id, ppi.protein2_id from disnet_biolayer.ppi ;", nativeQuery = true)
    List<Ppi_Bio> findPpis();
}
