package com.project.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Tabuleiro {
	
	private int linhas;
	private int colunas;
	private int minas;
	
	private final List<Campo> campos = new ArrayList<>();

	public Tabuleiro(int linhas, int colunas, int minas) {
		
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		gerarCampos();
		associarOsVizinhos();
		sortearMinas();
		
	}
	
	public void abrir(int linha, int coluna) {
		
		try {
				campos.parallelStream()
					  .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
					  .findFirst()
					  .ifPresent(c -> c.abrir());	
				
				//FIXME ajustar a implementacao
		} catch (Exception e) {
			
			campos.forEach(c -> c.setAberto(true));
			
			throw e;
		}
		
	}
	
	public void marcar(int linha, int coluna) {
		
		campos.parallelStream()
			  .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
			  .findFirst()
			  .ifPresent(c -> c.alterarMarcacao());		  
		
	}

	private void sortearMinas() {
		
		long minaArmadas = 0;
		
		Predicate<Campo> minado = c -> c.isMinado();
		
		do {
			int aleatorio = (int)(Math.random() * campos.size());
			
			campos.get(aleatorio).minar();
			minaArmadas = campos.stream().filter(minado).count();
			
		} while (minaArmadas < minas);
		
	}
	
	public boolean objetivoAlcancado() {
		
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}
	
	public void reiniciar() {
		
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
	}

 	private void associarOsVizinhos() {
		
		for(Campo c1: campos) {
			
			for(Campo c2: campos) {
				
				c1.adicionarVizinho(c2);
			}
		}
		
	}

	private void gerarCampos() {
		
		for(int l = 0; l < linhas; l++) {
			
			for(int c = 0; c < colunas; c++) {
				
				campos.add(new Campo(l, c));
				
			}
		}
		
	}

}