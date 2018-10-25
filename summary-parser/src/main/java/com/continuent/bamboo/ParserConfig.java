package com.continuent.bamboo;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.task.TaskTestResultsSupport;
import com.atlassian.bamboo.utils.error.ErrorCollection;

import java.util.Map;

public class ParserConfig extends AbstractTaskConfigurator  implements TaskTestResultsSupport {
    @Override
    public boolean taskProducesTestResults(TaskDefinition taskDefinition) {
        return true;
    }

    @Override
    public Map<String, String> generateTaskConfigMap(ActionParametersMap params, TaskDefinition previousTaskDefinition) {
        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);
        config.put("testFilePattern", params.getString("testFilePattern"));
        return config;
    }

    @Override
    public void populateContextForCreate(Map<String, Object> context) {
        super.populateContextForCreate(context);
        context.put("testFilePattern", "**/summary-parse.*");
    }

    @Override
    public void populateContextForEdit(Map<String, Object> context, TaskDefinition taskDefinition) {
        super.populateContextForEdit(context, taskDefinition);
        context.put("testFilePattern", taskDefinition.getConfiguration().get("testFilePattern"));
    }

    @Override
    public void populateContextForView(Map<String, Object> context, TaskDefinition taskDefinition) {
        super.populateContextForView(context, taskDefinition);
        context.put("testFilePattern", taskDefinition.getConfiguration().get("testFilePattern"));
    }

    @Override
    public void validate(ActionParametersMap params, ErrorCollection errorCollection) {
        super.validate(params, errorCollection);        
    }
}