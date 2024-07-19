package com.neomind.fusion.custom.tecnoperfil.totvs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.google.gson.Gson;
import com.neomind.fusion.custom.tecnoperfil.totvs.entidades.Itens;
import com.neomind.fusion.custom.tecnoperfil.totvs.entidades.Order;
import com.neomind.fusion.custom.tecnoperfil.totvs.entidades.Pedido;
import com.neomind.fusion.custom.tecnoperfil.totvs.entidades.Vendedor;

public class TotvsPedidoVenda
{

	public static void main(String[] args) throws JSONException {
		
		Pedido pedidototvs = new Pedido();
		
		List<Order> ListPedido = new ArrayList<Order>();
		
		Order ordertotvs = new Order(); 
		
		ordertotvs.setIdClienteFusion("123345");
		ordertotvs.setC5_EMISSAO("20240715");
		ordertotvs.setC5_ZFUSION("8.123456");
		ordertotvs.setC5_CONDPAG("021");
		ordertotvs.setC5_TABELA("");
		ordertotvs.setC5_TRANSP("2565");
		ordertotvs.setC5_FRETE(23.89);
		ordertotvs.setC5_SEGURO(0.00);
		ordertotvs.setC5_REDESP("");
		ordertotvs.setC5_ZMENNF("Teste de Mensagem");
		ordertotvs.setC5_MOEDA("1");
		ordertotvs.setC5_TIPLIB("2");
		ordertotvs.setC5_TPFRETE("C");
		
		List<Vendedor> listvendedor = new ArrayList<Vendedor>();
		Vendedor vendedores = new Vendedor();
		vendedores.setVendedor("95632");
		vendedores.setAliquotaComissao(10.00);
		Vendedor vendedores2 = new Vendedor();
		vendedores2.setVendedor("956334");
		vendedores2.setAliquotaComissao(10.08);
		
		listvendedor.add(vendedores);
		listvendedor.add(vendedores2);
		
		ordertotvs.setVendedores(listvendedor);
		
		List<Itens> listitens = new ArrayList<Itens>();
		Itens itens = new Itens();
		itens.setC6_ITEM("1");
		itens.setC6_PRODUTO("2421036000");
		itens.setC6_QTDVEN(50.23);
		itens.setC6_PRCVEN(1.89);
		itens.setC6_VALOR(34.00);
		itens.setC6_OPER("1");
		itens.setC6_ENTREG("20240714");
		itens.setC6_NUMPCOM("");
		itens.setC6_ITEMPC("");
		
		
		listitens.add(itens);
		
		ordertotvs.setItens(listitens);
		
		
		
		
		
		
		ListPedido.add(ordertotvs);
		
		pedidototvs.setOrder(ListPedido);
		
		
		
		
		
		String jsonped = new Gson().toJson(pedidototvs);
		
		System.out.println(jsonped.toString());
		
		
		
		
		
	}
	
	
	
}
