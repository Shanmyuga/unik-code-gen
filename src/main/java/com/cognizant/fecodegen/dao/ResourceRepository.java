/**
 * 
 */
package com.cognizant.fecodegen.dao;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cognizant.fecodegen.model.ResourceEntity;

/**
 * @author 238209
 *
 */
public interface ResourceRepository extends MongoRepository<ResourceEntity, BigInteger> {

}
