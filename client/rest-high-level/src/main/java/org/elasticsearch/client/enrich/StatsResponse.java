/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */
package org.elasticsearch.client.enrich;

import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.tasks.TaskInfo;

import java.util.List;
import java.util.Objects;

public final class StatsResponse {

    private static final ParseField EXECUTING_POLICIES_FIELD = new ParseField("executing_policies");
    private static final ParseField COORDINATOR_STATS_FIELD = new ParseField("coordinator_stats");
    private static final ParseField CACHE_STATS_FIELD = new ParseField("cache_stats");

    @SuppressWarnings("unchecked")
    private static final ConstructingObjectParser<StatsResponse, Void> PARSER = new ConstructingObjectParser<>(
        "stats_response",
        true,
        args -> new StatsResponse((List<ExecutingPolicy>) args[0], (List<CoordinatorStats>) args[1], (List<CacheStats>) args[2])
    );

    static {
        PARSER.declareObjectArray(ConstructingObjectParser.constructorArg(), ExecutingPolicy.PARSER::apply, EXECUTING_POLICIES_FIELD);
        PARSER.declareObjectArray(ConstructingObjectParser.constructorArg(), CoordinatorStats.PARSER::apply, COORDINATOR_STATS_FIELD);
        PARSER.declareObjectArray(ConstructingObjectParser.constructorArg(), CacheStats.PARSER::apply, CACHE_STATS_FIELD);
    }

    public static StatsResponse fromXContent(XContentParser parser) {
        return PARSER.apply(parser, null);
    }

    private final List<ExecutingPolicy> executingPolicies;
    private final List<CoordinatorStats> coordinatorStats;
    private final List<CacheStats> cacheStats;

    public StatsResponse(List<ExecutingPolicy> executingPolicies, List<CoordinatorStats> coordinatorStats, List<CacheStats> cacheStats) {
        this.executingPolicies = executingPolicies;
        this.coordinatorStats = coordinatorStats;
        this.cacheStats = cacheStats;
    }

    public List<ExecutingPolicy> getExecutingPolicies() {
        return executingPolicies;
    }

    public List<CoordinatorStats> getCoordinatorStats() {
        return coordinatorStats;
    }

    public List<CacheStats> getCacheStats() {
        return cacheStats;
    }

    public static final class CoordinatorStats {

        static ParseField NODE_ID_FIELD = new ParseField("node_id");
        static ParseField QUEUE_SIZE_FIELD = new ParseField("queue_size");
        static ParseField REMOTE_REQUESTS_CONCURRENT_FIELD = new ParseField("remote_requests_current");
        static ParseField REMOTE_REQUESTS_TOTAL_FIELD = new ParseField("remote_requests_total");
        static ParseField EXECUTED_SEARCHES_FIELD = new ParseField("executed_searches_total");

        private static final ConstructingObjectParser<CoordinatorStats, Void> PARSER = new ConstructingObjectParser<>(
            "coordinator_stats_item",
            true,
            args -> new CoordinatorStats((String) args[0], (int) args[1], (int) args[2], (long) args[3], (long) args[4])
        );

        static {
            PARSER.declareString(ConstructingObjectParser.constructorArg(), NODE_ID_FIELD);
            PARSER.declareInt(ConstructingObjectParser.constructorArg(), QUEUE_SIZE_FIELD);
            PARSER.declareInt(ConstructingObjectParser.constructorArg(), REMOTE_REQUESTS_CONCURRENT_FIELD);
            PARSER.declareLong(ConstructingObjectParser.constructorArg(), REMOTE_REQUESTS_TOTAL_FIELD);
            PARSER.declareLong(ConstructingObjectParser.constructorArg(), EXECUTED_SEARCHES_FIELD);
        }

        private final String nodeId;
        private final int queueSize;
        private final int remoteRequestsCurrent;
        private final long remoteRequestsTotal;
        private final long executedSearchesTotal;

        public CoordinatorStats(String nodeId,
                                int queueSize,
                                int remoteRequestsCurrent,
                                long remoteRequestsTotal,
                                long executedSearchesTotal) {
            this.nodeId = nodeId;
            this.queueSize = queueSize;
            this.remoteRequestsCurrent = remoteRequestsCurrent;
            this.remoteRequestsTotal = remoteRequestsTotal;
            this.executedSearchesTotal = executedSearchesTotal;
        }

        public String getNodeId() {
            return nodeId;
        }

        public int getQueueSize() {
            return queueSize;
        }

        public int getRemoteRequestsCurrent() {
            return remoteRequestsCurrent;
        }

        public long getRemoteRequestsTotal() {
            return remoteRequestsTotal;
        }

        public long getExecutedSearchesTotal() {
            return executedSearchesTotal;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CoordinatorStats stats = (CoordinatorStats) o;
            return Objects.equals(nodeId, stats.nodeId) &&
                queueSize == stats.queueSize &&
                remoteRequestsCurrent == stats.remoteRequestsCurrent &&
                remoteRequestsTotal == stats.remoteRequestsTotal &&
                executedSearchesTotal == stats.executedSearchesTotal;
        }

        @Override
        public int hashCode() {
            return Objects.hash(nodeId, queueSize, remoteRequestsCurrent, remoteRequestsTotal, executedSearchesTotal);
        }
    }

    public static class ExecutingPolicy {

        static ParseField NAME_FIELD = new ParseField("name");
        static ParseField TASK_FIELD = new ParseField("task");

        private static final ConstructingObjectParser<ExecutingPolicy, Void> PARSER = new ConstructingObjectParser<>(
            "executing_policy_item",
            true,
            args -> new ExecutingPolicy((String) args[0], (TaskInfo) args[1])
        );

        static {
            PARSER.declareString(ConstructingObjectParser.constructorArg(), NAME_FIELD);
            PARSER.declareObject(ConstructingObjectParser.constructorArg(), (p, c) -> TaskInfo.fromXContent(p), TASK_FIELD);
        }

        private final String name;
        private final TaskInfo taskInfo;

        public ExecutingPolicy(String name, TaskInfo taskInfo) {
            this.name = name;
            this.taskInfo = taskInfo;
        }

        public String getName() {
            return name;
        }

        public TaskInfo getTaskInfo() {
            return taskInfo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ExecutingPolicy that = (ExecutingPolicy) o;
            return name.equals(that.name) &&
                taskInfo.equals(that.taskInfo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, taskInfo);
        }
    }

    public static final class CacheStats {

        static ParseField NODE_ID_FIELD = new ParseField("node_id");
        static ParseField COUNT_FIELD = new ParseField("count");
        static ParseField HITS_FIELD = new ParseField("hits");
        static ParseField MISSES_FIELD = new ParseField("misses");
        static ParseField EVICTIONS_FIELD = new ParseField("evictions");

        private static final ConstructingObjectParser<CacheStats, Void> PARSER = new ConstructingObjectParser<>(
            "coordinator_stats_item",
            true,
            args -> new CacheStats((String) args[0], (long) args[1], (long) args[2], (long) args[3], (long) args[4])
        );

        static {
            PARSER.declareString(ConstructingObjectParser.constructorArg(), NODE_ID_FIELD);
            PARSER.declareLong(ConstructingObjectParser.constructorArg(), COUNT_FIELD);
            PARSER.declareLong(ConstructingObjectParser.constructorArg(), HITS_FIELD);
            PARSER.declareLong(ConstructingObjectParser.constructorArg(), MISSES_FIELD);
            PARSER.declareLong(ConstructingObjectParser.constructorArg(), EVICTIONS_FIELD);
        }

        private final String nodeId;
        private final long count;
        private final long hits;
        private final long misses;
        private final long evictions;

        public CacheStats(String nodeId, long count, long hits, long misses, long evictions) {
            this.nodeId = nodeId;
            this.count = count;
            this.hits = hits;
            this.misses = misses;
            this.evictions = evictions;
        }

        public String getNodeId() {
            return nodeId;
        }

        public long getCount() {
            return count;
        }

        public long getHits() {
            return hits;
        }

        public long getMisses() {
            return misses;
        }

        public long getEvictions() {
            return evictions;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheStats that = (CacheStats) o;
            return count == that.count && hits == that.hits && misses == that.misses &&
                evictions == that.evictions && nodeId.equals(that.nodeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nodeId, count, hits, misses, evictions);
        }
    }
}
