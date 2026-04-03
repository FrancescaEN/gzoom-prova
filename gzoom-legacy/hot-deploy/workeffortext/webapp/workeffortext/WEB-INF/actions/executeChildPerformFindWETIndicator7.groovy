import org.ofbiz.base.util.*;


context.contentIdInd = 'WEFLD_IND7';
parameters.contentIdInd = 'WEFLD_IND7';
context.contentIdSecondary = 'WEFLD_AIND7';
parameters.contentIdSecondary = 'WEFLD_AIND7';

Debug.log("executeChildPerformFindWETIndicator7.groovy");

GroovyUtil.runScriptAtLocation("component:/workeffortext/webapp/workeffortext/WEB-INF/actions/executeChildPerformFindWETIndicator.groovy", context);
