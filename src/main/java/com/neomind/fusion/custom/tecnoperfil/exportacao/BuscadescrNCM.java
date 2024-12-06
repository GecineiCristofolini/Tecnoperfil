package com.neomind.fusion.custom.tecnoperfil.exportacao;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.persist.PersistEngine;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;

//busca descrição do ncm 

public class BuscadescrNCM implements AdapterInterface
{

	
	private static final Log log = LogFactory.getLog(BuscadescrNCM.class);
	
	public void start(Task arg0, EntityWrapper PedidoEW, Activity arg2)
	{

		    

			List<NeoObject> itensfusion = PedidoEW.findGenericValue("AdicionaPedido");
			
			for (NeoObject itensPedido : itensfusion)
			{

				
				EntityWrapper itensPedidoWrapper = new EntityWrapper(itensPedido);
				log.info("Itens para o projeto : " +  itensfusion.size());
				String ncm = itensPedidoWrapper.findGenericValue("NCM");
				String descrncm = itensPedidoWrapper.findGenericValue("DescricaoNcm");
				
				
//				String sql = "SELECT YD_DESC_P FROM SYD010  WHERE YD_TEC = '" + ncm.trim()
//				+ "'";
				
				String sql = "SELECT YD_DESC_P FROM SYD010 GROUP BY YD_DESC_P,D_E_L_E_T_ HAVING (((Max(YD_TEC))='" + ncm.trim()
			    + "') AND((D_E_L_E_T_)<>'*'))";
				
				
				
				
				log.info("Query executada: " + sql);
				
				Query query = PersistEngine.getEntityManager("Totvs").createNativeQuery(sql);
				
				try
				{
					//quando retornar mais de um objeto, usar lista e pegar cada elemento da lista
					//List<Object> resultList = query.getResultList();
					//Object[] row = (Object[]) resultList.get(0);
					//Object[] row = (Object[]) result;

					
					Object result = query.getSingleResult();

					
					descrncm = result.toString();					
					
					itensPedidoWrapper.setValue("DescricaoNcm", descrncm.toUpperCase());
					
					log.info("Query executada, retornado codigo e descricao "+ descrncm );

					

				}
				catch (Exception e)
				{

					log.debug("Item não encontrado na base de dados.", e.getCause());
					

				}

			
				
				
				
				
			}	
				
				

				

			
	}

	@Override
	public void back(EntityWrapper arg0, Activity arg1)
	{
		// TODO Auto-generated method stub

	}

}
