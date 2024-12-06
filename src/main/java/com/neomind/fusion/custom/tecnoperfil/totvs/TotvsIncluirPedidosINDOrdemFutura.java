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

public class TotvsIncluirPedidosINDOrdemFutura implements AdapterInterface
{

	private static final Log log = LogFactory.getLog(TotvsIncluirPedidosINDOrdemFutura.class);

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

			
			
			String tipooperacaoordem = wraperpedido.findGenericValue("WPedInd.TipoDeOperacaoOrdem.x5_chave");
			
			
			if (tipooperacaoordem != null) {

				

			// Informações para cabeçalho do pedido "Order"

			String idClienteFusiono = wraperpedido.findGenericValue("WPedInd.ClienteTotvs.ClienteTotvs.a1_zfusion");
			idClienteFusiono = idClienteFusiono.trim();
			GregorianCalendar emissaoo = wraperpedido.findGenericValue("WPedInd.DatEMI");
			String emissaofusiono = NeoCalendarUtils.dateToString(emissaoo);

			String anoo = emissaofusiono.substring(6, 10);
			String meso = emissaofusiono.substring(3, 5);
			String diao = emissaofusiono.substring(0, 2);
			String dataemissaoo = anoo + meso + diao;
			String numeropedidofusiono = wraperpedido.findGenericValue("WPedInd.NumPedI");
			numeropedidofusiono = numeropedidofusiono +"o";
			String condpago = wraperpedido.findGenericValue("WPedInd.CondpagTotvs.e4_codigo");
			String transportadorao = wraperpedido.findGenericValue("WPedInd.TraspoficialT.a4_cod");
			BigDecimal freteo = wraperpedido.findGenericValue("WPedInd.VlFretI");
			
			String redespashoo = "";
			String clienteentregao = "";
			String lojaentregao = "";

			String mensagem1 = wraperpedido.findGenericValue("WPedInd.mensagemnotaentrega");
			String mensagem2 = wraperpedido.findGenericValue("WPedInd.mensagementregafutura2");
			
			StringBuilder mensagem = new StringBuilder();
			
			
			mensagem.append(mensagem1);
			mensagem.append("\n");
			mensagem.append(mensagem2);
			
			
			
			

			String mensagemnotao = mensagem.toString();
			String tipoliberacaoo = wraperpedido.findGenericValue("WPedInd.TipoDeLiberacao.Codigo");
			String tipofreteo = wraperpedido.findGenericValue("WPedInd.TipFINd.TipoFreteTotvs");
			BigDecimal pesobrutoo = wraperpedido.findGenericValue("WPedInd.PesoBruto");
			BigDecimal pesoliquidoo = wraperpedido.findGenericValue("WPedInd.PesoLiquido");
			LocalDateTime d05o = LocalDateTime.now();
			DateTimeFormatter fmt2o = DateTimeFormatter.ofPattern("yyyyMMdd");
			String dateinclusaoo = d05o.format(fmt2o);

			

			
			Long volumeo = wraperpedido.findGenericValue("WPedInd.Volumes");
			
			BigDecimal despesa = BigDecimal.ZERO;
			BigDecimal valoripi = wraperpedido.findGenericValue("WPedInd.ValTIPIM");
			BigDecimal valoripisobrefrete = wraperpedido.findGenericValue("WPedInd.VlIPIFI");
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
