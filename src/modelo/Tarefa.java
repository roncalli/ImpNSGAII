package modelo;

public class Tarefa {
	private int id;
	private String descricao;
	private float tempoInicio;
	private float dataEntrega; // em horas
	private int prioridade;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}

	public float getTempoInicio() {
		return tempoInicio;
	}

	public void setTempoInicio(float tempoInicio) {
		this.tempoInicio = tempoInicio;
	}

	public float getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(float dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public Tarefa(int id, String descricao, int prioridade) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.prioridade = prioridade;
		this.tempoInicio = 0; // Tempo padr�o;
	}

	public Tarefa(int id) {
		super();
		this.id = id;
		this.descricao = "Tarefa " + id;
		this.prioridade = 1; // prioridade 1 - Padr�o enquanto n�o trabalhamos com a medida de prioridade.
		this.tempoInicio = 0; // Tempo padr�o;
	}

	public Tarefa(int id, float tempoInicio, float dataEntrega) {
		super();
		this.id = id;
		this.descricao = "Tarefa " + id;
		this.prioridade = 1; // prioridade 1 - Padr�o enquanto n�o trabalhamos com a medida de prioridade.
		this.dataEntrega = dataEntrega;
		this.tempoInicio = tempoInicio;
	}

	public Tarefa(int id, int prioridade, float dataEntrega, boolean ils) {
		super();
		this.id = id;
		this.descricao = "Tarefa " + id;
		this.prioridade = prioridade; // prioridade 1 - Padr�o enquanto n�o trabalhamos com a medida de prioridade.
		this.dataEntrega = dataEntrega;
		this.tempoInicio = 0;
	}

}
