package com.neomind.fusion.custom.tecnoperfil.exportacao;

public class TecnoperfilItensPackingListDataSource {
	
	private String volumen;
	private String pesoneto;
	private String pesobruto;
	private String altura;
	private String anchura;
	private String longitud;
	private String descricao;
	private String ferramenta;
	private String color;
	
	
	public TecnoperfilItensPackingListDataSource() {
		
	}


	public TecnoperfilItensPackingListDataSource(String volumen, String pesoneto, String pesobruto,
			String altura, String anchura, String longitud, String descricao, String ferramenta,
			String color)
	{
		super();
		this.volumen = volumen;
		this.pesoneto = pesoneto;
		this.pesobruto = pesobruto;
		this.altura = altura;
		this.anchura = anchura;
		this.longitud = longitud;
		this.descricao = descricao;
		this.ferramenta = ferramenta;
		
		this.color = color;
	}


	public String getVolumen()
	{
		return volumen;
	}


	public void setVolumen(String volumen)
	{
		this.volumen = volumen;
	}


	public String getPesoneto()
	{
		return pesoneto;
	}


	public void setPesoneto(String pesoneto)
	{
		this.pesoneto = pesoneto;
	}


	public String getPesobruto()
	{
		return pesobruto;
	}


	public void setPesobruto(String pesobruto)
	{
		this.pesobruto = pesobruto;
	}


	public String getAltura()
	{
		return altura;
	}


	public void setAltura(String altura)
	{
		this.altura = altura;
	}


	public String getAnchura()
	{
		return anchura;
	}


	public void setAnchura(String anchura)
	{
		this.anchura = anchura;
	}


	public String getLongitud()
	{
		return longitud;
	}


	public void setLongitud(String longitud)
	{
		this.longitud = longitud;
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


	


	public String getColor()
	{
		return color;
	}


	public void setColor(String color)
	{
		this.color = color;
	}


	@Override
	public String toString()
	{
		return "TecnoperfilItensPackingListDataSource [volumen=" + volumen + ", pesoneto=" + pesoneto
				+ ", pesobruto=" + pesobruto + ", altura=" + altura + ", anchura=" + anchura
				+ ", longitud=" + longitud + ", descricao=" + descricao + ", ferramenta=" + ferramenta
				+  ", color=" + color + "]";
	}

    
	

	
}