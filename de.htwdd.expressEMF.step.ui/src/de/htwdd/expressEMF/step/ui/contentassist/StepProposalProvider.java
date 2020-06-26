/*
 * generated by Xtext 2.20.0
 */
package de.htwdd.expressEMF.step.ui.contentassist;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.CrossReference;
import org.eclipse.xtext.GrammarUtil;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;

/**
 * See https://www.eclipse.org/Xtext/documentation/310_eclipse_support.html#content-assist
 * on how to customize the content assistant.
 */
public class StepProposalProvider extends AbstractStepProposalProvider {
	@Override
	public void completeSimpleEntityInstance_Type(EObject model, Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		IScope scope = getScopeProvider().getScope(model, GrammarUtil.getReference((CrossReference) assignment.getTerminal()));
		for (IEObjectDescription el : scope.getAllElements()) {
			acceptor.accept(createCompletionProposal(el.getName().toUpperCase().toString(), context));
		}
		super.completeSimpleEntityInstance_Type(model, assignment, context, acceptor);
	}
}
