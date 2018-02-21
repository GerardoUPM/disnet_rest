package edu.upm.midas.data.relational.service.helperNative;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import edu.upm.midas.common.util.TimeProvider;
import edu.upm.midas.constants.Constants;
import edu.upm.midas.data.relational.entities.edsssdb.SemanticType;
import edu.upm.midas.data.relational.service.*;
import edu.upm.midas.enums.ApiErrorEnum;
import edu.upm.midas.model.*;
import edu.upm.midas.common.util.Common;
import edu.upm.midas.common.util.UniqueId;
import edu.upm.midas.model.response.*;
import edu.upm.midas.model.response.validations.CodeAndTypeCodeValidation;
import edu.upm.midas.model.response.validations.SemanticTypesValidation;
import edu.upm.midas.model.response.validations.TypeSearchValidation;
import edu.upm.midas.model.response.validations.Validation;
import edu.upm.midas.service.error.ErrorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by gerardo on 13/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className DiseaseHelper
 * @see
 */
@Service
public class DiseaseHelperNative {

    @Autowired
    private DiseaseService diseaseService;
    @Autowired
    private SymptomService symptomService;
    @Autowired
    private SourceService sourceService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private SemanticTypeService semanticTypeService;
    @Autowired
    private UniqueId uniqueId;
    @Autowired
    private DocumentHelperNative documentHelperNative;
    @Autowired
    private ErrorService errorService;
    @Autowired
    private Common common;
    @Autowired
    private TimeProvider timeProvider;

    private static final Logger logger = LoggerFactory.getLogger(DiseaseHelperNative.class);
    @Autowired
    ObjectMapper objectMapper;




    /**
     * @param disnetConcepts
     * @return
     */
    public String getDiseaseUrlFromDisnetConceptList(List<DisnetConcept> disnetConcepts){
        String url = "";
        for (DisnetConcept disnetConcept: disnetConcepts) {
            url = disnetConcept.getUrl();
            break;
        }
        return url;
    }


    /**
     * @param sourceName
     * @param version
     * @return
     */
    public Integer getNumberDiseases(String sourceName, Date version){
        return diseaseService.numberDiseasesBySourceAndVersion(sourceName, version);
    }

    //Antes se usaba
    public List<Disease> diseaseList(String sourceName, Date version){
        return diseaseService.findAllBySourceAndVersion(sourceName, version);
    }


    /** YA NO SE USA
     * @param sourceName
     * @param version
     * @param diseaseName
     * @param isValidated
     * @return
     */
    public List<Disease> getDiseasesAndTheirDisnetConcepts(String sourceName, Date version,
                                                           String diseaseName,
                                                           String code, String typeCode,
                                                           boolean isValidated,
                                                           TypeSearchValidation validation, boolean matchExactName){
        List<Disease> diseaseList = null;
        if (validation.getTypeSearch().equals(Constants.TYPE_QUERY_NAME)){
            if (validation.getTypeSemanticTypesSearch().equals(Constants.FORCE_SEM_TYPES)){
                diseaseList = diseaseService.findSymptomsBySourceAndVersionAndDiseaseNameAndValidatedAndForceSemanticTypesNative(sourceName, version, diseaseName, isValidated, validation.getForceSemanticTypes());
            } else if(validation.getTypeSemanticTypesSearch().equals(Constants.EXCLUDE_SEM_TYPES)){
                diseaseList = diseaseService.findSymptomsBySourceAndVersionAndDiseaseNameAndValidatedAndExcludeSemanticTypesNative(sourceName, version, diseaseName, isValidated, validation.getExcludeSemanticTypes());
            }else {
                diseaseList = diseaseService.findSymptomsBySourceAndVersionAndDiseaseNameAndIsValidated(sourceName, version, diseaseName, isValidated);
            }
        } else if (validation.getTypeSearch().equals(Constants.TYPE_QUERY_CODES)){
            if (validation.getTypeSemanticTypesSearch().equals(Constants.FORCE_SEM_TYPES)){
                diseaseList = diseaseService.findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndValidatedAndForceSemanticTypesNative(sourceName, version, code, typeCode, isValidated, validation.getForceSemanticTypes());
            } else if(validation.getTypeSemanticTypesSearch().equals(Constants.EXCLUDE_SEM_TYPES)){
                diseaseList = diseaseService.findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndValidatedAndExcludeSemanticTypesNative(sourceName, version, code, typeCode, isValidated, validation.getExcludeSemanticTypes());
            }else {
                diseaseList = diseaseService.findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndIsValidatedNative(sourceName, version, code, typeCode, isValidated);
            }
        }

//        if (disnetConcepts != null) {disnetConcepts = removeRepetedDisnetConcepts(disnetConcepts);}
//        for (DisnetConcept finding: disnetConcepts) {System.out.println(finding.toString());}
        return diseaseList;
    }


    public List<Disease> getDiseasesAndTheirDisnetConcepts_2(List<ApiResponseError> apiResponseErrors,
                                                             String sourceName, Date version,
                                                             String diseaseName,
                                                             String code, String typeCode,
                                                             boolean isValidated,
                                                             TypeSearchValidation validation,
                                                             boolean matchExactName,
                                                             boolean detectionInformation) {
        List<Disease> diseaseList = new ArrayList<>();
        try {
            //Primero se busca la lista de las enfermedades que coincidan con los parametros
            //después por cada enfermedad encontrada se buscarán sus semantic types, si los tiene
            List<Disease> diseases = null;
            //Si la busqueda es por nombre de enfermedad
            if (validation.getTypeSearch().equals(Constants.TYPE_QUERY_NAME)){
                //Si es por nombre, se tiene la opción de buscar por: 1) nombre exacto, o 2) nombre parecido
                //Si matchExactName es TRUE se hace uso de la consulta que busca por nombre de enfermedad exacto
                if (matchExactName) diseases = diseaseService.findBySourceAndVersionAndMatchExactNameTrueNative(sourceName, version, diseaseName);
                else diseases = diseaseService.findBySourceAndVersionAndMatchExactNameFalseNative(sourceName, version, diseaseName);
            } else //Si la busqueda es por código de la enfermedad
                if (validation.getTypeSearch().equals(Constants.TYPE_QUERY_CODES)){
                    diseases = diseaseService.findBySourceAndVersionAndCodeAndTypeCodeNative(sourceName, version, code, typeCode);
            }
            //Con la lista de las enfermedades cargada, se buscan sus conceptos, si los tiene
            if (diseases != null) {
                diseaseList = getDisnetConcepts(apiResponseErrors, validation, sourceName, version, isValidated, detectionInformation, diseases);
            }
        }catch (Exception e){
            //Se agrega el error en la lista principal de la respuesta
            errorService.insertApiErrorEnumGenericError(
                    apiResponseErrors,
                    ApiErrorEnum.INTERNAL_SERVER_ERROR,
                    Throwables.getRootCause(e).getClass().getName(),
                    e.getMessage() /*+ e.getLocalizedMessage() + e.getClass()*/,
                    true,
                    null);
        }
        return diseaseList;
    }


    public List<Disease> getDiseasesWithTheirCodes(List<ApiResponseError> apiResponseErrors, String sourceName, Date version, String diseaseName, boolean matchExactName){
        //return diseaseService.findCodesBySourceAndVersionAndDiseaseNameNative(sourceName, version, diseaseName, 0);
        List<Disease> diseaseList = new ArrayList<>();
        try {
            List<Disease> diseases;
            //Si matchExactName es TRUE se hace uso de la consulta que busca por nombre de enfermedad exacto
            if (matchExactName) diseases = diseaseService.findBySourceAndVersionAndMatchExactNameTrueNative(sourceName, version, diseaseName);
            else diseases = diseaseService.findBySourceAndVersionAndMatchExactNameFalseNative(sourceName, version, diseaseName);
            //Con la lista de las enfermedades cargada, se buscan sus códigos, si los tiene
            if (diseases != null) {
                diseaseList = findCodes(apiResponseErrors, sourceName, version, diseases);
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
        return diseaseList;
    }

    public List<Disease> getAllDiseaseWithTheirCodes(List<ApiResponseError> apiResponseErrors, String sourceName, Date version, boolean isValidated){
        List<Disease> diseaseList = new ArrayList<>();
        try {
            List<Disease> diseases = diseaseService.findAllBySourceAndVersion(sourceName, version);
            if (diseases != null) {
                diseaseList = findCodes(apiResponseErrors, sourceName, version, diseases);
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
        return diseaseList;
        //return diseaseService.findAllWithUrlAndSymptomsCountBySourceAndVersionAndIsValidated(sourceName, version, isValidated);
    }


    public List<Disease> getDiseasesWithMoreOrFewerDisnetConcepts(List<ApiResponseError> apiResponseErrors, String sourceName, Date version, boolean isValidated, int limit, boolean moreDisnetConcepts, TypeSearchValidation validation, boolean detectionInformation){
        List<Disease> diseaseList = new ArrayList<>();
        try {
            List<Disease> diseases = diseaseService.withMoreOrFewerSymptomsBySourceAndVersionAndIsValidated(sourceName, version, isValidated, limit, moreDisnetConcepts);
            //Con la lista de las enfermedades cargada, se buscan sus códigos, si los tiene
            if (diseases != null) {
                diseaseList = getDisnetConcepts(apiResponseErrors, validation, sourceName, version, isValidated, detectionInformation, diseases);
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
        //REGRESA LA MISMA LISTA PERO CON INFORMACIÓN DE SUS SINTOMAS... QUE CON LA PRIMER CONSULTA NO SE CONSIGUE
        //List<DiseaseDisnetConcepts> diseaseList = getDiseaseWithSymptomsList(sourceName, version, isValidated, limit, diseases);
        return diseaseList;
    }

    public List<Disease> excelExport(String sourceName, Date version, int symtomsCount){
        List<Disease> diseaseList = new ArrayList<>();
        List<Disease> diseases = diseaseService.findAllBySourceAndVersionAndSymptomsCountNative(sourceName, version, symtomsCount);
        if (diseases != null){
            diseaseList = diseases;
            for (Disease dis: diseases) {
                if (dis.getDocumentList() != null) {
                    for (Document doc : dis.getDocumentList()) {
                        List<DisnetConcept> disnetConcepts = diseaseService.findTermsBySourceAndVersionAndDocumentAndDiseaseIdNative(sourceName, version, doc.getDocumentId(), dis.getDiseaseId());
                        if (disnetConcepts != null) {
                            dis.setDisnetConceptList(disnetConcepts);
                            dis.setDisnetConceptsCount(disnetConcepts.size());
                            for (DisnetConcept disnetConcept : disnetConcepts) {
                                List<Text> texts = diseaseService.findTextsBySourceAndVersionAndDocumentAndDiseaseIdAndCuiNative(sourceName, version, doc.getDocumentId(), dis.getDiseaseId(), disnetConcept.getCui());
                                if (texts != null) {
                                    disnetConcept.setTexts(texts);
                                    disnetConcept.setTextsCount(texts.size());
                                }
                            }
                        }//disnetConcepts!=null
                        List<Text> texts_ = diseaseService.findTextsBySourceAndVersionAndDocumentAndDiseaseIdNative(sourceName, version, doc.getDocumentId(), dis.getDiseaseId());
                        if (texts_ != null){
                            doc.setTextList(texts_);
                            doc.setTextsCount(texts_.size());
                        }
                    }//documents of disease
                }//documents!=null
            }//diseases
        }//disease!=null
        return diseaseList;
    }


    public List<DiseaseDisnetConcepts> getDiseasesWithFewerFindings(String sourceName, Date version, boolean isValidated, int limit){
        List<DiseaseDisnetConcepts> diseases = diseaseService.withFewerSymptomsBySourceAndVersionAndIsValidated(sourceName, version, isValidated, limit);
        //REGRESA LA MISMA LISTA PERO CON INFORMACIÓN DE SUS SINTOMAS... QUE CON LA PRIMER CONSULTA NO SE CONSIGUE
        List<DiseaseDisnetConcepts> diseaseList = getDiseaseWithSymptomsList(sourceName, version, isValidated, limit, diseases);
        return diseaseList;
    }


    public List<Disease> findCodes(List<ApiResponseError> apiResponseErrors, String sourceName, Date version, List<Disease> diseases){
        try {
            for (Disease disease : diseases) {
                List<Code> codes = diseaseService.findCodesBySourceAndVersionAndDiseaseIdNative(sourceName, version, disease.getDiseaseId());
                if (codes != null)
                    disease.setCodes(codes);
                else disease.setCodes(new ArrayList<>());
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
        return diseases;
    }


    public List<DiseaseDisnetConcepts> getDiseaseWithSymptomsList(String sourceName, Date version, boolean isValidated, int limit, List<DiseaseDisnetConcepts> diseases){
        List<DiseaseDisnetConcepts> diseaseList = null;
        if (diseases != null) {
            diseaseList = new ArrayList<>();
            for (DiseaseDisnetConcepts diseaseDisnetConcepts : diseases) {
                //SE OBTIENEN LOS SINTOMAS POR CADA ENFERMEDAD ENCONTRADA
                List<DisnetConcept> disnetConcepts = diseaseService.findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidated(sourceName, version, diseaseDisnetConcepts.getDiseaseId(), isValidated);
                if (disnetConcepts.size() > 0) {
                    DiseaseDisnetConcepts disease = new DiseaseDisnetConcepts();
                    disease.setDisnetConceptList(disnetConcepts);
                    //disease.setDiseaseId(diseaseDisnetConcepts.getDiseaseId());
                    disease.setName(diseaseDisnetConcepts.getName());
                    disease.setDisnetConceptCount(diseaseDisnetConcepts.getDisnetConceptCount());
                    diseaseList.add(disease);
                }
            }
        }
        return diseaseList;
    }


    public List<Disease> getDisnetConcepts(List<ApiResponseError> apiResponseErrors,
                                           TypeSearchValidation validation,
                                           String sourceName, Date version,
                                           boolean isValidated,
                                           boolean detectionInformation,
                                           List<Disease> diseases){
        if (diseases != null) {
            try {
                for (Disease disease : diseases) {
                    //SE OBTIENEN LOS SINTOMAS POR CADA ENFERMEDAD ENCONTRADA
                    List<DisnetConcept> disnetConcepts;
                    if (validation.getTypeSemanticTypesSearch().equals(Constants.FORCE_SEM_TYPES)){
                        disnetConcepts = diseaseService.findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidatedAndForceOrExludeSemanticTypes(sourceName, version, disease.getDiseaseId(), isValidated, true, validation.getForceSemanticTypes());
                    }else if(validation.getTypeSemanticTypesSearch().equals(Constants.EXCLUDE_SEM_TYPES)){
                        disnetConcepts = diseaseService.findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidatedAndForceOrExludeSemanticTypes(sourceName, version, disease.getDiseaseId(), isValidated, false, validation.getExcludeSemanticTypes());
                    }else{
                        disnetConcepts = diseaseService.findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidated(sourceName, version, disease.getDiseaseId(), isValidated);
                    }

                    //Buscará por cada concepto su información de detección, es decir y de momento, el número de veces que
                    // se encontró el concepto en todos los textos del artículo relacionado con una enfermedad
                    if (detectionInformation){
                        if (disnetConcepts.size()>0) {
                            try {
                                String documentId = diseaseService.findDocumentIdBySourceAndVersionAndDiseaseIdNative(sourceName, version, disease.getDiseaseId());
                                for (DisnetConcept disnetConcept : disnetConcepts) {
                                    DetectionInformation detectInfo = diseaseService.findDetectionInformationBySourceAndVersionAndDocumentIdAndDiseaseIdAndCuiAndValidatedToDisnetConceptNative(sourceName, version, documentId, disease.getDiseaseId(), disnetConcept.getCui(), isValidated);
                                    disnetConcept.setDetectionInformation(detectInfo);
                                }
                            }catch (Exception e){
                                //Se agrega el error en la lista principal de la respuesta
                                errorService.insertApiErrorEnumGenericError(
                                        apiResponseErrors,
                                        ApiErrorEnum.INTERNAL_SERVER_ERROR,
                                        Throwables.getRootCause(e).getClass().getName(),
                                        "Data could not be obtained from the detection of information: " + e.getMessage(),
                                        true,
                                        null);
                            }
                        }
                    }
                    //Agregar validación para obtener información la busqueda del concepto "detectionInformation"

                    disease.setDisnetConceptList(disnetConcepts);
                    disease.setDisnetConceptsCount(disnetConcepts.size());
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
        }
        return diseases;
    }


    public List<DisnetConcept> getMostOrLessCommonDisnetConcepts(List<ApiResponseError> apiResponseErrors,
                                                                 String sourceName, Date version,
                                                                 boolean isValidated,
                                                                 int limit,
                                                                 boolean mostSymptoms, TypeSearchValidation validation){
        List<DisnetConcept> disnetConceptList = new ArrayList<>();
        try {
            List<DisnetConcept> disnetConcepts = null;
            if (validation.getTypeSemanticTypesSearch().equals(Constants.FORCE_SEM_TYPES)){
                disnetConcepts = symptomService.mostOrLessCommonBySourceAndVersionAndIsValidatedAndForceOrExludeSemanticTypes(sourceName, version, isValidated, limit, mostSymptoms, true, validation.getForceSemanticTypes());
            }else if(validation.getTypeSemanticTypesSearch().equals(Constants.EXCLUDE_SEM_TYPES)){
                disnetConcepts = symptomService.mostOrLessCommonBySourceAndVersionAndIsValidatedAndForceOrExludeSemanticTypes(sourceName, version, isValidated, limit, mostSymptoms, false, validation.getExcludeSemanticTypes());
            }else{
                disnetConcepts = symptomService.mostOrLessCommonBySourceAndVersionAndIsValidatedAndForceOrExludeSemanticTypes(sourceName, version, isValidated, limit, mostSymptoms, false, new ArrayList<>());
            }
            if (disnetConcepts != null){
                disnetConceptList = disnetConcepts;
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
        return disnetConceptList;
    }


    public List<DisnetConcept> getLessCommonSymptoms(String sourceName, Date version, boolean isValidated, int limit){
        return symptomService.lessCommonBySourceAndVersionAndValidated(sourceName, version, isValidated, limit);
    }


    public List<Configuration> getConfigurationList(List<ApiResponseError> apiResponseErrors,
                                                    String sourceName, Date version){
        List<Configuration> configurationList = new ArrayList<>();
        try {
            List<Configuration> configurations = sourceService.findSourceAndVersionConfigurationBySourceAndVersion(sourceName, version);
            if (configurations != null) {
                configurationList = configurations;
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
        return configurationList;
    }


    public TypeSearchValidation sourceAndVersionAndDiseaseNameValidation(List<ApiResponseError> apiResponseErrors, List<Parameter> parameters, String sourceName, Date version, String diseaseName, boolean matchExactName) throws Exception {
        TypeSearchValidation validation = new TypeSearchValidation();

        TypeSearchValidation svVal = sourceAndVersionValidation(apiResponseErrors, parameters, sourceName, version);

        if (!svVal.isErrors()) {
            Validation diseaseNameVal = validateDiseaseName(apiResponseErrors, parameters, sourceName, version, diseaseName, matchExactName);
            if (diseaseNameVal.isFound())
                validation.setErrors(false);
            else validation.setErrors(true);
        } else validation.setErrors(true);
        return validation;
    }


    public TypeSearchValidation sourceAndVersionValidation(List<ApiResponseError> apiResponseErrors, List<Parameter> parameters, String sourceName, Date version){
        TypeSearchValidation validation = new TypeSearchValidation();

        Validation sourceValidation = sourceValidation(apiResponseErrors, parameters, sourceName);

        if (sourceValidation.isFound()) {
            Validation versionValidation = versionValidation(apiResponseErrors, parameters, sourceName, version);
            if (versionValidation.isFound())
                validation.setErrors(false);
            else validation.setErrors(true);
        }else validation.setErrors(true);
        return validation;
    }


    public TypeSearchValidation sourceAndVersionAndSemanticTypesValidation(List<ApiResponseError> apiResponseErrors,
                                                                           List<Parameter> parameters,
                                                                           String sourceName, Date version,
                                                                           String excludeSemanticTypes,
                                                                           String forceSemanticTypes){
        TypeSearchValidation validation = new TypeSearchValidation();

        Validation sourceValidation = sourceValidation(apiResponseErrors, parameters, sourceName);

        if (sourceValidation.isFound()) {
            Validation versionValidation = versionValidation(apiResponseErrors, parameters, sourceName, version);
            if (versionValidation.isFound()) {
                validation.setErrors(false);
                semanticTypesValidationProcedure(apiResponseErrors, parameters, excludeSemanticTypes, forceSemanticTypes, validation);
            }else validation.setErrors(true);
        }else validation.setErrors(true);
        return validation;
    }


    public void semanticTypesValidationProcedure(List<ApiResponseError> apiResponseErrors,
                                                 List<Parameter> parameters,
                                                 String excludeSemanticTypes,
                                                 String forceSemanticTypes,
                                                 TypeSearchValidation validation){
        SemanticTypesValidation exludeSemTypesValidation = excludeSemanticTypesValidation(apiResponseErrors, parameters, excludeSemanticTypes);
        SemanticTypesValidation forceSemTypesValidation = forceSemanticTypesValidation(apiResponseErrors, parameters, forceSemanticTypes);

        if (!exludeSemTypesValidation.isEmpty() && !forceSemTypesValidation.isEmpty()){
            //ERROR
            errorService.insertApiErrorEnumGenericErrorWithParameters(
                    apiResponseErrors,
                    ApiErrorEnum.INVALID_PARAMETERS,
                    "Disease search",
                    "Exclusive parameters.  Only one of them can be defined.",
                    true,
                    new ArrayList<Parameter>() {{
                        add(new Parameter(Constants.EXCLUDE_SEM_TYPES, false, false, excludeSemanticTypes, null));
                        add(new Parameter(Constants.FORCE_SEM_TYPES, false, false, forceSemanticTypes, null));
                    }});
            validation.setErrors(true);
        } else if(  (!exludeSemTypesValidation.isEmpty() && forceSemTypesValidation.isEmpty()) ||
                (exludeSemTypesValidation.isEmpty() && !forceSemTypesValidation.isEmpty())){//OK
            System.out.println("OK");
            if (!exludeSemTypesValidation.isEmpty() && forceSemTypesValidation.isEmpty()){
                validation.setTypeSemanticTypesSearch(Constants.EXCLUDE_SEM_TYPES);
                validation.setExcludeSemanticTypes(exludeSemTypesValidation.getValidSemanticTypes());
                validation.setErrors(false);
            }
            if (exludeSemTypesValidation.isEmpty() && !forceSemTypesValidation.isEmpty()){
                validation.setTypeSemanticTypesSearch(Constants.FORCE_SEM_TYPES);
                validation.setForceSemanticTypes(forceSemTypesValidation.getValidSemanticTypes());
                validation.setErrors(false);
            }
        }else{}
    }


    public TypeSearchValidation validateDiseaseSearchingParameters(List<ApiResponseError> apiResponseErrors,
                                                                   List<Parameter> parameters,
                                                                   String sourceName, Date version,
                                                                   String diseaseName,
                                                                   String diseaseCode, String typeCode,
                                                                   String excludeSemanticTypes,
                                                                   String forceSemanticTypes,
                                                                   boolean matchExactName) throws Exception {
        TypeSearchValidation validation = new TypeSearchValidation();
        System.out.println(diseaseCode +"|"+typeCode);
        boolean diseaseNameEmpty = common.isEmpty(diseaseName);
        boolean diseaseCodeEmpty = common.isEmpty(diseaseCode);
        boolean typeCodeEmpty = common.isEmpty(typeCode);

        Validation sourceValidation = sourceValidation(apiResponseErrors, parameters, sourceName);

        if (sourceValidation.isFound()) {
            Validation versionValidation = versionValidation(apiResponseErrors, parameters, sourceName, version);
            if (versionValidation.isFound()) {
                if (diseaseNameEmpty && diseaseCodeEmpty && typeCodeEmpty) {
                    //ERROR
                    //System.out.println("1");
                    //Es necesario tener al menos un parametro de busqueda
                    errorService.insertApiErrorEnumGenericErrorWithParameters(
                            apiResponseErrors,
                            ApiErrorEnum.INVALID_PARAMETERS,
                            "Disease search",
                            "A search parameter must be selected. By name or by disease code.",
                            true,
                            new ArrayList<Parameter>() {{
                                add(new Parameter(Constants.DISEASE_NAME, true, false, diseaseCode, null));
                                add(new Parameter(Constants.DISEASE_CODE, true, false, diseaseCode, null));
                                add(new Parameter(Constants.TYPE_CODE, true, false, typeCode, null));
                            }});
                    validation.setErrors(true);
                } else if (!diseaseNameEmpty && !diseaseCodeEmpty && !typeCodeEmpty) {
                    //Solo se debe seleccionar un parametro de busqueda, no los dos
                    //System.out.println("4");
                    errorService.insertApiErrorEnumGenericErrorWithParameters(
                            apiResponseErrors,
                            ApiErrorEnum.INVALID_PARAMETERS,
                            "Disease search",
                            "It is necessary to select only one search parameter, not both.",
                            true,
                            new ArrayList<Parameter>() {{
                                add(new Parameter(Constants.DISEASE_NAME, true, false, diseaseName, null));
                                add(new Parameter(Constants.DISEASE_CODE, false, false, diseaseCode, null));
                                add(new Parameter(Constants.TYPE_CODE, false, false, typeCode, null));
                            }});
                    validation.setErrors(true);
                } else if (diseaseNameEmpty && (diseaseCodeEmpty || typeCodeEmpty)) {
                    //ERROR
                    //System.out.println("2");
                    //Si el nombre de enfermedad esta vacío, no se debe dejar vacío, el código ni el tipo de código
                    errorService.insertApiErrorEnumGenericErrorWithParameters(
                            apiResponseErrors,
                            ApiErrorEnum.INVALID_PARAMETERS,
                            "Disease search",
                            "Both disease code and type of code are pair and neither should be empty.",
                            true,
                            new ArrayList<Parameter>() {{
                                add(new Parameter(Constants.DISEASE_CODE, false, false, diseaseCode, null));
                                add(new Parameter(Constants.TYPE_CODE, false, false, typeCode, null));
                            }});
                    validation.setErrors(true);
                } else if (!diseaseNameEmpty && ((diseaseCodeEmpty && !typeCodeEmpty) || (!diseaseCodeEmpty && typeCodeEmpty))) {
                    //ERROR
                    //System.out.println("3");
                    //Si se busca por nombre de enfermedad, no se debe rellenar ni código, ni tipo de código
                    errorService.insertApiErrorEnumGenericErrorWithParameters(
                            apiResponseErrors,
                            ApiErrorEnum.INVALID_PARAMETERS,
                            "Disease search",
                            "If searching by disease name, do not fill in disease code or type of code.",
                            true,
                            new ArrayList<Parameter>() {{
                                add(new Parameter(Constants.DISEASE_CODE, false, false, diseaseCode, null));
                                add(new Parameter(Constants.TYPE_CODE, false, false, typeCode, null));
                            }});
                    validation.setErrors(true);
                }

                if (!validation.isErrors()) {
                    //Resetea los errores para volver a validar
                    validation.setErrors(true);
                    validation.setTypeSearch(Constants.TYPE_QUERY_UNKNOWN);

                    if (!diseaseNameEmpty) {
                        //System.out.println("ENFERMEDAD");
                        Validation diseaseNameValidation = validateDiseaseName(apiResponseErrors, parameters, sourceName, version, diseaseName, matchExactName);
                        if (diseaseNameValidation.isFound() && !diseaseNameValidation.isInternalError()) {
                            validation.setErrors(false);
                            validation.setTypeSearch(Constants.TYPE_QUERY_NAME);
                        }
                    } else if (!diseaseCodeEmpty && !typeCodeEmpty) {
                        //System.out.println("CODIGOS");
                        CodeAndTypeCodeValidation validationCodes = validateDiseaseCodeAndTypeCode(apiResponseErrors, parameters, sourceName, version, diseaseCode, typeCode);
                        System.out.println(validationCodes.toString());
                        if (validationCodes.isFoundCode() && validationCodes.isFoundTypeCode() && !validationCodes.isInternalError()) {
                            validation.setErrors(false);
                            validation.setTypeSearch(Constants.TYPE_QUERY_CODES);
                        }
                    }
                    semanticTypesValidationProcedure(apiResponseErrors, parameters, excludeSemanticTypes, forceSemanticTypes, validation);
                }

            }else{
                validation.setErrors(true);
                validation.setTypeSearch(Constants.TYPE_QUERY_UNKNOWN);
            }
        }else {
            validation.setErrors(true);
            validation.setTypeSearch(Constants.TYPE_QUERY_UNKNOWN);
        }

        return validation;
    }


    public Validation validateDiseaseName(List<ApiResponseError> apiResponseErrors, List<Parameter> parameters, String sourceName, Date version, String diseaseName, boolean matchExactName) throws Exception{
        Validation validation = new Validation();
        boolean existDisease;
        String message;
//        if (!common.isEmpty(diseaseName)){
        try {

                if (matchExactName) {
                    existDisease = diseaseService.existDiseaseBySourceAndVersionAndMatchExactNameTrueNative(sourceName, version, diseaseName);
                    message = "No disease with that exact name was found. Verify the exact disease name in the DISNET disease list.";
                }else {
                    existDisease = diseaseService.existDiseaseBySourceAndVersionAndMatchExactNameFalseNative(sourceName, version, diseaseName);
                    message = "No diseases were found in which this name appears. Verify the disease name in the DISNET disease list.";
                }
                if (!existDisease){
                    //Se agrega el error en la lista principal de la respuesta
                    errorService.insertApiErrorEnumGenericError(
                            apiResponseErrors,
                            ApiErrorEnum.RESOURCE_NOT_FOUND,
                            "Disease exception",
                            message + "",
                            true,
                            new Parameter(Constants.DISEASE_NAME, true, false, diseaseName, null));
                    validation.setFound(false);
                }else{
                    validation.setFound(true);
                    validation.setEmpty(false);
                    validation.setInternalError(false);
                }
            }catch (Exception e){
                //Se agrega el error en la lista principal de la respuesta
                errorService.insertApiErrorEnumGenericError(
                        apiResponseErrors,
                        ApiErrorEnum.INTERNAL_SERVER_ERROR,
                        Throwables.getRootCause(e).getClass().getName(),
                        e.getMessage(),
                        true,
                        new Parameter(Constants.DISEASE_NAME, false, false, diseaseName, null));
                validation.setInternalError(true);
            }
//        }else{
//            //Se agrega información extra en la lista principal de la respuesta
//            parameters.add(new Parameter(Constants.DISEASE_NAME, true, true, "", Constants.MESSAGE_PARAM_NOT_USED));
//            validation.setEmpty(true);
//        }
        return validation;
    }


    public CodeAndTypeCodeValidation validateDiseaseCodeAndTypeCode(List<ApiResponseError> apiResponseErrors, List<Parameter> parameters, String sourceName, Date version, String diseaseCode, String typeCode) throws Exception{
        CodeAndTypeCodeValidation validation = new CodeAndTypeCodeValidation();
        boolean diseaseCodeEmpty = common.isEmpty(diseaseCode);
        boolean typeCodeEmpty = common.isEmpty(typeCode);
//        if (!diseaseCodeEmpty){
//            if (!typeCodeEmpty) {
                try {
                    boolean existCodeAndTypeCode = codeService.existCodeByCodeAndResourceNameAndSourceAndVersionNative(sourceName, version, diseaseCode, typeCode);
                    if (!existCodeAndTypeCode) {//Si es falso
                        //Se agrega el error en la lista principal de la respuesta
                        errorService.insertApiErrorEnumGenericError(
                                apiResponseErrors,
                                ApiErrorEnum.RESOURCE_NOT_FOUND,
                                "Disease exception",
                                "Disease code not found or does not exist. Verify the disease code in the DISNET disease list.",
                                true,
                                new Parameter(Constants.DISEASE_CODE, true, false, diseaseCode, null));
                        errorService.insertApiErrorEnumGenericError(
                                apiResponseErrors,
                                ApiErrorEnum.RESOURCE_NOT_FOUND,
                                "Disease exception",
                                "Type code not found or does not exist. Verify the disease type code in the DISNET disease list.",
                                true,
                                new Parameter(Constants.TYPE_CODE, true, false, typeCode, null));
                        validation.setFoundCode(false);
                        validation.setFoundTypeCode(false);
                    }else{//EXITO
                        validation.setFoundCode(true);
                        validation.setFoundTypeCode(true);
                        validation.setEmpty(false);
                        validation.setInternalError(false);
                    }
                } catch (Exception e) {
                    //Se agrega el error en la lista principal de la respuesta
                    errorService.insertApiErrorEnumGenericErrorWithParameters(
                            apiResponseErrors,
                            ApiErrorEnum.INTERNAL_SERVER_ERROR,
                            Throwables.getRootCause(e).getClass().getName(),
                            e.getMessage(),
                            true,
                            new ArrayList<Parameter>(){{
                                add(new Parameter(Constants.DISEASE_CODE, false, false, diseaseCode, null));
                                add(new Parameter(Constants.TYPE_CODE, false, false, typeCode, null));
                            }} );
                    validation.setInternalError(true);
                }
//            } else{
//                //Se agrega información extra en la lista principal de la respuesta
//                parameters.add(new Parameter(Constants.TYPE_CODE, false, true, "", Constants.MESSAGE_PARAM_NOT_USED));
//                validation.setTypeCodeEmpty(true);
//            }
//        }else{
//            //Se agrega información extra en la lista principal de la respuesta
//            parameters.add(new Parameter(Constants.DISEASE_CODE, false, true, "", Constants.MESSAGE_PARAM_NOT_USED));
//            validation.setCodeEmpty(true);
//        }
        //if (validation.isCodeEmpty() || validation.isTypeCodeEmpty())
            //validation.setEmpty(true);
        return validation;
    }


    public Validation sourceValidation(List<ApiResponseError> apiResponseErrors, List<Parameter> parameters, String sourceName){
        Validation validation = new Validation();
        try {
            String sourceId = sourceService.findByNameNative(sourceName);
            if (common.isEmpty(sourceId)){
                errorService.insertApiErrorEnumGenericError(
                        apiResponseErrors,
                        ApiErrorEnum.RESOURCE_NOT_FOUND,
                        "Source exception",
                        "The source was not found. Check the DISNET source list.",
                        true,
                        new Parameter(Constants.SOURCE, true, false, sourceName, null));
                validation.setFound(false);
            }else{
                validation.setFound(true);
                validation.setEmpty(false);
                validation.setInternalError(false);
            }
        }catch (Exception e){
            errorService.insertApiErrorEnumGenericError(
                    apiResponseErrors,
                    ApiErrorEnum.INTERNAL_SERVER_ERROR,
                    Throwables.getRootCause(e).getClass().getName(),
                    e.getMessage(),
                    true,
                    new Parameter(Constants.SOURCE, true, false, sourceName, null));
            validation.setInternalError(true);
        }

        return validation;
    }


    public Validation versionValidation(List<ApiResponseError> apiResponseErrors, List<Parameter> parameters, String sourceName, Date version){
        Validation validation = new Validation();
        try {
            List<Date> versions = sourceService.findAllVersionsBySourceNative(sourceName);
            boolean found = false;
            if (versions != null){
                for (Date version_: versions) {
                    if (version.equals(version_)) found = true;
                }
            }
            if (!found){
                errorService.insertApiErrorEnumGenericError(
                        apiResponseErrors,
                        ApiErrorEnum.RESOURCE_NOT_FOUND,
                        "Version exception",
                        "The version was not found. Check the DISNET version list by source.",
                        true,
                        new Parameter(Constants.VERSION, true, false, timeProvider.dateFormatyyyMMdd(version), null));
                validation.setFound(false);
            }else{
                validation.setFound(true);
                validation.setEmpty(false);
                validation.setInternalError(false);
            }
        }catch (Exception e){
            errorService.insertApiErrorEnumGenericError(
                    apiResponseErrors,
                    ApiErrorEnum.INTERNAL_SERVER_ERROR,
                    Throwables.getRootCause(e).getClass().getName(),
                    e.getMessage(),
                    true,
                    new Parameter(Constants.VERSION, true, false, timeProvider.dateFormatyyyMMdd(version), null));
            validation.setInternalError(true);
        }

        return validation;
    }


    public SemanticTypesValidation excludeSemanticTypesValidation(List<ApiResponseError> apiResponseErrors, List<Parameter> parameters, String excludeSemanticTypes){
        SemanticTypesValidation validation = new SemanticTypesValidation();
        List<String> validSemanticTypes = null;
        boolean excludeSemanticTypesEmpty = common.isEmpty(excludeSemanticTypes);
        if (!excludeSemanticTypesEmpty){
            String[] semanticTypes = excludeSemanticTypes.split(",");
            if (semanticTypes.length > 0){
                try {
                    validSemanticTypes = new ArrayList<>();
                    for (String semanticType : semanticTypes) {//System.out.println(semanticType);
                        SemanticType semType = semanticTypeService.findById(semanticType.trim());
                        if (semType != null) {
                            validSemanticTypes.add(semType.getSemanticType());
                        } else {
                            errorService.insertApiErrorEnumGenericError(
                                    apiResponseErrors,
                                    ApiErrorEnum.RESOURCE_NOT_FOUND,
                                    "Semantic types exception",
                                    "Semantic type not found.",
                                    true,
                                    new Parameter(Constants.EXCLUDE_SEM_TYPES, false, false, semanticType, null));
                            validation.setFound(false);
                        }
                    }
                    if(validSemanticTypes.size() > 0){
                        validation.setValidSemanticTypes(validSemanticTypes);
                        validation.setFound(true);
                        validation.setEmpty(false);
                        validation.setInternalError(false);
                    }
                }catch (Exception e){
                    //Se agrega el error en la lista principal de la respuesta
                    errorService.insertApiErrorEnumGenericError(
                            apiResponseErrors,
                            ApiErrorEnum.INTERNAL_SERVER_ERROR,
                            Throwables.getRootCause(e).getClass().getName(),
                            e.getMessage(),
                            true,
                            new Parameter(Constants.EXCLUDE_SEM_TYPES, false, false, excludeSemanticTypes, null));
                    validation.setInternalError(true);
                }
            }else{
                errorService.insertApiErrorEnumGenericError(
                        apiResponseErrors,
                        ApiErrorEnum.INVALID_PARAMETERS,
                        "Semantic types exception",
                        "Semantics types empty. Format not valid for semantic types. It is necessary to separate each one with commas.",
                        true,
                        new Parameter(Constants.EXCLUDE_SEM_TYPES, false, false, excludeSemanticTypes, null));
                validation.setFound(false);
            }
        }else{
            //No importa que se encuenten vacíos
//            errorService.insertApiErrorEnumGenericError(
//                    apiResponseErrors,
//                    ApiErrorEnum.INVALID_PARAMETERS,
//                    "Semantic types exception",
//                    "Semantics types empty. You can use this parameter to delimit your query. For example: excludeSemanticTypes=sosy,dsyn",
//                    true,
//                    new Parameter(Constants.EXCLUDE_SEM_TYPES, false, false, excludeSemanticTypes, null));
            validation.setEmpty(true);
        }



        return validation;
    }


    public SemanticTypesValidation forceSemanticTypesValidation(List<ApiResponseError> apiResponseErrors, List<Parameter> parameters, String forceSemanticTypes){
        SemanticTypesValidation validation = new SemanticTypesValidation();
        boolean forceSemanticTypesEmpty = common.isEmpty(forceSemanticTypes);
        List<String> validSemanticTypes = null;
        if (!forceSemanticTypesEmpty){
            String[] semanticTypes = forceSemanticTypes.split(",");
            if (semanticTypes.length > 0){
                try {
                    validSemanticTypes = new ArrayList<>();
                    for (String semanticType : semanticTypes) {
                        SemanticType semType = semanticTypeService.findById(semanticType.trim());
                        if (semType != null) {
                            validSemanticTypes.add(semType.getSemanticType());
                        } else {
                            errorService.insertApiErrorEnumGenericError(
                                    apiResponseErrors,
                                    ApiErrorEnum.RESOURCE_NOT_FOUND,
                                    "Semantic types exception",
                                    "Semantic type not found.",
                                    true,
                                    new Parameter(Constants.FORCE_SEM_TYPES, false, false, semanticType, null));
                            validation.setFound(false);
                        }
                    }
                    if(validSemanticTypes.size() > 0){
                        validation.setValidSemanticTypes(validSemanticTypes);
                        validation.setFound(true);
                        validation.setEmpty(false);
                        validation.setInternalError(false);
                    }
                }catch (Exception e){
                    //Se agrega el error en la lista principal de la respuesta
                    errorService.insertApiErrorEnumGenericError(
                            apiResponseErrors,
                            ApiErrorEnum.INTERNAL_SERVER_ERROR,
                            Throwables.getRootCause(e).getClass().getName(),
                            e.getMessage(),
                            true,
                            new Parameter(Constants.FORCE_SEM_TYPES, false, false, forceSemanticTypes, null));
                    validation.setInternalError(true);
                }
            }else{
                errorService.insertApiErrorEnumGenericError(
                        apiResponseErrors,
                        ApiErrorEnum.INVALID_PARAMETERS,
                        "Semantic types exception",
                        "Semantics types empty. Format not valid for semantic types. It is necessary to separate each one with commas.",
                        true,
                        new Parameter(Constants.FORCE_SEM_TYPES, false, false, forceSemanticTypes, null));
                validation.setFound(false);
            }
        }else{
            //No importa que se encuentren vacíos
//            errorService.insertApiErrorEnumGenericError(
//                    apiResponseErrors,
//                    ApiErrorEnum.INVALID_PARAMETERS,
//                    "Semantic types exception",
//                    "Semantics types empty. You can use this parameter to delimit your query. For example: excludeSemanticTypes=sosy,dsyn",
//                    true,
//                    new Parameter(Constants.FORCE_SEM_TYPES, false, false, forceSemanticTypes, null));
            validation.setEmpty(true);
        }
        return validation;
    }


    /**
     * @param elements
     * @return
     */
    public List<DisnetConcept> removeRepetedDisnetConcepts(List<DisnetConcept> elements){
        List<DisnetConcept> resList = elements;
        Set<DisnetConcept> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.addAll(elements);
        elements.clear();
        elements.addAll(linkedHashSet);
        return resList;
    }





}
