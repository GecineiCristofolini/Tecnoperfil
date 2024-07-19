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
		try {
			log.debug("Iniciar busca de info");		
			String json = buscarInformacoes(wrapercliente);
			log.debug("informacoes retornadas + " + json);	
			CadastrarCliente cadCliente = new CadastrarCliente();
			log.debug("Iniciando cadastro do cliente");
			cadCliente.cadastroDeCliente(json);
		
		}catch (WorkflowException e) {
            e.printStackTrace();
            log.error("Erro ao cadastrar cliente", e);
            throw e;
        } catch (Exception e) {
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
	
	
	public String buscarInformacoes(EntityWrapper wrapercliente ) {
		
		
		   try {
	    	   
	    	   
	    	   String codcliente = wrapercliente.findGenericValue("CodCli");
	    	   
	    	   if (codcliente == null) {
	    	   
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
			String inscriestadual = wrapercliente.findGenericValue("InsEst");
			if (inscriestadual == null)
			{
				inscriestadual = "ISENTO";
			}
			cadcli.setInscricaoEstadual(inscriestadual);
			cadcli.setInscricaoMunicipal(inscriestadual);
			cadcli.setCgc(wrapercliente.findGenericValue("CGC"));
			String estado = wrapercliente.findGenericValue(("EstadoTotvs.x5_chave"));
			cadcli.setEstado(estado.trim());
			cadcli.setBairro(wrapercliente.findGenericValue("Bairro"));
			String nomemunicipio = wrapercliente.findGenericValue(("MunicipioTovs.cc2_mun"));
			cadcli.setMunicipio(nomemunicipio.trim());
			cadcli.setCodigoMunicipio(wrapercliente.findGenericValue("MunicipioTovs.cc2_codmun"));
			cadcli.setComplementoEndereco(wrapercliente.findGenericValue("Comple"));
			cadcli.setEndereco(wrapercliente.findGenericValue("EnderecoCompleto"));
			if (cadcli.getEstado() == "EX")
			{

				cadcli.setCep("99999999");

			}
			else
			{

				cadcli.setCep(wrapercliente.findGenericValue("CEP"));

			}

			cadcli.setCep(wrapercliente.findGenericValue("CEP"));

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
			cadcli.setPais(wrapercliente.findGenericValue("PaisTovs.ya_codgi"));
			cadcli.setTipoCliente(wrapercliente.findGenericValue("TipoCliente.Tipo"));
			cadcli.setCodigoSuframa(wrapercliente.findGenericValue("Sufram"));
			cadcli.setTabeladePreco("");

			boolean Clintetransp = wrapercliente.findGenericValue("TraEspe");

			if (Clintetransp == false)
			{

				cadcli.setTransportadora("");

			}
			else
			{

				cadcli.setTransportadora(wrapercliente.findGenericValue("TransportadoraTOTVS.a4_cod"));

			}
			
			boolean Condpag = wrapercliente.findGenericValue("CodEspe");

			if (Condpag == false)
			{

				cadcli.setCodicaoPagamento("");

			}
			else
			{

				cadcli.setCodicaoPagamento(wrapercliente.findGenericValue("CondicaoDePagamentoTotvs.e4_codigo"));

			}


			boolean enderecoentrega = wrapercliente.findGenericValue("CteloE");

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

				cadcli.setCodigoMunicipioEntrega(wrapercliente.findGenericValue("MunicipioDeEntrega.cc2_codmun"));
				cadcli.setUfEntrega(wrapercliente.findGenericValue("UFDeEntrega"));
				cadcli.setBairroEntrega(wrapercliente.findGenericValue("BairroDeEntrega"));
				cadcli.setEnderecoEntrega(wrapercliente.findGenericValue("EnderecoDeEntrega"));
				cadcli.setCepentrega(wrapercliente.findGenericValue("CepDeEntrega"));
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
			cadcli.setDocumentoEstrageiro(wrapercliente.findGenericValue("documentoEstrageiro"));
			
			cadcli.setOptanteSimplesNacional(wrapercliente.findGenericValue("OptanteDoSimplesNacional.Codigo"));
			cadcli.setGrupoclientes(wrapercliente.findGenericValue("GrupoClienteFiscal.x5_chave"));
			cadcli.setDescontosuframa(wrapercliente.findGenericValue("DescontoFiscais.Codigo"));
			cadcli.setCodpaisbacen(wrapercliente.findGenericValue("PaisBacen.cch_codigo"));
			cadcli.setSegmento1(wrapercliente.findGenericValue("CategoriaTotvs.x5_chave"));
			cadcli.setSegmento2("");
			cadcli.setSegmento3("");
			cadcli.setSegmento4("");
			cadcli.setSegmento5("");
			cadcli.setStatus("1");
			cadcli.setCodvendedor("");
			cadcli.setCodigomunicipiosuframa(wrapercliente.findGenericValue("MunicipiSuframa.x5_chave"));
			
			cadcli.setContacontabil(wrapercliente.findGenericValue("ContaContabil"));
			cadcli.setNaturezafinanceira(wrapercliente.findGenericValue("NaturezaFinanceira"));
			
			
			
			List<NeoObject> listaContato = wrapercliente.findGenericValue("Telefo");
			
			List<Contato> contatos = new ArrayList<Contato>();
			
			for (NeoObject Listaordemcontato : listaContato)
			{
	      
				EntityWrapper contatoWrapper = new EntityWrapper(Listaordemcontato);
				
				cadcli.setEmail(contatoWrapper.findGenericValue("Telefo.Email"));
				
				int Tipo = contatoWrapper.findGenericValue("Telefo.TipTelefone");
				
				
				
				Contato contato = new Contato();
				
				if (Tipo == 1) {
					
					
					
					contato.setNome(contatoWrapper.findGenericValue("Telefo.Contato"));
					
					cadcli.setDdi("");
					contato.setDdi("");
					cadcli.setDdd(contatoWrapper.findGenericValue("Telefo.DDD"));
					contato.setDd(contatoWrapper.findGenericValue("Telefo.DDD"));
					
					cadcli.setTelefoneinternacional("");
					contato.setTelefoneinternacional("");
					cadcli.setTelefone(contatoWrapper.findGenericValue("Telefo.Telefone"));
					contato.setTelefone(contatoWrapper.findGenericValue("Telefo.Telefone"));
					
			        contato.setEmail(contatoWrapper.findGenericValue("Telefo.Email"));
			        contatos.add(contato);
				
				}else {
					
					contato.setNome(contatoWrapper.findGenericValue("Telefo.Contato"));
					
					cadcli.setDdd("");
					contato.setDd("");
					cadcli.setDdi(contatoWrapper.findGenericValue("Telefo.DDI"));
					contato.setDd(contatoWrapper.findGenericValue("Telefo.DDI"));
					
					cadcli.setTelefone("");
					contato.setTelefone("");
					cadcli.setTelefoneinternacional(contatoWrapper.findGenericValue("Telefo.NumeroInternacional"));
					contato.setTelefoneinternacional(contatoWrapper.findGenericValue("Telefo.NumeroInternacional"));
					
			        contato.setEmail(contatoWrapper.findGenericValue("Telefo.Email"));
			        contatos.add(contato);
					
					
				}
					
					
					
				
				
			}
			
				
				cadcli.setContatos(contatos);
				
		        
	        
		
			
			
			
			

			String jsoncli = new Gson().toJson(cadcli);
			
			System.out.println(jsoncli.toString());
			return jsoncli;

			
			
			

	       }catch (Exception e) {
	    	   
	    	   log.error("Erro ao cadastrar cliente : " + e.getCause());
	    	   e.printStackTrace();
	    	   throw new WorkflowException("Erro ao cadastrar cliente" + e.getCause());
	    	   
		}
			
	}
	
}