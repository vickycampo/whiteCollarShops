package com.whiteCollar.shops.data;

import org.springframework.data.repository.CrudRepository;
import com.whiteCollar.shops.data.Pictures;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called PicturesRepository
// CRUD refers Create, Read, Update, Delete

public interface PicturesRepository extends CrudRepository<Pictures, Integer>
{
    List<Pictures> findByShopid(int shopid);
}
