package com.neomind.fusion.custom.tecnoperfil.workflow.excecoes;

import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;
import com.neomind.fusion.workflow.exception.WorkflowException;

import java.io.*;
import java.time.LocalDateTime;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

public class ExecutaBatchAdapter implements AdapterInterface
{

	public ExecutaBatchAdapter()
	{

	}
	

	public void start(Task origin, EntityWrapper processEntity, Activity activity)
	{
		try
		{
			String retBat = (String) processEntity.findGenericValue("RetBat");
			if (retBat == null || retBat.isEmpty())
			{
				String comando = (String) processEntity.findGenericValue("acao.Bat");
				String url = (String) processEntity.findGenericValue("acao.urlcmd");
				String retorno = executeCmd(comando, url);
				processEntity.setValue("RetBat", retorno);
			}
			Thread.sleep(5000L); 
		}

		catch (Exception e)
		{
			String retorno = "Erro ao executar arquivo";
			processEntity.setValue("RetBat", retorno);
			throw new WorkflowException("Erro ao processar solicita\347\343o");
		}
		
		
	}

	public void back(EntityWrapper entitywrapper, Activity activity1)
	{
	}

	

	private String executeCmd(String command, String url) throws Exception
	{
		HttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url.concat(command));
		HttpResponse response = httpclient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		String result = convertStreamToString(entity.getContent());
		return result;
	}

	public static String convertStreamToString(InputStream is)
	{

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try
		{
			while ((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	public static void main(String args[]) throws Exception
	{

		ExecutaBatchAdapter adapter = new ExecutaBatchAdapter();

		System.out.println(LocalDateTime.now());
		Thread.sleep(0500L);
		
		String result;
		try
		{
			result = adapter.executeCmd("teste.bat",
					"http://192.168.1.100:8282/listener/execute?token=aTer3r:3d&batch=");
			System.out.println(result);
		}
		catch (Exception e)
		{
			result = "Erro";
			System.out.println(result);
		}

	}

}
