package pl.lukpecyn.minigrant.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import pl.lukpecyn.minigrant.model.Budget;

@Service
public class BudgetService {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	CostTypeService costTypeService;
	
	@Autowired
	PaymentService paymentService;
	
	public int addBudget(Budget budget) {
		String sql ="INSERT INTO budgets(grant_id,cost_type_id,description,dotation,contribution_own,contribution_personal,contribution_inkind) VALUES(?,?,?,?,?,?,?)";
		return jdbcTemplate.update(sql,budget.getIdGrant(),budget.getCostType().getId(),budget.getDescription(),budget.getDotation(),budget.getContributionOwn(),budget.getContributionPersonal(),budget.getContributionInkind());
	}

	public int updateBudget(Budget budget) {
		String sql ="UPDATE budgets SET grant_id=?, cost_type_id=?, description=?, dotation=?, contribution_own=?, contribution_personal=?, contribution_inkind=? WHERE id=?";
		return jdbcTemplate.update(sql,budget.getIdGrant(),budget.getCostType().getId(),budget.getDescription(),budget.getDotation(),budget.getContributionOwn(),budget.getContributionPersonal(),budget.getContributionInkind(), budget.getId());
	}

	public int deleteBudget(Integer idBudget) {
		String sql = "DELETE FROM budgets WHERE id=?";
		return jdbcTemplate.update(sql,idBudget);
	}

	public Budget getBudget(long idBudget) {
		return (Budget)jdbcTemplate.queryForObject("SELECT id,grant_id,cost_type_id,description,dotation,contribution_own,contribution_personal,contribution_inkind FROM budgets WHERE id=?", new Object[]{idBudget}, new RowMapper<Budget>(){
			public Budget mapRow(ResultSet rs, int arg1) throws SQLException {
				//CostType ct = costTypeService.getCostType(rs.getInt("cost_type_id"));
				Budget b = new Budget();
				b.setId(rs.getInt("id"));
				b.setIdGrant(rs.getInt("grant_id"));
				//b.setIdCostType(rs.getInt("cost_type_id"));
				b.setCostType(costTypeService.getCostType(rs.getInt("cost_type_id")));
				b.setDescription(rs.getString("description"));
				b.setDotation(rs.getBigDecimal("dotation"));
				b.setContributionOwn(rs.getBigDecimal("contribution_own"));
				b.setContributionPersonal(rs.getBigDecimal("contribution_personal"));
				b.setContributionInkind(rs.getBigDecimal("contribution_inkind"));
				return b;
			}
		}); 		
	}
	
	public List<Budget> getBudgetForGrantList(long idGrant) {
		return jdbcTemplate.query("SELECT id,grant_id,cost_type_id,description,dotation,contribution_own,contribution_personal,contribution_inkind FROM budgets WHERE grant_id=? ", new Object[]{idGrant}, new RowMapper<Budget>(){
		      public Budget mapRow(ResultSet rs, int arg1) throws SQLException {
		    	  	//CostType ct = costTypeService.getCostType(rs.getInt("cost_type_id"));
					Budget b = new Budget();
					b.setId(rs.getInt("id"));
					b.setIdGrant(rs.getInt("grant_id"));
					b.setCostType(costTypeService.getCostType(rs.getInt("cost_type_id")));
					b.setDescription(rs.getString("description"));
					b.setDotation(rs.getBigDecimal("dotation"));
					b.setContributionOwn(rs.getBigDecimal("contribution_own"));
					b.setContributionPersonal(rs.getBigDecimal("contribution_personal"));
					b.setContributionInkind(rs.getBigDecimal("contribution_inkind"));
					
					b.setPaidDotation(paymentService.getPaymentForBudgetDotationSum(b.getId()));
					b.setPaidContributionOwn(paymentService.getPaymentForBudgetContributionOwnSum(b.getId()));
					b.setPaidContributionPersonal(paymentService.getPaymentForBudgetContributionPersonalSum(b.getId()));
					b.setPaidContributionInkind(paymentService.getPaymentForBudgetContributionInkindSum(b.getId()));
					return b;
		      }
		    });
	}

}
