package com.neomind.fusion.custom.tecnoperfil.ccv;





import java.util.List;

import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.persist.PersistEngine;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;
import com.neomind.fusion.workflow.adapter.AdapterUtils;

public class CopiarItens implements AdapterInterface
{
	
	
	
	
	@Override
	public void start(Task arg0, EntityWrapper wrapper, Activity arg2)
	{
		try {
		
		String oc =	wrapper.findGenericValue("InformeAOrdemDeCompra");
		System.out.println(oc);
		
		
		List<NeoObject> listaordemcompra = wrapper.findGenericValue("AcrecentaAsLinhas");
		
	        
		
			for ( NeoObject Listaordemdecompras : listaordemcompra) {
				EntityWrapper listacompraWrapper = new EntityWrapper(Listaordemdecompras);
				String codigoitemoc = listacompraWrapper.findGenericValue("CodigoItem");
				String dscritemoc = listacompraWrapper.findGenericValue("DescricaoDoItem");
				
				
				
				List<NeoObject> itens = wrapper.findGenericValue("Itens");
				NeoObject itenspedido = AdapterUtils.createNewEntityInstance("ItenPed");
				EntityWrapper wrapperitenspedido = new EntityWrapper(itenspedido);
				wrapperitenspedido.setValue("CodItem", codigoitemoc);
				wrapperitenspedido.setValue("DesProd", dscritemoc);
				itens.add(itenspedido);
				
				
				PersistEngine.persist(itenspedido);

			}
		
		} catch (Exception e) {
			System.out.println("Error Ao Copiar Dados da OC para Itens do Pedido");  
			e.getMessage();
			
		}
		

	}

	@Override
	public void back(EntityWrapper arg0, Activity arg1)
	{
		// TODO Auto-generated method stub
		
	}

}
