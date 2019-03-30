package pl.lukpecyn.minigrant.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import pl.lukpecyn.minigrant.models.Beneficiary;
import pl.lukpecyn.minigrant.models.Coworker;

@Service
public class CoworkerService {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired 
	BeneficiaryService beneficiaryService;

	public int addCoworker(Coworker coworker) {
		String sql ="INSERT INTO coworkers(username,beneficiary_id) VALUES(?,?)";
		return jdbcTemplate.update(sql,coworker.getUsername(),coworker.getIdBeneficiary());
	}
	
	public int delCoworker(Coworker coworker) {
		//TODO: Chceck if Beneficiary is used
		if (beneficiaryService.CoworkersCount(beneficiaryService.getBeneficiary(coworker.getIdBeneficiary()))>1) {
			String sql = "DELETE FROM coworkers WHERE username=? AND beneficiary_id=?";
			return jdbcTemplate.update(sql,coworker.getUsername(),coworker.getIdBeneficiary());
		} else {
			return -1;
		}
	}

	public List<Coworker> getCoworkerList(Integer idBeneficiary) {
		String sql = "SELECT username, beneficiary_id FROM coworkers WHERE benficiary_id=? ORDER BY username";
		return jdbcTemplate.query(sql, new Object[]{idBeneficiary}, new RowMapper<Coworker>(){
			public Coworker mapRow(ResultSet rs, int arg1) throws SQLException {
				Coworker coworker = new Coworker(rs.getString("username"),rs.getInt("beneficiary_id"));
				return coworker;
			}
		}); 		
	}

	public Integer ChceckCoworkerExists(Coworker coworker) {
		String sql = "SELECT COUNT(*) FROM coworkers WHERE username=? AND beneficiary_id=?";
		return (Integer)jdbcTemplate.queryForObject(sql, new Object[]{coworker.getUsername(),coworker.getIdBeneficiary()}, Integer.class);
	}

}
