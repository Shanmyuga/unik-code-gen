/**
 * 
 */
package com.cognizant.fecodegen.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cognizant.fecodegen.model.LayoutEntity;

/**
 * @author 238209
 *
 */
public interface LayoutRepository extends MongoRepository<LayoutEntity, BigInteger> {

	
	public List<LayoutEntity> findByLayoutName(String name);
	
}
