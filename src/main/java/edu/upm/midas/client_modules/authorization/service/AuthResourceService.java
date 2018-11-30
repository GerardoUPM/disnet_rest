package edu.upm.midas.client_modules.authorization.service;

import edu.upm.midas.client_modules.authorization.model.UpdateQueryRuntimeRequest;
import edu.upm.midas.client_modules.authorization.model.ValidationResponse;
import org.springframework.http.HttpStatus;

/**
 * Created by gerardo on 08/08/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project eidw
 * @className AuthResourceService
 * @see
 */
public interface AuthResourceService {

    ValidationResponse validationServiceByToken(String token);

    HttpStatus updateQueryRunTime(UpdateQueryRuntimeRequest request);

}
