import org.ofbiz.base.util.*;

parameters.allegatoContentId = "WEFLD_CONT2";
context.inputFields.allegatoContentId= "WEFLD_CONT2";

// Debug.log("executeChildPerformFindWorkEffortContentView2 Passa da qui parameters.allegatoContentId " + parameters.allegatoContentId);
GroovyUtil.runScriptAtLocation("component:/workeffortext/webapp/workeffortext/WEB-INF/actions/executeChildPerformFindWorkEffortContentView.groovy", context);