package com.neomind.fusion.custom.tecnoperfil;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;



import org.json.simple.JSONArray;

import com.neomind.framework.base.entity.NeoBaseEntity;
import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.custom.tecnoperfil.bitrix.BitrixConnect;
import com.neomind.fusion.custom.tecnoperfil.bitrix.CompanyRequest;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;
import com.neomind.fusion.workflow.exception.WorkflowException;



public class ValidaClienteAdapter implements AdapterInterface {

	@Override
	public void start(Task arg0, EntityWrapper wrapper, Activity arg2) {

		CompanyRequest company = new CompanyRequest();
		String razaoSocial = wrapper.findGenericValue("RazSoc");// findGenericValue("novocliente.razaosocial");
		String codigoKugel = wrapper.findGenericValue("CodCli");

		razaoSocial = codigoKugel.concat(" - ").concat(razaoSocial);
		String nomeFantasia = wrapper.findGenericValue("NomFan");

		String cidade = wrapper.findGenericValue("MunicipioTovs.cc2_mun");
		cidade = cidade.trim();
		
		String estado = wrapper.findGenericValue("EstadoTotvs.x5_chave");
		estado = estado.trim();
		String bairro = wrapper.findGenericValue("Bairro");
		String complemento = wrapper.findGenericValue("Comple");
		String cep = wrapper.findGenericValue("CEP");
		// String email = wrapper.findGenericValue("novocliente.email");
		/*
		 * Long telefone = wrapper.findGenericValue("novocliente.telefone"); Long
		 * celular = wrapper.findGenericValue("novocliente.TelefoneCelular"); Long whats
		 * = wrapper.findGenericValue("novocliente.TelefoneWhats");
		 */
		String logradouro = wrapper.findGenericValue("Lograd.Abrevi");
		String endereco = wrapper.findGenericValue("EnderecoCompleto");
		Long numero = wrapper.findGenericValue("Numero");
		if (numero == null) {
			numero = 0L;
		}

		String categoria = wrapper.findGenericValue("CategoriaTotvs.x5_descri");
		StringBuilder rua = new StringBuilder();
		
		rua.append(logradouro);
		rua.append(" ");
		rua.append(endereco);
		rua.append(" ");
		rua.append(numero == null ? "" :numero.toString());
		rua.append("");
		rua.append(bairro);
		
		String inscricaoEstadual = wrapper.findGenericValue("InsEsta");
		String classificacao = wrapper.findGenericValue("SegmentoTotvs.Segmento");
		Integer pessoa = wrapper.findGenericValue("Pessoa");
		Boolean pessoaJuridica = pessoa != null ? pessoa == 1 : false;

		List<NeoBaseEntity> emails = wrapper.findGenericValue("Email");
		List<NeoBaseEntity> telefones = wrapper.findGenericValue("Telefo");

//		if (emails != null && !emails.isEmpty()) {
//			for (NeoBaseEntity emailObject : emails) {
//				EntityWrapper mailWrapper = new EntityWrapper(emailObject);
//				String email = mailWrapper.findGenericValue("Email");
//
//			    try {
//					InternetAddress emailAddr = new InternetAddress(email);
//					emailAddr.validate();
//
//					company.getEmails().add(email);
//				} catch (Exception e) {
//					email = null;
//				}
//			}
//		}

		if (telefones != null && !telefones.isEmpty()) {
			for (NeoBaseEntity telefone : telefones) {
				EntityWrapper telWrapper = new EntityWrapper(telefone);
				Long number;
				Boolean isWhatsApp = telWrapper.findGenericValue("WhatsApp");
				Integer indNacional = telWrapper.findGenericValue("TipTelefone");

				if (indNacional == 2) {
					String numerointernacional;
					String email = telWrapper.findGenericValue("Email");
					numerointernacional = telWrapper.findGenericValue("NumeroInternacional");
					String ddi;
					ddi = telWrapper.findGenericValue("DDI");
					company.getTelefones().add(ddi + numerointernacional);
					company.getEmails().add(email);
				} else {
					number = telWrapper.findGenericValue("Numero");
					company.getTelefones().add("0" + number.toString());
					String email = telWrapper.findGenericValue("Email");
					company.getEmails().add(email);
					if (isWhatsApp) {
						company.getTelefones().add("55" + number.toString());
					}
				}
			}
		}

		// Long cep = wrapper.findGenericValue("pedido.clientes.cep");
		String cnpj;
		try {
			cnpj = wrapper.findGenericValue("CNPJ");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			System.out.println("BITRIX CONNECTION - CREATE COMPANY");
			System.out.println("BITRIX CONNECTION - RAZAOSOCIAL " + razaoSocial);


			if (codigoKugel == null) {
				System.out.println("BITRIX CONNECTION - ID KUGEL UNDEFINED");
			} else {
				System.out.println("BITRIX CONNECTION - ID KUGEL " + codigoKugel);
			}

			String codigoResponsavel = "22"; // Recpetivo CCV
			try {
				JSONObject responsavelJson = findUser((wrapper.findGenericValue("ReBitrix")));
				codigoResponsavel = (String) responsavelJson.get("ID");
			} catch (Exception e) {
				codigoResponsavel = "127";
			}

			company.setNomeFantasia(nomeFantasia);
			company.setTitle(razaoSocial);
			company.setCodigoKugel(codigoKugel);
			company.setEstado(getEstadoCode(estado));
			company.setCidade(cidade);
			company.setBairro(bairro);
			company.setComplemento(complemento);
			
			if (pessoaJuridica) {
				cnpj = wrapper.findGenericValue("CNPJ");
				String strCnpj = cnpj.toString();
				int cnpjLength = 14;
				int length = strCnpj.length();
				int diff = cnpjLength - length;
				if (diff > -1) {
					for (int i = 0; i < diff; i++) {
						strCnpj = "0" + strCnpj;
					}
				}
				company.setCnpj(String.valueOf(strCnpj));
			} else {
				String strCpf = wrapper.findGenericValue("CPF");

				int cpfLength = 11;
				int length = strCpf.length();
				int diff = cpfLength - length;
				if (diff > -1) {
					for (int i = 0; i < diff; i++) {
						strCpf = "0" + strCpf;
					}
				}

				company.setCpf(strCpf);
			}
			company.setInscricaoEstadual(inscricaoEstadual == null ? "ISENTO" : inscricaoEstadual);

			company.setResponsavel(codigoResponsavel);

//			StringBuilder endereco = new StringBuilder();
//			endereco.append(rua);
//			endereco.append(" ");
//			endereco.append(numero);
			company.setCategoria(getCategoriaCode(categoria));

			company.setEndereco(endereco.toString());
			company.setNumero(numero.toString());

			if (classificacao != null) {
				company.setClassificacao(getSeguimentoCode(classificacao));
			}

			if (cep != null) {
				company.setCep(cep.toString());
			}


			createCliente(company);

		} catch (Exception e) {
			e.printStackTrace();

			throw new WorkflowException("Erro ao efetuar consulta no bitrix: " + e.getMessage());
		}

	}

	@Override
	public void back(EntityWrapper wrapper, Activity arg1) {

	}

	private static BitrixConnect getConnector() {
		BitrixConnect bConnect = new BitrixConnect();

		bConnect.configure("https://tecnoperfil.bitrix24.com.br/rest/8/", "oh712h1nelw14f6n");

		return bConnect;
	}

	private boolean createCliente(CompanyRequest company) {

		boolean sucesso = getConnector().createCompany(company);

		return sucesso;
	}

	public static JSONObject findCliente(String razaoSocial, String codigoKugel) {
		try {
			System.out.println("BITRIX - BUSCA " + razaoSocial);

			JSONObject response = getConnector().findCompany(razaoSocial, codigoKugel);
			JSONArray result = (JSONArray) response.get("result");
			int qtdResult = result.size();

			System.out.println("Result size: " + qtdResult);
			if (qtdResult > 1) {
				for (int i = 0; i < qtdResult; i++) {
					JSONObject json = (JSONObject) result.get(i);
					String kugelId = (String) json.get("UF_CRM_5DA47CA729CB2");

					if (kugelId.equals(codigoKugel.trim())) {
						return json;
					}
				}
			} else {
				if (result.size() > 0) {
					return (JSONObject) result.get(0);
				} else {
					return null;
				}
			}

			return null;
		} catch (Exception e) {
			e.printStackTrace();

			throw new WorkflowException("Erro ao efetuar consulta no bitrix");
		}
	}

	public static JSONObject findUser(String email) {
		try {
			System.out.println("BITRIX - BUSCA POR EMAIL" + email);

			JSONObject response = getConnector().findUserByEmail(email);
			JSONArray result = (JSONArray) response.get("result");

			if (result.size() > 0) {
				return (JSONObject) result.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();

			throw new WorkflowException("Erro ao efetuar consulta no bitrix");
		}
	}

	private String getCategoriaCode(String categoria) {
		
		System.out.println(">>>>> BUSCA COD CATEGORIA:" + categoria);
		Map<String, String> categorias = new HashMap<>();

		categorias.put("VRJ-ATACADISTA", "1193");
		categorias.put("EPC-CONSTRUTORA", "1195");
		categorias.put("EPC-CONSUMIDOR - CNPJ", "1197");
		categorias.put("FIS-CONSUMIDOR - CPF", "1199");
		categorias.put("VRJ-DISTRIBUIDOR", "1201");
		categorias.put("EPC-ESCOLA", "1203");
		categorias.put("VRJ-HOME CENTER", "1205");
		categorias.put("EPC-ARQUITETOS", "3387");
		categorias.put("EPC-HOSPITAL", "1207");
		categorias.put("EPC-HOTEL", "1209");
		categorias.put("VRJ-INSTALADOR VAREJO", "1211");
		categorias.put("EPC-POSTO DE COMBUSTIVEL", "3389");
		categorias.put("VRJ-LOJA DE DECORACAO", "1213");
		categorias.put("VRJ-MADEIREIRA", "1215");
		categorias.put("EPC-MONTADOR STANDS", "1217");
		categorias.put("VRJ-REVENDEDOR DE MAT. CONST.", "1219");
		categorias.put("VRJ-REVENDEDOR E INSTALADOR", "1221");
		categorias.put("VRJ-REVENDEDOR PERFIS DE ALUMINIO", "1223");
		categorias.put("HDP-HIDROPONIA", "1229");		
		categorias.put("HDP-HIDROPONIA - FRANQUEADO", "1231");
		categorias.put("HDP-AGENTE PIC", "1225");
		categorias.put("HDP-DISTRIBUIDOR", "1227");
		categorias.put("HDP-REVENDA GERAL PEFIL-ACESS", "5157");
		categorias.put("HDP-PRODUTOR RURAL", "1235");
		categorias.put("HDP-LINHA HOBBY-PESSOA FISICA", "1233");
		categorias.put("HDP-FAZENDAS URBANAS", "5159");
		categorias.put("HDP-REVENDA LINHA HOBBY", "1237");
		categorias.put("IND-AVICULTURA E SUINOCULTURA", "1239");
		categorias.put("IND-COMUNICACAO VISUAL", "1241");
		categorias.put("IND-CONSUMIDOR CNPJ", "1243");
		categorias.put("IND-DIVERSOS", "1245");
		categorias.put("IND-FAB.APAR.REC.AUDIO E VID", "1247");
		categorias.put("IND-FABR. EQUIP. GINASTICA", "1249");
		categorias.put("IND-FABR. GELAD. E FREEZERS", "1251");
		categorias.put("IND-FABR. GOND. E CHECK-OUTS", "1253");
		categorias.put("IND-FABR. PISOS ELEVADOS", "1255");
		categorias.put("IND-FABR. REFRIG. INDUSTRIAL.", "1257");
		categorias.put("IND-FABRICANTE ESQUADRIAS", "1259");
		categorias.put("IND-FARMACIAS", "1261");
		categorias.put("IND-INDUSTRIA DE MOVEIS", "1263");
		categorias.put("IND-MONTADORES DE VEICULOS", "1265");
		categorias.put("IND-PRODUTOS ODONTOLOGICOS", "1267");
		categorias.put("IND-REVENDA", "1269");
		categorias.put("IND-SUPERMERCADOS", "1271");
		categorias.put("EXP-ARQUITETOS", "1273");
		categorias.put("EXP-CONSTRUTORAS", "1275");
		categorias.put("EXP-DISTRIBUIDOR", "1277");
		categorias.put("EXP-IMPORTADORES", "1279");
		categorias.put("EXP-REVENDA", "1281");
		categorias.put("EXP-EXPORTACAO", "1283");
		categorias.put("REPRESENTANTE", "1404");
		categorias.put("TI", "1410");
		categorias.put("EPC-REVENDEDOR DE ESPECIFICACAO", "7375");

		return categorias.get(categoria.trim());
	}

	private String getSeguimentoCode(String seguimento) {
		Map<String, String> seguimentos = new HashMap<>();

		seguimentos.put("VRJ", "1183");
		seguimentos.put("EPC", "1185");
		seguimentos.put("HDP", "1187");
		seguimentos.put("IND", "1189");
		seguimentos.put("EXP", "1191");
		seguimentos.put("TI", "1408");

		return seguimentos.get(seguimento);
	}

	private String getEstadoCode(String sigla) {
		Map<String, String> estados = new HashMap<String, String>();

		estados.put("AC", "152");
		estados.put("AL", "154");
		estados.put("AP", "156");
		estados.put("AM", "158");
		estados.put("BA", "160");
		estados.put("CE", "162");
		estados.put("DF", "164");
		estados.put("ES", "166");
		estados.put("GO", "168");
		estados.put("MA", "170");
		estados.put("MT", "172");
		estados.put("MS", "174");
		estados.put("MG", "176");
		estados.put("PA", "178");
		estados.put("PB", "180");
		estados.put("PR", "182");
		estados.put("PE", "184");
		estados.put("PI", "186");
		estados.put("RJ", "188");
		estados.put("RN", "190");
		estados.put("RS", "192");
		estados.put("RO", "194");
		estados.put("RR", "196");
		estados.put("SC", "198");
		estados.put("SP", "200");
		estados.put("SE", "202");
		estados.put("TO", "204");

		return estados.get(sigla);
	}

}
