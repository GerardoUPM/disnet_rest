package edu.upm.midas.model.response;
/**
 * Created by gerardo on 21/12/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className Text
 * @see
 */
public class Text {

    private String textId;
    private Integer textOrder;
    private String text;
    private String section;


    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }

    public Integer getTextOrder() {
        return textOrder;
    }

    public void setTextOrder(Integer textOrder) {
        this.textOrder = textOrder;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
