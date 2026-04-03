QueryConfigManagementExOrientation = {
	load: function() {
        var form = $('QCM001_QueryConfig');
        if (form) {        
        QueryConfigManagementExOrientation.callFunction(form, 'orientation');
            QueryConfigManagementExOrientation.showDropList(form);                        
        }
    },
    
    showDropList : function(form) {

        var formName = form.readAttribute('name');
        
        var exportMimeTypeList = DropListMgr.getDropList(formName + '_exportMimeType');
        if (exportMimeTypeList) {
            exportMimeTypeList.registerOnChangeListener(QueryConfigManagementExOrientation.callFunction.curry(form, 'orientation'), 'exportMimeType');
        }
        
        
       
	  
    },
    
    callFunction : function(form, name, element) {
    
    if (form) {
        	var formName = form.readAttribute('name');
        	var orientationId = form.down("div#" + formName + "_" + name);
        	var exportMimeTypeId = form.down("div#" + formName + "_exportMimeType");
            var keyExportMimeTypeId = exportMimeTypeId.down("input.droplist_code_field").getValue();
            var td = orientationId.up('td');
            
            if (keyExportMimeTypeId == "text/html") {
            	td.removeClassName("displayNone");
            	var trOr = orientationId.up('tr');
            	var tdOr = trOr.down('td',2);
            	tdOr.removeClassName("displayNone");
            }
            else {
            	td.addClassName("displayNone");
            	var trOr = orientationId.up('tr');
            	var tdOr = trOr.down('td', 2);
            	tdOr.addClassName("displayNone");
            }
            
         
        }   

	  
    },
}
QueryConfigManagementExOrientation.load();