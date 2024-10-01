package com.neomind.fusion.custom.tecnoperfil.testes;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

public class AnaliseFinanceira
{
	private static final Log log = LogFactory.getLog(AnaliseFinanceira.class);
	
	public void AnaliseFi(String idcliente)
	{
		try
		{
			ObterToken token = new ObterToken();
			String accessToken = token.obterToken();
			log.debug("Token Obtido : " + accessToken);	
			
			String urlCadastrarCliente = "https://tecnoperfil169383.protheus.cloudtotvs.com.br:1623/rest/api/fat/v1/CustomerCreditLimit/%20%20%20%2096738935";
						
			String url = urlCadastrarCliente + idcliente;
			System.out.println(accessToken);
			
			final String URL_GET = urlCadastrarCliente;
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
					  .header("Authorization", "Bearer " + accessToken)
					  .header("Content-Type", "application/json")
			          .GET()
			          .timeout(Duration.ofSeconds(10))
			          .uri(URI.create(URL_GET))
			          .build();
			
			System.out.print("tete");
//			
//			HttpClient clientw = HttpClient.newHttpClient();
//			System.out.println(url);
//
//			         HttpRequest request = HttpRequest.newBuilder()
//					  .header("Authorization", "Bearer " + accessToken)
//					  .header("Content-Type", "application/json")
//			          .GET()
//			          .timeout(Duration.ofSeconds(10))
//			          .uri(URI.create(url))
//			          .build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
     		JSONObject jsonResponse = new JSONObject(response.body());
     		System.out.println("Response status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            
            System.out.println(jsonResponse);
            
//			System.out.println("erroooo");
//			
//			log.debug("Status code :" + response.statusCode());
//
//			if (response.statusCode() != 200)
//			{
//
//				throw new WorkflowException("Erro na integração" + response.body());
//			}
//
//			String responseBody = response.body();
//			log.debug("responde body request: " + responseBody);
//
//			if (response.statusCode() == 401)
//			{
//
//				log.debug("Token expirado! atualizando token");
//				String refreshToken = token.RefreshToken();
//				HttpRequest requestRefreshed = HttpRequest.newBuilder().uri(URI.create(url))
//						.header("Authorization", "Bearer " + refreshToken)
//						.header("Content-Type", "application/json")
//						.GET().build();
//
//				HttpResponse<String> responseRefresh = client.send(requestRefreshed,
//						HttpResponse.BodyHandlers.ofString());
//
//				log.debug("status code token refresh: " + response.statusCode());
//
//				String responseBodyRefresh = responseRefresh.body();
//
//				if (responseRefresh.statusCode() != 200)
//				{
//
//					throw new WorkflowException("Erro na integração" + responseBodyRefresh);
//				}
//
//			}
	}
		catch (Exception e)
		{

		}

	}

}
