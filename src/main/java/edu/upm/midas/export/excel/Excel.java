package edu.upm.midas.export.excel;

import edu.upm.midas.constants.Constants;
import edu.upm.midas.model.Disease;
import edu.upm.midas.model.DisnetConcept;
import edu.upm.midas.model.Document;
import edu.upm.midas.model.Text;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Created by gerardo on 22/12/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className Excel
 * @see
 */
@Service
public class Excel {

    public void buildExcelDocument(String path, Disease disease) throws Exception {


        String PATH = path +disease.getName()+".xlsx";

        // create excel xls sheet
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("TERMS");
        CellStyle generalStyle = workbook.createCellStyle();
        generalStyle.setAlignment(HorizontalAlignment.LEFT);
        generalStyle.setVerticalAlignment(VerticalAlignment.TOP);
        int rowCount = 0;
        //sheet.setDefaultColumnWidth(30);

        Row diseaseInfoHeadRow = sheet.createRow(0);
        diseaseInfoHeadRow.createCell(0).setCellValue("DiseaseName");
        diseaseInfoHeadRow.createCell(1).setCellValue("DiseaseCode");
        diseaseInfoHeadRow.createCell(2).setCellValue("disnetConceptCount");

        Row diseaseInfoBodyRow = sheet.createRow(1);
        diseaseInfoBodyRow.createCell(0).setCellValue(disease.getName());
        diseaseInfoBodyRow.getCell(0).setCellStyle(generalStyle);
        diseaseInfoBodyRow.createCell(1).setCellValue(disease.getDiseaseId());
        diseaseInfoBodyRow.getCell(1).setCellStyle(generalStyle);
        diseaseInfoBodyRow.createCell(2).setCellValue(disease.getDisnetConceptsCount());
        diseaseInfoBodyRow.getCell(2).setCellStyle(generalStyle);

        Row separationRow = sheet.createRow(3);

        if (disease.getDocumentList() != null){
            Row documentHeadRow = sheet.createRow(4);
            documentHeadRow.createCell(0).setCellValue("DocumentId");
            documentHeadRow.createCell(1).setCellValue("Version");
            documentHeadRow.createCell(2).setCellValue("Url");

            rowCount = 5;
            for (Document doc: disease.getDocumentList()) {
                Row documentBodyRow = sheet.createRow(rowCount++);
                documentBodyRow.createCell(0).setCellValue(doc.getDocumentId());
                documentBodyRow.getCell(0).setCellStyle(generalStyle);
                documentBodyRow.createCell(1).setCellValue(doc.getVersion());
                documentBodyRow.getCell(1).setCellStyle(generalStyle);
                documentBodyRow.createCell(2).setCellValue(doc.getUrl());
                documentBodyRow.getCell(2).setCellStyle(generalStyle);
            }
            Row textHeadRow = sheet.createRow(10);
            textHeadRow.createCell(0).setCellValue("TextId");
            textHeadRow.createCell(1).setCellValue("Section");
            textHeadRow.createCell(2).setCellValue("TextOrder");
            textHeadRow.createCell(3).setCellValue("Text");

            rowCount = 11;
            int showOne = 1;
            //TEXTOS NO REPETIDOS
            for (Document doc: disease.getDocumentList()) {
                if (showOne == 1){
                    if (doc.getTextList() != null) {
                        for (Text txt:doc.getTextList()){
                            Row documentBodyRow = sheet.createRow(rowCount++);
                            documentBodyRow.createCell(0).setCellValue(txt.getTextId());
                            documentBodyRow.getCell(0).setCellStyle(generalStyle);
                            documentBodyRow.createCell(1).setCellValue(txt.getSection());
                            documentBodyRow.getCell(1).setCellStyle(generalStyle);
                            documentBodyRow.createCell(2).setCellValue(txt.getTextOrder());
                            documentBodyRow.getCell(2).setCellStyle(generalStyle);
                            documentBodyRow.createCell(3).setCellValue(txt.getText());
                            documentBodyRow.getCell(0).setCellStyle(generalStyle);
                        }
                    }
                    showOne++;
                }
            }

        }

        if (disease.getDisnetConceptList() != null){
            Row disnetConceptHeadRow = sheet.createRow(60);
            disnetConceptHeadRow.createCell(0).setCellValue("TextsId");
            disnetConceptHeadRow.createCell(1).setCellValue("CUI");
            disnetConceptHeadRow.createCell(2).setCellValue("MatchedWords");
            disnetConceptHeadRow.createCell(3).setCellValue("Name");
            disnetConceptHeadRow.createCell(4).setCellValue("SemanticTypes");
            disnetConceptHeadRow.createCell(5).setCellValue("Validated");

            disnetConceptHeadRow.createCell(6).setCellValue("TP");
            disnetConceptHeadRow.createCell(7).setCellValue("FP");
            disnetConceptHeadRow.createCell(8).setCellValue("FN");
            disnetConceptHeadRow.createCell(9).setCellValue("TN");

            rowCount = 61;

            for (DisnetConcept term: disease.getDisnetConceptList()) {
                String texts = "";
                String matchedWords = "";
                int count = 1;
                for (Text text: term.getTexts()) {
                    texts = texts + text.getTextId() +
                        "\nLocation => Word(s): " + text.getMatchedWords() +
                        " | Position: " + text.getPositionalInfo() + "\n";
                    if (count == 1) {
                        matchedWords = matchedWords + text.getMatchedWords();
                    }else{
                        matchedWords = matchedWords + ", " + text.getMatchedWords();
                    }
                    count++;
                }
                CellStyle styleWrapText = workbook.createCellStyle();
                styleWrapText.setWrapText(true);

                Row disnetConceptBodyRow = sheet.createRow(rowCount++);
                disnetConceptBodyRow.createCell(0).setCellValue(texts);
                disnetConceptBodyRow.getCell(0).setCellStyle(styleWrapText);
                //disnetConceptBodyRow.getCell(0).setCellStyle(generalStyle);
                disnetConceptBodyRow.createCell(1).setCellValue(term.getCui());
                disnetConceptBodyRow.getCell(1).setCellStyle(generalStyle);
                disnetConceptBodyRow.createCell(2).setCellValue(deDup(matchedWords.replace("[", "").replace("]", "").replace("&", ", ")));
                disnetConceptBodyRow.getCell(2).setCellStyle(generalStyle);
                disnetConceptBodyRow.createCell(3).setCellValue(term.getName());
                disnetConceptBodyRow.getCell(3).setCellStyle(generalStyle);
                disnetConceptBodyRow.createCell(4).setCellValue(term.getSemanticTypes().toString());
                disnetConceptBodyRow.getCell(4).setCellStyle(generalStyle);
                disnetConceptBodyRow.createCell(5).setCellValue(term.getValidated());
                disnetConceptBodyRow.getCell(5).setCellStyle(generalStyle);

            }

        }

        try {
            FileOutputStream outputStream = new FileOutputStream(PATH);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println("Done");

        /*// create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);


        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Firstname");
        header.getCell(0).setCellStyle(style);
        header.createCell(1).setCellValue("LastName");
        header.getCell(1).setCellStyle(style);
        header.createCell(2).setCellValue("Age");
        header.getCell(2).setCellStyle(style);
        header.createCell(3).setCellValue("Job Title");
        header.getCell(3).setCellStyle(style);
        header.createCell(4).setCellValue("Company");
        header.getCell(4).setCellStyle(style);
        header.createCell(5).setCellValue("Address");
        header.getCell(5).setCellStyle(style);
        header.createCell(6).setCellValue("City");
        header.getCell(6).setCellStyle(style);
        header.createCell(7).setCellValue("Country");
        header.getCell(7).setCellStyle(style);
        header.createCell(8).setCellValue("Phone Number");
        header.getCell(8).setCellStyle(style);*/

    }

    public String deDup(String s) {
        return new LinkedHashSet<String>(Arrays.asList(s.split(", "))).toString().replaceAll("(^\\[|\\]$)", "");//.replace(", ", "-");
    }

}
