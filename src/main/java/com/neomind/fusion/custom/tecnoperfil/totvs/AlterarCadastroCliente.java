package com.neomind.fusion.custom.tecnoperfil.totvs;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.persist.PersistEngine;
import com.neomind.fusion.persist.QLEqualsFilter;
import com.neomind.fusion.workflow.adapter.AdapterUtils;
import com.neomind.fusion.workflow.exception.WorkflowException;

public class AlterarCadastroCliente
{
	private static final Log log = LogFactory.getLog(AlterarCadastroCliente.class);
	public void alterarCadastroCliente(String json,String idcliente)
	{
		try
		{
			ObterToken token = new ObterToken();
			String accessToken = token.obterToken();
			log.debug("Token Obtido : " + accessToken);	
			
			NeoObject endPointURL = PersistEngine
					.getObject(AdapterUtils.getEntityClass("IntegracaoTOTVS"), new QLEqualsFilter("Endpoint", "CadastrarCliente"));
			
			EntityWrapper endPointEW = new EntityWrapper(endPointURL);
			
			String urlCadastrarCliente = endPointEW.findGenericValue("URL");
						
			String url = urlCadastrarCliente + idcliente;
			
			
			
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.header("Authorization", "Bearer " + accessToken)
					.header("Content-Type", "application/json")
					.PUT(HttpRequest.BodyPublishers.ofString(json)).build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			log.debug("Status code :" + response.statusCode());

			if (response.statusCode() != 200)
			{

				throw new WorkflowException("Erro na integração" + response.body());
			}

			String responseBody = response.body();
			log.debug("responde body request: " + responseBody);

			if (response.statusCode() == 401)
			{

				log.debug("Token expirado! atualizando token");
				String refreshToken = token.RefreshToken();
				HttpRequest requestRefreshed = HttpRequest.newBuilder().uri(URI.create(url))
						.header("Authorization", "Bearer " + refreshToken)
						.header("Content-Type", "application/json")
						.POST(HttpRequest.BodyPublishers.ofString(json)).build();

				HttpResponse<String> responseRefresh = client.send(requestRefreshed,
						HttpResponse.BodyHandlers.ofString());

				log.debug("status code token refresh: " + response.statusCode());

				String responseBodyRefresh = responseRefresh.body();

				if (responseRefresh.statusCode() != 200)
				{

					throw new WorkflowException("Erro na integração" + responseBodyRefresh);
				}

			}
		}
		catch (Exception e)
		{

		}

	}

}
