/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.client.security;

import org.elasticsearch.client.Validatable;
import org.elasticsearch.client.security.user.privileges.Role;
import org.elasticsearch.core.Nullable;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Objects;

/**
 * Request object to create or update a role.
 */
public final class PutRoleRequest implements Validatable, ToXContentObject {

    private final Role role;
    private final RefreshPolicy refreshPolicy;

    public PutRoleRequest(Role role, @Nullable final RefreshPolicy refreshPolicy) {
        this.role = Objects.requireNonNull(role);
        this.refreshPolicy = (refreshPolicy == null) ? RefreshPolicy.getDefault() : refreshPolicy;
    }

    public Role getRole() {
        return role;
    }

    public RefreshPolicy getRefreshPolicy() {
        return refreshPolicy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, refreshPolicy);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PutRoleRequest other = (PutRoleRequest) obj;

        return (refreshPolicy == other.getRefreshPolicy()) &&
               Objects.equals(role, other.role);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (role.getApplicationPrivileges() != null) {
            builder.field(Role.APPLICATIONS.getPreferredName(), role.getApplicationPrivileges());
        }
        if (role.getClusterPrivileges() != null) {
            builder.field(Role.CLUSTER.getPreferredName(), role.getClusterPrivileges());
        }
        if (role.getGlobalPrivileges() != null) {
            builder.field(Role.GLOBAL.getPreferredName(), role.getGlobalPrivileges());
        }
        if (role.getIndicesPrivileges() != null) {
            builder.field(Role.INDICES.getPreferredName(), role.getIndicesPrivileges());
        }
        if (role.getMetadata() != null) {
            builder.field(Role.METADATA.getPreferredName(), role.getMetadata());
        }
        if (role.getRunAsPrivilege() != null) {
            builder.field(Role.RUN_AS.getPreferredName(), role.getRunAsPrivilege());
        }
        return builder.endObject();
    }

}
