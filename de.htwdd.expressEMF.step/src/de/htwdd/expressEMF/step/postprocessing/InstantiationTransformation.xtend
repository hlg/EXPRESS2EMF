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
		for(feature : entityType.EAllStructuralFeatures){
			var superPositions = feature.EContainingClass.EAllSuperTypes.map[type | type.EStructuralFeatures.empty ? 0 : type.EStructuralFeatures.map[sf | Integer.valueOf( sf.getEAnnotation("Express_emoflon").details.get("position"))].max + 1 ]
			val basePos = superPositions.empty ? 0 : superPositions.reduce[basePos, maxPos | basePos + maxPos]
			System.out.println("Base position for "+feature.EContainingClass.name + ": " + basePos + ", super positions: " + superPositions + ", super types: " + feature.EContainingClass.EAllSuperTypes)
			// TODO: These base positions need to be cached! 
			val position = basePos + Integer.valueOf(feature.getEAnnotation("Express_emoflon").details.get("position"))
			if(e.parameters.values.size <= position){
				System.out.println("Warning: no value for feature '" + feature.name + "' in " + entityType.name + " " + e.name + ", position: "+ position + ", parameters: " + e.parameters.values.size)
			} else {
				var value = e.parameters.values.get(position)
				if(value instanceof UntypedParameter){
					var entityValue = value as UntypedParameter
					if ( entityValue.e !== null && feature instanceof EReference && (feature.EType as EClass).isSuperTypeOf(entityValue.e.type)){
						result.eSet(feature, entityValue.e.copyInstance)		 		
					}
					if ( entityValue.v !== null && feature instanceof EAttribute ){ // && feature.EType.instanceClass.isAssignableFrom(entityValue.v.class)
						if (feature.EType.instanceClass.equals(String))
							result.eSet(feature, entityValue.v) 
						else if (feature.EType.instanceClass.equals(Double) && ! '$'.equals(entityValue.v))
							result.eSet(feature, Double.valueOf(entityValue.v))
						else {
						// TODO type conversion							
						}

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
		System.out.println(entity.type.name)
		EcoreUtil.create(entity.type as EClass)
	}
	
}