package org.adapt.repository;

import java.util.List;

import org.adapt.domain.EndUser;
import org.adapt.domain.LineItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EndUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EndUserRepository extends JpaRepository<EndUser, Long> {

	@Query("from EndUser where email like %?1%")
	public EndUser findByEmail(String email);
	
}
