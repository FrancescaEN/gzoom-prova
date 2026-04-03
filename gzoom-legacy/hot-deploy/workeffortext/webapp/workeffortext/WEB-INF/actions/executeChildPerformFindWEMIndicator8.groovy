import org.ofbiz.base.util.*;


context.contentIdInd = 'WEFLD_IND8'
parameters.contentIdInd = 'WEFLD_IND8';
context.contentIdSecondary = 'WEFLD_AIND8';
parameters.contentIdSecondary = 'WEFLD_AIND8';

GroovyUtil.runScriptAtLocation("component:/workeffortext/webapp/workeffortext/WEB-INF/actions/executeChildPerformFindWEMIndicator.groovy", context);
