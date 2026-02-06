package local.nca.callcenter;

import local.nca.callcenter.asterisk.application.facade.AsteriskFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class CallcenterApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(CallcenterApplication.class, args);

		AsteriskFacade facade = context.getBean(AsteriskFacade.class);
		facade.startMonitoring();
	}
}