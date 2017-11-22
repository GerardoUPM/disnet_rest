package edu.upm.midas.authorization.token.service;

import edu.upm.midas.authorization.model.ValidationResponse;
import edu.upm.midas.authorization.service.AuthResourceService;
import edu.upm.midas.authorization.token.component.JwtTokenUtil;
import edu.upm.midas.model.response.ResponseFather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

/**
 * Created by gerardo on 26/09/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project tvp_rest
 * @className TokenAuthorization
 * @see
 */
@Service
public class TokenAuthorization {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthResourceService authResourceService;


    public ResponseFather validateService(String userToken, String request, String path, Device device){
        ResponseFather response = new ResponseFather();
        String token = jwtTokenUtil.generateToken( userToken, request, path, device );

        System.out.println( "Call Authorization API... " + token );
        ValidationResponse validationResponse = authResourceService.validationServiceByToken( token );
        response.setAuthorized( validationResponse.isAuthorized() );
        response.setAuthorizationMessage( validationResponse.getMessage() );
        response.setToken( userToken );

        return response;
    }


}
