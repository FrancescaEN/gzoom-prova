package it.mapsgroup.gzoom.persistence.common;

import org.slf4j.Logger;

import javax.sql.DataSource;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Andrea Fossi.
 *
 * Class is a port of OfBiz {@link SequenceGenerator}
 */
public class SequenceGenerator {
    private static final Logger LOG = getLogger(SequenceGenerator.class);

    private final DataSource dataSource;

    private SequenceUtil sequencer;

    public SequenceGenerator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

//    public String getNextSeqId(Class entity) {
//        return this.getNextSeqId(SequenceEntity.sequenceEntity(entity), 1);
//    }

    public String getNextSeqId(String seqName) {
        return this.getNextSeqId(seqName, 1);
    }

    /* (non-Javadoc)
     * @see org.ofbiz.entity.Delegator#getNextSeqId(java.lang.String, long)
     */
    public String getNextSeqId(String seqName, long staggerMax) {
        Long nextSeqLong = this.getNextSeqIdLong(seqName, staggerMax);

        if (nextSeqLong == null) {
            // NOTE: the getNextSeqIdLong method SHOULD throw a runtime exception when no sequence value is found, which means we should never see it get here
            throw new IllegalArgumentException("Could not get next sequenced ID for sequence name: " + seqName);
        }

        //TODO see ofbiz behavior
       /* if (UtilValidate.isNotEmpty(this.getDelegatorInfo().sequencedIdPrefix)) {
            return this.getDelegatorInfo().sequencedIdPrefix + nextSeqLong.toString();
        } else {
            return nextSeqLong.toString();
        }*/
        return nextSeqLong.toString();
    }

    /* (non-Javadoc)
     * @see org.ofbiz.entity.Delegator#getNextSeqIdLong(java.lang.String)
     */
    public Long getNextSeqIdLong(String seqName) {
        return this.getNextSeqIdLong(seqName, 1);
    }

    /* (non-Javadoc)
     * @see org.ofbiz.entity.Delegator#getNextSeqIdLong(java.lang.String, long)
     */
    public Long getNextSeqIdLong(String seqName, long staggerMax) {
        try {
            if (sequencer == null) {
                synchronized (this) {
                    if (sequencer == null) {
                        sequencer = new SequenceUtil("SEQUENCE_VALUE_ITEM", "SEQ_NAME", "SEQ_ID", null, dataSource);
                    }
                }
            }

            // might be null, but will usually match the entity name

            Long newSeqId = sequencer == null ? null : sequencer.getNextSeqId(seqName, staggerMax);

            return newSeqId;
        } catch (Exception e) {
            String errMsg = "Failure in getNextSeqIdLong operation for seqName [" + seqName + "]: " + e.toString() + ". Rolling back transaction.";
            LOG.error("[GenericDelegator] Could not rollback transaction: " + e.toString(), e);
            throw new PersistenceException(errMsg, e);
        }
    }

}
