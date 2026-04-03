package it.mapsgroup.gzoom.entity.sequencegenerator.gateway;

public interface SequenceGeneratorGateway {
    String getNextSeqId(String seqName, long staggerMax);
}
