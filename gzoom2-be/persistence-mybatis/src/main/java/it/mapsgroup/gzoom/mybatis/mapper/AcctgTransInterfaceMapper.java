package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.AcctgTransInterface;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AcctgTransInterfaceMapper {

    @Insert({
        "insert into acctg_trans_interface (id, stato, ",
        "data_source, voucher_ref, ",
        "uom_descr, ref_date, ",
        "gl_account_type_enum_id, gl_account_code, ",
        "amount, amount_code, ",
        "gl_fiscal_type_id, uorg_code, ",
        "uorg_role_type_id, note, ",
        "comments, product_code, ",
        "work_effort_code, party_code, ",
        "role_type_id, from_date_competence, ",
        "to_date_competence, note_lang, ",
        "comments_lang, custom_text01, ",
        "custom_text02, custom_text03, ",
        "custom_text04, custom_text05, ",
        "custom_date01, custom_date02, ",
        "custom_date03, custom_date04, ",
        "custom_date05, custom_value01, ",
        "custom_value02, custom_value03, ",
        "custom_value04, custom_value05, ",
        "seq)",
        "values (#{id,jdbcType=VARCHAR}, #{stato,jdbcType=VARCHAR}, ",
        "#{dataSource,jdbcType=VARCHAR}, #{voucherRef,jdbcType=VARCHAR}, ",
        "#{uomDescr,jdbcType=VARCHAR}, #{refDate,jdbcType=TIMESTAMP}, ",
        "#{glAccountTypeEnumId,jdbcType=VARCHAR}, #{glAccountCode,jdbcType=VARCHAR}, ",
        "#{amount,jdbcType=NUMERIC}, #{amountCode,jdbcType=VARCHAR}, ",
        "#{glFiscalTypeId,jdbcType=VARCHAR}, #{uorgCode,jdbcType=VARCHAR}, ",
        "#{uorgRoleTypeId,jdbcType=VARCHAR}, #{note,jdbcType=VARCHAR}, ",
        "#{comments,jdbcType=VARCHAR}, #{productCode,jdbcType=VARCHAR}, ",
        "#{workEffortCode,jdbcType=VARCHAR}, #{partyCode,jdbcType=VARCHAR}, ",
        "#{roleTypeId,jdbcType=VARCHAR}, #{fromDateCompetence,jdbcType=TIMESTAMP}, ",
        "#{toDateCompetence,jdbcType=TIMESTAMP}, #{noteLang,jdbcType=VARCHAR}, ",
        "#{commentsLang,jdbcType=VARCHAR}, #{customText01,jdbcType=VARCHAR}, ",
        "#{customText02,jdbcType=VARCHAR}, #{customText03,jdbcType=VARCHAR}, ",
        "#{customText04,jdbcType=VARCHAR}, #{customText05,jdbcType=VARCHAR}, ",
        "#{customDate01,jdbcType=TIMESTAMP}, #{customDate02,jdbcType=TIMESTAMP}, ",
        "#{customDate03,jdbcType=TIMESTAMP}, #{customDate04,jdbcType=TIMESTAMP}, ",
        "#{customDate05,jdbcType=TIMESTAMP}, #{customValue01,jdbcType=DOUBLE}, ",
        "#{customValue02,jdbcType=DOUBLE}, #{customValue03,jdbcType=DOUBLE}, ",
        "#{customValue04,jdbcType=DOUBLE}, #{customValue05,jdbcType=DOUBLE}, ",
        "#{seq,jdbcType=NUMERIC})"
    })
    int insert(AcctgTransInterface row);


    @Select({
        "select",
        "id, stato, data_source, voucher_ref, uom_descr, ref_date, gl_account_type_enum_id, ",
        "gl_account_code, amount, amount_code, gl_fiscal_type_id, uorg_code, uorg_role_type_id, ",
        "note, comments, product_code, work_effort_code, party_code, role_type_id, from_date_competence, ",
        "to_date_competence, note_lang, comments_lang, custom_text01, custom_text02, ",
        "custom_text03, custom_text04, custom_text05, custom_date01, custom_date02, custom_date03, ",
        "custom_date04, custom_date05, custom_value01, custom_value02, custom_value03, ",
        "custom_value04, custom_value05, seq",
        "from acctg_trans_interface",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="stato", property="stato", jdbcType=JdbcType.VARCHAR),
        @Result(column="data_source", property="dataSource", jdbcType=JdbcType.VARCHAR),
        @Result(column="voucher_ref", property="voucherRef", jdbcType=JdbcType.VARCHAR),
        @Result(column="uom_descr", property="uomDescr", jdbcType=JdbcType.VARCHAR),
        @Result(column="ref_date", property="refDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="gl_account_type_enum_id", property="glAccountTypeEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="gl_account_code", property="glAccountCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="amount", property="amount", jdbcType=JdbcType.NUMERIC),
        @Result(column="amount_code", property="amountCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="gl_fiscal_type_id", property="glFiscalTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="uorg_code", property="uorgCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="uorg_role_type_id", property="uorgRoleTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="note", property="note", jdbcType=JdbcType.VARCHAR),
        @Result(column="comments", property="comments", jdbcType=JdbcType.VARCHAR),
        @Result(column="product_code", property="productCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_code", property="workEffortCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="party_code", property="partyCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="role_type_id", property="roleTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="from_date_competence", property="fromDateCompetence", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="to_date_competence", property="toDateCompetence", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="note_lang", property="noteLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="comments_lang", property="commentsLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="custom_text01", property="customText01", jdbcType=JdbcType.VARCHAR),
        @Result(column="custom_text02", property="customText02", jdbcType=JdbcType.VARCHAR),
        @Result(column="custom_text03", property="customText03", jdbcType=JdbcType.VARCHAR),
        @Result(column="custom_text04", property="customText04", jdbcType=JdbcType.VARCHAR),
        @Result(column="custom_text05", property="customText05", jdbcType=JdbcType.VARCHAR),
        @Result(column="custom_date01", property="customDate01", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="custom_date02", property="customDate02", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="custom_date03", property="customDate03", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="custom_date04", property="customDate04", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="custom_date05", property="customDate05", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="custom_value01", property="customValue01", jdbcType=JdbcType.DOUBLE),
        @Result(column="custom_value02", property="customValue02", jdbcType=JdbcType.DOUBLE),
        @Result(column="custom_value03", property="customValue03", jdbcType=JdbcType.DOUBLE),
        @Result(column="custom_value04", property="customValue04", jdbcType=JdbcType.DOUBLE),
        @Result(column="custom_value05", property="customValue05", jdbcType=JdbcType.DOUBLE),
        @Result(column="seq", property="seq", jdbcType=JdbcType.NUMERIC)
    })
    AcctgTransInterface selectByPrimaryKey(String id);


    @Select({
        "select",
        "id, stato, data_source, voucher_ref, uom_descr, ref_date, gl_account_type_enum_id, ",
        "gl_account_code, amount, amount_code, gl_fiscal_type_id, uorg_code, uorg_role_type_id, ",
        "note, comments, product_code, work_effort_code, party_code, role_type_id, from_date_competence, ",
        "to_date_competence, note_lang, comments_lang, custom_text01, custom_text02, ",
        "custom_text03, custom_text04, custom_text05, custom_date01, custom_date02, custom_date03, ",
        "custom_date04, custom_date05, custom_value01, custom_value02, custom_value03, ",
        "custom_value04, custom_value05, seq",
        "from acctg_trans_interface"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="stato", property="stato", jdbcType=JdbcType.VARCHAR),
        @Result(column="data_source", property="dataSource", jdbcType=JdbcType.VARCHAR),
        @Result(column="voucher_ref", property="voucherRef", jdbcType=JdbcType.VARCHAR),
        @Result(column="uom_descr", property="uomDescr", jdbcType=JdbcType.VARCHAR),
        @Result(column="ref_date", property="refDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="gl_account_type_enum_id", property="glAccountTypeEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="gl_account_code", property="glAccountCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="amount", property="amount", jdbcType=JdbcType.NUMERIC),
        @Result(column="amount_code", property="amountCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="gl_fiscal_type_id", property="glFiscalTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="uorg_code", property="uorgCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="uorg_role_type_id", property="uorgRoleTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="note", property="note", jdbcType=JdbcType.VARCHAR),
        @Result(column="comments", property="comments", jdbcType=JdbcType.VARCHAR),
        @Result(column="product_code", property="productCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_code", property="workEffortCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="party_code", property="partyCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="role_type_id", property="roleTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="from_date_competence", property="fromDateCompetence", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="to_date_competence", property="toDateCompetence", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="note_lang", property="noteLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="comments_lang", property="commentsLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="custom_text01", property="customText01", jdbcType=JdbcType.VARCHAR),
        @Result(column="custom_text02", property="customText02", jdbcType=JdbcType.VARCHAR),
        @Result(column="custom_text03", property="customText03", jdbcType=JdbcType.VARCHAR),
        @Result(column="custom_text04", property="customText04", jdbcType=JdbcType.VARCHAR),
        @Result(column="custom_text05", property="customText05", jdbcType=JdbcType.VARCHAR),
        @Result(column="custom_date01", property="customDate01", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="custom_date02", property="customDate02", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="custom_date03", property="customDate03", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="custom_date04", property="customDate04", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="custom_date05", property="customDate05", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="custom_value01", property="customValue01", jdbcType=JdbcType.DOUBLE),
        @Result(column="custom_value02", property="customValue02", jdbcType=JdbcType.DOUBLE),
        @Result(column="custom_value03", property="customValue03", jdbcType=JdbcType.DOUBLE),
        @Result(column="custom_value04", property="customValue04", jdbcType=JdbcType.DOUBLE),
        @Result(column="custom_value05", property="customValue05", jdbcType=JdbcType.DOUBLE),
        @Result(column="seq", property="seq", jdbcType=JdbcType.NUMERIC)
    })
    List<AcctgTransInterface> selectAll();

}