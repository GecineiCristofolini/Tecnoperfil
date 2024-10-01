package com.neomind.fusion.custom.tecnoperfil.industrial;

import java.util.List;

import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;

public class ApagaHistoricoAnaliseIND implements AdapterInterface
{

	

	@Override
	public void back(EntityWrapper arg0, Activity arg1)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(Task arg0, EntityWrapper arg1, Activity arg2)
	{
		try
		{

			List<NeoObject> listaHistorico = arg1.findGenericValue("WPedInd.HistoricosDeAprovacao");
			listaHistorico.clear();
			   
		}catch (Exception e) {
			System.out.print(e.getMessage());
		}
		
	}
	
}
