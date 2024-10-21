package com.neomind.fusion.custom.tecnoperfil;

import java.math.BigDecimal;

public class DescontoHelper {

	
	@SuppressWarnings("deprecation")
	public static BigDecimal aplicarDesconto(BigDecimal valor, BigDecimal desconto) {
		float valorFloatVal = valor.floatValue();
		float descontoFloatval = (100 - desconto.floatValue()) / 100;
		
		
		
		BigDecimal valorFinal = new BigDecimal(valorFloatVal * descontoFloatval);
		
		return valorFinal.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		
		
	}
	
	
	@SuppressWarnings("deprecation")
	public static BigDecimal aplicarIPI(BigDecimal valor, BigDecimal ipi) {
		float valorFloatVal = valor.floatValue();
		float ipiFloatval = (ipi.floatValue()) / 100;
		
		
		
		BigDecimal valorFinal = new BigDecimal(valorFloatVal * ipiFloatval);
		
		return valorFinal.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		
		
	}
	
	
}
