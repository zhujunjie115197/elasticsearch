/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.upgrade.actions;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.test.AbstractWireSerializingTestCase;
import org.elasticsearch.xpack.core.upgrade.actions.IndexUpgradeAction.Request;

public class IndexUpgradeActionRequestTests extends AbstractWireSerializingTestCase<Request> {
    @Override
    protected Request createTestInstance() {
        return new Request(randomAlphaOfLength(10));
    }

    @Override
    protected Writeable.Reader<Request> instanceReader() {
        return Request::new;
    }
}
