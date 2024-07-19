package com.neomind.fusion.custom.tecnoperfil.totvs.entidades;

import java.util.ArrayList;
import java.util.List;

public class Order
{

	private String idClienteFusion;
	private String C5_EMISSAO;
	private String C5_ZFUSION;
	private String C5_CONDPAG;
	private String C5_TABELA;
	private String C5_TRANSP;
	private Double C5_FRETE;
	private Double C5_SEGURO;
	private String C5_REDESP;
	private String C5_ZMENNF;
	private String C5_MOEDA;
	private String C5_TIPLIB;
	private String C5_TPFRETE;
	private List<Vendedor> vendedores = new ArrayList<Vendedor>();
	private List<Itens> itens = new ArrayList<Itens>();

	public String getIdClienteFusion()
	{
		return idClienteFusion;
	}

	public String getC5_EMISSAO()
	{
		return C5_EMISSAO;
	}

	public String getC5_ZFUSION()
	{
		return C5_ZFUSION;
	}

	public String getC5_CONDPAG()
	{
		return C5_CONDPAG;
	}

	public String getC5_TABELA()
	{
		return C5_TABELA;
	}

	public String getC5_TRANSP()
	{
		return C5_TRANSP;
	}

	public Double getC5_FRETE()
	{
		return C5_FRETE;
	}

	public Double getC5_SEGURO()
	{
		return C5_SEGURO;
	}

	public String getC5_REDESP()
	{
		return C5_REDESP;
	}

	public String getC5_ZMENNF()
	{
		return C5_ZMENNF;
	}

	public String getC5_MOEDA()
	{
		return C5_MOEDA;
	}

	public String getC5_TIPLIB()
	{
		return C5_TIPLIB;
	}

	public String getC5_TPFRETE()
	{
		return C5_TPFRETE;
	}

	public List<Vendedor> getVendedores()
	{
		return vendedores;
	}

	public List<Itens> getItens()
	{
		return itens;
	}

	public void setIdClienteFusion(String idClienteFusion)
	{
		this.idClienteFusion = idClienteFusion;
	}

	public void setC5_EMISSAO(String c5_EMISSAO)
	{
		C5_EMISSAO = c5_EMISSAO;
	}

	public void setC5_ZFUSION(String c5_ZFUSION)
	{
		C5_ZFUSION = c5_ZFUSION;
	}

	public void setC5_CONDPAG(String c5_CONDPAG)
	{
		C5_CONDPAG = c5_CONDPAG;
	}

	public void setC5_TABELA(String c5_TABELA)
	{
		C5_TABELA = c5_TABELA;
	}

	public void setC5_TRANSP(String c5_TRANSP)
	{
		C5_TRANSP = c5_TRANSP;
	}

	public void setC5_FRETE(Double c5_FRETE)
	{
		C5_FRETE = c5_FRETE;
	}

	public void setC5_SEGURO(Double c5_SEGURO)
	{
		C5_SEGURO = c5_SEGURO;
	}

	public void setC5_REDESP(String c5_REDESP)
	{
		C5_REDESP = c5_REDESP;
	}

	public void setC5_ZMENNF(String c5_ZMENNF)
	{
		C5_ZMENNF = c5_ZMENNF;
	}

	public void setC5_MOEDA(String c5_MOEDA)
	{
		C5_MOEDA = c5_MOEDA;
	}

	public void setC5_TIPLIB(String c5_TIPLIB)
	{
		C5_TIPLIB = c5_TIPLIB;
	}

	public void setC5_TPFRETE(String c5_TPFRETE)
	{
		C5_TPFRETE = c5_TPFRETE;
	}

	public void setVendedores(List<Vendedor> vendedores)
	{
		this.vendedores = vendedores;
	}

	public void setItens(List<Itens> itens)
	{
		this.itens = itens;
	}

}
