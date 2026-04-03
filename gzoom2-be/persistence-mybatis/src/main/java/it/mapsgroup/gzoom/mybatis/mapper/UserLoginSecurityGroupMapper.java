package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.PortalPage;
import it.mapsgroup.gzoom.mybatis.dto.UserLoginSecurityGroup;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Mapper
@Repository
public interface UserLoginSecurityGroupMapper {

    @Delete({
        "delete from user_login_security_group",
        "where user_login_id = #{userLoginId,jdbcType=VARCHAR}",
          "and group_id = #{groupId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    int deleteByPrimaryKey(@Param("userLoginId") String userLoginId, @Param("groupId") String groupId, @Param("fromDate") Instant fromDate);

    @Delete({
            "delete from user_login_security_group",
            "where group_id = #{groupId,jdbcType=VARCHAR}"
    })
    int deleteByGroupId(@Param("groupId") String groupId);


    @Insert({
        "insert into user_login_security_group (user_login_id, group_id, ",
        "from_date, thru_date, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp)",
        "values (#{userLoginId,jdbcType=VARCHAR}, #{groupId,jdbcType=VARCHAR}, ",
        "#{fromDate,jdbcType=TIMESTAMP}, #{thruDate,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(UserLoginSecurityGroup row);


    @Select({
        "select",
        "user_login_id, group_id, from_date, thru_date, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp",
        "from user_login_security_group",
        "where user_login_id = #{userLoginId,jdbcType=VARCHAR}",
          "and group_id = #{groupId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    @Results({
        @Result(column="user_login_id", property="userLoginId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="from_date", property="fromDate", jdbcType=JdbcType.TIMESTAMP, id=true),
        @Result(column="thru_date", property="thruDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    UserLoginSecurityGroup selectByPrimaryKey(@Param("userLoginId") String userLoginId, @Param("groupId") String groupId, @Param("fromDate") Instant fromDate);


    @Select({
        "select",
        "user_login_id, group_id, from_date, thru_date, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp",
        "from user_login_security_group"
    })
    @Results(id = "userLoginSecurityGroupResultMap", value = {
        @Result(column="user_login_id", property="userLoginId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="from_date", property="fromDate", jdbcType=JdbcType.TIMESTAMP, id=true),
        @Result(column="thru_date", property="thruDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    List<UserLoginSecurityGroup> selectAll();

    @Select({
            "select",
            "user_login_id, group_id, from_date, thru_date, last_updated_stamp, last_updated_tx_stamp, ",
            "created_stamp, created_tx_stamp",
            "from user_login_security_group",
            "where group_id = #{groupId,jdbcType=VARCHAR}",
            "order by user_login_id, from_date"

    })
    @ResultType(UserLoginSecurityGroup.class)
    List<UserLoginSecurityGroup> selectByGroupIdOrderByPrimaryKey(@Param("groupId") String groupId);


    @Update({
        "update user_login_security_group",
        "set thru_date = #{thruDate,jdbcType=TIMESTAMP},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where user_login_id = #{userLoginId,jdbcType=VARCHAR}",
          "and group_id = #{groupId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    int updateByPrimaryKey(UserLoginSecurityGroup row);

    List<UserLoginSecurityGroup> getUserLoginSecurityGroups(@Param("userLoginId") String userLoginId, @Param("groupId") String groupId);

    @ResultType(PortalPage.class)
    List<PortalPage> getDefaultPortalPage(@Param("userLoginId") String userLoginId);

    List<UserLoginSecurityGroup> getUserLoginSecurityGroupsByUserLoginId(@Param("userLoginId") String userLoginId);

}