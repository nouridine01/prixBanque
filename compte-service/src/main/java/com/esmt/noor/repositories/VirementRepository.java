package com.esmt.noor.repositories;

import com.esmt.noor.entities.Virement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface VirementRepository extends JpaRepository<Virement,Long> {
    //public Collection<Virement> findByBillId(Long id);
}
