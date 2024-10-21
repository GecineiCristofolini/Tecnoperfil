package com.neomind.fusion.custom.tecnoperfil.totvs.entidades;

import java.math.BigDecimal;
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
	private BigDecimal C5_FRETE;
	private BigDecimal C5_PBRUTO;
	private BigDecimal C5_PESOL;
	private BigDecimal C5_SEGURO;
	private String C5_REDESP;
	private String C5_ZMENNF;
	private Long C5_MOEDA;
	private String C5_TIPLIB;
	private String C5_TPFRETE;
	private String C5_ZDTFUSI;
	private String C5_CLIENT;
	private String C5_LOJAENT;
	private Long C5_VOLUME1;
	private String C5_VEND1;
	private BigDecimal C5_COMIS1;
	private String C5_VEND2;
	private BigDecimal C5_COMIS2;
	private String C5_VEND3;
	private BigDecimal C5_COMIS3;
	private String C5_VEND4;
	private BigDecimal C5_COMIS4;
	private String C5_VEND5;
	private BigDecimal C5_COMIS5;
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

	public BigDecimal getC5_FRETE()
	{
		return C5_FRETE;
	}

	public BigDecimal getC5_PBRUTO()
	{
		return C5_PBRUTO;
	}

	public BigDecimal getC5_PESOL()
	{
		return C5_PESOL;
	}

	public BigDecimal getC5_SEGURO()
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

	public Long getC5_MOEDA()
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

	public String getC5_ZDTFUSI()
	{
		return C5_ZDTFUSI;
	}

	public String getC5_CLIENT()
	{
		return C5_CLIENT;
	}

	public String getC5_LOJAENT()
	{
		return C5_LOJAENT;
	}

	public Long getC5_VOLUME1()
	{
		return C5_VOLUME1;
	}

	public String getC5_VEND1()
	{
		return C5_VEND1;
	}

	public BigDecimal getC5_COMIS1()
	{
		return C5_COMIS1;
	}

	public String getC5_VEND2()
	{
		return C5_VEND2;
	}

	public BigDecimal getC5_COMIS2()
	{
		return C5_COMIS2;
	}

	public String getC5_VEND3()
	{
		return C5_VEND3;
	}

	public BigDecimal getC5_COMIS3()
	{
		return C5_COMIS3;
	}

	public String getC5_VEND4()
	{
		return C5_VEND4;
	}

	public BigDecimal getC5_COMIS4()
	{
		return C5_COMIS4;
	}

	public String getC5_VEND5()
	{
		return C5_VEND5;
	}

	public BigDecimal getC5_COMIS5()
	{
		return C5_COMIS5;
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

	public void setC5_FRETE(BigDecimal c5_FRETE)
	{
		C5_FRETE = c5_FRETE;
	}

	public void setC5_PBRUTO(BigDecimal c5_PBRUTO)
	{
		C5_PBRUTO = c5_PBRUTO;
	}

	public void setC5_PESOL(BigDecimal c5_PESOL)
	{
		C5_PESOL = c5_PESOL;
	}

	public void setC5_SEGURO(BigDecimal c5_SEGURO)
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

	public void setC5_MOEDA(Long c5_MOEDA)
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

	public void setC5_ZDTFUSI(String c5_ZDTFUSI)
	{
		C5_ZDTFUSI = c5_ZDTFUSI;
	}

	public void setC5_CLIENT(String c5_CLIENT)
	{
		C5_CLIENT = c5_CLIENT;
	}

	public void setC5_LOJAENT(String c5_LOJAENT)
	{
		C5_LOJAENT = c5_LOJAENT;
	}

	public void setC5_VOLUME1(Long c5_VOLUME1)
	{
		C5_VOLUME1 = c5_VOLUME1;
	}

	public void setC5_VEND1(String c5_VEND1)
	{
		C5_VEND1 = c5_VEND1;
	}

	public void setC5_COMIS1(BigDecimal c5_COMIS1)
	{
		C5_COMIS1 = c5_COMIS1;
	}

	public void setC5_VEND2(String c5_VEND2)
	{
		C5_VEND2 = c5_VEND2;
	}

	public void setC5_COMIS2(BigDecimal c5_COMIS2)
	{
		C5_COMIS2 = c5_COMIS2;
	}

	public void setC5_VEND3(String c5_VEND3)
	{
		C5_VEND3 = c5_VEND3;
	}

	public void setC5_COMIS3(BigDecimal c5_COMIS3)
	{
		C5_COMIS3 = c5_COMIS3;
	}

	public void setC5_VEND4(String c5_VEND4)
	{
		C5_VEND4 = c5_VEND4;
	}

	public void setC5_COMIS4(BigDecimal c5_COMIS4)
	{
		C5_COMIS4 = c5_COMIS4;
	}

	public void setC5_VEND5(String c5_VEND5)
	{
		C5_VEND5 = c5_VEND5;
	}

	public void setC5_COMIS5(BigDecimal c5_COMIS5)
	{
		C5_COMIS5 = c5_COMIS5;
	}

	public void setItens(List<Itens> itens)
	{
		this.itens = itens;
	}

}