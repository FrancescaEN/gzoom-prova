import org.ofbiz.base.util.*;


context.contentIdInd = 'WEFLD_IND10';
parameters.contentIdInd = 'WEFLD_IND10';
context.contentIdSecondary = 'WEFLD_AIND10';
parameters.contentIdSecondary = 'WEFLD_AIND10';

Debug.log("executeChildPerformFindWETIndicator10.groovy");

GroovyUtil.runScriptAtLocation("component:/workeffortext/webapp/workeffortext/WEB-INF/actions/executeChildPerformFindWETIndicator.groovy", context);
