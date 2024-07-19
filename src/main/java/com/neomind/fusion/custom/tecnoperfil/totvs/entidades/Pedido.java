package com.neomind.fusion.custom.tecnoperfil.totvs.entidades;

import java.util.ArrayList;
import java.util.List;

public class Pedido
{

	private List<Order> order =  new ArrayList<Order>();
	
	
	public List<Order> getOrder()
	{
		return order;
	
		
	}
	public void setOrder(List<Order> order)
	{
		this.order = order;
	}
	
	
	
	
}
