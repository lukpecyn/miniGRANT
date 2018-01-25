package pl.lukpecyn.minigrant;

import java.net.SocketException;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MiniGrantApplication {

	@Value("${app.version}")
	public String appVersion;
	
	@Value("${app.name}")
	public String appName;
	
	private static final Logger logger = LoggerFactory.getLogger(MiniGrantApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MiniGrantApplication.class, args);
	}
	
	@PostConstruct
	public void init() throws UnknownHostException, SocketException {
		System.out.println(">=== " + appName + " v" + appVersion + " ===<");
		logger.info(appName + " v" + appVersion);
	}

}
