package it.mapsgroup.gzoom.birt;

import it.mapsgroup.gzoom.birt.Report;

/**
 * @author Andrea Fossi.
 */
public interface ReportRunner {

    ReportHandler runReport(Report report);
}