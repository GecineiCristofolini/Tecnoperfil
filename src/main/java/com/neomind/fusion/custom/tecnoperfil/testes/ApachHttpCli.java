package com.neomind.fusion.custom.tecnoperfil.testes;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.neomind.fusion.custom.tecnoperfil.totvs.entidades.CadCliente;
import com.neomind.fusion.custom.tecnoperfil.totvs.entidades.Contato;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApachHttpCli
{

	private final CloseableHttpClient httpClient = HttpClients.createDefault();

	public static void main(String[] args) throws Exception
	{

		ApachHttpCli obj = new ApachHttpCli();

		try
		{
			System.out.println("Testing 1 - Send Http GET request");
			//obj.sendGet();

			//System.out.println("Testing 2 - Send Http POST request");
			obj.sendPost();

		}
		finally
		{
			obj.close();
		}
	}

	private void close() throws IOException
	{
		httpClient.close();
	}

	private void sendGet() throws Exception
	{

		HttpGet request = new HttpGet("https://publica.cnpj.ws/cnpj/00341857000175");
		//HttpGet request = new HttpGet("http://viacep.com.br/ws/89224005/json");

		// add request headers
		//request.addHeader("custom-key", "mkyong");
		//request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

		try (CloseableHttpResponse response = httpClient.execute(request))
		{

			// Get HttpResponse Status
			System.out.println(response.getStatusLine().toString());

			HttpEntity entity = response.getEntity();
			Header headers = entity.getContentType();
			System.out.println(headers);

			if (entity != null)
			{
				// return it as a String
				String result = EntityUtils.toString(entity);
				System.out.println(result);
				Map<String, String> mapa = new HashMap<>();
				Matcher matcher = Pattern.compile(":").matcher(result);
				while (matcher.find())
				{
					String[] group = matcher.group().split(":");

					mapa.put(group[0].replaceAll("\"", "").trim(), group[1].replaceAll("\"", "").trim());
					System.out.println(group[0]+ group[1] );
				}
				
			}
		}

	}
	
	private void sendPost() throws Exception {
		
		CadCliente cadcli = new CadCliente();

//		cadcli.setRazaosocial("Gecinei Cristofolini");
//		cadcli.setNomefantasia("Cinei");
//		cadcli.setIdade("34 Anos");
//		Contato it = new Contato();
//		it.setCodigo("12334");
//		it.setVersao("");
//		Contato it2 = new Contato();
//		it2.setCodigo("598");
//		it2.setVersao("");
//
//		List<Contato> listitem = new ArrayList<Contato>();
//		listitem.add(it);
//		listitem.add(it2);
//
//		cadcli.setItem(listitem);
//		

		String jsoncli = new Gson().toJson(cadcli);
		
		List<NameValuePair> urlParameters = new ArrayList<>();
		urlParameters.add(new BasicNameValuePair("username", "abc"));
        urlParameters.add(new BasicNameValuePair("password", "123"));
        urlParameters.add(new BasicNameValuePair("custom", "secret"));
        
        
        
       // System.out.println(urlParameters.toString());

        HttpPost post = new HttpPost("https://httpbin.org/post");
        
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(post)) {

               System.out.println(EntityUtils.toString(response.getEntity()));
           }
        
       // StringEntity entity = new StringEntity(jsoncli);
       // post.setEntity(entity);
        //post.setHeader("Accept", "application/json");
       // post.setHeader("Content-type", "application/json");
        
        System.out.println( EntityUtils.toString(post.getEntity()));

//       

       // try (CloseableHttpClient httpClient = HttpClients.createDefault();
          //   CloseableHttpResponse response = httpClient.execute(post)) {

           // System.out.println(EntityUtils.toString(response.getEntity()));
       // }

    }


}
