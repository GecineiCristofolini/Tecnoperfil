package com.neomind.fusion.custom.tecnoperfil.totvs;

public class Item
{
  private String Codigo;
  private String versao;
  
  
  
  
  public Item()
  {

  	
  }
   

  
  
@Override
public String toString()
{
	return "Item [Codigo=" + Codigo + "]";
}




public String getCodigo()
{
	return Codigo;
}




public void setCodigo(String codigo)
{
	Codigo = codigo;
}




public Item(String codigo)
{

	Codigo = codigo;
}




public String getVersao()
{
	return versao;
}




public void setVersao(String versao)
{
	this.versao = versao;
}


  
}
