package org.emoflon.ibex.tgg.operational.csp.constraints.custom.expressemf;

import org.emoflon.ibex.tgg.operational.csp.RuntimeTGGAttributeConstraint;
import org.emoflon.ibex.tgg.operational.csp.RuntimeTGGAttributeConstraintVariable;

public class UserDefined_eq_booleanPrimitive extends RuntimeTGGAttributeConstraint
{

   /**
    * Constraint eq_booleanPrimitive(v0, v1)
    * 
    * @see TGGLanguage.csp.impl.ConstraintImpl#solve()
    */
	@Override
	public void solve() {
		if (variables.size() != 2)
			throw new RuntimeException("The CSP -EQ_BOOLEANPRIMITIVE- needs exactly 2 variables");

		RuntimeTGGAttributeConstraintVariable v0 = variables.get(0);
		RuntimeTGGAttributeConstraintVariable v1 = variables.get(1);
		String bindingStates = getBindingStates(v0, v1);

	  	switch(bindingStates) {
	  		case "BB": 
	  			setSatisfied(v0.getValue().equals(v1.getValue()));
	  			break;
	  		case "BF": 
	  			v1.bindToValue(v0.getValue());
	  			setSatisfied(true);
	  			break;
	  		case "FB":
	  			v0.bindToValue(v1.getValue());
	  			setSatisfied(true);
	  			break;
	  		case "FF":
	  			boolean value = Math.random()>0.5; 
	  			v0.bindToValue(value);
	  			v1.bindToValue(value);
	  			setSatisfied(true);
	  			break;
	  		default:  throw new UnsupportedOperationException("This case in the constraint has not been implemented yet: " + bindingStates);
	  		 	}
	  	}
}

