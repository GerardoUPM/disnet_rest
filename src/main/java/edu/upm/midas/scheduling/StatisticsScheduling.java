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
import org.springframework.stereotype.Service;

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
@Service
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


    /**
     *
     *
     * Explicación de las expresiones Cron:
     *
     * Dada la siguiente expresión: @Scheduled(cron = "0 9 23 ? * 5 ")
     * La tarea anterior se ejecutará a las 23 horas con 9 minutos y 0 segundos, todos los meses, los días 5 (viernes).

     Las expresiones cron tienen 6 valores obligatorios.

     Segundos. En nuestro ejemplo tiene el valor 0. Acepta valores del 0-59 y caracteres especiales como , - * /
     Minutos. En nuestro ejemplo tiene el valor 9. Acepta valores del 0-59 y caracteres especiales como , - * /
     Hora. En nuestro ejemplo tiene el valor 23. Acepta valores del 0-23 y caracteres especiales como , - * /
     Día del mes. En nuestro ejemplo tiene el caracter especial “?” el cual significa no definido ya que no deseamos que se ejecute un determinado día del mes, en su lugar deseamos que se ejecute un determinado día de la semana. Acepta valores del 1-31 y caracteres especiales como , - * ? /
     Mes. En nuestro ejemplo tiene el caracter especial “*” el cuál significa todos , es decir, deseamos se ejecute todos los meses. Acepta valores del 1-12 o abreviaturas JAN-DEC y caracteres especiales como , - * /
     Día de la semana. En nuestro ejemplo tiene el valor 5, es decir, deseamos se ejecute el quinto día (Viernes). Acepta valores del 1-7 o abreviaturas SUN-SAT y caracteres especiales como , - * ? /
     El día del mes y el día de la semana son excluyentes, es decir que podemos definir solo uno de los dos, no ámbos. En nuestro ejemplo queremos que se ejecute siempre un día de la semana por lo tanto en la posición de día del mes asignaremos un “?” para indicar que no está definido.

     El caracter especial “/” se usa para especificar incrementos. Por ejemplo en el campo de minutos, un valor como 0/1 indica que la tarea se ejecutará cada minuto, en el campo de segundos un valor como 0/15 indica una ejecución cada 15 segundos.
     Se ejecuta cada minuto de todos los dias sábados a media noche.
     @Scheduled(cron = "0 0/1 0 ? * 6 ")

     El caracter especial “,” se usa para especificar un conjunto de valores. Por ejemplo en el campo de día de la semana, un valor como “6,7” indica que la tarea se ejecutará todos los sábados y domingos.
     Se ejecuta cada 15 segundos los días sábados y domingos a media noche.
     @Scheduled(cron = "0/15 * 0 ? * 6,7 ")
     */
    @Scheduled(cron = "0 20 15 14 * ?")
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
