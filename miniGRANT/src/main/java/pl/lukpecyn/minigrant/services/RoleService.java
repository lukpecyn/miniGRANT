package pl.lukpecyn.minigrant.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import pl.lukpecyn.minigrant.models.Role;

@Service
public class RoleService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public int addRole(Role role) {
		String sql = "INSERT INTO authorities(username,authority) VALUES(?,?)";
	    return jdbcTemplate.update(sql, role.getUsername(), role.getRole());
	    
	}
	
	public List<Role> getAllRoles(){
	    return jdbcTemplate.query("SELECT * FROM authorities", new RowMapper<Role>(){

	      public Role mapRow(ResultSet rs, int arg1) throws SQLException {
	        Role r = new Role();
	        
	        r.setRole(rs.getString("authority"));
	        r.setUsername(rs.getString("username"));
	        return r;
	      }

	    });
	  }

	public Integer getCount() {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM authorities", Integer.class);
	}
}
