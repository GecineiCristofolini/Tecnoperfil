package com.neomind.fusion.custom.tecnoperfil.totvs;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.custom.tecnoperfil.totvs.entidades.Produto;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;
import com.neomind.fusion.workflow.exception.WorkflowException;

public class TotvsIncluirProduto implements AdapterInterface
{

	private static final Log log = LogFactory.getLog(TotvsIncluirProduto.class);

	@Override
	public void back(EntityWrapper arg0, Activity arg1)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void start(Task arg0, EntityWrapper wraperproduto, Activity arg2)
	{

		try
		{
			log.debug("Iniciar inclusao de Produto");

			IncluirProduto(wraperproduto);
			
			log.debug("Pedido incluído com sucesso.");

		}
		catch (WorkflowException e)
		{
			e.printStackTrace();
			log.error("Erro ao incluir produto", e);
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Erro ao incluir produto", e);
			throw new WorkflowException("Erro ao incluir Produto" + e.getCause());
		}
	}

	public String IncluirProduto(EntityWrapper ewProduto)
	{

		try
		{

			List<NeoObject> listaProjetos = ewProduto.findGenericValue("ItensProH");

			for (NeoObject projeto : listaProjetos)
			{


				EntityWrapper ewProjeto = new EntityWrapper(projeto);
				List<NeoObject> listaItens = ewProjeto.findGenericValue("ItensProHdp");
				//String Gerencia = ewProjeto.findGenericValue("Gerencia");

				for (NeoObject item : listaItens)
				{
					EntityWrapper ewItem = new EntityWrapper(item);
					String codigo = ewItem.findGenericValue("CodItemP");

					if (codigo.equals("0"))
					{

						String descricao = ewItem.findGenericValue("descricao");
						String tipo = "PA";
						String grupo = "30356";
						String origem = "0";
						String descricaoProdutor = "";
						String ferramenta = ewItem.findGenericValue("FeritemPr");
						String cor = "";
						BigDecimal tamanho = ewItem.findGenericValue("TamanPr");
						String embalagem = "";
						String unidadeMedida = ewItem.findGenericValue("unidade");
						String ncm = "84248229";
						String cest = "";
						Long iditem = ewItem.findGenericValue("neoId");
						String idfusion = Long.toString(iditem);
						BigDecimal pesoliquido = ewItem.findGenericValue("PesoLiquido");
						BigDecimal pesoBruto = ewItem.findGenericValue("PesoBruto");
						BigDecimal metroLinear = new BigDecimal(0);
						BigDecimal metroQuadrado = new BigDecimal(0);
						String descricaoEspanhol = "";
						String descricaoIngles = "";
						BigDecimal quantidadeCaixa = new BigDecimal(0);
						Long cubagem = ewItem.findGenericValue("QtdVolIP");
						BigDecimal comprimento = ewItem.findGenericValue("TamanPr");
						BigDecimal largura = ewItem.findGenericValue("Largura");
						BigDecimal altura = ewItem.findGenericValue("Altura");
						String contaContabil = "11030205";

						Produto produto = new Produto();

						produto.setDescricao(descricao);
						produto.setTipo(tipo);
						produto.setGrupo(grupo);
						produto.setOrigem(origem);
						produto.setDescricaoProdutor(descricaoProdutor);
						produto.setFerramenta(ferramenta);
						produto.setCor(cor);
						produto.setTamanho(tamanho);
						produto.setEmbalagem(embalagem);
						produto.setUnidadeMedida(unidadeMedida);
						produto.setNcm(ncm);
						produto.setCest(cest);
						produto.setIdFusion(idfusion);
						produto.setPesoliquido(pesoliquido);
						produto.setPesoBruto(pesoBruto);
						produto.setMetroLinear(metroLinear);
						produto.setMetroQuadrado(metroQuadrado);
						produto.setDescricaoEspanhol(descricaoEspanhol);
						produto.setDescricaoIngles(descricaoIngles);
						produto.setQuantidadeCaixa(quantidadeCaixa);
						produto.setCubagem(cubagem);
						produto.setComprimento(comprimento);
						produto.setAltura(altura);
						produto.setLargura(largura);
						produto.setContaContabil(contaContabil);
						
						String json = new Gson().toJson(produto);
						
						System.out.println(json);
						
						log.debug("informacoes retornadas + " + json);
						IncluirProduto IncluirProduto = new IncluirProduto();
						IncluirProduto.IntegracaoIncluirProduto(json, ewProduto);
						
						log.debug("Itens atualizados");
						
						

					}
					
			

				}

			}

		}
		catch (Exception e)
		{
			System.out.print(e.getMessage());
			e.printStackTrace();
			throw new WorkflowException("Erro ao incluir produto " + e.getCause());

		}
		return null;

	}

}
