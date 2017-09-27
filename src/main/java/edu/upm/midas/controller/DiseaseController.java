package edu.upm.midas.controller;

import edu.upm.midas.authorization.model.ValidationResponse;
import edu.upm.midas.authorization.token.service.TokenAuthorization;
import edu.upm.midas.constants.Constants;
import edu.upm.midas.data.relational.service.helperNative.DiseaseHelperNative;
import edu.upm.midas.model.response.diseases.SymptomsResponse;
import edu.upm.midas.common.util.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
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
    @Autowired
    private TokenAuthorization tokenAuthorization;
    @Autowired
    private Constants constants;


    @RequestMapping(path = { "/{disease}/findings" },//disease name
                    method = RequestMethod.GET,
                    params = {"source", "version"})
    public SymptomsResponse getFindings(@PathVariable(value = "disease") String disease,
                                        @RequestParam(value = "source") String source,
                                        @RequestParam(value = "version") String version,
                                        @RequestParam(value = "validated", required = false, defaultValue = "true") boolean validated,
                                        HttpServletRequest httpRequest,
                                        Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        String token = httpRequest.getHeader(constants.HEADER_PARAM_TOKEN_NAME);
        System.out.println("token en el header: " + token);
        ValidationResponse validationResponse = tokenAuthorization.validateService(token, httpRequest.getServletPath(), httpRequest.getServletPath(), device);

        SymptomsResponse symptomsResponse = new SymptomsResponse();
        symptomsResponse.setAuthorization( validationResponse.isAuthorized() );
        symptomsResponse.setAuthorizationMessage( validationResponse.getMessage() );
        symptomsResponse.setToken( token );
        //</editor-fold>
        //Si la autorización es exitosa se completa la respuesta
        if (validationResponse.isAuthorized()){
            Date dataVersion = timeProvider.getSdf().parse(version);
            System.out.println(String.format("DIS: " + disease + " SOURCE: " + source + " VERSION: " + dataVersion + " VAL: " + validated));
            return diseaseService.getFindings(source, dataVersion, disease, validated, symptomsResponse);
        }
        return symptomsResponse;
    }

}
