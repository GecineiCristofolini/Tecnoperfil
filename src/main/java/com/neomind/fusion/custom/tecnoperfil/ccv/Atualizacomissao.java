package com.neomind.fusion.custom.tecnoperfil.ccv;

import java.math.BigDecimal;
import java.util.List;

import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;

public class Atualizacomissao implements AdapterInterface
{

	public void start(Task arg0, EntityWrapper wrapper, Activity arg2)
	{

		try
		{
			String Agentevenda = wrapper.findGenericValue("WPedido.CodigoVendedor.ACodRep");
			String Agentevendaentega = wrapper.findGenericValue("WPedido.AgevendaEng.ACodRep");
			String Agentetlv = wrapper.findGenericValue("WPedido.AgeVeCom.ACodRep");
			String Agentetlventrega = wrapper.findGenericValue("WPedido.AgenteTLVEntrega.ACodRep");

			if (Agentevenda == null || Agentevenda.isBlank())
			{
				Agentevenda = "";
			}

			if (Agentevendaentega == null || Agentevenda.isBlank())
			{
				Agentevendaentega = "";
			}

			if (Agentetlv == null || Agentevenda.isBlank())
			{
				Agentetlv = "";
			}

			if (Agentetlventrega == null || Agentevenda.isBlank())
			{
				Agentetlventrega = "";
			}

			List<NeoObject> itensfusion = wrapper.findGenericValue("WPedido.PItenPed");

			for (NeoObject itensPedido : itensfusion)
			{
				EntityWrapper itensPedidoWrapper = new EntityWrapper(itensPedido);

				BigDecimal comissaoincial = itensPedidoWrapper.findGenericValue("ComissaoInicial");
				BigDecimal divisor = new BigDecimal("2");


				BigDecimal comissoagente = BigDecimal.ZERO;
				BigDecimal comissoagenteentrega = BigDecimal.ZERO;
				BigDecimal comissaotlv = BigDecimal.ZERO;
				BigDecimal comissatlventrega = BigDecimal.ZERO;
				
				String Agentevendaitem = Agentevenda;
				String Agentevendaentegaitem = Agentevendaentega ;
				String Agentetlvitem = Agentetlv;
				String Agentetlventregaitem = Agentetlventrega;
				
				
				if (Agentevendaentegaitem == "" || Agentevendaentegaitem.isBlank()
						|| Agentevendaitem.equals(Agentevendaentegaitem))
				{
					comissoagente = comissaoincial;
				}
				else
				{

					comissoagente = comissaoincial.divide(divisor);

				}

				itensPedidoWrapper.setValue("Agenterv", Agentevendaitem);
				itensPedidoWrapper.setValue("ComissaoAgenteRV", comissoagente);

				if (Agentevendaentegaitem == "" || Agentevendaentegaitem.isBlank()|| Agentevendaitem.equals(Agentevendaentegaitem))
				{
					comissoagenteentrega = BigDecimal.ZERO;
					Agentevendaentegaitem = "";

				}
				else
				{
					if (!Agentevendaitem.equals(Agentevendaentegaitem))
					{

						comissoagenteentrega = comissaoincial.divide(divisor);

					}
					else
					{

						comissoagenteentrega = comissaoincial;

					}
				}

				itensPedidoWrapper.setValue("AgenteEntregaRV", Agentevendaentegaitem);
				itensPedidoWrapper.setValue("ComissaoAgenteEntrega", comissoagenteentrega);
				
				if (Agentetlventregaitem == "" || Agentetlventregaitem.isBlank())
				{
					comissaotlv = BigDecimal.ZERO;
					Agentetlvitem = "";
				}	

				itensPedidoWrapper.setValue("AgenteTLV", Agentetlvitem);
				itensPedidoWrapper.setValue("ComissaoAgenteTLV", comissaotlv);
					

				if (Agentetlventregaitem == "" || Agentetlventregaitem.isBlank() ||Agentetlventregaitem.equals(Agentetlvitem) )
				{
					comissatlventrega = BigDecimal.ZERO;
					Agentetlventregaitem = "";
				}

				itensPedidoWrapper.setValue("AgenteEntregaTLV", Agentetlventregaitem);
				itensPedidoWrapper.setValue("ComissaoEntregatlv", comissatlventrega);

			

			}
			
			

		}
		catch (Exception e)
		{
			System.out.print(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void back(EntityWrapper arg0, Activity arg1)
	{
		// TODO Auto-generated method stub
		
	}

}
