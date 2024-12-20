package com.neomind.fusion.custom.tecnoperfil.exportacao;

import java.util.Collection;

public class DeclaracaoProdutorDataSourceMenu
{

	private String pathLogo;
	private String Invoice;
	private String nomeempresa;
	private String acordo;
	private String materiaisnacional;
	private String materiaisoriginais;
	private String materiaisterceiros;
	private String pathSubRelatorio1;
	private String pathSubRelatorio2;
	private Collection<DeclaracaoProdutorDataSourceItem> listaItens;
	private Collection<DeclaracaoProdutorDataSourceItemNCM> listancm;

	public DeclaracaoProdutorDataSourceMenu()
	{

	}

	public String getPathLogo()
	{
		return pathLogo;
	}

	public String getInvoice()
	{
		return Invoice;
	}

	public String getNomeempresa()
	{
		return nomeempresa;
	}

	public String getAcordo()
	{
		return acordo;
	}

	public String getMateriaisnacional()
	{
		return materiaisnacional;
	}

	public String getMateriaisoriginais()
	{
		return materiaisoriginais;
	}

	public String getMateriaisterceiros()
	{
		return materiaisterceiros;
	}

	public String getPathSubRelatorio1()
	{
		return pathSubRelatorio1;
	}

	public String getPathSubRelatorio2()
	{
		return pathSubRelatorio2;
	}

	public Collection<DeclaracaoProdutorDataSourceItem> getListaItens()
	{
		return listaItens;
	}

	public Collection<DeclaracaoProdutorDataSourceItemNCM> getListancm()
	{
		return listancm;
	}

	public void setPathLogo(String pathLogo)
	{
		this.pathLogo = pathLogo;
	}

	public void setInvoice(String invoice)
	{
		Invoice = invoice;
	}

	public void setNomeempresa(String nomeempresa)
	{
		this.nomeempresa = nomeempresa;
	}

	public void setAcordo(String acordo)
	{
		this.acordo = acordo;
	}

	public void setMateriaisnacional(String materiaisnacional)
	{
		this.materiaisnacional = materiaisnacional;
	}

	public void setMateriaisoriginais(String materiaisoriginais)
	{
		this.materiaisoriginais = materiaisoriginais;
	}

	public void setMateriaisterceiros(String materiaisterceiros)
	{
		this.materiaisterceiros = materiaisterceiros;
	}

	public void setPathSubRelatorio1(String pathSubRelatorio1)
	{
		this.pathSubRelatorio1 = pathSubRelatorio1;
	}

	public void setPathSubRelatorio2(String pathSubRelatorio2)
	{
		this.pathSubRelatorio2 = pathSubRelatorio2;
	}

	public void setListaItens(Collection<DeclaracaoProdutorDataSourceItem> listaItens)
	{
		this.listaItens = listaItens;
	}

	public void setListancm(Collection<DeclaracaoProdutorDataSourceItemNCM> listancm)
	{
		this.listancm = listancm;
	}
	
	
}