package com.neomind.fusion.custom.tecnoperfil.hidroponia;

import java.util.List;

import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;

public class ApagaHistoricoAnaliseHDP implements AdapterInterface
{

	public void start(Task arg0, EntityWrapper wrapper, Activity arg2)
	{

		try
		{

			List<NeoObject> listaHistorico = wrapper.findGenericValue("HistoricoDeAprovacaoFic");
			listaHistorico.clear();
	
		}catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}

	@Override
	public void back(EntityWrapper arg0, Activity arg1)
	{
		// TODO Auto-generated method stub
		
	}
	
}
