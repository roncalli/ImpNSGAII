package modelo;

public class Maquina {
	private int id;
	private String nomeMaquina;
	private TipoMaquina tipoMaquina;
	private float capacidade;
	private float nivelConsumo; // 1 até 5, sendo 1 o mais baixo e 5 o mais elevado
	private float custo; // Esse custo seria custo/h da máquina
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNomeMaquina() {
		return nomeMaquina;
	}

	public void setNomeMaquina(String nomeMaquina) {
		this.nomeMaquina = nomeMaquina;
	}

	public TipoMaquina getTipoMaquina() {
		return tipoMaquina;
	}

	public void setTipoMaquina(TipoMaquina tipoMaquina) {
		this.tipoMaquina = tipoMaquina;
	}

	public float getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(float capacidade) {
		this.capacidade = capacidade;
	}

	public float getNivelConsumo() {
		return nivelConsumo;
	}

	public void setNivelConsumo(float nivelConsumo) {
		this.nivelConsumo = nivelConsumo;
	}

	public float getCusto() {
		return custo;
	}

	public void setCusto(float custo) {
		this.custo = custo;
	}

	public Maquina(int id, String nomeMaquina, TipoMaquina tipoMaquina) {
		super();
		this.id = id;
		this.nomeMaquina = nomeMaquina;
		this.tipoMaquina = tipoMaquina;
		this.capacidade = 8; // Definiï¿½ï¿½o padrï¿½o de 8 horas / dia por mï¿½quina;
		this.nivelConsumo = 1;
	}

	public Maquina(int id) {
		super();
		this.id = id;
		this.nomeMaquina = "Maquina " + id;
		this.tipoMaquina = TipoMaquina.COPIADORA;
		this.capacidade = 8;
		this.nivelConsumo = 1;
	}

	public Maquina(int id, float diasJanela) {
		super();
		this.id = id;
		this.nomeMaquina = "Maquina " + id;
		this.tipoMaquina = TipoMaquina.COPIADORA;
		this.capacidade = 8 * diasJanela;
		this.nivelConsumo = (float) (1 + (float) (Math.random() * (5))) / 5; // Calculo Normalizado
	}

}
