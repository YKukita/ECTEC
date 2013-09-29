package jp.ac.osaka_u.ist.sdl.ectec.analyzer.vcs;

import java.util.Map;

import jp.ac.osaka_u.ist.sdl.ectec.data.Commit;
import jp.ac.osaka_u.ist.sdl.ectec.data.RevisionInfo;
import jp.ac.osaka_u.ist.sdl.ectec.settings.Language;

/**
 * An interface that represents a detector of target revisions
 * 
 * @author k-hotta
 * 
 */
public interface ITargetRevisionDetector {

	/**
	 * detect target revisions and commits
	 * 
	 * @param language
	 * @param startRevisionIdentifier
	 * @param endRevisionIdentifier
	 * @throws Exception
	 */
	public void detect(final Language language,
			final String startRevisionIdentifier,
			final String endRevisionIdentifier) throws Exception;

	/**
	 * get the detected target revisions <br>
	 * this method must be called after
	 * {@link ITargetRevisionDetector#detect(Language, String, String)}
	 * 
	 * @return
	 */
	public Map<Long, RevisionInfo> getTargetRevisions();

	/**
	 * get the detected target commits <br>
	 * this method must be called after
	 * {@link ITargetRevisionDetector#detect(Language, String, String)}
	 * 
	 * @return
	 */
	public Map<Long, Commit> getCommits();

}
