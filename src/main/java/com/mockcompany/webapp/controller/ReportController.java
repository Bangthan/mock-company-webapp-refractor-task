package com.mockcompany.webapp.controller;

import com.mockcompany.webapp.api.SearchReportResponse;
import com.mockcompany.Service.SearchReportResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

import java.util.HashMap;
import java.util.Map;

import com.mockcompany.webapp.service.SearchService;

/**
 * Management decided it is super important that we have lots of products that match the following terms.
 * So much so, that they would like a daily report of the number of products for each term along with the total
 * product count.
 *
 * TODO: Refactor this class by rewritting the runReport method to use the SearchService
 */
@RestController
public class ReportController {

    // After reading code/tests, we can capture the important terms in an array!
    private static final String[] importantTerms = new String[] {
            "Cool",
            "Amazing",
            "Perfect",
            "Kids"
    };

    private final EntityManager entityManager;

    private final SearchService searchService;


    @Autowired
    public ReportController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @GetMapping("/api/products/report")
    public SearchReportResponse runReport() {
       
         Number count = (Number) this.entityManager.createQuery("SELECT count(item) FROM ProductItem item").getSingleResult();

        // For each important term, query on it and add size of results to our Map
        Map<String, Integer> hits = new HashMap<>();
        for (String term : importantTerms) {
            hits.put(term, searchService.search(term).size());
        }

         SearchReportResponse response = new SearchReportResponse();
        response.setProductCount(count.intValue());
        response.setSearchTermHits(hits);

        return response;
    }
}
