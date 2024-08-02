package com.neomind.fusion.custom.tecnoperfil.testes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.neomind.fusion.doc.NeoFile;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;
import com.neomind.fusion.workflow.exception.WorkflowException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class AdapterLerArquivos implements AdapterInterface
{

	private static final Logger log = LoggerFactory.getLogger(AdapterLerArquivos.class);

	@Override
	public void back(EntityWrapper arg0, Activity arg1)
	{
		
	}

	@Override
	public void start(Task arg0, EntityWrapper arg1, Activity arg2)
	{
		Workbook excel = null;
		try
		{
			NeoFile arquivo = arg1.findGenericValue("AnexarArquivo");
			WorkbookSettings encode = new WorkbookSettings();
			encode.setEncoding("ISO-8859-1");
			excel = Workbook.getWorkbook(arquivo.getAsFile(), encode);
			Sheet planilha1 = excel.getSheet(0);
			int nroLinhas = planilha1.getRows();

			for (int i = 4; i < nroLinhas; i++)
			{
				String numero = planilha1.getCell(0, i).getContents();
				arg1.findField("Numero").setValue(numero);

				String dataEntrega = planilha1.getCell(1, i).getContents();
				GregorianCalendar dataEntregaFusion = parseDate(dataEntrega);
				arg1.findField("DataEntrega").setValue(dataEntregaFusion);

				String dataEmissao = planilha1.getCell(2, i).getContents();
				GregorianCalendar dataEmissaoFusion = parseDate(dataEmissao);
				arg1.findField("DataEmissao").setValue(dataEmissaoFusion);
			}
		}
		catch (BiffException e)
		{
			log.error("Erro ao processar o arquivo Excel: {}", e.getMessage(), e);
			e.printStackTrace();
			throw new WorkflowException("Erro na leitura do arquivo: " + e.getMessage());
		}
		catch (IOException e)
		{
			log.error("Erro ao ler o arquivo: {}", e.getMessage(), e);
			e.printStackTrace();
			throw new WorkflowException("Erro na leitura do arquivo: " + e.getMessage());
		}
		catch (ParseException e)
		{
			log.error("Erro ao parsear data: {}", e.getMessage(), e);
			e.printStackTrace();
			throw new WorkflowException("Erro ao parsear data: " + e.getMessage());
		}
		finally
		{
			if (excel != null)
			{
				excel.close();
			}
		}
	}

	private GregorianCalendar parseDate(String dateString) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
		Date date = sdf.parse(dateString);
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar;
	}
}
