package com.whiteCollar.shops.data;

import org.springframework.data.repository.CrudRepository;

import com.whiteCollar.shops.data.Shops;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called ShopsRepository
// CRUD refers Create, Read, Update, Delete

public interface ShopsRepository extends CrudRepository<Shops, Integer>
{
    List<Shops> findByNameIgnoreCase(String name);
    Shops findById (int id);
}
