/*
 * Created on Mar 16, 2005
 *
 */
package com.anacleto.parsing.filetype;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.anacleto.hierarchy.BookPage;
import com.anacleto.parsing.ParserException;

/**
 * Class to read Excel files. All content found in the file will be added
 * in the content field of the indexEntity
 * Based on the jakarta POI project 
 * @author robi
 */
public class ExcelParser implements FileTypeParser{

    public void processStream(InputStream in, BookPage page) throws ParserException{
        try {
            
            POIFSFileSystem fs = new POIFSFileSystem( in );
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            
            int sheetNo = wb.getNumberOfSheets();
            for (int i = 0; i < sheetNo; i++) {
                HSSFSheet sheet = wb.getSheetAt(i);

                //process content per sheet:
                String sheetContent = processSheet(sheet);
                page.addTextField("content", sheetContent);
            }
        } catch ( IOException e ) {
            throw new ParserException(e);
        }
    }
    
    /**
     * 
     */
    private String processSheet(HSSFSheet sheet) {
        String retStr = "";
        Iterator rows = sheet.rowIterator(); 
        while( rows.hasNext() ) {           
            HSSFRow row = (HSSFRow) rows.next();

            // Iterate over each cell in the row and print out the cell's content
            Iterator cells = row.cellIterator();
            while( cells.hasNext() ) {
                HSSFCell cell = (HSSFCell) cells.next();
                switch ( cell.getCellType() ) {
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        retStr = retStr + String.valueOf( cell.getNumericCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_STRING: 
                        retStr = retStr + cell.getStringCellValue();
                        break;
                    default:
                        System.out.println( "unsuported cell type" );
                        break;
                }
            }
            
        }
        return retStr;
    }
}

