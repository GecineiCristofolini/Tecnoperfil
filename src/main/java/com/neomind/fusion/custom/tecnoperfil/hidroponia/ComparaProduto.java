package com.neomind.fusion.custom.tecnoperfil.hidroponia;

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

public class ComparaProduto implements AdapterInterface
{

	private static final Log log = LogFactory.getLog(ComparaProduto.class);

	@Override
	public void start(Task arg0, EntityWrapper arg1, Activity arg2)
	{

		List<NeoObject> listaProjetos = arg1.findGenericValue("ItensProH");

		for (NeoObject projeto : listaProjetos)
		{

			log.info("Projetos : " + listaProjetos.size());

			EntityWrapper ewProjeto = new EntityWrapper(projeto);
			List<NeoObject> listaItens = ewProjeto.findGenericValue("ItensProHdp");
			//String Gerencia = ewProjeto.findGenericValue("Gerencia");
			String ncm = ewProjeto.findGenericValue("ProdutoBaseBarra.zb4_posipi");

			for (NeoObject item : listaItens)
			{

				log.info("Itens para o projeto : " + listaItens.size());
				EntityWrapper ewItem = new EntityWrapper(item);
				String codigoItem = ewItem.findGenericValue("CodItemP");

				String descricao = ewItem.findGenericValue("descricao");

				if (codigoItem.equals("0"))
				{

					log.info("codigo do Item igual a 0");

					String sql = "SELECT B1_COD, B1_DESC FROM SB1010 WHERE B1_DESC = '" + descricao
							+ "' AND B1_POSIPI = '" + ncm.trim() + "'";

					log.info("Query executada: " + sql);

					Query query = PersistEngine.getEntityManager("Totvs").createNativeQuery(sql);

					try
					{
						//quando retornar mais de um objeto, usar lista e pegar cada elemento da lista
						//List<Object> resultList = query.getResultList();
						//Object[] row = (Object[]) resultList.get(0);
						
						Object result = query.getSingleResult();

						Object[] row = (Object[]) result;

						String codigo = (String) row[0];
						String descricaoItem = (String) row[1];
						
						log.info("Query executada, retornado codigo e descricao "+ codigo + " " + descricaoItem);

						ewItem.findField("CodItemP").setValue(codigo);

					}
					catch (Exception e)
					{

						log.debug("Item não encontrado na base de dados.", e.getCause());
						ewItem.findField("CodItemP").setValue("0");

					}

				}

			}

		}

	}

	@Override
	public void back(EntityWrapper arg0, Activity arg1)
	{
		// TODO Auto-generated method stub

	}

}
