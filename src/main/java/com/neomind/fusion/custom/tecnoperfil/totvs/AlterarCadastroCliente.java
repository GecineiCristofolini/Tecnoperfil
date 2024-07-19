package com.neomind.fusion.custom.tecnoperfil.totvs;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.persist.PersistEngine;
import com.neomind.fusion.workflow.adapter.AdapterUtils;

public class AlterarCadastroCliente
{

	public void alterarCadastroCliente()
	{
		try
		{
			ObterToken obterToken = new ObterToken();
			String token = obterToken.obterToken();
			
			//EntityWrapper ew = new EntityWrapper();
			
			String id = "teste";//ew.findGenericValue("id");
			
			String url = "https://tecnoperfil169383.protheus.cloudtotvs.com.br:1623/rest/fusion/v1/cliente/" + id;
			
			
			
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.header("Authorization", "Bearer " + token)
					.header("Content-Type", "application/json")
					.PUT(HttpRequest.BodyPublishers.noBody()).build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		}
		catch (Exception e)
		{

		}

	}

}
