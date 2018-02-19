package pl.lukpecyn.minigrant.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import pl.lukpecyn.minigrant.controllers.SecurityController;
import pl.lukpecyn.minigrant.models.Role;
import pl.lukpecyn.minigrant.models.User;

@Service
public class UserService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	public int addUser(User user) {
		String sql_user = "INSERT INTO users(username,password,fullname,email,guid,confirmed,enabled) VALUES(?,?,?,?,?,?,?)";
	    jdbcTemplate.update(sql_user, user.getUsername(),user.getPassword(),user.getFullname(),user.getEmail(),user.getGuid(),user.getConfirmed(),user.getEnabled());
		
	    String sql_role = "INSERT INTO authorities(username,authority) VALUES(?,?)";
	    return jdbcTemplate.update(sql_role, user.getUsername(),user.getRole().getRole());
	}
	
	public int updateUser(User user) {
		String sql_user = "UPDATE users SET password=?,fullname=?,email=?,guid=?,confirmed=?,enabled=? WHERE username=?";
	    return jdbcTemplate.update(sql_user, user.getPassword(),user.getFullname(),user.getEmail(),user.getGuid(),user.getConfirmed(),user.getEnabled(),user.getUsername());
	}
	
	public List<User> getAllUser(){
		return jdbcTemplate.query("SELECT users.username,users.fullname,users.fullname,users.email,users.guid,users.confirmed,users.enabled,authorities.authority FROM users LEFT JOIN authorities ON users.username=authorities.username", new RowMapper<User>(){
	      public User mapRow(ResultSet rs, int arg1) throws SQLException {
	  		User u = new User();
	        Role r = new Role();
	        u.setUsername(rs.getString("username"));
	        u.setFullname(rs.getString("fullname"));
	        u.setEmail(rs.getString("email"));
	        u.setGuid(UUID.fromString(rs.getString("guid")));
	        u.setConfirmed(rs.getBoolean("confirmed"));
	        u.setEnabled(rs.getBoolean("enabled"));
	        u.setRole(new Role(rs.getString("username"),rs.getString("authority")));
	        return u;
	      }

	    });
	  }

	public User getUser(String username) {
		return (User)jdbcTemplate.queryForObject("SELECT * FROM users WHERE username=?", new Object[] {username}, new RowMapper<User>(){

		      public User mapRow(ResultSet rs, int arg1) throws SQLException {
		        User u = new User();
		        Role r = new Role();
		        u.setUsername(rs.getString("username"));
		        u.setPassword(rs.getString("password"));
		        u.setFullname(rs.getString("fullname"));
		        u.setEmail(rs.getString("email"));
		        u.setGuid(UUID.fromString(rs.getString("guid")));
		        u.setConfirmed(rs.getBoolean("confirmed"));
		        u.setEnabled(rs.getBoolean("enabled"));
		        return u;
		      }
		    });
	}

	public User getUser(UUID guid) {
		return (User)jdbcTemplate.queryForObject("SELECT * FROM users WHERE guid=?", new Object[] {guid}, new RowMapper<User>(){

		      public User mapRow(ResultSet rs, int arg1) throws SQLException {
		        User u = new User();
		        Role r = new Role();
		        u.setUsername(rs.getString("username"));
		        u.setPassword(rs.getString("password"));
		        u.setFullname(rs.getString("fullname"));
		        u.setEmail(rs.getString("email"));
		        u.setGuid(UUID.fromString(rs.getString("guid")));
		        u.setConfirmed(rs.getBoolean("confirmed"));
		        u.setEnabled(rs.getBoolean("enabled"));
		        return u;
		      }
		    });
	}

	public Integer getCount() {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
	}

	public Integer ChceckUsernameExists(String username) {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users WHERE username=?", new Object[]{username}, Integer.class);
	}

	public Integer ChceckEmailExists(String email) {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users WHERE email=?", new Object[]{email}, Integer.class);
	}

}
