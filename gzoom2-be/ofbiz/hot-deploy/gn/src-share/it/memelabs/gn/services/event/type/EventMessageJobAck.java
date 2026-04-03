package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;
import it.memelabs.gn.services.job.JobResultOfbiz;

/**
 * 15/01/14
 *
 * @author Andrea Fossi
 */
public class EventMessageJobAck extends EventMessage {

    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.JOB_ACK;
    }

    public void setFailMessage(String failMessage) {
        this.put("failMessage", failMessage);
    }

    public String getFailMessage() {
        return (String) this.get("failMessage");
    }

    public void setJobId(String jobId) {
        this.put("jobId", jobId);
    }

    public String getJobId() {
        return (String) this.get("jobId");
    }

    public void setJobResultId(JobResultOfbiz jobResultId) {
        this.put("jobResultId", jobResultId.name());
    }

    public void setJobResultId(String jobResultId) {
        this.put("jobResultId", jobResultId);
    }

    public String getJobResultId() {
        return (String) this.get("jobResultId");
    }
}
