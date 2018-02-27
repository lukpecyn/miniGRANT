package pl.lukpecyn.minigrant.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import pl.lukpecyn.minigrant.models.CostType;

@Service
public class CostTypeService {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public int addCostType(CostType costType) {
		String sql ="INSERT INTO cost_types(name,description,beneficiary_id) VALUES(?,?,?)";
		return jdbcTemplate.update(sql,costType.getName(),costType.getDescription(),costType.getIdBeneficiary());
	}

	public int updateCostType(CostType costType) {
		String sql = "UPDATE cost_types SET name=?,description=?,beneficiary_id=? WHERE id=?";
		return jdbcTemplate.update(sql,costType.getName(),costType.getDescription(),costType.getIdBeneficiary(),costType.getId());
	}
	
	public int delCostType(Integer id) {
		//TODO: Chceck if CostType is used
		String sql = "DELETE FROM cost_types WHERE id=?";
		return jdbcTemplate.update(sql,id);
	}

	public CostType getCostType(long idCostType) {
		return (CostType)jdbcTemplate.queryForObject("SELECT * FROM cost_types WHERE id=?", new Object[]{idCostType}, new RowMapper<CostType>(){
			public CostType mapRow(ResultSet rs, int arg1) throws SQLException {
				CostType ct = new CostType();
				ct.setId(rs.getInt("id"));
				ct.setName(rs.getString("name"));
				ct.setDescription(rs.getString("description"));
				ct.setIdBeneficiary(rs.getInt("beneficiary_id"));
				return ct;
			}
		}); 		
	}

	public List<CostType> getCostTypeList(long idBeneficiary) {
		return jdbcTemplate.query("SELECT * FROM cost_types WHERE beneficiary_id=? ORDER BY name", new Object[]{idBeneficiary}, new RowMapper<CostType>(){
			public CostType mapRow(ResultSet rs, int arg1) throws SQLException {
				CostType ct = new CostType();
				ct.setId(rs.getInt("id"));
				ct.setName(rs.getString("name"));
				ct.setDescription(rs.getString("description"));
				ct.setIdBeneficiary(rs.getInt("beneficiary_id"));
				return ct;
			}
		}); 		
	}
}
