package com.neomind.fusion.custom.tecnoperfil.exportacao;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.neomind.framework.base.entity.NeoBaseEntity;
import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.doc.NeoStorage;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.entity.InstantiableEntityInfo;
import com.neomind.fusion.persist.PersistEngine;
import com.neomind.fusion.persist.QLEqualsFilter;
import com.neomind.fusion.portal.PortalUtil;
import com.neomind.fusion.security.NeoUser;
import com.neomind.fusion.workflow.adapter.AdapterUtils;
import com.neomind.util.NeoCalendarUtils;
import com.neomind.util.NeoUtils;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class TecnoperfilRelatorioProforma
{
	private static final Log log = LogFactory.getLog(TecnoperfilRelatorioProforma.class);

	/**
	 * Gera o PDF do relatório, utilizando JasperReports
	 */
	public static File geraPDF(String numeroproformula)
	{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		InputStream is = null;
		String path = "";
		Long proformulaId = Long.parseLong(numeroproformula);

		try
		{
			InstantiableEntityInfo ieiColaborador = AdapterUtils.getInstantiableEntityInfo("DocEXP");
			@SuppressWarnings({ "unchecked", "rawtypes" })
			NeoBaseEntity pedido = (NeoObject) PersistEngine.getObject(ieiColaborador.getEntityClass(),
					new QLEqualsFilter("neoId", proformulaId));

			//EntityWrapper pedidoWrapper = new EntityWrapper(pedido);

			String nome_modelo = "ProformaInvoice.jasper";

			// ...files/relatorios
			path = NeoStorage.getDefault().getPath() + File.separator + "relatorios" + File.separator
					+ nome_modelo;
			// obtém os parâmetros
			paramMap = preencheParametros(pedido);
			is = new BufferedInputStream(new FileInputStream(path));

			if (paramMap != null)
			{

				File file = File.createTempFile("Proforma Invoice", ".pdf");
				file.deleteOnExit();

				JasperPrint impressao = JasperFillManager.fillReport(is, paramMap);
				if (impressao != null && file != null)
				{
					JasperExportManager.exportReportToPdfFile(impressao, file.getAbsolutePath());
					return file;
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();

			log.error("Erro ao gerar o PDF do Relatório de Posição Física Financeira!!", e);
		}
		return null;
	}

	/**
	 * Preenche o mapa de parâmetros enviados ao relatório.
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> preencheParametros(NeoBaseEntity pedido)
	{

		Map<String, Object> paramMap = null;
		String PATH_LOGO = NeoStorage.getDefault().getPath() + File.separator + "relatorios"
				+ File.separator + "logo_tecnoperfil.JPG";
		String subRelatorio = NeoStorage.getDefault().getPath() + File.separator + "relatorios"
				+ File.separator + "ProformaInvoiceItens.jasper";

		try
		{
			paramMap = new HashMap<String, Object>();
			EntityWrapper wrapper = new EntityWrapper(pedido);

			// Dados do Cabeçario 
			@SuppressWarnings("unused")
			NeoUser usuarioLogado = PortalUtil.getCurrentUser();
			paramMap.put("pathLogo", PATH_LOGO);
			paramMap.put("Invoice", NeoUtils.safeOutputString(wrapper.findValue("Proformula")).trim());

			String nomeempresa = NeoUtils
					.safeOutputString(wrapper.findValue("Clienteexport.ClienteTotvs.a1_nome")).trim();
			String docestrangeiro = NeoUtils
					.safeOutputString(wrapper.findValue("Clienteexport.ClienteTotvs.a1_zdocext")).trim();
			String importer = nomeempresa + " " + docestrangeiro;
			paramMap.put("importer", importer);
			String endereco = NeoUtils
					.safeOutputString(wrapper.findValue("Clienteexport.ClienteTotvs.a1_end")).trim();
			String municipio = NeoUtils
					.safeOutputString(wrapper.findValue("Clienteexport.ClienteTotvs.a1_mun")).trim();
			String pais = NeoUtils.safeOutputString(wrapper.findValue("Clienteexport.NomePais.ya_descr"))
					.trim();
			String importador = endereco + "," + municipio + "," + pais;
			paramMap.put("importador", importador);

			Integer Contato = wrapper.findGenericValue("Contatos");
			String estado = NeoUtils
					.safeOutputString(wrapper.findValue("Clienteexport.ClienteTotvs.a1_est")).trim();

			if (Contato == 2)
			{
				String DDD = NeoUtils.safeOutputString(wrapper.findValue("ContatoPessoa.u5_ddd")).trim();
				String DDI = NeoUtils.safeOutputString(wrapper.findValue("ContatoPessoa.u5_codpais"))
						.trim();
				String numero = NeoUtils.safeOutputString(wrapper.findValue("ContatoPessoa.u5_fone"))
						.trim();
				paramMap.put("contact",
						NeoUtils.safeOutputString(wrapper.findValue("ContatoPessoa.u5_contat")).trim());
				paramMap.put("email",
						NeoUtils.safeOutputString(wrapper.findValue("ContatoPessoa.u5_email")).trim());
				String telefo = "";
				if (estado.equals("EX"))
				{

					telefo = "(" + DDI + ") " + numero;

				}
				else
				{

					telefo = "(" + DDD + ") " + numero;

				}

				paramMap.put("tel", telefo);

			}

			GregorianCalendar emissao = wrapper.findGenericValue("DataDeEmissao");
			paramMap.put("datafecha", NeoCalendarUtils.dateToString(emissao));
			paramMap.put("paymentcond", "CondicaoDePagamento");

			String pedidos = "";
			
			StringBuilder pedidoordem = new StringBuilder();

			List<NeoObject> AdicionaPedido = wrapper.findGenericValue("AdicionaPedido");
			
			

			for (NeoObject ListaAdicianaPedido : AdicionaPedido)
			{

				

				EntityWrapper ListaPedidoWraper = new EntityWrapper(ListaAdicianaPedido);

				String NumeroPedido = ListaPedidoWraper.findGenericValue("PedidoFusion");

				
				

				if (pedidos.equals(NumeroPedido))
				{

				}
				else
				{
					pedidos = NumeroPedido;
					pedidoordem.append(NumeroPedido);
					pedidoordem.append("-");

				}

				

			}
			
			String orderpedido = pedidoordem.toString();

			paramMap.put("orderpedido", orderpedido);
			String Incoterm = NeoUtils.safeOutputString(wrapper.findValue("Incoterm.Codigo"))+" - "
					+ NeoUtils.safeOutputString(wrapper.findValue("Incoterm.Descricao"));
			paramMap.put("Inconterm", Incoterm);
			paramMap.put("portshipment",
					NeoUtils.safeOutputString(wrapper.findValue("PortoEmbarque")).trim());

			paramMap.put("portdestination",
					NeoUtils.safeOutputString(wrapper.findValue("PortoDesembarque")).trim());

			paramMap.put("condiciones",
					NeoUtils.safeOutputString(wrapper.findValue("CondicoesFornecimento")).trim());

			paramMap.put("siglamoeda",
					NeoUtils.safeOutputString(wrapper.findValue("Moeda.cto_simb")).trim());
			paramMap.put("incoterm",
					NeoUtils.safeOutputString(wrapper.findValue("Incoterm.Codigo")).trim());

			paramMap.put("TotMerc",
					NeoUtils.safeOutputString(wrapper.findValue("ValorMercadoria")).trim());

			paramMap.put("FLETE", NeoUtils.safeOutputString(wrapper.findValue("ValorFrete")).trim());

			paramMap.put("SEGURO", NeoUtils.safeOutputString(wrapper.findValue("ValorSeguro")).trim());

			paramMap.put("TOTALFCA", NeoUtils.safeOutputString(wrapper.findValue("ValorTotal")).trim());

			paramMap.put("PESONETO", NeoUtils.safeOutputString(wrapper.findValue("PesoLiquido")).trim());

			paramMap.put("PESOBRUTO", NeoUtils.safeOutputString(wrapper.findValue("PesoBruto")).trim());

			paramMap.put("VOLUME", NeoUtils.safeOutputString(wrapper.findValue("Volumes")).trim());

			paramMap.put("VOLUMEM", NeoUtils.safeOutputString(wrapper.findValue("Volumecubico")).trim());

			paramMap.put("pathSubRelatorio1", subRelatorio);

			String indioma = NeoUtils.safeOutputString(wrapper.findValue("Indioma.Sigla"));
			String moeda = NeoUtils.safeOutputString(wrapper.findValue("Moeda.cto_desc"));

			@SuppressWarnings("unchecked")
			Collection<NeoObject> itens = (Collection<NeoObject>) wrapper.findField("AdicionaPedido")
					.getValues();
			Collection<TecnoperfilItensProformaDataSource> listaItens = populaGrid(itens, indioma,
					moeda);

			paramMap.put("listaItens", listaItens);

			return paramMap;
		}
		catch (Exception e)
		{
			log.error("Erro ao preencher o mapa de parâmetros da Impressão do Relatório", e);
			e.printStackTrace();
		}
		return paramMap;
	}

	/**
	 * Retorna dados dos itens do pedido
	 */

	public static Collection<TecnoperfilItensProformaDataSource> populaGrid(Collection<NeoObject> itens,
			String indioma, String moeda)
	{

		List<TecnoperfilItensProformaDataSource> listaItens = new ArrayList<TecnoperfilItensProformaDataSource>();

		try
		{

			int Contitem = 0;
			for (NeoObject item : itens)
			{
				EntityWrapper wItem = new EntityWrapper(item);

				Contitem++;

				String seqitem = Integer.toString(Contitem);
				String quantidade = NeoUtils.safeOutputString(wItem.findValue("AdicionaPedido.Qtd"));
				String um = NeoUtils.safeOutputString(wItem.findValue("AdicionaPedido.UM"));
				String descricao = "";

				if (indioma.contains("E"))
				{

					descricao = NeoUtils
							.safeOutputString(wItem.findValue("AdicionaPedido.DescricaoEspanhol"));

				}

				if (indioma.contains("P"))
				{

					descricao = NeoUtils.safeOutputString(wItem.findValue("AdicionaPedido.Descricao"));

				}

				if (indioma.contains("I"))
				{

					descricao = NeoUtils
							.safeOutputString(wItem.findValue("AdicionaPedido.DescricaoIngles"));

				}

				String siglamoeda = "";

				if (moeda.contains("REAL"))
				{

					siglamoeda = "R$";

				}

				if (moeda.contains("DOLAR"))
				{

					siglamoeda = "US$";

				}

				if (moeda.contains("EURO"))
				{

					siglamoeda = "€";

				}

				String ferramenta = NeoUtils
						.safeOutputString(wItem.findValue("AdicionaPedido.Ferramenta"));
				String cor = NeoUtils.safeOutputString(wItem.findValue("AdicionaPedido.DescricaoCor"));
				String ncm = NeoUtils.safeOutputString(wItem.findValue("AdicionaPedido.NCM"));
				String codigo = NeoUtils
						.safeOutputString(wItem.findValue("AdicionaPedido.CodigoDoItem"));

				String valoruni = NeoUtils
						.safeOutputString(wItem.findValue("AdicionaPedido.ValorUnitario"));
				String total = NeoUtils.safeOutputString(wItem.findValue("AdicionaPedido.ValorMerc"));

				TecnoperfilItensProformaDataSource ped = new TecnoperfilItensProformaDataSource();
				ped.setItem(seqitem);
				ped.setQuantidade(quantidade);
				ped.setUnidade(um);
				ped.setDescricao(descricao);
				ped.setFerramenta(ferramenta);
				ped.setCor(cor);
				ped.setNcm(ncm);
				ped.setCodigoitem(codigo);
				ped.setValoUnitario(valoruni);
				ped.setVltotal(total);
				ped.setSiglamoeda(siglamoeda);

				listaItens.add(ped);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// System.out.println(listaItens.toString());
		return listaItens;

	}

	@SuppressWarnings("static-access")
	public static String formatCurrencyValue(String plainValue)
	{
		try
		{
			BigDecimal value = new BigDecimal(plainValue);
			NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pt", "BR"));

			return nf.getCurrencyInstance().format(value);
		}
		catch (Exception e)
		{
			System.out.println("Erro ao aplicar máscara monetária: " + plainValue);
		}
		return "R$ 0,00";
	}

	@SuppressWarnings("unused")
	private static String safeNotNullString(Object value)
	{
		try
		{
			if (value instanceof String)
			{
				String valueStr = (String) value;
				if (valueStr != null && !valueStr.isEmpty())
				{
					return valueStr.trim();
				}
			}
		}
		catch (Exception e)
		{

		}

		return "";
	}
}
