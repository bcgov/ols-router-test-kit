package ca.bc.gov.ols.router.testing.engine.hibernate;

import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.boot.model.FunctionContributions;


public class PgFunctionContributor implements FunctionContributor {
	@Override
	public void contributeFunctions(FunctionContributions functionContributions) {

		//get_binary_partition_sig(json_data jsonb, key_name TEXT)
		functionContributions.getFunctionRegistry()
        .register("get_binary_partition_sig", new GetBinaryPartitionSQLFunction("get_binary_partition_sig"));
	}
}



