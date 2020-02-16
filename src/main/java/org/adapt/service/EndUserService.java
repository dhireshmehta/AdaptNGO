package org.adapt.service;

import org.adapt.domain.EndUser;
import org.adapt.repository.EndUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link EndUser}.
 */
@Service
@Transactional
public class EndUserService {

    private final Logger log = LoggerFactory.getLogger(EndUserService.class);

    private final EndUserRepository endUserRepository;

    public EndUserService(EndUserRepository endUserRepository) {
        this.endUserRepository = endUserRepository;
    }

    /**
     * Save a endUser.
     *
     * @param endUser the entity to save.
     * @return the persisted entity.
     */
    public EndUser save(EndUser endUser) {
        log.debug("Request to save EndUser : {}", endUser);
        return endUserRepository.save(endUser);
    }
    
    public EndUser getByEmail(String email)
    {
    	log.debug("Request to get EndUser By Email : {}",email);
    	return endUserRepository.findByEmail(email);
    }
    
    public Boolean checkEndUserExistsByEmail(String email)
    {
    	log.debug("Request to get EndUser By Email : {}",email);
    	return endUserRepository.findByEmail(email)!=null;
    }
    /**
     * Get all the endUsers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EndUser> findAll() {
        log.debug("Request to get all EndUsers");
        return endUserRepository.findAll();
    }


    /**
     * Get one endUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EndUser> findOne(Long id) {
        log.debug("Request to get EndUser : {}", id);
        return endUserRepository.findById(id);
    }

    /**
     * Delete the endUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EndUser : {}", id);
        endUserRepository.deleteById(id);
    }
}
