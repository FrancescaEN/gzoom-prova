package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.commons.TableLazyLoad;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.AcctgTransEntry;
import it.mapsgroup.gzoom.service.AcctgTransEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "acctg-trans-entry", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AcctgTransEntryController {
    private final AcctgTransEntryService acctgTransEntryService;

    @Autowired
    public AcctgTransEntryController(AcctgTransEntryService acctgTransEntryService) {
        this.acctgTransEntryService = acctgTransEntryService;
    }

    @GetMapping("{acctgTransId}/{acctgTransEntrySeqId}")
    @ResponseBody
    public AcctgTransEntry getMovementByPrimaryKey(@PathVariable String acctgTransId, @PathVariable String acctgTransEntrySeqId) {
        return this.acctgTransEntryService.getMovementByPrimaryKey(acctgTransId, acctgTransEntrySeqId);
    }

    @PostMapping(value = {
            "indicator-movements/{accountTypeEnumId}/{inputEnumId}/{detectOrgUnitIdFlag}",
            "indicator-movements/{accountTypeEnumId}/{inputEnumId}/{detectOrgUnitIdFlag}/{isReservedAccount}"})
    @ResponseBody
    public Result<AcctgTransEntry> getIndicatorMovements(
            @PathVariable String accountTypeEnumId,
            @PathVariable String inputEnumId,
            @PathVariable String detectOrgUnitIdFlag,
            @PathVariable(required = false) String isReservedAccount,
            @RequestBody TableLazyLoad tableLazyLoad,
            @RequestParam(required = false) String glAccountId,
            @RequestParam(required = false) String uomId,
            @RequestParam(required = false) String customTimePeriodId,
            @RequestParam(required = false) String glFiscalTypeId
            ) {

        return Exec.exec("get indicator-movements", () -> this.acctgTransEntryService.getIndicatorMovements(
                accountTypeEnumId,
                isReservedAccount,
                inputEnumId,
                detectOrgUnitIdFlag,
                tableLazyLoad,
                glAccountId,
                uomId,
                customTimePeriodId,
                glFiscalTypeId
        ));
    }

    @PostMapping(value = {
            "count-indicator-movements/{accountTypeEnumId}/{inputEnumId}/{detectOrgUnitIdFlag}",
            "count-indicator-movements/{accountTypeEnumId}/{inputEnumId}/{detectOrgUnitIdFlag}/{isReservedAccount}"})
    @ResponseBody
    public Integer countIndicatorMovements(
            @PathVariable String accountTypeEnumId,
            @PathVariable String inputEnumId,
            @PathVariable String detectOrgUnitIdFlag,
            @PathVariable(required = false) String isReservedAccount,
            @RequestBody(required = false) TableLazyLoad tableLazyLoad,
            @RequestParam(required = false) String glAccountId,
            @RequestParam(required = false) String uomId,
            @RequestParam(required = false) String customTimePeriodId,
            @RequestParam(required = false) String glFiscalTypeId) {
        return Exec.exec("get indicator-movements", () -> this.acctgTransEntryService.countIndicatorMovements(accountTypeEnumId, isReservedAccount, inputEnumId, detectOrgUnitIdFlag, tableLazyLoad,  glAccountId,
                uomId,
                customTimePeriodId,
                glFiscalTypeId));
    }

    @PostMapping("create/{customTimePeriodId}")
    @ResponseBody
    public boolean createAcctgTransEntryEx(@PathVariable String customTimePeriodId, @RequestBody AcctgTransEntry acctgTransEntry) {
        return Exec.exec("create acctg-trans-entry", () -> this.acctgTransEntryService.createAcctgTransEntryEx(acctgTransEntry, customTimePeriodId) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateAcctgTransEntry(@RequestBody AcctgTransEntry[] acctgTransEntries) {
        return Exec.exec("updateAcctgTransEntry", () -> this.acctgTransEntryService.updateAcctgTransEntry(acctgTransEntries));
    }

    @PutMapping("detail")
    @ResponseBody
    public boolean updateDetailAcctgTransEntry(@RequestBody AcctgTransEntry acctgTransEntry) {
        return Exec.exec("updateDetailAcctgTransEntry", () -> this.acctgTransEntryService.updateDetailAcctgTransEntry(acctgTransEntry));
    }

    @PostMapping("delete")
    @ResponseBody
    public boolean deleteAcctgTransEntries(@RequestBody AcctgTransEntry[] acctgTransEntries) {
        return Exec.exec("deleteAcctgTransEntries", () -> this.acctgTransEntryService.deleteAcctgTransEntries(acctgTransEntries));
    }

}
