package jp.ac.osaka_u.ist.sdl.ectec.main;

import jp.ac.osaka_u.ist.sdl.ectec.PropertiesKeys;
import jp.ac.osaka_u.ist.sdl.ectec.PropertiesReader;
import jp.ac.osaka_u.ist.sdl.ectec.settings.MessagePrintLevel;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

/**
 * An abstract class having common functions to keep runtime settings
 * 
 * @author k-hotta
 * 
 */
public abstract class AbstractSettings implements PropertiesKeys {

	/**
	 * the level of verbose output
	 */
	private MessagePrintLevel verboseLevel;

	/**
	 * the path of the properties file
	 */
	private String propertyFilePath;

	public final MessagePrintLevel getVerboseLevel() {
		return this.verboseLevel;
	}

	public final String getPropertyFilePath() {
		return this.propertyFilePath;
	}

	/**
	 * parse and load the given arguments
	 * 
	 * @param args
	 */
	public void load(final String[] args) throws Exception {
		final Options options = defineOptions();

		final CommandLineParser parser = new PosixParser();
		final CommandLine cmd = parser.parse(options, args);

		// initialize the property file path
		propertyFilePath = (cmd.hasOption("p")) ? cmd.getOptionValue("p")
				: null;

		// load the given or default properties file
		final PropertiesReader propReader = new PropertiesReader(
				propertyFilePath);

		// initialize other common settings
		verboseLevel = (cmd.hasOption("v")) ? MessagePrintLevel
				.getCorrespondingLevel(cmd.getOptionValue("v"))
				: MessagePrintLevel.getCorrespondingLevel(propReader
						.getProperty(VERBOSE_LEVEL));

		// initialize other settings
		initializeParticularSettings(cmd, propReader);
	}

	/**
	 * define options
	 * 
	 * @return
	 */
	protected final Options defineOptions() {
		final Options options = new Options();

		{
			final Option v = new Option("v", "verbose", true, "verbose output");
			v.setArgs(1);
			v.setRequired(false);
			options.addOption(v);
		}

		{
			final Option p = new Option("p", "properties", true,
					"properties file");
			p.setArgs(1);
			p.setRequired(false);
			options.addOption(p);
		}

		return addParticularOptions(options);
	}

	/**
	 * define particular options for each subsystem
	 * 
	 * @param options
	 * @return
	 */
	protected abstract Options addParticularOptions(final Options options);

	/**
	 * initialize particular settings in each subsystem
	 * 
	 * @param cmd
	 * @param propReader
	 * @throws Exception
	 */
	protected abstract void initializeParticularSettings(final CommandLine cmd,
			final PropertiesReader propReader) throws Exception;

}
