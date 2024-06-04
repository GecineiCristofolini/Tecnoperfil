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
import com.neomind.fusion.custom.tecnoperfil.TecnoperfilItensPedidoDataSource;
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

public class TecnoperfilRelatorioPackingList {
	private static final Log log = LogFactory.getLog(TecnoperfilRelatorioPackingList.class);

	/**
	 * Gera o PDF do relatório, utilizando JasperReports
	 */
	public static File geraPDF(String numeroPedido) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		InputStream is = null;
		String path = "";
		Long pedidoId = Long.parseLong(numeroPedido);

		try {
			InstantiableEntityInfo ieiColaborador = AdapterUtils.getInstantiableEntityInfo("PedExp");
			@SuppressWarnings({ "unchecked", "rawtypes" })
			NeoBaseEntity pedido = (NeoObject) PersistEngine.getObject(ieiColaborador.getEntityClass(),
					new QLEqualsFilter("neoId", pedidoId));

			//EntityWrapper pedidoWrapper = new EntityWrapper(pedido);
			
			String nome_modelo = "PackingList.jasper";
			
			// ...files/relatorios
			path = NeoStorage.getDefault().getPath() + File.separator + "relatorios" + File.separator + nome_modelo;
			// obtém os parâmetros
			paramMap = preencheParametros(pedido);
			is = new BufferedInputStream(new FileInputStream(path));

			if (paramMap != null) {

				File file = File.createTempFile("Packing List", ".pdf");
				file.deleteOnExit();

				JasperPrint impressao = JasperFillManager.fillReport(is, paramMap);
				if (impressao != null && file != null) {
					JasperExportManager.exportReportToPdfFile(impressao, file.getAbsolutePath());
					return file;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

			log.error("Erro ao gerar o PDF do Relatório de Posição Física Financeira!!", e);
		}
		return null;
	}

	/**
	 * Preenche o mapa de parâmetros enviados ao relatório.
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> preencheParametros(NeoBaseEntity pedido) {

		Map<String, Object> paramMap = null;
		String PATH_LOGO = NeoStorage.getDefault().getPath() + File.separator + "relatorios" + File.separator
				+ "logo_tecnoperfil.JPG";
		String subRelatorio = NeoStorage.getDefault().getPath() + File.separator + "relatorios" + File.separator
				+ "PackingListItens.jasper";

		try {
			paramMap = new HashMap<String, Object>();
			EntityWrapper wrapper = new EntityWrapper(pedido);
            // Dados do Cabeçario 
			@SuppressWarnings("unused")
			NeoUser usuarioLogado = PortalUtil.getCurrentUser();
			paramMap.put("pathLogo", PATH_LOGO);
			paramMap.put("Invoice", "EXP 12-24");
		    paramMap.put("importer",  NeoUtils.safeOutputString(wrapper.findValue("ClienteExp.Client.nome_empresa")).trim());
		    paramMap.put("importador",  NeoUtils.safeOutputString(wrapper.findValue("ClienteExp.CEnder")).trim());
		    paramMap.put("contact",  "Verficar");
		    paramMap.put("tel",  "Verficar Telefone");
		    paramMap.put("email",  "Verificar email");
		    GregorianCalendar emissao = wrapper.findGenericValue("DataDeEmissao");
			paramMap.put("datafecha",NeoCalendarUtils.dateToString(emissao));		    
			paramMap.put("orderpedido",NeoUtils.safeOutputString(wrapper.findValue("NumPed")).trim());
			paramMap.put("portshipment",NeoUtils.safeOutputString(wrapper.findValue("ClienteExp.Cidade")).trim()+ " "
			+NeoUtils.safeOutputString(wrapper.findValue("ClienteExp.Estado")).trim());
			paramMap.put("portdestination","verificar destination ");
			paramMap.put("pathSubRelatorio1", subRelatorio);
						
		
		Collection<NeoObject> itens = (Collection<NeoObject>) wrapper.findField("ItenPedExp").getValues();
		Collection<TecnoperfilItensPackingListDataSource> listaItens = populaGrid(itens);
		
		paramMap.put("listaItens", listaItens);

		
		paramMap.put("listaItens", listaItens);

			return paramMap;
		} catch (Exception e) {
			log.error("Erro ao preencher o mapa de parâmetros da Impressão do Relatório", e);
			e.printStackTrace();
		}
		return paramMap;
	}

	/**
	 * Retorna dados dos itens do pedido 
	 */
	
	public static Collection<TecnoperfilItensPackingListDataSource> populaGrid(Collection<NeoObject> itens) {
		
	
		
		List<TecnoperfilItensPackingListDataSource> listaItens = new ArrayList();

		try {
			
			
			for (NeoObject item : itens) {
				EntityWrapper wItem = new EntityWrapper(item);
				
				String volumen = "1";
				String pesoneto = "2";
				String pesobruto= "3";
				String altura= "4";
				String anchura = "5";
				String longitud = "6";
				String descricao = NeoUtils.safeOutputString(wItem.findValue("DesItemEXP.linha_nar_item"));
				String ferramenta = NeoUtils.safeOutputString(wItem.findValue("Ferrament.produto"));
				String color = NeoUtils.safeOutputString(wItem.findValue("CorEXP.cor_formulacao")) ;
								
				
				
				
				
				

				TecnoperfilItensPackingListDataSource ped = new TecnoperfilItensPackingListDataSource();
				ped.setVolumen(volumen);
				ped.setPesoneto(pesoneto);
				ped.setPesobruto(pesobruto);
				ped.setAltura(altura);
				ped.setAnchura(anchura);
				ped.setLongitud(longitud);
				ped.setDescricao(descricao);
				ped.setFerramenta(ferramenta);
				ped.setColor(color);
				
				
				

				listaItens.add(ped);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
       // System.out.println(listaItens.toString());
		return listaItens;
	}

	@SuppressWarnings("static-access")
	public static String formatCurrencyValue(String plainValue) {
		try {
			BigDecimal value = new BigDecimal(plainValue);
			NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pt", "BR"));

			return nf.getCurrencyInstance().format(value);
		} catch (Exception e) {
				System.out.println("Erro ao aplicar máscara monetária: "+plainValue);
		}
		return "R$ 0,00";
	}

	@SuppressWarnings("unused")
	private static String safeNotNullString(Object value) {
		try {
			if (value instanceof String) {
				String valueStr = (String) value;
				if (valueStr != null && !valueStr.isEmpty()) {
					return valueStr.trim();
				}
			}
		} catch (Exception e) {

		}

		return "";
	}
}
