package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.UomRangeValues;
import it.mapsgroup.gzoom.mybatis.dto.UomRangeValuesExt;
import it.mapsgroup.gzoom.service.UomRangeValuesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class UomRangeValuesController {

    private final UomRangeValuesService uomRangeValuesService;

    @Autowired
    public UomRangeValuesController(UomRangeValuesService uomRangeValuesService) {
        this.uomRangeValuesService = uomRangeValuesService;
    }

    @RequestMapping(value = "uom-range-values/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Result<UomRangeValues> getUomRangeValues(@PathVariable(value = "id") String id) {
        return Exec.exec("uom-range-values/ get", () -> uomRangeValuesService.getUomRangeValues(id));
    }

    @RequestMapping(value = "uom-range-values-list/{uomRangeId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<UomRangeValues> getUomRangeValuesList(@PathVariable(value = "uomRangeId") String uomRangeId) {
        return Exec.exec("uom-range-values-list/ get", () -> uomRangeValuesService.getUomRangeValuesList(uomRangeId));
    }

    @RequestMapping(value = "uom-range-values-max/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Result<BigDecimal> getUomRangeValuesMax(@PathVariable(value = "id") String id) {
        return Exec.exec("get max uom-range-values", () -> uomRangeValuesService.getUomRangeValuesMax(id));
    }

    @RequestMapping(value = "uom-range-values-min/{id}", method = RequestMethod.GET)
    @ResponseBody
    public BigDecimal getUomRangeValuesMin(@PathVariable(value = "id") String id) {
        return Exec.exec("get min uom-range-values", () -> uomRangeValuesService.getUomRangeValuesMin(id));
    }

    @RequestMapping(value = "uom-range-values-path/{rangeDefault}/{amount}", method = RequestMethod.GET)
    @ResponseBody
    public Result<UomRangeValuesExt> getPathEmoticon(@PathVariable(value = "rangeDefault") String rangeDefault, @PathVariable(value = "amount") Float amount) {
        return Exec.exec("get Emoticon path", () -> uomRangeValuesService.getPathEmoticon(rangeDefault, amount));
    }

    @RequestMapping(value = "uom-range-values", method = RequestMethod.POST)
    @ResponseBody
    public boolean createUomRangeValues(@RequestBody UomRangeValues req){
        return Exec.exec("create uom-range-values", () -> this.uomRangeValuesService.createUomRangeValues(req) );
    }

    @RequestMapping(value = "uom-range-values", method = RequestMethod.PUT)
    @ResponseBody
    public boolean updateUomRangeValues(@RequestBody UomRangeValues req){
        return Exec.exec("update uom-range-values", () -> this.uomRangeValuesService.updateUomRangeValues(req) );
    }

    @RequestMapping(value = "uom-range-values/{uomRangeValuesId}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteUomRangeValues(@PathVariable(value = "uomRangeValuesId") String id){
        return Exec.exec("delete uom-range-values", () -> this.uomRangeValuesService.deleteUomRangeValues(id) );
    }
}
