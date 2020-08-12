package org.emoflon.ibex.tgg.run.ifc2citygml;

import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.emoflon.ibex.tgg.compiler.defaults.IRegistrationHelper;
import org.emoflon.ibex.tgg.operational.strategies.modules.TGGResourceHandler;

import org.emoflon.ibex.tgg.run.ifc2citygml.config.*;
import org.moflon.core.utilities.ExtensionsUtil;
import org.emoflon.ibex.tgg.operational.strategies.sync.INITIAL_FWD;
import org.emoflon.ibex.tgg.ide.admin.BuilderExtension;

public class INITIAL_FWD_App extends INITIAL_FWD {

	// eMoflon supports other pattern matching engines. Replace _DefaultRegistrationHelper with one of the other registrationHelpers from the *.config-package to choose between them. Default: Democles 
	public static IRegistrationHelper registrationHelper = new _DefaultRegistrationHelper();

	public INITIAL_FWD_App() throws IOException {
		super(registrationHelper.createIbexOptions().resourceHandler(new TGGResourceHandler() {
			@Override
			public void saveModels() throws IOException {
				// Use the commented code below to implement saveModels individually.
				// source.save(null);
				// target.save(null);
				// corr.save(null);
				// protocol.save(null);
				
				super.saveModels();
			}
			
			@Override
			public void loadModels() throws IOException {
				// Use the commented code below to implement loadModels individually.
				// loadResource loads from a file while createResource creates a new resource without content
				// source = loadResource(options.project.path() + "/instances/src.xmi");
				// target = createResource(options.project.path() + "/instances/trg.xmi");
				// corr = createResource(options.project.path() + "/instances/corr.xmi");
				// protocol = createResource(options.project.path() + "/instances/protocol.xmi");
				
				
				super.loadModels();
			}
		}));
	}

	public static void main(String[] args) throws IOException {
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.INFO);

		/*
		TggPackage tgg = TggPackage.eINSTANCE;
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		ResourceSet resSet = new ResourceSetImpl();
        Resource flattenedResource = resSet.getResource(URI.createURI("model/IFC2CityGML_flattened.editor.xmi"), true);
        flattenedResource.load(null);
        Resource editorResource = resSet.getResource(URI.createURI("model/IFC2CityGML.editor.xmi"), true);
        editorResource.load(null);
        TripleGraphGrammarFile flattened = (TripleGraphGrammarFile) flattenedResource.getContents().get(0);
		TripleGraphGrammarFile editor = (TripleGraphGrammarFile) editorResource.getContents().get(0);
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("IFC2CityGML");
		new EditorTGGtoInternalTGG().generateInternalModels(editor, flattened, project).ifPresent(m -> new AttrCondDefLibraryProvider().generateAttrCondLibsAndStubs(m, project));
		// does not work because I can not get a project if not in an eclipse context like during build in a builder

		*/

		// Collection<BuilderExtension> extensions = ExtensionsUtil.collectExtensions("org.emoflon.ibex.tgg.ide.IbexTGGBuilderExtension", "class", BuilderExtension.class);
		// logger.info("Extensions: " + extensions.size());

		logger.info("Starting INITIAL FWD");
		long tic = System.currentTimeMillis();
		INITIAL_FWD_App init_fwd = new INITIAL_FWD_App();
		long toc = System.currentTimeMillis();
		logger.info("Completed init for FWD in: " + (toc - tic) + " ms");
		
		tic = System.currentTimeMillis();
		init_fwd.forward();
		toc = System.currentTimeMillis();
		logger.info("Completed INITIAL_FWD in: " + (toc - tic) + " ms");
		
		init_fwd.saveModels();
		init_fwd.terminate();
	}
}
