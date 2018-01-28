package pl.lukpecyn.minigrant;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public int addDocument(Document document) {
		String sql = "INSERT INTO documents(grant_id,name,description,value) VALUES(?,?,?,?)";
		return jdbcTemplate.update(sql,document.getIdGrant(),document.getName(),document.getDescription(),document.getValue());		
	}

	public Document getDocument(long idDocument) {
		return (Document)jdbcTemplate.queryForObject("SELECT * FROM documents WHERE id=?", new Object[]{idDocument}, new RowMapper<Document>(){
			public Document mapRow(ResultSet rs, int arg1) throws SQLException {
				Document d = new Document();
				d.setId(rs.getInt("id"));
				d.setIdGrant(rs.getInt("grant_id"));
				d.setName(rs.getString("name"));
				d.setDescription(rs.getString("description"));
				d.setValue(rs.getBigDecimal("value"));
				return d;
			}
		}); 		
	}

	public List<Document> getDocumentForGrantList(long idGrant) {
		return jdbcTemplate.query("SELECT * FROM documents WHERE grant_id=? ", new Object[]{idGrant}, new RowMapper<Document>(){
		      public Document mapRow(ResultSet rs, int arg1) throws SQLException {
					Document d = new Document();
					d.setId(rs.getInt("id"));
					d.setIdGrant(rs.getInt("grant_id"));
					d.setName(rs.getString("name"));
					d.setDescription(rs.getString("description"));
					d.setValue(rs.getBigDecimal("value"));
					return d;
		      }
		    });
	}

}
