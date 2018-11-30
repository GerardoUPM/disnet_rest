package edu.upm.midas.service.analysis;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.upm.midas.common.util.Common;
import edu.upm.midas.common.util.TimeProvider;
import edu.upm.midas.constants.Constants;
import edu.upm.midas.model.response.ApiResponseError;
import edu.upm.midas.model.response.analysis.DatabaseStatisticsResponse;
import edu.upm.midas.model.response.analysis.Snapshot;
import edu.upm.midas.model.response.analysis.Source;
import edu.upm.midas.service.error.ErrorService;
import edu.upm.midas.service.jpa.DiseaseService;
import edu.upm.midas.service.jpa.SourceService;
import edu.upm.midas.service.jpa.helperNative.SourceHelperNative;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by gerardo on 16/11/2018.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className DatabaseStatisticsResponse
 * @see
 */
@Service
public class Descriptive {
    @Autowired
    private SourceHelperNative sourceHelper;
    @Autowired
    private DiseaseService diseaseService;

    @Autowired
    private TimeProvider timeProvider;
    @Autowired
    private ErrorService errorService;
    @Autowired
    private Common common;

    private static final Logger logger = LoggerFactory.getLogger(SourceHelperNative.class);
    @Autowired
    ObjectMapper objectMapper;

    public List<Source> getDatabaseStatistics(List<ApiResponseError> errorsFound, DatabaseStatisticsResponse response) throws ParseException {
        List<Source> sources            = new ArrayList<>();
        List<String> existingSources    = sourceHelper.getSources(errorsFound);
        List<String> vocabularies       = diseaseService.getWikipediaMedicalVocabulariesNative();
        //            int numDiseaseSynonyms;
        response.setNumVocabularies(vocabularies.size());
        response.setVocabularies(vocabularies);

        for (String source: existingSources) {
            Source _source = new Source();
            List<Snapshot> snapshotList = new ArrayList<>();

            _source.setName(source);
            _source.setSnapshots(snapshotList);
            sources.add(_source);

            List<String> snapshots = sourceHelper.getVersions(errorsFound, source);
            for (String snapshot: snapshots) {
                Snapshot _snapshot = new Snapshot();
                int numTotDiseases = diseaseService.getExtractedDiseasesTotBySourceAndSnapshotNative(source, timeProvider.stringToDate(snapshot));
                int numDisease = diseaseService.getDiseasesNumberWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative(source, timeProvider.stringToDate(snapshot), true);
                int numTotMedicalTerms = diseaseService.getTotalValidatedMedicalTermsNumberBySourceAndSnapshotNative(source, timeProvider.stringToDate(snapshot));
                int numMedicalTerms = diseaseService.getValidatedMedicalTermsNumberBySourceAndSnapshotNative(source, timeProvider.stringToDate(snapshot), true);//Validated medical terms
                int numTotTexts = diseaseService.getTotalTextsBySourceAndSnapshotNative(source, timeProvider.stringToDate(snapshot));
                int numTexts = diseaseService.getTextsNumberWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative(source, timeProvider.stringToDate(snapshot), true);
                int numDiseaseWithCodes = diseaseService.getDiseasesNumberWithCodesAndWithALeastOneValidatedMedicalTermsBySourceAndSnapshotNative(source, timeProvider.stringToDate(snapshot), true);
                int numDiseaseCodes = diseaseService.getDiseasesCodesNumberBySourceAndSnapshotNative(source, timeProvider.stringToDate(snapshot));

                _snapshot.setDate(snapshot);
//                System.out.println(diseaseService.getExtractedDiseasesTotBySourceAndSnapshotNative(source, timeProvider.stringToDate(snapshot)));
                _snapshot.setNumTotDiseases(numTotDiseases);
                _snapshot.setNumDisease(numDisease);
                _snapshot.setNumTotMedicalTerms(numTotMedicalTerms);
                _snapshot.setNumMedicalTerms(numMedicalTerms);
                _snapshot.setNumTotTexts(numTotTexts);
                _snapshot.setNumTexts(numTexts);
                _snapshot.setNumDiseaseWithCodes(numDiseaseWithCodes);
                _snapshot.setNumDiseaseCodes(numDiseaseCodes);

                snapshotList.add(_snapshot);
            }
            _source.setSnapshotCount(snapshotList.size());
        }

        return sources;

    }

    public DatabaseStatisticsResponse getLastDatabaseStatistics() throws Exception {
        DatabaseStatisticsResponse response = new DatabaseStatisticsResponse();
        File folder = new File(Constants.STATISTICS_HISTORY_FOLDER);
        File[] listOfFiles = folder.listFiles();
        List<String> dates = new ArrayList<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String[] file = listOfFiles[i].getName().split("_");
                String date = file[0];
                dates.add(date);
            } else if (listOfFiles[i].isDirectory()) {
//                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
//        System.out.println("Tamaño: "+dates.size());
        //Verifica que haya elementos a ordenar y obtener
        if (dates.size() > 0) {
            //Ordena la lista
            dates.sort(String::compareTo);
            //Obtiene el último elemento de la lista, la fecha más actual
            String lastDate = dates.get(dates.size() - 1);
//        System.out.println("This is the last date: " + lastDate);

            response = common.readJSONFile(lastDate, Constants.DESCRIPTIVE_STATISTICS_FILE_NAME);
        }else{
            response.setResponseCode(HttpStatus.NOT_FOUND.toString());
            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
        }

        return response;
    }
}
