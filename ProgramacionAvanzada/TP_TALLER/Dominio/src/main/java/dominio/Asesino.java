package dominio;

/**
 *
 * Clase que define a un tipo Asesino, la misma hereda de la clase abstracta
 * Casta por lo que se definen los métodos abstractos declarados en Casta y se
 * define al nombreCasta como "Asesino".
 *
 */
public class Asesino extends Casta {

	private static final int ENERGIANECESARIA = 10;
	/**
	 * <h3>Constructor de Asesino</h3>
	 * @param probCrit
	 *            Probabilidad de critico
	 * @param evasion
	 *            Evacion del personaje
	 * @param danioCrit
	 *            Cantidad de danio critico
	 */
	public Asesino(final double probCrit, final double evasion, final double danioCrit) {
		super(probCrit, evasion, danioCrit);
		this.nombreCasta = "Asesino";
	}

	/**
	 * <h3>Constructor de Asesino</h3>
	 */
	public Asesino() {
		super();
		this.nombreCasta = "Asesino";
		habilidadesCasta = new String[CANT_HABILIDADES];
		habilidadesCasta[HABILIDAD_1] = "Golpe Critico";
		habilidadesCasta[HABILIDAD_2] = "Aumentar Evasion";
		habilidadesCasta[HABILIDAD_3] = "Robar";
	}

	// Golpe Crítico
	/**
	 * <h3><u>Golpe Critico</u></h3> Implementacion del método abstracto
	 * habilidad1 declarado en la clase abstracta Casta.
	 * <p>
	 * En este caso el método es el encargado de producir un Golpe Critico por
	 * parte de un asesino hacia un objeto Peleable.<br>
	 * Nótese que en este caso el Personaje (Asesino) que ataca es parámetro.
	 * @param caster
	 *            De tipo Personaje (Asesino) encargado de obtener y setear
	 *            energías.
	 * @param atacado
	 *            De tipo Peleable a ser atacado por el Asesino.
	 * @return Si el caster tiene suficiente energia para realizar el ataque
	 *         devuelve un boolean true, caso contrario devuelve false.
	 */
	public boolean habilidad1(final Personaje caster, final Peleable atacado) {
		if (caster.getEnergia() > ENERGIANECESARIA) {
			caster.serDesenergizado(ENERGIANECESARIA);
			if (atacado.serAtacado((int) (caster.ataque * caster.getCasta().getDanioCritico())) > 0) {
				return true;
			}
		}
		return false;
	}

	// Aumentar Evasion
	/**
	 * <h3><u>Aumentar Evasión</u></h3> Se redefine el método abstracto
	 * habilidad2 heredado.
	 * <p>
	 * El objetivo de dicho método es el de aumentar la probabilidad de evasión
	 * de los personajes pertenecientes a dicha casta. (Asesinos)
	 *
	 * @param caster
	 *            De tipo Personaje encargado de obtener y setear energías.
	 * @param atacado
	 *            De tipo Peleable (no se utiliza)
	 * @return boolean, true en caso de poder aumentar la evasion, false en caso
	 *         contrario.
	 */
	public boolean habilidad2(final Personaje caster, final Peleable atacado) {
		if (caster.getEnergia() > ENERGIANECESARIA) {
			caster.serDesenergizado(ENERGIANECESARIA);
			if (this.getProbabilidadEvitarDanio() + 0.15 < 0.5) {
				this.probabilidadEvitarDanio += 0.15;
			} else {
				this.probabilidadEvitarDanio = 0.5;
			}
			return true;
		}
		return false;
	}

	// Robar
	/**
	 * <h3>Método habilidad3</h3> Robar
	 *
	 * @param caster
	 *            De tipo Personaje encargado de obtener y setear energías.
	 * @param atacado
	 *            De tipo Peleable (no se utiliza)
	 * @return boolean
	 */
	public boolean habilidad3(final Personaje caster, final Peleable atacado) {
		return false;
	}

	@Override
	public int getBonusFuerza() {
		return 0;
	}

	@Override
	public int getBonusInteligencia() {
		return 0;
	}

	@Override
	public int getBonusDestreza() {
		return 5;
	}
}
