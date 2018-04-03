package es.service.impl;

import es.entity.DocEntity;
import es.esRepository.DocRepository;
import es.service.SearchService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    DocRepository lawRepository;

    @Override
    public List<DocEntity> searchLaw(Integer pageNumber, Integer pageSize, String searchAttr, String searchContent) {
        // 校验分页参数
        if (pageSize == null || pageSize <= 0) {
            pageSize = this.pageSize;
        }
        if (pageNumber == null || pageNumber < defaultPageNumber) {
            pageNumber = defaultPageNumber;
        }
        // 构建搜索查询
        SearchQuery searchQuery = getCitySearchQuery(pageNumber,pageSize,searchAttr,searchContent);
        LOGGER.info(
                "\nsearch: \n" +
                "\tsearchAttr =" + searchAttr + "\n" +
                "\tsearchContent =" + searchContent + " \n " +
                "DSL  = \n " + searchQuery.getQuery().toString());
        Page<DocEntity> lawPage = lawRepository.search(searchQuery);
        return lawPage.getContent();
    }
    /**
     * 根据搜索词构造搜索查询语句
     *
     * 代码流程：
     *      - 权重分查询
     *      - 短语匹配
     *      - 设置权重分最小值
     *      - 设置分页参数
     *
     * @param pageNumber 当前页码
     * @param pageSize 每页大小
     * @param searchContent 搜索内容
     * @return
     */
    private SearchQuery getCitySearchQuery(Integer pageNumber, Integer pageSize, String searchAttr, String searchContent) {
        // 短语匹配到的搜索词，求和模式累加权重分
        // 权重分查询 https://www.elastic.co/guide/c ... .html
        //   - 短语匹配 https://www.elastic.co/guide/c ... .html
        //   - 字段对应权重分设置，可以优化成 enum
        //   - 由于无相关性的分值默认为 1 ，设置权重分最小值为 10
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
                .add(QueryBuilders.matchPhraseQuery(searchAttr, searchContent),
                        ScoreFunctionBuilders.weightFactorFunction(1000))
                .scoreMode(scoreModeSum).setMinScore(minScore);
        // 分页参数
        Pageable pageable = new PageRequest(pageNumber, pageSize);
        return new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(functionScoreQueryBuilder).build();
    }
}

