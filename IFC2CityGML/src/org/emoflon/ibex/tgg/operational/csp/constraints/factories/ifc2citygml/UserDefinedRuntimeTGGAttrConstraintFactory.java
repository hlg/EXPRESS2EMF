package org.emoflon.ibex.tgg.operational.csp.constraints.factories.ifc2citygml;

import java.util.HashMap;
import java.util.HashSet;			

import org.emoflon.ibex.tgg.operational.csp.constraints.factories.RuntimeTGGAttrConstraintFactory;			

import org.emoflon.ibex.tgg.operational.csp.constraints.custom.ifc2citygml.UserDefined_rand_Enum;

public class UserDefinedRuntimeTGGAttrConstraintFactory extends RuntimeTGGAttrConstraintFactory {

	public UserDefinedRuntimeTGGAttrConstraintFactory() {
		super();
	}
	
	@Override
	protected void initialize() {
		creators = new HashMap<>();
		creators.put("rand_Enum", () -> new UserDefined_rand_Enum());

		constraints = new HashSet<String>();
		constraints.addAll(creators.keySet());
	}
}
