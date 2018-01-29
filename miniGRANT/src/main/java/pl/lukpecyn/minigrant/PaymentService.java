package pl.lukpecyn.minigrant;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	BudgetService budgetService;
	
	@Autowired
	DocumentService documentService;
	
	public int addPayment(Payment payment) {
		String sql ="INSERT INTO payment(budget_id,document_id,dotation,own,volunteerism) VALUES(?,?,?,?,?)";
		return jdbcTemplate.update(sql,payment.getBudget().getId(),payment.getDocument().getId(),payment.getDotation(),payment.getOwn(),payment.getVolunteerism());
	}
	
	public Payment getPayment(long idPayment) {
		return (Payment)jdbcTemplate.queryForObject("SELECT * FROM payments WHERE id=?", new Object[]{idPayment}, new RowMapper<Payment>(){
			public Payment mapRow(ResultSet rs, int arg1) throws SQLException {
				Payment p = new Payment();
				p.setId(rs.getInt("id"));
				p.setBudget(budgetService.getBudget(rs.getInt("budget_id")));
				p.setDocument(documentService.getDocument(rs.getInt("document_id")));
				p.setDotation(rs.getBigDecimal("dotation"));
				p.setOwn(rs.getBigDecimal("own"));
				p.setVolunteerism(rs.getBigDecimal("volunteerism"));
				return p;
			}
		}); 		
	}
	
	public List<Payment> getPaymentForDocumentList(long idDocument) {
		return jdbcTemplate.query("SELECT * FROM payments WHERE document_id=? ", new Object[]{idDocument}, new RowMapper<Payment>(){
		      public Payment mapRow(ResultSet rs, int arg1) throws SQLException {
					Payment p = new Payment();
					p.setId(rs.getInt("id"));
					p.setBudget(budgetService.getBudget(rs.getInt("budget_id")));
					p.setDocument(documentService.getDocument(rs.getInt("document_id")));
					p.setDotation(rs.getBigDecimal("dotation"));
					p.setOwn(rs.getBigDecimal("own"));
					p.setVolunteerism(rs.getBigDecimal("volunteerism"));
					return p;
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
					p.setOwn(rs.getBigDecimal("own"));
					p.setVolunteerism(rs.getBigDecimal("volunteerism"));
					return p;
		      }
		    });
	}
}
