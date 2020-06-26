package de.htwdd.expressEMF.step.api;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import com.google.inject.Injector;

import de.htwdd.expressEMF.step.api.RunParser;
import de.htwdd.expressEMF.step.StepStandaloneSetup;
import de.htwdd.expressEMF.step.step.Step;

public class RunParser {
	private final static Injector injector = new StepStandaloneSetup().createInjectorAndDoEMFRegistration();
	
	private URI fileURI;
	private Resource resource;
	private XtextResourceSet resourceSet;
	
	public RunParser(String filePath) {
		fileURI = URI.createFileURI(filePath);
	}
	
	public Optional<Step> parse() {
		// resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("exp", new XMLResourceFactoryImpl());
		resourceSet = injector.getInstance(XtextResourceSet.class);
		resource = resourceSet.getResource(fileURI, true);
		
		// Validation
		IResourceValidator validator = ((XtextResource) resource).getResourceServiceProvider().getResourceValidator();
		List<Issue> issues = validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl);
		for (Issue issue : issues) {
			System.err.println(issue.getMessage());
		}
		if (issues.isEmpty()) return Optional.of((de.htwdd.expressEMF.step.step.Step) resource.getContents().get(0));
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
	public static void main(String[] args) {
		RunParser parser = new RunParser(args[0]);
		Step stepFile = parser.parse().get();
		parser.saveXmi();
	
	}
}