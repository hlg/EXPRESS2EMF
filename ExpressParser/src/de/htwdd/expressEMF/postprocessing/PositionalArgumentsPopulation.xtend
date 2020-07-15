package de.htwdd.expressEMF.postprocessing

import de.htwdd.expressEMF.express.AttributeDecl
import de.htwdd.expressEMF.express.EntityDecl
import de.htwdd.expressEMF.express.ExpressPackage
import org.eclipse.xtext.resource.DerivedStateAwareResource
import org.eclipse.xtext.resource.IDerivedStateComputer

class PositionalArgumentsPopulation implements IDerivedStateComputer {
	
	override discardDerivedState(DerivedStateAwareResource resource) {
		resource.allContents.filter(AttributeDecl).forEach[ AttributeDecl attr |
			attr.eUnset(ExpressPackage.eINSTANCE.attributeDecl_Position)
		]
	}
	
	override installDerivedState(DerivedStateAwareResource resource, boolean preLinkingPhase) {
		if(!preLinkingPhase)
		resource.allContents.filter(EntityDecl).forEach[ entity |
			entity.attrs.map[attrs].flatten.forEach[a,i|
				a.position = i
			]
		]
	}
	

}
