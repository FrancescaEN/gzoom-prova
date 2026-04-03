package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.GlResourceType;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GlResourceTypeMapper {

    @Delete({
        "delete from gl_resource_type",
        "where gl_resource_type_id = #{glResourceTypeId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String glResourceTypeId);


    @Insert({
        "insert into gl_resource_type (gl_resource_type_id, description, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "description_lang, last_modified_by_user_login, ",
        "created_by_user_login)",
        "values (#{glResourceTypeId,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{descriptionLang,jdbcType=VARCHAR}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(GlResourceType row);


    @Select({
        "select",
        "gl_resource_type_id, description, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, description_lang, last_modified_by_user_login, ",
        "created_by_user_login",
        "from gl_resource_type",
        "where gl_resource_type_id = #{glResourceTypeId,jdbcType=VARCHAR}"
    })
    @Results(id = "glResourceType", value = {
        @Result(column="gl_resource_type_id", property="glResourceTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="description_lang", property="descriptionLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    GlResourceType selectByPrimaryKey(String glResourceTypeId);


    @Select({
        "select",
        "gl_resource_type_id, description, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, description_lang, last_modified_by_user_login, ",
        "created_by_user_login",
        "from gl_resource_type",
            "order by gl_resource_type_id"
    })
    @ResultMap("glResourceType")
    List<GlResourceType> selectAllOrderByPrimaryKey();

    @Select({
            "select grt.*",
            "from gl_resource_type grt",
            "join gl_account_resource gar on gar.gl_resource_type_id = grt.gl_resource_type_id",
            "where gar.gl_account_type_id = #{glAccountTypeId,jdbcType=VARCHAR}"
    })
    @ResultMap("glResourceType")
    List<GlResourceType> selectByGlAccountTypeId(String glAccountTypeId);


    @Update({
        "update gl_resource_type",
        "set description = #{description,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "description_lang = #{descriptionLang,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where gl_resource_type_id = #{glResourceTypeId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(GlResourceType row);
}