package com.neomind.fusion.custom.tecnoperfil.testes;

import java.util.ArrayList;
import java.util.List;

import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;

public class analisecliteste implements AdapterInterface {

	@Override
	public void back(EntityWrapper arg0, Activity arg1)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(Task arg0, EntityWrapper arg1, Activity arg2)
	{
		
		
		String ordemcompra = arg1.findGenericValue("InformeAOrdemDeCompra");
		String localentrega = arg1.findGenericValue("LocalDeEntrega");
		StringBuilder mensagem = new StringBuilder();
		mensagem.append(ordemcompra);
		mensagem.append("\n");
		mensagem.append(localentrega);
		mensagem.append("\n");
		
		arg1.setValue("MensagemNota", mensagem.toString());
		
	}
	
	
}

