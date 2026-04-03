import org.ofbiz.base.util.*;


context.contentIdInd = 'WEFLD_IND9';
parameters.contentIdInd = 'WEFLD_IND9';
context.contentIdSecondary = 'WEFLD_AIND9';
parameters.contentIdSecondary = 'WEFLD_AIND9';

Debug.log("executeChildPerformFindWETIndicator9.groovy");

GroovyUtil.runScriptAtLocation("component:/workeffortext/webapp/workeffortext/WEB-INF/actions/executeChildPerformFindWETIndicator.groovy", context);
