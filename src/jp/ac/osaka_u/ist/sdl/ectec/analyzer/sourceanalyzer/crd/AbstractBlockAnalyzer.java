package jp.ac.osaka_u.ist.sdl.ectec.analyzer.sourceanalyzer.crd;

import java.util.ArrayList;
import java.util.List;

import jp.ac.osaka_u.ist.sdl.ectec.analyzer.sourceanalyzer.hash.IHashCalculator;
import jp.ac.osaka_u.ist.sdl.ectec.data.BlockType;
import jp.ac.osaka_u.ist.sdl.ectec.data.CRD;

import org.eclipse.jdt.core.dom.ASTNode;

/**
 * A class to create a crd for a given node
 * 
 * @author k-hotta
 * 
 */
public abstract class AbstractBlockAnalyzer<T extends ASTNode> {

	/**
	 * the node to be analyzed
	 */
	protected final T node;

	/**
	 * the crd for the parent of this node
	 */
	protected final CRD parent;

	/**
	 * the type of the block
	 */
	protected final BlockType bType;

	/**
	 * the normalized anchor
	 */
	protected final IHashCalculator visitor;

	/**
	 * the crd created as a result of analysis
	 */
	private CRD createdCrd;

	/**
	 * the normalized string created as a result of analysis
	 */
	private String stringForCloneDetection;

	public AbstractBlockAnalyzer(final T node, final CRD parent,
			final BlockType bType, final IHashCalculator visitor) {
		this.node = node;
		this.parent = parent;
		this.bType = bType;
		this.visitor = visitor;
	}

	public CRD getCreatedCrd() {
		return createdCrd;
	}

	public String getStringForCloneDetection() {
		return stringForCloneDetection;
	}

	/**
	 * create a new instance of CRD for this block
	 * 
	 * @return
	 */
	public void analyze() {
		final String head = bType.getHead();
		final String anchor = getAnchor();

		final List<Long> ancestorIds = new ArrayList<Long>();

		if (parent != null) {
			for (final long ancestorId : parent.getAncestors()) {
				ancestorIds.add(ancestorId);
			}
			ancestorIds.add(parent.getId());
		}

		final MetricsCalculator cmCalculator = new MetricsCalculator();
		node.accept(cmCalculator);
		final int cm = cmCalculator.getCC() + cmCalculator.getFO();

		final String thisCrdStr = getStringCrdForThisBlock(head, anchor, cm);
		final String fullText = (parent == null) ? thisCrdStr : parent
				.getFullText() + "\n" + thisCrdStr;

		visit();

		createdCrd = new CRD(bType, head, anchor,
				visitor.getNormalizedAnchor(), cm, ancestorIds, fullText);
		stringForCloneDetection = visitor.getString();
	}

	protected void visit() {
		node.accept(visitor);
	}

	/**
	 * get the string representation of THIS block
	 * 
	 * @param head
	 * @param anchor
	 * @param cm
	 * @return
	 */
	private String getStringCrdForThisBlock(final String head,
			final String anchor, final int cm) {
		final StringBuilder builder = new StringBuilder();

		builder.append(head + ",");
		builder.append(anchor + ",");
		builder.append(cm + "\n");

		return builder.toString();
	}

	/**
	 * get the anchor of the block
	 * 
	 * @return
	 */
	protected abstract String getAnchor();

}
