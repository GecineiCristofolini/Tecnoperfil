package com.neomind.fusion.custom.tecnoperfil.exportacao;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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
import com.neomind.util.NeoUtils;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

public class TecnoperfilRelatorioDeclaracaoProdutor
{
	private static final Log log = LogFactory.getLog(TecnoperfilRelatorioDeclaracaoProdutor.class);

	/**
	 * Gera o PDF do relatório, utilizando JasperReports
	 */
	public static File geraPDF(String numeroPedido)
	{
		Map<String, Object> paramMap = new HashMap<String, Object>();

		List<Map<String, Object>> ListMap = new ArrayList<Map<String, Object>>();

		InputStream is = null;
		String path = "";
		Long pedidoId = Long.parseLong(numeroPedido);
		Set<String> setncm = new HashSet<>();

		try
		{
			InstantiableEntityInfo ieiColaborador = AdapterUtils.getInstantiableEntityInfo("DocEXP");
			@SuppressWarnings({ "unchecked", "rawtypes" })
			NeoBaseEntity pedido = (NeoObject) PersistEngine.getObject(ieiColaborador.getEntityClass(),
					new QLEqualsFilter("neoId", pedidoId));

			EntityWrapper wrapper = new EntityWrapper(pedido);
			List<NeoObject> AdicionaPedido = wrapper.findGenericValue("AdicionaPedido");

			for (NeoObject ncms : AdicionaPedido)
			{

				EntityWrapper ncmwrapper = new EntityWrapper(ncms);

				String buscancm = ncmwrapper.findGenericValue("NCM");

				setncm.add(buscancm);

			}

			for (String ncm : setncm)
			{

				paramMap = preencheParametros(pedido, ncm);
				ListMap.add(paramMap);

			}

			String nome_modelo = "DeclaracaoProdutor.jasper";

			// ...files/relatorios
			path = NeoStorage.getDefault().getPath() + File.separator + "relatorios" + File.separator
					+ nome_modelo;
			// obtém os parâmetros

			
           
			

			List<JasperPrint> listimprimir = new ArrayList<JasperPrint>();
			
			
			

			for (Map<String, Object> parametros : ListMap)
			{
				is = new BufferedInputStream(new FileInputStream(path));
				JasperPrint impressao = JasperFillManager.fillReport(is, parametros);
			
				
				listimprimir.add(impressao);
				
			}

			
			String nomeFile = "/opt/tomcat/temp/DeclaracaoProduto.pdf";
          
            

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(SimpleExporterInput.getInstance(listimprimir)); //Set as export input my list with JasperPrint s
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(nomeFile)); //or any other out streaam
            
            SimplePdfExporterConfiguration conf = new SimplePdfExporterConfiguration();
            exporter.setConfiguration(conf);
            exporter.exportReport();
            
            
//            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
//            configuration.setCreatingBatchModeBookmarks(true);
//            exporter.setConfiguration(configuration);
//            exporter.exportReport();
            
            
            
 
            
           
            File file = new File(nomeFile);
			
			
			
			
				
			
			return file;

						
				 
					
				

		
			
			
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
	public static Map<String, Object> preencheParametros(NeoBaseEntity pedido,String ncm)
	{

		Map<String, Object> paramMap = null;
		String PATH_LOGO = NeoStorage.getDefault().getPath() + File.separator + "relatorios"
				+ File.separator + "logo_tecnoperfil.JPG";
		String subRelatorio = NeoStorage.getDefault().getPath() + File.separator + "relatorios"
				+ File.separator + "DocumentoprodutoListItens.jasper";
		String subRelatorio2 = NeoStorage.getDefault().getPath() + File.separator + "relatorios"
				+ File.separator + "DocumentoprodutoListNCM.jasper";

		try
		{
			paramMap = new HashMap<String, Object>();
			EntityWrapper wrapper = new EntityWrapper(pedido);

			// Dados do Cabeçario 
				@SuppressWarnings("unused")
				NeoUser usuarioLogado = PortalUtil.getCurrentUser();
				paramMap.put("pathLogo", PATH_LOGO);
				paramMap.put("Invoice", NeoUtils.safeOutputString(wrapper.findValue("Invoice")).trim());
				paramMap.put("nomeempresa", NeoUtils
						.safeOutputString(wrapper.findValue("Clienteexport.ClienteTotvs.a1_nome"))
						.trim());
				paramMap.put("acordo",
						NeoUtils.safeOutputString(wrapper.findValue("MensagemParte1")).trim());
				paramMap.put("materiaisnacional",
						NeoUtils.safeOutputString(wrapper.findValue("AMateriaisNacionais")).trim());
				paramMap.put("materiaisoriginais",
						NeoUtils.safeOutputString(wrapper.findValue("MateriaisOriginarios")).trim());
				paramMap.put("materiaisterceiros",
						NeoUtils.safeOutputString(wrapper.findValue("MateriaisTerceirosPaises")).trim());

				paramMap.put("pathSubRelatorio1", subRelatorio);
				paramMap.put("pathSubRelatorio2", subRelatorio2);

				String idioma = NeoUtils.safeOutputString(wrapper.findValue("Indioma.Sigla"));
				@SuppressWarnings("unchecked")
				Collection<NeoObject> itens = (Collection<NeoObject>) wrapper.findField("AdicionaPedido")
						.getValues();
				@SuppressWarnings("unchecked")
				Collection<NeoObject> itensncm = (Collection<NeoObject>) wrapper
						.findField("AdicionaPedido").getValues();

				Collection<DeclaracaoProdutorDataSourceItem> listaItens = populaGrid(itens, ncm, idioma);
				Collection<DeclaracaoProdutorDataSourceItemNCM> listancm = populaGridncm(itensncm, ncm,
						idioma);

				paramMap.put("listaItens", listaItens);

				paramMap.put("listancm", listancm);
			
			return paramMap;
		}catch(

	Exception e)
	{
		log.error("Erro ao preencher o mapa de parâmetros da Impressão do Relatório", e);
		e.printStackTrace();
	}return paramMap;
	}

	/**
	 * Retorna dados dos itens do pedido
	 */

	public static Collection<DeclaracaoProdutorDataSourceItem> populaGrid(Collection<NeoObject> itens,
			String ncm, String idioma)
	{

		List<DeclaracaoProdutorDataSourceItem> listaitem = new ArrayList<DeclaracaoProdutorDataSourceItem>();

		try
		{

			for (NeoObject item : itens)
			{
				EntityWrapper wItem = new EntityWrapper(item);

				String ncmitem = NeoUtils.safeOutputString(wItem.findValue("AdicionaPedido.NCM"));

				if (ncmitem.equals(ncm))
				{

					String descricao = "";

					if (idioma.contains("E"))
					{

						descricao = NeoUtils
								.safeOutputString(wItem.findValue("AdicionaPedido.DescricaoEspanhol"));

					}

					if (idioma.contains("P"))
					{

						descricao = NeoUtils
								.safeOutputString(wItem.findValue("AdicionaPedido.Descricao"));

					}

					if (idioma.contains("I"))
					{

						descricao = NeoUtils
								.safeOutputString(wItem.findValue("AdicionaPedido.DescricaoIngles"));

					}

					String ferramenta = NeoUtils.safeOutputString(wItem.findValue("AdicionaPedido.Ferramenta"));

					String cor = NeoUtils
							.safeOutputString(wItem.findValue("AdicionaPedido.DescricaoCor"));

					DeclaracaoProdutorDataSourceItem ped = new DeclaracaoProdutorDataSourceItem();

					ped.setDescricao(descricao);
					ped.setFerramenta(ferramenta);
					ped.setCor(cor);

					listaitem.add(ped);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// System.out.println(listaItens.toString());
		return listaitem;
	}

	public static Collection<DeclaracaoProdutorDataSourceItemNCM> populaGridncm(
			Collection<NeoObject> itens, String ncm, String idioma)
	{

		List<DeclaracaoProdutorDataSourceItemNCM> listaItensncm = new ArrayList<DeclaracaoProdutorDataSourceItemNCM>();

		try
		{

			Long Contitem = 0L;
			String ncmla = "";
			String descrincm = "";
			BigDecimal valor = BigDecimal.ZERO;

			for (NeoObject item : itens)
			{
				EntityWrapper wItem = new EntityWrapper(item);

				String ncmitem = NeoUtils.safeOutputString(wItem.findValue("AdicionaPedido.NCM"));

				if (ncmitem.equals(ncm))
				{
					Contitem++;
					ncmla = ncmitem;

					descrincm = NeoUtils
							.safeOutputString(wItem.findValue("AdicionaPedido.DescricaoNcm"));

					valor = valor.add(wItem.findGenericValue("AdicionaPedido.ValorUnitario"));

				}
			}
			BigDecimal divisor = new BigDecimal(Contitem);
			BigDecimal media = valor.divide(divisor,2, RoundingMode.HALF_UP);
			String valortexto = media.toString();

			DeclaracaoProdutorDataSourceItemNCM pedncm = new DeclaracaoProdutorDataSourceItemNCM();

			pedncm.setNcm(ncmla);
			pedncm.setDescrincm(descrincm);
			pedncm.setValor(valortexto);

			listaItensncm.add(pedncm);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// System.out.println(listaItens.toString());
		return listaItensncm;
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