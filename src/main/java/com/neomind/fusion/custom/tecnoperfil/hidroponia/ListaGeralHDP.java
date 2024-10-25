package com.neomind.fusion.custom.tecnoperfil.hidroponia;

import java.math.BigDecimal;
import java.util.List;

import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;
import com.neomind.fusion.workflow.adapter.AdapterUtils;

public class ListaGeralHDP implements AdapterInterface
{

	@Override
	public void start(Task arg0, EntityWrapper wrapper, Activity arg2)
	{

		try
		{

			List<NeoObject> listageralitens = wrapper.findGenericValue("ListaItens");
			listageralitens.clear();

			//usado para criar uma sequencia de pedido
			int Contitem = 0;

			// Criado um for para percorrer os dados campo listado encontrado no Campo ItenPedido avulso do Workflow HDP

			List<NeoObject> ItensdoPedidoavulso = wrapper.findGenericValue("IteAvProH");

			for (NeoObject ListaItensdoPedidoavulso : ItensdoPedidoavulso)
			{
				EntityWrapper ItensdoPedidoWraper = new EntityWrapper(ListaItensdoPedidoavulso);

				Contitem++;

				String seq = Integer.toString(Contitem);
				String quantidade = ItensdoPedidoWraper.findGenericValue("Qtd");
				String um = ItensdoPedidoWraper.findGenericValue("univenH");
				String descricao = ItensdoPedidoWraper.findGenericValue("DescIav");
				String descricaoIngles = "";
				String descricaoespanhol = "";
				String ferramenta = ItensdoPedidoWraper.findGenericValue("DadosDoProduto.zb4_ferram");
				String grupo = ItensdoPedidoWraper.findGenericValue("DadosDoProduto.zb4_grupo");
				String codigocor = ItensdoPedidoWraper.findGenericValue("DadosDoProduto.zb4_zcorprod");
				String cor = ItensdoPedidoWraper.findGenericValue("DadosDoProduto.zb4_cor");
				String codigoitem = ItensdoPedidoWraper.findGenericValue("CodItemH");
				String ncm = ItensdoPedidoWraper.findGenericValue("DadosDoProduto.zb4_posipi");
				BigDecimal aliqipi = ItensdoPedidoWraper.findGenericValue("DadosDoProduto.zb4_ipi");
				BigDecimal valunitario = ItensdoPedidoWraper.findGenericValue("PvItenAv");
				BigDecimal valmercadoria = ItensdoPedidoWraper.findGenericValue("PrecToTal");
				String tipo = "PA";
				BigDecimal pesoliquido = ItensdoPedidoWraper.findGenericValue("DadosDoProduto.zb4_peso");
				BigDecimal pesobruto = ItensdoPedidoWraper.findGenericValue("DadosDoProduto.zb4_pesbru");
				BigDecimal pesoTOTliquido = ItensdoPedidoWraper.findGenericValue("QtdPesoLiquidoItem");
				BigDecimal pesoTOTBruto = ItensdoPedidoWraper.findGenericValue("QtdPesItem");
				BigDecimal cubagem = ItensdoPedidoWraper.findGenericValue("CubItA");
			    Long volume = ItensdoPedidoWraper.findGenericValue("QtdVol");
			    BigDecimal qtdembalagembigdecimal = ItensdoPedidoWraper.findGenericValue("Produto.b1_qe");
			    Long qtdembalagem =  qtdembalagembigdecimal.longValue();
				BigDecimal tamanho = ItensdoPedidoWraper.findGenericValue("DadosDoProduto.zb4_compr");
				BigDecimal Altura = ItensdoPedidoWraper.findGenericValue("DadosDoProduto.zb4_alt");
				BigDecimal Largura = ItensdoPedidoWraper.findGenericValue("DadosDoProduto.zb4_larg");
				String narrativatecnia = "-";
				String itemped = ItensdoPedidoWraper.findGenericValue("OrderItem");
				String origem = "0";
				String grupoTrib = "P01";
				String armazem ="01";
				String pedidofusion = wrapper.findGenericValue("NumOrcH");
				
				
				
				NeoObject listaitenspedido = AdapterUtils
						.createNewEntityInstance("HDPListaItens");

				EntityWrapper wrapperitenspedido = new EntityWrapper(listaitenspedido);
				
				if (codigoitem.equals("0")){
					
					wrapperitenspedido.setValue("contzero",1L);
					
				} else {
					
					wrapperitenspedido.setValue("contzero",0L);
				}

				wrapperitenspedido.setValue("Seq", seq);
				wrapperitenspedido.setValue("CodigoDoItem", codigoitem);
				wrapperitenspedido.setValue("Descricao", descricao);
				wrapperitenspedido.setValue("DescricaoIngles", descricaoIngles);
				wrapperitenspedido.setValue("DescricaoIngles", descricaoIngles);
				wrapperitenspedido.setValue("DescricaoEspanhol", descricaoespanhol);
			    wrapperitenspedido.setValue("Qtd", quantidade.replaceAll("\\.", ","));
				wrapperitenspedido.setValue("UM", um);
				wrapperitenspedido.setValue("ValorUnitario", valunitario);
				wrapperitenspedido.setValue("ValorMerc", valmercadoria);
				wrapperitenspedido.setValue("Grupo", grupo);
				wrapperitenspedido.setValue("Ferramenta", ferramenta);
				wrapperitenspedido.setValue("Tipo",tipo);
				wrapperitenspedido.setValue("Armazem", armazem);
				wrapperitenspedido.setValue("Origem", origem);
			    wrapperitenspedido.setValue("DescricaoProdutor", "");
				wrapperitenspedido.setValue("Codigocor",codigocor);
				wrapperitenspedido.setValue("DescricaoCor",cor);
				wrapperitenspedido.setValue("Tamanho",tamanho);
				wrapperitenspedido.setValue("NCM", ncm);
				wrapperitenspedido.setValue("ALIQIPI", aliqipi);
				wrapperitenspedido.setValue("QtdEmbalagem", qtdembalagem);
				wrapperitenspedido.setValue("PesoLiquido", pesoliquido);
				wrapperitenspedido.setValue("QtdPesoLiquido",pesoTOTliquido);
				wrapperitenspedido.setValue("PesoBruto", pesobruto);
				wrapperitenspedido.setValue("QtdPesoBruto", pesoTOTBruto);
				wrapperitenspedido.setValue("Volume", volume);
				wrapperitenspedido.setValue("Cubagem", cubagem);
				wrapperitenspedido.setValue("grupotrib", grupoTrib);
				wrapperitenspedido.setValue("Largura", Largura);
				wrapperitenspedido.setValue("Altura", Altura);
				wrapperitenspedido.setValue("NomeCientifico",narrativatecnia );
				wrapperitenspedido.setValue("PedidoFusion",pedidofusion );
				wrapperitenspedido.setValue("Itemped",itemped );


				
				
				
				

				// Adicionar na Lista do Campo Itens no lista itens
				listageralitens.add(listaitenspedido);

			}
			
			//listar itens do Projetos hidroponicos 
			List<NeoObject> listaProjetohdp = wrapper.findGenericValue("ItensProH");
			
			for (NeoObject projetohdp : listaProjetohdp)
			{
				
				EntityWrapper projetoWrapper = new EntityWrapper(projetohdp);
				
				List<NeoObject> listaitensdoprojeto = projetoWrapper.findGenericValue("ItensProHdp");
				
				for (NeoObject itensprojeto : listaitensdoprojeto)
				{
					
					EntityWrapper itensprojetopWraper = new EntityWrapper(itensprojeto);
					
					Contitem++;
   
					String seq = Integer.toString(Contitem);
					Long quantidadelong = itensprojetopWraper.findGenericValue("quantidade");
					String quantidade = Long.toString(quantidadelong);
					String um = itensprojetopWraper.findGenericValue("unidade");
					String codigoitem = itensprojetopWraper.findGenericValue("CodItemP");
					String ferramenta = itensprojetopWraper.findGenericValue("FeritemPr");
					BigDecimal tamanho = itensprojetopWraper.findGenericValue("TamanPr");
					String descricao = itensprojetopWraper.findGenericValue("descricao");
					String descricaoIngles = "";
					String descricaoespanhol = "";
					BigDecimal valunitario = itensprojetopWraper.findGenericValue("PvItenPr");
					BigDecimal valmercadoria = itensprojetopWraper.findGenericValue("PreTotalP");
					BigDecimal Largura = itensprojetopWraper.findGenericValue("Largura");
					String grupo = itensprojetopWraper.findGenericValue("Grupo");
					String codigocor = itensprojetopWraper.findGenericValue("CodigoCor");
					String cor = itensprojetopWraper.findGenericValue("DescricaoDaCor");
					BigDecimal Altura = itensprojetopWraper.findGenericValue("Altura");
					BigDecimal pesoliquido = itensprojetopWraper.findGenericValue("PesoLiquido");
					BigDecimal aliqipi = BigDecimal.ZERO;
					BigDecimal pesobruto = itensprojetopWraper.findGenericValue("PesoBruto");
					BigDecimal qtdembalagembigdecimal = itensprojetopWraper.findGenericValue("Embalagem");
					Long qtdembalagem =  qtdembalagembigdecimal.longValue();
					Long volume = itensprojetopWraper.findGenericValue("QtdVolIP");
					BigDecimal pesoTOTBruto = itensprojetopWraper.findGenericValue("QtdPesItemP");
					String tipo = "PA";
					BigDecimal cubagem = itensprojetopWraper.findGenericValue("CubItemP");
					BigDecimal pesoTOTliquido = itensprojetopWraper.findGenericValue("QtdPesItemPL");
					String narrativatecnia = "-";
					String itemped = itensprojetopWraper.findGenericValue("OrderItem");
					String origem = "0";
					String armazem ="01";
					String pedidofusion = wrapper.findGenericValue("NumOrcH");
					String ncm = itensprojetopWraper.findGenericValue("Ncm");
					String grupoTrib = "P01";
					
							
					
					NeoObject listaitenspedido = AdapterUtils
							.createNewEntityInstance("HDPListaItens");

					EntityWrapper wrapperitensprojeto = new EntityWrapper(listaitenspedido);
					
					
					
					if (codigoitem.equals("0")){
						
						wrapperitensprojeto.setValue("contzero",1L);
						
					} else {
						
						wrapperitensprojeto.setValue("contzero",0L);
					}
							

					wrapperitensprojeto.setValue("Seq", seq);
					wrapperitensprojeto.setValue("CodigoDoItem", codigoitem);
					wrapperitensprojeto.setValue("Descricao", descricao);
					wrapperitensprojeto.setValue("DescricaoIngles", descricaoIngles);
					wrapperitensprojeto.setValue("DescricaoIngles", descricaoIngles);
					wrapperitensprojeto.setValue("DescricaoEspanhol", descricaoespanhol);
					wrapperitensprojeto.setValue("Qtd", quantidade.replaceAll("\\.", ","));
					wrapperitensprojeto.setValue("UM", um);
					wrapperitensprojeto.setValue("ValorUnitario", valunitario);
					wrapperitensprojeto.setValue("ValorMerc", valmercadoria);
					wrapperitensprojeto.setValue("Grupo", grupo);
					wrapperitensprojeto.setValue("Ferramenta", ferramenta);
					wrapperitensprojeto.setValue("Tipo",tipo);
					wrapperitensprojeto.setValue("Armazem", armazem);
					wrapperitensprojeto.setValue("Origem", origem);
					wrapperitensprojeto.setValue("DescricaoProdutor", "");
					wrapperitensprojeto.setValue("Codigocor",codigocor);
					wrapperitensprojeto.setValue("DescricaoCor",cor);
					wrapperitensprojeto.setValue("Tamanho",tamanho);
					wrapperitensprojeto.setValue("NCM", ncm);
					wrapperitensprojeto.setValue("ALIQIPI", aliqipi);
					wrapperitensprojeto.setValue("QtdEmbalagem", qtdembalagem);
					wrapperitensprojeto.setValue("PesoLiquido", pesoliquido);
					wrapperitensprojeto.setValue("QtdPesoLiquido",pesoTOTliquido);
					wrapperitensprojeto.setValue("PesoBruto", pesobruto);
					wrapperitensprojeto.setValue("QtdPesoBruto", pesoTOTBruto);
					wrapperitensprojeto.setValue("Volume", volume);
					wrapperitensprojeto.setValue("Cubagem", cubagem);
					wrapperitensprojeto.setValue("grupotrib", grupoTrib);
					wrapperitensprojeto.setValue("Largura", Largura);
					wrapperitensprojeto.setValue("Altura", Altura);
					wrapperitensprojeto.setValue("NomeCientifico",narrativatecnia );
					wrapperitensprojeto.setValue("PedidoFusion",pedidofusion );
					wrapperitensprojeto.setValue("Itemped",itemped );

					// Adicionar na Lista do Campo Itens do Pedido hdp no Listagem Geral 
					listageralitens.add(listaitenspedido);

					
					
				
				}
				
				
			}
			
			
			
			
			
			
			
			

		}
		catch (Exception e)
		{
			System.out.println("Error ao fazer Listagem");
			System.out.println(e.getMessage());
			e.printStackTrace();

		}

	}

	@Override
	public void back(EntityWrapper arg0, Activity arg1)
	{
		// TODO Auto-generated method stub

	}

}
