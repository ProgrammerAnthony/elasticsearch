/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.core.security.action.rolemapping;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;

/**
 * Response when adding/updating a role-mapping.
 *
 * see org.elasticsearch.xpack.security.authc.support.mapper.NativeRoleMappingStore
 */
public class PutRoleMappingResponse extends ActionResponse implements ToXContentObject {

    private boolean created;

    public PutRoleMappingResponse(StreamInput in) throws IOException {
        super(in);
        this.created = in.readBoolean();
    }

    public PutRoleMappingResponse(boolean created) {
        this.created = created;
    }

    public boolean isCreated() {
        return created;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject().field("created", created).endObject();
        return builder;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeBoolean(created);
    }

    }
