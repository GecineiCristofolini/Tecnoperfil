package com.neomind.fusion.custom.tecnoperfil.testes;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.simple.JSONObject;

import com.neomind.fusion.custom.tecnoperfil.totvs.entidades.CadCliente;
import com.neomind.fusion.custom.tecnoperfil.totvs.entidades.Contato;

public class jason
{
	

	
	public static void main(String[] args) throws JSONException {
         
		CadCliente cadcli = new CadCliente();
		
		List<String> lista = new ArrayList<String>();
		
		String Listaemail = null;
		for( int i = 0; i< 10 ; i++) {
           
			if (Listaemail == null) {
				
				Listaemail = "gecinei.cristofolini@tecnoperfil.com.br"+i;
			}else {
			
			Listaemail += ";gecinei.cristofolini@tecnoperfil.com.br"+i;

		}
		}
		
		String Emaildanfe = lista.toString();
		System.out.print(Listaemail);
		
//     
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
//		
//		JSONObject my_obj = new JSONObject();
//		
//		my_obj.put("Codigo",it.getCodigo());
//		my_obj.put("versao",it.getVersao());
//		my_obj.put("Codigo",it2.getCodigo());
//		my_obj.put("versao",it2.getVersao());
//		my_obj.put("idade", cadcli.getIdade());
//		my_obj.put("fantasia", cadcli.getNomefantasia());
//		my_obj.put("nomeempresa", cadcli.getRazaosocial());
//		
//		
		
	
		
		//System.out.println(my_obj.toJSONString());
				
		
		
	}
}
