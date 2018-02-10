package pl.lukpecyn.minigrant.services;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import pl.lukpecyn.minigrant.models.Grant;

@Service
public class GrantService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private DonorService donorService;
	
	@Autowired
	private BeneficiaryService beneficiaryService;

	public int addGrant(Grant grant) {
		String sql = "INSERT INTO grants(donor_id,beneficiary_id,name,description,date_begin,date_end,status) VALUES(?,?,?,?,?,?,?)";
		return jdbcTemplate.update(sql,grant.getDonor().getId(),grant.getBeneficiary().getId(),grant.getName(),grant.getDescription(),grant.getDateBegin(),grant.getDateEnd(),grant.getStatus());
	}
	
	public int updateGrant(Grant grant) {
		String sql = "UPDATE grants SET donor_id=?, beneficiary_id=?, name=?, description=?, date_begin=?,date_end=?,status=? WHERE id=?";
		return jdbcTemplate.update(sql,grant.getDonor().getId(),grant.getBeneficiary().getId(),grant.getName(), grant.getDescription(), grant.getDateBegin(),grant.getDateEnd(),grant.getStatus(),grant.getId());
	}
	
	public int delGrant(Integer id) {
		String sql = "DELETE FROM grants where id=?";
		return jdbcTemplate.update(sql,id);
	}

	public Grant getGrant(long idGrant) {
		String sql = "SELECT g.id, g.donor_id, g.beneficiary_id, g.name, g.description, g.date_begin, g.date_end, g.status, COALESCE(b.dotation,0) AS dotation, COALESCE(b.contribution_own,0) AS contribution_own, COALESCE(b.contribution_personal,0) AS contribution_personal, COALESCE(b.contribution_inkind,0) AS contribution_inkind FROM grants g LEFT JOIN (SELECT grant_id, SUM(dotation) AS dotation, SUM(contribution_own) AS contribution_own, SUM(contribution_personal) AS contribution_personal, SUM(contribution_inkind) AS contribution_inkind FROM budgets GROUP BY grant_id) b ON g.id=b.grant_id WHERE g.id=?";
		return (Grant)jdbcTemplate.queryForObject(sql, new Object[]{idGrant}, new RowMapper<Grant>(){
			public Grant mapRow(ResultSet rs, int arg1) throws SQLException {
				Grant g = new Grant();
				g.setId(rs.getInt("id"));
				g.setDonor(donorService.getDonor(rs.getInt("donor_id")));
				g.setBeneficiary(beneficiaryService.getBeneficiary(rs.getInt("beneficiary_id")));
				g.setName(rs.getString("name"));
				g.setDescription(rs.getString("description"));
				g.setDateBegin(rs.getString("date_begin"));
				g.setDateEnd(rs.getString("date_end"));
				g.setStatus(rs.getInt("status"));
				g.setDotation(rs.getBigDecimal("dotation"));
				g.setContributionOwn(rs.getBigDecimal("contribution_own"));
				g.setContributionPersonal(rs.getBigDecimal("contribution_personal"));
				g.setContributionInkind(rs.getBigDecimal("contribution_Inkind"));
				return g;
			}
		}); 		
	}
	
	public List<Grant> getGrantsListByDateBegin() {
		return jdbcTemplate.query("SELECT * FROM grants ORDER BY date_begin DESC,name", new RowMapper<Grant>(){
		      public Grant mapRow(ResultSet rs, int arg1) throws SQLException {
		        Grant g = new Grant();
		        g.setId(rs.getInt("id"));
				g.setDonor(donorService.getDonor(rs.getInt("donor_id")));
				g.setBeneficiary(beneficiaryService.getBeneficiary(rs.getInt("beneficiary_id")));
		        g.setName(rs.getString("name"));
				g.setDescription(rs.getString("description"));
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
				g.setDonor(donorService.getDonor(rs.getInt("donor_id")));
				g.setBeneficiary(beneficiaryService.getBeneficiary(rs.getInt("beneficiary_id")));
		        g.setName(rs.getString("name"));
				g.setDescription(rs.getString("description"));
		        g.setDateBegin(rs.getString("date_begin"));
		        g.setDateEnd(rs.getString("date_end"));
		        g.setStatus(rs.getInt("status"));
		        return g;
		      }
		    });
	}	
}
