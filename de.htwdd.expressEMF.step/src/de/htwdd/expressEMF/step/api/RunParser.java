package de.htwdd.expressEMF.step.api;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.ecore.EcoreSupport;

import com.google.inject.Injector;

import de.htwdd.expressEMF.step.api.RunParser;
import de.htwdd.expressEMF.step.postprocessing.InstantiationTransformation;
import de.htwdd.expressEMF.ExpressStandaloneSetup;
import de.htwdd.expressEMF.step.StepStandaloneSetup;
import de.htwdd.expressEMF.step.step.SimpleEntityInstance;
import de.htwdd.expressEMF.step.step.Step;

public class RunParser {
	private final static Injector stepInjector = new StepStandaloneSetup().createInjectorAndDoEMFRegistration();
	
	private URI fileURI;
	private Resource stepResource;
	private XtextResourceSet stepResourceSet;
	
	public RunParser(String filePath) {
		fileURI = URI.createFileURI(filePath);
	}

	public Optional<Step> parse() throws IOException {

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("exprecore", new EcoreResourceFactoryImpl());
		stepResourceSet = stepInjector.getInstance(XtextResourceSet.class);
		Resource meta = stepResourceSet.getResource(URI.createURI("testdata/IFC4mod.exprecore"), true);
		new EcoreSupport().registerServices(true);
		stepResource = stepResourceSet.getResource(fileURI, true);
		Step step = (Step) stepResource.getContents().get(0);
		EPackage ifc4 = (EPackage) meta.getContents().get(0);
		boolean debug = true;
		if (debug) for (EClassifier classifier : ifc4.getEClassifiers()) {
			if (classifier instanceof EClass) {
				System.out.println(classifier.getName() + ": " + ((EClass) classifier).getEAllStructuralFeatures()
						.stream().map(c -> c.getName()).collect(Collectors.toList()));
			}

		}
		if (debug) for (SimpleEntityInstance ent : step.getData().getEntities()) {
			System.out.println(ent.getName() + " " + ent.getType().getName());
		}
		List<EObject> instantiated = new InstantiationTransformation().transform(step);

		Resource xmi = stepResourceSet.createResource(fileURI.trimFileExtension().appendFileExtension("inst.xmi"));
		xmi.getContents().addAll(instantiated);
		xmi.save(null);

		for (EObject obj : instantiated) {
			System.out.println("=================");
			System.out.println(obj.eClass());
			for (EStructuralFeature feature : obj.eClass().getEAllStructuralFeatures()) {
				System.out.println("  " + feature.getName() + ": " + obj.eGet(feature));
			}

		}

		// Validation
		IResourceValidator validator = ((XtextResource) stepResource).getResourceServiceProvider().getResourceValidator();
		List<Issue> issues = validator.validate(stepResource, CheckMode.ALL, CancelIndicator.NullImpl);
		for (Issue issue : issues) {
			System.err.println(issue.getMessage());
		}
		if (issues.isEmpty())
			return Optional.of((de.htwdd.expressEMF.step.step.Step) stepResource.getContents().get(0));
		return Optional.empty();
	}

	public void saveXmi() {
		EcoreUtil.resolveAll(stepResource);
		Resource xmiResource = stepResourceSet.createResource(fileURI.trimFileExtension().appendFileExtension("xmi"));
		xmiResource.getContents().add(stepResource.getContents().get(0));
		try {
			xmiResource.save(null);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException {
		RunParser parser = new RunParser(args[0]);
		Step stepFile = parser.parse().get();
		new InstantiationTransformation().transform(stepFile);
		parser.saveXmi();

	}
}