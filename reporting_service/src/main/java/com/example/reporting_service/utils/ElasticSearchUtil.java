package com.example.reporting_service.utils;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public final class ElasticSearchUtil {
    private ElasticSearchUtil() {};

    public static SearchRequest buildSearchRequest(String indexName, String dateField, DateRangeDto range, String idField, Integer userId)
    {
        try
        {
            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .postFilter(getSearchByUserIdAndDateRangeQueryBuilder(dateField, range, idField, userId));

            SearchRequest request = new SearchRequest(indexName);
            request.source(builder);

            return request;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private static BoolQueryBuilder getSearchByUserIdAndDateRangeQueryBuilder(String dateField, DateRangeDto range, String idField, Integer userId)
    {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.rangeQuery(dateField).gte(range.getStartDate()).lte(range.getEndDate()));
        boolQuery.must(QueryBuilders.termQuery(idField, userId));
        return boolQuery;
    }

}
