package org.emoflon.ibex.tgg.run.ifc2citygml.config;

import java.io.IOException;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EcorePackageImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.emoflon.ibex.tgg.operational.csp.constraints.factories.ifc2citygml.UserDefinedRuntimeTGGAttrConstraintFactory;
import org.emoflon.ibex.tgg.operational.defaults.IbexOptions;
import org.emoflon.ibex.tgg.compiler.defaults.IRegistrationHelper;
import org.emoflon.ibex.tgg.compiler.patterns.FilterNACStrategy;
import org.emoflon.ibex.tgg.operational.strategies.modules.IbexExecutable;
import org.emoflon.ibex.tgg.runtime.democles.DemoclesTGGEngine;

public class DemoclesRegistrationHelper implements IRegistrationHelper {

	/** Load and register source and target metamodels */
	public void registerMetamodels(ResourceSet rs, IbexExecutable executable) throws IOException {

		// Replace to register generated code or handle other URI-related requirements
		executable.getResourceHandler().loadAndRegisterMetamodel("platform:/resource/IFC2CityGML/model/IFC4mod.ecore");
		executable.getResourceHandler().loadAndRegisterMetamodel("platform:/resource/IFC2CityGML/model/CityGML_Model.ecore");
	}

	/** Create default options **/
	public IbexOptions createIbexOptions() {
		IbexOptions options = new IbexOptions();
		options.blackInterpreter(new DemoclesTGGEngine());
		options.project.name("IFC2CityGML");
		options.project.path("IFC2CityGML");
		options.debug.ibexDebug(true);
		options.patterns.lookAheadStrategy(FilterNACStrategy.NONE);
		options.csp.userDefinedConstraints(new UserDefinedRuntimeTGGAttrConstraintFactory());
		options.registrationHelper(this);
		return options;
	}
}
