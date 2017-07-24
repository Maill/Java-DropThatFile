package DropThatFile.pluginManager.plugins.excelPreviewer;

import DropThatFile.pluginManager.IPreviewable;
import org.apache.poi.ss.usermodel.*;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * Created by Olivier on 09/05/2017.
 */
public class ExcelFilePlugin extends Plugin {
    public ExcelFilePlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class ExcelFilePreviewer implements IPreviewable {
        @Override
        public String getFileContent(File fileToPreview) throws IOException {
            StringBuilder sb = new StringBuilder();
            try {
                FileInputStream excelFile = new FileInputStream(fileToPreview.getAbsolutePath());
                Workbook workbook = new XSSFWorkbook(excelFile);
                Sheet dataTypeSheet = workbook.getSheetAt(0);

                for (Row currentRow : dataTypeSheet) {
                    for (Cell currentCell : currentRow) {
                        if (currentCell.getCellTypeEnum().equals(CellType.STRING))
                            sb.append(currentCell.getStringCellValue()).append("\t");
                        else if (currentCell.getCellTypeEnum().equals(CellType.NUMERIC))
                            sb.append(currentCell.getNumericCellValue()).append("\t");
                    }
                    sb.append("\n");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return sb.toString();
        }
    }
}