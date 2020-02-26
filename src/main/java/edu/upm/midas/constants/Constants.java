package edu.upm.midas.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gerardo on 27/3/17.
 * @project ExtractionInformationWikipedia
 * @version ${<VERSION>}
 * @author Gerardo Lagunes G.
 * @className Constants
 * @see
 */
@Component
public class Constants {

    public static final String HTTP_HEADER = "https://";
    public static final String VERSION_PROJECT = "1.0";

    public static final String LOCALHOST = "localhost";

    public final static String ERR_NO_PARAMETER = "No parameter was sent";
    public final static String ERR_EMPTY_PARAMETER = "Empty parameter";

    public final static String OK = "OK";
    public final static String RESPONSE_INVALID_SOURCES = "Invalid source list";
    public final static String RESPONSE_SEMANTIC_TYPES = "Invalid semantic type list";

    public static final String WIKIPEDIA_SOURCE = "wikipedia";
    public static final String PUBMED_SOURCE = "pubmed";

    public static final String EXPORT_FOLDER = "tmp/excel/";
    public static final String EXPORT_WIKIPEDIA_FOLDER = "tmp/excel/wikipedia/2018_12_20/";
    public static final String EXPORT_PUBMED_FOLDER = "tmp/excel/pubmed/2019_01_28/";
    public static final String WIKIPEDIA_VALIDATION_FOLDER = "tmp/excel/wikipedia/1.Validated/OK_DISNET_VALIDATION_(100)_v0.10/";
    public static final String PUBMED_VALIDATION_FOLDER = "tmp/excel/pubmed/1.Validated/OK_DISNET_VALIDATION_PUBMED_(100)_v0.3/";
    public static final String STATISTICS_HISTORY_FOLDER = "tmp/statistics/";
    public static final String DESCRIPTIVE_STATISTICS_FILE_NAME = "_descriptive_statistics";

    public final static String TOKEN = "token";
    public static final String CLIENT_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJncmFyZG9sYWdhckBob3RtYWlsLmNvbSIsImF1ZCI6IndlYiIsIm5hbWUiOiJHZXJhcmRvIExhZ3VuZXMiLCJ1c2VyIjp0cnVlLCJpYXQiOjE1MDk2MTQyNjh9.uVhDgfLrAgdnj02Hsbgfj9tkVlfni89i0hKVYW31eHApCHpheikK9ae1MhbzRhiyUcFGMKwtiyVgff5NCMY3PA";
    public final static String SOURCE = "source";
    public final static String VERSION = "version";
    public final static String DISEASE_NAME = "diseaseName";
    public final static String DISEASE_CODE = "diseaseCode";
    public final static String TYPE_CODE = "typeCode";
    public final static String FORCE_SEM_TYPES = "forceSemanticTypes";
    public final static String EXCLUDE_SEM_TYPES = "excludeSemanticTypes";
    public final static String LIMIT = "limit";

    public final static String TYPE_QUERY_NAME = "name";
    public final static String TYPE_QUERY_NAMES = "names";
    public final static String TYPE_QUERY_CODES = "codes";
    public final static String TYPE_QUERY_UNKNOWN = "unknown";


    public final static String METAMAP_API_REST_CODE = "SE007";
    public final static String TVP_API_REST_CODE = "SE006";
    public final static String DIS_ALBUM_API_REST_CODE = "SE010";

    public final static String MESSAGE_PARAM_NOT_USED = "Parameter not used.";

    public final static String AMPERSAND = "&";

    public static final String DOT_JSON = ".json";
    public static final String DOT_XML = ".xml";



    @Value("${my.header.param.token.name}")
    public String HEADER_PARAM_TOKEN_NAME;

}
