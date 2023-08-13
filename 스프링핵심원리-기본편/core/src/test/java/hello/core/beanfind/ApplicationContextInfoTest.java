package hello.core.beanfind;

import hello.core.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

 class ApplicationContextInfoTest {

    AnnotationConfigApplicationContext ac= new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean(){
        String[] beanDefinitionNames = ac.getBeanDefinitionNames(); //스프링에 등록된 모든 빈 이름을 조회
        for (String beanDefinitionName : beanDefinitionNames) { //각 빈에 대한 이름에 접근해서 각 빈을 가져와 출력
            Object bean = ac.getBean(beanDefinitionName); //빈 이름으로 빈 객체(인스턴스)를 조회
            System.out.println("name = " + beanDefinitionName+ " object = "+bean); //name은 key, object는 value

        }
    }


     @Test
     @DisplayName("애플리케이션 빈 출력하기")
     void findApplicationBean(){
         String[] beanDefinitionNames = ac.getBeanDefinitionNames(); //스프링에 등록된 모든 빈 이름을 조회
         for (String beanDefinitionName : beanDefinitionNames) { //각 빈에 대한 이름에 접근해서 각 빈을 가져와 출력

             BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);//빈 하나하나에 대한 메타데이터 정보 접근

             ////스프링 내부 빈이 아닌 내가 어플리케이션 만들면서 등록한 빈들만 찾기 위한 조건
             //Role ROLE_Application: 직접 등록한 애플리케이션 빈
             //Role ROLE_INFRASTRUCTURE: 스프링이 내부에서 사요하는 빈
             if(beanDefinition.getRole()== BeanDefinition.ROLE_APPLICATION) { //애플리케이션에 등록된 빈이라면
                 Object bean = ac.getBean(beanDefinitionName);
                 System.out.println("name = " + beanDefinitionName+ " object = "+bean); //name은 key, object는 value
             }
         }

         }

}
