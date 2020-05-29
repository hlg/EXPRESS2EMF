package org.emoflon.ibex.tgg.run.expressemf.config;

import de.htwdd.expressEMF.express.impl.ExpressPackageImpl;
import de.htwdd.expressEMF.express.AttributeDecl;
import de.htwdd.expressEMF.express.EntityDecl;
import de.htwdd.expressEMF.express.ExpressPackage;
import java.io.IOException;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EcorePackageImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.emoflon.ibex.tgg.operational.csp.constraints.factories.expressemf.UserDefinedRuntimeTGGAttrConstraintFactory;
import org.emoflon.ibex.tgg.operational.defaults.IbexOptions;
import org.emoflon.ibex.tgg.compiler.defaults.IRegistrationHelper;
import org.emoflon.ibex.tgg.compiler.patterns.FilterNACStrategy;
import org.emoflon.ibex.tgg.operational.strategies.modules.IbexExecutable;
import org.emoflon.ibex.tgg.runtime.democles.DemoclesTGGEngine;

public class DemoclesRegistrationHelper implements IRegistrationHelper {

	/** Load and register source and target metamodels */
	public void registerMetamodels(ResourceSet rs, IbexExecutable executable) throws IOException {
		ExpressPackageImpl.init();
		EcorePackageImpl.init();
		rs.getPackageRegistry().put("platform:/resource/ExpressParser/model/generated/Express.ecore", ExpressPackage.eINSTANCE);
		rs.getPackageRegistry().put("platform:/plugin/org.eclipse.emf.ecore/model/Ecore.ecore", EcorePackage.eINSTANCE);
		// Replace to register generated code or handle other URI-related requirements
	}

	/** Create default options **/
	public IbexOptions createIbexOptions() {
		IbexOptions options = new IbexOptions();
		options.blackInterpreter(new DemoclesTGGEngine());
		options.project.name("ExpressEMF");
		options.project.path("ExpressEMF");
		options.debug.ibexDebug(true);
		options.patterns.lookAheadStrategy(FilterNACStrategy.NONE);
		options.patterns.ignoreInjectivity((EntityDecl,EClass)->true);
		options.patterns.ignoreInjectivity((AttributeDecl, EStructuralFeature)->true);
		options.patterns.ignoreInjectivity((SimpleDataType, EDataType)->true);
		options.csp.userDefinedConstraints(new UserDefinedRuntimeTGGAttrConstraintFactory());
		options.registrationHelper(this);
		return options;
	}
}
