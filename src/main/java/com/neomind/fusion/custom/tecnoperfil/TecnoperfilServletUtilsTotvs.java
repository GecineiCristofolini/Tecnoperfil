package com.neomind.fusion.custom.tecnoperfil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.MaskFormatter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.neomind.util.NeoUtils;

@WebServlet(name = "TecnoperfilServletUtilsTotvs", urlPatterns = { "/servlet/TecnoperfilServletUtilsTotvs" })
public class TecnoperfilServletUtilsTotvs extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(TecnoperfilServletUtilsTotvs.class);
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException
	{
		this.doGet(req, resp);
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException
	{
		String action = req.getParameter("action");

		if (NeoUtils.safeIsNotNull(new Object[] { action }))
		{
			if (action.equalsIgnoreCase("geraRelatorioPedido"))
				geraRelatorioPedido(req, resp);
			
		}
	}
	
		
	private void geraRelatorioPedido(HttpServletRequest request, HttpServletResponse response)
	{
		String numeroPedido = NeoUtils.safeOutputString(request.getParameter("pedido"));

		try
		{
			if (numeroPedido != null && !numeroPedido.isEmpty()) 
			{
				File fileProposta = TecnoperfilRelatorioPedidoTotvs.geraPDF(numeroPedido);
				
				if(fileProposta == null) {
					System.out.println("Erro ao montar arquivo");
					return;
				}
				
				OutputStream out = response.getOutputStream();
				try
				{
					String sContentType = null;
					sContentType = "application/pdf";
					response.setContentType(sContentType);
					response.addHeader("content-disposition", "attachment; filename=" + fileProposta.getName());
					//response.setCharacterEncoding("ISO-8859-1" );
					InputStream in = null;
					in = new BufferedInputStream(new FileInputStream(fileProposta));
					response.setContentLength((int) in.available());
					int l;
					byte b[] = new byte[1024];
					while ((l = in.read(b, 0, b.length)) != -1)
					{
						out.write(b, 0, l);
					}
					out.flush();
					in.close();
				}
				catch (Exception e)
				{
					System.out.println("Error trying to download file ");
					e.printStackTrace();
				}
				finally
				{
					out.close();
				}
			}
		}
		catch (Exception e)
		{
			log.error("Erro ao imprimir o relatório de posição física financeira!", e);
		}	
	}
	
	/*
	 * se isCPNJ = false, então é mascara de CPF
	 */
	public static String aplicaMascara(String valor, boolean isCNPJ)
	{
		try
		{
			if (valor.contains(".") || valor.contains("/") || valor.contains("-") || valor.equals("")
					|| valor.contains("_"))
				return valor;

			MaskFormatter msk = new MaskFormatter(getCNPJMask());
			if(isCNPJ)
				msk = new MaskFormatter(getCNPJMask());
			else
				msk = new MaskFormatter(getCPFMask());
			msk.setValueContainsLiteralCharacters(false);
			if (msk != null)
			{
				return msk.valueToString(valor);
			}
		}
		catch (Exception e)
		{
			log.error("Erro ao aplicar a máscara de CNPJ no valor!", e);
		}
		return "";
	}
	
	public static String getCNPJMask()
	{
		return "##.###.###/####-##";
	}
	
	public static String getCPFMask()
	{
		return "###.###.###-##";
	}
}
