/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */
package org.elasticsearch.client.ml.dataframe.evaluation.classification;

import org.elasticsearch.client.ml.dataframe.evaluation.MlEvaluationNamedXContentProvider;
import org.elasticsearch.client.ml.dataframe.evaluation.classification.PrecisionMetric.Result;
import org.elasticsearch.xcontent.NamedXContentRegistry;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.test.AbstractXContentTestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrecisionMetricResultTests extends AbstractXContentTestCase<Result> {

    @Override
    protected NamedXContentRegistry xContentRegistry() {
        return new NamedXContentRegistry(new MlEvaluationNamedXContentProvider().getNamedXContentParsers());
    }

    public static Result randomResult() {
        int numClasses = randomIntBetween(2, 100);
        List<String> classNames = Stream.generate(() -> randomAlphaOfLength(10)).limit(numClasses).collect(Collectors.toList());
        List<PerClassSingleValue> classes = new ArrayList<>(numClasses);
        for (int i = 0; i < numClasses; i++) {
            double precision = randomDoubleBetween(0.0, 1.0, true);
            classes.add(new PerClassSingleValue(classNames.get(i), precision));
        }
        double avgPrecision = randomDoubleBetween(0.0, 1.0, true);
        return new Result(classes, avgPrecision);
    }

    @Override
    protected Result createTestInstance() {
        return randomResult();
    }

    @Override
    protected Result doParseInstance(XContentParser parser) throws IOException {
        return Result.fromXContent(parser);
    }

    @Override
    protected boolean supportsUnknownFields() {
        return true;
    }
}
