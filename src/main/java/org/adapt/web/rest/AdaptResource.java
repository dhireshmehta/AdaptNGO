package org.adapt.web.rest;

import java.util.List;
import java.util.Optional;

import org.adapt.domain.LineItem;
import org.adapt.service.LineItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AdaptResource {

    private final Logger log = LoggerFactory.getLogger(LineItemResource.class);

	private final LineItemService lineItemService;

	public AdaptResource(LineItemService lineItemService) {
		this.lineItemService = lineItemService;
	}
	
	/**
     * {@code GET  /line-items} : get all the lineItems by Category.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lineItems in body.
     */
    @GetMapping("/adapt/getBy/categories")
    public List<LineItem> getAllLineItemsByCategory(@RequestParam("category") String category) {
        log.debug("REST request to get all LineItems by Category");
        return lineItemService.findByCategory(category);
    }
    
    /**
     * {@code GET  /line-items} : get all the lineItems by Role.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lineItems in body.
     */
    @GetMapping("/adapt/getBy/roles")
    public List<LineItem> getAllLineItemsByRole(@RequestParam("role") String role) {
        log.debug("REST request to get all LineItems by Role");
        return lineItemService.findByRole(role);
    }

    /**
     * {@code GET  /line-items} : get all the lineItems by Desc.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lineItems in body.
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
        if(lineItem.isPresent()) {
        	LineItem item = lineItem.get();
        	item.setViewCount(item.getViewCount()+1);
        	lineItemService.save(item);
        }
        else {
        	return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(true);
    }

	
	
}
