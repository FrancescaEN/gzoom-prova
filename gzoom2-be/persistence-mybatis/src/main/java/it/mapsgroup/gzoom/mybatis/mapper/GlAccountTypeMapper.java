package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.GlAccountType;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GlAccountTypeMapper {

    @Delete({
        "delete from gl_account_type",
        "where gl_account_type_id = #{glAccountTypeId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String glAccountTypeId);


    @Insert({
        "insert into gl_account_type (gl_account_type_id, parent_type_id, ",
        "has_table, description, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "account_type_enum_id, is_reserved_account, ",
        "description_lang, last_modified_by_user_login, ",
        "created_by_user_login)",
        "values (#{glAccountTypeId,jdbcType=VARCHAR}, #{parentTypeId,jdbcType=VARCHAR}, ",
        "#{hasTable,jdbcType=CHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{accountTypeEnumId,jdbcType=VARCHAR}, #{isReservedAccount,jdbcType=CHAR}, ",
        "#{descriptionLang,jdbcType=VARCHAR}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(GlAccountType row);


    @Select({
        "select",
        "gl_account_type_id, parent_type_id, has_table, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, account_type_enum_id, ",
        "is_reserved_account, description_lang, last_modified_by_user_login, created_by_user_login",
        "from gl_account_type",
        "where gl_account_type_id = #{glAccountTypeId,jdbcType=VARCHAR}"
    })
    @Results(id = "glAccountType", value = {
        @Result(column="gl_account_type_id", property="glAccountTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="parent_type_id", property="parentTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="has_table", property="hasTable", jdbcType=JdbcType.CHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="account_type_enum_id", property="accountTypeEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="is_reserved_account", property="isReservedAccount", jdbcType=JdbcType.CHAR),
        @Result(column="description_lang", property="descriptionLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    GlAccountType selectByPrimaryKey(String glAccountTypeId);


    @Select({
        "select",
        "gl_account_type_id, parent_type_id, has_table, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, account_type_enum_id, ",
        "is_reserved_account, description_lang, last_modified_by_user_login, created_by_user_login",
        "from gl_account_type",
            "order by gl_account_type_id"
    })
    @ResultMap("glAccountType")
    List<GlAccountType> selectAllOrderByPrimaryKey();

    @Select({
            "select",
            "gl_account_type_id, parent_type_id, has_table, description, last_updated_stamp, ",
            "last_updated_tx_stamp, created_stamp, created_tx_stamp, account_type_enum_id, ",
            "is_reserved_account, description_lang, last_modified_by_user_login, created_by_user_login",
            "from gl_account_type",
            "where account_type_enum_id = #{accountTypeEnumId,jdbcType=VARCHAR}",
            "order by gl_account_type_id"
    })
    @ResultMap("glAccountType")
    List<GlAccountType> selectByAccountTypeEnumIdOrderByPrimaryKey(@Param("accountTypeEnumId") String accountTypeEnumId);

    @Select({
            "select",
            "gl_account_type_id, parent_type_id, has_table, description, last_updated_stamp, ",
            "last_updated_tx_stamp, created_stamp, created_tx_stamp, account_type_enum_id, ",
            "is_reserved_account, description_lang, last_modified_by_user_login, created_by_user_login",
            "from gl_account_type",
            "where account_type_enum_id = #{accountTypeEnumId,jdbcType=VARCHAR}",
            "and is_reserved_account = #{isReservedAccount,jdbcType=VARCHAR}"
    })
    @ResultMap("glAccountType")
    List<GlAccountType> selectByAccountTypeEnumIdAndIsReservedAccountOrdByPK(String accountTypeEnumId, String isReservedAccount);


    @Update({
        "update gl_account_type",
        "set parent_type_id = #{parentTypeId,jdbcType=VARCHAR},",
          "has_table = #{hasTable,jdbcType=CHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "account_type_enum_id = #{accountTypeEnumId,jdbcType=VARCHAR},",
          "is_reserved_account = #{isReservedAccount,jdbcType=CHAR},",
          "description_lang = #{descriptionLang,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where gl_account_type_id = #{glAccountTypeId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(GlAccountType row);
}