package com.neomind.fusion.custom.tecnoperfil.exportacao;

import java.math.BigDecimal;
import java.util.List;

import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;
import com.neomind.fusion.workflow.adapter.AdapterUtils;

public class ListaGeralItens implements AdapterInterface
{

	@Override
	public void start(Task arg0, EntityWrapper wrapper, Activity arg2)
	{

		try
		{

			List<NeoObject> listageralitens = wrapper.findGenericValue("PedidoEXP.ListaGeralItens");
			listageralitens.clear();

			//usado para criar uma sequencia de pedido
			int Contitem = 0;

			// Criado um for para percorrer os dados campo listado encontrado no Campo ItenPedExp do Workflow Exportacao

			List<NeoObject> ItensdoPedido = wrapper.findGenericValue("PedidoEXP.ItenPedExp");

			for (NeoObject ListaItensdoPedido : ItensdoPedido)
			{
				EntityWrapper ItensdoPedidoWraper = new EntityWrapper(ListaItensdoPedido);

				Contitem++;

				String seq = Integer.toString(Contitem);
				String quantidade = ItensdoPedidoWraper.findGenericValue("QtdEXP");
				String um = ItensdoPedidoWraper.findGenericValue("UmeExp.cod_um");
				String descricaoitem = ItensdoPedidoWraper.findGenericValue("DesItemEXP.linha_nar_item");
				String ferramenta = ItensdoPedidoWraper.findGenericValue("Ferramentaitem");
				String descricaoferramenta = ItensdoPedidoWraper
						.findGenericValue("DescricaoDaFerramenta");
				String cor = ItensdoPedidoWraper.findGenericValue("CorItens");
				String codigoitem = ItensdoPedidoWraper.findGenericValue("codItemE");
				String ncm = ItensdoPedidoWraper.findGenericValue("NCMitem.cod_nbm_item");
				String descricaoncm = ItensdoPedidoWraper.findGenericValue("NCMitem.cod_nbm_item");
				String CodigoOperacao = "";
				BigDecimal valunitario = ItensdoPedidoWraper.findGenericValue("ValorUnitario");
				BigDecimal valmercadoria = ItensdoPedidoWraper.findGenericValue("VlmeEXP");
				String materialnacional = "";
				BigDecimal pesoliquido = ItensdoPedidoWraper.findGenericValue("PesoLiquido");
				BigDecimal pesobruto = ItensdoPedidoWraper.findGenericValue("PesoBruto");
				Long volume = ItensdoPedidoWraper.findGenericValue("Volumes");
				BigDecimal volumem3 = ItensdoPedidoWraper.findGenericValue("VolumesM3");
				String qtd2 = ItensdoPedidoWraper.findGenericValue("QuantidadeSegundaUM");
				String um2 = ItensdoPedidoWraper.findGenericValue("SegundaUnidadeMedida");
				
				
				NeoObject listaitenspedido = AdapterUtils
						.createNewEntityInstance("ExportacaoListaGeralItens");

				EntityWrapper wrapperitenspedido = new EntityWrapper(listaitenspedido);

				wrapperitenspedido.setValue("Seq", seq);
				wrapperitenspedido.setValue("Quantidade", quantidade.replaceAll("\\.", ","));
				wrapperitenspedido.setValue("UnidadeDeMedida", um);
				wrapperitenspedido.setValue("DescricaoItem", descricaoitem);
				wrapperitenspedido.setValue("Ferramenta", ferramenta);
				wrapperitenspedido.setValue("DescricaoFerramenta", descricaoferramenta);
				wrapperitenspedido.setValue("Cor", cor);
				wrapperitenspedido.setValue("CodigoItem", codigoitem);
				wrapperitenspedido.setValue("NCM", ncm);
				wrapperitenspedido.setValue("DescricaoNCM", descricaoncm);
				wrapperitenspedido.setValue("CodigoOperacao", CodigoOperacao);
				wrapperitenspedido.setValue("Vluni", valunitario);
				wrapperitenspedido.setValue("ValorMerc", valmercadoria);
				wrapperitenspedido.setValue("MaterialNacional", materialnacional);
				wrapperitenspedido.setValue("PesoLiquido", pesoliquido);
				wrapperitenspedido.setValue("PesoBruto", pesobruto);
				wrapperitenspedido.setValue("Volume", volume);
				wrapperitenspedido.setValue("VolumeM3", volumem3);
				wrapperitenspedido.setValue("Qtd2", qtd2);
				wrapperitenspedido.setValue("UM2", um2);

				// Adicionar na Lista do Campo Itens do Pedido
				listageralitens.add(listaitenspedido);

			}
			
			// Lista itens do Pedido de exportação HDP do Workflow de Hidroponia
			List<NeoObject> ItensdoPedidohdp = wrapper.findGenericValue("PedidoEXP.ProjetoHEXP.ItensExportacaoAvulso");
			
			for (NeoObject ListaItensdoPedidohdp : ItensdoPedidohdp)
			{
				EntityWrapper ItensdoPedidohdpWraper = new EntityWrapper(ListaItensdoPedidohdp);

				Contitem++;

				String seq = Integer.toString(Contitem);
				String quantidade = ItensdoPedidohdpWraper.findGenericValue("QtdEXP");
				String um = ItensdoPedidohdpWraper.findGenericValue("UmeExp.cod_um");
				String descricaoitem = ItensdoPedidohdpWraper.findGenericValue("DesItemEXP.linha_nar_item");
				String ferramenta = ItensdoPedidohdpWraper.findGenericValue("Ferramentaitem");
				String descricaoferramenta = ItensdoPedidohdpWraper
						.findGenericValue("DescricaoDaFerramenta");
				String cor = ItensdoPedidohdpWraper.findGenericValue("CorItens");
				String codigoitem = ItensdoPedidohdpWraper.findGenericValue("codItemE");
				String ncm = ItensdoPedidohdpWraper.findGenericValue("NCMitem.cod_nbm_item");
				String descricaoncm = ItensdoPedidohdpWraper.findGenericValue("NCMitem.cod_nbm_item");
				String CodigoOperacao = "";
				BigDecimal valunitario = ItensdoPedidohdpWraper.findGenericValue("ValorUnitario");
				BigDecimal valmercadoria = ItensdoPedidohdpWraper.findGenericValue("VlmeEXP");
				String materialnacional = "";
				BigDecimal pesoliquido =ItensdoPedidohdpWraper.findGenericValue("PesoLiquido");
				BigDecimal pesobruto = ItensdoPedidohdpWraper.findGenericValue("PesoBruto");
				Long volume = ItensdoPedidohdpWraper.findGenericValue("Volumes");
				BigDecimal volumem3 = ItensdoPedidohdpWraper.findGenericValue("VolumesM3");

				NeoObject listaitenspedido = AdapterUtils
						.createNewEntityInstance("ExportacaoListaGeralItens");

				EntityWrapper wrapperitenspedido = new EntityWrapper(listaitenspedido);

				wrapperitenspedido.setValue("Seq", seq);
				wrapperitenspedido.setValue("Quantidade", quantidade.replaceAll("\\.", ","));
				wrapperitenspedido.setValue("UnidadeDeMedida", um);
				wrapperitenspedido.setValue("DescricaoItem", descricaoitem);
				wrapperitenspedido.setValue("Ferramenta", ferramenta);
				wrapperitenspedido.setValue("DescricaoFerramenta", descricaoferramenta);
				wrapperitenspedido.setValue("Cor", cor);
				wrapperitenspedido.setValue("CodigoItem", codigoitem);
				wrapperitenspedido.setValue("NCM", ncm);
				wrapperitenspedido.setValue("DescricaoNCM", descricaoncm);
				wrapperitenspedido.setValue("CodigoOperacao", CodigoOperacao);
				wrapperitenspedido.setValue("Vluni", valunitario);
				wrapperitenspedido.setValue("ValorMerc", valmercadoria);
				wrapperitenspedido.setValue("MaterialNacional", materialnacional);
				wrapperitenspedido.setValue("PesoLiquido", pesoliquido);
				wrapperitenspedido.setValue("PesoBruto", pesobruto);
				wrapperitenspedido.setValue("Volume", volume);
				wrapperitenspedido.setValue("VolumeM3", volumem3);

				// Adicionar na Lista do Campo Itens do Pedido hdp no Listagem Geral 
				listageralitens.add(listaitenspedido);

			}
			
			//listar itens do Projetos hidroponicos 
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
				    long quantidade = itensprojetopWraper.findGenericValue("quantidade");
				    String qtd = String.valueOf(quantidade);
					
					String um = itensprojetopWraper.findGenericValue("unidade");
					String descricaoitem = itensprojetopWraper.findGenericValue("descricao");
					String ferramenta = itensprojetopWraper.findGenericValue("FeritemPr");
					String descricaoferramenta = itensprojetopWraper
							.findGenericValue("FeritemPr");
					String cor ="-";
					String codigoitem = "-";
					String ncm = "-";
					String descricaoncm = "-";
					String CodigoOperacao = "";
					BigDecimal valunitario = itensprojetopWraper.findGenericValue("ValorAjustado");
					BigDecimal valmercadoria =itensprojetopWraper.findGenericValue("PrecoTotalExp");
					String materialnacional = "";
					BigDecimal pesoliquido = BigDecimal.ZERO;
					BigDecimal pesobruto = BigDecimal.ZERO;;
					Long volume = itensprojetopWraper.findGenericValue("QtdVolIP");
					BigDecimal volumem3 = itensprojetopWraper.findGenericValue("CubItemP");

					NeoObject listaitenspedido = AdapterUtils
							.createNewEntityInstance("ExportacaoListaGeralItens");

					EntityWrapper wrapperitenspedido = new EntityWrapper(listaitenspedido);

					wrapperitenspedido.setValue("Seq", seq);
					wrapperitenspedido.setValue("Quantidade", qtd);
					wrapperitenspedido.setValue("UnidadeDeMedida", um);
					wrapperitenspedido.setValue("DescricaoItem", descricaoitem);
					wrapperitenspedido.setValue("Ferramenta", ferramenta);
					wrapperitenspedido.setValue("DescricaoFerramenta", descricaoferramenta);
					wrapperitenspedido.setValue("Cor", cor);
					wrapperitenspedido.setValue("CodigoItem", codigoitem);
					wrapperitenspedido.setValue("NCM", ncm);
					wrapperitenspedido.setValue("DescricaoNCM", descricaoncm);
					wrapperitenspedido.setValue("CodigoOperacao", CodigoOperacao);
					wrapperitenspedido.setValue("Vluni", valunitario);
					wrapperitenspedido.setValue("ValorMerc", valmercadoria);
					wrapperitenspedido.setValue("MaterialNacional", materialnacional);
					wrapperitenspedido.setValue("PesoLiquido", pesoliquido);
					wrapperitenspedido.setValue("PesoBruto", pesobruto);
					wrapperitenspedido.setValue("Volume", volume);
					wrapperitenspedido.setValue("VolumeM3", volumem3);

					// Adicionar na Lista do Campo Itens do Pedido hdp no Listagem Geral 
					listageralitens.add(listaitenspedido);

					
					
				
				}
				
				
			}
			
			
			
			
			
			
			
			

		}
		catch (Exception e)
		{
			System.out.println("Error ao fazer Listagem");
			System.out.println(e.getMessage());

		}

	}

	@Override
	public void back(EntityWrapper arg0, Activity arg1)
	{
		// TODO Auto-generated method stub

	}

}
