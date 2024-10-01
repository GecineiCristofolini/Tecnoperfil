package com.neomind.fusion.custom.tecnoperfil;

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
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.neomind.framework.base.entity.NeoBaseEntity;
import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.doc.NeoStorage;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.entity.InstantiableEntityInfo;
import com.neomind.fusion.entity.NeoPhone;
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

public class TecnoperfilRelatorioPedidoTotvs {
	private static final Log log = LogFactory.getLog(TecnoperfilRelatorioPedidoTotvs.class);

	/**
	 * Gera o PDF do relatório, utilizando JasperReports
	 */
	public static File geraPDF(String numeroPedido) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		InputStream is = null;
		String path = "";
		Long pedidoId = Long.parseLong(numeroPedido);

		try {
			InstantiableEntityInfo ieiColaborador = AdapterUtils.getInstantiableEntityInfo("Pedido");
			NeoBaseEntity pedido = (NeoObject) PersistEngine.getObject(ieiColaborador.getEntityClass(),
					new QLEqualsFilter("neoId", pedidoId));

			EntityWrapper pedidoWrapper = new EntityWrapper(pedido);
			
			String nome_modelo = pedidoWrapper.findGenericValue("tipoPedido"); //"tecnoperfilPedidos.jasper";
			if(nome_modelo == null || nome_modelo.isEmpty()) {
				nome_modelo = "tecnoperfilPedidosTotvs.jasper";
			}
			// ...files/relatorios
			path = NeoStorage.getDefault().getPath() + File.separator + "relatorios" + File.separator + nome_modelo;
			// obtém os parâmetros
			paramMap = preencheParametros(pedido);
			is = new BufferedInputStream(new FileInputStream(path));

			if (paramMap != null) {

				File file = File.createTempFile("Relatorio_Pedido", ".pdf");
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
	public static Map<String, Object> preencheParametros(NeoBaseEntity pedido) {

		Map<String, Object> paramMap = null;
		String PATH_LOGO = NeoStorage.getDefault().getPath() + File.separator + "relatorios" + File.separator
				+ "logo_tecnoperfil.JPG";
		String subRelatorio = NeoStorage.getDefault().getPath() + File.separator + "relatorios" + File.separator
				+ "tecnoperfilPedidos_itenstotvs.jasper";

		try {
			paramMap = new HashMap<String, Object>();
			EntityWrapper wrapper = new EntityWrapper(pedido);

			NeoUser usuarioLogado = PortalUtil.getCurrentUser();
			paramMap.put("pathSubRelatorio1", subRelatorio);
			paramMap.put("pathLogo", PATH_LOGO);
			paramMap.put("numeroNota", NeoUtils.safeOutputString(wrapper.findValue("NumPed")).trim());
			paramMap.put("email", NeoUtils.safeOutputString(usuarioLogado.getEmail()));

			paramMap.put("obsPedido", safeNotNullString(wrapper.findGenericValue("PObsPed")));
			
			paramMap.put("obsobra", safeNotNullString(wrapper.findGenericValue("InforObra")));

			try {
				paramMap.put("descPisCofins", NeoUtils.safeOutputString(wrapper.findValue("AbICMS").toString()).trim());
			} catch (Exception e) {
				paramMap.put("descPisCofins", "");
			}

			try {
				BigDecimal pisAliq = wrapper.findGenericValue("PisCoAli");
				if (pisAliq.floatValue() > 0) {
					paramMap.put("pisAliq", NeoUtils.safeOutputString(pisAliq.toString()));
				} else {
					paramMap.put("pisAliq", null);
				}
			} catch (Exception e) {
				paramMap.put("pisAliq", null);
			}

			try {
				BigDecimal abatIcms = wrapper.findGenericValue("AbICMS");
				if (abatIcms.floatValue() > 0) {
					paramMap.put("abatIcms", NeoUtils.safeOutputString(abatIcms.toString()));
				} else {
					paramMap.put("abatIcms", null);
				}
			} catch (Exception e) {
				paramMap.put("abatIcms", null);
			}

			paramMap.put("cliente", NeoUtils.safeOutputString(wrapper.findValue("ClientesTovs.ClienteTotvs.a1_nome")).trim());
			paramMap.put("codigo", NeoUtils.safeOutputString(wrapper.findValue("ClientesTovs.ClienteTotvs.a1_zfusion")).trim());
			String tipoCliente = wrapper.findGenericValue("ClientesTovs.ClienteTotvs.a1_pessoa");
			String cpfCNPJ = NeoUtils.safeOutputString(wrapper.findValue("ClientesTovs.ClienteTotvs.a1_cgc")).trim();

			if (tipoCliente.equalsIgnoreCase("J")) {
				if (cpfCNPJ.length() < 14) {
					cpfCNPJ = String.format("%014d", Long.parseLong(cpfCNPJ));
				}

				System.out.println("Mascara CNPJ :" + TecnoperfilServletUtilsTotvs.aplicaMascara(cpfCNPJ, true));
				paramMap.put("cnpj", TecnoperfilServletUtilsTotvs.aplicaMascara(cpfCNPJ, true));
			} else {
				if (cpfCNPJ.length() < 11) {
					cpfCNPJ = String.format("%011d", Long.parseLong(cpfCNPJ));
				}

				System.out.println("Mascara CPF :" + TecnoperfilServletUtilsTotvs.aplicaMascara(cpfCNPJ, false));
				paramMap.put("cnpj", TecnoperfilServletUtilsTotvs.aplicaMascara(cpfCNPJ, false));
			}

			paramMap.put("ie", NeoUtils.safeOutputString(wrapper.findValue("ClientesTovs.ClienteTotvs.a1_inscrm")).trim());
			paramMap.put("suframa", NeoUtils.safeOutputString(wrapper.findValue("ClientesTovs.ClienteTotvs.a1_suframa")).trim());

			

			
			paramMap.put("endereco", NeoUtils.safeOutputString(wrapper.findValue("ClientesTovs.ClienteTotvs.a1_end")).trim());
			paramMap.put("bairroCli", NeoUtils.safeOutputString(wrapper.findValue("ClientesTovs.ClienteTotvs.a1_bairro")).trim());
			paramMap.put("cidadeCli", NeoUtils.safeOutputString(wrapper.findValue("ClientesTovs.ClienteTotvs.a1_mun")).trim());
			paramMap.put("ufCli", NeoUtils.safeOutputString(wrapper.findValue("ClientesTovs.ClienteTotvs.a1_est")).trim());
			paramMap.put("cepCli", NeoUtils.safeOutputString(wrapper.findValue("ClientesTovs.ClienteTotvs.a1_cep")).trim());

			
			String condPgto = wrapper.findGenericValue("CondPagtovs.e4_descri");
			

			paramMap.put("condpgto", condPgto);
			paramMap.put("natoper", NeoUtils.safeOutputString(wrapper.findValue("TipoDeOperacao.x5_descri")).trim());

			String transportadora;

			String tipofrete = wrapper.findGenericValue("TipoDeFrete.Descri");
			if (tipofrete.toUpperCase().contains("FOB")) {
				transportadora = wrapper.findGenericValue("Transportadora.a4_nome");
			} else {
				transportadora = wrapper.findGenericValue("Transportadora.a4_nome");
			}

			paramMap.put("transportadora", transportadora);
			
			boolean entregaDiferenciada = NeoUtils.safeBoolean(wrapper.findValue("TemEntredif.InformeSimOuNao"));
			
			String tipoembarque = wrapper.findGenericValue("TipoDeEntrega.Codigo");
			
			if (entregaDiferenciada) {
			
			
			
			if (tipoembarque.equals("3")) {
				String transportadoraRedes = NeoUtils.safeOutputString(wrapper.findValue("infotransrede.a4_cod"))
						+ " - " + NeoUtils.safeOutputString(wrapper.findValue("infotransrede.a4_nome"));
				paramMap.put("redespacho", transportadoraRedes);
			} else {
				paramMap.put("redespacho", "");
			}
            
			}else {
				paramMap.put("redespacho", "");
			}
			

			/*
			 * se for entrega diferenciada, então pega o obsEntrega como endereço e o estado
			 * e cidade caso negativo, deixar em branco
			 */
			if (entregaDiferenciada) {
				
				if (tipoembarque.equals("1")||tipoembarque.equals("5") ) {
					
				String Numero = NeoUtils.safeOutputString(wrapper.findValue("infNumend"));
				
				 paramMap.put("localentrega", NeoUtils.safeOutputString(wrapper.findValue("InfLog.Abrevi"))+" "+
						 NeoUtils.safeOutputString(wrapper.findValue("InfSomEnde")).trim()+" "+
						 NeoUtils.safeOutputString(wrapper.findValue("InfSomEnde")).trim()+" "+ Numero);
				 paramMap.put("bairro", NeoUtils.safeOutputString(wrapper.findValue("InfBairro")));
				 paramMap.put("cidade", NeoUtils.safeOutputString(wrapper.findValue("MunicipiEntregaT.cc2_mun")));
				 paramMap.put("uf", NeoUtils.safeOutputString(wrapper.findValue("MunicipiEntregaT.cc2_est")));
				 paramMap.put("cep",NeoUtils.safeOutputString(wrapper.findValue("InfCEP")));
				 }
				else {
					paramMap.put("localentrega",NeoUtils.safeOutputString(wrapper.findValue("ClienteEntrega.a1_end")).trim());
					 paramMap.put("bairro", NeoUtils.safeOutputString(wrapper.findValue("ClienteEntrega.a1_bairro")));
					 paramMap.put("cidade", NeoUtils.safeOutputString(wrapper.findValue("ClienteEntrega.a1_mun")));
					 paramMap.put("uf", NeoUtils.safeOutputString(wrapper.findValue("ClienteEntrega.a1_est")));
					 paramMap.put("cep",NeoUtils.safeOutputString(wrapper.findValue("ClienteEntrega.a1_cep")));
				}
				
			} else {
				paramMap.put("localentrega", "");
				paramMap.put("bairro", "");
				paramMap.put("cidade", "");
				paramMap.put("uf", "");
				paramMap.put("cep", "");
			}

			GregorianCalendar emissao = wrapper.findGenericValue("DataEm");
			paramMap.put("dtemissao", NeoCalendarUtils.dateToString(emissao));
			paramMap.put("frete", NeoUtils.safeOutputString(wrapper.findValue("TipoDeFrete.Descri")).trim());
			paramMap.put("ordcompra", NeoUtils.safeOutputString(wrapper.findValue("OrdCom")).trim());
			paramMap.put("representante", NeoUtils.safeOutputString(wrapper.findValue("PVende.fullName")).trim());
			paramMap.put("previsao", NeoCalendarUtils.dateToString((GregorianCalendar) wrapper.findValue("PrevEmb")));

			paramMap.put("emailrepresentante", NeoUtils.safeOutputString(wrapper.findValue("PVende.email")));
			String celCliente;
			try {
				celCliente = NeoUtils.safeOutputString(wrapper.findValue("ClientesTovs.ClienteTotvs.a1_ddd")).trim() + "-"
						+ NeoUtils.safeOutputString(wrapper.findValue("ClientesTovs.ClienteTotvs.a1_tel")).trim();
			} catch (Exception e) {
				celCliente = "";
			}

			paramMap.put("celCliente", celCliente);
			String fixoCliente;
			try {
				fixoCliente = NeoUtils.safeOutputString(wrapper.findValue("ClientesTovs.ClienteTotvs.a1_ddd")).trim()
						+ "-" + NeoUtils.safeOutputString(wrapper.findValue("ClientesTovs.ClienteTotvs.a1_tel")).trim();
			} catch (Exception e) {
				fixoCliente = "";
			}
			paramMap.put("fixoCliente", fixoCliente);

			try {
				Set<NeoPhone> telefones = wrapper.findGenericValue("PVende.phoneList");
				if (telefones.size() > 0) {
					NeoPhone neoPhone = (NeoPhone) telefones.toArray()[0];
					paramMap.put("contato", neoPhone.getNumber());
				} else {
					paramMap.put("contato", "");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			paramMap.put("valortotal",
					formatCurrencyValue(NeoUtils.safeOutputString(wrapper.findValue("VldaDaMerc")).trim()));
			paramMap.put("valorfrete",
					formatCurrencyValue(NeoUtils.safeOutputString(wrapper.findValue("ValFret")).trim()));

			String situacao = NeoUtils.safeOutputString(wrapper.findValue("Situac.Descri"));
			if ("Pedido".equalsIgnoreCase(situacao.trim())) {
				paramMap.put("situacao", "PEDIDO DE VENDAS");
			} else {
				paramMap.put("situacao", "COTAÇÃO DE VENDAS");
			}

			BigDecimal valoripi = (BigDecimal) wrapper.findValue("VltoIPI");// mercadorias
			paramMap.put("valoripi", formatCurrencyValue(NeoUtils.safeOutputString(valoripi).trim()));
			BigDecimal valoripifrete = (BigDecimal) wrapper.findValue("VIPIFre");// frete
			paramMap.put("valoripifrete", formatCurrencyValue(NeoUtils.safeOutputString(valoripifrete).trim()));
			BigDecimal valortotalipi = valoripi.add(valoripifrete);
			paramMap.put("valortotalipi",formatCurrencyValue(NeoUtils.safeOutputString(valortotalipi).trim()));

			paramMap.put("subtrib", formatCurrencyValue(NeoUtils.safeOutputString(wrapper.findValue("VlTotST")).trim()));

			paramMap.put("total", formatCurrencyValue(NeoUtils.safeOutputString(wrapper.findValue("VlTotPe")).trim()));
			Collection<NeoObject> itens = (Collection<NeoObject>) wrapper.findField("PItenPed").getValues();
			Collection<TecnoperfilItensPedidoDataSource> listaItens = populaGrid(itens);
			paramMap.put("listaItens", listaItens);

			return paramMap;
		} catch (Exception e) {
			log.error("Erro ao preencher o mapa de parâmetros da Impressão do Relatório", e);
			e.printStackTrace();
		}
		return paramMap;
	}

	/**
	 * Retorna os dados dos meses.
	 */
	public static Collection<TecnoperfilItensPedidoDataSource> populaGrid(Collection<NeoObject> itens) {
		List<TecnoperfilItensPedidoDataSource> listaItens = new ArrayList();

		try {
			for (NeoObject item : itens) {
				EntityWrapper wItem = new EntityWrapper(item);
				String codigo = NeoUtils.safeOutputString(wItem.findValue("CodItem"));
				String quantidade = NeoUtils.safeOutputString(wItem.findValue("Qtd"));
				String uni = NeoUtils.safeOutputString(wItem.findValue("UniVenL"));
				String descricao = NeoUtils.safeOutputString(wItem.findValue("DesProd"));
				String valor = formatCurrencyValue(NeoUtils.safeOutputString(wItem.findValue("ValUni")));
				String ipi = NeoUtils.safeOutputString(wItem.findValue("AliIPI"));
				String total = NeoUtils.safeOutputString(wItem.findValue("vlmerc"));
				String observacao = NeoUtils.safeOutputString(wItem.findValue("IObsItem"));
				String valoripi = NeoUtils.safeOutputString(wItem.findValue("ValIpi"));
				String VltotCipi = NeoUtils.safeOutputString(wItem.findValue("VltotCipi"));
				
				String qtdumsegunda = wItem.findGenericValue("QuantidadeUM");
							
				
				String unim = NeoUtils.safeOutputString(wItem.findValue("SegundaUM"));										
				String valorum = NeoUtils.safeOutputString(wItem.findValue("ValorUnitarioTotvs"));
				
				TecnoperfilItensPedidoDataSource ped = new TecnoperfilItensPedidoDataSource();
				ped.setCodigo(codigo);
				ped.setQuantidade(quantidade);
				ped.setUnidade(uni);
				ped.setDescItem(descricao + "\r\nOBS DO ITEM:  " + observacao);// \r\n
				ped.setValorUnit(valor);
				ped.setIpi(ipi);
				ped.setTotal(formatCurrencyValue(total));
				ped.setvaloripi(formatCurrencyValue(valoripi));
				ped.setVltotCipi(formatCurrencyValue(VltotCipi));
				ped.setUnidadem(unim);
				ped.setQtdumsegunda(qtdumsegunda);
				
				
				BigDecimal valorumseg  = wItem.findGenericValue("ValorUnitarioTotvs");
				if (valorumseg.floatValue() > 0) {
					ped.setValorunim(formatCurrencyValue(valorum));
				} else {
					ped.setValorunim("");
				}
				
				
				

				listaItens.add(ped);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listaItens;
	}

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
