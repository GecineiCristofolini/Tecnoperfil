package com.neomind.fusion.custom.tecnoperfil.testes;

import java.io.IOException;

import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

public class UtilTexto
{
	/**
	 * Le o conteudo (texto) de um arquivo pdf
	 *
	 */
	public static String extraiTextoDoPDF(String caminho)

	{
		PDDocument pdfDocument = null;

		try
		{

			pdfDocument = PDDocument.load(caminho);

			PDFTextStripper stripper = new PDFTextStripper();

			String texto = stripper.getText(pdfDocument);

			return texto;
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);

		}
		finally
		{
			if (pdfDocument != null)
				try
				{
					pdfDocument.close();
				}
				catch (IOException e)
				{
					throw new RuntimeException(e);
				}
		}
	}

	/**
	 *
	 * Extrai o conteudo do arquivo indicado
	 *
	 */
	public static void main(String[] args)
	{
		String caminho = "C:\\Java11\\MARCOS.pdf";
		String texto = extraiTextoDoPDF(caminho);
		System.out.println(texto);
		
		
		
				
        
	}				


	
}
