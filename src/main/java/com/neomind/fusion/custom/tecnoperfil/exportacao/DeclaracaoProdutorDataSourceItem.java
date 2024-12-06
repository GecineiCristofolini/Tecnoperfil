package com.neomind.fusion.custom.tecnoperfil.exportacao;

public class DeclaracaoProdutorDataSourceItem
{

	
	private String descricao;
	private String ferramenta;
	private String cor;
	
	public DeclaracaoProdutorDataSourceItem(String descricao, String ferramenta, String cor)
	{
		

		this.descricao = descricao;
		this.ferramenta = ferramenta;
		this.cor = cor;
	}

	public DeclaracaoProdutorDataSourceItem()
	{
		// TODO Auto-generated constructor stub
	}

	public String getDescricao()
	{
		return descricao;
	}

	public String getFerramenta()
	{
		return ferramenta;
	}

	public String getCor()
	{
		return cor;
	}

	public void setDescricao(String descricao)
	{
		this.descricao = descricao;
	}

	public void setFerramenta(String ferramenta)
	{
		this.ferramenta = ferramenta;
	}

	public void setCor(String cor)
	{
		this.cor = cor;
	}
	
	
	
	

}