package pl.lukpecyn.minigrant.services;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pl.lukpecyn.minigrant.MiniGrantApplication;
import pl.lukpecyn.minigrant.models.Grant;

@Service
public class GrantService {

	private static final Logger logger = LoggerFactory.getLogger(GrantService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private DonorService donorService;
	
	@Autowired
	private BeneficiaryService beneficiaryService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public int addGrant(Grant grant) throws DataAccessException, ParseException {
		String sql = "INSERT INTO grants(donor_id,beneficiary_id,name,description,date_begin,date_end,status) VALUES(?,?,?,?,?,?,?)";
		return jdbcTemplate.update(sql,grant.getDonor().getId(),grant.getIdBeneficiary(),grant.getName(),grant.getDescription(),sdf.parse(grant.getDateBegin()),sdf.parse(grant.getDateEnd()),grant.getStatus());
	}
	
	public int updateGrant(Grant grant) throws DataAccessException, ParseException {
		String sql = "UPDATE grants SET donor_id=?, beneficiary_id=?, name=?, description=?, date_begin=?,date_end=?,status=? WHERE id=?";
		return jdbcTemplate.update(sql,grant.getDonor().getId(),grant.getIdBeneficiary(),grant.getName(), grant.getDescription(), sdf.parse(grant.getDateBegin()),sdf.parse(grant.getDateEnd()),grant.getStatus(),grant.getId());
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
				g.setIdBeneficiary(rs.getInt("beneficiary_id"));
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
	
	public List<Grant> getGrantsListByDateBeginDesc(Integer idBeneficiary) {
		String sql = "SELECT * FROM grants WHERE beneficiary_id=? ORDER BY date_begin DESC,name";
		return jdbcTemplate.query(sql, new Object[]{idBeneficiary}, new RowMapper<Grant>(){
		      public Grant mapRow(ResultSet rs, int arg1) throws SQLException {
		        Grant g = new Grant();
		        g.setId(rs.getInt("id"));
				g.setDonor(donorService.getDonor(rs.getInt("donor_id")));
				g.setIdBeneficiary(rs.getInt("beneficiary_id"));
		        g.setName(rs.getString("name"));
				g.setDescription(rs.getString("description"));
		        g.setDateBegin(rs.getString("date_begin"));
		        g.setDateEnd(rs.getString("date_end"));
		        g.setStatus(rs.getInt("status"));
		        return g;
		      }
		    });
	}
	
	public List<Grant> getGrantsListByName(Integer idBeneficiary) {
		String sql = "SELECT id, donor_id, beneficiary_id, name, description, date_begin, date_end, statu"
				+ " FROM grants WHERE beneficiary_id=? ORDER BY name";
		return jdbcTemplate.query(sql, new Object[]{idBeneficiary}, new RowMapper<Grant>(){
		      public Grant mapRow(ResultSet rs, int arg1) throws SQLException {
		        Grant g = new Grant();
		        g.setId(rs.getInt("id"));
				g.setDonor(donorService.getDonor(rs.getInt("donor_id")));
				g.setIdBeneficiary(rs.getInt("beneficiary_id"));
		        g.setName(rs.getString("name"));
				g.setDescription(rs.getString("description"));
		        g.setDateBegin(rs.getString("date_begin"));
		        g.setDateEnd(rs.getString("date_end"));
		        g.setStatus(rs.getInt("status"));
		        return g;
		      }
		    });
	}
	
	//@Scheduled(fixedDelay=60000)
	@Scheduled(cron="0 10 0 * * *")
	public void autoStatus() {
		//String sql = "UPDATE grants SET status=10 WHERE DATEDIFF('DD', date_begin,CURDATE())>=0 AND status=0";
		String sql = "UPDATE grants SET status=10 WHERE date_begin>=? AND status=0";
		Integer count = jdbcTemplate.update(sql, new Object[] {new Date()});
		if(count!=0) logger.info("Ustawiono status 10 dla " + count + " grantów");

		sql = "UPDATE grants SET status=20 WHERE date_end>=? AND status=10";
		count = jdbcTemplate.update(sql, new Object[] {new Date()});
		if(count!=0) logger.info("Ustawiono status 20 dla " + jdbcTemplate.update(sql) + " grantów");

	}
}
