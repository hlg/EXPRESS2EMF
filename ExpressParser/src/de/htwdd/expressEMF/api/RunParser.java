package de.htwdd.expressEMF.api;


import java.io.IOException;import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import com.google.inject.Injector;

import de.htwdd.expressEMF.ExpressStandaloneSetup;
import de.htwdd.expressEMF.express.Declaration;
import de.htwdd.expressEMF.express.EntityDecl;
import de.htwdd.expressEMF.express.ExpressFactory;
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
		// resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("exp", new XMLResourceFactoryImpl());
		resourceSet = injector.getInstance(XtextResourceSet.class);
		resource = resourceSet.getResource(fileURI, true);
		SchemaDecl schema = (SchemaDecl) resource.getContents().get(0);
		EntityDecl newEntityDecl = ExpressFactory.eINSTANCE.createEntityDecl();
		newEntityDecl.setName("Programmatic");
		schema.getDeclarations().add(newEntityDecl);
		resource.save(null);

		// Validation
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
		RunParser parser = new RunParser(args[0]);
		SchemaDecl schema = parser.parse().get();
		parser.saveXmi();
	
	}
}