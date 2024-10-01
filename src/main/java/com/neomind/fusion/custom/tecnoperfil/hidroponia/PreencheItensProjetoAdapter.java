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
				
				
				

				List<NeoObject> itens = projetoWrapper.findGenericValue(listaDeItens);
				NeoObject itemObject = AdapterUtils.createNewEntityInstance("hidItemProj");
				String ferramenta = arg1.findGenericValue("FerraAdesiv");
				
				
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
					projeto.findGenericValue("QtdPLAB1L"),projeto.findGenericValue("PMKLAB1.b5_altura"),projeto.findGenericValue("PMKLAB1.b5_larg")
					,projeto.findGenericValue("PesoBarras.b1_pesbru"),projeto.findGenericValue("PesoBarras.b1_peso"),projeto.findGenericValue("PesoBarras.b1_qe")));
			
		}

		// Barra1B
		// Incluido POr Gecinei CubLBB1,QtdVLBB1,QtdPLBB1
		if (validateLong(qtdBarra1LadoB)) {
			BigDecimal tamanho = projeto.findGenericValue("Tmbarr1");
			String codigo = "0";
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DesPeLBB1"), qtdBarra1LadoB,
					unidade, codigo, projeto.findGenericValue("PreLaBB1"), ferramentaBarra, tamanho,
					projeto.findGenericValue("CubLBB1"),projeto.findGenericValue("QtdVLBB1"),projeto.findGenericValue("QtdPLBB1"),
					projeto.findGenericValue("QtdPLBB1L"),projeto.findGenericValue("PMKLAB1.b5_altura"),projeto.findGenericValue("PMKLAB1.b5_larg")
					,projeto.findGenericValue("PesoBarras.b1_pesbru"),projeto.findGenericValue("PesoBarras.b1_peso"),projeto.findGenericValue("PesoBarras.b1_qe")));
			
		}
		
		

		// Barra2A
		// Incluido POr Gecinei CubLAB2,QtdVLAB2,QtdPLAB2
		if (validateLong(qtdBarra2LadoA)) {
			BigDecimal tamanho = projeto.findGenericValue("Tmbarr2");
			String codigo = "0";
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DesPELAB2"), qtdBarra2LadoA,
					unidade, codigo, projeto.findGenericValue("PreLaAB2"), ferramentaBarra, tamanho,
					projeto.findGenericValue("CubLAB2"),projeto.findGenericValue("QtdVLAB2"),projeto.findGenericValue("QtdPLAB2")
					,projeto.findGenericValue("QtdPLAB2l"),projeto.findGenericValue("PMKLAB1.b5_altura"),projeto.findGenericValue("PMKLAB1.b5_larg")
					,projeto.findGenericValue("PesoBarras.b1_pesbru"),projeto.findGenericValue("PesoBarras.b1_peso"),projeto.findGenericValue("PesoBarras.b1_qe")));
			
		}

		// Barra2B
		// Incluido POr Gecinei CubLBB2,QtdVLBB2,QtdPLBB2
		
		if (validateLong(qtdBarra2LadoB)) {
			BigDecimal tamanho = projeto.findGenericValue("Tmbarr2");
			String codigo = "0";
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DesPeLaBB2"), qtdBarra2LadoB,
					unidade, codigo, projeto.findGenericValue("PreLaBB2"), ferramentaBarra, tamanho,
					projeto.findGenericValue("CubLBB2"),projeto.findGenericValue("QtdVLBB2"),projeto.findGenericValue("QtdPLBB2"),
					projeto.findGenericValue("QtdPLBB2L"),projeto.findGenericValue("PMKLAB1.b5_altura"),projeto.findGenericValue("PMKLAB1.b5_larg")
					,projeto.findGenericValue("PesoBarras.b1_pesbru"),projeto.findGenericValue("PesoBarras.b1_peso"),projeto.findGenericValue("PesoBarras.b1_qe")));
			
			
		}

		// Barra3A
		// Incluido POr Gecinei CubLAB3,QtdVLAB3,QtdPLAB3
		if (validateLong(qtdBarra3LadoA)) {
			BigDecimal tamanho = projeto.findGenericValue("Tmbarr3");
			String codigo = "0";

			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DesPeLAB3"), qtdBarra3LadoA,
					unidade, codigo, projeto.findGenericValue("PreLAB3"), ferramentaBarra, tamanho,
					projeto.findGenericValue("CubLAB3"),projeto.findGenericValue("QtdVLAB3"),projeto.findGenericValue("QtdPLAB3"),
					projeto.findGenericValue("QtdPLAB3L"),projeto.findGenericValue("PMKLAB1.b5_altura"),projeto.findGenericValue("PMKLAB1.b5_larg")
					,projeto.findGenericValue("PesoBarras.b1_pesbru"),projeto.findGenericValue("PesoBarras.b1_peso"),projeto.findGenericValue("PesoBarras.b1_qe")));
			
		}

		// Barra3B
		// Incluido POr Gecinei CubLBB3,QtdVLBB3,QtdPLBB3
		if (validateLong(qtdBarra3LadoB)) {
			BigDecimal tamanho = projeto.findGenericValue("Tmbarr3");
			String codigo = "0";
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DesPELBB3"), qtdBarra3LadoB,
					unidade, codigo, projeto.findGenericValue("PreLaBB3"), ferramentaBarra, tamanho,
					projeto.findGenericValue("CubLBB3"),projeto.findGenericValue("QtdVLBB3"),projeto.findGenericValue("QtdPLBB3"),
					projeto.findGenericValue("QtdPLBB3L"),projeto.findGenericValue("PMKLAB1.b5_altura"),projeto.findGenericValue("PMKLAB1.b5_larg")
					,projeto.findGenericValue("PesoBarras.b1_pesbru"),projeto.findGenericValue("PesoBarras.b1_peso"),projeto.findGenericValue("PesoBarras.b1_qe")));
			
		}

		// Barra4A
		// Incluido POr Gecinei CubLAB4,QtdVLAB4,QtdPLAB4
		if (validateLong(qtdBarra4LadoA)) {
			BigDecimal tamanho = projeto.findGenericValue("Tmbarr4");
			String codigo = "0";
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DesPELAB4"), qtdBarra4LadoA,
					unidade,codigo, projeto.findGenericValue("PreLaAB4"), ferramentaBarra, tamanho,
					projeto.findGenericValue("CubLAB4"),projeto.findGenericValue("QtdVLAB4"),projeto.findGenericValue("QtdPLAB4"),
					projeto.findGenericValue("QtdPLB4L"),projeto.findGenericValue("PMKLAB1.b5_altura"),projeto.findGenericValue("PMKLAB1.b5_larg")
					,projeto.findGenericValue("PesoBarras.b1_pesbru"),projeto.findGenericValue("PesoBarras.b1_peso"),projeto.findGenericValue("PesoBarras.b1_qe")));
			
		}

		// Barra4B
		// Incluido POr Gecinei CubLBB4,QtdVLBB4,QtdPLBB4
		if (validateLong(qtdBarra4LadoB)) {
			BigDecimal tamanho = projeto.findGenericValue("Tmbarr4");
			String codigo = "0";
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DesPELBB4"), qtdBarra4LadoB,
					unidade,codigo, projeto.findGenericValue("PreLaBB4"), ferramentaBarra, tamanho,
					projeto.findGenericValue("CubLBB4"),projeto.findGenericValue("QtdVLBB4"),projeto.findGenericValue("QtdPLBB4"),
					projeto.findGenericValue("QtdPLBB4L"),projeto.findGenericValue("PMKLAB1.b5_altura"),projeto.findGenericValue("PMKLAB1.b5_larg")
					,projeto.findGenericValue("PesoBarras.b1_pesbru"),projeto.findGenericValue("PesoBarras.b1_peso"),projeto.findGenericValue("PesoBarras.b1_qe")));
			
		}

		// Barra5A
		// Incluido POr Gecinei CubLAB5,QtdVLAB5,QtdPLAB5
		if (validateLong(qtdBarra5LadoA)) {
			BigDecimal tamanho = projeto.findGenericValue("Tmbarr5");
			String codigo = "0";
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DesPELAB5"), qtdBarra5LadoA,
					unidade,codigo, projeto.findGenericValue("PreLaAB5"), ferramentaBarra, tamanho,
					projeto.findGenericValue("CubLAB5"),projeto.findGenericValue("QtdVLAB5"),projeto.findGenericValue("QtdPLAB5"),
					projeto.findGenericValue("QtdPLAB5L"),projeto.findGenericValue("PMKLAB1.b5_altura"),projeto.findGenericValue("PMKLAB1.b5_larg")
					,projeto.findGenericValue("PesoBarras.b1_pesbru"),projeto.findGenericValue("PesoBarras.b1_peso"),projeto.findGenericValue("PesoBarras.b1_qe")));
		}

		// Barra5B
		// Incluido POr Gecinei CubLBB5,QtdVLBB5,QtdPLBB5
		if (validateLong(qtdBarra5LadoB)) {
			BigDecimal tamanho = projeto.findGenericValue("Tmbarr5");
			String codigo = "0";
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DesPeLBB5"), qtdBarra5LadoB,
					unidade, codigo, projeto.findGenericValue("PreLaBB5"), ferramentaBarra, tamanho,
					projeto.findGenericValue("CubLBB5"),projeto.findGenericValue("QtdVLBB5"),projeto.findGenericValue("QtdPLBB5"),
					projeto.findGenericValue("QtdPLBB5l"),projeto.findGenericValue("PMKLAB1.b5_altura"),projeto.findGenericValue("PMKLAB1.b5_larg")
					,projeto.findGenericValue("PesoBarras.b1_pesbru"),projeto.findGenericValue("PesoBarras.b1_peso"),projeto.findGenericValue("PesoBarras.b1_qe")));
			
		}

		// Tampa Entrada
		// Incluido POr Gecinei CubTAE,QtdVTE,QtdPTE
		if (validateLong(qtdTampaEntrada)) {
			String ferramenta = projeto.findGenericValue("FerTENT.b1_zferram");
			String codigo = projeto.findGenericValue("FerTENT.b1_cod");
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DescTE"), qtdTampaEntrada,
					unidade, codigo, getPrecofromObject(projeto.findGenericValue("PreTaEnT")), ferramenta, BigDecimal.ZERO,
					projeto.findGenericValue("CubTAE"),projeto.findGenericValue("QtdVTE"),projeto.findGenericValue("QtdPTE"),
					projeto.findGenericValue("QtdPTEL"),projeto.findGenericValue("PMKTamEnt.b5_altura"),projeto.findGenericValue("PMKTamEnt.b5_larg")
					,projeto.findGenericValue("FerTENT.b1_pesbru"),projeto.findGenericValue("FerTENT.b1_peso"),projeto.findGenericValue("FerTENT.b1_qe")));
			
		}

		// Tampa Saida
		// Incluido POr Gecinei CubTAS,QtdVTS,QtdPTS 
		if (validateLong(qtdTampaSaida)) {
			String ferramenta = projeto.findGenericValue("FerramentaTampaSaida.b1_zferram");
			String codigo = projeto.findGenericValue("FerramentaTampaSaida.b1_cod");

			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DescTS"), qtdTampaSaida,
					unidade,codigo, getPrecofromObject(projeto.findGenericValue("PreTASAT")), ferramenta, BigDecimal.ZERO,
					projeto.findGenericValue("CubTAS"),projeto.findGenericValue("QtdVTS"),projeto.findGenericValue("QtdPTS"),
					projeto.findGenericValue("QtdPTSL"),projeto.findGenericValue("PMKTaS.b5_altura"),projeto.findGenericValue("PMKTaS.b5_larg")
					,projeto.findGenericValue("FerramentaTampaSaida.b1_pesbru"),projeto.findGenericValue("FerramentaTampaSaida.b1_peso")
					,projeto.findGenericValue("FerramentaTampaSaida.b1_qe")));
			
		}
		// Emenda
		// Incluido POr Gecinei CubEme,QtdVEme,QtdPEme
		
		if (validateLong(qtdEmenda)) {
			String ferramenta = projeto.findGenericValue("FerEmT.b1_zferram");
			String codigo = projeto.findGenericValue("FerEmT.b1_cod");
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DescEmen"), qtdEmenda,
					unidade,codigo, getPrecofromObject(projeto.findGenericValue("PreEmT")), ferramenta, BigDecimal.ZERO,
					projeto.findGenericValue("CubEme"),projeto.findGenericValue("QtdVEme"),projeto.findGenericValue("QtdPEme")
					,projeto.findGenericValue("QtdPEmel"),projeto.findGenericValue("PMKEMT.b5_altura"),projeto.findGenericValue("PMKEMT.b5_larg")
					,projeto.findGenericValue("FerEmT.b1_pesbru"),projeto.findGenericValue("FerEmT.b1_peso"),projeto.findGenericValue("FerEmT.b1_qe")));
			
			
		}

		// Coletor Antigo
		// Incluido POr Gecinei CubCOA,QtdVCOA,QtdPeCOA
		if (validateLong(qtdcoletorAntigo)) {
			String ferramenta = projeto.findGenericValue("fercolantT.b1_zferram");
			String codigo = "0";
			
			BigDecimal tamanho = projeto.findGenericValue("LargHi");
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DescPeCo"), qtdcoletorAntigo,
					unidade,codigo, projeto.findGenericValue("PrColAnt"), ferramenta, tamanho,
					projeto.findGenericValue("CubCOA"),projeto.findGenericValue("QtdVCOA"),projeto.findGenericValue("QtdPeCOA"),
					projeto.findGenericValue("QtdPeCOAl"),projeto.findGenericValue("PMKCoAT.b5_altura"),projeto.findGenericValue("PMKCoAT.b5_larg")
					,projeto.findGenericValue("fercolantT.b1_pesbru"),projeto.findGenericValue("fercolantT.b1_peso"),projeto.findGenericValue("fercolantT.b1_qe")));
			
		}

		// Coletor Novo
		// Incluido POr Gecinei CubCON, QtdVCON,QtdPCON
		if (validateLong(qtdcoletorNovo)) {
			String ferramenta = projeto.findGenericValue("FerColNT.b1_zferram");
			String codigo = "0";
			BigDecimal tamanho = projeto.findGenericValue("LargHi");
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("descrPeCoN"), qtdcoletorNovo,
					unidade,codigo, projeto.findGenericValue("PreUColN"), ferramenta, tamanho,
					projeto.findGenericValue("CubCON"),projeto.findGenericValue("QtdVCON"),projeto.findGenericValue("QtdPCON"),
					projeto.findGenericValue("QtdPCONL"),projeto.findGenericValue("PMKColNT.b5_altura"),projeto.findGenericValue("PMKColNT.b5_larg")
					,projeto.findGenericValue("FerColNT.b1_pesbru"),projeto.findGenericValue("FerColNT.b1_peso"),projeto.findGenericValue("FerColNT.b1_qe")));
			
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
					projeto.findGenericValue("QtdPTRNT"),projeto.findGenericValue("PMKTrnT.b5_altura"),projeto.findGenericValue("PMKTrnT.b5_larg")
					,projeto.findGenericValue("FerTravnoT.b1_pesbru"),projeto.findGenericValue("FerTravnoT.b1_peso")
					,projeto.findGenericValue("FerTravnoT.b1_qe")));
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
							,projeto.findGenericValue("QtdPTRAL"),projeto.findGenericValue("PMKTrAT.b5_altura"),projeto.findGenericValue("PMKTrAT.b5_larg")
							,projeto.findGenericValue("FerTravveT.b1_pesbru"),projeto.findGenericValue("FerTravveT.b1_peso")
							,projeto.findGenericValue("FerTravveT.b1_qe")));
			
		}

		// Coluna Nova
		// Incluido POr Gecinei CubColN ,QtdVColN,QtdPColN
		if (validateLong(qtdColunaNova)) {
			String ferramenta = projeto.findGenericValue("FerConoT.b1_zferram");
			String codigo = "0";
			BigDecimal tamanho = projeto.findGenericValue("Coluna");
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DescrPerCCN"), qtdColunaNova,
					unidade,codigo, projeto.findGenericValue("PreUColNo"), ferramenta, tamanho,
					projeto.findGenericValue("CubColN"),projeto.findGenericValue("QtdVColN"),
					projeto.findGenericValue("QtdPColN"),projeto.findGenericValue("QtdPColNT"),projeto.findGenericValue("PMKColnoT.b5_altura"),projeto.findGenericValue("PMKColnoT.b5_larg")
					,projeto.findGenericValue("FerConoT.b1_pesbru"),projeto.findGenericValue("FerConoT.b1_peso")
					,projeto.findGenericValue("FerConoT.b1_qe")));
			
			
		}

		// Coluna Antiga
		// Incluido POr Gecinei CubColA,QtdVColA,QtdPColA
		if (validateLong(qtdColunaAntiga)) {
			String ferramenta = projeto.findGenericValue("FerCoAnT.b1_zferram");
			String codigo = "0";
			BigDecimal tamanho = projeto.findGenericValue("Coluna");
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DescPCCA"), qtdColunaAntiga,
					unidade,codigo, projeto.findGenericValue("PreUClANt"), ferramenta, tamanho,
					projeto.findGenericValue("CubColA"),projeto.findGenericValue("QtdVColA"),projeto.findGenericValue("QtdPColA"),
					projeto.findGenericValue("QtdPColAT"),projeto.findGenericValue("PMKColAntT.b5_altura"),projeto.findGenericValue("PMKColAntT.b5_larg")
					,projeto.findGenericValue("FerCoAnT.b1_pesbru"),projeto.findGenericValue("FerCoAnT.b1_peso"),projeto.findGenericValue("FerCoAnT.b1_qe")));
			
		}

		// Suporte Coletor
		// Incluido POr Gecinei CubSuC,QtdVSC,QtdPSC
		
		if (validateLong(qtdSuporteColetor)) {
			String ferramenta = projeto.findGenericValue("FerSuCol");
			String codigo = projeto.findGenericValue("PreuniSCT.da1_codpro"); 
			totalProjeto = totalProjeto
					.add(addItenToList(lista, projeto.findGenericValue("DescSuCOl"), qtdSuporteColetor, unidade,codigo,
							getPrecofromObject(projeto.findGenericValue("PreuniSCT")), ferramenta, BigDecimal.ZERO,
							projeto.findGenericValue("CubSuC"),projeto.findGenericValue("QtdVSC"),projeto.findGenericValue("QtdPSC"),
							projeto.findGenericValue("QtdPSCT"),projeto.findGenericValue("PMKSupCT.b5_altura"),projeto.findGenericValue("PMKSupCT.b5_larg")
							,projeto.findGenericValue("FerSuColT.b1_pesbru"),projeto.findGenericValue("FerSuColT.b1_peso")
							,projeto.findGenericValue("FerSuColT.b1_qe")));
			
			
		}

		// Injetor
		// Incluido POr Gecinei CubINJ,QtdVINJ,QtdPINJ
		if (validateLong(qtdInjetor)) {
			String ferramenta = projeto.findGenericValue("PreUNllnHt.b1_zferram");
			String codigo = projeto.findGenericValue("PreUNllnHt.b1_cod"); 
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DescInjet"), qtdInjetor,
					unidade,codigo, getPrecofromObject(projeto.findGenericValue("PreUNIlnHT")), ferramenta, BigDecimal.ZERO,
					projeto.findGenericValue("CubINJ"),projeto.findGenericValue("QtdVINJ"),projeto.findGenericValue("QtdPINJ"),
					projeto.findGenericValue("QtdPINJT"),projeto.findGenericValue("PMKINjT.b5_altura"),projeto.findGenericValue("PMKINjT.b5_larg")
					,projeto.findGenericValue("PreUNllnHt.b1_pesbru"),projeto.findGenericValue("PreUNllnHt.b1_peso")
					,projeto.findGenericValue("PreUNllnHt.b1_qe")));
			
		}

		// Presilhas
		// Incluido POr Gecinei CubPre,QtdVPre,QtdPPre
		if (validateLong(qtdPresilhas)) {
			String ferramenta = projeto.findGenericValue("FerrPrest.b1_zferram");
			String codigo = projeto.findGenericValue("FerrPrest.b1_cod"); 
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DescPres"), qtdPresilhas,
					unidade, codigo, getPrecofromObject(projeto.findGenericValue("PreUPresT")), ferramenta, BigDecimal.ZERO,
					projeto.findGenericValue("CubPre"),projeto.findGenericValue("QtdVPre"),projeto.findGenericValue("QtdPPre"),
					projeto.findGenericValue("QtdPPret"),projeto.findGenericValue("PMKpreT.b5_altura"),projeto.findGenericValue("PMKpreT.b5_larg")
					,projeto.findGenericValue("FerrPrest.b1_pesbru"),projeto.findGenericValue("FerrPrest.b1_peso")
					,projeto.findGenericValue("FerrPrest.b1_qe")));
			
			
		}
		
		// Suporte Coletor Flauta
		// Incluido por gecinei 29/0/2023
		
		if (validateLong(qtdSuporteflauta)) {
			String ferramenta = projeto.findGenericValue("FerSupFlat.b1_zferram");
			String codigo = projeto.findGenericValue("FerSupFlat.b1_cod"); 
			totalProjeto = totalProjeto.add(addItenToList(lista, projeto.findGenericValue("DescrSupFla"), qtdSuporteflauta,
					unidade,codigo, getPrecofromObject(projeto.findGenericValue("PreSupFlaT")), ferramenta, BigDecimal.ZERO,
					projeto.findGenericValue("CubSupFla"),projeto.findGenericValue("QtdVolSupFla"),projeto.findGenericValue("QtdPesoSufla"),
					projeto.findGenericValue("QtdPesoSul"),projeto.findGenericValue("PMKSupFlauta.b5_altura"),projeto.findGenericValue("PMKSupFlauta.b5_larg")
					,projeto.findGenericValue("FerSupFlat.b1_pesbru"),projeto.findGenericValue("FerSupFlat.b1_peso")
					,projeto.findGenericValue("FerSupFlat.b1_qe")));
			
			
		}

		
		projeto.setValue(listaDeItens, lista);
		projeto.setValue("vltotproj", totalProjeto);
		
		
		
		
		
	}
   // Incluido por Gecinei BigDecimal Cubagem, volume,pesobruto,pesoliquido,total item EXP
	private BigDecimal addItenToList(List<NeoObject> lista, String descricao, Long quantidade, String unidade,
			String codigo, BigDecimal preco, String ferramenta, BigDecimal tamanho,BigDecimal cubagem, Long volume,BigDecimal peso,
			BigDecimal pesoliquido,BigDecimal altura,BigDecimal Largura, BigDecimal pesobru,BigDecimal pesoliq,BigDecimal embalagem) {
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
			wrapper.setValue("PesoLiquido", pesobru);
			wrapper.setValue("PesoBruto", pesoliq);
			wrapper.setValue("Embalagem", embalagem);
			

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
