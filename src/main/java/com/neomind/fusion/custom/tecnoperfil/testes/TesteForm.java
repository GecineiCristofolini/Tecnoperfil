package com.neomind.fusion.custom.tecnoperfil.testes;

import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;

public class TesteForm implements AdapterInterface   {
	public static void main(String[] args){
	    
    System.out.println("Ola Mundo");   
}

	@Override
	public void back(EntityWrapper arg0, Activity arg1)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(Task arg0, EntityWrapper arg1, Activity arg2)
	{
		// TODO Auto-generated method stub
		
	}

	
}