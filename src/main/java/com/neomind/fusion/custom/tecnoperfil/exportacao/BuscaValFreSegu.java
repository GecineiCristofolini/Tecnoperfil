package com.neomind.fusion.custom.tecnoperfil.exportacao;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.persist.PersistEngine;
import com.neomind.fusion.persist.QLEqualsFilter;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;
import com.neomind.fusion.workflow.adapter.AdapterUtils;

// BUscar valor do frete e seguro

public class BuscaValFreSegu implements AdapterInterface
{

	@SuppressWarnings("deprecation")
	@Override
	public void start(Task arg0, EntityWrapper PedidoEW, Activity arg2)
	{

		try
		{

			BigDecimal valorfrete = BigDecimal.ZERO;
			BigDecimal valorseguro = BigDecimal.ZERO;
			BigDecimal valormerc = PedidoEW.findGenericValue("ValorMercadoria");
			System.out.println(valormerc);
			BigDecimal valortotal = BigDecimal.ZERO;
			Set<String> listapedido = new HashSet<>();
			

			//lista o pedido do adicionaPedido

			List<NeoObject> itensfusion = PedidoEW.findGenericValue("AdicionaPedido");
			for (NeoObject itensPedido : itensfusion)
			{

				EntityWrapper itensPedidoWrapper = new EntityWrapper(itensPedido);

				String NumeroPedido = itensPedidoWrapper.findGenericValue("PedidoFusion");

								
				listapedido.add(NumeroPedido);

				

			}

			for (String buscafrete : listapedido)
			{

				String pedido = buscafrete;
				
				NeoObject pedexp = PersistEngine.getObject(AdapterUtils.getEntityClass("PedExp"),
						new QLEqualsFilter("NumPed", pedido));
				EntityWrapper pedexpEW = new EntityWrapper(pedexp);
				BigDecimal buscavalrofrete = pedexpEW.findGenericValue("ValorDoFrete");
				BigDecimal buscavalorseguro = pedexpEW.findGenericValue("ValorDoSeguro");
				
				valorfrete = valorfrete.add(buscavalrofrete);
				valorseguro = valorseguro.add(buscavalorseguro);
				
				
				
			}

			valortotal  = ((valormerc).add(valorfrete).add(valorseguro)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			
						
						
						
			 		   PedidoEW.setValue("ValorFrete",valorfrete);
				       PedidoEW.setValue("ValorSeguro",valorseguro);
		  		       PedidoEW.setValue("ValorTotal",valortotal);

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
