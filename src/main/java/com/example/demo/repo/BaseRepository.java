package com.example.demo.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.List;


@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {
		
	@Query("select a from #{#entityName} a")
	public Iterable<T> findAllList();
	
	@Query("select a from #{#entityName} a")
	public List<T> findAll();

}
