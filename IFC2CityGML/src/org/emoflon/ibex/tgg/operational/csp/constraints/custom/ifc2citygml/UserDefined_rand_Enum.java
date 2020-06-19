package org.emoflon.ibex.tgg.operational.csp.constraints.custom.ifc2citygml;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.emf.ecore.EEnum;
import org.emoflon.ibex.tgg.operational.csp.RuntimeTGGAttributeConstraint;
import org.emoflon.ibex.tgg.operational.csp.RuntimeTGGAttributeConstraintVariable;

import language.TGGAttributeExpression;

public class UserDefined_rand_Enum extends RuntimeTGGAttributeConstraint
{

   /**
    * Constraint rand_Enum(v0)
    * 
    * @see TGGLanguage.csp.impl.ConstraintImpl#solve()
    */
	@Override
	public void solve() {
		if (variables.size() != 1)
			throw new RuntimeException("The CSP -RAND_ENUM- needs exactly 1 variables");

		RuntimeTGGAttributeConstraintVariable v0 = variables.get(0);
		String bindingStates = getBindingStates(v0);

	  	switch(bindingStates) {
  			case "B": 
	  			setSatisfied(true);
	  			break;
	  		case "F":
	  			// this is a hack, I want the type from v0.getType() where it is null
	  			EEnum type = (EEnum) ((List<Pair<TGGAttributeExpression, Object>>) this.getBoundAttrExprValues()).get(0).getLeft().getAttribute().getEAttributeType();
	  			// TODO move random enum literal generation to generateValue()
	  			int value = (int) Math.floor(Math.random()*type.getELiterals().size());
	  			v0.bindToValue(type.getELiterals().get(value).getInstance());
	  			setSatisfied(true);
	  			break;
	  		default:  throw new UnsupportedOperationException("This case in the constraint has not been implemented yet: " + bindingStates);
	  		 	}
	  	}
}

