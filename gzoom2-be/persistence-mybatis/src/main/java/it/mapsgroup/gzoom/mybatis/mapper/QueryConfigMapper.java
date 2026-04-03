package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.QueryConfig;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface QueryConfigMapper {

    @Delete({
        "delete from query_config",
        "where query_id = #{queryId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String queryId);


    @Insert({
        "insert into query_config (query_id, query_code, ",
        "query_name, query_comm, ",
        "query_type, query_ctx, ",
        "query_public, query_active, ",
        "query_info, cond0_name, ",
        "cond0_comm, cond0_info, ",
        "cond1_name, cond1_comm, ",
        "cond1_info, cond2_name, ",
        "cond2_comm, cond2_info, ",
        "cond3_name, cond3_comm, ",
        "cond3_info, cond4_name, ",
        "cond4_comm, cond4_info, ",
        "cond5_name, cond5_comm, ",
        "cond5_info, cond6_name, ",
        "cond6_comm, cond6_info, ",
        "cond7_name, cond7_comm, ",
        "cond7_info, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, export_mime_type, ",
        "query_columns_format_param, orientation)",
        "values (#{queryId,jdbcType=VARCHAR}, #{queryCode,jdbcType=VARCHAR}, ",
        "#{queryName,jdbcType=VARCHAR}, #{queryComm,jdbcType=VARCHAR}, ",
        "#{queryType,jdbcType=CHAR}, #{queryCtx,jdbcType=VARCHAR}, ",
        "#{queryPublic,jdbcType=CHAR}, #{queryActive,jdbcType=CHAR}, ",
        "#{queryInfo,jdbcType=VARCHAR}, #{cond0Name,jdbcType=VARCHAR}, ",
        "#{cond0Comm,jdbcType=VARCHAR}, #{cond0Info,jdbcType=VARCHAR}, ",
        "#{cond1Name,jdbcType=VARCHAR}, #{cond1Comm,jdbcType=VARCHAR}, ",
        "#{cond1Info,jdbcType=VARCHAR}, #{cond2Name,jdbcType=VARCHAR}, ",
        "#{cond2Comm,jdbcType=VARCHAR}, #{cond2Info,jdbcType=VARCHAR}, ",
        "#{cond3Name,jdbcType=VARCHAR}, #{cond3Comm,jdbcType=VARCHAR}, ",
        "#{cond3Info,jdbcType=VARCHAR}, #{cond4Name,jdbcType=VARCHAR}, ",
        "#{cond4Comm,jdbcType=VARCHAR}, #{cond4Info,jdbcType=VARCHAR}, ",
        "#{cond5Name,jdbcType=VARCHAR}, #{cond5Comm,jdbcType=VARCHAR}, ",
        "#{cond5Info,jdbcType=VARCHAR}, #{cond6Name,jdbcType=VARCHAR}, ",
        "#{cond6Comm,jdbcType=VARCHAR}, #{cond6Info,jdbcType=VARCHAR}, ",
        "#{cond7Name,jdbcType=VARCHAR}, #{cond7Comm,jdbcType=VARCHAR}, ",
        "#{cond7Info,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP}, #{exportMimeType,jdbcType=VARCHAR}, ",
        "#{queryColumnsFormatParam,jdbcType=VARCHAR}, #{orientation,jdbcType=VARCHAR})"
    })
    int insert(QueryConfig row);


    @Select({
        "select",
        "query_id, query_code, query_name, query_comm, query_type, query_ctx, query_public, ",
        "query_active, query_info, cond0_name, cond0_comm, cond0_info, cond1_name, cond1_comm, ",
        "cond1_info, cond2_name, cond2_comm, cond2_info, cond3_name, cond3_comm, cond3_info, ",
        "cond4_name, cond4_comm, cond4_info, cond5_name, cond5_comm, cond5_info, cond6_name, ",
        "cond6_comm, cond6_info, cond7_name, cond7_comm, cond7_info, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, export_mime_type, query_columns_format_param, ",
        "orientation",
        "from query_config",
        "where query_id = #{queryId,jdbcType=VARCHAR}"
    })
    @Results(id = "queryConfig", value = {
        @Result(column="query_id", property="queryId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="query_code", property="queryCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="query_name", property="queryName", jdbcType=JdbcType.VARCHAR),
        @Result(column="query_comm", property="queryComm", jdbcType=JdbcType.VARCHAR),
        @Result(column="query_type", property="queryType", jdbcType=JdbcType.CHAR),
        @Result(column="query_ctx", property="queryCtx", jdbcType=JdbcType.VARCHAR),
        @Result(column="query_public", property="queryPublic", jdbcType=JdbcType.CHAR),
        @Result(column="query_active", property="queryActive", jdbcType=JdbcType.CHAR),
        @Result(column="query_info", property="queryInfo", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond0_name", property="cond0Name", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond0_comm", property="cond0Comm", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond0_info", property="cond0Info", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond1_name", property="cond1Name", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond1_comm", property="cond1Comm", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond1_info", property="cond1Info", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond2_name", property="cond2Name", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond2_comm", property="cond2Comm", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond2_info", property="cond2Info", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond3_name", property="cond3Name", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond3_comm", property="cond3Comm", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond3_info", property="cond3Info", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond4_name", property="cond4Name", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond4_comm", property="cond4Comm", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond4_info", property="cond4Info", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond5_name", property="cond5Name", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond5_comm", property="cond5Comm", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond5_info", property="cond5Info", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond6_name", property="cond6Name", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond6_comm", property="cond6Comm", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond6_info", property="cond6Info", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond7_name", property="cond7Name", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond7_comm", property="cond7Comm", jdbcType=JdbcType.VARCHAR),
        @Result(column="cond7_info", property="cond7Info", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="export_mime_type", property="exportMimeType", jdbcType=JdbcType.VARCHAR),
        @Result(column="query_columns_format_param", property="queryColumnsFormatParam", jdbcType=JdbcType.VARCHAR),
        @Result(column="orientation", property="orientation", jdbcType=JdbcType.VARCHAR)
    })
    QueryConfig selectByPrimaryKey(String queryId);

    List<QueryConfig> selectByActiveTrueOrderByQueryCode(@Param("parentTypeId") String parentTypeId, @Param("queryType") String queryType, @Param("filter") Map<String, Object> filter);

    @Select({
            "select",
            "query_id, query_code, query_name, query_comm, query_type, query_ctx, query_public, ",
            "query_active, query_info, cond0_name, cond0_comm, cond0_info, cond1_name, cond1_comm, ",
            "cond1_info, cond2_name, cond2_comm, cond2_info, cond3_name, cond3_comm, cond3_info, ",
            "cond4_name, cond4_comm, cond4_info, cond5_name, cond5_comm, cond5_info, cond6_name, ",
            "cond6_comm, cond6_info, cond7_name, cond7_comm, cond7_info, last_updated_stamp, ",
            "last_updated_tx_stamp, created_stamp, created_tx_stamp, export_mime_type, query_columns_format_param, ",
            "orientation",
            "from query_config",
            "order by query_code"
    })
    @ResultMap("queryConfig")
    List<QueryConfig> selectAllOrderByQueryCode();

    @Select({
            "select",
            "query_id, query_code, query_name, query_comm, query_type, query_ctx, query_public, ",
            "query_active, query_info, cond0_name, cond0_comm, cond0_info, cond1_name, cond1_comm, ",
            "cond1_info, cond2_name, cond2_comm, cond2_info, cond3_name, cond3_comm, cond3_info, ",
            "cond4_name, cond4_comm, cond4_info, cond5_name, cond5_comm, cond5_info, cond6_name, ",
            "cond6_comm, cond6_info, cond7_name, cond7_comm, cond7_info, last_updated_stamp, ",
            "last_updated_tx_stamp, created_stamp, created_tx_stamp, export_mime_type, query_columns_format_param, ",
            "orientation",
            "from query_config",
            "where query_type = #{queryType,jdbcType=CHAR}",
            "order by query_code"
    })
    @ResultMap("queryConfig")
    List<QueryConfig> selectByQueryTypeOrderByQueryCode(@Param("queryType") String queryType);

    @Update({
        "update query_config",
        "set query_code = #{queryCode,jdbcType=VARCHAR},",
          "query_name = #{queryName,jdbcType=VARCHAR},",
          "query_comm = #{queryComm,jdbcType=VARCHAR},",
          "query_type = #{queryType,jdbcType=CHAR},",
          "query_ctx = #{queryCtx,jdbcType=VARCHAR},",
          "query_public = #{queryPublic,jdbcType=CHAR},",
          "query_active = #{queryActive,jdbcType=CHAR},",
          "query_info = #{queryInfo,jdbcType=VARCHAR},",
          "cond0_name = #{cond0Name,jdbcType=VARCHAR},",
          "cond0_comm = #{cond0Comm,jdbcType=VARCHAR},",
          "cond0_info = #{cond0Info,jdbcType=VARCHAR},",
          "cond1_name = #{cond1Name,jdbcType=VARCHAR},",
          "cond1_comm = #{cond1Comm,jdbcType=VARCHAR},",
          "cond1_info = #{cond1Info,jdbcType=VARCHAR},",
          "cond2_name = #{cond2Name,jdbcType=VARCHAR},",
          "cond2_comm = #{cond2Comm,jdbcType=VARCHAR},",
          "cond2_info = #{cond2Info,jdbcType=VARCHAR},",
          "cond3_name = #{cond3Name,jdbcType=VARCHAR},",
          "cond3_comm = #{cond3Comm,jdbcType=VARCHAR},",
          "cond3_info = #{cond3Info,jdbcType=VARCHAR},",
          "cond4_name = #{cond4Name,jdbcType=VARCHAR},",
          "cond4_comm = #{cond4Comm,jdbcType=VARCHAR},",
          "cond4_info = #{cond4Info,jdbcType=VARCHAR},",
          "cond5_name = #{cond5Name,jdbcType=VARCHAR},",
          "cond5_comm = #{cond5Comm,jdbcType=VARCHAR},",
          "cond5_info = #{cond5Info,jdbcType=VARCHAR},",
          "cond6_name = #{cond6Name,jdbcType=VARCHAR},",
          "cond6_comm = #{cond6Comm,jdbcType=VARCHAR},",
          "cond6_info = #{cond6Info,jdbcType=VARCHAR},",
          "cond7_name = #{cond7Name,jdbcType=VARCHAR},",
          "cond7_comm = #{cond7Comm,jdbcType=VARCHAR},",
          "cond7_info = #{cond7Info,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "export_mime_type = #{exportMimeType,jdbcType=VARCHAR},",
          "query_columns_format_param = #{queryColumnsFormatParam,jdbcType=VARCHAR},",
          "orientation = #{orientation,jdbcType=VARCHAR}",
        "where query_id = #{queryId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(QueryConfig row);

    @Update({
            "update query_config",
            "set query_code = #{queryCode,jdbcType=VARCHAR},",
            "query_name = #{queryName,jdbcType=VARCHAR},",
            "query_comm = #{queryComm,jdbcType=VARCHAR},",
            "query_type = #{queryType,jdbcType=CHAR},",
            "query_ctx = #{queryCtx,jdbcType=VARCHAR},",
            "query_public = #{queryPublic,jdbcType=CHAR},",
            "query_active = #{queryActive,jdbcType=CHAR},",
            "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
            "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
            "export_mime_type = #{exportMimeType,jdbcType=VARCHAR},",
            "orientation = #{orientation,jdbcType=VARCHAR}",
            "where query_id = #{queryId,jdbcType=VARCHAR}"
    })
    int updateInfoBase(QueryConfig row);


    @Update({
            "update query_config",
            "set",
            "cond0_name = #{cond0Name,jdbcType=VARCHAR},",
            "cond0_comm = #{cond0Comm,jdbcType=VARCHAR},",
            "cond0_info = #{cond0Info,jdbcType=VARCHAR},",
            "cond1_name = #{cond1Name,jdbcType=VARCHAR},",
            "cond1_comm = #{cond1Comm,jdbcType=VARCHAR},",
            "cond1_info = #{cond1Info,jdbcType=VARCHAR},",
            "cond2_name = #{cond2Name,jdbcType=VARCHAR},",
            "cond2_comm = #{cond2Comm,jdbcType=VARCHAR},",
            "cond2_info = #{cond2Info,jdbcType=VARCHAR},",
            "cond3_name = #{cond3Name,jdbcType=VARCHAR},",
            "cond3_comm = #{cond3Comm,jdbcType=VARCHAR},",
            "cond3_info = #{cond3Info,jdbcType=VARCHAR},",
            "cond4_name = #{cond4Name,jdbcType=VARCHAR},",
            "cond4_comm = #{cond4Comm,jdbcType=VARCHAR},",
            "cond4_info = #{cond4Info,jdbcType=VARCHAR},",
            "cond5_name = #{cond5Name,jdbcType=VARCHAR},",
            "cond5_comm = #{cond5Comm,jdbcType=VARCHAR},",
            "cond5_info = #{cond5Info,jdbcType=VARCHAR},",
            "cond6_name = #{cond6Name,jdbcType=VARCHAR},",
            "cond6_comm = #{cond6Comm,jdbcType=VARCHAR},",
            "cond6_info = #{cond6Info,jdbcType=VARCHAR},",
            "cond7_name = #{cond7Name,jdbcType=VARCHAR},",
            "cond7_comm = #{cond7Comm,jdbcType=VARCHAR},",
            "cond7_info = #{cond7Info,jdbcType=VARCHAR},",
            "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
            "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
            "where query_id = #{queryId,jdbcType=VARCHAR}"
    })
    int updateConditions(QueryConfig row);

}