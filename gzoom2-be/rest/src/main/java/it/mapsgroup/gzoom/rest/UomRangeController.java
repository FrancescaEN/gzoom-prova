package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.UomRange;
import it.mapsgroup.gzoom.service.UomRangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leonardo Minaudo.
 */
@RestController
@RequestMapping(value = "uom-range", produces = { MediaType.APPLICATION_JSON_VALUE })
public class UomRangeController {
    private final UomRangeService uomRangeService;

    @Autowired
    public UomRangeController(UomRangeService uomRangeService) {
        this.uomRangeService = uomRangeService;
    }

    @GetMapping
    @ResponseBody
    public Result<UomRange> getUomRangeList(){
        return Exec.exec("get uom-range-list", this.uomRangeService::getUomRangeList);
    }

    @PostMapping
    @ResponseBody
    public boolean createUomRange(@RequestBody UomRange req){
        return Exec.exec("create uom-range", () -> this.uomRangeService.createUomRange(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateUomRange(@RequestBody UomRange req){
        return Exec.exec("update uom-range", () -> this.uomRangeService.updateUomRange(req) );
    }

    @DeleteMapping(value = "/{uomRangeId}")
    @ResponseBody
    public boolean deleteUomRange(@PathVariable(value = "uomRangeId") String id){
        return Exec.exec("delete uom-range", () -> this.uomRangeService.deleteUomRange(id) );
    }
}
