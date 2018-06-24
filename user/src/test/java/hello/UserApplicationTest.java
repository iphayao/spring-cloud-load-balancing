package hello;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApplicationTest {
    ConfigurableApplicationContext app1;
    ConfigurableApplicationContext app2;
    ConfigurableApplicationContext app3;

    @Before
    public void startApp() {
        this.app1 = startApp(8090);
        this.app2 = startApp(9092);
        this.app3 = startApp(9999);
    }

    @After
    public void claseApp() {
        this.app1.close();
        this.app2.close();
        this.app3.close();
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldRoundRibbinOverInstancesTest() {
        ResponseEntity<String> res1 = this.restTemplate.getForEntity("http://localhost:" + this.port + "/hi?name=foo", String.class);
        ResponseEntity<String> res2 = this.restTemplate.getForEntity("http://localhost:" + this.port + "/hi?name=foo", String.class);
        ResponseEntity<String> res3 = this.restTemplate.getForEntity("http://localhost:" + this.port + "/hi?name=foo", String.class);
        
        assertEquals(HttpStatus.OK, res1.getStatusCode());
        assertEquals("1, foo!", res1.getBody());
        assertEquals(HttpStatus.OK, res2.getStatusCode());
        assertEquals("2, foo!", res2.getBody());
        assertEquals(HttpStatus.OK, res3.getStatusCode());
        assertEquals("3, foo!", res3.getBody());
    }

	private ConfigurableApplicationContext startApp(int port) {
		return SpringApplication.run(TestApplication.class, "--server.port=" + port, "--spring.jmx.enabled=false");
    }
    

    @Configuration
    @RestController
    @EnableAutoConfiguration
    static class TestApplication {
        static AtomicInteger atomicInteger = new AtomicInteger();

        @RequestMapping(value = "/greeting")
        public Integer greet() {
            return atomicInteger.incrementAndGet();
        }

        @RequestMapping(value = "/")
        public String health() {
            return "ok";
        }
    }
}

 