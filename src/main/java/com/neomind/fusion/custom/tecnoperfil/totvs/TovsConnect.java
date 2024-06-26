package com.neomind.fusion.custom.tecnoperfil.totvs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.neomind.fusion.custom.tecnoperfil.bitrix.Conversor;

public class TovsConnect

{

	public static void main(String[] args) throws Exception
	{
		CadCliente cadcli = new CadCliente();

		cadcli.setRazaosocial("Gecinei Cristofolini");
		cadcli.setNomefantasia("Cinei");
		cadcli.setIdade("34 Anos");
		Item it = new Item();
		it.setCodigo("12334");
		it.setVersao("");
		Item it2 = new Item();
		it2.setCodigo("598");
		it2.setVersao("");

		List<Item> listitem = new ArrayList<Item>();
		listitem.add(it);
		listitem.add(it2);

		cadcli.setItem(listitem);

		String jsoncli = new Gson().toJson(cadcli);

		System.out.println(jsoncli.toString());
		
		HttpClient httpclient = HttpClients.createDefault();
		
		String url = "http://viacep.com.br/ws/89224005/json";
				
		HttpPost httppost = new HttpPost(url.toString());
		
		HttpResponse response = httpclient.execute(httppost);
		
		HttpEntity entity = response.getEntity();
		String result = Conversor.convertStreamToString(entity.getContent());
		JSONObject json = Conversor.stringToJson(result);
		

		
		System.out.println(json.toJSONString());

	}
}