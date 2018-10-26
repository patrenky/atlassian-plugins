package com.continuent.bamboo;

import com.atlassian.bamboo.resultsummary.ResultsSummary;
import com.atlassian.core.util.map.EasyMap;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.web.ContextProvider;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SummaryPanel implements ContextProvider{

    public SummaryPanel() {}

    @Override
    public void init(Map params) throws PluginParseException {}

    @Override
    public Map getContextMap(Map context) {
        String summaryLink = "/artifact";
        boolean failed = false;

        try {
            ResultsSummary resultsummary = (ResultsSummary) context.get("resultSummary");
            String fullPlanKey = resultsummary.getPlanResultKey().getKey();

            String regex = "^(\\w+-\\w+)-(\\d+)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(fullPlanKey);

            failed = !matcher.find();
            String planKey = matcher.group(1);
            String buildNum = matcher.group(2);
            String jobKey = "RES";

            if (planKey.length() < 1 || buildNum.length() < 1) {
                failed = true;
            }
        
            // /artifact/$planKey/$jobKey/build-$buildNum/Logs/0summary.html
            summaryLink += "/" + planKey;
            summaryLink += "/" + jobKey;
            summaryLink += "/build-" + buildNum;
            summaryLink += "/Logs/0summary.html";
                
        } catch (Exception e) {
            failed = true;
        }

        return EasyMap.build("summaryLink", failed ? "NULL" : summaryLink);
    }
}
