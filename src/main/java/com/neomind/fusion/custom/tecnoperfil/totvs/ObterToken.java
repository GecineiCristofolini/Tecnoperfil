package com.neomind.fusion.custom.tecnoperfil.totvs;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.persist.PersistEngine;
import com.neomind.fusion.workflow.adapter.AdapterUtils;

public class ObterToken
{
	
	private static final Log log = LogFactory.getLog(ObterToken.class);

	public String obterToken()
	{

		try
		{
			
			log.debug("Obtendo token oauth2/v1/token");
			String baseUrl = "https://tecnoperfil169383.protheus.cloudtotvs.com.br:1623/rest/api/oauth2/v1/token";
			String grantType = "password";
			String password = "F5l9jvKHD89j0ZEMVSiACL";
			String username = "fusion";

			String url = String.format("%s?grant_type=%s&password=%s&username=%s", baseUrl, grantType,
					password, username);

			HttpClient client = HttpClient.newHttpClient();

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.header("Content-Type", "application/x-www-form-urlencoded")
					.POST(HttpRequest.BodyPublishers.noBody()).build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			JSONObject jsonResponse = new JSONObject(response.body());
			log.debug("status code token request : " + response.statusCode());
			String accessToken = jsonResponse.getString("access_token");

			List<NeoObject> listaformToken = PersistEngine
					.getObjects(AdapterUtils.getEntityClass("CadastroDeClientesParametros"));

			for (NeoObject token : listaformToken)
			{

				EntityWrapper EWToken = new EntityWrapper(token);
				String tokenSalvo = EWToken.findGenericValue("AccesstokenTotvs");

				if (tokenSalvo != null || tokenSalvo != accessToken)
				{
					EWToken.findField("AccesstokenTotvs").setValue(accessToken);
				}

			}

			return accessToken;

		}
		catch (Exception e)
		{

			e.printStackTrace();
			new Exception(e);

		}
		return null;

	}

	public String RefreshToken()
	{

		try
		{
			log.debug("Obtendo  refresh token");
			List<NeoObject> listaformToken = PersistEngine
					.getObjects(AdapterUtils.getEntityClass("CadastroDeClientesParametros"));
			String baseUrl = "https://localhost:8080/rest/api/oauth2/v1/token";
			String grantType = "refresh_token";
			String refreshToken = "";
			for (NeoObject token : listaformToken)
			{

				EntityWrapper ewToken = new EntityWrapper(token);
				refreshToken = ewToken.findGenericValue("AccesstokenTotvs");

			}

			String url = baseUrl + "?grant_type=" + grantType + "&refresh_token=" + refreshToken;

			HttpClient client = HttpClient.newHttpClient();

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.header("Content-Type", "application/x-www-form-urlencoded")
					.POST(HttpRequest.BodyPublishers.noBody()).build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			JSONObject jsonResponse = new JSONObject(response.body());
			log.debug("status code token request : " + response.statusCode());
			String accessToken = jsonResponse.getString("access_token");
			
			for (NeoObject token : listaformToken)
			{

				EntityWrapper EWToken = new EntityWrapper(token);
				String tokenSalvo = EWToken.findGenericValue("AccesstokenTotvs");

				if (tokenSalvo != null || tokenSalvo != accessToken)
				{
					EWToken.findField("AccesstokenTotvs").setValue(accessToken);
					return accessToken;
				}

			}
			
			
			

		}
		catch (Exception e)
		{

			e.printStackTrace();
			e.getCause();
		}
		return null;

	}

}
