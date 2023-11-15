package ${package}.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ${package}.entity.${className}Entity;

@Repository
public interface ${className}Repository extends CrudRepository<${className}Entity, String>{
	
}