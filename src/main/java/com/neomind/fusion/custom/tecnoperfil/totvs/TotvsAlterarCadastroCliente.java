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
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;
import com.neomind.fusion.workflow.exception.WorkflowException;

public class TotvsAlterarCadastroCliente implements AdapterInterface
{

	private static final Log log = LogFactory.getLog(TotvsAlterarCadastroCliente.class);

	@Override
	public void start(Task arg0, EntityWrapper wrapercliente, Activity arg2)
	{
		try
		{
			log.debug("Iniciar busca de info");
			
			String idcliente = wrapercliente.findGenericValue("CodigoProtheus");
			String json = buscarInformacoes(wrapercliente);
			
			log.debug("informacoes retornadas + " + json);
			AlterarCadastroCliente altcadCliente = new AlterarCadastroCliente();
			log.debug("Iniciando cadastro do cliente");
			altcadCliente.alterarCadastroCliente(json,idcliente);

		}
		catch (WorkflowException e)
		{
			e.printStackTrace();
			log.error("Erro ao Alterar cadastrar cliente", e);
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

		

					

			CadCliente cadcli = new CadCliente();
			cadcli.setIdFusion(wrapercliente.findGenericValue("AltCli.CodCli"));
			String cgc = wrapercliente.findGenericValue("AltCli.CGC"); 
			cgc = cgc.replaceAll("[^0-9]+", "");
			cadcli.setCgc(cgc);
			String nomereduzido = wrapercliente.findGenericValue("AltCli.NomFan");
			cadcli.setNomeReduzido(nomereduzido.trim());
			String nome = wrapercliente.findGenericValue("AltCli.RazSoc");
			cadcli.setNome(nome.trim());
			String inscriestadual = wrapercliente.findGenericValue("AltCli.InsEsta");
			if (inscriestadual == null || inscriestadual.isBlank())
			{
				inscriestadual = "ISENTO";
			}
			cadcli.setInscricaoEstadual(inscriestadual);
			cadcli.setInscricaoMunicipal(inscriestadual);
			String estado = wrapercliente.findGenericValue(("AltCli.EstadoTotvs.x5_chave"));
			String estadosupreespaco = estado.trim();
			cadcli.setEstado(estadosupreespaco);
			cadcli.setBairro(wrapercliente.findGenericValue("AltCli.Bairro"));
			String nomemunicipio = wrapercliente.findGenericValue(("AltCli.MunicipioTovs.cc2_mun"));
			cadcli.setMunicipio(nomemunicipio.trim());
			String codmunicipio = wrapercliente.findGenericValue("AltCli.MunicipioTovs.cc2_codmun");
			cadcli.setCodigoMunicipio(codmunicipio.trim());
			String endereco = wrapercliente.findGenericValue("AltCli.EnderecoCompleto");
			cadcli.setEndereco(endereco.trim());
			cadcli.setCep(wrapercliente.findGenericValue("AltCli.CEP"));
			
			boolean Clientexp = wrapercliente.findGenericValue("AltCli.ClienteExportacao");
			
			
			if (Clientexp == true)
			{
                
				cadcli.setCep("00000000");
				cadcli.setMunicipio(wrapercliente.findGenericValue("AltCli.MunicipioEstrangeiro"));

			}
			

			
			

			List<NeoObject> listaDanfe = wrapercliente.findGenericValue("AltCli.Email");

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

			cadcli.setTipo(wrapercliente.findGenericValue("AltCli.PessoaT"));
			String pais = wrapercliente.findGenericValue("AltCli.PaisTovs.ya_codgi");
			cadcli.setPais(pais.trim());
			cadcli.setTipoCliente(wrapercliente.findGenericValue("AltCli.TipoCliente.Tipo"));
			cadcli.setCodigoSuframa(wrapercliente.findGenericValue("AltCli.Sufram"));
			cadcli.setTabeladePreco("");
			

			boolean clintetransp = wrapercliente.findGenericValue("AltCli.TraEspe");

			if (clintetransp == false)
			{

				cadcli.setTransportadora("");

			}
			else
			{
                String transportadora = wrapercliente.findGenericValue("AltCli.TransportadoraTOTVS.a4_cod");
				cadcli.setTransportadora(transportadora.trim());

			}

			boolean condpag = wrapercliente.findGenericValue("AltCli.CodEspe");

			if (condpag == false)
			{

				cadcli.setCodicaoPagamento("");

			}
			else
			{

				String condpagmento = wrapercliente.findGenericValue("AltCli.CondicaoDePagamentoTotvs.e4_codigo");
				cadcli.setCodicaoPagamento(condpagmento.trim());

			}

			boolean enderecoentrega = wrapercliente.findGenericValue("AltCli.CteloE");

			if (enderecoentrega == false)
			{

				cadcli.setCodigoMunicipioEntrega("");
				cadcli.setUfEntrega("");
				cadcli.setBairroEntrega("");
				cadcli.setEnderecoEntrega("");
				cadcli.setCepentrega("");

			}
			else
			{
                String codmunentrega = wrapercliente.findGenericValue("AltCli.MunicipioDeEntrega.cc2_codmun");
				cadcli.setCodigoMunicipioEntrega(codmunentrega.trim());
				cadcli.setUfEntrega(wrapercliente.findGenericValue("AltCli.UFDeEntrega"));
				cadcli.setBairroEntrega(wrapercliente.findGenericValue("AltCli.BairroDeEntrega"));
				cadcli.setEnderecoEntrega(wrapercliente.findGenericValue("AltCli.EnderecoDeEntrega"));
				cadcli.setCepentrega(wrapercliente.findGenericValue("AltCli.CepDeEntrega"));
			}

			boolean tipofrete = wrapercliente.findGenericValue("AltCli.Clietipfret");

			if (tipofrete == false)
			{

				cadcli.setTipoFrete("");

			}
			else
			{

				cadcli.setTipoFrete(wrapercliente.findGenericValue("AltCli.TipoFreteC.Codigo"));

			}

			cadcli.setContribuinte(wrapercliente.findGenericValue("AltCli.ContribuinteICMS.Codigo"));
			cadcli.setDocumentoEstrageiro(wrapercliente.findGenericValue("AltCli.DocumentoEstrangeiro"));

			cadcli.setOptanteSimplesNacional(wrapercliente.findGenericValue("AltCli.OptanteDoSimplesNacional.Codigo"));
			cadcli.setGrupoclientes(wrapercliente.findGenericValue("AltCli.CodigoGrupoCliente"));
			
			cadcli.setDescontosuframa(wrapercliente.findGenericValue("AltCli.DescontoFiscais.Codigo"));
			String paisbacen = wrapercliente.findGenericValue("AltCli.PaisBacen.cch_codigo");
			cadcli.setCodpaisbacen(paisbacen.trim());
			String segmento1 = wrapercliente.findGenericValue("AltCli.CategoriaTotvs.x5_chave");
			cadcli.setSegmento1(segmento1.trim());
			cadcli.setSegmento2("");
			cadcli.setSegmento3("");
			cadcli.setSegmento4("");
			cadcli.setSegmento5("");
			cadcli.setStatus("1");
			cadcli.setCodvendedor("6001");
			String codmuniciposuframa = wrapercliente.findGenericValue("AltCli.CodigoMunicipioSuframa");
			cadcli.setCodigomunicipiosuframa(codmuniciposuframa.trim());

			cadcli.setContacontabil(wrapercliente.findGenericValue("AltCli.ContaContabil"));
			cadcli.setNaturezafinanceira(wrapercliente.findGenericValue("AltCli.NaturezaFinanceira"));

			List<NeoObject> listaContato = wrapercliente.findGenericValue("AltCli.Telefo");

			List<Contato> contatos = new ArrayList<Contato>();

			for (NeoObject Listaordemcontato : listaContato)
			{

				EntityWrapper contatoWrapper = new EntityWrapper(Listaordemcontato);

				cadcli.setEmail(contatoWrapper.findGenericValue("Telefo.Email"));

				Contato contato = new Contato();

				contato.setNome(contatoWrapper.findGenericValue("Telefo.Contato"));

				cadcli.setDdd(contatoWrapper.findGenericValue("Telefo.DDD"));
				cadcli.setDdi(contatoWrapper.findGenericValue("Telefo.DDI"));
				cadcli.setTelefone(contatoWrapper.findGenericValue("Telefo.Telefone"));

				contato.setDd(contatoWrapper.findGenericValue("Telefo.DDD"));
				contato.setDdi(contatoWrapper.findGenericValue("Telefo.DDI"));
				contato.setTelefone(contatoWrapper.findGenericValue("Telefo.Telefone"));

				cadcli.setEmail(contatoWrapper.findGenericValue("Telefo.Email"));
				contato.setEmail(contatoWrapper.findGenericValue("Telefo.Email"));
				
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