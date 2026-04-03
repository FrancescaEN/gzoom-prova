import org.ofbiz.base.util.*;


context.contentIdInd = 'WEFLD_IND9'
parameters.contentIdInd = 'WEFLD_IND9';
context.contentIdSecondary = 'WEFLD_AIND9';
parameters.contentIdSecondary = 'WEFLD_AIND9';

GroovyUtil.runScriptAtLocation("component:/workeffortext/webapp/workeffortext/WEB-INF/actions/executeChildPerformFindWEMIndicator.groovy", context);
