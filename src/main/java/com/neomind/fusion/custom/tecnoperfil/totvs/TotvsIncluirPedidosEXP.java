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

public class TotvsIncluirPedidosEXP implements AdapterInterface
{

	private static final Log log = LogFactory.getLog(TotvsIncluirPedidosEXP.class);

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

			wraperpedido.findField("PedidoEXP.Situacao").setValue(
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
					.findGenericValue("PedidoEXP.ClienteExportacao.ClienteTotvs.a1_zfusion");
			idClienteFusion = idClienteFusion.trim();
			GregorianCalendar emissao = wraperpedido.findGenericValue("PedidoEXP.DataDeEmissao");
			String emissaofusion = NeoCalendarUtils.dateToString(emissao);

			String ano = emissaofusion.substring(6, 10);
			String mes = emissaofusion.substring(3, 5);
			String dia = emissaofusion.substring(0, 2);
			String dataemissao = ano + mes + dia;
			String numeropedidofusion = wraperpedido.findGenericValue("PedidoEXP.NumPed");
			String condpag = wraperpedido.findGenericValue("PedidoEXP.CondicaoDePagamentoTotvs.e4_codigo");
			String transportadora = wraperpedido.findGenericValue("PedidoEXP.TransportadoraTtovs.a4_cod");
			if (transportadora == null) {
				transportadora = "";
			}
			BigDecimal frete = wraperpedido.findGenericValue("PedidoEXP.ValorFreteOfficial");
			BigDecimal seguro = wraperpedido.findGenericValue("PedidoEXP.ValorSeguroKugel");

			String redespasho = "";
			String clienteentrega = "";
			String lojaentrega = "";
//         Exportação Tem algumas informaçoes que nao precisa por enquento entao sera documentado
//			Boolean tementregadif = wraperpedido
//					.findGenericValue("TemEntregaDiferenciada.InformeSimOuNao");
//			String tipoentrega = wraperpedido.findGenericValue("TipoDeEntrega.Codigo");
//
//			if (tementregadif)
//			{
//
//				if (tipoentrega.equals("3"))
//				{
//
//					redespasho = wraperpedido.findGenericValue("TransportadoraRedespacho.a4_cod");
//
//				}
//
//				if (tipoentrega.equals("2") || tipoentrega.equals("4"))
//				{
//
//					clienteentrega = wraperpedido.findGenericValue("ClienteEntrrega.a1_cod");
//					lojaentrega = wraperpedido.findGenericValue("ClienteEntrrega.a1_loja");
//
//				}
//
//			}
//
//			String infmensagem = wraperpedido.findGenericValue("InformeMensagemNaNota");
//			String ordemcompra = wraperpedido.findGenericValue("OrdComHDP");
//			String suframa = wraperpedido.findGenericValue("ClienteCadastratoTotvs.ClienteTotvs.a1_suframa");
//			String LocalEntrega = wraperpedido.findGenericValue("EnderecoDeEntregaOficial");
//
//			if (ordemcompra == null || ordemcompra.equals("") || ordemcompra.isBlank())
//			{
//
//				ordemcompra = "";
//
//			}
//			else
//			{
//
//				ordemcompra = "Ordem de Compra:" + ordemcompra;
//			}
//
//			if (suframa == null || suframa.equals("")|| suframa.isBlank())
//			{
//
//				suframa = "";
//
//			}
//			else
//			{
//
//				suframa = "Inscrição Suframa:" + suframa;
//			}
//
//			if (LocalEntrega == null || LocalEntrega.equals(""))
//			{
//
//				LocalEntrega = "";
//			}
//
//			StringBuilder mensagem = new StringBuilder();
//			mensagem.append(infmensagem);
//			mensagem.append("\n");
//			mensagem.append(ordemcompra);
//			mensagem.append("\n");
//			mensagem.append(suframa);
//			mensagem.append("\n");
//			mensagem.append(LocalEntrega);

			String mensagemnota = wraperpedido.findGenericValue("PedidoEXP.MensagemNaNota");
			String ordemcompra = wraperpedido.findGenericValue("PedidoEXP.OrdemDeCompra");

			String tipoliberacao = wraperpedido.findGenericValue("PedidoEXP.TipoDeLiberacao.Codigo");
			String tipofrete = wraperpedido.findGenericValue("PedidoEXP.TipoDeFreteEXP.SiglaTipoFrete");
			BigDecimal pesobruto = wraperpedido.findGenericValue("PedidoEXP.PesoBrutoTotalDosItens");
			BigDecimal pesoliquido = wraperpedido.findGenericValue("PedidoEXP.PesoLiquidoTotalDosItens");
			LocalDateTime d05 = LocalDateTime.now();
			DateTimeFormatter fmt2 = DateTimeFormatter.ofPattern("yyyyMMdd");
			String dateinclusao = d05.format(fmt2);
			
			Long moeda = wraperpedido.findGenericValue("PedidoEXP.Codmoeda");

			Long volume = wraperpedido.findGenericValue("PedidoEXP.VolumeTotalDosItens");

			Order ordertotvs = new Order();

			ordertotvs.setIdClienteFusion(idClienteFusion);
			ordertotvs.setC5_EMISSAO(dateinclusao);
			ordertotvs.setC5_ZFUSION(numeropedidofusion);
			ordertotvs.setC5_CONDPAG(condpag);
			ordertotvs.setC5_TRANSP(transportadora);
			ordertotvs.setC5_FRETE(frete);
			ordertotvs.setC5_SEGURO(seguro);
			ordertotvs.setC5_REDESP(redespasho);
			ordertotvs.setC5_ZMENNF(mensagemnota);
			ordertotvs.setC5_MOEDA(moeda);
			ordertotvs.setC5_TIPLIB(tipoliberacao);
			ordertotvs.setC5_TPFRETE(tipofrete);
			ordertotvs.setC5_PBRUTO(pesobruto);
			ordertotvs.setC5_PESOL(pesoliquido);
			ordertotvs.setC5_ZDTFUSI(dataemissao);
			ordertotvs.setC5_CLIENT(clienteentrega);
			ordertotvs.setC5_LOJAENT(lojaentrega);
			ordertotvs.setC5_VOLUME1(volume);
			ordertotvs.setC5_DESPESA(BigDecimal.ZERO);

			// Fim do Cabeçario Pedido

			// Vendedores de Vendas 
//
//			boolean vendcompartilhada = wraperpedido
//					.findGenericValue("TemVendaCompartilhadaRepresentante");
//			boolean TeAgenE = wraperpedido.findGenericValue("TemVendaCompartilhadaRepresentante");

			String Agente = wraperpedido.findGenericValue("PedidoEXP.CodvenAgent.ACodRep");

//			String Agentecompartilhado = wraperpedido.findGenericValue("temvendcomp.a3_cod");
//			BigDecimal ComissaoAgenteCompartilhado = wraperpedido
//					.findGenericValue("ComissaoRepresentante");
//			String Pic = wraperpedido.findGenericValue("CodigovendePIC.a3_cod");
//			BigDecimal ComissaoAgentePic = wraperpedido.findGenericValue("Infperpic");
//
//			if (!vendcompartilhada)
//			{
//
//				Agentecompartilhado = "";
//				ComissaoAgenteCompartilhado = BigDecimal.ZERO;
//			}
//
//			if (!TeAgenE)
//			{
//
//				Pic = "";
//				ComissaoAgentePic = BigDecimal.ZERO;
//
//			}

			ordertotvs.setC5_VEND1(Agente);
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

			// Busca Itens do Pedido Para Protheus

			List<Itens> listitens = new ArrayList<Itens>();

			String tabelapreco = "";
			String tipooperacao = wraperpedido.findGenericValue("PedidoEXP.TipoDeOperacao.x5_chave");
			GregorianCalendar dataembarque = wraperpedido.findGenericValue("PedidoEXP.DataDeEmbarque");
			String prevembarquepedido = NeoCalendarUtils.dateToString(dataembarque);

			String anoem = prevembarquepedido.substring(6, 10);
			String mesem = prevembarquepedido.substring(3, 5);
			String diaem = prevembarquepedido.substring(0, 2);
			String dtembarquepedido = anoem + mesem + diaem;

			List<NeoObject> itensfusion = wraperpedido.findGenericValue("PedidoEXP.ListaItens");

			for (NeoObject itensPedido : itensfusion)
			{

				EntityWrapper itensPedidoWrapper = new EntityWrapper(itensPedido);

				String codigoproduto = itensPedidoWrapper.findGenericValue("CodigoDoItem");
				String qtd = itensPedidoWrapper.findGenericValue("Qtd");
				BigDecimal quantidade = new BigDecimal(qtd);
				BigDecimal vlunitario = itensPedidoWrapper.findGenericValue("ValorUnitario");
				BigDecimal valormerc = itensPedidoWrapper.findGenericValue("ValorMerc");
				String tabelaprecoitem = "930";

				if (tabelaprecoitem != null)
				{
					tabelapreco = tabelaprecoitem;
				}

				String tipooperacaoitem = tipooperacao.trim();
				String dtembarque = "";
				dtembarque = dtembarquepedido;

				String ordemitem = itensPedidoWrapper.findGenericValue("Itemped");

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
				itensped.setC6_ITEMCTA("200400000"); // conta contabil EXP
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
