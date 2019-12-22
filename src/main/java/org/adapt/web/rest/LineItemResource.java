package org.adapt.web.rest;

import org.adapt.domain.LineItem;
import org.adapt.service.LineItemService;
import org.adapt.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.adapt.domain.LineItem}.
 */
@RestController
@RequestMapping("/api")
public class LineItemResource {

    private final Logger log = LoggerFactory.getLogger(LineItemResource.class);

    private static final String ENTITY_NAME = "lineItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LineItemService lineItemService;

    public LineItemResource(LineItemService lineItemService) {
        this.lineItemService = lineItemService;
    }

    /**
     * {@code POST  /line-items} : Create a new lineItem.
     *
     * @param lineItem the lineItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lineItem, or with status {@code 400 (Bad Request)} if the lineItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/line-items")
    public ResponseEntity<LineItem> createLineItem(@RequestBody LineItem lineItem) throws URISyntaxException {
        log.debug("REST request to save LineItem : {}", lineItem);
        if (lineItem.getId() != null) {
            throw new BadRequestAlertException("A new lineItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LineItem result = lineItemService.save(lineItem);
        return ResponseEntity.created(new URI("/api/line-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /line-items} : Updates an existing lineItem.
     *
     * @param lineItem the lineItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lineItem,
     * or with status {@code 400 (Bad Request)} if the lineItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lineItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/line-items")
    public ResponseEntity<LineItem> updateLineItem(@RequestBody LineItem lineItem) throws URISyntaxException {
        log.debug("REST request to update LineItem : {}", lineItem);
        if (lineItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LineItem result = lineItemService.save(lineItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lineItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /line-items} : get all the lineItems.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lineItems in body.
     */
    @GetMapping("/line-items")
    public List<LineItem> getAllLineItems() {
        log.debug("REST request to get all LineItems");
        return lineItemService.findAll();
    }
    
    /**
     * {@code GET  /line-items} : get all the lineItems by Category.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lineItems in body.
     */
    @GetMapping("/line-items/categories")
    public List<LineItem> getAllLineItemsByCategory(@RequestParam("category") String category) {
        log.debug("REST request to get all LineItems by Category");
        return lineItemService.findByCategory(category);
    }
    
    /**
     * {@code GET  /line-items} : get all the lineItems by Role.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lineItems in body.
     */
    @GetMapping("/line-items/roles")
    public List<LineItem> getAllLineItemsByRole(@RequestParam("role") String role) {
        log.debug("REST request to get all LineItems by Role");
        return lineItemService.findByRole(role);
    }

    /**
     * {@code GET  /line-items} : get all the lineItems by Desc.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lineItems in body.
     */
    @GetMapping("/line-items/desc")
    public List<LineItem> getAllLineItemsByDesc(@RequestParam("desc") String desc) {
        log.debug("REST request to get all LineItems by Desc");
        return lineItemService.findByDesc(desc);
    }


    /**
     * {@code GET  /line-items/:id} : get the "id" lineItem.
     *
     * @param id the id of the lineItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lineItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/line-items/{id}")
    public ResponseEntity<LineItem> getLineItem(@PathVariable Long id) {
        log.debug("REST request to get LineItem : {}", id);
        Optional<LineItem> lineItem = lineItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lineItem);
    }
    
    /**
     * {@code GET  /line-items/:id} : get the "id" lineItem.
     *
     * @param id the id of the lineItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lineItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/line-items/incrementViews/{id}")
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

    /**
     * {@code DELETE  /line-items/:id} : delete the "id" lineItem.
     *
     * @param id the id of the lineItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/line-items/{id}")
    public ResponseEntity<Void> deleteLineItem(@PathVariable Long id) {
        log.debug("REST request to delete LineItem : {}", id);
        lineItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
