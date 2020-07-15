package de.htwdd.expressEMF.postprocessing

import org.eclipse.xtext.xtext.ecoreInference.IXtext2EcorePostProcessor
import org.eclipse.xtext.GeneratedMetamodel
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EClass
import de.htwdd.expressEMF.express.AttributeDecl
import org.eclipse.emf.ecore.EcoreFactory
import org.eclipse.emf.ecore.EcorePackage

class PositionalArgumentsTransient implements IXtext2EcorePostProcessor {
  
  override process(GeneratedMetamodel metamodel) {
    metamodel.EPackage.process
  }
  
  def process(EPackage p) {
    for (clazz : p.EClassifiers.filter(typeof(EClass))) {
      if (clazz.name == typeof(AttributeDecl).simpleName) {
        val typeAttribute = EcoreFactory::eINSTANCE.createEAttribute
        typeAttribute.name = "position"
        typeAttribute.EType = EcorePackage::eINSTANCE.EInt
        clazz.EStructuralFeatures += typeAttribute
      }
    }
  }
}