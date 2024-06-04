package com.neomind.fusion.custom.tecnoperfil.totvs;

import java.util.List;

public class CadCliente
{
 
	private String razaosocial;
	private String nomefantasia;
	private String idade;
	
	private List<Item> item;
	
	public CadCliente()
	{
		
	}

	
	
	
	


	public CadCliente(String razaosocial, String nomefantasia,String idade)
	{
		
		this.razaosocial = razaosocial;
		this.nomefantasia = nomefantasia;
		this.item = item;
	}







	public String getIdade()
	{
		return idade;
	}







	public void setIdade(String idade)
	{
		this.idade = idade;
	}







	public String getRazaosocial()
	{
		return razaosocial;
	}


	@Override
	public String toString()
	{
		return "CadCliente [razaosocial=" + razaosocial + ", nomefantasia=" + nomefantasia + "]";
	}




	public void setRazaosocial(String razaosocial)
	{
		this.razaosocial = razaosocial;
	}


	public String getNomefantasia()
	{
		return nomefantasia;
	}


	public void setNomefantasia(String nomefantasia)
	{
		this.nomefantasia = nomefantasia;
	}
	
	


	




	public List<Item> getItem()
	{
		return item;
	}







	public void setItem(List<Item> item)
	{
		this.item = item;
	}







	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nomefantasia == null) ? 0 : nomefantasia.hashCode());
		result = prime * result + ((razaosocial == null) ? 0 : razaosocial.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CadCliente other = (CadCliente) obj;
		if (nomefantasia == null)
		{
			if (other.nomefantasia != null)
				return false;
		}
		else if (!nomefantasia.equals(other.nomefantasia))
			return false;
		if (razaosocial == null)
		{
			if (other.razaosocial != null)
				return false;
		}
		else if (!razaosocial.equals(other.razaosocial))
			return false;
		return true;
	}







	




	



	
	
	
	
	
}
