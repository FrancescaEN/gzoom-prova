package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.ContactMech;
import it.mapsgroup.gzoom.mybatis.dto.ContactMechEx;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Mapper
@Repository
public interface ContactMechMapper {

    @Delete({
        "delete from contact_mech",
        "where contact_mech_id = #{contactMechId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String contactMechId);


    @Insert({
        "insert into contact_mech (contact_mech_id, contact_mech_type_id, ",
        "info_string, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, last_modified_by_user_login, ",
        "created_by_user_login)",
        "values (#{contactMechId,jdbcType=VARCHAR}, #{contactMechTypeId,jdbcType=VARCHAR}, ",
        "#{infoString,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(ContactMech row);


    @Select({
        "select",
        "contact_mech_id, contact_mech_type_id, info_string, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, last_modified_by_user_login, created_by_user_login",
        "from contact_mech",
        "where contact_mech_id = #{contactMechId,jdbcType=VARCHAR}"
    })
    @Results(id = "contactMech", value = {
        @Result(column="contact_mech_id", property="contactMechId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="contact_mech_type_id", property="contactMechTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="info_string", property="infoString", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    ContactMech selectByPrimaryKey(String contactMechId);


    @Select({
        "select",
        "contact_mech_id, contact_mech_type_id, info_string, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, last_modified_by_user_login, created_by_user_login",
        "from contact_mech"
    })
    @ResultMap("contactMech")
    List<ContactMech> selectAll();


    @Update({
        "update contact_mech",
        "set contact_mech_type_id = #{contactMechTypeId,jdbcType=VARCHAR},",
          "info_string = #{infoString,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where contact_mech_id = #{contactMechId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(ContactMech row);

    int anonymizeContactMech(Instant expirationDate);

    List<ContactMechEx> getEmailInfoString(@Param("username") String username);

    List<ContactMech> getInfoString(@Param("username") String username);
}