package com.continuent.bamboo;

import java.io.File;
import java.util.Collections;
import java.util.Set;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet;

import com.atlassian.bamboo.build.test.TestCollationService;
import com.atlassian.bamboo.build.test.TestCollectionResult;
import com.atlassian.bamboo.results.tests.TestResults;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.task.TaskType;
import com.atlassian.bamboo.v2.build.CurrentBuildResult;
import com.google.common.collect.Sets;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

@Scanned
public class ParserTask implements TaskType {
    private final TestCollationService testCollationService;

    public ParserTask(@ComponentImport final TestCollationService testCollationService) {
        this.testCollationService = testCollationService;
    }

    public TaskResult execute(TaskContext taskContext) throws TaskException {
        final TaskResultBuilder taskResultBuilder = TaskResultBuilder.create(taskContext);
        
        // build result
        final CurrentBuildResult currentBuildResult = taskContext.getBuildContext().getBuildResult();
        final Set<TestResults> failedTestResults = Collections.synchronizedSet(Sets.<TestResults>newHashSet());
        final Set<TestResults> successfulTestResults = Collections.synchronizedSet(Sets.<TestResults>newHashSet());
        final Set<TestResults> skippedTestResults = Collections.synchronizedSet(Sets.<TestResults>newHashSet());

        // file pattern from config
        final String testFilePattern = taskContext.getConfigurationMap().get("testFilePattern");

        // files of working directory
        FileSet fileSet = new FileSet();
        fileSet.setDir(taskContext.getRootDirectory());
        PatternSet.NameEntry include = fileSet.createInclude();
        include.setName(testFilePattern);
        DirectoryScanner ds = fileSet.getDirectoryScanner(new Project());
        String[] srcFiles = ds.getIncludedFiles();

        try {
            // collect results from files
            ParserCollector parserCollector = new ParserCollector();
            
            for (String src : srcFiles) {
                File file = new File(taskContext.getRootDirectory() + "/" + src);
                TestCollectionResult result = parserCollector.collect(file);

                failedTestResults.addAll(result.getFailedTestResults());
                successfulTestResults.addAll(result.getSuccessfulTestResults());
            }
        

            // update build result
            currentBuildResult.appendTestResults(successfulTestResults, failedTestResults, skippedTestResults);

            return taskResultBuilder.checkTestFailures().build();
        
        } catch (Exception e) {
            throw new TaskException("Failed to execute task", e);
        }
    }
}