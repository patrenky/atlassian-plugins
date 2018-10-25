package com.continuent.bamboo;

import com.atlassian.bamboo.build.test.TestCollectionResult;
import com.atlassian.bamboo.build.test.TestCollectionResultBuilder;
import com.atlassian.bamboo.build.test.TestReportCollector;
import com.atlassian.bamboo.results.tests.TestResults;
import com.atlassian.bamboo.resultsummary.tests.TestState;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

public class ParserCollector implements TestReportCollector {
    public ParserCollector() {}

    public TestCollectionResult collect(File file) throws Exception {
        TestCollectionResultBuilder builder = new TestCollectionResultBuilder();

        Collection<TestResults> successfulTestResults = Lists.newArrayList();
        Collection<TestResults> failingTestResults = Lists.newArrayList();

        List<String> lines = Files.readLines(file, Charset.forName("UTF-8"));

        for (String line : lines) {
            String[] atoms = StringUtils.split(line, '|');
            
            if (atoms.length < 3) {
                continue;
            }
            
            String suiteName = atoms[0];
            String testName = atoms[1];
            String duration = atoms[2];
            String status = atoms[3];

            TestResults testResult = new TestResults(suiteName, testName, duration);
            
            if (status.equals("SUCCESS")) {
                testResult.setState(TestState.SUCCESS);
                successfulTestResults.add(testResult);
            }
            else {
                testResult.setState(TestState.FAILED);
                failingTestResults.add(testResult);
            }
        }

        return builder
                .addSuccessfulTestResults(successfulTestResults)
                .addFailedTestResults(failingTestResults)
                .build();
    }

    public Set<String> getSupportedFileExtensions() {
        return Sets.newHashSet("log");
    }
}
