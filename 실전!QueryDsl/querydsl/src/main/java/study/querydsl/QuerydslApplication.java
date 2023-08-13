package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;

@SpringBootApplication
public class QuerydslApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuerydslApplication.class, args);
	}

//	//여기에 아예 queryfactory를 스프링 빈으로 등록할 수도 있음
	@Bean
	JPAQueryFactory jpaQueryFactory(EntityManager em) //엔티티 매니저는 스프링 빈에서 자동으로 빈에 등록시켜줌
	{
		return new JPAQueryFactory(em);
	}

}
