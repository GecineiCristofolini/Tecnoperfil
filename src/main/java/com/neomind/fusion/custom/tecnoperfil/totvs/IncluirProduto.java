package com.neomind.fusion.custom.tecnoperfil.totvs;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.persist.PersistEngine;
import com.neomind.fusion.persist.QLEqualsFilter;
import com.neomind.fusion.workflow.adapter.AdapterUtils;
import com.neomind.fusion.workflow.exception.WorkflowException;

public class IncluirProduto
{

	private static final Log log = LogFactory.getLog(IncluirProduto.class);

	public String IntegracaoIncluirProduto(String json)
	{

		try
		{
            
			ObterToken token = new ObterToken();
			String accessToken = token.obterToken();
			log.debug("Token Obtido : " + accessToken);

			NeoObject endPointURL = PersistEngine.getObject(
					AdapterUtils.getEntityClass("IntegracaoTOTVS"),
					new QLEqualsFilter("Endpoint", "IncluirProduto"));

			EntityWrapper endPointEW = new EntityWrapper(endPointURL);

			String urlIncluirPedido = endPointEW.findGenericValue("URL");

			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlIncluirPedido))
					.header("Authorization", "Bearer " + accessToken)
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(json)).build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			System.out.println(response.body());

			log.debug("Status code :" + response.statusCode());
			String codigo ="0";
			
			if (response.statusCode() != 202)
			{

				log.error("Erro ao cadastrar Produto");
				throw new WorkflowException("Erro na integração" + response.body());
				
			}else {		
		
			
			JSONObject responseBody = new JSONObject(response.body());
			codigo = responseBody.getString("id");
			
			}
			
//			String codigoitem = ewProjeto.findGenericValue("CodigoDoItem");
//			System.out.println(codigoitem +"enc");

			
			//codigoitem = codigo;
			
			//ewProjeto.setValue("CodigoDoItem",codigo);
			
			
			
			log.debug("Produto Incluído com sucesso");
			
			return codigo;

		}
		catch (WorkflowException e)
		{
			e.printStackTrace();
			log.error("Erro ao cadastrar Produto");
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Erro ao cadastrar Produto");
			throw new WorkflowException("Erro ao montar o Json de Inclusão de produto");
		}

	}

}
