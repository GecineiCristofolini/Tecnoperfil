package com.neomind.fusion.custom.tecnoperfil.industrial;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.neomind.framework.base.entity.NeoBaseEntity;
import com.neomind.fusion.custom.tecnoperfil.TecnoperfilServletUtils;
import com.neomind.fusion.doc.NeoStorage;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.entity.NeoPhone;
import com.neomind.fusion.persist.PersistEngine;
import com.neomind.fusion.workflow.adapter.AdapterUtils;
import com.neomind.util.NeoCalendarUtils;

public class IndustrialSnapShotPedidoTotvs {
/*
  Classe responsável pela extração de dados do pedido e preenchimento do html do pdf
  
 */
	public String buildContent(NeoBaseEntity workfowObject) {
		try {
			
			
			//busca o modelo presente na pasta files/relatorios
			String modeloCaminho = NeoStorage.getDefault().getPath() + File.separator + "relatorios" + File.separator
					+ "modelo_industrialTotvs.html";
			EntityWrapper ew = new EntityWrapper(workfowObject);
			
			byte[] encoded = Files.readAllBytes(Paths.get(modeloCaminho));
			//extração dos parametros necessários para preencher os dados do relatorio
			//todos os parametros são armazenados via chave valor nessa variável
			Map<String, String> parametros = extrairParametros(ew);
			//extração dos dados de itens
			setItens(parametros, ew);

			String output = new String(encoded, "UTF-8");
			
			//extraimos todas as chaves e colocamos na lsita params
			Set<String> params = parametros.keySet();
			
			// iteramos todas as chaves aqui
			for (String key : params) {
				String chave = "{{" + key + "}}";
				try {
					output = output.replace(chave, parametros.get(key));
				} catch (Exception e) {
					System.out.println("Erro ao aplicar valor da chave: " + key);
				}
			}
			return output;
		} catch (Exception e) {
			e.printStackTrace();

			return "Erro ao imprimir";
		}
	}

	public String takeSnapshot(NeoBaseEntity workfowObject) {
		String content = buildContent(workfowObject);
		UUID id = UUID.randomUUID();
		NeoBaseEntity snapshotObject = AdapterUtils.createNewEntityInstance("SnapshotPedido");

		EntityWrapper snapshotWrapper = new EntityWrapper(snapshotObject);
		snapshotWrapper.setValue("content", content);
		snapshotWrapper.setValue("identificador", id.toString());

		PersistEngine.persist(snapshotWrapper.getObject());

		return id.toString();
	}

	private void setItens(Map<String, String> parametros, EntityWrapper ew) {
		StringBuilder itemOutput = new StringBuilder();
		List<NeoBaseEntity> itens = ew.findGenericValue("WPedInd.ItePIND");
		NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
		for (NeoBaseEntity item : itens) {

			EntityWrapper itemWrapper = new EntityWrapper(item);
			StringBuilder itemBuilder = new StringBuilder();
			BigDecimal valor = itemWrapper.findGenericValue("ValUniI");
			BigDecimal quantidade = itemWrapper.findGenericValue("QtdIND");
			BigDecimal valorum = itemWrapper.findGenericValue("VaorUnitarioProtheus");
			String valorunitum = formatCurrencyValue(valorum);
			BigDecimal quantidadeum = itemWrapper.findGenericValue("QtdProtheus");
		    String qtdum = nf.format(quantidadeum);
			
			
			String segundaum = itemWrapper.findGenericValue("UMtTOVS").toString();
			if (segundaum.equals("")){
				
				segundaum = "";
				valorunitum ="";
				qtdum = "";		
				
			} 
				
			

			itemBuilder.append("<tr>");
			itemBuilder.append("<td>");
			itemBuilder.append((String) itemWrapper.findGenericValue("CodItI"));
			itemBuilder.append("</td>");
			itemBuilder.append("<td>");
			itemBuilder.append(nf.format(quantidade));
			itemBuilder.append("<br>");
			itemBuilder.append(qtdum);
			itemBuilder.append("</td>");
			itemBuilder.append("<td>");
			itemBuilder.append(itemWrapper.findGenericValue("UnMedI").toString());
			itemBuilder.append("<br>");
			itemBuilder.append(segundaum);
			itemBuilder.append("</td>");
			itemBuilder.append("<td colspan='2' class='itemDesc'>");
			itemBuilder.append(itemWrapper.findGenericValue("DeItIND").toString());
//			itemBuilder.append("</td>");
//			itemBuilder.append("<td>");
			if (itemWrapper.findGenericValue("ObsItGe") != null && itemWrapper.findGenericValue("ObsItGe").toString().trim().length() > 1) {
				itemBuilder.append("<br>Obs: " + itemWrapper.findGenericValue("ObsItGe").toString());
			}
			itemBuilder.append("</td>");
			itemBuilder.append("<td>");
			itemBuilder.append(formatCurrencyValue(valor));
			itemBuilder.append("<br>");
			itemBuilder.append(valorunitum);
			itemBuilder.append("</td>");
			itemBuilder.append("<td colspan=\"2\">");
			itemBuilder.append(itemWrapper.findGenericValue("AliqIPi").toString());
			itemBuilder.append("</td>");
			itemBuilder.append("<td>");
			itemBuilder.append(formatCurrencyValue(valor.multiply(quantidade)));
			itemBuilder.append("</td>");
			itemBuilder.append("</tr>");

			itemOutput.append(itemBuilder);
		}

		parametros.put("itemList", itemOutput.toString());
	}

	private Map<String, String> extrairParametros(EntityWrapper ew) {
		Map<String, String> p = new HashMap<String, String>();

		p.put("tpproc", getValue(ew, "WPedInd.SitPedI.DescSIT").toUpperCase());
		p.put("nrPedido", getValue(ew, "WPedInd.NumPedI"));
		p.put("clientName", getValue(ew, "WPedInd.ClienteTotvs.ClienteTotvs.a1_nome"));
		p.put("clientCode", getValue(ew, "WPedInd.ClienteTotvs.ClienteTotvs.a1_zfusion"));
		if (getValue(ew, "WPedInd.ClienteTotvs.ClienteTotvs.a1_pessoa").toUpperCase().equals("J")) {
			p.put("clienteDOC", TecnoperfilServletUtils.aplicaMascara(getValue(ew, "WPedInd.ClienteTotvs.ClienteTotvs.a1_cgc"), true));
		} else {
			p.put("clienteDOC", TecnoperfilServletUtils.aplicaMascara(getValue(ew, "WPedInd.ClienteTotvs.ClienteTotvs.a1_cgc"), false));
		}

		p.put("clientIE", getValue(ew, "WPedInd.ClienteTotvs.ClienteTotvs.a1_inscrm"));
		p.put("clientSuframa", getValue(ew, "WPedInd.ClienteTotvs.ClienteTotvs.a1_suframa"));
		p.put("endereco", getValue(ew, "WPedInd.ClienteTotvs.ClienteTotvs.a1_end"));
		p.put("enderecoBairro", getValue(ew, "WPedInd.ClienteTotvs.ClienteTotvs.a1_bairro"));
		p.put("enderecoCidade", getValue(ew, "WPedInd.ClienteTotvs.ClienteTotvs.a1_mun"));
		p.put("enderecoUF", getValue(ew, "WPedInd.ClienteTotvs.ClienteTotvs.a1_est"));
		p.put("enderecoCEP", getValue(ew, "WPedInd.ClienteTotvs.ClienteTotvs.a1_cep").toString());
		try {
			String phone = getValue(ew, "WPedInd.ClienteTotvs.ClienteTotvs.a1_ddd").toString() + " "
					+ getValue(ew, "WPedInd.ClienteTotvs.ClienteTotvs.a1_tel").toString();
			p.put("clienteFixo", phone);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		try {
			String phone = getValue(ew, "WPedInd.ClienteTotvs.ClienteTotvs.a1_ddd").toString() + " "
					+ getValue(ew, "WPedInd.ClienteTotvs.ClienteTotvs.a1_tel").toString();
			p.put("clienteCelular", phone);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		p.put("condpgto", getValue(ew, "WPedInd.CondpagTotvs.e4_descri"));
		p.put("natoperacao", getValue(ew, "WPedInd.TipoDeOperacao.x5_descri"));
		p.put("transportadora", getValue(ew, "WPedInd.TraspoficialT.a4_nome"));
		
		Boolean TemEmbarque = ew.findGenericValue("WPedInd.TemEntrdifTotvs.InformeSimOuNao");
		
		if(TemEmbarque) {
			
			String tipoembarque = ew.findGenericValue("WPedInd.TipoDeEntregaDif.Codigo");
		 
		if (tipoembarque.equals("3")) {
			
			p.put("localEntrega", getValue(ew, "WPedInd.LocEntreg"));
			p.put("entregaCidade", getValue(ew, "WPedInd.TransportadoraRedespacho.a4_mun"));
			p.put("entregaUF", getValue(ew, "WPedInd.TransportadoraRedespacho.a4_est")); 
		}	
		if (tipoembarque.equals("1")) {
				
			p.put("localEntrega", getValue(ew, "WPedInd.LocEntreg"));
			p.put("entregaCidade", getValue(ew, "WPedInd.MunicipioDeEntrega.cc2_mun"));
			p.put("entregaUF", getValue(ew, "WPedInd.MunicipioDeEntrega.cc2_est")); 	
		}	
		if (tipoembarque.equals("2")||tipoembarque.equals("4")     ) {
					
			p.put("localEntrega", getValue(ew, "WPedInd.LocalEntregacli"));
			p.put("entregaCidade", getValue(ew, "WPedInd.ClienteEntrega.a1_mun"));
			p.put("entregaUF", getValue(ew, "WPedInd.ClienteEntrega.a1_est"));	
		}   
		   
		} else{
			
			p.put("localEntrega","");
			p.put("entregaCidade","");
			p.put("entregaUF","");
		}
		
		

		try {
			GregorianCalendar emissao = ew.findGenericValue("WPedInd.DatEMI");
			p.put("dtEmissao", NeoCalendarUtils.dateToString(emissao));
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			GregorianCalendar emissao = ew.findGenericValue("WPedInd.PrevEmI");
			p.put("dtEmbarque", NeoCalendarUtils.dateToString(emissao));
		} catch (Exception e) {
			// TODO: handle exception
		}
		p.put("tpFrete", getValue(ew, "WPedInd.TipFINd.DescrI"));
		p.put("ordemCompra", getValue(ew, "WPedInd.OrdComI").toString());
		p.put("vendedor", getValue(ew, "WPedInd.AgVeInd.NoRepr.nome_empresa"));
		try {
			Set<NeoPhone> telefones = ew.findGenericValue("WPedInd.AgVeInd.UserFu.phoneList");
			if (telefones.size() > 0) {
				NeoPhone neoPhone = (NeoPhone) telefones.toArray()[0];
				p.put("vendedorCelular", neoPhone.getNumber());
			} else {
				p.put("vendedorCelular", "");
			}
		} catch (Exception e) {
			p.put("vendedorCelular", "");
			e.printStackTrace();
		}
		p.put("vendedorEmail", getValue(ew, "WPedInd.AgVeInd.UserFu.email"));
		p.put("vlTotalMer", getCurrency(ew, "WPedInd.VatTMer"));
		p.put("vlFrete", getCurrency(ew, "WPedInd.VlFretI"));
		p.put("vlIpiMerc", getCurrency(ew, "WPedInd.ValTIPIM"));
		p.put("vlIpiFrete", getCurrency(ew, "WPedInd.VlIPIFI"));
		p.put("vlTotalIPI", getCurrency(ew, "WPedInd.VLTOIPI"));
		p.put("vlST", getCurrency(ew, "WPedInd.VlTICMSST"));
		p.put("vlTotal", getCurrency(ew, "WPedInd.VlTotPedI"));
		p.put("obsPedido", getValue(ew, "WPedInd.ObsPedI"));

		return p;
		
	}

	private String getValue(EntityWrapper wrapper, String key) {
		try {
			Object value = wrapper.findGenericValue(key);
			if (value != null) {
				if (value instanceof String) {
					return (String) value;
				}
				if (value instanceof BigDecimal) {
					BigDecimal num = (BigDecimal) value;

					return num.toString();
				}
				if (value instanceof Long) {
					Long num = (Long) value;

					return num.toString();
					
					
					
				}
				return value.toString();
			}
		} catch (Exception e) {
			System.out.println("Erro ao extrair " + key);

			return "";
		}

		return "";
	}

	private String getCurrency(EntityWrapper wrapper, String key) {
		try {
			BigDecimal value = wrapper.findGenericValue(key);
			if (value != null) {
				return formatCurrencyValue(value);
			}
		} catch (Exception e) {
			System.out.println("Erro ao extrair " + key);

			return "";
		}

		return "";
	}

	public static String formatCurrencyValue(BigDecimal value) {
		if (value == null) {
			return "";
		}
		try {
			NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pt", "BR"));

			return nf.getCurrencyInstance().format(value);
		} catch (Exception e) {
			System.out.println("Erro ao aplicar máscara monetária: " + value.toEngineeringString());
		}
		return "R$ 0,00";
	}
	
	
}
