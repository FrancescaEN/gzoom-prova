import org.ofbiz.base.util.*;

context.inputFields.workEffortId = parameters.workEffortId;

parameters.allegatoContentId = UtilValidate.isEmpty(parameters.allegatoContentId) ? "WEFLD_CONT" : parameters.allegatoContentId;
context.inputFields.allegatoContentId = UtilValidate.isEmpty(context.inputFields.allegatoContentId) ? "WEFLD_CONT" : context.inputFields.allegatoContentId;

// Debug.log("executeChildPerformFindWorkEffortContentView Passa da qui parameters.allegatoContentId " + parameters.allegatoContentId);

GroovyUtil.runScriptAtLocation("component://base/webapp/common/WEB-INF/actions/executeChildPerformFind.groovy", context);
context.insertMode = context.insertMode = UtilValidate.isEmpty(context.insertMode) ? UtilValidate.isEmpty(parameters.insertMode) ? "N" : parameters.insertMode : context.insertMode;