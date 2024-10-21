package com.neomind.fusion.custom.tecnoperfil.ccv;

import java.util.List;

import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;

public class Atualizacomissao
{

	public void start(Task arg0, EntityWrapper wrapper, Activity arg2)
	{

		try
		{

			List<NeoObject> listaHistorico = wrapper.findGenericValue("WPedido.HistoricosDeAprovacao");
			listaHistorico.clear();
			
	
		}catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}
	
}
