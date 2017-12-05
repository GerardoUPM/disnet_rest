package edu.upm.midas.data.relational.service.helperNative;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import edu.upm.midas.common.util.TimeProvider;
import edu.upm.midas.constants.Constants;
import edu.upm.midas.data.relational.entities.edsssdb.SemanticType;
import edu.upm.midas.data.relational.service.*;
import edu.upm.midas.enums.ApiErrorEnum;
import edu.upm.midas.model.Disease;
import edu.upm.midas.model.DisnetConcept;
import edu.upm.midas.model.DiseaseDisnetConcepts;
import edu.upm.midas.common.util.Common;
import edu.upm.midas.common.util.UniqueId;
import edu.upm.midas.model.response.ApiResponseError;
import edu.upm.midas.model.response.Parameter;
import edu.upm.midas.model.response.validations.CodeAndTypeCodeValidation;
import edu.upm.midas.model.response.validations.SemanticTypesValidation;
import edu.upm.midas.model.response.validations.TypeSearchValidation;
import edu.upm.midas.model.response.validations.Validation;
import edu.upm.midas.service.error.ErrorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
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



    public List<Disease> getDiseasesWithTheirCodes(String sourceName, Date version, String diseaseName){
        return diseaseService.findCodesBySourceAndVersionAndDiseaseNameNative(sourceName, version, diseaseName, 0);
    }

    /**
     * @param sourceName
     * @param version
     * @param diseaseName
     * @param isValidated
     * @return
     */
    public List<Disease> getDiseasesAndTheirDisnetConcepts(String sourceName, Date version, String diseaseName, String code, String resourceName, boolean isValidated, TypeSearchValidation validation){
        List<Disease> diseaseList = null;
        if (validation.getTypeSearch().equals(Constants.TYPE_QUERY_NAME)){
            if (validation.getTypeSemanticTypesSearch().equals(Constants.FORCE_SEM_TYPES)){
                diseaseList = diseaseService.findSymptomsBySourceAndVersionAndDiseaseNameAndValidatedAndSemanticTypesNative(sourceName, version, diseaseName, isValidated, validation.getForceSemanticTypes());
            } else if(validation.getTypeSemanticTypesSearch().equals(Constants.EXCLUDE_SEM_TYPES)){
                diseaseList = diseaseService.findSymptomsBySourceAndVersionAndDiseaseNameAndValidatedAndExcludeSemanticTypesNative(sourceName, version, diseaseName, isValidated, validation.getExcludeSemanticTypes());
            }else {
                diseaseList = diseaseService.findSymptomsBySourceAndVersionAndDiseaseNameAndIsValidated(sourceName, version, diseaseName, isValidated);
            }
        } else if (validation.getTypeSearch().equals(Constants.TYPE_QUERY_CODES)){
            if (validation.getTypeSemanticTypesSearch().equals(Constants.FORCE_SEM_TYPES)){
                diseaseList = diseaseService.findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndValidatedAndForceSemanticTypesNative(sourceName, version, code, resourceName, isValidated, validation.getForceSemanticTypes());
            } else if(validation.getTypeSemanticTypesSearch().equals(Constants.EXCLUDE_SEM_TYPES)){
                diseaseList = diseaseService.findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndValidatedAndExcludeSemanticTypesNative(sourceName, version, code, resourceName, isValidated, validation.getExcludeSemanticTypes());
            }else {
                diseaseList = diseaseService.findSymptomsBySourceAndVersionAndCodeAndTypeCodeAndIsValidatedNative(sourceName, version, code, resourceName, isValidated);
            }
        }
/*
        if (disnetConcepts != null) {
            diseaseList = new ArrayList<>();
            Disease disease = new Disease();
            disease.setName(diseaseName);
            disease.setUrl(getDiseaseUrlFromDisnetConceptList(disnetConcepts));
            disease.setDisnetConceptList(disnetConcepts);
            disease.setDiseaseCount(disnetConcepts.size());
            diseaseList.add(disease);
        }
*/


//        if (disnetConcepts != null) {disnetConcepts = removeRepetedDisnetConcepts(disnetConcepts);}
//        for (DisnetConcept finding: disnetConcepts) {System.out.println(finding.toString());}
        return diseaseList;
    }

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
    public BigInteger getNumberDiseases(String sourceName, Date version){
        return diseaseService.numberDiseasesBySourceAndVersion(sourceName, version);
    }

    //Antes se usaba
    public List<edu.upm.midas.model.Disease> diseaseList(String sourceName, Date version){
        return diseaseService.findAllBySourceAndVersion(sourceName, version);
    }

    public List<edu.upm.midas.model.Disease> diseaseListWithUrlAndSymptomsCountBySourceAndVersionAndIsValidated(String sourceName, Date version, boolean isValidated){
        return diseaseService.findAllWithUrlAndSymptomsCountBySourceAndVersionAndIsValidated(sourceName, version, isValidated);
    }


    public List<DiseaseDisnetConcepts> getDiseasesWithFewerFindings(String sourceName, Date version, boolean isValidated, int limit){
        List<DiseaseDisnetConcepts> diseases = diseaseService.withFewerSymptomsBySourceAndVersionAndIsValidated(sourceName, version, isValidated, limit);
        //REGRESA LA MISMA LISTA PERO CON INFORMACIÓN DE SUS SINTOMAS... QUE CON LA PRIMER CONSULTA NO SE CONSIGUE
        List<DiseaseDisnetConcepts> diseaseList = getDiseaseWithSymptomsList(sourceName, version, isValidated, limit, diseases);
        return diseaseList;
    }


    public List<DiseaseDisnetConcepts> getDiseasesWithMoreFindings(String sourceName, Date version, boolean isValidated, int limit){
        List<DiseaseDisnetConcepts> diseases = diseaseService.withMoreSymptomsBySourceAndVersionAndIsValidated(sourceName, version, isValidated, limit);
        //REGRESA LA MISMA LISTA PERO CON INFORMACIÓN DE SUS SINTOMAS... QUE CON LA PRIMER CONSULTA NO SE CONSIGUE
        List<DiseaseDisnetConcepts> diseaseList = getDiseaseWithSymptomsList(sourceName, version, isValidated, limit, diseases);
        return diseaseList;
    }


    public List<DiseaseDisnetConcepts> getDiseaseWithSymptomsList(String sourceName, Date version, boolean isValidated, int limit, List<DiseaseDisnetConcepts> diseases){
        List<DiseaseDisnetConcepts> diseaseList = null;
        if (diseases != null) {
            diseaseList = new ArrayList<>();
            for (DiseaseDisnetConcepts diseaseDisnetConcepts : diseases) {
                //SE OBTIENEN LOS SINTOMAS POR CADA ENFERMEDAD ENCONTRADA
                List<DisnetConcept> disnetConcepts = diseaseService.findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidated(sourceName, version, diseaseDisnetConcepts.getDiseaseId(), isValidated);
                if (disnetConcepts != null) {
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


    public List<DisnetConcept> getMostCommonSymptoms(String sourceName, Date version, boolean isValidated, int limit){
        return symptomService.mostCommonBySourceAndVersionAndValidated(sourceName, version, isValidated, limit);
    }


    public List<DisnetConcept> getLessCommonSymptoms(String sourceName, Date version, boolean isValidated, int limit){
        return symptomService.lessCommonBySourceAndVersionAndValidated(sourceName, version, isValidated, limit);
    }

    public TypeSearchValidation sourceAndVersionAndDiseaseNameValidation(List<ApiResponseError> apiResponseErrors, List<Parameter> parameters, String sourceName, Date version, String diseaseName) throws Exception {
        TypeSearchValidation validation = new TypeSearchValidation();

        TypeSearchValidation svVal = sourceAndVersionValidation(apiResponseErrors, parameters, sourceName, version);
        Validation diseaseNameVal = validateDiseaseName(apiResponseErrors, parameters, sourceName, version, diseaseName, false);
        if (!svVal.isErrors()) {
            if (diseaseNameVal.isFound())
                validation.setErrors(false);
            else validation.setErrors(true);
        } else validation.setErrors(true);
        return validation;
    }


    public TypeSearchValidation sourceAndVersionValidation(List<ApiResponseError> apiResponseErrors, List<Parameter> parameters, String sourceName, Date version){
        TypeSearchValidation validation = new TypeSearchValidation();

        Validation sourceValidation = sourceValidation(apiResponseErrors, parameters, sourceName);
        Validation versionValidation = versionValidation(apiResponseErrors, parameters, sourceName, version);

        if (sourceValidation.isFound() && versionValidation.isFound()) {
            validation.setErrors(false);
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
        Validation versionValidation = versionValidation(apiResponseErrors, parameters, sourceName, version);

        if (sourceValidation.isFound() && versionValidation.isFound()) {
            validation.setErrors(false);
            semanticTypesValidationProcedure(apiResponseErrors, parameters, excludeSemanticTypes, forceSemanticTypes, validation);
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
                                                                   String forceSemanticTypes) throws Exception {
        TypeSearchValidation validation = new TypeSearchValidation();
        List<ApiResponseError> apiResponseErrorList = null;

        boolean diseaseNameEmpty = common.isEmpty(diseaseName);
        boolean diseaseCodeEmpty = common.isEmpty(diseaseCode);
        boolean typeCodeEmpty = common.isEmpty(typeCode);

        Validation sourceValidation = sourceValidation(apiResponseErrors, parameters, sourceName);
        Validation versionValidation = versionValidation(apiResponseErrors, parameters, sourceName, version);

        if (sourceValidation.isFound() && versionValidation.isFound()) {

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
                            add(new Parameter(Constants.DISEASE_NAME, true, false, diseaseCode, null));
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

                if (!diseaseNameEmpty) {
                    //
                    System.out.println("ENFERMEDAD");
                    Validation diseaseNameValidation = validateDiseaseName(apiResponseErrors, parameters, sourceName, version, diseaseName, true);
                    if (diseaseNameValidation.isFound() && !diseaseNameValidation.isInternalError()) {
                        validation.setErrors(false);
                        validation.setTypeSearch(Constants.TYPE_QUERY_NAME);
                    }
                } else if (!diseaseCodeEmpty && !typeCodeEmpty) {
                    System.out.println("CODIGOS");
                    CodeAndTypeCodeValidation validationCodes = validateDiseaseCodeAndTypeCode(apiResponseErrors, parameters, sourceName, version, diseaseCode, typeCode);
                    System.out.println(validationCodes.toString());
                    if (validationCodes.isFoundCode() && validationCodes.isFoundTypeCode() && !validationCodes.isInternalError()) {
                        validation.setErrors(false);
                        validation.setTypeSearch(Constants.TYPE_QUERY_CODES);
                    }
                } else {
                    validation.setErrors(true);
                    validation.setTypeSearch(Constants.TYPE_QUERY_UNKNOWN);
                }

                semanticTypesValidationProcedure(apiResponseErrors, parameters, excludeSemanticTypes, forceSemanticTypes, validation);
            }
        }else {
            validation.setErrors(true);
            validation.setTypeSearch(Constants.TYPE_QUERY_UNKNOWN);
        }

        return validation;
    }


    public Validation validateDiseaseName(List<ApiResponseError> apiResponseErrors, List<Parameter> parameters, String sourceName, Date version, String diseaseName, boolean exact) throws Exception{
        Validation validation = new Validation();
//        if (!common.isEmpty(diseaseName)){
            try {
                boolean existDisease = false;
                if (exact) existDisease = diseaseService.existDiseaseByExactNameAndSourceAndVersionNative(sourceName, version, diseaseName);
                else existDisease = diseaseService.existDiseaseByLikeNameAndSourceAndVersionNative(sourceName, version, diseaseName);
                if (!existDisease){
                    //Se agrega el error en la lista principal de la respuesta
                    errorService.insertApiErrorEnumGenericError(
                            apiResponseErrors,
                            ApiErrorEnum.RESOURCE_NOT_FOUND,
                            "Disease exception",
                            "Disease not found or does not exist. Verify the disease name in the DISNET disease list.",
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
                    new Parameter(Constants.SOURCE, false, false, sourceName, null));
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
                    new Parameter(Constants.VERSION, false, false, timeProvider.dateFormatyyyMMdd(version), null));
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
            errorService.insertApiErrorEnumGenericError(
                    apiResponseErrors,
                    ApiErrorEnum.INVALID_PARAMETERS,
                    "Semantic types exception",
                    "Semantics types empty. You can use this parameter to delimit your query. For example: excludeSemanticTypes=sosy,dsyn",
                    true,
                    new Parameter(Constants.EXCLUDE_SEM_TYPES, false, false, excludeSemanticTypes, null));
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
            errorService.insertApiErrorEnumGenericError(
                    apiResponseErrors,
                    ApiErrorEnum.INVALID_PARAMETERS,
                    "Semantic types exception",
                    "Semantics types empty. You can use this parameter to delimit your query. For example: excludeSemanticTypes=sosy,dsyn",
                    true,
                    new Parameter(Constants.FORCE_SEM_TYPES, false, false, forceSemanticTypes, null));
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
