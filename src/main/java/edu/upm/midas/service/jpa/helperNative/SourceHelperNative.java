package edu.upm.midas.service.jpa.helperNative;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import edu.upm.midas.constants.Constants;
import edu.upm.midas.service.jpa.SourceService;
import edu.upm.midas.common.util.Common;
import edu.upm.midas.enums.ApiErrorEnum;
import edu.upm.midas.model.response.ApiResponseError;
import edu.upm.midas.model.response.Parameter;
import edu.upm.midas.model.response.ResponseFather;
import edu.upm.midas.service.error.ErrorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 12/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className SourceHelper
 * @see
 */
@Service
public class SourceHelperNative {

    @Autowired
    private SourceService sourceService;
    @Autowired
    private UrlHelperNative urlHelperNative;
    @Autowired
    private ErrorService errorService;
    @Autowired
    private Common common;

    private static final Logger logger = LoggerFactory.getLogger(SourceHelperNative.class);
    @Autowired
    ObjectMapper objectMapper;


    /**
     * @return
     */
    public List<String> getSources(List<ApiResponseError> apiResponseErrors){
        List<String> sources = new ArrayList<>();
        try {
            List<Object[]> sourceAllNativeList = sourceService.findAllNative();
            if (sourceAllNativeList != null) {
                for (Object[] source : sourceAllNativeList) {
                    sources.add((String) source[0]);
                }
            }
        }catch (Exception e){
            //Se agrega el error en la lista principal de la respuesta
            errorService.insertApiErrorEnumGenericError(
                    apiResponseErrors,
                    ApiErrorEnum.INTERNAL_SERVER_ERROR,
                    Throwables.getRootCause(e).getClass().getName(),
                    e.getMessage(),
                    true,
                    null);
        }
        return sources;
    }


    /**
     * @param source
     * @return
     */
    public List<String> getVersions(List<ApiResponseError> apiResponseErrors, String source) {
        List<String> versions = new ArrayList<>();
        try {
            List<Date> versionAllNativeList = sourceService.findAllVersionsBySourceNative(source);
            if (versionAllNativeList != null) {
                for (Date version : versionAllNativeList) {
                    versions.add(version.toString());
                }
            }
        }catch (Exception e){
            //Se agrega el error en la lista principal de la respuesta
            errorService.insertApiErrorEnumGenericError(
                    apiResponseErrors,
                    ApiErrorEnum.INTERNAL_SERVER_ERROR,
                    Throwables.getRootCause(e).getClass().getName(),
                    e.getMessage(),
                    true,
                    new Parameter(Constants.DISEASE_NAME, false, false, source, null));
        }
        return versions;
    }


    public void validateSource(ResponseFather responseFather, String source) throws Exception{

    }

    public void validateVersion(ResponseFather responseFather, String version) throws Exception{

    }


}
