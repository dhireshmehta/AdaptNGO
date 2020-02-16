package org.adapt.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.adapt.domain.EndUser;
import org.adapt.domain.LineItem;
import org.adapt.domain.Roles;
import org.adapt.repository.RolesRepository;
import org.adapt.service.EndUserService;
import org.adapt.service.LineItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AdaptResource {

	private final Logger log = LoggerFactory.getLogger(LineItemResource.class);

	private final LineItemService lineItemService;

	private final RolesRepository rolesRepository;

	private final EndUserService endUserService;

	public AdaptResource(LineItemService lineItemService, RolesRepository rolesRepository,
			EndUserService endUserService) {
		this.lineItemService = lineItemService;
		this.rolesRepository = rolesRepository;
		this.endUserService = endUserService;
	}

	/**
	 * {@code GET  /line-items} : get all the lineItems by Category.
	 *
	 * 
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of lineItems in body.
	 */
	@GetMapping("/adapt/getBy/categories")
	public List<LineItem> getAllLineItemsByCategory(@RequestParam("category") String category) {
		log.debug("REST request to get all LineItems by Category");
		return lineItemService.findByCategory(category);
	}

	/**
	 * {@code GET  /line-items} : get all the lineItems by Role.
	 *
	 * 
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of lineItems in body.
	 */
	@GetMapping("/adapt/getBy/roles")
	public List<LineItem> getAllLineItemsByRole(@RequestParam("role") String roles) {
		log.debug("REST request to get all LineItems by Role");

		List<LineItem> retVal = new ArrayList<LineItem>();
		for (String role : roles.split(",")) {
			retVal.addAll(lineItemService.findByRole(role));
		}
		return retVal;
	}

	/**
	 * {@code GET  /line-items} : get all the lineItems by Desc.
	 *
	 * 
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of lineItems in body.
	 */
	@GetMapping("/adapt/getBy/desc")
	public List<LineItem> getAllLineItemsByDesc(@RequestParam("desc") String desc) {
		log.debug("REST request to get all LineItems by Desc");
		return lineItemService.findByDesc(desc);
	}

	@GetMapping("/adapt/incrementViewCount/{id}")
	public ResponseEntity<Boolean> addView(@PathVariable Long id) {
		log.debug("REST request to Update LineItem View Count : {}", id);
		Optional<LineItem> lineItem = lineItemService.findOne(id);
		if (lineItem.isPresent()) {
			LineItem item = lineItem.get();
			item.setViewCount(item.getViewCount() + 1);
			lineItemService.save(item);
		} else {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(true);
	}

	@GetMapping("/adapt/getAllRoles")
	public List<Roles> getAllRoles() {
		log.debug("REST request to get all Roles");
		return rolesRepository.findAll();
	}

	@GetMapping("/adapt/checkUserNameExists")
	public Boolean checkUserNameExists(@RequestParam("email") String email) {
		return endUserService.checkEndUserExistsByEmail(email);
	}
	@GetMapping("/adapt/getUserByEmail")
	public EndUser getUserNameByEmail(@RequestParam("email") String email) {
		return endUserService.getByEmail(email);
	}	
	@GetMapping("/adapt/createOrSaveEndUser")
	public EndUser createOrSaveEndUser(@RequestParam("email") String email, @RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, @RequestParam("roles") String roles) {
		
		EndUser endUser = endUserService.getByEmail(email);
		if(endUser==null) {
			endUser=new EndUser();
		}
		
		endUser.email(email).firstName(firstName).lastName(lastName).roles(roles);
		endUserService.save(endUser);
		return endUser;
	}

}
