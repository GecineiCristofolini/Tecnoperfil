package com.neomind.fusion.custom.tecnoperfil.exportacao;

import com.neomind.framework.base.entity.NeoBaseEntity;
import com.neomind.fusion.eform.EFormField;
import com.neomind.fusion.eform.converter.OriginEnum;
import com.neomind.fusion.eform.converter.StringConverter;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.portal.PortalUtil;

public class BotaoImprimirDeclaracaoProdutor extends StringConverter {
	@Override
	protected String getHTMLView(EFormField field, OriginEnum origin) {

		return getBotao(field, origin);
	}

	private String getBotao(EFormField field, OriginEnum origin) {
		System.out.println("NeoID form "
				+ field.getForm().getObject().getNeoId());
		EntityWrapper ew = new EntityWrapper((NeoBaseEntity<Long>)field.getForm().getObject());
		
		String pedido = ew.findGenericValue("NumPed");
		System.out.println("numero pedido" + pedido);
		String url = PortalUtil.getBaseURL();
		String botao = "<script>" 
				
				+ "function opnreportDocumento(){"
				+ "var url = '"
				+ url
				+ "servlet/ExportacaoServeletUtilsDocumentoProdutor?action=geraRelatorioPedido&pedido="
				+ field.getForm().getObject().getNeoId()
				+ "';"
				+ "window,open(url);"
				+ "} </script> "
				+ "<input type='button' class='input_button' value='Exportar Invoice' onclick='opnreportDocumento()'>";
		return botao;
	}

	@Override
	protected String getHTMLInput(EFormField field, OriginEnum origin) {

		return getBotao(field, origin);
	}

}
