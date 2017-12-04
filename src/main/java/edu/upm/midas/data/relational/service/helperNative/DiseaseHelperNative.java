package edu.upm.midas.data.relational.service.helperNative;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import edu.upm.midas.constants.Constants;
import edu.upm.midas.data.relational.service.CodeService;
import edu.upm.midas.data.relational.service.DiseaseService;
import edu.upm.midas.data.relational.service.SymptomService;
import edu.upm.midas.enums.ApiErrorEnum;
import edu.upm.midas.model.Disease;
import edu.upm.midas.model.DisnetConcept;
import edu.upm.midas.model.DiseaseDisnetConcepts;
import edu.upm.midas.common.util.Common;
import edu.upm.midas.common.util.UniqueId;
import edu.upm.midas.model.SymptomWithCount;
import edu.upm.midas.model.response.ApiResponseError;
import edu.upm.midas.model.response.Parameter;
import edu.upm.midas.model.response.ResponseFather;
import edu.upm.midas.model.response.validations.CodeAndTypeCodeValidation;
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
    private CodeService codeService;
    @Autowired
    private UniqueId uniqueId;
    @Autowired
    private DocumentHelperNative documentHelperNative;
    @Autowired
    private ErrorService errorService;
    @Autowired
    private Common common;

    private static final Logger logger = LoggerFactory.getLogger(DiseaseHelperNative.class);
    @Autowired
    ObjectMapper objectMapper;


    /**
     * @param sourceName
     * @param version
     * @param diseaseName
     * @param isValidated
     * @return
     */
    public List<Disease> getDiseasesAndTheirDisnetConcepts(String sourceName, Date version, String diseaseName, boolean isValidated, String typeSearch){
        List<Disease> diseaseList = null;
        if (typeSearch.equals("name")){System.out.println("NOMBRE");
            diseaseList = diseaseService.findSymptomsBySourceAndVersionAndDiseaseNameAndIsValidated(sourceName, version, diseaseName, isValidated);
        } else if (typeSearch.equals("code")){
            Disease disease = new Disease();
            diseaseList = diseaseService.findSymptomsBySourceAndVersionAndDiseaseNameAndIsValidated(sourceName, version, diseaseName, isValidated);

        }

/*
        if (disnetConcepts != null) {
            diseaseList = new ArrayList<>();
            Disease disease = new Disease();
            disease.setName(diseaseName);
            disease.setUrl(getDiseaseUrlFromDisnetConceptList(disnetConcepts));
            disease.setDisnetConceptList(disnetConcepts);
            disease.setDisnetConceptsCount(disnetConcepts.size());
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
        List<DiseaseDisnetConcepts> diseaseList = getDiseaseWithCountList(sourceName, version, isValidated, limit, diseases);
        return diseaseList;
    }


    public List<DiseaseDisnetConcepts> getDiseasesWithMoreFindings(String sourceName, Date version, boolean isValidated, int limit){
        List<DiseaseDisnetConcepts> diseases = diseaseService.withMoreSymptomsBySourceAndVersionAndIsValidated(sourceName, version, isValidated, limit);
        //REGRESA LA MISMA LISTA PERO CON INFORMACIÓN DE SUS SINTOMAS... QUE CON LA PRIMER CONSULTA NO SE CONSIGUE
        List<DiseaseDisnetConcepts> diseaseList = getDiseaseWithCountList(sourceName, version, isValidated, limit, diseases);
        return diseaseList;
    }


    public List<DiseaseDisnetConcepts> getDiseaseWithCountList(String sourceName, Date version, boolean isValidated, int limit, List<DiseaseDisnetConcepts> diseases){
        List<DiseaseDisnetConcepts> diseaseList = null;
        if (diseases != null) {
            diseaseList = new ArrayList<>();
            for (DiseaseDisnetConcepts diseaseDisnetConcepts : diseases) {
                //SE OBTIENEN LOS SINTOMAS POR CADA ENFERMEDAD ENCONTRADA
                List<DisnetConcept> DisnetConcepts = diseaseService.findSymptomsBySourceAndVersionAndDiseaseIdAndIsValidated(sourceName, version, diseaseDisnetConcepts.getDiseaseId(), isValidated);
                if (DisnetConcepts != null) {
                    DiseaseDisnetConcepts disease = new DiseaseDisnetConcepts();
                    disease.setDisnetConceptList(DisnetConcepts);
                    disease.setDiseaseId(diseaseDisnetConcepts.getDiseaseId());
                    disease.setName(diseaseDisnetConcepts.getName());
                    disease.setCount(diseaseDisnetConcepts.getCount());
                    diseaseList.add(disease);
                }
            }
        }
        return diseaseList;
    }


    public List<SymptomWithCount> getMostCommonSymptoms(String sourceName, Date version, boolean isValidated, int limit){
        return symptomService.mostCommonBySourceAndVersionAndValidated(sourceName, version, isValidated, limit);
    }


    public List<SymptomWithCount> getLessCommonSymptoms(String sourceName, Date version, boolean isValidated, int limit){
        return symptomService.lessCommonBySourceAndVersionAndValidated(sourceName, version, isValidated, limit);
    }


    public TypeSearchValidation validateDiseaseSearchingParameters(List<ApiResponseError> apiResponseErrors, List<Parameter> parameters, String sourceName, Date version, String diseaseName, String diseaseCode, String typeCode) throws Exception {
        TypeSearchValidation validation = new TypeSearchValidation();
        List<ApiResponseError> apiResponseErrorList = null;

        boolean diseaseNameEmpty = common.isEmpty(diseaseName);
        boolean diseaseCodeEmpty = common.isEmpty(diseaseCode);
        boolean typeCodeEmpty = common.isEmpty(typeCode);

        if (diseaseNameEmpty && diseaseCodeEmpty && typeCodeEmpty){
            //ERROR
            //System.out.println("1");
            //Es necesario tener al menos un parametro de busqueda
            errorService.insertApiErrorEnumGenericErrorWithParameters(
                    apiResponseErrors,
                    ApiErrorEnum.INVALID_PARAMETERS,
                    "Disease search",
                    "A search parameter must be selected. By name or by disease code.",
                    true,
                    new ArrayList<Parameter>(){{
                        add(new Parameter(Constants.DISEASE_NAME, true, false, diseaseCode, null));
                        add(new Parameter(Constants.DISEASE_CODE, true, false, diseaseCode, null));
                        add(new Parameter(Constants.TYPE_CODE, true, false, typeCode, null));
                    }} );
            validation.setErrors(true);
        }else if (!diseaseNameEmpty && !diseaseCodeEmpty && !typeCodeEmpty ){
            //Solo se debe seleccionar un parametro de busqueda, no los dos
            //System.out.println("4");
            errorService.insertApiErrorEnumGenericErrorWithParameters(
                    apiResponseErrors,
                    ApiErrorEnum.INVALID_PARAMETERS,
                    "Disease search",
                    "It is necessary to select only one search parameter, not both.",
                    true,
                    new ArrayList<Parameter>(){{
                        add(new Parameter(Constants.DISEASE_NAME, true, false, diseaseCode, null));
                        add(new Parameter(Constants.DISEASE_CODE, false, false, diseaseCode, null));
                        add(new Parameter(Constants.TYPE_CODE, false, false, typeCode, null));
                    }} );
            validation.setErrors(true);
        }else if(diseaseNameEmpty && ( diseaseCodeEmpty || typeCodeEmpty) ){
            //ERROR
            //System.out.println("2");
            //Si el nombre de enfermedad esta vacío, no se debe dejar vacío, el código ni el tipo de código
            errorService.insertApiErrorEnumGenericErrorWithParameters(
                    apiResponseErrors,
                    ApiErrorEnum.INVALID_PARAMETERS,
                    "Disease search",
                    "Both disease code and type of code are pair and neither should be empty.",
                    true,
                    new ArrayList<Parameter>(){{
                        add(new Parameter(Constants.DISEASE_CODE, false, false, diseaseCode, null));
                        add(new Parameter(Constants.TYPE_CODE, false, false, typeCode, null));
                    }} );
            validation.setErrors(true);
        }else if(!diseaseNameEmpty && ( (diseaseCodeEmpty && !typeCodeEmpty) || (!diseaseCodeEmpty && typeCodeEmpty) )){
            //ERROR
            //System.out.println("3");
            //Si se busca por nombre de enfermedad, no se debe rellenar ni código, ni tipo de código
            errorService.insertApiErrorEnumGenericErrorWithParameters(
                    apiResponseErrors,
                    ApiErrorEnum.INVALID_PARAMETERS,
                    "Disease search",
                    "If searching by disease name, do not fill in disease code or type of code.",
                    true,
                    new ArrayList<Parameter>(){{
                        add(new Parameter(Constants.DISEASE_CODE, false, false, diseaseCode, null));
                        add(new Parameter(Constants.TYPE_CODE, false, false, typeCode, null));
                    }} );
            validation.setErrors(true);
        }

        if (!validation.isErrors()){

            validation.setErrors(true);

            if(!diseaseNameEmpty){
                //
                System.out.println("ENFERMEDAD");
                Validation diseaseNameValidation = validateDiseaseName(apiResponseErrors, parameters, sourceName, version, diseaseName);
                if(diseaseNameValidation.isFound() && diseaseNameValidation.isInternalError()==false){
                    validation.setErrors(false);
                    validation.setTypeSearch("name");
                }
            } else if(!diseaseCodeEmpty && !typeCodeEmpty){
                System.out.println("CODIGOS");
                CodeAndTypeCodeValidation validationCodes = validateDiseaseCodeAndTypeCode(apiResponseErrors, parameters, sourceName, version, diseaseCode, typeCode);
                if (validationCodes.isFoundCode() && validationCodes.isFoundTypeCode() && validationCodes.isInternalError()==false){
                    validation.setErrors(false);
                    validation.setTypeSearch("codes");
                }
            } else {
                validation.setErrors(true);
                validation.setTypeSearch("unknown");
            }

        }

        return validation;
    }


    public Validation validateDiseaseName(List<ApiResponseError> apiResponseErrors, List<Parameter> parameters, String sourceName, Date version, String diseaseName) throws Exception{
        Validation validation = new Validation();
//        if (!common.isEmpty(diseaseName)){
            try {
                boolean existDisease = diseaseService.existDiseaseByExactNameAndSourceAndVersionNative(sourceName, version, diseaseName);
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
                }
            }catch (Exception e){
                //Se agrega el error en la lista principal de la respuesta
                errorService.insertApiErrorEnumGenericError(
                        apiResponseErrors,
                        ApiErrorEnum.INTERNAL_SERVER_ERROR,
                        Throwables.getRootCause(e).getClass().getName(),
                        e.getMessage(),
                        true,
                        new Parameter(Constants.DISEASE_NAME, diseaseName));
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
                                add(new Parameter(Constants.DISEASE_CODE, diseaseCode));
                                add(new Parameter(Constants.TYPE_CODE, typeCode));
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

    public void validateTypeCode(ResponseFather responseFather, List<Parameter> parameters, String typeCode) throws Exception{

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
