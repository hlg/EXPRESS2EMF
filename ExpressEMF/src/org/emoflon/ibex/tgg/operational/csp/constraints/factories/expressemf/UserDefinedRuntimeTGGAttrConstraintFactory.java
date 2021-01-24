package org.emoflon.ibex.tgg.operational.csp.constraints.factories.expressemf;

import java.util.HashMap;
import java.util.HashSet;			

import org.emoflon.ibex.tgg.operational.csp.constraints.factories.RuntimeTGGAttrConstraintFactory;			

import org.emoflon.ibex.tgg.operational.csp.constraints.custom.expressemf.UserDefined_eq_booleanPrimitive;

public class UserDefinedRuntimeTGGAttrConstraintFactory extends RuntimeTGGAttrConstraintFactory {

	public UserDefinedRuntimeTGGAttrConstraintFactory() {
		super();
	}
	
	@Override
	protected void initialize() {
		creators = new HashMap<>();
		creators.put("eq_booleanPrimitive", () -> new UserDefined_eq_booleanPrimitive());

		constraints = new HashSet<String>();
		constraints.addAll(creators.keySet());
	}
}
