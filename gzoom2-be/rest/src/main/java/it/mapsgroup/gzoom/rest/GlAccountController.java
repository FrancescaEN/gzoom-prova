package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.GlAccount;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountEx;
import it.mapsgroup.gzoom.service.GlAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
public class GlAccountController {

    private final GlAccountService glAccountService;

    @Autowired
    public GlAccountController(GlAccountService glAccountService) {
        this.glAccountService = glAccountService;
    }

    @RequestMapping(value = "glaccount-precision/{glAccountId}", method = RequestMethod.GET)
    @ResponseBody
    public BigInteger getDecimalPrecision(@PathVariable(value = "glAccountId") String glAccountId) {
        return Exec.exec("get glaccount-precision", () -> glAccountService.getDecimalPrecision(glAccountId));
    }

    @GetMapping("gl-account/{glAccountId}")
    @ResponseBody
    public GlAccount getGlAccount(@PathVariable String glAccountId) {
        return Exec.exec("get GlAccount by id", () -> glAccountService.getGlAccount(glAccountId));
    }

    @GetMapping("gl-account/{glAccountId}/input-enum-id-and-detect-org-unit-id-flag/{inputEnumId}/{detectOrgUnitIdFlag}")
    @ResponseBody
    public boolean isByInputEnumIdAndDetectOrgUnitIdFlag(@PathVariable String glAccountId, @PathVariable String inputEnumId, @PathVariable String detectOrgUnitIdFlag) {
        return Exec.exec("isByInputEnumIdAndDetectOrgUnitIdFlag", () -> glAccountService.isByInputEnumIdAndDetectOrgUnitIdFlag(glAccountId, inputEnumId, detectOrgUnitIdFlag));
    }

    @GetMapping(value = {"gl-account/account-type-enum-id/{accountTypeEnumId}/{isReservedAccount}", "gl-account/account-type-enum-id/{accountTypeEnumId}"})
    @ResponseBody
    public Result<GlAccount> getGlAccount(
            @PathVariable String accountTypeEnumId,
            @PathVariable(required = false) String isReservedAccount,
            @RequestParam(required = false) boolean isSecondaryLang,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String matchModeSearch,
            @RequestParam(required = false) String[] workEffortPurposeTypeId,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderType,
            @RequestParam(required = false) String glAccountTypeId,
            @RequestParam(required = false) String inputEnumId,
            @RequestParam(required = false) String detectOrgUnitIdFlag,
            @RequestParam(required = false) String debitCreditDefault,
            @RequestParam(required = false) String periodTypeId,
            @RequestParam(required = false) String currentStatusId,
            @RequestParam(required = false) String respCenterId

    ) {
        return Exec.exec("get GlAccount By AccountTypeEnumId ", () -> this.glAccountService.getGlAccount(
                accountTypeEnumId,
                isReservedAccount,
                isSecondaryLang,
                search,
                matchModeSearch,
                workEffortPurposeTypeId,
                orderBy,
                orderType,
                glAccountTypeId,
                inputEnumId,
                detectOrgUnitIdFlag,
                debitCreditDefault,
                periodTypeId,
                currentStatusId,
                respCenterId)
        );
    }

    @GetMapping(value = {"gl-account/filter-params"})
    @ResponseBody
    public Result<GlAccount> getGlAccountByFilterParams(
            @RequestParam(required = false) boolean isSecondaryLang,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String matchModeSearch,
            @RequestParam(required = false) String[] workEffortPurposeTypeId,
            @RequestParam(required = false) String glAccountTypeId

    ) {
        return Exec.exec("get GlAccount By AccountTypeEnumId ", () -> this.glAccountService.getGlAccountByFilterParams(
                        isSecondaryLang,
                        search,
                        matchModeSearch,
                        workEffortPurposeTypeId,
                        glAccountTypeId
                )
        );
    }

    @GetMapping(value = {
            "gl-account/movements/{accountTypeEnumId}/{inputEnumId}/{detectOrgUnitIdFlag}",
            "gl-account/movements/{accountTypeEnumId}/{inputEnumId}/{detectOrgUnitIdFlag}/{isReservedAccount}",
    })
    @ResponseBody
    public Result<GlAccount> getGlAccountMovements(
            @PathVariable String accountTypeEnumId,
            @PathVariable(required = false) String isReservedAccount,
            @PathVariable String inputEnumId,
            @PathVariable String detectOrgUnitIdFlag
    ) {
        return Exec.exec("get GlAccount By AccountTypeEnumId ", () -> this.glAccountService.getGlAccountMovements(
                accountTypeEnumId, isReservedAccount, inputEnumId, detectOrgUnitIdFlag)
        );
    }

    @GetMapping("gl-account/referenced-account-id")
    @ResponseBody
    public Result<GlAccount> getGlAccountByOrganizationPartyId() {
        return Exec.exec("get GlAccount By OrganizationPartyId", this.glAccountService::getGlAccountByOrganizationPartyId);
    }

    @GetMapping("gl-account/select-all-by-org-user")
    @ResponseBody
    public Result<GlAccount> selectGlAccountByOrgId() {
        return Exec.exec("get GlAccount By OrganizationId and User", this.glAccountService::selectGlAccountByOrgId);
    }

    @PostMapping(value = "gl-account")
    @ResponseBody
    public GlAccount createGlAccount(@RequestBody GlAccountEx glAccountEx) {
        return Exec.exec("create gl-account", () -> this.glAccountService.createGlAccount(glAccountEx.getGlAccount(), glAccountEx.getWorkEffortPurposeTypeId()));
    }

    @PutMapping(value = "gl-account")
    @ResponseBody
    public boolean updateGlAccount(@RequestBody GlAccount glAccount) {
        return Exec.exec("update gl-account", () -> this.glAccountService.updateGlAccount(glAccount));
    }

    @PutMapping(value = {"gl-account/{glAccountId}/calc-method"})
    @ResponseBody
    public boolean updateCalcCustomMethodAndPrioCalc(@PathVariable String glAccountId, @PathVariable(required = false) String calcCustomMethodId, @RequestBody(required = false) GlAccount glAccount) {
        return Exec.exec("update gl-account-calc-method", () -> this.glAccountService.updateCalcCustomMethodAndPrioCalc(glAccountId, glAccount.getCalcCustomMethodId(), glAccount.getPrioCalc()));
    }

    @DeleteMapping("gl-account/{glAccountIds}")
    @ResponseBody
    public boolean deleteGlAccount(@PathVariable String[] glAccountIds) {
        return Exec.exec("delete gl-account", () -> this.glAccountService.deleteGlAccount(glAccountIds));
    }
}
