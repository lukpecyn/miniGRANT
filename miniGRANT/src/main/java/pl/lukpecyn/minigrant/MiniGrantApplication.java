package pl.lukpecyn.minigrant;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
public class MiniGrantApplication {

	@Value("${app.version}")
	public String appVersion;
	
	@Value("${app.name}")
	public String appName;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(MiniGrantApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MiniGrantApplication.class, args);
	}
	
	@PostConstruct
	public void init() throws UnknownHostException, SocketException {
		System.out.println(">=== " + appName + " ver. " + appVersion + " ===<");
		logger.info(appName + " v" + appVersion);
	}

	@PreDestroy
	@Scheduled(cron="0 0 0 * * *")
	public void backupDB() {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.HHmmss");
		String backupPath = "./backup/db_" + sdf.format(date) + "_" + appVersion +".tgz";
		String sql = "BACKUP DATABASE TO '" + backupPath + "' BLOCKING";
		jdbcTemplate.execute(sql);
	}
	
/*	
	@Scheduled(fixedDelay=60000)
	public void test() {
		String sql = "SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE, TYPE_NAME, COLUMN_SIZE,DECIMAL_DIGITS FROM INFORMATION_SCHEMA.SYSTEM_COLUMNS WHERE TABLE_NAME='DOCUMENTS'";
		jdbcTemplate.query(sql, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int arg1) throws SQLException {
				System.out.println(rs.getString("TABLE_NAME") 
						+ " - " + rs.getString("COLUMN_NAME")
						+ " - " + rs.getString("TYPE_NAME")
						+ " - " + rs.getString("COLUMN_SIZE")
						+ " - " + rs.getString("DECIMAL_DIGITS"));
				return rs.getString("TABLE_NAME") + " - " + rs.getString("COLUMN_NAME");
			}
		});
	}
*/
}
