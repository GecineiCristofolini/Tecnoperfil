package com.neomind.fusion.custom.tecnoperfil.totvs;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;
import com.neomind.fusion.workflow.exception.WorkflowException;

public class TotvsAnaliseFinanceiraEXP implements AdapterInterface
{

	private static final Log log = LogFactory.getLog(TotvsAnaliseFinanceiraEXP.class);

	@Override
	public void start(Task arg0, EntityWrapper wrapercliente, Activity arg2)
	{
		try
		{
			log.debug("Iniciar busca de info");

			String idcliente = wrapercliente.findGenericValue("CodCHDP");

			AnaliseFinanceira analisefina = new AnaliseFinanceira();
			log.debug("Iniciando cadastro do cliente");
			JSONObject dadosFinanceiros = analisefina.Analise(idcliente);

			log.debug("JSON retornado : " + dadosFinanceiros.toString());

			//BigDecimal creditLimit =  (BigDecimal) dadosFinanceiros.get("CreditLimit");
			//BigDecimal creditLimitUsedByBilling =  (BigDecimal) dadosFinanceiros.get("CreditLimitUsedByBilling");
			//BigDecimal creditLimitUsedBySalesOrders =  (BigDecimal) dadosFinanceiros.get("CreditLimitUsedBySalesOrders");
			BigDecimal totalCreditLimit = BigDecimal
					.valueOf(dadosFinanceiros.getDouble("TotalCreditLimit"));
			BigDecimal creditLimitUsedByOrdersAndFinance = BigDecimal
					.valueOf(dadosFinanceiros.getDouble("CreditLimitUsedByOrdersAndFinance"));
			String limitDate = dadosFinanceiros.getString("LimitDate");

			String formattedDate = "";

			if (limitDate != null && !limitDate.contains("    -  -  "))
			{
				String ano = limitDate.substring(0, 4);
				String mes = limitDate.substring(5, 7);
				String dia = limitDate.substring(8, 10);

				formattedDate = dia + "/" + mes + "/" + ano;

			}

			wrapercliente.findField("VlcredHDP").setValue(totalCreditLimit);
			wrapercliente.findField("VlcoreHDP").setValue(creditLimitUsedByOrdersAndFinance);
			wrapercliente.findField("DataVencimento").setValue(formattedDate);

			String inadimplente = "N";
			wrapercliente.findField("InadiHDP").setValue(inadimplente);

			if (formattedDate != null && !formattedDate.equals(""))
			{
				System.out.println("limitDate");
				LocalDate limitLocalDate = LocalDate.parse(limitDate);
				LocalDate today = LocalDate.now(); // Data de hoje

				if (limitLocalDate.isBefore(today))
				{

					inadimplente = "S";
					wrapercliente.findField("InadiHDP").setValue(inadimplente);

				}

			}

			LocalDateTime Timehoje = LocalDateTime.now();
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			String datahoje = Timehoje.format(fmt);

			wrapercliente.findField("DatAFHDP").setValue(datahoje);

		}
		catch (WorkflowException e)
		{
			e.printStackTrace();
			log.error("Erro ao Analisear Cliente", e);
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Erro ao iniciar atendimento", e);
			throw new WorkflowException("Erro ao iniciar atendimento" + e.getCause());
		}

	}

	@Override
	public void back(EntityWrapper arg0, Activity arg1)
	{
		// TODO Auto-generated method stub

	}

}
