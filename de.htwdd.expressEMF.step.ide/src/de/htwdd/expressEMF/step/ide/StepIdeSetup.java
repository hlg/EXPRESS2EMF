/*
 * generated by Xtext 2.20.0
 */
package de.htwdd.expressEMF.step.ide;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.htwdd.expressEMF.step.StepRuntimeModule;
import de.htwdd.expressEMF.step.StepStandaloneSetup;
import org.eclipse.xtext.util.Modules2;

/**
 * Initialization support for running Xtext languages as language servers.
 */
public class StepIdeSetup extends StepStandaloneSetup {

	@Override
	public Injector createInjector() {
		return Guice.createInjector(Modules2.mixin(new StepRuntimeModule(), new StepIdeModule()));
	}
	
}
