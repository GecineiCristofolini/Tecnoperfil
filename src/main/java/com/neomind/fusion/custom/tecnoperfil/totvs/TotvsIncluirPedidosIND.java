package com.neomind.fusion.custom.tecnoperfil.totvs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.custom.tecnoperfil.totvs.entidades.Itens;
import com.neomind.fusion.custom.tecnoperfil.totvs.entidades.Order;
import com.neomind.fusion.custom.tecnoperfil.totvs.entidades.Pedido;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;
import com.neomind.fusion.workflow.exception.WorkflowException;
import com.neomind.util.NeoCalendarUtils;

public class TotvsIncluirPedidosIND implements AdapterInterface
{

	private static final Log log = LogFactory.getLog(TotvsIncluirPedidosIND.class);

	@Override
	public void back(EntityWrapper arg0, Activity arg1)
	{
		// Back dfd

	}

	@Override
	public void start(Task arg0, EntityWrapper wraperpedido, Activity arg2)
	{

		try
		{
			
			
			
			log.debug("Iniciar inclusao de pedido");

			String json = IncluirPedido(wraperpedido);
			log.debug("informacoes retornadas + " + json);
			IncluirPedido incluirPedido = new IncluirPedido();
			incluirPedido.IntegracaoIncluirPedido(json, wraperpedido);
			log.debug("Pedido incluído com sucesso.");
			
			wraperpedido.findField("WPedInd.StatPedI").setValue("Importado Com Sucesso - Verifique no Campo Abaixo se Aparece Numero Pedido Totvs Se não entre em contato com TI!!");

			
			Timer timer = new Timer(); // Creating a Timer object from the timer class

			TimerTask task1 = new TimerTask()
			{

				@Override
				public void run()
				{

					System.out.println("Fim da Importação do Pedido");

				}

			};

			timer.schedule(task1, 8000);
			
						

		}
		catch (WorkflowException e)
		{
			e.printStackTrace();
			log.error("Erro incluir pedido", e);
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Erro ao incluir pedido", e);
			throw new WorkflowException("Erro ao incluir pedido" + e.getCause());
		}

	}

	public String IncluirPedido(EntityWrapper wraperpedido)
	{

		try
		{
			
			
			
			Pedido pedidototvs = new Pedido();
			
			
			//Busca Lista De pedidos

			List<Order> ListPedido = new ArrayList<Order>();

			// Informações para cabeçalho do pedido "Order"

			String idClienteFusion = wraperpedido
					.findGenericValue("WPedInd.ClienteTotvs.ClienteTotvs.a1_zfusion");
			GregorianCalendar emissao = wraperpedido.findGenericValue("WPedInd.DatEMI");
			String emissaofusion = NeoCalendarUtils.dateToString(emissao);

			String ano = emissaofusion.substring(6, 10);
			String mes = emissaofusion.substring(3, 5);
			String dia = emissaofusion.substring(0, 2);
			String dataemissao = ano + mes + dia;
			String numeropedidofusion = wraperpedido.findGenericValue("WPedInd.NumPedI");
			String condpag = wraperpedido.findGenericValue("WPedInd.CondpagTotvs.e4_codigo");
			String transportadora = wraperpedido.findGenericValue("WPedInd.TraspoficialT.a4_cod");
			BigDecimal frete = wraperpedido.findGenericValue("WPedInd.VlFretI");
			
			String redespasho = "";
			String clienteentrega = "";
			String lojaentrega = "";

			Boolean tementregadif = wraperpedido.findGenericValue("WPedInd.TemEntrdifTotvs.InformeSimOuNao");
			String tipoentrega = wraperpedido.findGenericValue("WPedInd.TipoDeEntregaDif.Codigo");
			
			if (tementregadif) {

			 if (tipoentrega.equals("3"))
			{

				redespasho = wraperpedido.findGenericValue("WPedInd.TransportadoraRedespacho.a4_cod");

			}
			
			 if (tipoentrega.equals("2") || tipoentrega.equals("4"))
			{

				clienteentrega = wraperpedido.findGenericValue("WPedInd.ClienteEntrega.a1_cod");
				lojaentrega = wraperpedido.findGenericValue("WPedInd.ClienteEntrega.a1_loja");

			}

			}

			String mensagemnota = wraperpedido.findGenericValue("WPedInd.MensagemNota");
			String tipoliberacao = wraperpedido.findGenericValue("WPedInd.TipoDeLiberacao.Codigo");
			String tipofrete = wraperpedido.findGenericValue("WPedInd.TipFINd.TipoFreteTotvs");
			BigDecimal pesobruto = wraperpedido.findGenericValue("WPedInd.PesoBruto");
			BigDecimal pesoliquido = wraperpedido.findGenericValue("WPedInd.PesoLiquido");
			LocalDateTime d05 = LocalDateTime.now();
			DateTimeFormatter fmt2 = DateTimeFormatter.ofPattern("yyyyMMdd");
			String dateinclusao = d05.format(fmt2);

			

			
			Long volume = wraperpedido.findGenericValue("WPedInd.Volumes");

			Order ordertotvs = new Order();

			ordertotvs.setIdClienteFusion(idClienteFusion);
			ordertotvs.setC5_EMISSAO(dateinclusao);
			ordertotvs.setC5_ZFUSION(numeropedidofusion);
			ordertotvs.setC5_CONDPAG(condpag);
			ordertotvs.setC5_TRANSP(transportadora);
			ordertotvs.setC5_FRETE(frete);
			ordertotvs.setC5_SEGURO(BigDecimal.ZERO);
			ordertotvs.setC5_REDESP(redespasho);
			ordertotvs.setC5_ZMENNF(mensagemnota);
			ordertotvs.setC5_MOEDA(1L);
			ordertotvs.setC5_TIPLIB(tipoliberacao);
			ordertotvs.setC5_TPFRETE(tipofrete);
			ordertotvs.setC5_PBRUTO(pesobruto);
			ordertotvs.setC5_PESOL(pesoliquido);
			ordertotvs.setC5_ZDTFUSI(dataemissao);
			ordertotvs.setC5_CLIENT(clienteentrega);
			ordertotvs.setC5_LOJAENT(lojaentrega);
			ordertotvs.setC5_VOLUME1(volume);

			// Fim do Cabeçario Pedido

			
			// Vendedores de Vendas 

			boolean vendcompartilhada =wraperpedido.findGenericValue("WPedInd.EVendaCompartilhada");
					

			String Agente = wraperpedido.findGenericValue("WPedInd.AgVeInd.ACodRep");
			BigDecimal ComissaoAgente = wraperpedido.findGenericValue("WPedInd.ComEspI");
			String Agentecompartilhado = wraperpedido.findGenericValue("WPedInd.AgentCompartilhado.ACodRep");
			BigDecimal ComissaoAgenteCompartilhado = wraperpedido.findGenericValue("WPedInd.ComissaoCompOf");
			
			if(vendcompartilhada) {
			
			ordertotvs.setC5_VEND1(Agente);
			ordertotvs.setC5_COMIS1(ComissaoAgente);
			ordertotvs.setC5_VEND2(Agentecompartilhado);
			ordertotvs.setC5_COMIS2(ComissaoAgenteCompartilhado);
			ordertotvs.setC5_VEND3("");
			ordertotvs.setC5_COMIS3(BigDecimal.ZERO);
			ordertotvs.setC5_VEND4("");
			ordertotvs.setC5_COMIS4(BigDecimal.ZERO);
			ordertotvs.setC5_VEND5("");
			ordertotvs.setC5_COMIS5(BigDecimal.ZERO);
            
            
			}else {
				
				ordertotvs.setC5_VEND1(Agente);
				ordertotvs.setC5_COMIS1(ComissaoAgente);
				ordertotvs.setC5_VEND2("");
				ordertotvs.setC5_COMIS2(BigDecimal.ZERO);
				ordertotvs.setC5_VEND3("");
				ordertotvs.setC5_COMIS3(BigDecimal.ZERO);
				ordertotvs.setC5_VEND4("");
				ordertotvs.setC5_COMIS4(BigDecimal.ZERO);
				ordertotvs.setC5_VEND5("");
				ordertotvs.setC5_COMIS5(BigDecimal.ZERO);
				
			}
			
			// Fim do Vendedores 

			// Busca Itens do Pedido Para Protheus

			List<Itens> listitens = new ArrayList<Itens>();

			String tabelapreco = "";
			String tipooperacao = wraperpedido.findGenericValue("WPedInd.TipoDeOperacao.x5_chave");
			GregorianCalendar dataembarque = wraperpedido.findGenericValue("WPedInd.PrevEmI");
			String prevembarquepedido = NeoCalendarUtils.dateToString(dataembarque);

			String anoem = prevembarquepedido.substring(6, 10);
			String mesem = prevembarquepedido.substring(3, 5);
			String diaem = prevembarquepedido.substring(0, 2);
			String dtembarquepedido = anoem + mesem + diaem;
			String ordemcompra = wraperpedido.findGenericValue("OrdComI");

			List<NeoObject> itensfusion = wraperpedido.findGenericValue("WPedInd.ItePIND");

			for (NeoObject itensPedido : itensfusion)
			{

				EntityWrapper itensPedidoWrapper = new EntityWrapper(itensPedido);

				String codigoproduto = itensPedidoWrapper.findGenericValue("CodItI");
				BigDecimal quantidade = itensPedidoWrapper.findGenericValue("QtdIND");
				BigDecimal vlunitario = itensPedidoWrapper.findGenericValue("ValUniI");
				BigDecimal valormerc = itensPedidoWrapper.findGenericValue("ValMSI");
				String tabelaprecoitem = itensPedidoWrapper
						.findGenericValue("TabelaDePrecoIND.da0_codtab");

				if (tabelaprecoitem != null)
				{
					tabelapreco = tabelaprecoitem;
				}

				String tipooperacaoitem = tipooperacao.trim();

				Boolean desejaembarqueitem = itensPedidoWrapper.findGenericValue("DesDaEI");
				String dtembarque = "";

				if (desejaembarqueitem)
				{

					GregorianCalendar dataembarqueitem = itensPedidoWrapper.findGenericValue("DatEMIT");
					String prevembarquepedidoitem = NeoCalendarUtils.dateToString(dataembarqueitem);

					String anoemitem = prevembarquepedidoitem.substring(6, 10);
					String mesemitem = prevembarquepedidoitem.substring(3, 5);
					String diaemitem = prevembarquepedidoitem.substring(0, 2);
					dtembarque = anoemitem + mesemitem + diaemitem;

				}
				else
				{

					dtembarque = dtembarquepedido;
				}

				String ordemitem = itensPedidoWrapper.findGenericValue("itemordem");

				Long iditem = itensPedidoWrapper.findGenericValue("neoId");

				String neoid = Long.toString(iditem);
				System.out.print(neoid);

				Itens itensped = new Itens();

				itensped.setC6_PRODUTO(codigoproduto);
				itensped.setC6_QTDVEN(quantidade);
				itensped.setC6_PRCVEN(vlunitario);
				itensped.setC6_VALOR(valormerc);
				itensped.setC6_OPER(tipooperacaoitem);
				itensped.setC6_ENTREG(dtembarque);
				itensped.setC6_NUMPCOM(ordemcompra);
				itensped.setC6_ITEMPC(ordemitem);
				itensped.setC6_ITEMCTA("200300000"); // conta contabil IND 
				itensped.setC6_COMIS1(BigDecimal.ZERO);
				itensped.setC6_COMIS2(BigDecimal.ZERO);
				itensped.setC6_COMIS3(BigDecimal.ZERO);
				itensped.setC6_COMIS4(BigDecimal.ZERO);
				itensped.setC6_COMIS5(BigDecimal.ZERO);
				itensped.setIdItemFusion(neoid);

				listitens.add(itensped);

			}

			ordertotvs.setC5_TABELA(tabelapreco);

			ordertotvs.setItens(listitens);

			ListPedido.add(ordertotvs);

			
			
			String tipooperacaoordem = wraperpedido.findGenericValue("WPedInd.TipoDeOperacaoOrdem.x5_chave");
			
			
			if (tipooperacaoordem != null) {

				

			// Informações para cabeçalho do pedido "Order"

			String idClienteFusiono = wraperpedido
					.findGenericValue("WPedInd.ClienteOrdem.a1_zfusion");
			GregorianCalendar emissaoo = wraperpedido.findGenericValue("WPedInd.DatEMI");
			String emissaofusiono = NeoCalendarUtils.dateToString(emissaoo);

			String anoo = emissaofusiono.substring(6, 10);
			String meso = emissaofusiono.substring(3, 5);
			String diao = emissaofusiono.substring(0, 2);
			String dataemissaoo = anoo + meso + diao;
			String numeropedidofusiono = wraperpedido.findGenericValue("WPedInd.NumPedI");
			numeropedidofusiono = numeropedidofusion +"o";
			String condpago = wraperpedido.findGenericValue("WPedInd.CondpagTotvs.e4_codigo");
			String transportadorao = wraperpedido.findGenericValue("WPedInd.TraspoficialT.a4_cod");
			BigDecimal freteo = wraperpedido.findGenericValue("WPedInd.VlFretI");
			
			String redespashoo = "";
			String clienteentregao = "";
			String lojaentregao = "";

			
			
			
			

			String mensagemnotao = wraperpedido.findGenericValue("");
			String tipoliberacaoo = wraperpedido.findGenericValue("WPedInd.TipoDeLiberacao.Codigo");
			String tipofreteo = wraperpedido.findGenericValue("WPedInd.TipFINd.TipoFreteTotvs");
			BigDecimal pesobrutoo = wraperpedido.findGenericValue("WPedInd.PesoBruto");
			BigDecimal pesoliquidoo = wraperpedido.findGenericValue("WPedInd.PesoLiquido");
			LocalDateTime d05o = LocalDateTime.now();
			DateTimeFormatter fmt2o = DateTimeFormatter.ofPattern("yyyyMMdd");
			String dateinclusaoo = d05o.format(fmt2o);

			

			
			Long volumeo = wraperpedido.findGenericValue("WPedInd.Volumes");

			Order ordertotvso = new Order();

			ordertotvso.setIdClienteFusion(idClienteFusiono);
			ordertotvso.setC5_EMISSAO(dateinclusaoo);
			ordertotvso.setC5_ZFUSION(numeropedidofusiono);
			ordertotvso.setC5_CONDPAG(condpago);
			ordertotvso.setC5_TRANSP(transportadorao);
			ordertotvso.setC5_FRETE(freteo);
			ordertotvso.setC5_SEGURO(BigDecimal.ZERO);
			ordertotvso.setC5_REDESP(redespashoo);
			ordertotvso.setC5_ZMENNF(mensagemnotao);
			ordertotvso.setC5_MOEDA(1L);
			ordertotvso.setC5_TIPLIB(tipoliberacaoo);
			ordertotvso.setC5_TPFRETE(tipofreteo);
			ordertotvso.setC5_PBRUTO(pesobrutoo);
			ordertotvso.setC5_PESOL(pesoliquidoo);
			ordertotvso.setC5_ZDTFUSI(dataemissaoo);
			ordertotvso.setC5_CLIENT(clienteentregao);
			ordertotvso.setC5_LOJAENT(lojaentregao);
			ordertotvso.setC5_VOLUME1(volumeo);

			// Fim do Cabeçario Pedido

			// Vendedores de Vendas ordem

				
				ordertotvs.setC5_VEND1("");
				ordertotvs.setC5_COMIS1(BigDecimal.ZERO);
				ordertotvs.setC5_VEND2("");
				ordertotvs.setC5_COMIS2(BigDecimal.ZERO);
				ordertotvs.setC5_VEND3("");
				ordertotvs.setC5_COMIS3(BigDecimal.ZERO);
				ordertotvs.setC5_VEND4("");
				ordertotvs.setC5_COMIS4(BigDecimal.ZERO);
				ordertotvs.setC5_VEND5("");
				ordertotvs.setC5_COMIS5(BigDecimal.ZERO);
				
			

			// Fim do Vendedores 

			// Busca Itens do Pedido Para Protheus ordem

			List<Itens> listitenso = new ArrayList<Itens>();

			String tabelaprecoo = "";
			String tipooperacaoo = wraperpedido.findGenericValue("WPedInd.TipoDeOperacaoOrdem.x5_chave");
			GregorianCalendar dataembarqueo = wraperpedido.findGenericValue("WPedInd.PrevEmI");
			String prevembarquepedidoo = NeoCalendarUtils.dateToString(dataembarqueo);

			String anoemo = prevembarquepedidoo.substring(6, 10);
			String mesemo = prevembarquepedidoo.substring(3, 5);
			String diaemo = prevembarquepedidoo.substring(0, 2);
			String dtembarquepedidoo = anoemo + mesemo + diaemo;
			String ordemcomprao = wraperpedido.findGenericValue("WPedInd.OrdComI");

			List<NeoObject> itensfusiono = wraperpedido.findGenericValue("WPedInd.ItePIND");

			for (NeoObject itensPedidoo : itensfusiono)
			{

				EntityWrapper itensPedidoWrappero = new EntityWrapper(itensPedidoo);

				String codigoprodutoo = itensPedidoWrappero.findGenericValue("CodItI");
				BigDecimal quantidadeo = itensPedidoWrappero.findGenericValue("QtdIND");
				BigDecimal vlunitarioo = itensPedidoWrappero.findGenericValue("ValUniI");
				BigDecimal valormerco = itensPedidoWrappero.findGenericValue("ValMSI");
				String tabelaprecoitemo = itensPedidoWrappero
						.findGenericValue("TabelaDePrecoIND.da0_codtab");

				if (tabelaprecoitemo == null)
				{
					tabelaprecoo = tabelaprecoitemo;
				}

				String tipooperacaoitemo = tipooperacaoo.trim();

				Boolean desejaembarqueitemo = itensPedidoWrappero.findGenericValue("DesDaEI");
				String dtembarqueo = "";

				if (desejaembarqueitemo)
				{

					GregorianCalendar dataembarqueitemo = itensPedidoWrappero.findGenericValue("DatEMIT");
					String prevembarquepedidoitemo = NeoCalendarUtils.dateToString(dataembarqueitemo);

					String anoemitemo = prevembarquepedidoitemo.substring(6, 10);
					String mesemitemo = prevembarquepedidoitemo.substring(3, 5);
					String diaemitemo = prevembarquepedidoitemo.substring(0, 2);
					dtembarqueo = anoemitemo + mesemitemo + diaemitemo;

				}
				else
				{

					dtembarqueo = dtembarquepedidoo;
				}

				String ordemitemo = itensPedidoWrappero.findGenericValue("itemordem");

				Long iditemo = itensPedidoWrappero.findGenericValue("neoId");

				String neoido = Long.toString(iditemo);
				

				Itens itenspedo = new Itens();

				itenspedo.setC6_PRODUTO(codigoprodutoo);
				itenspedo.setC6_QTDVEN(quantidadeo);
				itenspedo.setC6_PRCVEN(vlunitarioo);
				itenspedo.setC6_VALOR(valormerco);
				itenspedo.setC6_OPER(tipooperacaoitemo);
				itenspedo.setC6_ENTREG(dtembarqueo);
				itenspedo.setC6_NUMPCOM(ordemcomprao);
				itenspedo.setC6_ITEMPC(ordemitemo);
				itenspedo.setC6_ITEMCTA("200300000"); // conta contabil IND 
				itenspedo.setIdItemFusion(neoido);

				listitenso.add(itenspedo);

			}

			ordertotvso.setC5_TABELA(tabelaprecoo);

			ordertotvso.setItens(listitenso);

			ListPedido.add(ordertotvso);

			
			}
			
			pedidototvs.setOrder(ListPedido);

			String json = new Gson().toJson(pedidototvs);

			System.out.println(json);

			return json;
	
			
			


		}
		catch (Exception e)
		{

			System.out.print(e.getMessage());
			e.printStackTrace();
			throw new WorkflowException("Erro ao incluir pedido" + e.getCause());

		}
		
	}
		
		

	
}
