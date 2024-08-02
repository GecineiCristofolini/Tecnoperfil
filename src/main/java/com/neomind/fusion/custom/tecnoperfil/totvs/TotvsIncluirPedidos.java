package com.neomind.fusion.custom.tecnoperfil.totvs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.neomind.fusion.custom.tecnoperfil.totvs.entidades.Itens;
import com.neomind.fusion.custom.tecnoperfil.totvs.entidades.Order;
import com.neomind.fusion.custom.tecnoperfil.totvs.entidades.Pedido;
import com.neomind.fusion.custom.tecnoperfil.totvs.entidades.Vendedor;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;
import com.neomind.fusion.workflow.exception.WorkflowException;

public class TotvsIncluirPedidos implements AdapterInterface
{
	
	private static final Log log = LogFactory.getLog(TotvsIncluirPedidos.class);

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
			log.debug("Iniciar inclusao de pedido");
			
			String json = IncluirPedido();
			log.debug("informacoes retornadas + " + json);
			IncluirPedido incluirPedido = new IncluirPedido();
			incluirPedido.IntegracaoIncluirPedido(json);
			log.debug("Pedido incluído com sucesso.");

		}
		catch (WorkflowException e)
		{
			e.printStackTrace();
			log.error("Erro incluir pedido", e);
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Erro ao incluir pedido", e);
			throw new WorkflowException("Erro ao incluir pedido" + e.getCause());
		}

	}

	public String IncluirPedido()
	{

		try
		{

			Pedido pedidototvs = new Pedido();

			List<Order> ListPedido = new ArrayList<Order>();

			Order ordertotvs = new Order();

			ordertotvs.setIdClienteFusion("120025");
			ordertotvs.setC5_EMISSAO("20240801");
			ordertotvs.setC5_ZFUSION("8.123456");
			ordertotvs.setC5_CONDPAG("001");
			ordertotvs.setC5_TABELA("");
			ordertotvs.setC5_TRANSP("000001");
			ordertotvs.setC5_FRETE(23.89);
			ordertotvs.setC5_SEGURO(7.00);
			ordertotvs.setC5_REDESP("");
			ordertotvs.setC5_ZMENNF("Teste de Mensagem");
			ordertotvs.setC5_MOEDA("1");
			ordertotvs.setC5_TIPLIB("2");
			ordertotvs.setC5_TPFRETE("C");

			List<Vendedor> listvendedor = new ArrayList<Vendedor>();
			Vendedor vendedores = new Vendedor();
			vendedores.setVendedor("104422");
			vendedores.setAliquotaComissao(10.00);
			Vendedor vendedores2 = new Vendedor();
			vendedores2.setVendedor("24954");
			vendedores2.setAliquotaComissao(10.08);

			listvendedor.add(vendedores);
			listvendedor.add(vendedores2);

			ordertotvs.setVendedores(listvendedor);

			List<Itens> listitens = new ArrayList<Itens>();
			Itens itens = new Itens();
			itens.setC6_ITEM("1");
			itens.setC6_PRODUTO("00000002344");
			itens.setC6_QTDVEN(50.23);
			itens.setC6_PRCVEN(1.89);
			itens.setC6_VALOR(34.00);
			itens.setC6_OPER("1");
			itens.setC6_ENTREG("20240814");
			itens.setC6_NUMPCOM("");
			itens.setC6_ITEMPC("");

			listitens.add(itens);

			ordertotvs.setItens(listitens);

			ListPedido.add(ordertotvs);

			pedidototvs.setOrder(ListPedido);

			String jsonped = new Gson().toJson(pedidototvs);

			return jsonped;
			


		}
		catch (Exception e)
		{

			System.out.print(e.getMessage());
			e.printStackTrace();
			throw new WorkflowException("Erro ao incluir pedido" + e.getCause());

		}

	}

}
