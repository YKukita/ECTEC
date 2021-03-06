package jp.ac.osaka_u.ist.sdl.ectec.main.fragmentdetector.crd;

import jp.ac.osaka_u.ist.sdl.ectec.db.data.BlockType;
import jp.ac.osaka_u.ist.sdl.ectec.db.data.DBCrdInfo;
import jp.ac.osaka_u.ist.sdl.ectec.main.fragmentdetector.normalizer.NormalizedStringCreator;
import jp.ac.osaka_u.ist.sdl.ectec.main.fragmentdetector.normalizer.StringCreateVisitor;

import org.eclipse.jdt.core.dom.EnhancedForStatement;

/**
 * A crd creator for enhanced for statements
 * 
 * @author k-hotta
 * 
 */
public class EnhancedForStatementCRDCreator extends
		AbstractBlockAnalyzer<EnhancedForStatement> {

	public EnhancedForStatementCRDCreator(EnhancedForStatement node,
			DBCrdInfo parent, StringCreateVisitor visitor) {
		super(node, parent, BlockType.ENHANCED_FOR, visitor);
	}

	/**
	 * get the anchor (the conditional expression)
	 */
	@Override
	protected String getAnchor() {
		return getAnchor(node);
	}

	public static String getAnchor(final EnhancedForStatement node) {
		return node.getExpression().toString();
	}

	@Override
	protected String getNormalizedAnchor() {
		final NormalizedStringCreator anchorNormalizer = new NormalizedStringCreator();

		node.getParameter().accept(anchorNormalizer);
		anchorNormalizer.getBuffer().append(" : ");
		node.getExpression().accept(anchorNormalizer);

		return anchorNormalizer.getString();
	}

}
