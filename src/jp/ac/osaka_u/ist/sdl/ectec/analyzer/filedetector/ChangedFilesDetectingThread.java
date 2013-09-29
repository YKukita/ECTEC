package jp.ac.osaka_u.ist.sdl.ectec.analyzer.filedetector;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import jp.ac.osaka_u.ist.sdl.ectec.analyzer.vcs.IChangedFilesDetector;
import jp.ac.osaka_u.ist.sdl.ectec.data.RevisionInfo;
import jp.ac.osaka_u.ist.sdl.ectec.settings.Language;
import jp.ac.osaka_u.ist.sdl.ectec.settings.MessagePrinter;

/**
 * A thread class that detects changed files with specified detector
 * 
 * @author k-hotta
 * 
 */
public class ChangedFilesDetectingThread implements Runnable {

	/**
	 * a detector
	 */
	private final IChangedFilesDetector detector;

	/**
	 * the map contains the result of analysis <br>
	 * the key is the path of a file and the value is a set of ids of revisions
	 * where the file whose path is specified with the key is changed
	 */
	private final ConcurrentMap<String, SortedSet<ChangeOnFile>> changedFiles;

	/**
	 * the target language
	 */
	private final Language language;

	/**
	 * the target revisions as array
	 */
	private final RevisionInfo[] targetRevisions;

	/**
	 * the counter that points which revision is the next target
	 */
	private final AtomicInteger index;

	public ChangedFilesDetectingThread(final IChangedFilesDetector detector,
			final ConcurrentMap<String, SortedSet<ChangeOnFile>> changedFiles,
			final Language language, final RevisionInfo[] targetRevisions,
			final AtomicInteger index) {
		this.detector = detector;
		this.changedFiles = changedFiles;
		this.language = language;
		this.targetRevisions = targetRevisions;
		this.index = index;
	}

	@Override
	public void run() {
		while (true) {
			final int currentIndex = index.getAndIncrement();

			if (currentIndex >= targetRevisions.length) {
				break;
			}

			final RevisionInfo targetRevision = targetRevisions[currentIndex];

			MessagePrinter.println("\t[" + (currentIndex + 1) + "/"
					+ targetRevisions.length + "] analyzing revision "
					+ targetRevision.getIdentifier());

			try {
				// detect changed files in this revision
				final Map<String, Character> changedFilesInThisRevision = detector
						.detectChangedFiles(targetRevision.getIdentifier(),
								language);

				// store paths detected the above step into the concurrent map
				for (final Map.Entry<String, Character> entry : changedFilesInThisRevision
						.entrySet()) {
					// the path of a changed file
					final String path = entry.getKey();

					/*
					 * put a new entry into the map if the path is not contained
					 * in it as a key
					 */
					synchronized (changedFiles) {
						if (!changedFiles.containsKey(path)) {
							changedFiles.put(path, new TreeSet<ChangeOnFile>());
						}
					}

					final ChangeOnFile changeOnFile = new ChangeOnFile(path,
							targetRevision.getId(),
							ChangeTypeOnFile.getCorrespondingChangeType(entry
									.getValue()));
					changedFiles.get(path).add(changeOnFile);
				}

			} catch (Exception e) {
				MessagePrinter
						.eStronglyPrintln("[ERROR] something is occured when analyzing revision "
								+ targetRevision.getIdentifier());
				e.printStackTrace();
			}
		}
	}
}
