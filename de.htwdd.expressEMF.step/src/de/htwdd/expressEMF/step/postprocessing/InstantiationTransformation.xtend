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
import org.eclipse.emf.ecore.EcoreFactory
import de.htwdd.expressEMF.step.step.TypedParameter
import org.eclipse.emf.ecore.EStructuralFeature
import java.util.ArrayList
import java.util.HashSet
import java.util.Set

class InstantiationTransformation {
	EcoreFactory factory = EcoreFactory::eINSTANCE
	// List<EObject> resources = new ArrayList()
	
	def List<EObject> transform(Step step){
		step.data.entities.map[copyInstance]
	}

	def EObject create result: instantiate(e) copyInstance(SimpleEntityInstance e) {
		if(e.type.EAllStructuralFeatures.size!= e.parameters.values.size){
			System.out.println("Warning: feature ("+e.type.EAllStructuralFeatures.size+") and parameter size ("+e.parameters.values.size+") not matching during instantiation: "+e.name)
		}
		for(feature : e.type.EAllStructuralFeatures){
			val position = expressPositionOf(feature)
			if(e.parameters.values.size <= position){
				System.out.println("Warning: no value for feature '" + feature.name + "' in " + e.type.name + " " + e.name + ", position: "+ position + ", parameters: " + e.parameters.values.size)
			} else {
				var value = e.parameters.values.get(position)
				if(value instanceof UntypedParameter){
					if ( value.e !== null && feature instanceof EReference ){ // entity reference
						if ((feature.EType as EClass).isSuperTypeOf(value.e.type)){
							result.eSet(feature, value.e.copyInstance)
						}		 		
					}
					if ( value.v !== null && feature instanceof EReference ){ // reference to defined type (TODO: enumeration)
						var definedType = feature.EType as EClass
						if (definedType.EAllAttributes.exists[it.name=="value"]) { // TODO change recognition to be based on schema annotation instead of attribute name
							var wrappedValueFeature = definedType.EAttributes.findFirst[it.name=="value"]
							var definedTypeObject = EcoreUtil.create(definedType)
							// resources.add(definedTypeObject)
							result.eSet(feature, definedTypeObject)
							definedTypeObject.setConverted(wrappedValueFeature, value.v)
						}
					}
					if ( value.v !== null && feature instanceof EAttribute ){ // attribute (TODO: enumeration)
						result.setConverted(feature, value.v)
					}
					// TODO warning if type cannot be inferred
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
					if (value instanceof TypedParameter && feature instanceof EReference){
						var typedValue = value as TypedParameter
						var param = typedValue.parameter
						var type = typedValue.type
						// entity reference like above, but type does not need to be taken from schema (verify compatibility though) 
					}
					if (value instanceof TypedParameter && feature instanceof EAttribute){
						// defined type like above, but type does not need to be taken from schema (verify compatibility though) 
					}
				}
			}
		}
	}
	
	def void setConverted(EObject object, EStructuralFeature feature, String value){
		// TODO dispatch method?
		if ('$'.equals(value)) // && feature.EType.instanceClass.isAssignableFrom(entit7yValue.v.class)
			object.eSet(feature, null) // necessary or just skip?
		else if (feature.EType.instanceClass.equals(String))
			object.eSet(feature, value) 
		else if (feature.EType.instanceClass.equals(Double))
			object.eSet(feature, Double.valueOf(value))
		else {		
		// TODO further simpletype/datatype conversion							
		}
	}
	
	def EObject instantiate(SimpleEntityInstance entity){
		System.out.println(entity.type.name)
		EcoreUtil.create(entity.type)
	}

	def int expressPositionOf(EStructuralFeature feature){
		var superPositions = feature.EContainingClass.EAllSuperTypes.map[supertype | 
			supertype.EStructuralFeatures.empty ? 0 : supertype.EStructuralFeatures.map[supertypeFeature | 
				Integer.valueOf( supertypeFeature.getEAnnotation("Express_emoflon").details.get("position"))
			].max + 1
		]
		val basePos = superPositions.empty ? 0 : superPositions.reduce[basePos, maxPos | basePos + maxPos]
		System.out.println("Base position for "+feature.EContainingClass.name + ": " + basePos + ", super positions: " + superPositions + ", super types: " + feature.EContainingClass.EAllSuperTypes)
		// TODO: These base positions need to be cached! 
		basePos + Integer.valueOf(feature.getEAnnotation("Express_emoflon").details.get("position"))
	}	
}