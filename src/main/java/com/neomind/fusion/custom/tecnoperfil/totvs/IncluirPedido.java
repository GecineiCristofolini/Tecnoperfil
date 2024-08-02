package com.neomind.fusion.custom.tecnoperfil.totvs;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.persist.PersistEngine;
import com.neomind.fusion.persist.QLEqualsFilter;
import com.neomind.fusion.workflow.adapter.AdapterUtils;
import com.neomind.fusion.workflow.exception.WorkflowException;

public class IncluirPedido
{

	private static final Log log = LogFactory.getLog(IncluirPedido.class);

	public void IntegracaoIncluirPedido(String json)
	{
		
		try {
			
			ObterToken token = new ObterToken();
			String accessToken = token.obterToken();
			log.debug("Token Obtido : " + accessToken);

			NeoObject endPointURL = PersistEngine.getObject(AdapterUtils.getEntityClass("IntegracaoTOTVS"),
					new QLEqualsFilter("Endpoint", "IncluirPedido"));

			EntityWrapper endPointEW = new EntityWrapper(endPointURL);

			String urlIncluirPedido = endPointEW.findGenericValue("URL");
			
		

			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlIncluirPedido))
					.header("Authorization", "Bearer " + accessToken)
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(json)).build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			log.debug("Status code :" + response.statusCode());

			if (response.statusCode() != 202)
			{

				throw new WorkflowException("Erro na integração" + response.body());
			}
			
			

			
			JSONObject responseBody = new JSONObject(response.body());
			JSONArray errors = responseBody.getJSONArray("errors");
			
			if (errors.length() > 0) {
			    JSONObject firstError = errors.getJSONObject(0);
			    String errorMessage = firstError.getString("message");
			    log.debug("Erro no json de retorno. " + errorMessage );
			    throw new WorkflowException("Erro na integração: " + errorMessage);
			}else
			{
				log.debug("Pedido Incluído com sucesso");
			}
			
			
			
			
		}catch (WorkflowException e)
		{
			e.printStackTrace();
			log.error("Erro ao cadastrar cliente");
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Erro ao cadastrar cliente");
			throw new WorkflowException("Erro ao montar o Json de Inclusão de pedido");
		}

		
	}



}
