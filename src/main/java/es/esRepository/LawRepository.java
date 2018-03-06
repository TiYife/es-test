package es.esRepository;

import es.entity.LawEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by TYF on 2018/1/25.
 */
@Repository
public interface LawRepository extends ElasticsearchRepository<LawEntity, Long> {


}
