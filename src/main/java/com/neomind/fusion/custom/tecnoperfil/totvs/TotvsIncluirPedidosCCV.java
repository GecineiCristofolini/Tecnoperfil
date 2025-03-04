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

public class TotvsIncluirPedidosCCV implements AdapterInterface
{

	private static final Log log = LogFactory.getLog(TotvsIncluirPedidosCCV.class);

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

			wraperpedido.findField("WPedido.StatPed").setValue(
					"Importado Com Sucesso - Verifique no Campo Abaixo se Aparece Numero Pedido Totvs Se não entre em contato com TI!!");

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
					.findGenericValue("WPedido.ClientesTovs.ClienteTotvs.a1_zfusion");
			idClienteFusion = idClienteFusion.trim();
			GregorianCalendar emissao = wraperpedido.findGenericValue("WPedido.DataEm");
			String emissaofusion = NeoCalendarUtils.dateToString(emissao);

			String ano = emissaofusion.substring(6, 10);
			String mes = emissaofusion.substring(3, 5);
			String dia = emissaofusion.substring(0, 2);
			String dataemissao = ano + mes + dia;
			String numeropedidofusion = wraperpedido.findGenericValue("WPedido.NumPed");
			String condpag = wraperpedido.findGenericValue("WPedido.CondPagtovs.e4_codigo");
			String transportadora = wraperpedido.findGenericValue("WPedido.Transportadora.a4_cod");
			BigDecimal frete = wraperpedido.findGenericValue("WPedido.ValFret");

			String redespasho = "";
			String clienteentrega = "";
			String lojaentrega = "";

			Boolean tementregadif = wraperpedido.findGenericValue("WPedido.TemEntredif.InformeSimOuNao");
			String tipoentrega = wraperpedido.findGenericValue("WPedido.TipoDeEntrega.Codigo");

			if (tementregadif)
			{

				if (tipoentrega.equals("3"))
				{

					redespasho = wraperpedido
							.findGenericValue("WPedido.TransportadoraRedespacho.a4_cod");

				}

				if (tipoentrega.equals("2") || tipoentrega.equals("4") || tipoentrega.equals("5"))
				{

					clienteentrega = wraperpedido.findGenericValue("WPedido.ClienteEntrega.a1_cod");
					lojaentrega = wraperpedido.findGenericValue("WPedido.ClienteEntrega.a1_loja");

				}

			}

			String infmensagem = wraperpedido.findGenericValue("WPedido.InformeMensagemNaNota");
			String ordemcompra = wraperpedido.findGenericValue("WPedido.OrdCom");
			String suframa = wraperpedido
					.findGenericValue("WPedido.ClientesTovs.ClienteTotvs.a1_suframa");
			String LocalEntrega = wraperpedido.findGenericValue("WPedido.LocalEntrega");

			if (ordemcompra == null || ordemcompra.equals("")|| ordemcompra.isBlank())
			{

				ordemcompra = "";

			}
			else
			{

				ordemcompra = "Ordem de Compra:" + ordemcompra;
			}

			if (suframa == null || suframa.equals("") || suframa.isBlank())
			{

				suframa = "";

			}
			else
			{

				suframa = "Inscricao Suframa:" + suframa;
			}

			if (LocalEntrega == null || LocalEntrega.equals(""))
			{

				LocalEntrega = "";
			}

			StringBuilder mensagem = new StringBuilder();
			mensagem.append(infmensagem);
			mensagem.append("\n");
			mensagem.append(ordemcompra);
			mensagem.append("\n");
			mensagem.append(suframa);
			mensagem.append("\n");
			mensagem.append(LocalEntrega);

			String mensagemnota = mensagem.toString();

			String tipoliberacao = wraperpedido.findGenericValue("WPedido.TipLib.Codigo");
			String tipofrete = wraperpedido.findGenericValue("WPedido.TipoDeFrete.SiglaTipoFrete");
			BigDecimal pesobruto = wraperpedido.findGenericValue("WPedido.PesoBrutoTotal");
			BigDecimal pesoliquido = wraperpedido.findGenericValue("WPedido.PesoLiquidoTotal");
			LocalDateTime d05 = LocalDateTime.now();
			DateTimeFormatter fmt2 = DateTimeFormatter.ofPattern("yyyyMMdd");
			String dateinclusao = d05.format(fmt2);

			Long volume = wraperpedido.findGenericValue("WPedido.VolumeTotal");

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
			ordertotvs.setC5_PBRUTO(BigDecimal.ZERO);
			ordertotvs.setC5_PESOL(BigDecimal.ZERO);
			ordertotvs.setC5_ZDTFUSI(dataemissao);
			ordertotvs.setC5_CLIENT(clienteentrega);
			ordertotvs.setC5_LOJAENT(lojaentrega);
			ordertotvs.setC5_VOLUME1(volume);
			ordertotvs.setC5_DESPESA(BigDecimal.ZERO);

			List<Itens> listitens = new ArrayList<Itens>();

			String tabelapreco = "";
			String tipooperacao = wraperpedido.findGenericValue("WPedido.TipoDeOperacao.x5_chave");
			GregorianCalendar dataembarque = wraperpedido.findGenericValue("WPedido.PrevEmb");
			String prevembarquepedido = NeoCalendarUtils.dateToString(dataembarque);

			String anoem = prevembarquepedido.substring(6, 10);
			String mesem = prevembarquepedido.substring(3, 5);
			String diaem = prevembarquepedido.substring(0, 2);
			String dtembarquepedido = anoem + mesem + diaem;

			List<NeoObject> itensfusion = wraperpedido.findGenericValue("WPedido.PItenPed");

			for (NeoObject itensPedido : itensfusion)
			{

				EntityWrapper itensPedidoWrapper = new EntityWrapper(itensPedido);

				String codigoproduto = itensPedidoWrapper.findGenericValue("CodItem");
				BigDecimal quantidade = itensPedidoWrapper.findGenericValue("QtdTotvs");
				BigDecimal vlunitario = itensPedidoWrapper.findGenericValue("ValUni");
				BigDecimal valormerc = itensPedidoWrapper.findGenericValue("vlmerc");
				String tabelaprecoitem = itensPedidoWrapper
						.findGenericValue("TabelaDePrecoTotvs.da0_codtab");

				if (tabelaprecoitem != null)
				{
					tabelapreco = tabelaprecoitem;
				}

				String tipooperacaoitem = tipooperacao.trim();

				Boolean desejaembarqueitem = itensPedidoWrapper.findGenericValue("TeDaEmIT");
				String dtembarque = "";

				if (desejaembarqueitem)
				{

					GregorianCalendar dataembarqueitem = itensPedidoWrapper.findGenericValue("DatEmbI");
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

				String ordemitem = itensPedidoWrapper.findGenericValue("OrderItem");

				Long iditem = itensPedidoWrapper.findGenericValue("neoId");
				
				String ordemcompraNUMCOM = wraperpedido.findGenericValue("WPedido.OrdCom");

				String neoid = Long.toString(iditem);
				System.out.print(neoid);

				Itens itensped = new Itens();

				itensped.setC6_PRODUTO(codigoproduto);
				itensped.setC6_QTDVEN(quantidade);
				itensped.setC6_PRCVEN(vlunitario);
				itensped.setC6_VALOR(valormerc);
				itensped.setC6_OPER(tipooperacaoitem);
				itensped.setC6_ENTREG(dtembarque);
				itensped.setC6_NUMPCOM(ordemcompraNUMCOM);
				itensped.setC6_ITEMPC(ordemitem);
				itensped.setC6_ITEMCTA("200200000"); // conta contabil ccv

				// Comissao POr Linha 

				BigDecimal comissao1 = itensPedidoWrapper.findGenericValue("ComissaoAgenteTLV");
				BigDecimal comissao2 = itensPedidoWrapper.findGenericValue("ComissaoAgenteRV");
				BigDecimal comissao3 = itensPedidoWrapper.findGenericValue("ComissaoAgenteEntrega");
				BigDecimal comissao4 = itensPedidoWrapper.findGenericValue("ComissaoEntregatlv");
				BigDecimal comissao5 = BigDecimal.ZERO;
				String Vendedor1 = itensPedidoWrapper.findGenericValue("AgenteTLV");
				String Vendedor2 = itensPedidoWrapper.findGenericValue("Agenterv");
				String Vendedor3 = itensPedidoWrapper.findGenericValue("AgenteEntregaRV");
				String Vendedor4 = itensPedidoWrapper.findGenericValue("AgenteEntregaTLV");
				String Vendedor5 = "";

				ordertotvs.setC5_VEND1(Vendedor1);
				ordertotvs.setC5_COMIS1(BigDecimal.ZERO);
				ordertotvs.setC5_VEND2(Vendedor2);
				ordertotvs.setC5_COMIS2(BigDecimal.ZERO);
				ordertotvs.setC5_VEND3(Vendedor3);
				ordertotvs.setC5_COMIS3(BigDecimal.ZERO);
				ordertotvs.setC5_VEND4(Vendedor4);
				ordertotvs.setC5_COMIS4(BigDecimal.ZERO);
				ordertotvs.setC5_VEND5(Vendedor5);
				ordertotvs.setC5_COMIS5(BigDecimal.ZERO);

				itensped.setC6_COMIS1(comissao1);
				itensped.setC6_COMIS2(comissao2);
				itensped.setC6_COMIS3(comissao3);
				itensped.setC6_COMIS4(comissao4);
				itensped.setC6_COMIS5(comissao5);
				itensped.setIdItemFusion(neoid);

				listitens.add(itensped);

			}

			ordertotvs.setC5_TABELA(tabelapreco);

			ordertotvs.setItens(listitens);

			ListPedido.add(ordertotvs);

			String tipooperacaoordem = wraperpedido
					.findGenericValue("WPedido.TipoDeOperacaoOrdem.x5_chave");
			
			
			
			if (tipooperacaoordem != null)
			{
				tipooperacaoordem = tipooperacaoordem.trim();

				if (!tipooperacaoordem.equals("03"))
				{

					// Informações para cabeçalho do pedido "Order"

					String idClienteFusiono = wraperpedido
							.findGenericValue("ClienteOrdem.a1_zfusion");
					idClienteFusiono = idClienteFusion.trim();
					GregorianCalendar emissaoo = wraperpedido.findGenericValue("WPedido.DataEm");
					String emissaofusiono = NeoCalendarUtils.dateToString(emissaoo);

					String anoo = emissaofusiono.substring(6, 10);
					String meso = emissaofusiono.substring(3, 5);
					String diao = emissaofusiono.substring(0, 2);
					String dataemissaoo = anoo + meso + diao;
					String numeropedidofusiono = wraperpedido.findGenericValue("WPedido.NumPed");
					numeropedidofusiono = numeropedidofusiono + "o";
					String condpago = wraperpedido.findGenericValue("WPedido.CondPagtovs.e4_codigo");
					String transportadorao = wraperpedido
							.findGenericValue("WPedido.Transportadora.a4_cod");
					BigDecimal freteo = wraperpedido.findGenericValue("WPedido.ValFret");

					String redespashoo = "";
					String clienteentregao = "";
					String lojaentregao = "";

					String mensagemnotao = "";
					String tipoliberacaoo = wraperpedido.findGenericValue("WPedido.TipLib.Codigo");
					String tipofreteo = wraperpedido
							.findGenericValue("WPedido.TipoDeFrete.SiglaTipoFrete");
					BigDecimal pesobrutoo = wraperpedido.findGenericValue("WPedido.PesoBrutoTotal");
					BigDecimal pesoliquidoo = wraperpedido.findGenericValue("WPedido.PesoLiquidoTotal");
					LocalDateTime d05o = LocalDateTime.now();
					DateTimeFormatter fmt2o = DateTimeFormatter.ofPattern("yyyyMMdd");
					String dateinclusaoo = d05o.format(fmt2o);

					Long volumeo = wraperpedido.findGenericValue("WPedido.VolumeTotal");

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
					ordertotvso.setC5_PBRUTO(BigDecimal.ZERO);
					ordertotvso.setC5_PESOL(BigDecimal.ZERO);
					ordertotvso.setC5_ZDTFUSI(dataemissaoo);
					ordertotvso.setC5_CLIENT(clienteentregao);
					ordertotvso.setC5_LOJAENT(lojaentregao);
					ordertotvso.setC5_VOLUME1(volumeo);
					ordertotvso.setC5_DESPESA(BigDecimal.ZERO);

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
					String tipooperacaoo = wraperpedido
							.findGenericValue("WPedido.TipoDeOperacaoOrdem.x5_chave");
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

							GregorianCalendar dataembarqueitemo = itensPedidoWrappero
									.findGenericValue("DatEmbI");
							String prevembarquepedidoitemo = NeoCalendarUtils
									.dateToString(dataembarqueitemo);

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
						String ordemcompraNUMCOMO = wraperpedido.findGenericValue("WPedido.OrdCom");
						

						Itens itenspedo = new Itens();

						itenspedo.setC6_PRODUTO(codigoprodutoo);
						itenspedo.setC6_QTDVEN(quantidadeo);
						itenspedo.setC6_PRCVEN(vlunitarioo);
						itenspedo.setC6_VALOR(valormerco);
						itenspedo.setC6_OPER(tipooperacaoitemo);
						itenspedo.setC6_ENTREG(dtembarqueo);
						itenspedo.setC6_NUMPCOM(ordemcompraNUMCOMO);
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
