/*******************************************************************************
 * Copyright (c) 2019 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package de.htwdd.expressEMF.step

import org.eclipse.xtext.resource.generic.AbstractGenericResourceRuntimeModule
import org.eclipse.xtext.naming.DefaultDeclarativeQualifiedNameProvider

/**
 * This class is used to configure the runtime dependency injection (DI) container for the UML language.
 */
class EcoreRuntimeModule extends AbstractGenericResourceRuntimeModule {

	override protected getFileExtensions() {
		'exprecore'
	}

	override protected getLanguageName() {
		LanguageConstants.EXPRESS_ECORE
	}

	override bindIQualifiedNameProvider() {
		DefaultDeclarativeQualifiedNameProvider
	}
}
