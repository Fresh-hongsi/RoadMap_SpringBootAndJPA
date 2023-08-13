package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing //이게 있어야 datajpa에서 수정일자, 생성일자, 생성자, 수정자 관리할 수 있음!!!!!!!
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}


	@Bean
	public AuditorAware<String> auditorProvider(){ //@CreatedBy, @LastModifiedBy 호출될때마다 현재 시스템 사용자를 uuid로 이벤트성으로 가져감
		return () -> Optional.of(UUID.randomUUID().toString()); //해당 시스템 사용자 식별
	}

}
