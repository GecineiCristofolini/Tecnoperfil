package com.neomind.fusion.custom.tecnoperfil.exportacao;

import java.util.Calendar;

import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.persist.PersistEngine;
import com.neomind.fusion.persist.QLEqualsFilter;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;
import com.neomind.fusion.workflow.adapter.AdapterUtils;

// Esta Classe serve Para Sequenciar o numero da invoice

public class SequenciadorInvoice implements AdapterInterface
{

	@Override
	public void start(Task arg0, EntityWrapper SeqEW, Activity arg2)
	{

		try
		{

			String invoice = SeqEW.findGenericValue("Invoice");
			
			if(invoice.isEmpty() || invoice.equals("")|| invoice == null) {
			
			
			
			
			// Primeiror acha a tabela que esta o sequenciador formulario  ExpSeqProInvo a invoice
			NeoObject linvoice = PersistEngine.getObject(AdapterUtils.getEntityClass("ExpSeqProInvo"),
					new QLEqualsFilter("Nome", "Invoice"));

			EntityWrapper invoiceEW = new EntityWrapper(linvoice);

			String Sequencia = invoiceEW.findGenericValue("Sequencia");
			String Sigla = invoiceEW.findGenericValue("Sigla");
			String ano = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			invoice = Sigla + " " + Sequencia + "-" + ano;
			SeqEW.setValue("Invoice", invoice);
			long numero = Long.parseLong(Sequencia);
			numero = numero +1;
			String novasequencia = Long.toString(numero);
			invoiceEW.setValue("Sequencia", novasequencia);
			}

			}
		catch (Exception e)
		{
			System.out.println("Erro ao Pegar a mensagem" + e.getMessage());
		}
	}

	@Override
	public void back(EntityWrapper arg0, Activity arg1)
	{
		// TODO Auto-generated method stub

	}

}
