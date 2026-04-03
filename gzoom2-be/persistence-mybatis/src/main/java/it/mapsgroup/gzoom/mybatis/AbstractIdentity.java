package it.mapsgroup.gzoom.mybatis;

import java.time.Instant;

public interface AbstractIdentity {

    Instant getCreatedStamp();

    void setCreatedStamp(java.time.Instant createdStamp);

    Instant getCreatedTxStamp();

    void setCreatedTxStamp(java.time.Instant createdTxStamp);

    Instant getLastUpdatedStamp();

    void setLastUpdatedStamp(java.time.Instant lastUpdatedStamp);

    Instant getLastUpdatedTxStamp();

    void setLastUpdatedTxStamp(java.time.Instant lastUpdatedTxStamp);

}
