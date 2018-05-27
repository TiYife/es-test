package es.service.impl;

import es.entity.esEntity.DocEntity;
import es.entity.jpaEntity.FavoriteEntity;
import es.repository.esRepository.DocRepository;
import es.repository.jpaRepository.FavoriteRepository;
import es.service.SearchService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


import static es.Constant.timeFormat;
import static es.service.impl.SearchServiceImpl.SearchType.and;
import static es.service.impl.SearchServiceImpl.SearchType.or;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

/**
 * Created by TYF on 2018/1/29.
 */
@Service
public class SearchServiceImpl implements SearchService {
    /*日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);

    /* 分页参数 */
    private int pageSize = 12;          // 每页数量
    private int defaultPageNumber = 0; // 默认当前页码
    /* 搜索模式 */
    private String scoreModeSum = "sum"; // 权重分求和模式
    private Float minScore = 10.0F;      // 由于无相关性的分值默认为 1 ，设置权重分最小值为 10

    public enum SearchType {or, and, not}    ;

    @Autowired
    DocRepository docRepository;
    @Autowired
    FavoriteRepository favoriteRepository;

    @Override
    public List<DocEntity> searchLaw(Integer pageNumber, Integer pageSize, String searchAttr, String searchKeyWord) {
        // 校验分页参数
        if (pageSize == null || pageSize <= 0) {
            pageSize = this.pageSize;
        }
        if (pageNumber == null || pageNumber < defaultPageNumber) {
            pageNumber = defaultPageNumber;
        }
        // 构建搜索查询
        SearchQuery searchQuery = getSimpleSearchQuery(pageNumber, pageSize, searchAttr, searchKeyWord);
        LOGGER.info(
                "\nsearch: \n" +
                        "\tsearchAttr =" + searchAttr + "\n" +
                        "\tsearchKeyWord =" + searchKeyWord + " \n " +
                        "DSL  = \n " + searchQuery.getQuery().toString());
        Page<DocEntity> lawPage = docRepository.search(searchQuery);
        return lawPage.getContent();
    }


    //@Override
    public List<DocEntity> allLaw() {

        return docRepository.findAll();
    }

    @Override
    public List<DocEntity> multiSearch(Integer pageNumber, Integer pageSize, JSONArray json) throws JSONException {
        if (pageSize == null || pageSize <= 0) {
            pageSize = this.pageSize;
        }
        if (pageNumber == null || pageNumber < defaultPageNumber) {
            pageNumber = defaultPageNumber;
        }
        SearchQuery searchQuery = getMultiSearchQuery(pageNumber,pageSize,json);
        Page<DocEntity> lawPage = docRepository.search(searchQuery);
        return lawPage.getContent();
    }

    @Override
    public List<DocEntity> similarSearch(Integer pageNumber, Integer pageSize, String searchContent){
        // 校验分页参数
        if (pageSize == null || pageSize <= 0) {
            pageSize = this.pageSize;
        }
        if (pageNumber == null || pageNumber < defaultPageNumber) {
            pageNumber = defaultPageNumber;
        }
        // 构建搜索查询
        SearchQuery searchQuery = getSimilarSearchQuery(pageNumber, pageSize,searchContent);
        Page<DocEntity> lawPage = docRepository.search(searchQuery);
        return lawPage.getContent();
    }

    @Override
    public void favorDoc(int userId, String docId){
        DocEntity docEntity = docRepository.findOne(docId);
        FavoriteEntity favoriteEntity = new FavoriteEntity();
        favoriteEntity.setUserId(userId);
        favoriteEntity.setDocId(docEntity.getDocId());
        favoriteEntity.setDocName(docEntity.getCaseName());
        favoriteEntity.setFavorTime(timeFormat.format(new Date()));
        favoriteRepository.save(favoriteEntity);
    }

    @Override
    public List<FavoriteEntity> listFavorDocs(int userId){
        return favoriteRepository.findByUserId(userId);
    }

    /**
     * 根据搜索词构造搜索查询语句
     * <p>
     * 代码流程：
     * - 权重分查询
     * - 短语匹配
     * - 设置权重分最小值
     * - 设置分页参数
     *
     * @param pageNumber    当前页码
     * @param pageSize      每页大小
     * @param searchKeyWord 搜索内容
     * @return
     */
    private SearchQuery getSimpleSearchQuery(Integer pageNumber, Integer pageSize, String searchAttr, String searchKeyWord) {
        // 短语匹配到的搜索词，求和模式累加权重分
        // 权重分查询 https://www.elastic.co/guide/c ... .html
        //   - 短语匹配 https://www.elastic.co/guide/c ... .html
        //   - 字段对应权重分设置，可以优化成 enum
        //   - 由于无相关性的分值默认为 1 ，设置权重分最小值为 10
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
                .add(boolQuery().should(QueryBuilders.matchQuery(searchAttr, searchKeyWord)),
                        ScoreFunctionBuilders.weightFactorFunction(1000))
                .scoreMode(scoreModeSum).setMinScore(minScore);
        // 分页参数
        Pageable pageable = new PageRequest(pageNumber, pageSize);
        return new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(functionScoreQueryBuilder).build();
    }

    private SearchQuery getMultiSearchQuery(Integer pageNumber, Integer pageSize, JSONArray json) throws JSONException {
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery();
        if (json.length() < 0)
            return null;
        for (int i = 0; i < json.length(); i++) {
            JSONObject jsonObject = json.getJSONObject(i);
            String searchAttr = jsonObject.get("attr").toString();
            String searchKeyWord = jsonObject.get("keyword").toString();
            SearchType searchType = SearchType.valueOf(jsonObject.get("type").toString());
            int weight = jsonObject.getInt("weight");
            functionScoreQueryBuilder =
                    functionScoreQueryBuilder.add(getSubMultiSearchQuery(searchAttr, searchKeyWord, searchType),
                            ScoreFunctionBuilders.weightFactorFunction(100 * weight));
        }

        functionScoreQueryBuilder.scoreMode(scoreModeSum).setMinScore(minScore);
        Pageable pageable = new PageRequest(pageNumber, pageSize);
        return new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(functionScoreQueryBuilder).build();

    }

    private BoolQueryBuilder getSubMultiSearchQuery(String searchAttr, String searchContent, SearchType searchType) {
        BoolQueryBuilder boolQueryBuilder;
        if (searchType == or) {
            boolQueryBuilder = boolQuery().should(QueryBuilders.matchQuery(searchAttr, searchContent));
        } else if (searchType == and) {
            boolQueryBuilder = boolQuery().must(QueryBuilders.matchQuery(searchAttr, searchContent));
        } else {
            boolQueryBuilder = boolQuery().mustNot(QueryBuilders.matchQuery(searchAttr, searchContent));
        }
        return boolQueryBuilder;
    }

    private SearchQuery getSimilarSearchQuery(Integer pageNumber, Integer pageSize, String searchContent){
        QueryBuilder queryBuilder = QueryBuilders.moreLikeThisQuery().like(searchContent);
        Pageable pageable = new PageRequest(pageNumber, pageSize);
        return new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(queryBuilder).build();
    }
}

