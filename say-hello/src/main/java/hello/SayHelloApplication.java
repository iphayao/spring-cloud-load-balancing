package hello;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SayHelloApplication {
    private static Logger log = LoggerFactory.getLogger(SayHelloApplication.class);

    @RequestMapping(value = "/greeting")
    public String greet() {
        log.info("Access /greeting");

        List<String> greeting = Arrays.asList("Hi there", "Greetings", "Satutations");
        Random rnd = new Random();

        int rndNum = rnd.nextInt(greeting.size());
        return greeting.get(rndNum);
    }

    @RequestMapping(value = "/")
    public String home() {
        log.info("Access /");
        return "Hi!";
    }

    public static void main(String[] args) {
        SpringApplication.run(SayHelloApplication.class, args);
    }
}