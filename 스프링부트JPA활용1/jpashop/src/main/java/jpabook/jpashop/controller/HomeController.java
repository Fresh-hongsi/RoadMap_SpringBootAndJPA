package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j //롬복 통한 SLF4J 로거
public class HomeController {

    @RequestMapping("/") //홈 화면
    public String home(){
        log.info("home controller");
        return "home"; //home.html파일 찾음
    }
}
