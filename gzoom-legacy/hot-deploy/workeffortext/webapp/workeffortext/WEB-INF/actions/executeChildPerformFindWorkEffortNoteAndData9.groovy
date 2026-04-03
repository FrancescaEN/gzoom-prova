import org.ofbiz.base.util.*;

parameters.noteContentId = "WEFLD_NOTE9";
context.inputFields.noteContentId= "WEFLD_NOTE9";

GroovyUtil.runScriptAtLocation("component:/workeffortext/webapp/workeffortext/WEB-INF/actions/executeChildPerformFindWorkEffortNoteAndData.groovy", context);