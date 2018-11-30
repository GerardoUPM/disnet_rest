package edu.upm.midas.common.util;

import com.google.gson.Gson;
import edu.upm.midas.client_modules.authorization.token.component.JwtTokenUtil;
import edu.upm.midas.client_modules.authorization.token.service.TokenAuthorization;
import edu.upm.midas.constants.Constants;
import edu.upm.midas.model.response.analysis.DatabaseStatisticsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

/**
 * Created by gerardo on 10/05/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project ExtractionInformationDiseasesWikipedia
 * @className Validations
 * @see
 */
@Component
public class Common {

    @Autowired
    private TokenAuthorization tokenAuthorization;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private static final Logger logger = LoggerFactory.getLogger(Common.class);


    /**
     * @param token
     * @param start
     * @param end
     * @throws Exception
     */
    public void saveQueryRuntime(String token, String start, String end) throws Exception {
        try {
            //Aunque exista problema al insertar el runtime no hay problema con la ejecución de la consulta
            String queryId = jwtTokenUtil.getQueryIdJWTDecode(token);
            if (!isEmpty(queryId)) {
                HttpStatus response = tokenAuthorization.updateQueryRuntime(queryId, start, end);
                System.out.println(response);
            }
        }catch (Exception e){}
    }


    public boolean isEmpty(String string) {
        if (string == null) {
            return true;
        }
        else {
            if (string.trim().equalsIgnoreCase("")) {
                return true;
            }
            else {
                return false;
            }

        }
    }


    public String createForceSemanticTypesQuery(List<String> semanticTypes){
        String query = "";
        int count = 1;
        for (String semanticType: semanticTypes) {
            if(count == 1)
                query = " hst.semantic_type = '" + semanticType +"' ";
            else
                query = query + " OR hst.semantic_type = '" + semanticType + "' ";

            count++;
        }
        if (query.length() > 0){
            query = "AND ( " + query + ")";
        }
        return query;
    }


    public String createExcludeSemanticTypesQuery(List<String> semanticTypes){//System.out.println("HOLA");
        String query = "";
        int count = 1;
        for (String semanticType: semanticTypes) {
            if (count == 1)
                query = " hst.semantic_type != '" + semanticType +"' ";
            else
                query = query + " AND hst.semantic_type != '" + semanticType +"' ";
            count++;
        }
        if (query.length() > 0){
            query = "AND ( " + query + ")";
        }//System.out.println("HOLA" + query);
        return query;
    }


    public void writeStatisticsJSONFile(String jsonBody, String snapshot, String file_name) throws IOException {
        String fileName = snapshot + file_name + Constants.DOT_JSON;
        String path = Constants.STATISTICS_HISTORY_FOLDER + fileName;
        InputStream in = getClass().getResourceAsStream(path);
        //BufferedReader bL = new BufferedReader(new InputStreamReader(in));
        File file = new File(path);
        BufferedWriter bW;

        if (!file.exists()){
            bW = new BufferedWriter(new FileWriter(file));
            bW.write(jsonBody);
            bW.close();
        }
    }


    /**
     * @param snapshot
     * @return
     * @throws Exception
     */
    public DatabaseStatisticsResponse readJSONFile(String snapshot, String file_name) throws Exception {
        DatabaseStatisticsResponse response = null;
        System.out.println("Read JSON!... ");
        Gson gson = new Gson();
        String fileName = snapshot + file_name + Constants.DOT_JSON;
        String path = Constants.STATISTICS_HISTORY_FOLDER + fileName;
        System.out.println(path);
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            response = gson.fromJson(br, DatabaseStatisticsResponse.class);
//            gson = new GsonBuilder().setPrettyPrinting().create();
//            System.out.println(gson.toJson(response));
        }catch (Exception e){
            logger.error("Error to read or convert JSON {}", path, e);
//            System.out.println("Error to read or convert JSON!..." + e.getLocalizedMessage() + e.getMessage() + e.getCause());
        }
        return response;
    }


}
