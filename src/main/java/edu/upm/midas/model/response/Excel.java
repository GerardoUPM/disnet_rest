package edu.upm.midas.model.response;
import edu.upm.midas.model.Disease;
import edu.upm.midas.model.DisnetConcept;
import edu.upm.midas.model.Document;
import edu.upm.midas.model.Text;

import java.util.List;

/**
 * Created by gerardo on 22/12/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className Excel
 * @see
 */
public class Excel {

    private Disease disease;
    private Integer documentsCount;
    private List<Document> documentList;
    //Los textos no repetidos se encuentran dentro de los documentos
    //Los terminos no repetidos se encuentran dentro de la enfermedad


    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public Integer getDocumentsCount() {
        return documentsCount;
    }

    public void setDocumentsCount(Integer documentsCount) {
        this.documentsCount = documentsCount;
    }

    public List<Document> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<Document> documentList) {
        this.documentList = documentList;
    }
}
