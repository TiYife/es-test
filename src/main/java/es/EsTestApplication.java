package es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("es.repository.jpaRepository")
@EnableElasticsearchRepositories("es.repository.esRepository")
public class EsTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(EsTestApplication.class, args);
	}
}
