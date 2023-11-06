package com.neomind.fusion.custom.tecnoperfil.apis;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.neomind.fusion.eform.EFormField;

import com.neomind.fusion.eform.converter.OriginEnum;
import com.neomind.fusion.eform.converter.StringConverter;
// classe ApiCnpj ele vai extender de uma classe fusion para podermos incluir o botão 
public class ApiCnpj extends StringConverter
{
	@Override
	protected String getHTMLView(EFormField field, OriginEnum origin)
	{

		return getBotao(field, origin);
	}

	private String getBotao(EFormField field, OriginEnum origin)
	{
		
        // vai cria um Script para o JSP buscarcnpj que fica dentro do pacote war da fusion
		StringBuilder sb = new StringBuilder();

		sb.append(
				"<a class='btn edit_buttons' style='color: rgb(90, 112, 137); padding: 3.5px 9px; margin-left: 4px; border: 1px solid rgb(204, 204, 204); background: rgb(255, 255, 255); text-align: center; font-weight: 700; font-size: 11px; cursor: pointer; line-height: normal;' onClick='processaBuscaCNPJ()'>Buscar CNPJ</a>");

		sb.append("				<script>" + " function processaBuscaCNPJ(){"

				+ "                     const cnpj = $(\"#var_InfoCNPJ__\").val();"
				+ "						const cadId = $(\"#hid_root\").val();"
				+ "						 $('input[name=\"action.send\"]').length ==1? $('input[name=\"action.save\"]').click() : $('input[name=\"action.apply\"]').click();"
				+ "						if(cadId && cnpj ){"
				+ "							fetch(`custom/apicepcnpj/buscacnpj.jsp?cadId=${cadId}&cnpj=${cnpj}`).then(function(response) {"
				+ "							  if(response.ok) {"
				+ "								$('input[name=\"action.send\"]').length ==1? $('input[name=\"action.save\"]').click() : $('input[name=\"action.apply\"]').click();"
				+ "							  } else {"
				+ "							    console.log('Network response was not ok.');"
				+ "							  }" + "							})"
				+ "							.catch(function(error) {"
				+ "							  console.log('There has been a problem with your fetch operation: ' + error.message);"
				+ "							});" + "						}" + "					}"
				+ "				</script>");
		return sb.toString();

	}

	@Override
	protected String getHTMLInput(EFormField field, OriginEnum origin)
	{

		return getBotao(field, origin);
	}

	// Realiza uma Conexao via api rest json Com o Site cnpjws este metodo vai ser buscado pelo jsp

	public static String buscarCnpj(String cnpj)
	{
		String json = null;

		try
		{

			URL url = new URL("https://publica.cnpj.ws/cnpj/" + cnpj);

			URLConnection urlConnection = url.openConnection();
			InputStream is = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			StringBuilder jsonSb = new StringBuilder();

			br.lines().forEach(l -> jsonSb.append(l.trim()));

			json = jsonSb.toString();

		}
		catch (Exception e)

		{

			System.out.println("CNPJ INVALIDO");

		}

		return json;

	}
	// realiza conexao com webservice viacep que o jsp vai chamar este metodo
	public static String buscarCep(String cep) {
		String json = null;

		try {
			URL url = new URL("http://viacep.com.br/ws/" + cep + "/json");
			URLConnection urlConnection = url.openConnection();
			InputStream is = urlConnection.getInputStream();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			StringBuilder jsonSb = new StringBuilder();

			br.lines().forEach(l -> jsonSb.append(l.trim()));

			json = jsonSb.toString();
			
			

		} catch (Exception e) {
			
			System.out.println("CEP INVALIDO ");
			
			
		}

		return json;
	}
	
	

}
