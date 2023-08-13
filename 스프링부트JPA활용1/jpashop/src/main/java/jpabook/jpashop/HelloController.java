package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello") //localhost:8080/hello
    public String hello(Model model){ //여기서 모델은 mvc의 모델
        model.addAttribute("data","hello!!!"); //모델에 key, value 추기
        return "hello"; //해당 모델을 기반으로 resource->templates->hello.html에서 렌더링 명렬(서버사이드 렌더링)
    }
}
