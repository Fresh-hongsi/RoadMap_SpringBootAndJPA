package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // 스프링 프로젝트의 Controller클래스에는 이 어노테이션을 반드시 써줘야함
public class HelloController {

    @GetMapping("hello") // '/hello'라는 경로로 들어오면 아래 hello 메서드 호출
    public String hello(Model model)
    {
        model.addAttribute("data","hello!!");
        return "hello"; //꼭 getMapping의 hello와는 똑같지 않아도 됨 적어도 html파일이름인 hello.html과는 이름이 동일해야함
        //그러면 컨트롤러는 resource의 template의 hello.html파일에게 해당 모델에 대한 데이터를 가지고 렌더링하라고 명령함
    }

    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model)
    //@requestParam을 해서 키 name에 대한 값인 spring을 string name에 저장
    {
        model.addAttribute("name",name); //모델에 name이란 키를 만들고, 그에 대한 값을 spring으로 저장하게 됨
        return "hello-template"; //타임리프 템플릿 엔진에 렌더링 요청
    }

    @GetMapping("hello-string")
    @ResponseBody //html의 body가 아닌, http통신 프로토콜에서의 헤더,바디 중 바디에 이 데이터를 직접 넣어주겠다는 뜻
    public String helloString(@RequestParam("name") String name)
    {
        return "hello "+ name; //이 문자열이 바로 화면에 내려짐(html태그가 하나도 없는 방식으로 내려짐)
    }


    @GetMapping("hello-api") //api방식 -> json형식으로 내려짐
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name){
        Hello hello = new Hello();
        hello.setName(name); //파라미터로 받은 key name에 해당하는 값을 hello객체에 저장
        return hello;

    }

    static class Hello {
        private String name;


        public String getName() //class Hello의 getter함수
        {
            return name;
        }

        public void setName(String name)
        {
            this.name=name;
        }

    }
}
