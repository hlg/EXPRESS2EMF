package de.htwdd.expressEMF.step.ui.internal

import de.htwdd.expressEMF.step.UMLRuntimeModule
import de.htwdd.expressEMF.step.ui.UMLUiModule
import de.htwdd.expressEMF.step.EcoreRuntimeModule
import de.htwdd.expressEMF.step.LanguageConstants

class StepActivatorEx extends StepActivator {

	override getRuntimeModule(String grammar) {
		if (grammar.isUMLLanguage) {
			return new UMLRuntimeModule
		}
		if (grammar.isEcoreLanguage){
			return new EcoreRuntimeModule
		}
		super.getRuntimeModule(grammar)
	}
	

	override getUiModule(String grammar) {
		if (grammar.isUMLLanguage || grammar.isEcoreLanguage) {
			return new UMLUiModule(this)
		}
		super.getUiModule(grammar)
	}

	private def isUMLLanguage(String grammar){
		LanguageConstants.ORG_ECLIPSE_UML2_UML.equals(grammar)
	}
	private def isEcoreLanguage(String grammar) {
		LanguageConstants.EXPRESS_ECORE.equals(grammar)
	}

}
