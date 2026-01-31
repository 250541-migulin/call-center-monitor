package local.nca.callcenter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class CallcenterApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(CallcenterApplication.class, args);
	}
}