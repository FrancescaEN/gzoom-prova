package it.mapsgroup.gzoom.sequencegenerator.usecase;

import it.mapsgroup.gzoom.entity.sequencegenerator.gateway.SequenceGeneratorGateway;

public class SequenceGeneratorUseCase {
    private final SequenceGeneratorGateway sequenceGeneratorGateway;

    public SequenceGeneratorUseCase(SequenceGeneratorGateway sequenceGeneratorGateway) {
        this.sequenceGeneratorGateway = sequenceGeneratorGateway;
    }

    public String getNextSeqId(String seqName, long staggerMax) {
        return sequenceGeneratorGateway.getNextSeqId(seqName, staggerMax);
    }

    public String getNextSeqId(String seqName) {
        return this.getNextSeqId(seqName, 1);
    }
}
