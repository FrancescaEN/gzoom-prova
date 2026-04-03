import org.ofbiz.base.util.*;


context.contentIdInd = 'WEFLD_IND7'
parameters.contentIdInd = 'WEFLD_IND7';
context.contentIdSecondary = 'WEFLD_AIND7';
parameters.contentIdSecondary = 'WEFLD_AIND7';

GroovyUtil.runScriptAtLocation("component:/workeffortext/webapp/workeffortext/WEB-INF/actions/executeChildPerformFindWEMIndicator.groovy", context);
