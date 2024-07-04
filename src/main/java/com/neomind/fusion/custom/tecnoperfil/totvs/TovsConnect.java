package com.neomind.fusion.custom.tecnoperfil.totvs;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;

public class TovsConnect implements AdapterInterface

{

	@Override
	public void start(Task arg0, EntityWrapper wrapercliente, Activity arg2)
	{

		CadCliente cadcli = new CadCliente();
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
		cadcli.setEstado(wrapercliente.findGenericValue("EstadoTotvs.x5_chave"));
		cadcli.setMunicipio(wrapercliente.findGenericValue("MunicipioTovs.cc2_mun"));
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
		cadcli.setIdFusion("123");
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

		boolean enderecoentrega = wrapercliente.findGenericValue("CteloE");

		if (enderecoentrega == false)
		{

			cadcli.setCodigoMunicipioEntrega("");
			cadcli.setUfEntrega("");
			cadcli.setBairroEntrega("");
			cadcli.setEnderecoEntrega("");
			

		}
		else
		{

			cadcli.setCodigoMunicipioEntrega(wrapercliente.findGenericValue("MunicipioDeEntrega.cc2_codmun"));
			cadcli.setUfEntrega(wrapercliente.findGenericValue("UFDeEntrega"));
			cadcli.setBairroEntrega(wrapercliente.findGenericValue("BairroDeEntrega"));
			cadcli.setEnderecoEntrega(wrapercliente.findGenericValue("EnderecoDeEntrega"));

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
		
		List<NeoObject> listaContato = wrapercliente.findGenericValue("Telefo");
		
		List<Contato> contatos = new ArrayList<Contato>();
		
		for (NeoObject Listaordemcontato : listaContato)
		{
      
			EntityWrapper contatoWrapper = new EntityWrapper(Listaordemcontato);
			
			cadcli.setEmail(contatoWrapper.findGenericValue("Telefo.Email"));
			
			Long Tipo = contatoWrapper.findGenericValue("Telefo.TipTelefone");
			
			Contato contato = new Contato();
			
			if (Tipo == 1L) {
				
				cadcli.setDdd(contatoWrapper.findGenericValue("Telefo.DDD"));
				contato.setDd(contatoWrapper.findGenericValue("Telefo.DDD"));
				cadcli.setTelefone(contatoWrapper.findGenericValue("Telefo.Telefone"));
				contato.setTelefone(contatoWrapper.findGenericValue("Telefo.Telefone"));
		        contato.setEmail(contatoWrapper.findGenericValue("Telefo.Email"));
		        contatos.add(contato);
			
			}else {
				
				cadcli.setDdd(contatoWrapper.findGenericValue("Telefo.DDI"));
				contato.setDd(contatoWrapper.findGenericValue("Telefo.DDI"));
				cadcli.setTelefone(contatoWrapper.findGenericValue("Telefo.NumeroInternacional"));
				contato.setTelefone(contatoWrapper.findGenericValue("Telefo.NumeroInternacional"));
		        contato.setEmail(contatoWrapper.findGenericValue("Telefo.Email"));
		        contatos.add(contato);
				
				
			}
				
				
				
			
			
			
		
			
			cadcli.setContatos(contatos);
			
	        
        
			
		}

		
		
		
		
		

		String jsoncli = new Gson().toJson(cadcli);

		System.out.println(jsoncli.toString());

		//		HttpClient httpclient = HttpClients.createDefault();
		//		
		//		String url = "http://viacep.com.br/ws/89224005/json";
		//				
		//		HttpPost httppost = new HttpPost(url.toString());
		//		
		//		HttpResponse response = httpclient.execute(httppost);
		//		
		//		HttpEntity entity = response.getEntity();
		//		String result = Conversor.convertStreamToString(entity.getContent());
		//		JSONObject json = Conversor.stringToJson(result);
		//		

		//System.out.println(json.toJSONString());

	}

	@Override
	public void back(EntityWrapper arg0, Activity arg1)
	{
		// TODO Auto-generated method stub

	}
}