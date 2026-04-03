package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.CustomMethodMatrix;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CustomMethodMatrixMapper {

    @Delete({
        "delete from custom_method_matrix",
        "where custom_method_matrix_id = #{customMethodMatrixId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String customMethodMatrixId);

    @Delete({
            "delete from custom_method_matrix",
            "where custom_method_id = #{customMethodId,jdbcType=VARCHAR}"
    })
    int deleteByCustomMethodId(@Param("customMethodId")String customMethodId);



    @Insert({
        "insert into custom_method_matrix (custom_method_matrix_id, custom_method_id, ",
        "row_input_value, column_input_value, ",
        "output_value, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp)",
        "values (#{customMethodMatrixId,jdbcType=VARCHAR}, #{customMethodId,jdbcType=VARCHAR}, ",
        "#{rowInputValue,jdbcType=NUMERIC}, #{columnInputValue,jdbcType=NUMERIC}, ",
        "#{outputValue,jdbcType=NUMERIC}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(CustomMethodMatrix row);


    @Select({
        "select",
        "custom_method_matrix_id, custom_method_id, row_input_value, column_input_value, ",
        "output_value, last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from custom_method_matrix",
        "where custom_method_matrix_id = #{customMethodMatrixId,jdbcType=VARCHAR}"
    })
    @Results(id = "customMethodMatrix", value = {
        @Result(column="custom_method_matrix_id", property="customMethodMatrixId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="custom_method_id", property="customMethodId", jdbcType=JdbcType.VARCHAR),
        @Result(column="row_input_value", property="rowInputValue", jdbcType=JdbcType.NUMERIC),
        @Result(column="column_input_value", property="columnInputValue", jdbcType=JdbcType.NUMERIC),
        @Result(column="output_value", property="outputValue", jdbcType=JdbcType.NUMERIC),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    CustomMethodMatrix selectByPrimaryKey(String customMethodMatrixId);


    @Select({
            "select",
            "custom_method_matrix_id, custom_method_id, row_input_value, column_input_value, ",
            "output_value, last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp",
            "from custom_method_matrix",
            "where  custom_method_id = #{customMethodId,jdbcType=VARCHAR}",
            "order by custom_method_matrix_id"
    })
    @ResultMap("customMethodMatrix")
    List<CustomMethodMatrix> selectByCustomMethodIdOrderByPrimaryKey(@Param("customMethodId")String customMethodId);


    @Update({
        "update custom_method_matrix",
        "set custom_method_id = #{customMethodId,jdbcType=VARCHAR},",
          "row_input_value = #{rowInputValue,jdbcType=NUMERIC},",
          "column_input_value = #{columnInputValue,jdbcType=NUMERIC},",
          "output_value = #{outputValue,jdbcType=NUMERIC},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where custom_method_matrix_id = #{customMethodMatrixId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(CustomMethodMatrix row);
}