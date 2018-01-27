package pl.lukpecyn.minigrant;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class CostTypeService {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public int addCostType(CostType costType) {
		String sql ="INSERT INTO cost_types(name,description) VALUES(?,?)";
		return jdbcTemplate.update(sql,costType.getName(),costType.getDescription());
	}
	
	public CostType getCostType(long idCostType) {
		return (CostType)jdbcTemplate.queryForObject("SELECT * FROM cost_types WHERE id=?", new Object[]{idCostType}, new RowMapper<CostType>(){
			public CostType mapRow(ResultSet rs, int arg1) throws SQLException {
				CostType ct = new CostType();
				ct.setId(rs.getInt("id"));
				ct.setName(rs.getString("name"));
				ct.setDescription(rs.getString("description"));
				return ct;
			}
		}); 		
	}

	public List<CostType> getCostTypeList() {
		return jdbcTemplate.query("SELECT * FROM cost_types ORDER BY id", new RowMapper<CostType>(){
			public CostType mapRow(ResultSet rs, int arg1) throws SQLException {
				CostType ct = new CostType();
				ct.setId(rs.getInt("id"));
				ct.setName(rs.getString("name"));
				ct.setDescription(rs.getString("description"));
				return ct;
			}
		}); 		
	}

}
