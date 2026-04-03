package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.GlAccountClass;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GlAccountClassMapper {
    @Delete({
        "delete from gl_account_class",
        "where gl_account_class_id = #{glAccountClassId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String glAccountClassId);

    @Insert({
        "insert into gl_account_class (gl_account_class_id, parent_class_id, ",
        "description, is_asset_class, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "account_class_code, local_description_content_id, ",
        "account_type_enum_id, child_folder_file, ",
        "comments, last_modified_by_user_login, ",
        "created_by_user_login)",
        "values (#{glAccountClassId,jdbcType=VARCHAR}, #{parentClassId,jdbcType=VARCHAR}, ",
        "#{description,jdbcType=VARCHAR}, #{isAssetClass,jdbcType=CHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{accountClassCode,jdbcType=VARCHAR}, #{localDescriptionContentId,jdbcType=VARCHAR}, ",
        "#{accountTypeEnumId,jdbcType=VARCHAR}, #{childFolderFile,jdbcType=VARCHAR}, ",
        "#{comments,jdbcType=VARCHAR}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(GlAccountClass row);

    @Select({
        "select",
        "gl_account_class_id, parent_class_id, description, is_asset_class, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, account_class_code, ",
        "local_description_content_id, account_type_enum_id, child_folder_file, comments, ",
        "last_modified_by_user_login, created_by_user_login",
        "from gl_account_class",
        "where gl_account_class_id = #{glAccountClassId,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="gl_account_class_id", property="glAccountClassId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="parent_class_id", property="parentClassId", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="is_asset_class", property="isAssetClass", jdbcType=JdbcType.CHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="account_class_code", property="accountClassCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="local_description_content_id", property="localDescriptionContentId", jdbcType=JdbcType.VARCHAR),
        @Result(column="account_type_enum_id", property="accountTypeEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="child_folder_file", property="childFolderFile", jdbcType=JdbcType.VARCHAR),
        @Result(column="comments", property="comments", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    GlAccountClass selectByPrimaryKey(String glAccountClassId);

    @Select({
        "select",
        "gl_account_class_id, parent_class_id, description, is_asset_class, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, account_class_code, ",
        "local_description_content_id, account_type_enum_id, child_folder_file, comments, ",
        "last_modified_by_user_login, created_by_user_login",
        "from gl_account_class"
    })
    @Results({
        @Result(column="gl_account_class_id", property="glAccountClassId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="parent_class_id", property="parentClassId", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="is_asset_class", property="isAssetClass", jdbcType=JdbcType.CHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="account_class_code", property="accountClassCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="local_description_content_id", property="localDescriptionContentId", jdbcType=JdbcType.VARCHAR),
        @Result(column="account_type_enum_id", property="accountTypeEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="child_folder_file", property="childFolderFile", jdbcType=JdbcType.VARCHAR),
        @Result(column="comments", property="comments", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    List<GlAccountClass> selectAll();

    @Select({
            "select gac.*",
            "from gl_account_class gac",
            "where gac.account_type_enum_id = #{accountTypeEnumId,jdbcType=VARCHAR}"
    })
    @ResultType(GlAccountClass.class)
    List<GlAccountClass> selectByAccountTypeEnumId(String accountTypeEnumId);

    @Update({
        "update gl_account_class",
        "set parent_class_id = #{parentClassId,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "is_asset_class = #{isAssetClass,jdbcType=CHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "account_class_code = #{accountClassCode,jdbcType=VARCHAR},",
          "local_description_content_id = #{localDescriptionContentId,jdbcType=VARCHAR},",
          "account_type_enum_id = #{accountTypeEnumId,jdbcType=VARCHAR},",
          "child_folder_file = #{childFolderFile,jdbcType=VARCHAR},",
          "comments = #{comments,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where gl_account_class_id = #{glAccountClassId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(GlAccountClass row);
}