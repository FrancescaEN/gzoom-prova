package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.UserPreference;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserPreferenceMapper {

    @Delete({
        "delete from user_preference",
        "where user_login_id = #{userLoginId,jdbcType=VARCHAR}",
          "and user_pref_type_id = #{userPrefTypeId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(@Param("userLoginId") String userLoginId, @Param("userPrefTypeId") String userPrefTypeId);


    @Insert({
        "insert into user_preference (user_login_id, user_pref_type_id, ",
        "user_pref_group_type_id, user_pref_value, ",
        "user_pref_data_type, xml_user_pref, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp)",
        "values (#{userLoginId,jdbcType=VARCHAR}, #{userPrefTypeId,jdbcType=VARCHAR}, ",
        "#{userPrefGroupTypeId,jdbcType=VARCHAR}, #{userPrefValue,jdbcType=VARCHAR}, ",
        "#{userPrefDataType,jdbcType=VARCHAR}, #{xmlUserPref,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(UserPreference row);


    @Select({
        "select",
        "user_login_id, user_pref_type_id, user_pref_group_type_id, user_pref_value, ",
        "user_pref_data_type, xml_user_pref, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp",
        "from user_preference",
        "where user_login_id = #{userLoginId,jdbcType=VARCHAR}",
          "and user_pref_type_id = #{userPrefTypeId,jdbcType=VARCHAR}"
    })
    @Results(id = "userPreference", value = {
        @Result(column="user_login_id", property="userLoginId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="user_pref_type_id", property="userPrefTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="user_pref_group_type_id", property="userPrefGroupTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_pref_value", property="userPrefValue", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_pref_data_type", property="userPrefDataType", jdbcType=JdbcType.VARCHAR),
        @Result(column="xml_user_pref", property="xmlUserPref", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    UserPreference selectByPrimaryKey(@Param("userLoginId") String userLoginId, @Param("userPrefTypeId") String userPrefTypeId);


    @Select({
        "select",
        "user_login_id, user_pref_type_id, user_pref_group_type_id, user_pref_value, ",
        "user_pref_data_type, xml_user_pref, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp",
        "from user_preference"
    })
    @ResultMap("userPreference")
    List<UserPreference> selectAll();


    @Update({
        "update user_preference",
        "set user_pref_group_type_id = #{userPrefGroupTypeId,jdbcType=VARCHAR},",
          "user_pref_value = #{userPrefValue,jdbcType=VARCHAR},",
          "user_pref_data_type = #{userPrefDataType,jdbcType=VARCHAR},",
          "xml_user_pref = #{xmlUserPref,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where user_login_id = #{userLoginId,jdbcType=VARCHAR}",
          "and user_pref_type_id = #{userPrefTypeId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(UserPreference row);
}