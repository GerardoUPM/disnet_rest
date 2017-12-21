package edu.upm.midas.common.util;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    public String createExcludeSemanticTypesQuery(List<String> semanticTypes){System.out.println("HOLA");
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
        }System.out.println("HOLA" + query);
        return query;
    }


    public String cutString(String str) {
        return str = str.substring(0, str.length()-2);
    }


    /**
     * @param cutStart
     * @param cutFinal
     * @param str
     * @return
     */
    public String cutStringPerformance(int cutStart, int cutFinal, String str) {
        return str = str.substring(cutStart, str.length() - cutFinal);
    }


    public String replaceUTFCharacters(String data){

        Pattern p = Pattern.compile("\\\\u(\\p{XDigit}{4})");
        Matcher m = p.matcher(data);
        StringBuffer buf = new StringBuffer(data.length());
        while (m.find()) {
            String ch = String.valueOf((char) Integer.parseInt(m.group(1), 16));
            m.appendReplacement(buf, Matcher.quoteReplacement(ch));
        }
        return m.appendTail(buf).toString();

    }


    public void removeRepetedElementsList(List<String> elementsList){
        Set<String> linkedHashSet = new LinkedHashSet<String>();
        linkedHashSet.addAll(elementsList);
        elementsList.clear();
        elementsList.addAll(linkedHashSet);
    }


    public boolean itsFound(String originalStr, String findStr){
//        System.out.println("RECIBE itsFound: ORI:" + originalStr + " | FIND: " + findStr);
        return originalStr.trim().indexOf(findStr.trim()) != -1;// Retorna true si ha encontrado la subcadena en la cadena
    }

    public boolean isAlfaNumeric(final String string) {
        for(int i = 0; i < string.length(); ++i) {
            char character = string.charAt(i);

            if(!Character.isLetterOrDigit(character)) {
                return false;
            }
        }
        return true;
    }


}
