package edu.upm.midas.scheduling;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.upm.midas.common.util.Common;
import edu.upm.midas.common.util.TimeProvider;
import edu.upm.midas.constants.Constants;
import edu.upm.midas.enums.ApiErrorEnum;
import edu.upm.midas.model.response.ApiResponseError;
import edu.upm.midas.model.response.analysis.DatabaseStatisticsResponse;
import edu.upm.midas.model.response.analysis.Source;
import edu.upm.midas.service.analysis.Descriptive;
import edu.upm.midas.service.error.ErrorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerardo on 20/11/2018.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className StatisticsScheduling
 * @see
 */
public class StatisticsScheduling {

    @Autowired
    private TimeProvider timeProvider;
    @Autowired
    private Descriptive descriptive;
    @Autowired
    private ErrorService errorService;
    @Autowired
    private Common common;


    private static final Logger logger = LoggerFactory.getLogger(StatisticsScheduling.class);


    public void createStatisticsReport(){
        List<ApiResponseError> errorsFound = new ArrayList<>();
        List<Source> sources = new ArrayList<>();
        DatabaseStatisticsResponse response = new DatabaseStatisticsResponse();
        String creationDate = timeProvider.getNowFormatyyyyMMdd();

        response.setAuthorized(true);
        response.setAuthorizationMessage(HttpStatus.ACCEPTED.toString());
        response.setToken(Constants.CLIENT_TOKEN);

        response.setSources(sources);
        response.setSourceCount(sources.size());
        //</editor-fold>
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()) {
            try {
                //Se registra el tiempo de ejecución de la consulta
                String start = timeProvider.getTimestampFormat();
                sources = descriptive.getDatabaseStatistics(errorsFound, response);
                String end = timeProvider.getTimestampFormat();
                if (sources.size() > 0) {
                    response.setSources(sources);
                    response.setSourceCount(sources.size());
                    response.setResponseCode(HttpStatus.OK.toString());
                    response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
//                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    //<editor-fold desc="WriteJSON">
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    common.writeStatisticsJSONFile(gson.toJson(response), creationDate, Constants.DESCRIPTIVE_STATISTICS_FILE_NAME);
                    System.out.println("JSON save successfully!");
                    //</editor-fold>
                }else{
                    response.setResponseCode(HttpStatus.OK.toString());
                    response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
//                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    errorService.insertApiErrorEnumGenericError(
                            errorsFound,
                            ApiErrorEnum.RESOURCES_NOT_FOUND,
                            "Source list empty.",
                            "Statistical information from sources is not charged or there are no data to analyze.",
                            true,
                            null);
                }
            }catch (Exception e){
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                errorService.insertInternatServerError(errorsFound, e, true);
            }
        }else {
            response.setAuthorized(false);
            response.setAuthorizationMessage(HttpStatus.UNAUTHORIZED.toString());

            response.setResponseCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.toString());
            response.setResponseMessage(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.getReasonPhrase());
            errorService.insertAuthorizationError(errorsFound, true);
        }
        response.setErrorsFound(errorsFound);
    }


    @Scheduled(cron = "0 0 4 5 * ?")
    public void createReportEveryFifthDayOfTheMonth() throws Exception {
        System.out.println("Scheduled task for the fifth of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " start.");
        createStatisticsReport();
        System.out.println("Scheduled task for the fifth of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " end.");
    }


    @Scheduled(cron = "0 0 4 20 * ?")
    public void createReportEveryTwentythDayOfTheMonth() throws Exception {
        System.out.println("Scheduled task for the fifth of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " start.");
        createStatisticsReport();
        System.out.println("Scheduled task for the fifth of each month at midnight " + timeProvider.getNowFormatyyyyMMdd() + " end.");
    }
}
