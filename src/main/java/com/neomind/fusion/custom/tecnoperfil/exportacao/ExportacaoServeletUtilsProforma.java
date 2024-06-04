package com.neomind.fusion.custom.tecnoperfil.exportacao;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.neomind.util.NeoUtils;

@WebServlet(name = "ExportacaoServeletUtilsProforma", urlPatterns = { "/servlet/ExportacaoServeletUtilsProforma" })
public class ExportacaoServeletUtilsProforma extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(ExportacaoServeletUtilsProforma.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		this.doGet(req, resp);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		String action = req.getParameter("action");

		if (NeoUtils.safeIsNotNull(new Object[] { action }))
		{
			if (action.equalsIgnoreCase("geraRelatorioProforma"))
				geraRelatorioProforma(req, resp);

		}
	}

	
	//metodo para Gerar Proformula
	@SuppressWarnings("unused")
	private void geraRelatorioProforma(HttpServletRequest request, HttpServletResponse response)
	{
		String numeroPedido = NeoUtils.safeOutputString(request.getParameter("pedido"));

		try
		{
			if (numeroPedido != null && !numeroPedido.isEmpty())
			{
				File fileProposta = TecnoperfilRelatorioProforma.geraPDF(numeroPedido);

				if (fileProposta == null)
				{
					System.out.println("Erro ao montar arquivo");
					return;
				}

				OutputStream out = response.getOutputStream();
				try
				{
					String sContentType = null;
					sContentType = "application/pdf";
					response.setContentType(sContentType);
					response.addHeader("content-disposition",
							"attachment; filename=" + fileProposta.getName());
					//response.setCharacterEncoding("ISO-8859-1" );
					InputStream in = null;
					in = new BufferedInputStream(new FileInputStream(fileProposta));
					if (in != null)
					{
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
					else
					{
						System.out.println("Trying to download an invalid file: ");
					}
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

}
