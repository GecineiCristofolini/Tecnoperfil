package com.neomind.fusion.custom.tecnoperfil.exportacao;

public class TecnoperfilItensInvoiceDataSource {
	
	private String item;
	private String quantidade;
	private String unidade;
	private String descricao;
	private String ferramenta;
	private String cor;
	private String ncm;
	private String codigoitem;
	private String valoUnitario;
	private String vltotal;
	
	
	public TecnoperfilItensInvoiceDataSource() {
		
	}


	public TecnoperfilItensInvoiceDataSource(String item, String quantidade, String unidade,
			String descricao, String ferramenta, String cor, String ncm, String codigoitem,
			String valoUnitario, String vltotal)
	{
		super();
		this.item = item;
		this.quantidade = quantidade;
		this.unidade = unidade;
		this.descricao = descricao;
		this.ferramenta = ferramenta;
		this.cor = cor;
		this.ncm = ncm;
		this.codigoitem = codigoitem;
		this.valoUnitario = valoUnitario;
		this.vltotal = vltotal;
	}


	public String getItem()
	{
		return item;
	}


	public void setItem(String item)
	{
		this.item = item;
	}


	public String getQuantidade()
	{
		return quantidade;
	}


	public void setQuantidade(String quantidade)
	{
		this.quantidade = quantidade;
	}


	public String getUnidade()
	{
		return unidade;
	}


	public void setUnidade(String unidade)
	{
		this.unidade = unidade;
	}


	public String getDescricao()
	{
		return descricao;
	}


	public void setDescricao(String descricao)
	{
		this.descricao = descricao;
	}


	public String getFerramenta()
	{
		return ferramenta;
	}


	public void setFerramenta(String ferramenta)
	{
		this.ferramenta = ferramenta;
	}


	public String getCor()
	{
		return cor;
	}


	public void setCor(String cor)
	{
		this.cor = cor;
	}


	public String getNcm()
	{
		return ncm;
	}


	public void setNcm(String ncm)
	{
		this.ncm = ncm;
	}


	public String getCodigoitem()
	{
		return codigoitem;
	}


	public void setCodigoitem(String codigoitem)
	{
		this.codigoitem = codigoitem;
	}


	public String getValoUnitario()
	{
		return valoUnitario;
	}


	public void setValoUnitario(String valoUnitario)
	{
		this.valoUnitario = valoUnitario;
	}


	public String getVltotal()
	{
		return vltotal;
	}


	public void setVltotal(String vltotal)
	{
		this.vltotal = vltotal;
	}


	@Override
	public String toString()
	{
		return "TecnoperfilItensInvoiceDataSource [item=" + item + ", quantidade=" + quantidade
				+ ", unidade=" + unidade + ", descricao=" + descricao + ", ferramenta=" + ferramenta
				+ ", cor=" + cor + ", ncm=" + ncm + ", codigoitem=" + codigoitem + ", valoUnitario="
				+ valoUnitario + ", vltotal=" + vltotal + "]";
	}

	

   


	
}