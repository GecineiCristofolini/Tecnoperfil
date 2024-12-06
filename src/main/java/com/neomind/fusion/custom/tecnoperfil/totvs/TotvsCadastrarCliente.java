package com.neomind.fusion.custom.tecnoperfil.totvs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.custom.tecnoperfil.totvs.entidades.CadCliente;
import com.neomind.fusion.custom.tecnoperfil.totvs.entidades.Contato;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.persist.PersistEngine;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;
import com.neomind.fusion.workflow.adapter.AdapterUtils;
import com.neomind.fusion.workflow.exception.WorkflowException;

public class TotvsCadastrarCliente implements AdapterInterface
{

	private static final Log log = LogFactory.getLog(TotvsCadastrarCliente.class);

	@Override
	public void start(Task arg0, EntityWrapper wrapercliente, Activity arg2)
	{
		try
		{
			log.debug("Iniciar busca de info");
			
			String json = buscarInformacoes(wrapercliente);
			log.debug("informacoes retornadas + " + json);
			CadastrarCliente cadCliente = new CadastrarCliente();
			log.debug("Iniciando cadastro do cliente");
			cadCliente.cadastroDeCliente(json);

		}
		catch (WorkflowException e)
		{
			e.printStackTrace();
			log.error("Erro ao cadastrar cliente", e);
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Erro ao iniciar atendimento", e);
			throw new WorkflowException("Erro ao iniciar atendimento" + e.getCause());
		}

	}

	@Override
	public void back(EntityWrapper arg0, Activity arg1)
	{
		// TODO Auto-generated method stub

	}

	public String buscarInformacoes(EntityWrapper wrapercliente)
	{

		try
		{

			String codcliente = wrapercliente.findGenericValue("CodCli");

			if (codcliente == null || codcliente.isBlank() )
			{

				List<NeoObject> sequenciadorcliente = PersistEngine
						.getObjects(AdapterUtils.getEntityClass("CadastroDeClientesParametros"));

				for (NeoObject neosec : sequenciadorcliente)
				{

					EntityWrapper wrappersequenciador = new EntityWrapper(neosec);

					long numero = (long) wrappersequenciador.findField("CodigoCliente").getValue();
					codcliente = Long.toString(numero);
					System.out.println(codcliente);

					numero = numero + 1;

					wrappersequenciador.setValue("CodigoCliente", numero);

				}

				// Seta o Valor Codigo Cliente
				wrapercliente.setValue("CodCli", codcliente);

			}
			
			

			CadCliente cadcli = new CadCliente();
			cadcli.setIdFusion(codcliente);
			cadcli.setCgc(wrapercliente.findGenericValue("CGC"));
			cadcli.setNomeReduzido(wrapercliente.findGenericValue("NomFan"));
			cadcli.setNome(wrapercliente.findGenericValue("RazSoc"));
			String inscriestadual = wrapercliente.findGenericValue("InsEsta");
			if (inscriestadual == null || inscriestadual.isBlank())
			{
				inscriestadual = "ISENTO";
			}
			cadcli.setInscricaoEstadual(inscriestadual);
			cadcli.setInscricaoMunicipal(inscriestadual);
			String estado = wrapercliente.findGenericValue(("EstadoTotvs.x5_chave"));
			String estadosupreespaco = estado.trim();
			cadcli.setEstado(estadosupreespaco);
			cadcli.setBairro(wrapercliente.findGenericValue("Bairro"));
			String nomemunicipio = wrapercliente.findGenericValue(("MunicipioTovs.cc2_mun"));
			cadcli.setMunicipio(nomemunicipio.trim());
			String codmunicipio = wrapercliente.findGenericValue("MunicipioTovs.cc2_codmun");
			cadcli.setCodigoMunicipio(codmunicipio.trim());
			String endereco = wrapercliente.findGenericValue("EnderecoCompleto");
			cadcli.setEndereco(endereco.trim());
			cadcli.setCep(wrapercliente.findGenericValue("CEP"));
			
			boolean Clientexp = wrapercliente.findGenericValue("ClienteExportacao");
			
			
			if (Clientexp == true)
			{
                
				cadcli.setCep("00000000");
				cadcli.setMunicipio(wrapercliente.findGenericValue("MunicipioEstrangeiro"));

			}
			

			
			

			List<NeoObject> listaDanfe = wrapercliente.findGenericValue("Email");

			String Listaemail = null;

			for (NeoObject Listaordemdanfe : listaDanfe)
			{

				EntityWrapper danfeWrapper = new EntityWrapper(Listaordemdanfe);

				if (Listaemail == null)
				{

					Listaemail = danfeWrapper.findGenericValue("Email");
				}
				else
				{

					Listaemail += ";" + danfeWrapper.findGenericValue("Email");

				}

			}

			cadcli.setEmailFinanceiro(Listaemail);

			cadcli.setTipo(wrapercliente.findGenericValue("PessoaT"));
			String pais = wrapercliente.findGenericValue("PaisTovs.ya_codgi");
			cadcli.setPais(pais.trim());
			cadcli.setTipoCliente(wrapercliente.findGenericValue("TipoCliente.Tipo"));
			cadcli.setCodigoSuframa(wrapercliente.findGenericValue("Sufram"));
			cadcli.setTabeladePreco("");
			

			boolean clintetransp = wrapercliente.findGenericValue("TraEspe");

			if (clintetransp == false)
			{

				cadcli.setTransportadora("");

			}
			else
			{
                String transportadora = wrapercliente.findGenericValue("TransportadoraTOTVS.a4_cod");
				cadcli.setTransportadora(transportadora.trim());

			}

			boolean condpag = wrapercliente.findGenericValue("CodEspe");

			if (condpag == false)
			{

				cadcli.setCodicaoPagamento("");

			}
			else
			{

				String condpagmento = wrapercliente.findGenericValue("CondicaoDePagamentoTotvs.e4_codigo");
				cadcli.setCodicaoPagamento(condpagmento.trim());

			}

			boolean enderecoentrega = wrapercliente.findGenericValue("CteloE");

			if (enderecoentrega == false)
			{

				cadcli.setCodigoMunicipioEntrega("");
				cadcli.setUfEntrega("");
				cadcli.setBairroEntrega("");
				cadcli.setEnderecoEntrega("");
				cadcli.setCepentrega("");
				cadcli.setCodclientrega("");
				cadcli.setLojaclientrega("");

			}
			else
			{
                String codmunentrega = wrapercliente.findGenericValue("MunicipioDeEntrega.cc2_codmun");
				cadcli.setCodigoMunicipioEntrega(codmunentrega.trim());
				cadcli.setUfEntrega(wrapercliente.findGenericValue("UFDeEntrega"));
				cadcli.setBairroEntrega(wrapercliente.findGenericValue("BairroDeEntrega"));
				cadcli.setEnderecoEntrega(wrapercliente.findGenericValue("EnderecoDeEntrega"));
				cadcli.setCepentrega(wrapercliente.findGenericValue("CepDeEntrega"));
				cadcli.setCodclientrega(wrapercliente.findGenericValue("infocodtotvs.a1_cod"));
				cadcli.setLojaclientrega(wrapercliente.findGenericValue("infocodtotvs.a1_loja"));
			}

			boolean tipofrete = wrapercliente.findGenericValue("Clietipfret");

			if (tipofrete == false)
			{

				cadcli.setTipoFrete("");

			}
			else
			{

				cadcli.setTipoFrete(wrapercliente.findGenericValue("TipoFreteC.Codigo"));

			}

			cadcli.setContribuinte(wrapercliente.findGenericValue("ContribuinteICMS.Codigo"));
			cadcli.setDocumentoEstrageiro(wrapercliente.findGenericValue("DocumentoEstrangeiro"));

			cadcli.setOptanteSimplesNacional(wrapercliente.findGenericValue("OptanteDoSimplesNacional.Codigo"));
			cadcli.setGrupoclientes(wrapercliente.findGenericValue("CodigoGrupoCliente"));
			
			cadcli.setDescontosuframa(wrapercliente.findGenericValue("DescontoFiscais.Codigo"));
			String paisbacen = wrapercliente.findGenericValue("PaisBacen.cch_codigo");
			cadcli.setCodpaisbacen(paisbacen.trim());
			String segmento1 = wrapercliente.findGenericValue("CategoriaTotvs.x5_chave");
			cadcli.setSegmento1(segmento1.trim());
			cadcli.setSegmento2("");
			cadcli.setSegmento3("");
			cadcli.setSegmento4("");
			cadcli.setSegmento5("");
			cadcli.setStatus("2");
			cadcli.setCodvendedor("");
			String codmuniciposuframa = wrapercliente.findGenericValue("CodigoMunicipioSuframa");
			cadcli.setCodigomunicipiosuframa(codmuniciposuframa.trim());

			cadcli.setContacontabil(wrapercliente.findGenericValue("ContaContabil"));
			cadcli.setNaturezafinanceira(wrapercliente.findGenericValue("NaturezaFinanceira"));

			List<NeoObject> listaContato = wrapercliente.findGenericValue("Telefo");

			List<Contato> contatos = new ArrayList<Contato>();

			for (NeoObject Listaordemcontato : listaContato)
			{

				EntityWrapper contatoWrapper = new EntityWrapper(Listaordemcontato);

				cadcli.setEmail(contatoWrapper.findGenericValue("Telefo.Email"));

				Contato contato = new Contato();

				contato.setNome(contatoWrapper.findGenericValue("Telefo.Contato"));
				cadcli.setEmail(contatoWrapper.findGenericValue("Telefo.Email"));
				contato.setEmail(contatoWrapper.findGenericValue("Telefo.Email"));
				
						

				cadcli.setDdd(contatoWrapper.findGenericValue("Telefo.DDD"));
				cadcli.setDdi(contatoWrapper.findGenericValue("Telefo.DDI"));
				cadcli.setTelefone(contatoWrapper.findGenericValue("Telefo.Telefone"));

				contato.setDd(contatoWrapper.findGenericValue("Telefo.DDD"));
				contato.setDdi(contatoWrapper.findGenericValue("Telefo.DDI"));
				contato.setTelefone(contatoWrapper.findGenericValue("Telefo.Telefone"));

				
				
				contatos.add(contato);

			}

			cadcli.setContatos(contatos);

			String jsoncli = new Gson().toJson(cadcli);

			System.out.println(jsoncli.toString());
			return jsoncli;

		}
		catch (Exception e)
		{

			log.error("Erro ao cadastrar cliente : " + e.getCause());
			System.out.print(e.getMessage());
			e.printStackTrace();
			throw new WorkflowException("Erro ao cadastrar cliente" + e.getCause());

		}

	}

}