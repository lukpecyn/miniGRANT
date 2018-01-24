package pl.lukpecyn.minigrant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class GrantService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

}
