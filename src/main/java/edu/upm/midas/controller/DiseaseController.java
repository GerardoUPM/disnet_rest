package edu.upm.midas.controller;

import edu.upm.midas.data.relational.service.helperNative.DiseaseHelperNative;
import edu.upm.midas.data.relational.service.model.response.diseases.SymptomsResponse;
import edu.upm.midas.common.util.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by gerardo on 28/08/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className VersionController
 * @see
 *
 */
@RestController
@RequestMapping("/api/diseases")
public class DiseaseController {

    @Autowired
    private TimeProvider timeProvider;
    @Autowired
    private DiseaseHelperNative diseaseService;


    @RequestMapping(path = { "/{disease}/findings" },//disease name
                    method = RequestMethod.GET,
                    params = {"source", "version"})
    public SymptomsResponse getFindings(@PathVariable(value = "disease") String disease,
                                        @RequestParam(value = "source") String source,
                                        @RequestParam(value = "version") String version,
                                        @RequestParam(value = "validated", required = false, defaultValue = "true") boolean validated,
                                        HttpServletRequest httpRequest) throws Exception {
        Date dataVersion = timeProvider.getSdf().parse(version);
        System.out.println(String.format("DIS: " + disease + " SOURCE: " + source + " VERSION: " + dataVersion + " VAL: " + validated));
        return diseaseService.getFindings(source, dataVersion, disease, validated);
    }

}
