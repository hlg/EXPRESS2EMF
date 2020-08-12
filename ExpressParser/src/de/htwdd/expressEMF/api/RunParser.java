package de.htwdd.expressEMF.api;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import com.google.inject.Injector;

import de.htwdd.expressEMF.express.ExpressPackage;
import de.htwdd.expressEMF.express.SchemaDecl;

public class RunParser {
	private final static Injector injector = new de.htwdd.expressEMF.ExpressStandaloneSetup().createInjectorAndDoEMFRegistration();
	
	private URI fileURI;
	private Resource resource;
	private XtextResourceSet resourceSet;
	
	public RunParser(String filePath) {
		fileURI = URI.createFileURI(filePath);
	}
	
	public Optional<SchemaDecl> parse() throws IOException {
		EPackage.Registry.INSTANCE.put(ExpressPackage.eNS_URI, ExpressPackage.eINSTANCE);
		resourceSet = injector.getInstance(XtextResourceSet.class);
		resource = resourceSet.getResource(fileURI, true);
		IResourceValidator validator = ((XtextResource) resource).getResourceServiceProvider().getResourceValidator();
		List<Issue> issues = validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl);
		for (Issue issue : issues) {
			System.err.println(issue.getMessage());
		}
		if (issues.isEmpty()) return Optional.of((SchemaDecl) resource.getContents().get(0));
		return Optional.empty();
	}

	public void saveXmi() {
	    EcoreUtil.resolveAll(resource);
	    Resource xmiResource = resourceSet.createResource(fileURI.trimFileExtension().appendFileExtension("xmi"));
	    xmiResource.getContents().add(resource.getContents().get(0));
	    try {
	        xmiResource.save(null);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	}
	public static void main(String[] args) throws IOException {
		String schemaFile = args.length > 0 ? args[0] : "schemas/IFC4.exp";
		RunParser runParser = new RunParser(schemaFile);
		Optional<SchemaDecl> schema = runParser.parse();
		if(schema.isPresent()) {
			runParser.saveXmi();
		} 
	
	}
}