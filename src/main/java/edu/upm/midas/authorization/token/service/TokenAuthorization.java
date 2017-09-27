package edu.upm.midas.authorization.token.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.upm.midas.authorization.model.ValidationResponse;
import edu.upm.midas.authorization.service.AuthResourceService;
import edu.upm.midas.authorization.token.component.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

import java.util.List;

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


    public ValidationResponse validateService(String userToken, String request, String path, Device device){
        String token = jwtTokenUtil.generateToken( userToken, request, path, device );
        System.out.println( "Call Authorization API... " + token );
        return authResourceService.validationServiceByToken( token );
    }


}
