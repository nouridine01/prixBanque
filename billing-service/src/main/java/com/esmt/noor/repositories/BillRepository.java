package com.esmt.noor.repositories;

import com.esmt.noor.entities.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface BillRepository extends JpaRepository<Bill,Long> {
    public List<Bill> findByClientFromId(Long id);
    public List<Bill> findByClientToId(Long id);
}
