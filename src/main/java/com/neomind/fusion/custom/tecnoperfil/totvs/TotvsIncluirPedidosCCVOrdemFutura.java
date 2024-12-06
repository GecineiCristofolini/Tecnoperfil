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

public class TotvsIncluirPedidosCCVOrdemFutura implements AdapterInterface
{

	private static final Log log = LogFactory.getLog(TotvsIncluirPedidosCCVOrdemFutura.class);

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
			
			wraperpedido.findField("WPedido.StatPed").setValue("Importado Com Sucesso - Verifique no Campo Abaixo se Aparece Numero Pedido Totvs Se não entre em contato com TI!!");

			
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

			
			String tipooperacaoordem = wraperpedido.findGenericValue("WPedido.TipoDeOperacaoOrdem.x5_chave");
			
			
			if (tipooperacaoordem != null) {

				

			// Informações para cabeçalho do pedido "Order"

			String idClienteFusiono = wraperpedido.findGenericValue("WPedido.ClientesTovs.ClienteTotvs.a1_zfusion");
			idClienteFusiono = idClienteFusiono.trim();
			GregorianCalendar emissaoo = wraperpedido.findGenericValue("WPedido.DataEm");
			String emissaofusiono = NeoCalendarUtils.dateToString(emissaoo);

			String anoo = emissaofusiono.substring(6, 10);
			String meso = emissaofusiono.substring(3, 5);
			String diao = emissaofusiono.substring(0, 2);
			String dataemissaoo = anoo + meso + diao;
			String numeropedidofusiono = wraperpedido.findGenericValue("WPedido.NumPed");
			numeropedidofusiono = numeropedidofusiono +"o";
			String condpago = wraperpedido.findGenericValue("WPedido.CondPagtovs.e4_codigo");
			String transportadorao = wraperpedido.findGenericValue("WPedido.Transportadora.a4_cod");
			BigDecimal freteo = wraperpedido.findGenericValue("WPedido.ValFret");
			
			String redespashoo = "";
			String clienteentregao = "";
			String lojaentregao = "";

			String mensagem1 = wraperpedido.findGenericValue("WPedido.MensagemParaNotaDeEntregaFuturaFutura");
			String mensagem2 = wraperpedido.findGenericValue("WPedido.MensagemParaNota2");
			
			StringBuilder mensagem = new StringBuilder();
			
			
			mensagem.append(mensagem1);
			mensagem.append("\n");
			mensagem.append(mensagem2);
			
			
			

			String mensagemnotao = mensagem.toString();
			String tipoliberacaoo = wraperpedido.findGenericValue("WPedido.TipLib.Codigo");
			String tipofreteo = wraperpedido.findGenericValue("WPedido.TipoDeFrete.SiglaTipoFrete");
			BigDecimal pesobrutoo = wraperpedido.findGenericValue("WPedido.PesoBrutoTotal");
			BigDecimal pesoliquidoo = wraperpedido.findGenericValue("WPedido.PesoLiquidoTotal");
			LocalDateTime d05o = LocalDateTime.now();
			DateTimeFormatter fmt2o = DateTimeFormatter.ofPattern("yyyyMMdd");
			String dateinclusaoo = d05o.format(fmt2o);

			

			
			Long volumeo = wraperpedido.findGenericValue("WPedido.VolumeTotal");
			
			BigDecimal despesa = BigDecimal.ZERO;
			BigDecimal valoripi = wraperpedido.findGenericValue("WPedido.VltoIPI");
			BigDecimal valoripisobrefrete = wraperpedido.findGenericValue("WPedido.VIPIFre");
			despesa = despesa.add(valoripi).add(valoripisobrefrete).add(freteo);
			
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
			ordertotvso.setC5_DESPESA(despesa);

			// Fim do Cabeçario Pedido

			// Vendedores de Vendas ordem

				
			    ordertotvso.setC5_VEND1("");
			    ordertotvso.setC5_COMIS1(BigDecimal.ZERO);
			    ordertotvso.setC5_VEND2("");
			    ordertotvso.setC5_COMIS2(BigDecimal.ZERO);
			    ordertotvso.setC5_VEND3("");
			    ordertotvso.setC5_COMIS3(BigDecimal.ZERO);
			    ordertotvso.setC5_VEND4("");
			    ordertotvso.setC5_COMIS4(BigDecimal.ZERO);
			    ordertotvso.setC5_VEND5("");
			    ordertotvso.setC5_COMIS5(BigDecimal.ZERO);
				
			

			// Fim do Vendedores 

			// Busca Itens do Pedido Para Protheus ordem

			List<Itens> listitenso = new ArrayList<Itens>();

			String tabelaprecoo = "";
			String tipooperacaoo = wraperpedido.findGenericValue("WPedido.TipoDeOperacaoOrdem.x5_chave");
			GregorianCalendar dataembarqueo = wraperpedido.findGenericValue("WPedido.PrevEmb");
			String prevembarquepedidoo = NeoCalendarUtils.dateToString(dataembarqueo);

			String anoemo = prevembarquepedidoo.substring(6, 10);
			String mesemo = prevembarquepedidoo.substring(3, 5);
			String diaemo = prevembarquepedidoo.substring(0, 2);
			String dtembarquepedidoo = anoemo + mesemo + diaemo;
			String ordemcomprao = wraperpedido.findGenericValue("WPedido.OrdCom");

			List<NeoObject> itensfusiono = wraperpedido.findGenericValue("WPedido.PItenPed");

			for (NeoObject itensPedidoo : itensfusiono)
			{

				EntityWrapper itensPedidoWrappero = new EntityWrapper(itensPedidoo);

				String codigoprodutoo = itensPedidoWrappero.findGenericValue("CodItem");
				BigDecimal quantidadeo = itensPedidoWrappero.findGenericValue("QtdTotvs");
				BigDecimal vlunitarioo = itensPedidoWrappero.findGenericValue("ValUni");
				BigDecimal valormerco = itensPedidoWrappero.findGenericValue("vlmerc");
				String tabelaprecoitemo = itensPedidoWrappero
						.findGenericValue("TabelaDePrecoTotvs.da0_codtab");

				if (tabelaprecoitemo == null)
				{
					tabelaprecoo = tabelaprecoitemo;
				}

				String tipooperacaoitemo = tipooperacaoo.trim();

				Boolean desejaembarqueitemo = itensPedidoWrappero.findGenericValue("TeDaEmIT");
				String dtembarqueo = "";

				if (desejaembarqueitemo)
				{

					GregorianCalendar dataembarqueitemo = itensPedidoWrappero.findGenericValue("DatEmbI");
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

				String ordemitemo = itensPedidoWrappero.findGenericValue("OrderItem");

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
				itenspedo.setC6_ITEMCTA("200200000"); // conta contabil ccv 
				itenspedo.setIdItemFusion(neoido);
				itenspedo.setC6_COMIS1(BigDecimal.ZERO);
				itenspedo.setC6_COMIS2(BigDecimal.ZERO);
				itenspedo.setC6_COMIS3(BigDecimal.ZERO);
				itenspedo.setC6_COMIS4(BigDecimal.ZERO);
				itenspedo.setC6_COMIS5(BigDecimal.ZERO);

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
