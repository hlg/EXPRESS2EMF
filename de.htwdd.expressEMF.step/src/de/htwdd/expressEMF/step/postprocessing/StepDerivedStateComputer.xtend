package de.htwdd.expressEMF.step.postprocessing

import org.eclipse.xtext.resource.IDerivedStateComputer
import org.eclipse.xtext.resource.DerivedStateAwareResource
import de.htwdd.expressEMF.step.step.SimpleEntityInstance
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.util.EcoreUtil
import de.htwdd.expressEMF.step.step.UntypedParameter
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.EAttribute

class StepDerivedStateComputer implements IDerivedStateComputer {
	
	override discardDerivedState(DerivedStateAwareResource resource) {
		resource.allContents.filter(SimpleEntityInstance).forEach[ entity |
			// TODO reset transient instance
		]
	}
	
	override installDerivedState(DerivedStateAwareResource resource, boolean preLinkingPhase) {
		if(!preLinkingPhase) resource.allContents.filter(SimpleEntityInstance).forEach[ entity |
			// TODO transient attribute
			 var entityType = entity.type as EClass
			 var eObject = EcoreUtil.create(entityType) // TODO first pass - set to transient attribute (or implement as model transformation with a map)
			 for(var i=0; i<entityType.EAllStructuralFeatures.size; i++){
			 	var feature = entityType.EAllStructuralFeatures.get(i)
			 	var value = entity.parameters.values.get(i)
			 	if(value instanceof UntypedParameter){
			 		var entityValue = value as UntypedParameter
					if ( entityValue.e !== null && feature instanceof EReference){
						eObject.eSet(feature, entityValue.e) // TODO generated instance? // TODO second pass			 		
				 	}
				 	if ( entityValue.v !== null && feature instanceof EAttribute){
				 		eObject.eSet(feature, entityValue.v) // TODO type conversion
				 	}
				}
			 }
		]
	}
	
}