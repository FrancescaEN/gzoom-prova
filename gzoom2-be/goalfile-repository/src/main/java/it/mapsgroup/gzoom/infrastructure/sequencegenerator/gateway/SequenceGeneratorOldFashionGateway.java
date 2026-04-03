package it.mapsgroup.gzoom.infrastructure.sequencegenerator.gateway;

import it.mapsgroup.gzoom.entity.sequencegenerator.gateway.SequenceGeneratorGateway;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;

public class SequenceGeneratorOldFashionGateway implements SequenceGeneratorGateway {

    private final SequenceGenerator sequenceGenerator;

    public SequenceGeneratorOldFashionGateway(SequenceGenerator sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public String getNextSeqId(String seqName, long staggerMax) {
        return sequenceGenerator.getNextSeqId(seqName, staggerMax);
    }
}
