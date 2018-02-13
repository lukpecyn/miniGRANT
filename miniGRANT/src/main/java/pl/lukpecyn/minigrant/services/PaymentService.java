package pl.lukpecyn.minigrant.services;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import pl.lukpecyn.minigrant.controllers.PaymentController;
import pl.lukpecyn.minigrant.models.Payment;

@Service
public class  PaymentService {
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	BudgetService budgetService;
	
	@Autowired
	DocumentService documentService;
	
	public int addPayment(Payment payment) {

		logger.debug("payment id            = " + payment.getId());
		logger.debug("Budget id             = " + payment.getBudget().getId());
		logger.debug("Document id           = " + payment.getDocument().getId());
		logger.debug("Dotation              = " + payment.getDotation());
		logger.debug("Contribution own      = " + payment.getContributionOwn());
		logger.debug("Contribution personal = " + payment.getContributionPersonal());
		logger.debug("Contribution in-kind  = " + payment.getContributionInkind());
		
		String sql ="INSERT INTO payments(budget_id,document_id,dotation,contribution_own,contribution_personal,contribution_inkind) VALUES(?,?,?,?,?,?)";
		return jdbcTemplate.update(sql,payment.getBudget().getId(),payment.getDocument().getId(),payment.getDotation(),payment.getContributionOwn(),payment.getContributionPersonal(),payment.getContributionInkind());
	}

	public int updatePayment(Payment payment) {
		String sql = "UPDATE payments SET budget_id=?, document_id=?, dotation=?, contribution_own=?, contribution_personal=?, contribution_inkind=? WHERE id=?";
		return jdbcTemplate.update(sql,payment.getBudget().getId(),payment.getDocument().getId(),payment.getDotation(),payment.getContributionOwn(),payment.getContributionPersonal(),payment.getContributionInkind(),payment.getId());
	}
	
	public int deletePayment(Integer idPayment) {
		String sql = "DELETE FROM payments WHERE id=?";
		return jdbcTemplate.update(sql,idPayment);
	}

	public Payment getPayment(long idPayment) {
		return (Payment)jdbcTemplate.queryForObject("SELECT * FROM payments WHERE id=?", new Object[]{idPayment}, new RowMapper<Payment>(){
			public Payment mapRow(ResultSet rs, int arg1) throws SQLException {
				Payment p = new Payment();
				p.setId(rs.getInt("id"));
				p.setBudget(budgetService.getBudget(rs.getInt("budget_id")));
				p.setDocument(documentService.getDocument(rs.getInt("document_id")));
				p.setDotation(rs.getBigDecimal("dotation"));
				p.setContributionOwn(rs.getBigDecimal("contribution_own"));
				p.setContributionPersonal(rs.getBigDecimal("contribution_personal"));
				p.setContributionInkind(rs.getBigDecimal("contribution_inkind"));
				return p;
			}
		}); 		
	}
	
	public List<Payment> getPaymentForDocumentList(long idDocument) {
		String sql = "SELECT * FROM payments WHERE document_id=?";
		return jdbcTemplate.query(sql, new Object[]{idDocument}, new RowMapper<Payment>(){
		      public Payment mapRow(ResultSet rs, int arg1) throws SQLException {
					Payment p = new Payment();
					p.setId(rs.getInt("id"));
					p.setBudget(budgetService.getBudget(rs.getInt("budget_id")));
					p.setDocument(documentService.getDocument(rs.getInt("document_id")));
					p.setDotation(rs.getBigDecimal("dotation"));
					p.setContributionOwn(rs.getBigDecimal("contribution_own"));
					p.setContributionPersonal(rs.getBigDecimal("contribution_personal"));
					p.setContributionInkind(rs.getBigDecimal("contribution_inkind"));
					return p;
		      }
		    });
	}
	
	public Integer getPaymentForDocumentCount(long idDocument) {
		String sql = "SELECT COUNT(*) AS count FROM payments WHERE document_id=?";
		return (Integer)jdbcTemplate.queryForObject(sql, new Object[]{idDocument}, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getInt("count");
			}
		});
	}

	public BigDecimal getPaymentForDocumentDotationSum(long idDocument) {
		String sql = "SELECT COALESCE(SUM(dotation),0) AS dotation_sum FROM payments WHERE document_id=?";
		return (BigDecimal)jdbcTemplate.queryForObject(sql, new Object[]{idDocument}, new RowMapper<BigDecimal>() {
			public BigDecimal mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getBigDecimal("dotation_sum");
			}
		});
	}

	public BigDecimal getPaymentForDocumentContributionOwnSum(long idDocument) {
		String sql = "SELECT COALESCE(SUM(contribution_own),0) AS contribution_own_sum FROM payments WHERE document_id=?";
		return (BigDecimal)jdbcTemplate.queryForObject(sql, new Object[]{idDocument}, new RowMapper<BigDecimal>() {
			public BigDecimal mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getBigDecimal("contribution_own_sum");
			}
		});
	}

	public BigDecimal getPaymentForDocumentContributionPersonalSum(long idDocument) {
		String sql = "SELECT COALESCE(SUM(contribution_personal),0) AS contribution_personal_sum FROM payments WHERE document_id=?";
		return (BigDecimal)jdbcTemplate.queryForObject(sql, new Object[]{idDocument}, new RowMapper<BigDecimal>() {
			public BigDecimal mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getBigDecimal("contribution_personal_sum");
			}
		});
	}
	
	public BigDecimal getPaymentForDocumentContributionInkindSum(long idDocument) {
		String sql = "SELECT COALESCE(SUM(contribution_inkind),0) AS contribution_inkind_sum FROM payments WHERE document_id=?";
		return (BigDecimal)jdbcTemplate.queryForObject(sql, new Object[]{idDocument}, new RowMapper<BigDecimal>() {
			public BigDecimal mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getBigDecimal("contribution_inkind_sum");
			}
		});
	}

	public List<Payment> getPaymentForBudgetList(long idBudget) {
		return jdbcTemplate.query("SELECT * FROM payments WHERE budget_id=? ", new Object[]{idBudget}, new RowMapper<Payment>(){
		      public Payment mapRow(ResultSet rs, int arg1) throws SQLException {
					Payment p = new Payment();
					p.setId(rs.getInt("id"));
					p.setBudget(budgetService.getBudget(rs.getInt("budget_id")));
					p.setDocument(documentService.getDocument(rs.getInt("document_id")));
					p.setDotation(rs.getBigDecimal("dotation"));
					p.setContributionOwn(rs.getBigDecimal("contribution_own"));
					p.setContributionPersonal(rs.getBigDecimal("contribution_personal"));
					p.setContributionInkind(rs.getBigDecimal("contribution_inkind"));
					return p;
		      }
		    });
	}

	public Integer getPaymentForBudgetCount(long idBudget) {
		String sql = "SELECT COUNT(*) AS count FROM payments WHERE budget_id=?";
		return (Integer)jdbcTemplate.queryForObject(sql, new Object[]{idBudget}, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getInt("count");
			}
		});
	}

	public BigDecimal getPaymentForBudgetDotationSum(long idBudget) {
		String sql = "SELECT COALESCE(SUM(dotation),0) AS dotation_sum FROM payments WHERE budget_id=?";
		return (BigDecimal)jdbcTemplate.queryForObject(sql, new Object[]{idBudget}, new RowMapper<BigDecimal>() {
			public BigDecimal mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getBigDecimal("dotation_sum");
			}
		});
	}

	public BigDecimal getPaymentForBudgetContributionOwnSum(long idBudget) {
		String sql = "SELECT COALESCE(SUM(contribution_own),0) AS contribution_own_sum FROM payments WHERE budget_id=?";
		return (BigDecimal)jdbcTemplate.queryForObject(sql, new Object[]{idBudget}, new RowMapper<BigDecimal>() {
			public BigDecimal mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getBigDecimal("contribution_own_sum");
			}
		});
	}

	public BigDecimal getPaymentForBudgetContributionPersonalSum(long idBudget) {
		String sql = "SELECT COALESCE(SUM(contribution_personal),0) AS contribution_personal_sum FROM payments WHERE budget_id=?";
		return (BigDecimal)jdbcTemplate.queryForObject(sql, new Object[]{idBudget}, new RowMapper<BigDecimal>() {
			public BigDecimal mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getBigDecimal("contribution_personal_sum");
			}
		});
	}
	
	public BigDecimal getPaymentForBudgetContributionInkindSum(long idBudget) {
		String sql = "SELECT COALESCE(SUM(contribution_inkind),0) AS contribution_inkind_sum FROM payments WHERE budget_id=?";
		return (BigDecimal)jdbcTemplate.queryForObject(sql, new Object[]{idBudget}, new RowMapper<BigDecimal>() {
			public BigDecimal mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getBigDecimal("contribution_inkind_sum");
			}
		});
	}
}
