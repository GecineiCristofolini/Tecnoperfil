package com.neomind.fusion.custom.tecnoperfil.exportacao;

import java.math.BigDecimal;
import java.util.List;

import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;
import com.neomind.fusion.workflow.adapter.AdapterUtils;

public class ListaGeralEXP implements AdapterInterface
{

	@SuppressWarnings("deprecation")
	@Override
	public void start(Task arg0, EntityWrapper wrapper, Activity arg2)
	{

		try
		{

			List<NeoObject> listageralitens = wrapper.findGenericValue("PedidoEXP.ListaItens");
			listageralitens.clear();

			//usado para criar uma sequencia de pedido
			int Contitem = 0;

			// Criado um for para percorrer os dados campo listado encontrado no Campo ItenPedExp do Workflow EXP

			List<NeoObject> ItensdoPedidoavulso = wrapper.findGenericValue("PedidoEXP.ItenPedExp");

			for (NeoObject ListaItensdoPedidoavulso : ItensdoPedidoavulso)
			{
				EntityWrapper ItensdoPedidoWraper = new EntityWrapper(ListaItensdoPedidoavulso);

				Contitem++;

				String seq = Integer.toString(Contitem);
				String quantidade = ItensdoPedidoWraper.findGenericValue("QtdEXP");
				String um = ItensdoPedidoWraper.findGenericValue("UindadeDeMedida");
				String descricao = ItensdoPedidoWraper.findGenericValue("DescricaoPortugues");
				String descricaoIngles = "";
				String descricaoespanhol = ItensdoPedidoWraper.findGenericValue("DescricaoEspanhol");
				String ferramenta = ItensdoPedidoWraper.findGenericValue("Ferramentaitem");
				String grupo = ItensdoPedidoWraper.findGenericValue("ProdutoTovs.b1_grupo");
				String codigocor = ItensdoPedidoWraper.findGenericValue("ProdutoTovs.b1_zcorprd");
				String cor = ItensdoPedidoWraper.findGenericValue("ProdutoTovs.b1_zcor");
				String codigoitem = ItensdoPedidoWraper.findGenericValue("codItemE");
				String ncm = ItensdoPedidoWraper.findGenericValue("ProdutoTovs.b1_posipi");
				BigDecimal aliqipi = ItensdoPedidoWraper.findGenericValue("ProdutoTovs.b1_ipi");
				BigDecimal valunitario = ItensdoPedidoWraper.findGenericValue("ValorUnitario");
				BigDecimal valmercadoria = BigDecimal.ZERO;
				valmercadoria = valunitario.multiply(new BigDecimal(quantidade)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
				String tipo = "PA";
				BigDecimal pesoliquido = ItensdoPedidoWraper.findGenericValue("ProdutoTovs.b1_peso");
				BigDecimal pesobruto = ItensdoPedidoWraper.findGenericValue("ProdutoTovs.b1_pesbru");
				BigDecimal pesoTOTliquido = ItensdoPedidoWraper.findGenericValue("PesoLiquido");
				BigDecimal pesoTOTBruto = ItensdoPedidoWraper.findGenericValue("PesoBruto");
				BigDecimal cubagem = ItensdoPedidoWraper.findGenericValue("VolumesM3");
			    Long volume = ItensdoPedidoWraper.findGenericValue("Volumes");
			    BigDecimal qtdembalagembigdecimal = ItensdoPedidoWraper.findGenericValue("ProdutoTovs.b1_qe");
			    Long qtdembalagem =  qtdembalagembigdecimal.longValue();
				BigDecimal tamanho = ItensdoPedidoWraper.findGenericValue("DadosComplementares.b5_compr");
				BigDecimal Altura = ItensdoPedidoWraper.findGenericValue("DadosComplementares.b5_altura");
				BigDecimal Largura = ItensdoPedidoWraper.findGenericValue("DadosComplementares.b5_larg");
				String narrativatecnia = "-";
				String itemped = "";
				String origem = ItensdoPedidoWraper.findGenericValue("ProdutoTovs.b1_origem");
				String grupoTrib =  ItensdoPedidoWraper.findGenericValue("ProdutoTovs.b1_grtrib");
				String armazem =ItensdoPedidoWraper.findGenericValue("ProdutoTovs.b1_locpad");
				String pedidofusion = wrapper.findGenericValue("PedidoEXP.NumPed");
				String codigocliente = wrapper.findGenericValue("PedidoEXP.ClienteExportacao.ClienteTotvs.a1_zfusion");
				String Lado = "";
				String passo ="";
				String diametro = "";
				
				
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
				wrapperitenspedido.setValue("Lado",Lado );
				wrapperitenspedido.setValue("Passo",passo );
				wrapperitenspedido.setValue("Diametro",diametro );
				wrapperitenspedido.setValue("CodigoCliente",codigocliente );


				
				
				
				

				// Adicionar na Lista do Campo Itens no lista itens
				listageralitens.add(listaitenspedido);

			}
			
			// Criado um for para percorrer os dados campo listado encontrado no Campo ItenPedExp no Worflow Hdp

						List<NeoObject> ItensdoPedidoavulsohdp = wrapper.findGenericValue("PedidoEXP.ProjetoHEXP.ItensExportacaoAvulso");

						for (NeoObject ListaItensdoPedidoavulsohdp : ItensdoPedidoavulsohdp)
						{
							EntityWrapper ItensdoPedidoWraperhdp = new EntityWrapper(ListaItensdoPedidoavulsohdp);

							Contitem++;

							String seq = Integer.toString(Contitem);
							String quantidade = ItensdoPedidoWraperhdp.findGenericValue("QtdEXP");
							String um = ItensdoPedidoWraperhdp.findGenericValue("UindadeDeMedida");
							String descricao = ItensdoPedidoWraperhdp.findGenericValue("DescricaoPortugues");
							String descricaoIngles = "";
							String descricaoespanhol = ItensdoPedidoWraperhdp.findGenericValue("DescricaoEspanhol");
							String ferramenta = ItensdoPedidoWraperhdp.findGenericValue("Ferramentaitem");
							String grupo = ItensdoPedidoWraperhdp.findGenericValue("ProdutoTovs.b1_grupo");
							String codigocor = ItensdoPedidoWraperhdp.findGenericValue("ProdutoTovs.b1_zcorprd");
							String cor = ItensdoPedidoWraperhdp.findGenericValue("ProdutoTovs.b1_zcor");
							String codigoitem = ItensdoPedidoWraperhdp.findGenericValue("codItemE");
							String ncm = ItensdoPedidoWraperhdp.findGenericValue("ProdutoTovs.b1_posipi");
							BigDecimal aliqipi = ItensdoPedidoWraperhdp.findGenericValue("ProdutoTovs.b1_ipi");
							BigDecimal valunitario = ItensdoPedidoWraperhdp.findGenericValue("ValorUnitario");
							BigDecimal valmercadoria = BigDecimal.ZERO;
							valmercadoria = valunitario.multiply(new BigDecimal(quantidade)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
							String tipo = "PA";
							BigDecimal pesoliquido = ItensdoPedidoWraperhdp.findGenericValue("ProdutoTovs.b1_peso");
							BigDecimal pesobruto = ItensdoPedidoWraperhdp.findGenericValue("ProdutoTovs.b1_pesbru");
							BigDecimal pesoTOTliquido = ItensdoPedidoWraperhdp.findGenericValue("PesoLiquido");
							BigDecimal pesoTOTBruto = ItensdoPedidoWraperhdp.findGenericValue("PesoBruto");
							BigDecimal cubagem = ItensdoPedidoWraperhdp.findGenericValue("VolumesM3");
						    Long volume = ItensdoPedidoWraperhdp.findGenericValue("Volumes");
						    BigDecimal qtdembalagembigdecimal = ItensdoPedidoWraperhdp.findGenericValue("ProdutoTovs.b1_qe");
						    Long qtdembalagem =  qtdembalagembigdecimal.longValue();
							BigDecimal tamanho = ItensdoPedidoWraperhdp.findGenericValue("DadosComplementares.b5_compr");
							BigDecimal Altura = ItensdoPedidoWraperhdp.findGenericValue("DadosComplementares.b5_altura");
							BigDecimal Largura = ItensdoPedidoWraperhdp.findGenericValue("DadosComplementares.b5_larg");
							String narrativatecnia = "-";
							String itemped = "";
							String origem = ItensdoPedidoWraperhdp.findGenericValue("ProdutoTovs.b1_origem");
							String grupoTrib =  ItensdoPedidoWraperhdp.findGenericValue("ProdutoTovs.b1_grtrib");
							String armazem =ItensdoPedidoWraperhdp.findGenericValue("ProdutoTovs.b1_locpad");
							String pedidofusion = wrapper.findGenericValue("PedidoEXP.NumPed");
							String codigocliente = wrapper.findGenericValue("PedidoEXP.ClienteExportacao.ClienteTotvs.a1_zfusion");
							String Lado = "";
							String passo ="";
							String diametro = "";
							
							
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
							wrapperitenspedido.setValue("Lado",Lado );
							wrapperitenspedido.setValue("Passo",passo );
							wrapperitenspedido.setValue("Diametro",diametro );
							wrapperitenspedido.setValue("CodigoCliente",codigocliente );

							
							
							
							

							// Adicionar na Lista do Campo Itens no lista itens
							listageralitens.add(listaitenspedido);

						}
			
			//listar itens dos Projetos 
						
			List<NeoObject> listaProjetohdp = wrapper.findGenericValue("PedidoEXP.ProjetoHEXP.ItensProH");
			
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
					String descricaoespanhol = itensprojetopWraper.findGenericValue("DescricaoEspanhol");
					BigDecimal valunitario = itensprojetopWraper.findGenericValue("ValorAjustado");
					BigDecimal valmercadoria = BigDecimal.ZERO;
					valmercadoria = valunitario.multiply(new BigDecimal(quantidade)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
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
					String pedidofusion = wrapper.findGenericValue("PedidoEXP.NumPed");
					String codigocliente = wrapper.findGenericValue("PedidoEXP.ClienteExportacao.ClienteTotvs.a1_zfusion");
					String ncm = itensprojetopWraper.findGenericValue("Ncm");
					String grupoTrib = "P01";
					String Lado = itensprojetopWraper.findGenericValue("Lado");
					String passo = itensprojetopWraper.findGenericValue("Passo");
					String diametro = itensprojetopWraper.findGenericValue("Diametro");
					
							
					
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
					wrapperitensprojeto.setValue("NomeCientifico",narrativatecnia);
					wrapperitensprojeto.setValue("PedidoFusion",pedidofusion );
					wrapperitensprojeto.setValue("Itemped",itemped );
					wrapperitensprojeto.setValue("Lado",Lado );
					wrapperitensprojeto.setValue("Passo",passo );
					wrapperitensprojeto.setValue("Diametro",diametro );
					wrapperitensprojeto.setValue("CodigoCliente",codigocliente );
					

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
