package commonapi.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.franca.core.dsl.FrancaIDLStandaloneSetup;
import org.franca.core.dsl.FrancaIDLVersion;
import org.franca.core.dsl.FrancaPersistenceManager;
import org.franca.core.franca.FModel;
import org.franca.deploymodel.core.FDModelExtender;
import org.franca.deploymodel.dsl.FDeployPersistenceManager;
import org.franca.deploymodel.dsl.FDeployStandaloneSetup;
import org.franca.deploymodel.dsl.fDeploy.FDInterface;
import org.franca.deploymodel.dsl.fDeploy.FDModel;
import org.genivi.commonapi.core.generator.FrancaGeneratorExtensions;

import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * Runner for any standalone generator/transformation from Franca files.
 * 
 * @author Klaus Birken (itemis)
 */
public class CommonAPIStandaloneGen {

	// prepare class for logging....
	private static final Logger logger = Logger
			.getLogger(CommonAPIStandaloneGen.class);

	private static final String TOOL_VERSION = "0.1.0";
	private static final String FIDL_VERSION = FrancaIDLVersion.getMajor()
			+ "." + FrancaIDLVersion.getMinor();

	private static final String HELP = "h";
	private static final String FIDLFILE = "f";
	private static final String OUTDIR = "o";

	private static final String VERSIONSTR = "FrancaStandaloneGen "
			+ TOOL_VERSION + " (Franca IDL version " + FIDL_VERSION + ").";

	private static final String HELPSTR = "java -jar FrancaStandaloneGen.jar [OPTIONS]";

	private static Injector injector;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		injector = new FrancaIDLStandaloneSetup()
				.createInjectorAndDoEMFRegistration();
		new FDeployStandaloneSetup().createInjectorAndDoEMFRegistration();

		int retval = injector.getInstance(CommonAPIStandaloneGen.class).run(
				args);
		if (retval != 0)
			System.exit(retval);
	}

	// injected fragments
	@Inject
	FrancaPersistenceManager fidlLoader;

	@Inject
	FDeployPersistenceManager fdeploymentModelLoader;

	ArrayList<File> m_writtenFiles = new ArrayList<File>();

	ArrayList<GeneratorInterface> m_generators = new ArrayList<GeneratorInterface>();

	public int run(String[] args) throws Exception {

		Options options = getOptions();

		// print version string
		System.out.println(VERSIONSTR);

		// create the parser
		CommandLineParser parser = new GnuParser();
		CommandLine line = null;
		final HelpFormatter formatter = new HelpFormatter();
		try {
			line = parser.parse(options, args);
		} catch (final ParseException exp) {
			logger.error(exp.getMessage());
			formatter.printHelp(HELPSTR, options);
			return -1;
		}

		if (line.hasOption(HELP) || checkCommandLineValues(line) == false) {
			formatter.printHelp(HELPSTR, options);
			return -1;
		}

		String fidlFile = line.getOptionValue(FIDLFILE);
		File file = new File(fidlFile);

		// call generator and save files
		final String outputFolder = line.getOptionValue(OUTDIR);

		FDModel fdeploymentModel = fdeploymentModelLoader.loadModel(
				file.getName(), file.getAbsoluteFile().getParentFile()
						.getAbsolutePath());
		if (fdeploymentModel == null) {
			logger.error("Couldn't load Franca deployment file '" + fidlFile
					+ "'.");
			return -1;
		}
		FDModelExtender fModelExtender = new FDModelExtender(fdeploymentModel);
		if (fModelExtender.getFDInterfaces().size() <= 0)
			System.err
					.println("No Interfaces were deployed, nothing to generate.");

		FrancaGeneratorExtensions franceGeneratorExtensions = new FrancaGeneratorExtensions();
		FModel fModel = franceGeneratorExtensions.getModel(fModelExtender
				.getFDInterfaces().get(0).getTarget());
		List<FDInterface> fInterfaces = fModelExtender.getFDInterfaces();

		IFileSystemAccess f = new IFileSystemAccess() {

			@Override
			public void generateFile(String arg0, String arg1, CharSequence arg2) {
				// TODO
				logger.error("Not implemented");
				throw new RuntimeException();
			}

			@Override
			public void generateFile(String path, CharSequence content) {
				File outputFile = new File(outputFolder + File.separator + path);

				// logger.info("Content of file : " + content);

				if (m_writtenFiles.contains(outputFile)) {
					logger.info("Skipping already written file : "
							+ outputFile.getAbsolutePath());
					logger.info("Content of skipped file : " + content);
					// logger.info("Content of already generated file : " +
					// content);
					return;
				}

				// m_writtenFiles.add(outputFile);

				try {
					outputFile.getParentFile().mkdirs();

					byte[] bytesToWrite = content.toString().getBytes();

					try {
						FileInputStream fis = new FileInputStream(outputFile);
						byte[] existingFileContent = new byte[fis.available()];
						fis.read(existingFileContent);
						fis.close();
						System.out.println("length "
								+ existingFileContent.length + " "
								+ bytesToWrite.length);
						if (Arrays.equals(bytesToWrite, existingFileContent)) {
							logger.info("No change to file "
									+ outputFile.getAbsolutePath());
							return;
						}

					} catch (Exception e) {
						// e.printStackTrace();
					}

					logger.info("Writing file " + outputFile.getAbsolutePath());

					outputFile.createNewFile();
					FileOutputStream os = new FileOutputStream(outputFile);

					os.write(bytesToWrite);
					os.close();

				} catch (IOException e) {
					logger.error("Can not create file "
							+ outputFile.getAbsolutePath());
					e.printStackTrace();
				}

			}

			@Override
			public void deleteFile(String arg0) {
				// TODO
				logger.error("Not implemented");
				throw new RuntimeException();
			}

		};

		m_generators.add(injector.getInstance(api.Generator.class));
		m_generators.add(injector.getInstance(dbus.Generator.class));
		m_generators.add(injector.getInstance(someip.Generator.class));
		m_generators.add(injector.getInstance(qt.Generator.class));

		for (GeneratorInterface generator : m_generators) {
			generator.generate(fModel, fInterfaces, f, null);
		}

		logger.info("FrancaStandaloneGen done.");
		return 0;
	}

	@SuppressWarnings("static-access")
	private Options getOptions() {
		// String[] set = LogFactory.getLog(getClass()).
		final Options options = new Options();

		// optional
		// Option optVerbose = OptionBuilder.withArgName("verbose")
		// .withDescription("Print Out Verbose Information").hasArg(false)
		// .isRequired(false).create(VERBOSE);
		// options.addOption(optVerbose);
		Option optHelp = OptionBuilder.withArgName("help")
				.withDescription("Print Usage Information").hasArg(false)
				.isRequired(false).create(HELP);
		options.addOption(optHelp);

		// required
		Option optInputFidl = OptionBuilder
				.withArgName("Franca deployment file")
				.withDescription(
						"Input file in Franca deployment (fdepl) format.")
				.hasArg().isRequired().withValueSeparator(' ').create(FIDLFILE);
		// optInputFidl.setType(File.class);
		options.addOption(optInputFidl);

		Option optOutputDir = OptionBuilder
				.withArgName("output directory")
				.withDescription(
						"Directory where the generated files will be stored")
				.hasArg().isRequired().withValueSeparator(' ').create(OUTDIR);
		options.addOption(optOutputDir);

		return options;
	}

	private boolean checkCommandLineValues(CommandLine line) {
		if (line.hasOption(FIDLFILE)) {
			String fidlFile = line.getOptionValue(FIDLFILE);
			File fidl = new File(fidlFile);
			if (fidl.exists()) {
				return true;
			} else {
				logger.error("Cannot open Franca IDL file '" + fidlFile + "'.");
			}
		}
		return false;
	}

}
