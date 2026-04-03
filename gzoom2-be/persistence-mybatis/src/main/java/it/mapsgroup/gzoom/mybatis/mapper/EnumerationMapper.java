package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.Enumeration;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EnumerationMapper {

    @Delete({
        "delete from enumeration",
        "where enum_id = #{enumId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String enumId);


    @Insert({
        "insert into enumeration (enum_id, enum_type_id, ",
        "enum_code, sequence_id, ",
        "description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, description_lang)",
        "values (#{enumId,jdbcType=VARCHAR}, #{enumTypeId,jdbcType=VARCHAR}, ",
        "#{enumCode,jdbcType=VARCHAR}, #{sequenceId,jdbcType=VARCHAR}, ",
        "#{description,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP}, #{descriptionLang,jdbcType=VARCHAR})"
    })
    int insert(Enumeration row);


    @Select({
        "select",
        "enum_id, enum_type_id, enum_code, sequence_id, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, description_lang",
        "from enumeration",
        "where enum_id = #{enumId,jdbcType=VARCHAR}"
    })
    @Results(id = "enumeration", value = {
        @Result(column="enum_id", property="enumId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="enum_type_id", property="enumTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="enum_code", property="enumCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="sequence_id", property="sequenceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="description_lang", property="descriptionLang", jdbcType=JdbcType.VARCHAR)
    })
    Enumeration selectByPrimaryKey(String enumId);


    @Select({
            "select",
            "enum_id, enum_type_id, enum_code, sequence_id, description, last_updated_stamp, ",
            "last_updated_tx_stamp, created_stamp, created_tx_stamp, description_lang",
            "from enumeration",
            "where enum_type_id = #{enumTypeId,jdbcType=VARCHAR}",
            "order by sequence_id"
    })
    @ResultMap("enumeration")
    List<Enumeration> selectByEnumTypeIdOrderBySequenceId(String enumTypeId);

    List<Enumeration> getEnumerationFilter(@Param("enumTypeId") String enumTypeId, @Param("field") String field, @Param("val") String value, @Param("secondaryLanguage") boolean secondaryLanguage);

    @Select({
        "select",
        "enum_id, enum_type_id, enum_code, sequence_id, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, description_lang",
        "from enumeration"
    })
    @ResultMap("enumeration")
    List<Enumeration> selectAll();


    @Update({
        "update enumeration",
        "set enum_type_id = #{enumTypeId,jdbcType=VARCHAR},",
          "enum_code = #{enumCode,jdbcType=VARCHAR},",
          "sequence_id = #{sequenceId,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "description_lang = #{descriptionLang,jdbcType=VARCHAR}",
        "where enum_id = #{enumId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(Enumeration row);
}