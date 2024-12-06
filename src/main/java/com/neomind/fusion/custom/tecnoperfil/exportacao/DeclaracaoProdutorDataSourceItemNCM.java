package com.neomind.fusion.custom.tecnoperfil.exportacao;

public class DeclaracaoProdutorDataSourceItemNCM
{

	private String ncm;
	private String descrincm;
	private String valor;
	
	public DeclaracaoProdutorDataSourceItemNCM(){
		
	}

	public DeclaracaoProdutorDataSourceItemNCM(String ncm, String descrincm, String valor)
	{

		this.ncm = ncm;
		this.descrincm = descrincm;
		this.valor = valor;
	}

	public String getNcm()
	{
		return ncm;
	}

	public String getDescrincm()
	{
		return descrincm;
	}

	public String getValor()
	{
		return valor;
	}

	public void setNcm(String ncm)
	{
		this.ncm = ncm;
	}

	public void setDescrincm(String descrincm)
	{
		this.descrincm = descrincm;
	}

	public void setValor(String valor)
	{
		this.valor = valor;
	}

}