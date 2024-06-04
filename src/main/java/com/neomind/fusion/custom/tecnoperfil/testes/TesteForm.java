package com.neomind.fusion.custom.tecnoperfil.testes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;

public class TesteForm    {
	public static void main(String[] args){
	    
    System.out.println("Ola Mundo"); 
    String dataembarque = "20/12/2024";
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); 
	Date dataFormatada;
	try
	{
		dataFormatada = formato.parse(dataembarque);
		
		System.out.println(formato.format(dataFormatada));
		DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		String sdata = "2008-04-01";
		Date data = df2.parse(sdata);
		System.out.println(df1.format(data));
		
		
		
		
	}
	catch (ParseException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
    
    
    
}

	
	
	
}