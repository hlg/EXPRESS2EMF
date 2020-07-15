package de.htwdd.expressEMF.step.postprocessing

import de.htwdd.expressEMF.step.step.Step
import org.eclipse.emf.ecore.EObject
import de.htwdd.expressEMF.step.step.SimpleEntityInstance
import org.eclipse.emf.ecore.EClass
import de.htwdd.expressEMF.step.step.UntypedParameter
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.util.EcoreUtil
import java.util.List
import de.htwdd.expressEMF.step.step.ParameterList
import org.eclipse.emf.common.util.EList

class InstantiationTransformation {
	def List<EObject> transform(Step step){
		step.data.entities.map[copyInstance]
	}

	def EObject create result: instantiate(e) copyInstance(SimpleEntityInstance e) {
		var entityType = e.type as EClass
		if(entityType.EAllStructuralFeatures.size!= e.parameters.values.size){
			System.out.println("Warning: feature ("+entityType.EAllStructuralFeatures.size+") and parameter size ("+e.parameters.values.size+") not matching during instantiation: "+e.name)
		}
		for(var i=0; i<entityType.EAllStructuralFeatures.size; i++){
			var feature = entityType.EAllStructuralFeatures.get(i)
			if(e.parameters.values.size <= i){
				System.out.println("Warning: no value for feature '" + feature.name + "' in " + entityType.name + " " + e.name)
			} else {
				var value = e.parameters.values.get(i)
				if(value instanceof UntypedParameter){
					var entityValue = value as UntypedParameter
					if ( entityValue.e !== null && feature instanceof EReference && (feature.EType as EClass).isSuperTypeOf(entityValue.e.type)){
						result.eSet(feature, entityValue.e.copyInstance)		 		
					}
					if ( entityValue.v !== null && feature instanceof EAttribute && feature.EType.instanceClass.isAssignableFrom(entityValue.v.class)){
						result.eSet(feature, entityValue.v) // TODO type conversion
					}
					if (value instanceof ParameterList && feature.many) { // TODO check, multidimensional arrays
						for (listvalue : (value as ParameterList).values) {
							if (listvalue instanceof UntypedParameter){
								var listEntVal = listvalue as UntypedParameter
							 	if(listEntVal.e !== null && feature instanceof EReference && (feature.EType as EClass).isSuperTypeOf(listEntVal.e.type)){
									(result.eGet(feature) as EList).add(listEntVal.e.copyInstance) 		
							 	}
							}
						}
					}
				}
			}
		}
	}
	
	def EObject instantiate(SimpleEntityInstance entity){
		EcoreUtil.create(entity.type as EClass)
	}
	
}