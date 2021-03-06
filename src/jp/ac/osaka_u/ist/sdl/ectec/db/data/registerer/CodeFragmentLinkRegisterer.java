package jp.ac.osaka_u.ist.sdl.ectec.db.data.registerer;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import jp.ac.osaka_u.ist.sdl.ectec.db.DBConnectionManager;
import jp.ac.osaka_u.ist.sdl.ectec.db.data.DBCodeFragmentLinkInfo;

/**
 * A class that represents a registerer for links of code fragments
 * 
 * @author k-hotta
 * 
 */
public class CodeFragmentLinkRegisterer extends
		AbstractUniqueElementRegisterer<DBCodeFragmentLinkInfo> {

	/**
	 * the constructor
	 * 
	 * @param dbManager
	 * @param maxBatchCount
	 */
	public CodeFragmentLinkRegisterer(DBConnectionManager dbManager,
			int maxBatchCount) {
		super(dbManager, maxBatchCount);
	}

	@Override
	protected String createPreparedStatementQueue() {
		return "insert into CODE_FRAGMENT_LINK values(?,?,?,?,?,?)";
	}

	@Override
	protected void setAttributes(PreparedStatement pstmt,
			DBCodeFragmentLinkInfo element) throws SQLException {
		int column = 0;
		pstmt.setLong(++column, element.getId());
		pstmt.setLong(++column, element.getBeforeElementId());
		pstmt.setLong(++column, element.getAfterElementId());
		pstmt.setLong(++column, element.getBeforeCombinedRevisionId());
		pstmt.setLong(++column, element.getAfterCombinedRevisionId());

		final int changed = (element.isChanged()) ? 1 : 0;
		pstmt.setInt(++column, changed);
	}

}
