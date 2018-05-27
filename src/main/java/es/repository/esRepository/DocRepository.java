package es.repository.esRepository;

import es.entity.esEntity.DocEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

/**
 * Created by TYF on 2018/1/25.
 */
@Repository
public interface DocRepository extends ElasticsearchRepository<DocEntity, String> {

}
