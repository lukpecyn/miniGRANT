package pl.lukpecyn.minigrant.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import pl.lukpecyn.minigrant.models.Donor;

@Service
public class DonorService {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public int addDonor(Donor donor) {
		String sql ="INSERT INTO donors(name) VALUES(?)";
		return jdbcTemplate.update(sql,donor.getName());
	}

	public int updateDonor(Donor donor) {
		String sql = "UPDATE donors SET name=? WHERE id=?";
		return jdbcTemplate.update(sql,donor.getName(),donor.getId());
	}
	
	public int delDonor(Integer idDonor) {
		//TODO: Chceck if Donor is used
		String sql = "DELETE FROM donors WHERE id=?";
		return jdbcTemplate.update(sql,idDonor);
	}

	public Donor getDonor(long idDonor) {
		String sql = "SELECT * FROM donors WHERE id=?";
		return (Donor)jdbcTemplate.queryForObject(sql, new Object[]{idDonor}, new RowMapper<Donor>(){
			public Donor mapRow(ResultSet rs, int arg1) throws SQLException {
				Donor donor = new Donor(rs.getInt("id"), rs.getString("name"));
				return donor;
			}
		}); 		
	}

	public List<Donor> getDonorList() {
		return jdbcTemplate.query("SELECT * FROM donors ORDER BY name", new RowMapper<Donor>(){
			public Donor mapRow(ResultSet rs, int arg1) throws SQLException {
				Donor donor = new Donor(rs.getInt("id"),rs.getString("name"));
				return donor;
			}
		}); 		
	}
}
