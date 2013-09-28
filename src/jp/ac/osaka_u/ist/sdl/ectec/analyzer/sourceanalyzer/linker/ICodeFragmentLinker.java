package jp.ac.osaka_u.ist.sdl.ectec.analyzer.sourceanalyzer.linker;

import java.util.Collection;
import java.util.Map;

import jp.ac.osaka_u.ist.sdl.ectec.data.CodeFragmentInfo;
import jp.ac.osaka_u.ist.sdl.ectec.data.CodeFragmentLinkInfo;

/**
 * An interface to provide a function to detect links of code fragments between
 * two revisions
 * 
 * @author k-hotta
 * 
 */
public interface ICodeFragmentLinker {

	/**
	 * detect pairs of code fragments <br>
	 * code fragments that are not changed in the commit must be removed from
	 * the arguments
	 * 
	 * @param beforeBlocks
	 * @param afterBlocks
	 * @return
	 */
	public Map<Long, CodeFragmentLinkInfo> detectFragmentPairs(
			final Collection<CodeFragmentInfo> beforeBlocks,
			final Collection<CodeFragmentInfo> afterBlocks);
}