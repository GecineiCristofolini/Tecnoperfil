package com.neomind.fusion.custom.tecnoperfil.apis;

// este adaptar no Worflow de Alteração de Cadastro 

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.neomind.fusion.eform.EFormField;

import com.neomind.fusion.eform.converter.OriginEnum;
import com.neomind.fusion.eform.converter.StringConverter;
//classe ApiVIAcep ele vai extender de uma classe fusion para podermos incluir o botão 
public class ApiViaCepAlt extends StringConverter
{
	@Override
	protected String getHTMLView(EFormField field, OriginEnum origin)
	{

		return getBotao(field, origin);
	}
	


	private String getBotao(EFormField field, OriginEnum origin)
	{
		
		  // vai cria um Script para o JSP buscarcep que fica dentro do pacote war da fusion
		StringBuilder sb = new StringBuilder();

		sb.append(
				"<a class='btn edit_buttons' style='color: rgb(90, 112, 137); padding: 3.5px 9px; margin-left: 4px; border: 1px solid rgb(204, 204, 204); background: rgb(255, 255, 255); text-align: center; font-weight: 700; font-size: 11px; cursor: pointer; line-height: normal;' onClick='processaBuscaCep()'>Buscar CEP</a>");
				

		sb.append("				<script>" + " function processaBuscaCep(){"

				
				+ "                     const cepcadalt = $(\"#var_AltCli__InfCep__\").val();"
				+ "						const cadId =" + field.getForm().getObject().getNeoId() + ";"
				+ "						$('input[name=\"action.save\"]').click();"
				+ "						if(cadId){"
			    + "							fetch(`custom/apicepcnpj/buscacepalt.jsp?cadId=${cadId}&cepcadalt=${cepcadalt}`).then(function(response) {"
				+ "							  if(response.ok) {"
				+ "                               console.log('Network response  ok');"
				+ "							  } else {"
				+ "							    console.log('Network response was not ok.');"
				+ "							  }" + "							})"
				+ "							.catch(function(error) {"
				+ "							  console.log('There has been a problem with your fetch operation: ' + error.message);"
				+ "							});" + "						}" + "					}"
				+ "				</script>");
		
		
		
		return sb.toString();
      
	}
	
	


	// Realiza uma Conexao via api do site Viacep este metodo que vai acessar o jsp
	
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
