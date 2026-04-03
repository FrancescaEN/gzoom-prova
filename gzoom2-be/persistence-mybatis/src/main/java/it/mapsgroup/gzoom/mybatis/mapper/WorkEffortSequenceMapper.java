package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortSequence;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WorkEffortSequenceMapper {

    @Delete({
        "delete from work_effort_sequence",
        "where seq_name = #{seqName,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String seqName);

    @Insert({
        "insert into work_effort_sequence (seq_name, seq_id, ",
        "last_modified_by_user_login, created_by_user_login, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp)",
        "values (#{seqName,jdbcType=VARCHAR}, #{seqId,jdbcType=NUMERIC}, ",
        "#{lastModifiedByUserLogin,jdbcType=VARCHAR}, #{createdByUserLogin,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(WorkEffortSequence row);

    @Select({
        "select",
        "seq_name, seq_id, last_modified_by_user_login, created_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from work_effort_sequence",
        "where seq_name = #{seqName,jdbcType=VARCHAR}"
    })
    @Results(id = "workEffortSequence", value = {
        @Result(column="seq_name", property="seqName", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="seq_id", property="seqId", jdbcType=JdbcType.NUMERIC),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    WorkEffortSequence selectByPrimaryKey(String seqName);

    @Select({
        "select",
        "seq_name, seq_id, last_modified_by_user_login, created_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from work_effort_sequence",
            "order by seq_name"
    })
    @ResultMap("workEffortSequence")
    List<WorkEffortSequence> selectAllOrderByPrimaryKey();

    @Update({
        "update work_effort_sequence",
        "set seq_id = #{seqId,jdbcType=NUMERIC},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where seq_name = #{seqName,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(WorkEffortSequence row);
}