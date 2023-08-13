package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Hello;
import study.querydsl.entity.QHello;
import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
@Commit //이게 있으면 db에서 롤백안함
class QuerydslApplicationTests {

	@Autowired //@PersistenceContext가 표준인데, @Autowired써도 됨
	EntityManager em;


	@Test
	void contextLoads() {
		Hello hello = new Hello();
		em.persist(hello);

		//쿼리 dsl 사용해보기
		JPAQueryFactory query = new JPAQueryFactory(em); //쿼리 dsl사용하려면 이런식으로 em을 parameter로 넣어줘야한다
		QHello qHello = QHello.hello;

		Hello result = query.selectFrom(qHello)
				.fetchOne();//Hello객체 중 첫번째 row 가져와라

		assertThat(result).isEqualTo(hello);
		assertThat(result.getId()).isEqualTo(hello.getId());
	}

}
