package de.htwdd.expressEMF.step.tests

import java.io.File
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.smoketest.XtextSmokeTestRunner
import org.eclipse.xtext.testing.util.ParseHelper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith
import com.google.inject.Inject
import com.google.inject.Injector
import de.htwdd.expressEMF.step.StepStandaloneSetup
import de.htwdd.expressEMF.step.step.Step
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

@ExtendWith(InjectionExtension)
@InjectWith(StepInjectorProvider)

class StepParserTests {
	@Inject package ParseHelper<Step> parserHelper

	@Test def void testIFC4() throws Exception {
		var Injector stepInjector = new StepStandaloneSetup().createInjectorAndDoEMFRegistration()
		var ResourceSet resourceSet = stepInjector.getInstance(XtextResourceSet)
		var Resource meta = resourceSet.getResource(
			URI.createFileURI(new File("testdata/IFC4.exprecore").getAbsolutePath()), true)
		var step = parserHelper.parse('''ISO-10303-21;
HEADER;
FILE_DESCRIPTION((''),'2;1');
FILE_NAME('','2018-12-21T13:07:44',(''),(''),'BuildingSmart IfcKit by Constructivity','IfcDoc 12.0.0.0','');
FILE_SCHEMA(('IFC4'));
ENDSEC;
DATA;
#1=IFCACTOR('abc',$,#22,#1);
#2=IFCACTIONREQUEST(#2);
ENDSEC;
END-ISO-10303-21;
''', resourceSet)
		assertEquals(2, step.data.entities.size)
		assertTrue(step.eResource.errors.empty)
		assertEquals("IfcActor", step.data.entities.get(0).type.name)
	}
}
