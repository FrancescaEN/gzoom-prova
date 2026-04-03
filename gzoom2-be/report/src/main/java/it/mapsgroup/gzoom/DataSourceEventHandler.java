package it.mapsgroup.gzoom;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.eventadapter.DataSourceEventAdapter;
import org.eclipse.birt.report.engine.api.script.instance.IDataSourceInstance;

public class DataSourceEventHandler extends DataSourceEventAdapter {

    public void beforeOpen(IDataSourceInstance dataSource, IReportContext reportContext) throws ScriptException {
        super.beforeOpen(dataSource, reportContext);
        String odaURL = (String) reportContext.getAppContext().get("odaURL");
        String odaDriverClass = (String) reportContext.getAppContext().get("odaDriverClass");
        String odaPassword = (String) reportContext.getAppContext().get("odaPassword");
        String odaUser = (String) reportContext.getAppContext().get("odaUser");
        String odaIsolationMode = (String) reportContext.getAppContext().get("odaIsolationMode");

        if (odaURL != null && odaDriverClass != null && odaPassword != null && odaUser != null) {
            dataSource.setExtensionProperty("odaURL", odaURL);
            dataSource.setExtensionProperty("odaDriverClass", odaDriverClass);
            dataSource.setExtensionProperty("odaPassword", odaPassword);
            dataSource.setExtensionProperty("odaUser", odaUser);
            dataSource.setExtensionProperty("odaJndiName",System.currentTimeMillis()+"_DS");
            if (StringUtils.isNotEmpty(odaIsolationMode)) {
                dataSource.setExtensionProperty("odaIsolationMode", odaIsolationMode);
            }
        }
    }
}
