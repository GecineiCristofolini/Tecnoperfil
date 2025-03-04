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

			List<NeoObject> listaItens = ewProduto.findGenericValue("ListaItens");
			StringBuilder mensagem = new StringBuilder();

			for (NeoObject projeto : listaItens)
			{

				EntityWrapper ewProjeto = new EntityWrapper(projeto);

				String codigo = ewProjeto.findGenericValue("CodigoDoItem");
				

				if (codigo.equals("0"))
				{

					String descricao = ewProjeto.findGenericValue("Descricao");
					String descricaoIngles = ewProjeto.findGenericValue("DescricaoIngles");
					String descricaoEspanhol = ewProjeto.findGenericValue("DescricaoEspanhol");
					String tipo = ewProjeto.findGenericValue("Tipo");
					String grupo = ewProjeto.findGenericValue("Grupo");
					String armazem = ewProjeto.findGenericValue("Armazem");
					String origem = ewProjeto.findGenericValue("Origem");
					String descricaoProdutor = ewProjeto.findGenericValue("DescricaoProdutor");
					String ferramenta = ewProjeto.findGenericValue("Ferramenta");
					String codcor = ewProjeto.findGenericValue("Codigocor");
					String descricor = ewProjeto.findGenericValue("DescricaoCor");
					descricor = descricor.trim();
					BigDecimal tamanho = ewProjeto.findGenericValue("Tamanho");
					String embalagem = "";
					String unidadeMedida = ewProjeto.findGenericValue("UM");
					String ncm = ewProjeto.findGenericValue("NCM");
					String cest = "";
					BigDecimal aliqipi = ewProjeto.findGenericValue("ALIQIPI");
					String hdp = "S";
					String ccv = "N";
					String ind = "N";
					String exp = "N";
					Long qtdEmbalagem = ewProjeto.findGenericValue("QtdEmbalagem");
					Long iditem = ewProjeto.findGenericValue("neoId");
					String idfusion = Long.toString(iditem);
					BigDecimal pesoliquido = ewProjeto.findGenericValue("PesoLiquido");
					BigDecimal pesoBruto = ewProjeto.findGenericValue("PesoBruto");
					BigDecimal metroLinear = new BigDecimal(0);
					BigDecimal metroQuadrado = new BigDecimal(0);
					BigDecimal quantidadeCaixa = new BigDecimal(0);
					Long cubagem = 0L;
					BigDecimal comprimento = ewProjeto.findGenericValue("Tamanho");
					BigDecimal largura = ewProjeto.findGenericValue("Largura");
					BigDecimal altura = ewProjeto.findGenericValue("Altura");
					String contaContabil = "11030205";
					String grupotrib = ewProjeto.findGenericValue("grupotrib");
					String lado = ewProjeto.findGenericValue("Lado");
					String passo1 = ewProjeto.findGenericValue("Passo");
					Long qtdfuros = ewProjeto.findGenericValue("QtdFuros");
					
					if (passo1 == "" || passo1.isBlank() || passo1 == null) {
						
						passo1 = "0";
						
					}
					Long passo = Long.parseLong(passo1);	
					String diametro1 = ewProjeto.findGenericValue("Diametro");
					
					if (diametro1 == "" || diametro1.isBlank() || diametro1 == null ) {
						
						diametro1 = "0";
						
					}
											
					Long diametro = Long.parseLong(diametro1);
					
					boolean inativo = false;
					

					Produto produto = new Produto();

					produto.setDescricao(descricao);
					produto.setTipo(tipo);
					produto.setGrupo(grupo);
					produto.setArmazem(armazem);
					produto.setOrigem(origem);
					produto.setDescricaoProdutor(descricaoProdutor);
					produto.setFerramenta(ferramenta);
					produto.setCodcor(codcor);
					produto.setDescricor(descricor);
					produto.setTamanho(tamanho);
					produto.setEmbalagem(embalagem);
					produto.setUnidadeMedida(unidadeMedida);
					produto.setNcm(ncm);
					produto.setCest(cest);
					produto.setAliqipi(aliqipi);
					produto.setHdp(hdp);
					produto.setCcv(ccv);
					produto.setInd(ind);
					produto.setExp(exp);
					produto.setQtdEmbalagem(qtdEmbalagem);
					produto.setIdFusion(idfusion);
					produto.setPesoLiquido(pesoliquido);
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
					produto.setGrupoTrib(grupotrib);
					produto.setInativo(inativo);
					produto.setB5_ZLADO(lado);
					produto.setB5_ZPASSO(passo);
					produto.setB5_ZDIAMET(diametro);
					produto.setB5_ZQTFURO(qtdfuros);
					
					
					String json = new Gson().toJson(produto);

					System.out.println(json);

					log.debug("informacoes retornadas + " + json);
					IncluirProduto IncluirProduto = new IncluirProduto();
                
					String codigoi = IncluirProduto.IntegracaoIncluirProduto(json);
					codigo = codigoi;
					

					

					mensagem.append(codigo);
					mensagem.append("-");
					mensagem.append(descricao);
					mensagem.append("\n");
					

					log.debug("Itens atualizados");

				}
				

				ewProjeto.findField("CodigoDoItem").setValue(codigo);
				
				String validacodigo = ewProjeto.findGenericValue("CodigoDoItem");
				

				if (validacodigo.equals("0"))
				{

					ewProjeto.findField("contzero").setValue(1l);
				}
				else
				{
					ewProjeto.findField("contzero").setValue(0l);
				}
				

			}
			
			ewProduto.setValue("AvisoProdutoCadastrado", mensagem.toString());

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
