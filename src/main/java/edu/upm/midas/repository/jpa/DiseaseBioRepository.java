package edu.upm.midas.repository.jpa;

import edu.upm.midas.model.DiseaseBio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiseaseBioRepository extends JpaRepository <DiseaseBio,String> {
    @Query(value = "SELECT disease_id, disease_name from disnet_biolayer.disease;", nativeQuery = true)
    List<DiseaseBio> findDiseases();
    @Query(value = "SELECT d.disease_id, d.disease_name from disnet_biolayer.disease d where d.disease_name LIKE :name",nativeQuery = true)
    DiseaseBio findDiseaseByName(@Param("name") String name);
}

