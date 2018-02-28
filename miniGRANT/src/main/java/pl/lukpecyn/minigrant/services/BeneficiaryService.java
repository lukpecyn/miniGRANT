package pl.lukpecyn.minigrant.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import pl.lukpecyn.minigrant.models.Beneficiary;

@Service
public class BeneficiaryService {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public int addBeneficiary(Beneficiary beneficiary) {
		String sql ="INSERT INTO beneficiaries(name) VALUES(?)";
		return jdbcTemplate.update(sql,beneficiary.getName());
	}

	public int updateBeneficiary(Beneficiary beneficiary) {
		String sql = "UPDATE beneficiaries SET name=? WHERE id=?";
		return jdbcTemplate.update(sql,beneficiary.getName(),beneficiary.getId());
	}
	
	public int delBeneficiary(Integer idBeneficiary) {
		//TODO: Chceck if Beneficiary is used
		String sql = "DELETE FROM beneficiaries WHERE id=?";
		return jdbcTemplate.update(sql,idBeneficiary);
	}

	public Beneficiary getBeneficiary(long idBeneficiary) {
		String sql = "SELECT * FROM beneficiaries WHERE id=?";
		return (Beneficiary)jdbcTemplate.queryForObject(sql, new Object[]{idBeneficiary}, new RowMapper<Beneficiary>(){
			public Beneficiary mapRow(ResultSet rs, int arg1) throws SQLException {
				Beneficiary beneficiary = new Beneficiary(rs.getInt("id"), rs.getString("name"));
				return beneficiary;
			}
		}); 		
	}

	public Beneficiary getBeneficiaryByName(String beneficiaryName) {
		String sql = "SELECT * FROM beneficiaries WHERE name=?";
		return (Beneficiary)jdbcTemplate.queryForObject(sql, new Object[]{beneficiaryName}, new RowMapper<Beneficiary>(){
			public Beneficiary mapRow(ResultSet rs, int arg1) throws SQLException {
				Beneficiary beneficiary = new Beneficiary(rs.getInt("id"), rs.getString("name"));
				return beneficiary;
			}
		}); 		
	}

	public List<Beneficiary> getBeneficiaryList(String username) {
		//return jdbcTemplate.query("SELECT * FROM beneficiaries ORDER BY name", new RowMapper<Beneficiary>(){
		return jdbcTemplate.query("SELECT beneficiaries.id, beneficiaries.name FROM coworkers LEFT JOIN beneficiaries ON coworkers.beneficiary_id=beneficiaries.id WHERE coworkers.username=? ORDER BY beneficiaries.name", new Object[]{username}, new RowMapper<Beneficiary>(){
			public Beneficiary mapRow(ResultSet rs, int arg1) throws SQLException {
				Beneficiary beneficiary = new Beneficiary(rs.getInt("id"),rs.getString("name"));
				return beneficiary;
			}
		}); 		
	}
	
	public int connectUser(Beneficiary beneficiary,String username) {
		String sql ="INSERT INTO coworkers(username,beneficiary_id) VALUES(?,?)";
		return jdbcTemplate.update(sql,username,beneficiary.getId());
	}
	
	public Integer checkUser(Beneficiary beneficiary, String username) {
		return (Integer)jdbcTemplate.queryForObject("SELECT COUNT(*) AS count FROM coworkers WHERE username=? AND beneficiary_id=?", new Object[]{username,beneficiary.getId()}, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getInt("count");
			}
		});
	}
}
