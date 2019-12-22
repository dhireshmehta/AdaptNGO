package org.adapt.repository;

import java.util.List;

import org.adapt.domain.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LineItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LineItemRepository extends JpaRepository<LineItem, Long> {
	
	@Query("from LineItem where categories like %?1%")
	public List<LineItem> findByCategory(String category);
	
	@Query("from LineItem where jhi_desc like %?1%")
	public List<LineItem> findByDesc(String desc);
	
	@Query("from LineItem where roles like %?1%")
	public List<LineItem> findByRole(String role);
}
