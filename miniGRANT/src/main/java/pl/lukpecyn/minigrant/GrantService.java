package pl.lukpecyn.minigrant;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class GrantService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int addGrant(Grant grant) {
		String sql = "INSERT INTO grants(name,date_begin,date_end,status) VALUES(?,?,?,?)";
		return jdbcTemplate.update(sql,grant.getName(),grant.getDateBegin(),grant.getDateEnd(),grant.getStatus());
	}
	
	public int updateGrant(Grant grant) {
		String sql = "UPDATE grants SET name=?,date_begin=?,date_end=?,status=? WHERE id=?";
		return jdbcTemplate.update(sql,grant.getName(),grant.getDateBegin(),grant.getDateEnd(),grant.getStatus(),grant.getId());
	}
	
	public int delGrant(Integer id) {
		String sql = "DELETE FROM grants where id=?";
		return jdbcTemplate.update(sql,id);
	}

	public Grant getGrant(long idGrant) {
		return (Grant)jdbcTemplate.queryForObject("SELECT * FROM grants WHERE id=?", new Object[]{idGrant}, new RowMapper<Grant>(){
			public Grant mapRow(ResultSet rs, int arg1) throws SQLException {
				Grant g = new Grant();
				g.setId(rs.getInt("id"));
				g.setName(rs.getString("name"));
				g.setDateBegin(rs.getString("date_begin"));
				g.setDateEnd(rs.getString("date_end"));
				g.setStatus(rs.getInt("status"));
				return g;
			}
		}); 		
	}
	
	public List<Grant> getGrantsListByDateBegin() {
		return jdbcTemplate.query("SELECT * FROM grants ORDER BY date_begin DESC,name", new RowMapper<Grant>(){
		      public Grant mapRow(ResultSet rs, int arg1) throws SQLException {
		        Grant g = new Grant();
		        g.setId(rs.getInt("id"));
		        g.setName(rs.getString("name"));
		        g.setDateBegin(rs.getString("date_begin"));
		        g.setDateEnd(rs.getString("date_end"));
		        g.setStatus(rs.getInt("status"));
		        return g;
		      }
		    });
	}
	
	public List<Grant> getGrantsListByName() {
		return jdbcTemplate.query("SELECT * FROM grants ORDER BY name", new RowMapper<Grant>(){
		      public Grant mapRow(ResultSet rs, int arg1) throws SQLException {
		        Grant g = new Grant();
		        g.setId(rs.getInt("id"));
		        g.setName(rs.getString("name"));
		        g.setDateBegin(rs.getString("date_begin"));
		        g.setDateEnd(rs.getString("date_end"));
		        g.setStatus(rs.getInt("status"));
		        return g;
		      }
		    });
	}
	
}
