package edu.upm.midas.service.validator;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author Eduardo P. Garcia del Valle
 */
@Service
public class Validator {

    public static final int VALIDATION_SHEET = 1;
    public static final int VALIDATION_SHEET_0 = 0;
    public static final int TVP_COL_NUM = 7;
    public static final int RELEVANT_COL_NUM = 8;
    public static final String TVP_COL_NAME = "TVP";

    private static double globalTruePositiveCount;//TP
    private static double globalTrueNegativeCount;//TN
    private static double globalFalsePositiveCount;
    private static double globalFalsePositiveContextCount;
    private static double globalFalsePositiveRealCount;
    private static double globalFalsePositiveErrorCount;
    private static double globalFalseNegativeCount;
    private static double globalFalseNegativeMetamapCount;
    private static double globalFalseNegativeTvpCount;
    private static double globalTotal;

    public void validator(String validationFolder, int validationSheet) throws IOException, InvalidFormatException {


        File dir = new File(validationFolder);

        File[] directoryListing = dir.listFiles();

        int count = 1;
        if (directoryListing != null) {
            for (File file : directoryListing) {
                try {
                    readExcel(file, validationSheet, count);
                    count++;
                }
                catch(NotOLE2FileException exception){
                    System.out.println("File " + file.getAbsolutePath() + " is not OLE");
                }
            }
        }

        validate(
                globalTruePositiveCount, globalTrueNegativeCount, globalFalsePositiveCount,
                globalFalsePositiveContextCount, globalFalsePositiveRealCount, globalFalsePositiveErrorCount,
                globalFalseNegativeCount,
                globalFalseNegativeMetamapCount, globalFalseNegativeTvpCount,
                globalTotal);
        
        Double globalPrecision =
                globalTruePositiveCount / (globalTruePositiveCount + (globalFalsePositiveRealCount + globalFalsePositiveContextCount) );

        Double globalPrecision_FPREAL = globalTruePositiveCount / ( globalTruePositiveCount + globalFalsePositiveRealCount);
        Double globalPrecision_FPCONTEXT = globalTruePositiveCount / ( globalTruePositiveCount + globalFalsePositiveContextCount);

        Double globalRecallMetamap = globalTruePositiveCount / (globalTruePositiveCount + globalFalseNegativeMetamapCount);
        Double globalRecallTvp = globalTruePositiveCount / (globalTruePositiveCount + globalFalseNegativeTvpCount);
        Double globalRecall = globalTruePositiveCount / (globalTruePositiveCount + (globalFalseNegativeMetamapCount + globalFalseNegativeTvpCount));

        Double f1Score = 2 * ( (globalPrecision * globalRecall) / (globalPrecision + globalRecall) );

        System.out.println("GLOBAL PRECISION: " + globalPrecision);
        System.out.println("GLOBAL RESULT: \n"
                + " TP: " + globalTruePositiveCount + ";\n"
                + " FP_REAL: " + globalFalsePositiveRealCount + ";\n"
                + " FP_CONTEXT: " + globalFalsePositiveContextCount + ";\n"
                + " FP: " + globalFalsePositiveCount + ";\n"
                + " TN: " + globalTrueNegativeCount + ";\n"
                + " FN_TVP: " + globalFalseNegativeTvpCount + ";\n"
                + " FN_METAMAP: " + globalFalseNegativeMetamapCount + ";\n"
                + " FN: " + globalFalseNegativeCount + ";\n"
                + " TOTAL: " + globalTotal + ";\n"
                + " PRECISION: " + globalPrecision + ";\n"
                + " ADDITIONAL MEASURES: \n"
                + " PRECISION_FPREAL: " + globalPrecision_FPREAL + ";\n"
                + " PRECISION_REPCONTEXT: " + globalPrecision_FPCONTEXT + ";\n"
                + " RECALL_METAMAP: " + globalRecallMetamap + ";\n"
                + " RECALL_TVP: " + globalRecallTvp + ";\n"
                + " RECALL: " + globalRecall + ";\n"
                + " F1_SCORE: " + f1Score + ";\n"
        );
    }

    private void readExcel(File file, int validationSheet, int count) throws InvalidFormatException, IOException {


        System.out.print(count +". Disease: " + file.getName().replace(".xlsx", ""));
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        Workbook workbook = WorkbookFactory.create(file);

        if (workbook.getNumberOfSheets() <= validationSheet) {/*if (workbook.getNumberOfSheets() <= VALIDATION_SHEET) {*/
            System.out.println("File " + file.getAbsolutePath() + " has wrong number of sheets");
        }

        // Getting the Sheet at index 1
        Sheet sheet = workbook.getSheetAt(validationSheet);/*Sheet sheet = workbook.getSheetAt(VALIDATION_SHEET);*/

        // 1. You can obtain a rowIterator and columnIterator and iterate over them
        //System.out.println("Processing file " + file.getAbsolutePath());

        Iterator<Row> rowIterator = sheet.rowIterator();

        boolean process = false;

        double partialTruePositiveCount= 0;
        double partialTrueNegativeCount= 0;
        double partialFalsePositiveCount= 0;
        double partialFalsePositiveContextCount= 0;
        double partialFalsePositiveRealCount= 0;
        double partialFalsePositiveErrorCount=0;
        double partialFalseNegativeCount= 0;
        double partialFalseNegativeMetamapCount= 0;
        double partialFalseNegativeTvpCount= 0;
        double partialTotal = 0;

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            Cell tvpCell = row.getCell(TVP_COL_NUM);
//            System.out.println("CELDA TVP TEST value empty: " + ((tvpCell==null)?"NULO":"NO NULO"));
//            System.out.println("CELDA TVP?: "+tvpCell.getStringCellValue().trim());
            String tvpCellValue = ((tvpCell==null)?"":tvpCell.getStringCellValue().trim().toUpperCase());

            if (!process) {
                process = tvpCellValue.equals(TVP_COL_NAME);
            }
            else if (tvpCellValue.equals("")) {
                continue;
            }
            else {
                Cell relevantCell = row.getCell(RELEVANT_COL_NUM);

                String relevantCellValue = relevantCell.getStringCellValue().trim().toUpperCase();

                // Using redundant checkings to avoid typos

                if (tvpCellValue.equals("YES")) {
                    if (relevantCellValue.equals("YES")) {
                        partialTruePositiveCount++;
                    } else if (relevantCellValue.startsWith("FPC")) {//FPCONTEXT
                        partialFalsePositiveContextCount++;
                    } else if (relevantCellValue.startsWith("FPR")) {//FPREAL
                        partialFalsePositiveRealCount++;
                    } else if (relevantCellValue.equals("NO")) {
                        partialFalsePositiveErrorCount++;//SI ES MAYOR A UNO HAY UN ERROR
                    }
                }
                else if (tvpCellValue.equals("NO")) {
                    if (relevantCellValue.equals("NO")) {
                        partialTrueNegativeCount++;
                    } else if (relevantCellValue.equals("YES")) {
                        partialFalseNegativeTvpCount++;
                    } else if (relevantCellValue.equals("FN")) {
                        partialFalseNegativeMetamapCount++;
                    }
                }


                partialTotal++;
            }
        }
        
        validate(
                partialTruePositiveCount, partialTrueNegativeCount, partialFalsePositiveCount, 
                partialFalsePositiveContextCount, partialFalsePositiveRealCount, partialFalsePositiveErrorCount,
                partialFalseNegativeCount,
                partialFalseNegativeMetamapCount, partialFalseNegativeTvpCount,
                partialTotal);

        //System.out.println("\tDoc total:" + partialTotal);

        globalTruePositiveCount+=partialTruePositiveCount;
        globalTrueNegativeCount+=partialTrueNegativeCount;
        globalFalsePositiveCount+=(partialFalsePositiveContextCount + partialFalsePositiveRealCount);/*partialFalsePositiveCount*/
        globalFalsePositiveContextCount+=partialFalsePositiveContextCount;
        globalFalsePositiveRealCount+=partialFalsePositiveRealCount;
        globalFalsePositiveErrorCount+=partialFalsePositiveErrorCount;
        globalFalseNegativeCount+=(partialFalseNegativeMetamapCount + partialFalseNegativeTvpCount);/*partialFalseNegativeCount*/
        globalFalseNegativeMetamapCount+=partialFalseNegativeMetamapCount;
        globalFalseNegativeTvpCount+=partialFalseNegativeTvpCount;
        globalTotal+=partialTotal;

        // Closing the workbook
        workbook.close();
    }
    
    private boolean validate(
            double truePositiveCount, Double trueNegativeCount, Double falsePositiveCount,
            double falsePositiveContextCount, Double falsePositiveRealCount, Double falsePositiveErrorCount,
            double falseNegativeCount,
            double falseNegativeMetamapCount, Double falseNegativeTvpCount,
            double total) {
        
        double expectedTotal = truePositiveCount + trueNegativeCount
                + falsePositiveContextCount + falsePositiveRealCount
                + falseNegativeMetamapCount + falseNegativeTvpCount;/*falseNegativeCount*/

        if (falsePositiveErrorCount>0){
            System.out.println("ERROR: " + falsePositiveErrorCount);
        }


        if (expectedTotal != total) {
            System.out.println(truePositiveCount + " + " + trueNegativeCount + " + " +
                    falsePositiveCount + " + " + falsePositiveContextCount + " + " +
                    falsePositiveRealCount + " + " + falseNegativeMetamapCount + " + " + falseNegativeTvpCount);
            System.out.println("\tERROR: Expected total " + expectedTotal + " does not match total " + total);

            return false;
        }

        //TP=15.0; TN=9.0; FP=0.0; FPCONTEXT=7.0; FPREAL=2.0; FNTVP=2.0; FNMETAMAP=6.0; TOTAL=41.0; PRECISION=0.625; NPV=0.5294117647058824; RECALL=0.6521739130434783; SPEC=0.5


        Double precision =
                truePositiveCount / (truePositiveCount + (falsePositiveRealCount + falsePositiveContextCount) );
        precision = precision.isNaN()?0:precision;

        Double precision_FPREAL = truePositiveCount / ( truePositiveCount + falsePositiveRealCount);
        precision_FPREAL = precision_FPREAL.isNaN()?0:precision_FPREAL;
        Double precision_FPCONTEXT = truePositiveCount / ( truePositiveCount + falsePositiveContextCount);
        precision_FPCONTEXT = precision_FPCONTEXT.isNaN()?0:precision_FPCONTEXT;

        Double recallMetamap = truePositiveCount / (truePositiveCount + falseNegativeMetamapCount);
        recallMetamap = recallMetamap.isNaN()?0:recallMetamap;
        Double recallTvp = truePositiveCount / (truePositiveCount + falseNegativeTvpCount);
        recallTvp = recallTvp.isNaN()?0:recallTvp;
        Double recall = truePositiveCount / (truePositiveCount + (falseNegativeMetamapCount + falseNegativeTvpCount));
        recall = recall.isNaN()?0:recall;
//        System.out.println("        recall = "+truePositiveCount +"/ ("+truePositiveCount +"+ ("+falseNegativeMetamapCount +"+"+ falseNegativeTvpCount+"))");

        Double f1Score = 2 * ( (precision * recall) / (precision + recall) );
        f1Score = f1Score.isNaN()?0:f1Score;

        System.out.println(
//                " TP="
                ";"
                        + truePositiveCount + ";"
//                + " TN="
                        + trueNegativeCount + ";"
//                + " FP="
                        + (falsePositiveContextCount + falsePositiveRealCount) + ";"
//                + " FPCONTEXT="
                        + falsePositiveContextCount + ";"
//                + " FPREAL="
                        + falsePositiveRealCount + ";"
//                + " FN="
                        + (falseNegativeMetamapCount + falseNegativeTvpCount) + ";"
//                + " FNTVP="
                        + falseNegativeTvpCount + ";"
//                + " FNMETAMAP="
                        + falseNegativeMetamapCount + ";"
//                + " TOTAL="
                        + total + ";"
//                + " PRECISION: "
                        + precision + ";"
//                + " ADDITIONAL MEASURES: "
//                + " PRECISION_FPREAL: "
                        + precision_FPREAL + ";"
//                + " PRECISION_REPCONTEXT: "
                        + precision_FPCONTEXT + ";"
//                + " RECALL_METAMAP: "
                        + recallMetamap + ";"
//                + " RECALL_TVP: "
                        + recallTvp + ";"
//                + " RECALL: "
                        + recall + ";"
//                + " F1_SCORE: "
                        + f1Score + ";"
        );

        return true;
    }



}
