package com.neomind.fusion.custom.tecnoperfil.sadt;

import java.util.List;

import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.persist.PersistEngine;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;
import com.neomind.fusion.workflow.adapter.AdapterUtils;

// Esta Classe serve Para Sequenciar o numero 

public class SequenciadorNumero implements AdapterInterface
{

	@Override
	public void start(Task arg0, EntityWrapper arg1, Activity arg2)
	{

		try
		{

			
			    String seq = arg1.findGenericValue("Numero");
			
				// Primeiror acha a tabela que esta o sequenciador
				List<NeoObject> sequenciador = PersistEngine
						.getObjects(AdapterUtils.getEntityClass("SADTSequenciadorNumero"));
				
				

				for (NeoObject neosec : sequenciador)
				{

					EntityWrapper wrappersequenciador = new EntityWrapper(neosec);

					long numero = (long) wrappersequenciador.findField("Numero").getValue();
					seq = Long.toString(numero);
					numero = numero + 1;

					wrappersequenciador.setValue("Numero", numero);
					
					PersistEngine.persist(sequenciador);
				}
				
				arg1.setValue("Numero", seq);
				
				
			
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
