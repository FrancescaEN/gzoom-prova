package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.PartyContactMech;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PartyContactMechMapper {

    @Delete({
        "delete from party_contact_mech",
        "where party_id = #{partyId,jdbcType=VARCHAR}",
          "and contact_mech_id = #{contactMechId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    int deleteByPrimaryKey(@Param("partyId") String partyId, @Param("contactMechId") String contactMechId, @Param("fromDate") LocalDateTime fromDate);


    @Insert({
        "insert into party_contact_mech (party_id, contact_mech_id, ",
        "from_date, thru_date, ",
        "role_type_id, allow_solicitation, ",
        "extension, verified, ",
        "comments, years_with_contact_mech, ",
        "months_with_contact_mech, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, last_modified_by_user_login, ",
        "created_by_user_login)",
        "values (#{partyId,jdbcType=VARCHAR}, #{contactMechId,jdbcType=VARCHAR}, ",
        "#{fromDate,jdbcType=TIMESTAMP}, #{thruDate,jdbcType=TIMESTAMP}, ",
        "#{roleTypeId,jdbcType=VARCHAR}, #{allowSolicitation,jdbcType=CHAR}, ",
        "#{extension,jdbcType=VARCHAR}, #{verified,jdbcType=CHAR}, ",
        "#{comments,jdbcType=VARCHAR}, #{yearsWithContactMech,jdbcType=NUMERIC}, ",
        "#{monthsWithContactMech,jdbcType=NUMERIC}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(PartyContactMech row);


    @Select({
        "select",
        "party_id, contact_mech_id, from_date, thru_date, role_type_id, allow_solicitation, ",
        "extension, verified, comments, years_with_contact_mech, months_with_contact_mech, ",
        "last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp, ",
        "last_modified_by_user_login, created_by_user_login",
        "from party_contact_mech",
        "where party_id = #{partyId,jdbcType=VARCHAR}",
          "and contact_mech_id = #{contactMechId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    @Results(id = "partyContactMech", value = {
        @Result(column="party_id", property="partyId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="contact_mech_id", property="contactMechId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="from_date", property="fromDate", jdbcType=JdbcType.TIMESTAMP, id=true),
        @Result(column="thru_date", property="thruDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="role_type_id", property="roleTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="allow_solicitation", property="allowSolicitation", jdbcType=JdbcType.CHAR),
        @Result(column="extension", property="extension", jdbcType=JdbcType.VARCHAR),
        @Result(column="verified", property="verified", jdbcType=JdbcType.CHAR),
        @Result(column="comments", property="comments", jdbcType=JdbcType.VARCHAR),
        @Result(column="years_with_contact_mech", property="yearsWithContactMech", jdbcType=JdbcType.NUMERIC),
        @Result(column="months_with_contact_mech", property="monthsWithContactMech", jdbcType=JdbcType.NUMERIC),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    PartyContactMech selectByPrimaryKey(@Param("partyId") String partyId, @Param("contactMechId") String contactMechId, @Param("fromDate") LocalDateTime fromDate);


    @Select({
        "select",
        "party_id, contact_mech_id, from_date, thru_date, role_type_id, allow_solicitation, ",
        "extension, verified, comments, years_with_contact_mech, months_with_contact_mech, ",
        "last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp, ",
        "last_modified_by_user_login, created_by_user_login",
        "from party_contact_mech"
    })
    @ResultMap("partyContactMech")
    List<PartyContactMech> selectAll();

    List<PartyContactMech> getContactMechWorkEffortTypeRole(@Param("workEffortTypeId") String workEffortTypeId);


    @Update({
        "update party_contact_mech",
        "set thru_date = #{thruDate,jdbcType=TIMESTAMP},",
          "role_type_id = #{roleTypeId,jdbcType=VARCHAR},",
          "allow_solicitation = #{allowSolicitation,jdbcType=CHAR},",
          "extension = #{extension,jdbcType=VARCHAR},",
          "verified = #{verified,jdbcType=CHAR},",
          "comments = #{comments,jdbcType=VARCHAR},",
          "years_with_contact_mech = #{yearsWithContactMech,jdbcType=NUMERIC},",
          "months_with_contact_mech = #{monthsWithContactMech,jdbcType=NUMERIC},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where party_id = #{partyId,jdbcType=VARCHAR}",
          "and contact_mech_id = #{contactMechId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    int updateByPrimaryKey(PartyContactMech row);
}