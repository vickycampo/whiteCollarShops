package com.whiteCollar.shops.data;

import org.springframework.data.repository.CrudRepository;

import com.whiteCollar.shops.data.Shops;

// This will be AUTO IMPLEMENTED by Spring into a Bean called ShopsRepository
// CRUD refers Create, Read, Update, Delete

public interface ShopsRepository extends CrudRepository<Shops, Integer>
{

}
