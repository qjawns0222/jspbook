package member.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import jdbc.connection.ConnectionProvider;
import member.model.Member;
import memeber.dao.MemberDao;

public class JoinService {
	private MemberDao memberDao = new MemberDao();

	public void join(JoinRequest joinReq) throws SQLException {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			Member member = memberDao.selectById(conn, joinReq.getId());
			if (member != null) {
				conn.rollback();
				throw new DuplicateIdException();
			}
			memberDao.insert(conn, new Member(joinReq.getId(), joinReq.getName(), joinReq.getPassword(), new Date()));
			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			throw new RuntimeException(e);
		} finally {
			conn.close();
		}
	}

}
