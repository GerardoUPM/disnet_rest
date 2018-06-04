package edu.upm.midas.validator;

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

    public void validator(String validationFolder) throws IOException, InvalidFormatException {


        File dir = new File(validationFolder);

        File[] directoryListing = dir.listFiles();

        if (directoryListing != null) {
            for (File file : directoryListing) {
                try {
                    readExcel(file);
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
        
        double globalPrecision =
                globalTruePositiveCount / (globalTruePositiveCount + (globalFalsePositiveRealCount + globalFalsePositiveContextCount) );
        System.out.println("GLOBAL PRECISION: " + globalPrecision);
        System.out.println("GLOBAL RESULT: "
                + " TP: " + globalTruePositiveCount + ";"
                + " FP_REAL: " + globalFalsePositiveRealCount + ";"
                + " FP_CONTEXT: " + globalFalsePositiveContextCount + ";"
                + " FP: " + globalFalsePositiveCount + ";"
                + " TN: " + globalTrueNegativeCount + ";"
                + " FN_TVP: " + globalFalseNegativeTvpCount + ";"
                + " FN_METAMAP: " + globalFalseNegativeMetamapCount + ";"
                + " FN: " + globalFalseNegativeCount + ";"
                + " PRECISION: " + globalPrecision + ";"
        );
    }

    private void readExcel(File file) throws InvalidFormatException, IOException {


        System.out.println("Disease: " + file.getName());
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        Workbook workbook = WorkbookFactory.create(file);

        if (workbook.getNumberOfSheets() <= VALIDATION_SHEET) {
            System.out.println("File " + file.getAbsolutePath() + " has wrong number of sheets");
        }

        // Getting the Sheet at index 1
        Sheet sheet = workbook.getSheetAt(VALIDATION_SHEET);

        // 1. You can obtain a rowIterator and columnIterator and iterate over them
        System.out.println("Processing file " + file.getAbsolutePath());

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

            String tvpCellValue = tvpCell.getStringCellValue().trim().toUpperCase();

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
            double truePositiveCount, double trueNegativeCount, double falsePositiveCount, 
            double falsePositiveContextCount, double falsePositiveRealCount, double falsePositiveErrorCount,
            double falseNegativeCount,
            double falseNegativeMetamapCount, double falseNegativeTvpCount,
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

        return true;
    }



}
