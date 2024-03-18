package com.neomind.fusion.custom.tecnoperfil.ccv;





import java.math.BigDecimal;
import java.util.List;

import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.persist.PersistEngine;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;
import com.neomind.fusion.workflow.adapter.AdapterUtils;

public class CopiarItens implements AdapterInterface
{
	
	
	
	
	@Override
	public void start(Task arg0, EntityWrapper wrapper, Activity arg2)
	{
		try {
		
		String oc =	wrapper.findGenericValue("InformeAOrdemDeCompra");
		System.out.println(oc);
		Long prazomedio = wrapper.findGenericValue("PrazoMedioImportado");
		
		// Lista o Campo lista Incluir as Linhas Da Ordem de Compra"Incluirlinhaoc"
		List<NeoObject> listaordemdecompraitens = wrapper.findGenericValue("Incluirlinhaoc");
		
	        
		  // Criado um for para percorrer os dados campo listado encontro os itens 
			for ( NeoObject Listaordemdecompras : listaordemdecompraitens) {
				EntityWrapper listacompraWrapper = new EntityWrapper(Listaordemdecompras);
				String codigoitemoc = listacompraWrapper.findGenericValue("codigoitem");
				String dscritemoc = listacompraWrapper.findGenericValue("descricaoitem");
				String qtdtexto = listacompraWrapper.findGenericValue("qtd");
				String unidademedida = listacompraWrapper.findGenericValue("um");
				BigDecimal vlunitario = listacompraWrapper.findGenericValue("valorunitario");
				BigDecimal  vlmercadoria = listacompraWrapper.findGenericValue("valormercadoria");
				BigDecimal  aliquotaipi = listacompraWrapper.findGenericValue("aliquotaipi");
				BigDecimal  valoripi = listacompraWrapper.findGenericValue("valoripi");
				BigDecimal  valortotalcomipi = listacompraWrapper.findGenericValue("valortotalcomipi");
				BigDecimal  pesoitem = listacompraWrapper.findGenericValue("pesoitem");
				BigDecimal  precokg = listacompraWrapper.findGenericValue("precokg");
						
				// incluir no Itens do Pedido do Workflow de pedidos contrução civil							
				List<NeoObject> itens = wrapper.findGenericValue("WPedido.PItenPed");
				
				NeoObject itenspedido = AdapterUtils.createNewEntityInstance("ItenPed");
				
				
				EntityWrapper wrapperitenspedido = new EntityWrapper(itenspedido);
				//codigo do item 
				wrapperitenspedido.setValue("CodigoItemImportado", codigoitemoc);
				wrapperitenspedido.setValue("CodItem", codigoitemoc);
				//descrição do item
				wrapperitenspedido.setValue("DescricaoDoItemImportado", dscritemoc);
				wrapperitenspedido.setValue("DesProd", dscritemoc);
				//Quantidade
				wrapperitenspedido.setValue("QtdList", qtdtexto);
				wrapperitenspedido.setValue("Qtd", qtdtexto);
				double qtddecimal = Double.parseDouble(qtdtexto);
				BigDecimal qtdkugel = BigDecimal.valueOf(qtddecimal);
				wrapperitenspedido.setValue("QtdKugel", qtdkugel);
				//Unidade de Medida
				wrapperitenspedido.setValue("UnidadeDeMedidaItemImportado", unidademedida);
				wrapperitenspedido.setValue("UniVenL", unidademedida);
				//valor unitario
				wrapperitenspedido.setValue("VlUNiL", vlunitario);
				wrapperitenspedido.setValue("ValUni", vlunitario);
				wrapperitenspedido.setValue("vlUniSDE", vlunitario);
				wrapperitenspedido.setValue("ValorUnitarioDoItemImportado", vlunitario);
				
				// valor Mercadoria 
				wrapperitenspedido.setValue("vlmerc", vlmercadoria);
				wrapperitenspedido.setValue("VlMercL",vlmercadoria);
				wrapperitenspedido.setValue("VlMercL",vlmercadoria);
				wrapperitenspedido.setValue("vlSdsc",vlmercadoria);
				//aliquota ipi
				wrapperitenspedido.setValue("AliquotaIPIItemImportado", aliquotaipi);
				wrapperitenspedido.setValue("AliqIPL",aliquotaipi);
				wrapperitenspedido.setValue("AliIPI",aliquotaipi);
				//valor ipi
				wrapperitenspedido.setValue("ValPIL", valoripi);
				wrapperitenspedido.setValue("ValIpi", valoripi);
				//Valor total com ipi 
				wrapperitenspedido.setValue("VltotCipi", valortotalcomipi);
				//peso item 
				wrapperitenspedido.setValue("PesItem", pesoitem);
				// Preço kg item 
				wrapperitenspedido.setValue("PrecoKG", precokg);
				// Tipo de inclusao
				wrapperitenspedido.setValue("TipoDeInclusao", "Importado");
				//prazo medio,descontos,tipo tabela,rentabilidade 
				wrapperitenspedido.setValue("PrecPrM", prazomedio);
				wrapperitenspedido.setValue("DesperL",BigDecimal.ZERO);
				wrapperitenspedido.setValue("DesEspL",BigDecimal.ZERO);
				wrapperitenspedido.setValue("RentCCV", "Importado");
				wrapperitenspedido.setValue("TablprL", "Importado");
				
				// Adicionar na Lista do Campo Itens do Pedido
				itens.add(itenspedido);
				
				
				PersistEngine.persist(itenspedido);

			}
		
		} catch (Exception e) {
			System.out.println("Error Ao Copiar Dados da OC para Itens do Pedido");
			System.out.println(e.getMessage());
		
			
		}
		

	}

	@Override
	public void back(EntityWrapper arg0, Activity arg1)
	{
		// TODO Auto-generated method stub
		
	}

}
