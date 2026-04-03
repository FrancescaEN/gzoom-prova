package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.CommEventContentAssoc;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CommEventContentAssocMapper {

    @Delete({
        "delete from comm_event_content_assoc",
        "where content_id = #{contentId,jdbcType=VARCHAR}",
          "and communication_event_id = #{communicationEventId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    int deleteByPrimaryKey(@Param("contentId") String contentId, @Param("communicationEventId") String communicationEventId, @Param("fromDate") LocalDateTime fromDate);


    @Insert({
        "insert into comm_event_content_assoc (content_id, communication_event_id, ",
        "from_date, comm_content_assoc_type_id, ",
        "thru_date, sequence_num, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp)",
        "values (#{contentId,jdbcType=VARCHAR}, #{communicationEventId,jdbcType=VARCHAR}, ",
        "#{fromDate,jdbcType=TIMESTAMP}, #{commContentAssocTypeId,jdbcType=VARCHAR}, ",
        "#{thruDate,jdbcType=TIMESTAMP}, #{sequenceNum,jdbcType=NUMERIC}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(CommEventContentAssoc row);


    @Select({
        "select",
        "content_id, communication_event_id, from_date, comm_content_assoc_type_id, thru_date, ",
        "sequence_num, last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from comm_event_content_assoc",
        "where content_id = #{contentId,jdbcType=VARCHAR}",
          "and communication_event_id = #{communicationEventId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    @Results({
        @Result(column="content_id", property="contentId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="communication_event_id", property="communicationEventId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="from_date", property="fromDate", jdbcType=JdbcType.TIMESTAMP, id=true),
        @Result(column="comm_content_assoc_type_id", property="commContentAssocTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="thru_date", property="thruDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="sequence_num", property="sequenceNum", jdbcType=JdbcType.NUMERIC),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    CommEventContentAssoc selectByPrimaryKey(@Param("contentId") String contentId, @Param("communicationEventId") String communicationEventId, @Param("fromDate") LocalDateTime fromDate);


    @Select({
        "select",
        "content_id, communication_event_id, from_date, comm_content_assoc_type_id, thru_date, ",
        "sequence_num, last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from comm_event_content_assoc"
    })
    @Results({
        @Result(column="content_id", property="contentId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="communication_event_id", property="communicationEventId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="from_date", property="fromDate", jdbcType=JdbcType.TIMESTAMP, id=true),
        @Result(column="comm_content_assoc_type_id", property="commContentAssocTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="thru_date", property="thruDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="sequence_num", property="sequenceNum", jdbcType=JdbcType.NUMERIC),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    List<CommEventContentAssoc> selectAll();


    @Update({
        "update comm_event_content_assoc",
        "set comm_content_assoc_type_id = #{commContentAssocTypeId,jdbcType=VARCHAR},",
          "thru_date = #{thruDate,jdbcType=TIMESTAMP},",
          "sequence_num = #{sequenceNum,jdbcType=NUMERIC},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where content_id = #{contentId,jdbcType=VARCHAR}",
          "and communication_event_id = #{communicationEventId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    int updateByPrimaryKey(CommEventContentAssoc row);
}