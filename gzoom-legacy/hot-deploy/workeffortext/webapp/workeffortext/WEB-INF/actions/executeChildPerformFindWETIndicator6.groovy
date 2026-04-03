import org.ofbiz.base.util.*;


context.contentIdInd = 'WEFLD_IND6';
parameters.contentIdInd = 'WEFLD_IND6';
context.contentIdSecondary = 'WEFLD_AIND6';
parameters.contentIdSecondary = 'WEFLD_AIND6';

Debug.log("executeChildPerformFindWETIndicator6.groovy");

GroovyUtil.runScriptAtLocation("component:/workeffortext/webapp/workeffortext/WEB-INF/actions/executeChildPerformFindWETIndicator.groovy", context);
