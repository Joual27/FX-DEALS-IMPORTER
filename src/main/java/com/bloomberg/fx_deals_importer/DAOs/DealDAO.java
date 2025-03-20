package com.bloomberg.fx_deals_importer.DAOs;

import com.bloomberg.fx_deals_importer.entities.Deal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealDAO extends CrudRepository<Deal, String> {
}
