package ca.bc.gov.ols.router.testing.engine.hibernate;

import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.sql.ast.tree.SqlAstNode;
import org.hibernate.type.BasicTypeReference;
import org.hibernate.type.SqlTypes;

import java.util.List;

public class GetBinaryPartitionSQLFunction extends StandardSQLFunction {

    private static final BasicTypeReference<String> RETURN_TYPE = new BasicTypeReference<>("string", String.class, SqlTypes.VARCHAR);

    public GetBinaryPartitionSQLFunction(final String functionName) {
        super(functionName, true, RETURN_TYPE);
    }

    @Override
    public void render(SqlAppender sqlAppender, List<? extends SqlAstNode> arguments, SqlAstTranslator<?> translator) {
        if (arguments.size() != 2) {
            throw new IllegalArgumentException(String.format("Function '%s' requires exactly 2 arguments", getName()));
        }

        sqlAppender.append("(");
        arguments.get(0).accept(translator);
        sqlAppender.append(" ->> ");
        arguments.get(1).accept(translator);
        sqlAppender.append(")");
    }

}

