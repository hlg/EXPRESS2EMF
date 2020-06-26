/*******************************************************************************
 * Copyright (c) 2019 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package de.htwdd.expressEMF.step.ui

import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory
import de.htwdd.expressEMF.step.ui.internal.StepActivatorEx
import de.htwdd.expressEMF.step.LanguageConstants

class UMLExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	override protected getBundle() {
		StepActivatorEx.instance.bundle
	}

	override protected getInjector() {
		StepActivatorEx.instance.getInjector(LanguageConstants.ORG_ECLIPSE_UML2_UML)
	}

}
