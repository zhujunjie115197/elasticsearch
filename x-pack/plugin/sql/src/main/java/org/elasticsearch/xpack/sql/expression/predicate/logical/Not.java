/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.sql.expression.predicate.logical;

import org.elasticsearch.xpack.sql.expression.Expression;
import org.elasticsearch.xpack.sql.expression.Expressions;
import org.elasticsearch.xpack.sql.expression.Expressions.ParamOrdinal;
import org.elasticsearch.xpack.sql.expression.function.scalar.UnaryScalarFunction;
import org.elasticsearch.xpack.sql.expression.gen.processor.Processor;
import org.elasticsearch.xpack.sql.expression.gen.script.Scripts;
import org.elasticsearch.xpack.sql.expression.predicate.BinaryOperator.Negateable;
import org.elasticsearch.xpack.sql.tree.Location;
import org.elasticsearch.xpack.sql.tree.NodeInfo;
import org.elasticsearch.xpack.sql.type.DataType;

public class Not extends UnaryScalarFunction {

    public Not(Location location, Expression child) {
        super(location, child);
    }

    @Override
    protected NodeInfo<Not> info() {
        return NodeInfo.create(this, Not::new, field());
    }

    @Override
    protected Not replaceChild(Expression newChild) {
        return new Not(location(), newChild);
    }

    @Override
    protected TypeResolution resolveType() {
        if (DataType.BOOLEAN == field().dataType()) {
            return TypeResolution.TYPE_RESOLVED;
        }
        return Expressions.typeMustBeBoolean(field(), functionName(), ParamOrdinal.DEFAULT);
    }

    @Override
    public Object fold() {
        return NotProcessor.INSTANCE.process(field().fold());
    }

    @Override
    protected Processor makeProcessor() {
        return NotProcessor.INSTANCE;
    }

    @Override
    public String processScript(String script) {
        return Scripts.formatTemplate(Scripts.SQL_SCRIPTS + ".not(" + script + ")");
    }

    @Override
    protected Expression canonicalize() {
        Expression canonicalChild = field().canonical();
        if (canonicalChild instanceof Negateable) {
            return ((Negateable) canonicalChild).negate();
        }
        return this;
    }

    @Override
    public DataType dataType() {
        return DataType.BOOLEAN;
    }
}
