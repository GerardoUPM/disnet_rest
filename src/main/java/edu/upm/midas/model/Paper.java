package edu.upm.midas.model;
import java.util.List;

/**
 * Created by gerardo on 16/05/2018.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className Paper
 * @see
 */
public class Paper {
    private String paperId;
    private String doi;
    private String alternativeId;
    private String title;
    private String authors;
    private String keywords;
    private Byte freeText;
    private String url;
    private List<Disease> diseases;
    private Text text;

    public Paper() {
    }

    public Paper(String paperId) {
        this.paperId = paperId;
    }

    public Paper(String paperId, String title, String authors, String keywords, String url, List<Disease> diseases, Text text) {
        this.paperId = paperId;
        this.title = title;
        this.authors = authors;
        this.keywords = keywords;
        this.url = url;
        this.diseases = diseases;
        this.text = text;
    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getAlternativeId() {
        return alternativeId;
    }

    public void setAlternativeId(String alternativeId) {
        this.alternativeId = alternativeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Byte getFreeText() {
        return freeText;
    }

    public void setFreeText(Byte freeText) {
        this.freeText = freeText;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<Disease> diseases) {
        this.diseases = diseases;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Paper{" +
                "paperId='" + paperId + '\'' +
                ", title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", keywords='" + keywords + '\'' +
                ", url='" + url + '\'' +
                ", diseases=" + diseases +
                ", text=" + text +
                '}';
    }
}
