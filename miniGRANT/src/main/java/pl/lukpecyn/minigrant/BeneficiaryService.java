package pl.lukpecyn.minigrant;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

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

	public List<Beneficiary> getBeneficiaryList() {
		return jdbcTemplate.query("SELECT * FROM beneficiaries ORDER BY name", new RowMapper<Beneficiary>(){
			public Beneficiary mapRow(ResultSet rs, int arg1) throws SQLException {
				Beneficiary beneficiary = new Beneficiary(rs.getInt("id"),rs.getString("name"));
				return beneficiary;
			}
		}); 		
	}
}
