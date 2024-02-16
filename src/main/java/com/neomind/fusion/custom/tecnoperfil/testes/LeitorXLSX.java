package com.neomind.fusion.custom.tecnoperfil.testes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class LeitorXLSX {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        FileInputStream fisPlanilha = null;
        
        List<String> Lista = new ArrayList<String>();
        

        try {
            File file = new File("C:\\Java11\\TesteTI.xlsx");
            fisPlanilha = new FileInputStream(file);

            //cria um workbook = planilha toda com todas as abas
            XSSFWorkbook workbook = new XSSFWorkbook(fisPlanilha);
            

            //recuperamos apenas a primeira aba ou primeira planilha
            XSSFSheet sheet = workbook.getSheetAt(0);
            

            //retorna todas as linhas da planilha 0 (aba 1)
            Iterator<Row> rowIterator = sheet.iterator();

            //varre todas as linhas da planilha 0
            while (rowIterator.hasNext()) {

                //recebe cada linha da planilha
                Row row = rowIterator.next();

                //pegamos todas as celulas desta linha
                Iterator<Cell> cellIterator = row.iterator();

                //varremos todas as celulas da linha atual
                
                while (cellIterator.hasNext()) {

                    //criamos uma celula
                    Cell cell = cellIterator.next();
                    
                    System.out.println(cell.toString());
                   
                 
                   
                    
                   if(cell.getRowIndex() >3){
                   
                	   
                    
                    switch (cell.getColumnIndex()) {
                    	
                        case 0: 
                        	
                            System.out.print(cell.getStringCellValue());
                            
                            break;
                            

                        case 1: 
                        	
                            System.out.print(cell.getStringCellValue());
                            
                            break;
                        
                            
                        } 
                       
                   }

                        //case 1:
                          //  System.out.println(cell.getStringCellValue());
                          //  break;
                            
                       
                  

                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(LeitorXLSX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LeitorXLSX.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fisPlanilha.close();
            } catch (IOException ex) {
                Logger.getLogger(LeitorXLSX.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
