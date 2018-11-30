package edu.upm.midas.client_modules.authorization.client.fallback;

import edu.upm.midas.client_modules.authorization.client.AuthClient;
import edu.upm.midas.client_modules.authorization.model.UpdateQueryRuntimeRequest;
import edu.upm.midas.client_modules.authorization.model.ValidationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Created by gerardo on 17/08/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project eidw
 * @className TvpClientFallback
 * @see
 */
@Component
public class AuthClientFallback implements AuthClient {

    @Override
    public ValidationResponse validationServiceByToken(String tokenService) {
        return new ValidationResponse();
    }

    @Override
    public HttpStatus updateQueryRunTime(UpdateQueryRuntimeRequest request) {
        return HttpStatus.NOT_FOUND;
    }


}

