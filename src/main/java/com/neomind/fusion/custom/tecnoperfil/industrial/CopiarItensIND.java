package com.neomind.fusion.custom.tecnoperfil.industrial;





import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.persist.PersistEngine;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;
import com.neomind.fusion.workflow.adapter.AdapterUtils;
import com.neomind.util.NeoCalendarUtils;

public class CopiarItensIND implements AdapterInterface
{
	
	
	
	
	@Override
	public void start(Task arg0, EntityWrapper wrapper, Activity arg2)
	{
		try {
		
		String oc =	wrapper.findGenericValue("InformeAOrdemDeCompra");
		System.out.println(oc);
		
		
		// Lista o Campo lista Incluir as Linhas Da Ordem de Compra"Incluirlinhaoc"
		List<NeoObject> listaordemdecompraitens = wrapper.findGenericValue("Incluirlinhaoc");
		
	        
		  // Criado um for para percorrer os dados campo listado encontro os itens 
			for ( NeoObject Listaordemdecompras : listaordemdecompraitens) {
				EntityWrapper listacompraWrapper = new EntityWrapper(Listaordemdecompras);
				String codigoitemoc = listacompraWrapper.findGenericValue("codigoitem");
				String dscritemoc = listacompraWrapper.findGenericValue("descricaoitem");
				BigDecimal quantidade = listacompraWrapper.findGenericValue("quantidade");
				String unidademedida = listacompraWrapper.findGenericValue("um");
				BigDecimal vlunitario = listacompraWrapper.findGenericValue("valorunitario");
				BigDecimal  vlmercadoria = listacompraWrapper.findGenericValue("valormercadoria");
				BigDecimal  aliquotaipi = listacompraWrapper.findGenericValue("aliquotaipi");
				BigDecimal  valoripi = listacompraWrapper.findGenericValue("valoripi");
				BigDecimal  valortotalcomipi = listacompraWrapper.findGenericValue("valortotalcomipi");
				Long volumeitem = listacompraWrapper.findGenericValue("volumeitem");
				BigDecimal  pesobruto = listacompraWrapper.findGenericValue("pesobruto");
				BigDecimal  pesoliquido = listacompraWrapper.findGenericValue("pesoliquido");
				String dataembarque =  listacompraWrapper.findGenericValue("dataembarqueitem");
				
				
				
				// incluir no Itens do Pedido do Workflow de pedidos Industrial 							
				List<NeoObject> itens = wrapper.findGenericValue("WPedInd.ItePIND");
				
				NeoObject itenspedido = AdapterUtils.createNewEntityInstance("ItePedI");
				
				
				EntityWrapper wrapperitenspedido = new EntityWrapper(itenspedido);
				//codigo do item 
				wrapperitenspedido.setValue("CodigoItemImportado", codigoitemoc);
				wrapperitenspedido.setValue("CodItI", codigoitemoc);
				
				//descrição do item
				wrapperitenspedido.setValue("DescricaoDoItemImportado", dscritemoc);
				wrapperitenspedido.setValue("DeItIND", dscritemoc);
				
				//Quantidade
				wrapperitenspedido.setValue("QtdIND", quantidade);				
				
				//Unidade de Medida
				wrapperitenspedido.setValue("UnidadeDeMedidaItemImp", unidademedida);
				wrapperitenspedido.setValue("UnMedI", unidademedida);
				
				//valor unitario
				wrapperitenspedido.setValue("ValUniI", vlunitario);
				wrapperitenspedido.setValue("ValorUnitarioDoItemImportado", vlunitario);
				
				// valor Mercadoria 
				wrapperitenspedido.setValue("ValMSI", vlmercadoria);
				
				//aliquota ipi
				wrapperitenspedido.setValue("AliquotaIPIItemImportado", aliquotaipi);
				wrapperitenspedido.setValue("AliqIPi",aliquotaipi);
				
				//valor ipi
				wrapperitenspedido.setValue("ValorDoIPI", valoripi);
				
				//Valor total com ipi 
				wrapperitenspedido.setValue("VlmerCI", valortotalcomipi);
				
				// Data de embarque Item 
			    wrapperitenspedido.setValue("DesDaEI", true);
			   			
			    wrapperitenspedido.setValue("DataDeEmbarqueImportado", dataembarque);
			    
			    wrapperitenspedido.setValue("DataDeEmbarqueItem", dataembarque);
			   			
				//Volume Item 
				wrapperitenspedido.setValue("VolItem", volumeitem);
				
				//Peso Liquido
				wrapperitenspedido.setValue("PesoLiquido", pesoliquido);
				
				//Peso Bruto
				wrapperitenspedido.setValue("PesoBruto", pesobruto);
				
								
				
				// Tipo de inclusao
				wrapperitenspedido.setValue("TipoDeInclusao", "Importado");
				//prazo medio,descontos,tipo tabela,rentabilidade 
				
				wrapperitenspedido.setValue("InDePer",BigDecimal.ZERO);
				wrapperitenspedido.setValue("InfDesE",BigDecimal.ZERO);
				wrapperitenspedido.setValue("infCAn",BigDecimal.ZERO);
				
				wrapperitenspedido.setValue("RentIND", "Importado");
				
				
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
