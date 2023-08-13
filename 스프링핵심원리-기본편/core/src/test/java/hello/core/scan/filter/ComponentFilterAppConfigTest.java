package hello.core.scan.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import static org.assertj.core.api.Assertions.assertThat; //이 방식으로써야함!!!!
public class ComponentFilterAppConfigTest {

    @Test
    void filterScan() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(ComponentFilterAppConfig.class);
        BeanA beanA = ac.getBean("beanA", BeanA.class);
        assertThat(beanA).isNotNull(); //컴포넌트 스캔에 beanA는 include되었으므로 스프링 컨테이너에 올라가있어야함-> not null이어야함

        //BeanB beanB = ac.getBean("beanB", BeanB.class); //컴포넌트 스캔에 beanB는 exclude되었으므로 스프링 컨테이너에 없어야함->  null이어야함
        //beanB를 가져오려고 했을 떄 NoSuch~~이 Exception이 터지면 테케 통과
        Assertions.assertThrows(
                NoSuchBeanDefinitionException.class, ()-> ac.getBean("beanB", BeanB.class)
        );
    }

    @Configuration
    //@MyIncludeComponent붙은 애는 스캔 대상에 추가
    //@MyExcludeComponent붙은 애는 스캔 대상에서 제외
    @ComponentScan(
            includeFilters = @Filter(type= FilterType.ANNOTATION,classes = MyIncludeComponent.class),
            excludeFilters = @Filter(type=FilterType.ANNOTATION,classes=MyExcludeComponent.class)
    )
    static class ComponentFilterAppConfig{ //이 테스트 예제에서 사용할 appConfig 클래스를 별도로 생성, 그 후 컴포넌트 스캔 지시

    }
}
