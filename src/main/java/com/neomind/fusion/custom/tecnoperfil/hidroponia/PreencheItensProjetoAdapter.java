package com.neomind.fusion.custom.tecnoperfil.hidroponia;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.neomind.framework.base.entity.NeoBaseEntity;
import com.neomind.fusion.common.NeoObject;
import com.neomind.fusion.entity.EntityWrapper;
import com.neomind.fusion.persist.PersistEngine;
import com.neomind.fusion.workflow.Activity;
import com.neomind.fusion.workflow.Task;
import com.neomind.fusion.workflow.adapter.AdapterInterface;
import com.neomind.fusion.workflow.adapter.AdapterUtils;
import com.neomind.util.NeoUtils;

import software.amazon.ion.Decimal;

public class PreencheItensProjetoAdapter implements AdapterInterface {

	String listaProjetos = "ItensProH";
	String listaDeItens = "ItensProHdp";
	String unidade = "PC";

	@Override
	public void back(EntityWrapper arg0, Activity arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void start(Task arg0, EntityWrapper arg1, Activity arg2) {
		List<NeoBaseEntity> projetos = arg1.findGenericValue(listaProjetos);

		for (NeoBaseEntity projeto : projetos) {
			EntityWrapper projetoWrapper = new EntityWrapper(projeto);
			List<NeoBaseEntity> itens = projetoWrapper.findGenericValue(listaDeItens);
			for (NeoBaseEntity item : itens) {
				PersistEngine.removeById((Long)item.id());
			}
			preencherItens(projetoWrapper);
		}

		try {
			NeoBaseEntity projeto = projetos.get(projetos.size() - 1);
			EntityWrapper projetoWrapper = new EntityWrapper(projeto);
			
			Long qtdAdesivo = arg1.findGenericValue("QtdAdes");
			if (qtdAdesivo > 0) {
				String descricaoAdesivo = arg1.findGenericValue("DescricaoDoAdesivo");
				BigDecimal valorAdesivo = getPrecofromObject(arg1.findGenericValue("PrecoUnitvAdesivo"));
				BigDecimal totalProjeto = projetoWrapper.findGenericValue("vltotproj");
				BigDecimal totalItem = BigDecimal.ZERO;
				BigDecimal totalCubagem = projetoWrapper.findGenericValue("CubTOP");
				BigDecimal CubagemItem = arg1.findGenericValue("CubagemAdesivo");
				Long volumeadesivo = arg1.findGenericValue("VolumeAdesivo");
				BigDecimal pesoliquidoadesivo = arg1.findGenericValue("PesoLiquidoAdesivo");
				BigDecimal pesobrutoadesivo = arg1.findGenericValue("PesoBrutoAdesivo");
				BigDecimal tamanhoadesivo = arg1.findGenericValue("DadosAdicionaisad.b5_compr");
				String codigoadesivo = arg1.findGenericValue("CodItemAdesivo.b1_cod");
				BigDecimal larguraadesivo = arg1.findGenericValue("DadosAdicionaisad.b5_altura");
				BigDecimal alturaadesivo =  arg1.findGenericValue("DadosAdicionaisad.b5_larg");
				BigDecimal pesobrutoadesivoun = arg1.findGenericValue("CodItemAdesivo.b1_pesbru");
				BigDecimal pesoliquidoadesivoun = arg1.findGenericValue("CodItemAdesivo.b1_pesbru");
				BigDecimal embalagem = arg1.findGenericValue("CodItemAdesivo.b1_qe");
				String ferramenta = arg1.findGenericValue("FerraAdesiv");
				String grupo = arg1.findGenericValue("CodItemAdesivo.b1_grupo");
				String codigocor = arg1.findGenericValue("CodItemAdesivo.b1_zcorprd");
				String cor = arg1.findGenericValue("CodItemAdesivo.b1_zcor");
				String ncm = arg1.findGenericValue("CodItemAdesivo.b1_posipi");
				String descesp = arg1.findGenericValue("CodItemAdesivo.b1_zdesces");

				List<NeoObject> itens = projetoWrapper.findGenericValue(listaDeItens);
				NeoObject itemObject = AdapterUtils.createNewEntityInstance("hidItemProj");
				
				
				
				EntityWrapper wrapper = new EntityWrapper(itemObject);

				wrapper.setValue("descricao", descricaoAdesivo);
				wrapper.setValue("quantidade", qtdAdesivo);
				wrapper.setValue("unidade", "PC");
				wrapper.setValue("CodItemP",codigoadesivo);
				wrapper.setValue("PreUniP", valorAdesivo);
				wrapper.setValue("FeritemPr", ferramenta);
				wrapper.setValue("TamanPr", tamanhoadesivo);
				wrapper.setValue("CubItemP",CubagemItem);
				wrapper.setValue("QtdVolIP",volumeadesivo);
				wrapper.setValue("QtdPesItemP",pesobrutoadesivo);
				wrapper.setValue("QtdPesItemPL",pesoliquidoadesivo);
				wrapper.setValue("ValorAjustado", valorAdesivo);
				wrapper.setValue("Altura", alturaadesivo);
				wrapper.setValue("Largura", larguraadesivo);
				wrapper.setValue("PesoLiquido", pesobrutoadesivoun);
				wrapper.setValue("PesoBruto", pesoliquidoadesivoun);
				wrapper.setValue("Embalagem", embalagem);
				wrapper.setValue("Grupo", grupo);
				wrapper.setValue("CodigoCor", codigocor);
				wrapper.setValue("DescricaoDaCor", cor);
				wrapper.setValue("Ncm", ncm);
				wrapper.setValue("DescricaoEspanhol",descesp);
				
			
				
				totalItem = valorAdesivo.multiply(new BigDecimal(qtdAdesivo));
				totalProjeto = totalItem.add(totalProjeto);
				wrapper.setValue("PreTotalP", totalItem);
				wrapper.setValue("PrecoTotalExp", totalItem);
				PersistEngine.persist(wrapper.getObject());

				itens.add(itemObject);

				projetoWrapper.setValue(listaDeItens, itens);
				projetoWrapper.setValue("vltotproj", totalProjeto);
				
				
				totalCubagem = CubagemItem.add(totalCubagem);
				
				projetoWrapper.setValue("CubTOP", totalCubagem);
			}
		} catch (Exception e) {
			System.out.println("Erro ao colocar adesivo");
			e.printStackTrace();
		}

	}

	private void preencherItens(EntityWrapper projeto) {
		List<NeoObject> lista = new ArrayList<NeoObject>();
		BigDecimal totalProjeto = new BigDecimal(0);
		
		// Criado Pelo Gecinei totalCubagem,totalvolume,totalpeso
		BigDecimal totalCubagem = projeto.findGenericValue("TotCubMI");
		projeto.setValue("CubTOP", totalCubagem);
				
	    Long totalvolume = projeto.findGenericValue("TOTVolMI");   
	    projeto.setValue("QtdVolpro",  totalvolume);
	    
	    BigDecimal totalpeso = projeto.findGenericValue("TotPesMI");    
	    projeto.setValue("QtdPesoDoProjeto", totalpeso);
	    
	    BigDecimal totalpesoliquido = projeto.findGenericValue("TotalPesLiqu");    
	    projeto.setValue("QtdPesoLiquidoDoProjeto", totalpeso);
			
		
		
		String ferramentaBarra = projeto.findGenericValue("FerBarra");
		String grupo = projeto.findGenericValue("PerfisH.Grupo");

		Long qtdBarra1LadoA = projeto.findGenericValue("QtdTLAB1");
		Long qtdBarra1LadoB = projeto.findGenericValue("QtdLBB1");
		Long qtdBarra2LadoA = projeto.findGenericValue("QtdTLAB2");
		Long qtdBarra2LadoB = projeto.findGenericValue("QtdTLBB2");
		Long qtdBarra3LadoA = projeto.findGenericValue("QtdTLAB3");
		Long qtdBarra3LadoB = projeto.findGenericValue("QtdTLBB3");
		Long qtdBarra4LadoA = projeto.findGenericValue("QtdTLAB4");
		Long qtdBarra4LadoB = projeto.findGenericValue("QtdTLBB4");
		Long qtdBarra5LadoA = projeto.findGenericValue("QtdTLAB5");
		Long qtdBarra5LadoB = projeto.findGenericValue("QtdTLBB5");

		Long qtdTampaEntrada = projeto.findGenericValue("QtdToTE");
		Long qtdTampaSaida = projeto.findGenericValue("QtdToTS");
		Long qtdEmenda = projeto.findGenericValue("QtdToEM");
		Long qtdcoletorAntigo = projeto.findGenericValue("QtdToPECo");
		Long qtdcoletorNovo = projeto.findGenericValue("qtdToPeCoN");
		Long qtdTravessaNova = projeto.findGenericValue("QtdTrCN");
		Long qtdTravessaAntiga = projeto.findGenericValue("QtdToTRCA");
		Long qtdColunaNova = projeto.findGenericValue("QtdToCCNo");
		Long qtdColunaAntiga = projeto.findGenericValue("QtdTPCCA");
		Long qtdSuporteColetor = projeto.findGenericValue("QtdToSuCo");
		Long qtdInjetor = projeto.findGenericValue("QtdToInj");
		Long qtdPresilhas = projeto.findGenericValue("QtdToPres");
		Long qtdSuporteflauta = projeto.findGenericValue("QtdToSupFla");

		// Barra1A
		// Incluido POr Gecinei CubLAB1,QtdVLAB1,QtdPLAB1
		if (validateLong(qtdBarra1LadoA)) {
			BigDecimal tamanho = projeto.findGenericValue("Tmbarr1");
			String codigo = "0";
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DesPeB1"), qtdBarra1LadoA,
					unidade,codigo, projeto.findGenericValue("PrUniBar1A"), ferramentaBarra, tamanho,
					projeto.findGenericValue("CubLAB1"),projeto.findGenericValue("QtdVLAB1"),projeto.findGenericValue("QtdPLAB1"),
					projeto.findGenericValue("QtdPLAB1L"),projeto.findGenericValue("ProdutoBaseBarra.zb4_alt"),projeto.findGenericValue("ProdutoBaseBarra.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseBarra.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseBarra.zb4_peso"),projeto.findGenericValue("ProdutoBaseBarra.zb4_qe"),
					grupo,projeto.findGenericValue("ProdutoBaseBarra.zb4_zcorprod"),projeto.findGenericValue("ProdutoBaseBarra.zb4_cor"),projeto.findGenericValue("ProdutoBaseBarra.zb4_posipi")
					,projeto.findGenericValue("LadoBarra1"),projeto.findGenericValue("DisdoFur.DistFHdp"),projeto.findGenericValue("FurosPe.DescFu")
					,projeto.findGenericValue("DescESPB1A")));
			
		}

		// Barra1B
		// Incluido POr Gecinei CubLBB1,QtdVLBB1,QtdPLBB1
		if (validateLong(qtdBarra1LadoB)) {
			BigDecimal tamanho = projeto.findGenericValue("Tmbarr1");
			String codigo = "0";
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DesPeLBB1"), qtdBarra1LadoB,
					unidade, codigo, projeto.findGenericValue("PreLaBB1"), ferramentaBarra, tamanho,
					projeto.findGenericValue("CubLBB1"),projeto.findGenericValue("QtdVLBB1"),projeto.findGenericValue("QtdPLBB1"),
					projeto.findGenericValue("QtdPLBB1L"),projeto.findGenericValue("ProdutoBaseBarra.zb4_alt"),projeto.findGenericValue("ProdutoBaseBarra.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseBarra.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseBarra.zb4_peso"),projeto.findGenericValue("ProdutoBaseBarra.zb4_qe"),
					grupo,projeto.findGenericValue("ProdutoBaseBarra.zb4_zcorprod"),projeto.findGenericValue("ProdutoBaseBarra.zb4_cor")
					,projeto.findGenericValue("ProdutoBaseBarra.zb4_posipi")
					,projeto.findGenericValue("LadoBarra1BOUC2"),projeto.findGenericValue("DisdoFur.DistFHdp"),projeto.findGenericValue("FurosPe.DescFu")
					,projeto.findGenericValue("DesESPB1B")));
			
		}
		
		

		// Barra2A
		// Incluido POr Gecinei CubLAB2,QtdVLAB2,QtdPLAB2
		if (validateLong(qtdBarra2LadoA)) {
			BigDecimal tamanho = projeto.findGenericValue("Tmbarr2");
			String codigo = "0";
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DesPELAB2"), qtdBarra2LadoA,
					unidade, codigo, projeto.findGenericValue("PreLaAB2"), ferramentaBarra, tamanho,
					projeto.findGenericValue("CubLAB2"),projeto.findGenericValue("QtdVLAB2"),projeto.findGenericValue("QtdPLAB2")
					,projeto.findGenericValue("QtdPLAB2l"),projeto.findGenericValue("ProdutoBaseBarra.zb4_alt"),projeto.findGenericValue("ProdutoBaseBarra.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseBarra.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseBarra.zb4_peso"),projeto.findGenericValue("ProdutoBaseBarra.zb4_qe"),
					grupo,projeto.findGenericValue("ProdutoBaseBarra.zb4_zcorprod"),projeto.findGenericValue("ProdutoBaseBarra.zb4_cor")
					,projeto.findGenericValue("ProdutoBaseBarra.zb4_posipi")
					,projeto.findGenericValue("LadoBarra2AOuD1"),projeto.findGenericValue("DisdoFur.DistFHdp"),projeto.findGenericValue("FurosPe.DescFu")
					,projeto.findGenericValue("DescESPB2A")));
			
		}

		// Barra2B
		// Incluido POr Gecinei CubLBB2,QtdVLBB2,QtdPLBB2
		
		if (validateLong(qtdBarra2LadoB)) {
			BigDecimal tamanho = projeto.findGenericValue("Tmbarr2");
			String codigo = "0";
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DesPeLaBB2"), qtdBarra2LadoB,
					unidade, codigo, projeto.findGenericValue("PreLaBB2"), ferramentaBarra, tamanho,
					projeto.findGenericValue("CubLBB2"),projeto.findGenericValue("QtdVLBB2"),projeto.findGenericValue("QtdPLBB2"),
					projeto.findGenericValue("QtdPLBB2L"),projeto.findGenericValue("ProdutoBaseBarra.zb4_alt"),projeto.findGenericValue("ProdutoBaseBarra.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseBarra.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseBarra.zb4_peso"),projeto.findGenericValue("ProdutoBaseBarra.zb4_qe"),
					grupo,projeto.findGenericValue("ProdutoBaseBarra.zb4_zcorprod"),projeto.findGenericValue("ProdutoBaseBarra.zb4_cor")
					,projeto.findGenericValue("ProdutoBaseBarra.zb4_posipi")
					,projeto.findGenericValue("LadoBarra2BOuD2"),projeto.findGenericValue("DisdoFur.DistFHdp"),projeto.findGenericValue("FurosPe.DescFu")
					,projeto.findGenericValue("DescESPB2B")));
			
			
		}

		// Barra3A
		// Incluido POr Gecinei CubLAB3,QtdVLAB3,QtdPLAB3
		if (validateLong(qtdBarra3LadoA)) {
			BigDecimal tamanho = projeto.findGenericValue("Tmbarr3");
			String codigo = "0";

			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DesPeLAB3"), qtdBarra3LadoA,
					unidade, codigo, projeto.findGenericValue("PreLAB3"), ferramentaBarra, tamanho,
					projeto.findGenericValue("CubLAB3"),projeto.findGenericValue("QtdVLAB3"),projeto.findGenericValue("QtdPLAB3"),
					projeto.findGenericValue("QtdPLAB3L"),projeto.findGenericValue("ProdutoBaseBarra.zb4_alt"),projeto.findGenericValue("ProdutoBaseBarra.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseBarra.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseBarra.zb4_peso"),projeto.findGenericValue("ProdutoBaseBarra.zb4_qe"),
					grupo,projeto.findGenericValue("ProdutoBaseBarra.zb4_zcorprod"),projeto.findGenericValue("ProdutoBaseBarra.zb4_cor")
					,projeto.findGenericValue("ProdutoBaseBarra.zb4_posipi")
					,projeto.findGenericValue("LadoBarra3A"),projeto.findGenericValue("DisdoFur.DistFHdp"),projeto.findGenericValue("FurosPe.DescFu")
					,projeto.findGenericValue("DesESPB3A")));
			
		}

		// Barra3B
		// Incluido POr Gecinei CubLBB3,QtdVLBB3,QtdPLBB3
		if (validateLong(qtdBarra3LadoB)) {
			BigDecimal tamanho = projeto.findGenericValue("Tmbarr3");
			String codigo = "0";
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DesPELBB3"), qtdBarra3LadoB,
					unidade, codigo, projeto.findGenericValue("PreLaBB3"), ferramentaBarra, tamanho,
					projeto.findGenericValue("CubLBB3"),projeto.findGenericValue("QtdVLBB3"),projeto.findGenericValue("QtdPLBB3"),
					projeto.findGenericValue("QtdPLBB3L"),projeto.findGenericValue("ProdutoBaseBarra.zb4_alt"),projeto.findGenericValue("ProdutoBaseBarra.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseBarra.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseBarra.zb4_peso"),projeto.findGenericValue("ProdutoBaseBarra.zb4_qe"),
					grupo,projeto.findGenericValue("ProdutoBaseBarra.zb4_zcorprod"),projeto.findGenericValue("ProdutoBaseBarra.zb4_cor")
					,projeto.findGenericValue("ProdutoBaseBarra.zb4_posipi")
					,projeto.findGenericValue("LadoBarra3B"),projeto.findGenericValue("DisdoFur.DistFHdp"),projeto.findGenericValue("FurosPe.DescFu")
					,projeto.findGenericValue("DescESPB3B")));
			
		}

		// Barra4A
		// Incluido POr Gecinei CubLAB4,QtdVLAB4,QtdPLAB4
		if (validateLong(qtdBarra4LadoA)) {
			BigDecimal tamanho = projeto.findGenericValue("Tmbarr4");
			String codigo = "0";
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DesPELAB4"), qtdBarra4LadoA,
					unidade,codigo, projeto.findGenericValue("PreLaAB4"), ferramentaBarra, tamanho,
					projeto.findGenericValue("CubLAB4"),projeto.findGenericValue("QtdVLAB4"),projeto.findGenericValue("QtdPLAB4"),
					projeto.findGenericValue("QtdPLB4L"),projeto.findGenericValue("ProdutoBaseBarra.zb4_alt"),projeto.findGenericValue("ProdutoBaseBarra.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseBarra.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseBarra.zb4_peso"),projeto.findGenericValue("ProdutoBaseBarra.zb4_qe"),
					grupo,projeto.findGenericValue("ProdutoBaseBarra.zb4_zcorprod"),projeto.findGenericValue("ProdutoBaseBarra.zb4_cor")
					,projeto.findGenericValue("ProdutoBaseBarra.zb4_posipi"),
					projeto.findGenericValue("LadoBarra4A"),projeto.findGenericValue("DisdoFur.DistFHdp"),projeto.findGenericValue("FurosPe.DescFu")
					,projeto.findGenericValue("DescESPB4A")));
			
		}

		// Barra4B
		// Incluido POr Gecinei CubLBB4,QtdVLBB4,QtdPLBB4
		if (validateLong(qtdBarra4LadoB)) {
			BigDecimal tamanho = projeto.findGenericValue("Tmbarr4");
			String codigo = "0";
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DesPELBB4"), qtdBarra4LadoB,
					unidade,codigo, projeto.findGenericValue("PreLaBB4"), ferramentaBarra, tamanho,
					projeto.findGenericValue("CubLBB4"),projeto.findGenericValue("QtdVLBB4"),projeto.findGenericValue("QtdPLBB4"),
					projeto.findGenericValue("QtdPLBB4L"),projeto.findGenericValue("ProdutoBaseBarra.zb4_alt"),projeto.findGenericValue("ProdutoBaseBarra.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseBarra.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseBarra.zb4_peso"),projeto.findGenericValue("ProdutoBaseBarra.zb4_qe"),
					grupo,projeto.findGenericValue("ProdutoBaseBarra.zb4_zcorprod"),projeto.findGenericValue("ProdutoBaseBarra.zb4_cor")
					,projeto.findGenericValue("ProdutoBaseBarra.zb4_posipi"),
					projeto.findGenericValue("LadoBarra4B"),projeto.findGenericValue("DisdoFur.DistFHdp"),projeto.findGenericValue("FurosPe.DescFu")
					,projeto.findGenericValue("DescESPB4B")));
		}

		// Barra5A
		// Incluido POr Gecinei CubLAB5,QtdVLAB5,QtdPLAB5
		if (validateLong(qtdBarra5LadoA)) {
			BigDecimal tamanho = projeto.findGenericValue("Tmbarr5");
			String codigo = "0";
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DesPELAB5"), qtdBarra5LadoA,
					unidade,codigo, projeto.findGenericValue("PreLaAB5"), ferramentaBarra, tamanho,
					projeto.findGenericValue("CubLAB5"),projeto.findGenericValue("QtdVLAB5"),projeto.findGenericValue("QtdPLAB5"),
					projeto.findGenericValue("QtdPLAB5L"),projeto.findGenericValue("ProdutoBaseBarra.zb4_alt"),projeto.findGenericValue("ProdutoBaseBarra.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseBarra.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseBarra.zb4_peso"),projeto.findGenericValue("ProdutoBaseBarra.zb4_qe"),
					projeto.findGenericValue("ProdutoBaseBarra.zb4_grupo"),projeto.findGenericValue("ProdutoBaseBarra.zb4_zcorprod"),projeto.findGenericValue("ProdutoBaseBarra.zb4_cor")
					,projeto.findGenericValue("ProdutoBaseBarra.zb4_posipi"),projeto.findGenericValue("LadoBarra5A"),projeto.findGenericValue("DisdoFur.DistFHdp"),projeto.findGenericValue("FurosPe.DescFu")
					,projeto.findGenericValue("DescESPB5A")));
		}

		// Barra5B
		// Incluido POr Gecinei CubLBB5,QtdVLBB5,QtdPLBB5
		if (validateLong(qtdBarra5LadoB)) {
			BigDecimal tamanho = projeto.findGenericValue("Tmbarr5");
			String codigo = "0";
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DesPeLBB5"), qtdBarra5LadoB,
					unidade, codigo, projeto.findGenericValue("PreLaBB5"), ferramentaBarra, tamanho,
					projeto.findGenericValue("CubLBB5"),projeto.findGenericValue("QtdVLBB5"),projeto.findGenericValue("QtdPLBB5"),
					projeto.findGenericValue("QtdPLBB5l"),projeto.findGenericValue("ProdutoBaseBarra.zb4_alt"),projeto.findGenericValue("ProdutoBaseBarra.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseBarra.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseBarra.zb4_peso"),projeto.findGenericValue("ProdutoBaseBarra.zb4_qe"),
					projeto.findGenericValue("ProdutoBaseBarra.zb4_grupo"),projeto.findGenericValue("ProdutoBaseBarra.zb4_zcorprod"),projeto.findGenericValue("ProdutoBaseBarra.zb4_cor")
					,projeto.findGenericValue("ProdutoBaseBarra.zb4_posipi"),projeto.findGenericValue("LadoBarra5B"),projeto.findGenericValue("DisdoFur.DistFHdp"),projeto.findGenericValue("FurosPe.DescFu")
					,projeto.findGenericValue("DescESPB5B")));
					
			
		}

		// Tampa Entrada
		// Incluido POr Gecinei CubTAE,QtdVTE,QtdPTE
		if (validateLong(qtdTampaEntrada)) {
			String ferramenta = projeto.findGenericValue("ProdutoBaseTampaEntrada.zb4_ferram");
			String codigo = projeto.findGenericValue("ProdutoBaseTampaEntrada.zb4_cod");
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DescTE"), qtdTampaEntrada,
					unidade, codigo, getPrecofromObject(projeto.findGenericValue("PreTaEnT")), ferramenta, BigDecimal.ZERO,
					projeto.findGenericValue("CubTAE"),projeto.findGenericValue("QtdVTE"),projeto.findGenericValue("QtdPTE"),
					projeto.findGenericValue("QtdPTEL"),projeto.findGenericValue("ProdutoBaseTampaEntrada.zb4_alt"),projeto.findGenericValue("ProdutoBaseTampaEntrada.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseTampaEntrada.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseTampaEntrada.zb4_peso"),projeto.findGenericValue("ProdutoBaseTampaEntrada.zb4_qe"),
					projeto.findGenericValue("ProdutoBaseTampaEntrada.zb4_grupo"),projeto.findGenericValue("ProdutoBaseTampaEntrada.zb4_zcorprod")
					,projeto.findGenericValue("ProdutoBaseTampaEntrada.zb4_cor"),projeto.findGenericValue("ProdutoBaseTampaEntrada.zb4_posipi")
					,"","","",projeto.findGenericValue("ProdutoBaseTampaEntrada.zb4_desces")));
			
		}

		// Tampa Saida
		// Incluido POr Gecinei CubTAS,QtdVTS,QtdPTS 
		if (validateLong(qtdTampaSaida)) {
			String ferramenta = projeto.findGenericValue("ProdutoBaseTampaSaida.zb4_ferram");
			String codigo = projeto.findGenericValue("ProdutoBaseTampaSaida.zb4_cod");

			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DescTS"), qtdTampaSaida,
					unidade,codigo, getPrecofromObject(projeto.findGenericValue("PreTASAT")), ferramenta, BigDecimal.ZERO,
					projeto.findGenericValue("CubTAS"),projeto.findGenericValue("QtdVTS"),projeto.findGenericValue("QtdPTS"),
					projeto.findGenericValue("QtdPTSL"),projeto.findGenericValue("ProdutoBaseTampaSaida.zb4_alt"),projeto.findGenericValue("ProdutoBaseTampaSaida.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseTampaSaida.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseTampaSaida.zb4_peso"),projeto.findGenericValue("ProdutoBaseTampaSaida.zb4_qe"),
					projeto.findGenericValue("ProdutoBaseTampaSaida.zb4_grupo"),projeto.findGenericValue("ProdutoBaseTampaSaida.zb4_zcorprod")
					,projeto.findGenericValue("ProdutoBaseTampaSaida.zb4_cor"),projeto.findGenericValue("ProdutoBaseTampaSaida.zb4_posipi"),"","",""
					,projeto.findGenericValue("ProdutoBaseTampaSaida.zb4_desces")));
		}
		// Emenda
		// Incluido POr Gecinei CubEme,QtdVEme,QtdPEme
		
		if (validateLong(qtdEmenda)) {
			String ferramenta = projeto.findGenericValue("ProdutoBaseEmenda.zb4_ferram");
			String codigo = projeto.findGenericValue("ProdutoBaseEmenda.zb4_cod");
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DescEmen"), qtdEmenda,
					unidade,codigo, getPrecofromObject(projeto.findGenericValue("PreEmT")), ferramenta, BigDecimal.ZERO,
					projeto.findGenericValue("CubEme"),projeto.findGenericValue("QtdVEme"),projeto.findGenericValue("QtdPEme")
					,projeto.findGenericValue("QtdPEmel"),projeto.findGenericValue("ProdutoBaseEmenda.zb4_alt"),projeto.findGenericValue("ProdutoBaseEmenda.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseEmenda.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseEmenda.zb4_peso"),projeto.findGenericValue("ProdutoBaseEmenda.zb4_qe"),
					projeto.findGenericValue("ProdutoBaseEmenda.zb4_grupo"),projeto.findGenericValue("ProdutoBaseEmenda.zb4_zcorprod")
					,projeto.findGenericValue("ProdutoBaseEmenda.zb4_cor"),projeto.findGenericValue("ProdutoBaseEmenda.zb4_posipi"),"","",""
					,projeto.findGenericValue("ProdutoBaseEmenda.zb4_desces")));
			
			
		}

		// Coletor Antigo
		// Incluido POr Gecinei CubCOA,QtdVCOA,QtdPeCOA
		if (validateLong(qtdcoletorAntigo)) {
			String ferramenta = projeto.findGenericValue("ProdutoBaseColetorAntigo.zb4_ferram");
			String codigo = "0";
			
			BigDecimal tamanho = projeto.findGenericValue("LargHi");
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DescPeCo"), qtdcoletorAntigo,
					unidade,codigo, projeto.findGenericValue("PrColAnt"), ferramenta, tamanho,
					projeto.findGenericValue("CubCOA"),projeto.findGenericValue("QtdVCOA"),projeto.findGenericValue("QtdPeCOA"),
					projeto.findGenericValue("QtdPeCOAl"),projeto.findGenericValue("ProdutoBaseColetorAntigo.zb4_alt"),projeto.findGenericValue("ProdutoBaseColetorAntigo.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseColetorAntigo.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseColetorAntigo.zb4_peso"),projeto.findGenericValue("ProdutoBaseColetorAntigo.zb4_qe"),
					projeto.findGenericValue("ProdutoBaseColetorAntigo.zb4_grupo"),projeto.findGenericValue("ProdutoBaseColetorAntigo.zb4_zcorprod")
					,projeto.findGenericValue("ProdutoBaseColetorAntigo.zb4_cor"),projeto.findGenericValue("ProdutoBaseColetorAntigo.zb4_posipi"),"","",""
					,projeto.findGenericValue("DescESPCOLANT")));
			
		}

		// Coletor Novo
		// Incluido POr Gecinei CubCON, QtdVCON,QtdPCON
		if (validateLong(qtdcoletorNovo)) {
			String ferramenta = projeto.findGenericValue("ProdutoBaseColetorNovo.zb4_ferram");
			String codigo = "0";
			BigDecimal tamanho = projeto.findGenericValue("LargHi");
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("descrPeCoN"), qtdcoletorNovo,
					unidade,codigo, projeto.findGenericValue("PreUColN"), ferramenta, tamanho,
					projeto.findGenericValue("CubCON"),projeto.findGenericValue("QtdVCON"),projeto.findGenericValue("QtdPCON"),
					projeto.findGenericValue("QtdPCONL"),projeto.findGenericValue("ProdutoBaseColetorNovo.zb4_alt"),projeto.findGenericValue("ProdutoBaseColetorNovo.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseColetorNovo.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseColetorNovo.zb4_peso"),projeto.findGenericValue("ProdutoBaseColetorNovo.zb4_qe"),
					projeto.findGenericValue("ProdutoBaseColetorNovo.zb4_grupo"),projeto.findGenericValue("ProdutoBaseColetorNovo.zb4_zcorprod")
					,projeto.findGenericValue("ProdutoBaseColetorNovo.zb4_cor"),projeto.findGenericValue("ProdutoBaseColetorNovo.zb4_posipi"),"","",""
					,projeto.findGenericValue("DescESPColetorNovo")));
			
		}

		// Travessa Nova
		// Incluido POr Gecinei CubTRN,QtdVTRN,QtdPTRN
		if (validateLong(qtdTravessaNova)) {
			String ferramenta = projeto.findGenericValue("FerTravno");
			String codigo = "0";
			BigDecimal tamanho = projeto.findGenericValue("CalculoLargura");
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DescTrCN"), qtdTravessaNova,
					unidade,codigo, projeto.findGenericValue("PreuTraN"), ferramenta, tamanho,
					projeto.findGenericValue("CubTRN"),projeto.findGenericValue("QtdVTRN"),projeto.findGenericValue("QtdPTRN"),
					projeto.findGenericValue("QtdPTRNT"),projeto.findGenericValue("ProdutoBaseTravessaNova.zb4_alt"),projeto.findGenericValue("ProdutoBaseTravessaNova.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseTravessaNova.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseTravessaNova.zb4_peso"),projeto.findGenericValue("ProdutoBaseTravessaNova.zb4_qe"),
				projeto.findGenericValue("ProdutoBaseTravessaNova.zb4_grupo"),projeto.findGenericValue("ProdutoBaseTravessaNova.zb4_zcorprod"),projeto.findGenericValue("ProdutoBaseTravessaNova.zb4_cor")
				,projeto.findGenericValue("ProdutoBaseTravessaNova.zb4_posipi"),"","","",projeto.findGenericValue("DescESPTravessaNovat")));
		}
		// Travessa Antiga 
		// Incluido POr Gecinei CubTRA,QtdVTRA,QtdPTRA
		if (validateLong(qtdTravessaAntiga)) {
			String ferramenta = projeto.findGenericValue("FerTravve");
			String codigo = "0";
			BigDecimal tamanho = projeto.findGenericValue("CalculoLargura");
			totalProjeto = totalProjeto
					.add(addItenToList(lista, projeto.findGenericValue("DescTrCoA"), qtdTravessaAntiga, unidade, codigo,
							projeto.findGenericValue("PrUTraA"), ferramenta, tamanho,
							projeto.findGenericValue("CubTRA"),projeto.findGenericValue("QtdVTRA"),projeto.findGenericValue("QtdPTRA")
							,projeto.findGenericValue("QtdPTRAL"),projeto.findGenericValue("ProdutoBaseTravessaAntiga.zb4_alt"),projeto.findGenericValue("ProdutoBaseTravessaAntiga.zb4_larg")
							,projeto.findGenericValue("ProdutoBaseTravessaAntiga.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseTravessaAntiga.zb4_peso"),projeto.findGenericValue("ProdutoBaseTravessaAntiga.zb4_qe"),
							projeto.findGenericValue("ProdutoBaseTravessaAntiga.zb4_grupo"),projeto.findGenericValue("ProdutoBaseTravessaAntiga.zb4_zcorprod")
							,projeto.findGenericValue("ProdutoBaseTravessaAntiga.zb4_cor"),projeto.findGenericValue("ProdutoBaseTravessaAntiga.zb4_posipi"),"","",""
							,projeto.findGenericValue("DescESPTravessaAntiga")));
			
		}

		// Coluna Nova
		// Incluido POr Gecinei CubColN ,QtdVColN,QtdPColN
		if (validateLong(qtdColunaNova)) {
			String ferramenta = projeto.findGenericValue("ProdutoBaseColunaNova.zb4_ferram");
			String codigo = "0";
			BigDecimal tamanho = projeto.findGenericValue("Coluna");
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DescrPerCCN"), qtdColunaNova,
					unidade,codigo, projeto.findGenericValue("PreUColNo"), ferramenta, tamanho,
					projeto.findGenericValue("CubColN"),projeto.findGenericValue("QtdVColN"),
					projeto.findGenericValue("QtdPColN"),projeto.findGenericValue("QtdPColNT"),projeto.findGenericValue("ProdutoBaseColunaNova.zb4_alt"),projeto.findGenericValue("ProdutoBaseColunaNova.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseColunaNova.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseColunaNova.zb4_peso"),projeto.findGenericValue("ProdutoBaseColunaNova.zb4_qe"),
					projeto.findGenericValue("ProdutoBaseColunaNova.zb4_grupo"),projeto.findGenericValue("ProdutoBaseColunaNova.zb4_zcorprod"),
					projeto.findGenericValue("ProdutoBaseColunaNova.zb4_cor"),projeto.findGenericValue("ProdutoBaseColunaNova.zb4_posipi"),"","",""
					,projeto.findGenericValue("DescEspColNova")));
			
			
		}

		// Coluna Antiga
		// Incluido POr Gecinei CubColA,QtdVColA,QtdPColA
		if (validateLong(qtdColunaAntiga)) {
			String ferramenta = projeto.findGenericValue("ProdutoBaseColunaAntiga.zb4_ferram");
			String codigo = "0";
			BigDecimal tamanho = projeto.findGenericValue("Coluna");
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DescPCCA"), qtdColunaAntiga,
					unidade,codigo, projeto.findGenericValue("PreUClANt"), ferramenta, tamanho,
					projeto.findGenericValue("CubColA"),projeto.findGenericValue("QtdVColA"),projeto.findGenericValue("QtdPColA"),
					projeto.findGenericValue("QtdPColAT"),projeto.findGenericValue("ProdutoBaseColunaAntiga.zb4_alt"),projeto.findGenericValue("ProdutoBaseColunaAntiga.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseColunaAntiga.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseColunaAntiga.zb4_peso"),projeto.findGenericValue("ProdutoBaseColunaAntiga.zb4_qe"),
					projeto.findGenericValue("ProdutoBaseColunaAntiga.zb4_grupo"),projeto.findGenericValue("ProdutoBaseColunaAntiga.zb4_zcorprod")
					,projeto.findGenericValue("ProdutoBaseColunaAntiga.zb4_cor"),projeto.findGenericValue("ProdutoBaseColunaAntiga.zb4_posipi"),"","",""
					,projeto.findGenericValue("DescESpColunaAntiga")));
			
		}

		// Suporte Coletor
		// Incluido POr Gecinei CubSuC,QtdVSC,QtdPSC
		
		if (validateLong(qtdSuporteColetor)) {
			String ferramenta = projeto.findGenericValue("FerSuCol");
			String codigo = projeto.findGenericValue("ProdutoBaseSuporteColetor.zb4_cod"); 
			totalProjeto = totalProjeto
					.add(addItenToList(lista, projeto.findGenericValue("DescSuCOl"), qtdSuporteColetor, unidade,codigo,
							getPrecofromObject(projeto.findGenericValue("PreuniSCT")), ferramenta, BigDecimal.ZERO,
							projeto.findGenericValue("CubSuC"),projeto.findGenericValue("QtdVSC"),projeto.findGenericValue("QtdPSC"),
							projeto.findGenericValue("QtdPSCT"),projeto.findGenericValue("ProdutoBaseSuporteColetor.zb4_alt"),projeto.findGenericValue("ProdutoBaseSuporteColetor.zb4_larg")
							,projeto.findGenericValue("ProdutoBaseSuporteColetor.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseSuporteColetor.zb4_peso"),projeto.findGenericValue("ProdutoBaseSuporteColetor.zb4_qe"),
							projeto.findGenericValue("ProdutoBaseSuporteColetor.zb4_grupo"),projeto.findGenericValue("ProdutoBaseSuporteColetor.zb4_zcorprod"),
							projeto.findGenericValue("ProdutoBaseSuporteColetor.zb4_cor"),projeto.findGenericValue("ProdutoBaseSuporteColetor.zb4_posipi"),"","",""
							,projeto.findGenericValue("ProdutoBaseSuporteColetor.zb4_desces")));
					
			
		}

		// Injetor
		// Incluido POr Gecinei CubINJ,QtdVINJ,QtdPINJ
		if (validateLong(qtdInjetor)) {
			String ferramenta = projeto.findGenericValue("ProdutoBaseInjetor.zb4_ferram");
			String codigo = projeto.findGenericValue("ProdutoBaseInjetor.zb4_cod"); 
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DescInjet"), qtdInjetor,
					unidade,codigo, getPrecofromObject(projeto.findGenericValue("PreUNIlnHT")), ferramenta, BigDecimal.ZERO,
					projeto.findGenericValue("CubINJ"),projeto.findGenericValue("QtdVINJ"),projeto.findGenericValue("QtdPINJ"),
					projeto.findGenericValue("QtdPINJT"),projeto.findGenericValue("ProdutoBaseInjetor.zb4_alt"),projeto.findGenericValue("ProdutoBaseInjetor.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseInjetor.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseInjetor.zb4_peso"),projeto.findGenericValue("ProdutoBaseInjetor.zb4_qe"),
					projeto.findGenericValue("ProdutoBaseInjetor.zb4_grupo"),projeto.findGenericValue("ProdutoBaseInjetor.zb4_zcorprod"),
					projeto.findGenericValue("ProdutoBaseInjetor.zb4_cor"),projeto.findGenericValue("ProdutoBaseInjetor.zb4_posipi"),"","",""
					,projeto.findGenericValue("ProdutoBaseInjetor.zb4_desces")));
			
		}

		// Presilhas
		// Incluido POr Gecinei CubPre,QtdVPre,QtdPPre
		if (validateLong(qtdPresilhas)) {
			String ferramenta = projeto.findGenericValue("ProdutoBasePresilhas.zb4_ferram");
			String codigo = projeto.findGenericValue("ProdutoBasePresilhas.zb4_cod"); 
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DescPres"), qtdPresilhas,
					unidade, codigo, getPrecofromObject(projeto.findGenericValue("PreUPresT")), ferramenta, BigDecimal.ZERO,
					projeto.findGenericValue("CubPre"),projeto.findGenericValue("QtdVPre"),projeto.findGenericValue("QtdPPre"),
					projeto.findGenericValue("QtdPPret"),projeto.findGenericValue("ProdutoBasePresilhas.zb4_alt"),projeto.findGenericValue("ProdutoBasePresilhas.zb4_larg")
					,projeto.findGenericValue("ProdutoBasePresilhas.zb4_pesbru"),projeto.findGenericValue("ProdutoBasePresilhas.zb4_peso"),projeto.findGenericValue("ProdutoBasePresilhas.zb4_qe"),
					projeto.findGenericValue("ProdutoBasePresilhas.zb4_grupo"),projeto.findGenericValue("ProdutoBasePresilhas.zb4_zcorprod"),projeto.findGenericValue("ProdutoBasePresilhas.zb4_cor")
					,projeto.findGenericValue("ProdutoBasePresilhas.zb4_posipi"),"","","",projeto.findGenericValue("ProdutoBasePresilhas.zb4_desces")));
			
		}
		
		// Suporte Coletor Flauta
		// Incluido por gecinei 29/0/2023
		
		if (validateLong(qtdSuporteflauta)) {
			String ferramenta = projeto.findGenericValue("ProdutoBaseFlauta.zb4_ferram");
			String codigo = projeto.findGenericValue("ProdutoBaseFlauta.zb4_cod"); 
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DescrSupFla"), qtdSuporteflauta,
					unidade,codigo, getPrecofromObject(projeto.findGenericValue("PreSupFlaT")), ferramenta, BigDecimal.ZERO,
					projeto.findGenericValue("CubSupFla"),projeto.findGenericValue("QtdVolSupFla"),projeto.findGenericValue("QtdPesoSufla"),
					projeto.findGenericValue("QtdPesoSul"),projeto.findGenericValue("ProdutoBaseFlauta.zb4_alt"),projeto.findGenericValue("ProdutoBaseFlauta.zb4_larg")
					,projeto.findGenericValue("ProdutoBaseFlauta.zb4_pesbru"),projeto.findGenericValue("ProdutoBaseFlauta.zb4_peso"),projeto.findGenericValue("ProdutoBaseFlauta.zb4_qe"),
					projeto.findGenericValue("ProdutoBaseFlauta.zb4_grupo"),projeto.findGenericValue("ProdutoBaseFlauta.zb4_zcorprod"),
					projeto.findGenericValue("ProdutoBaseFlauta.zb4_cor"),projeto.findGenericValue("ProdutoBasePresilhas.zb4_posipi"),"","",""
					,projeto.findGenericValue("ProdutoBasePresilhas.zb4_desces")));
			
			
		}

		
		projeto.setValue(listaDeItens, lista);
		projeto.setValue("vltotproj", totalProjeto);
		
		
		
		
		
	}
   // Incluido por Gecinei BigDecimal Cubagem, volume,pesobruto,pesoliquido,total item EXP
	private BigDecimal addItenToList(List<NeoObject> lista, String descricao, Long quantidade, String unidade,
			String codigo, BigDecimal preco, String ferramenta, BigDecimal tamanho,BigDecimal cubagem, Long volume,BigDecimal peso,
			BigDecimal pesoliquido,BigDecimal altura,BigDecimal Largura,BigDecimal pesobru,
			BigDecimal pesoliq,BigDecimal embalagem,String grupo,String codigocor,String cor,
			String ncm,String Lado,String passo,String Diametro,String descesp) {
		
		
		BigDecimal totalItem = BigDecimal.ZERO;
		BigDecimal totalItemEXP = BigDecimal.ZERO;
		

		if (quantidade != null && quantidade > 0) {

			System.out.println("Adicionando item: " + descricao);
			System.out.println("Ferramenta: " + ferramenta);
			System.out.println("Tamanho: " + tamanho);
			NeoObject itemObject = AdapterUtils.createNewEntityInstance("hidItemProj");
			EntityWrapper wrapper = new EntityWrapper(itemObject);

			wrapper.setValue("descricao", descricao);
			wrapper.setValue("quantidade", quantidade);
			wrapper.setValue("unidade", unidade);
			wrapper.setValue("CodItemP", codigo);
			wrapper.setValue("PreUniP", preco);
			wrapper.setValue("ValorAjustado", preco);
			wrapper.setValue("FeritemPr", ferramenta);
			wrapper.setValue("TamanPr", tamanho);
			wrapper.setValue("CubItemP", cubagem);
			wrapper.setValue("QtdVolIP", volume);
			wrapper.setValue("QtdPesItemP", peso);
			wrapper.setValue("QtdPesItemPL",pesoliquido);
			wrapper.setValue("Altura", altura);
			wrapper.setValue("Largura", Largura);
			wrapper.setValue("PesoBruto",pesobru);
			wrapper.setValue("PesoLiquido", pesoliq);
			wrapper.setValue("Embalagem", embalagem);
			wrapper.setValue("Grupo", grupo);
			wrapper.setValue("CodigoCor", codigocor);
			wrapper.setValue("DescricaoDaCor", cor);
			wrapper.setValue("Ncm", ncm);
			wrapper.setValue("Lado", Lado);
			wrapper.setValue("Passo", passo);
			wrapper.setValue("Diametro", Diametro);
			wrapper.setValue("DescricaoEspanhol", descesp);

			if (preco != null) {
				totalItem = preco.multiply(new BigDecimal(quantidade));
				wrapper.setValue("PreTotalP", totalItem);
				totalItemEXP = preco.multiply(new BigDecimal(quantidade));
				wrapper.setValue("PrecoTotalExp", totalItemEXP);
			}
			
			

			PersistEngine.persist(wrapper.getObject());

			lista.add(itemObject);
		}

		return totalItem;
	}
    
	
		
		
		
	
	private boolean validateLong(Long valor) {
		if (valor == null || valor < 1) {
			return false;
		}

		return true;
	}

	private BigDecimal getPrecofromObject(NeoObject tabelPreco) {
		try {
			EntityWrapper wrapper = new EntityWrapper(tabelPreco);

			BigDecimal valor = wrapper.findGenericValue("da1_prcven");
			if (NeoUtils.safeIsNotNull(valor)) {
				return valor;
			}
		} catch (Exception e) {

		}

		return BigDecimal.ZERO;
	}

}
