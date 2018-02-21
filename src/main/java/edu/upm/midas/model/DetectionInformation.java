package edu.upm.midas.model;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by gerardo on 21/02/2018.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className DetectionInformation
 * @see
 */
public class DetectionInformation {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String textId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String matchedWords;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String positionalInfo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer timesFoundInTexts;



    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }

    public String getMatchedWords() {
        return matchedWords;
    }

    public void setMatchedWords(String matchedWords) {
        this.matchedWords = matchedWords;
    }

    public String getPositionalInfo() {
        return positionalInfo;
    }

    public void setPositionalInfo(String positionalInfo) {
        this.positionalInfo = positionalInfo;
    }

    public Integer getTimesFoundInTexts() {
        return timesFoundInTexts;
    }

    public void setTimesFoundInTexts(Integer timesFoundInTexts) {
        this.timesFoundInTexts = timesFoundInTexts;
    }
}
