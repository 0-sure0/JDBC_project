package Score;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DatabaseManager {
	private static String className = "org.mariadb.jdbc.Driver";
	private static String url = "jdbc:mariadb://127.0.0.1:3306/MyDB";
	private static String ID = "root";
	private static String PW = "1234";
	
	static {
		try {
			Class.forName(className);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConn() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, ID, PW);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public int insertScore(Score score) {
		Connection con = null;
		Statement stmt = null;
		
		int result = 0;
		try {
			con = getConn();
			stmt = con.createStatement();
			String sql = "INSERT INTO score VALUES('" + score.getName() + "', "
					+ score.getKor() + ", " + score.getEng() + ", " + score.getMath() +", "+ score.getTotal() + ", " + score.getAverage() + ")";
			result = stmt.executeUpdate(sql);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null) {
					stmt.close();
				}
				if(con != null) {
					con.close();
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	//삭제
	public int deleteScore(Score score) {
		Connection con = null;
		Statement stmt = null;
		
		int result = 0;
		try {
			con = getConn();
			stmt = con.createStatement();
			String sql = "DELETE FROM score WHERE name = '" + score.getName() + "'";
			result = stmt.executeUpdate(sql);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt != null) {
					stmt.close();
				}
				if(con != null) {
					con.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	//수정
	public int updateScore(Score score) {
		Connection con = null;
		Statement stmt = null;
		
		int result = 0;
		try {
			con = getConn();
			stmt = con.createStatement();
			String sql = "UPDATE score SET kor = '" + score.getKor() + "', eng = '" + score.getEng() + 
					"', math = '" + score.getMath() + "', total = '" + score.getTotal() 
					+ "', average = '" + score.getAverage() + "' WHERE name = '" + score.getName() + "'";
			result = stmt.executeUpdate(sql);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt != null) {
					stmt.close();
				}
				if(con != null) {
					con.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	//getscore에 rank 추가
	public Vector getScore() {
		Vector data = new Vector();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			con = getConn();
			String sql = "SELECT * , RANK() OVER (ORDER BY total desc) as rank FROM score ";
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String name = rs.getString("name");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");				
				int average = rs.getInt("average");
				int rank = rs.getInt("rank");
				
				Vector row = new Vector();
				row.add(rank);
				row.add(name);
				row.add(kor);
				row.add(eng);
				row.add(math);
				row.add(total);
				row.add(average);				
				data.add(row);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null)rs.close();
				if(stmt!=null)stmt.close();
				if(con!=null)con.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	
}
