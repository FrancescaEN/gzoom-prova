package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.UserLogin;
import it.mapsgroup.gzoom.mybatis.dto.UserLoginPersistent;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Mapper
@Repository
public interface UserLoginMapper {

    int anonymizeUserLogin(Instant expirationDate);

    List<UserLogin> getUserLogin(@Param("username") String username);

    @Select({
            "select *",
            "from user_login",
            "where user_login.user_login_id = #{username}"
    })
    @ResultType(UserLogin.class)
    UserLogin selectByUserLoginId(@Param("username") String username);

    @Select({
            "select *",
            "from user_login",
            "order by user_login_id"
    })
    @ResultType(UserLogin.class)
    List<UserLogin> selectAllOrderByPrimaryKey();

    @Update({
            "update user_login",
            "set current_password = #{currentPassword,jdbcType=VARCHAR},",
            "password_hint = #{passwordHint,jdbcType=VARCHAR},",
            "is_system = #{isSystem,jdbcType=CHAR},",
            "enabled = #{enabled,jdbcType=CHAR},",
            "has_logged_out = #{hasLoggedOut,jdbcType=CHAR},",
            "require_password_change = #{requirePasswordChange,jdbcType=CHAR},",
            "last_currency_uom = #{lastCurrencyUom,jdbcType=VARCHAR},",
            "last_locale = #{lastLocale,jdbcType=VARCHAR},",
            "last_time_zone = #{lastTimeZone,jdbcType=VARCHAR},",
            "disabled_date_time = #{disabledDateTime,jdbcType=TIMESTAMP},",
            "successive_failed_logins = #{successiveFailedLogins,jdbcType=NUMERIC},",
            "external_auth_id = #{externalAuthId,jdbcType=VARCHAR},",
            "user_ldap_dn = #{userLdapDn,jdbcType=VARCHAR},",
            "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
            "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
            "party_id = #{partyId,jdbcType=VARCHAR},",
            "external_system = #{externalSystem,jdbcType=VARCHAR},",
            "description = #{description,jdbcType=VARCHAR}",
            "where user_login_id = #{userLoginId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(UserLoginPersistent row);
    
}