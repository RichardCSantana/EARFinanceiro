/**
 * 
 */
package br.com.earfinanceiro.entidades;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.earfinanceiro.exceptions.ErroCadastroException;

/**
 * @author Richard
 * 
 */
@Entity
@Table(name = "conta")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
@SequenceGenerator(name = AbstractConta.CONTA_SEQUENCE, sequenceName = AbstractConta.CONTA_SEQUENCE, initialValue = 1, allocationSize = 10)
public abstract class AbstractConta implements IConta {

	/**
	 * String que define a sequencia dessa entidade
	 */
	public static final String CONTA_SEQUENCE = "conta_sequence";

	protected Long id;
	protected String descricao;
	protected Calendar dataPrevisao;
	protected Calendar dataEfetivacao;
	protected Subgrupo subgrupo;
	protected boolean efetiva;
	protected double valor;
	protected int reincidencia;
	protected boolean reincidente;

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.earfinanceiro.entidades.IConta#efetiva(java.util.Calendar)
	 */
	@Override
	public void efetiva(Calendar dataEfetivacao) throws ErroCadastroException {
		if (dataEfetivacao.before(this.dataPrevisao)) {
			throw new ErroCadastroException(
					"Data de efetivacao deve ser posterior a data de cadastro");
		}
		if (this.efetiva) {
			throw new ErroCadastroException(
					"A conta que est�o tentando efetivar j� foi efetivada");
		}
		this.efetiva = true;
		this.dataEfetivacao = dataEfetivacao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.earfinanceiro.entidades.IConta#reincide(java.lang.Integer)
	 */
	@Override
	public void reincide(Integer reincidencia) throws ErroCadastroException {
		if (reincidencia < 1) {
			throw new ErroCadastroException(
					"O prazo de reincidencia deve ser no minimo 1 dia");
		}
		if (this.reincidente) {
			throw new ErroCadastroException(
					"A conta em quest�o j� foi marcada como reincidente");
		}
		this.reincidente = true;
		this.reincidencia = reincidencia;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractConta)) {
			return false;
		}
		AbstractConta other = (AbstractConta) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * Insere um valor
	 * 
	 * @param valor
	 *            - Double a ser inserida na conta
	 * @throws ErroCadastroException
	 *             - Quando o valor do Double for menor ou igual a 0
	 */
	public void setValor(Double valor) throws ErroCadastroException {
		if (valor.compareTo(0.0) < 1) {
			throw new ErroCadastroException("Valor deve ser maior que 0");
		}
		this.valor = valor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.earfinanceiro.entidades.IConta#getId()
	 */
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = CONTA_SEQUENCE)
	@Column(name = "id")
	public Long getId() {
		return this.id;
	}

	/**
	 * 
	 * Insere um id
	 * 
	 * @param id
	 *            - Long identificador da conta
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.earfinanceiro.entidades.IConta#getDescricao()
	 */
	@Override
	@Column(name = "descricao")
	@NotNull(message = "O campo descrição deve ser preenchido")
	@Size(min = 3, max = 40, message = "O campo descricao deve possuir entre 3 e 40 caracteres")
	public String getDescricao() {
		return this.descricao;
	}

	/**
	 * 
	 * Insere uma descrição
	 * 
	 * @param descricao
	 *            - String que servirá de descrição da conta
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.earfinanceiro.entidades.IConta#getDataCadastro()
	 */
	@Override
	@Column(name = "data_cadastro")
	@NotNull(message = "O campo data cadastro deve ser preenchido")
	public Calendar getDataPrevisao() {
		return this.dataPrevisao;
	}

	/**
	 * 
	 * Insere a data de previsão para pagamento da conta
	 * 
	 * @param dataPrevisao
	 *            - Calendar que define a data de previsão para o pagamento da
	 *            conta
	 */
	public void setDataPrevisao(Calendar dataPrevisao) {
		this.dataPrevisao = dataPrevisao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.earfinanceiro.entidades.IConta#getValor()
	 */
	@Override
	@Column(name = "valor")
	@NotNull(message = "O campo valor deve ser preenchido")
	public Double getValor() {
		return this.valor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.earfinanceiro.entidades.IConta#getDataEfetivacao()
	 */
	@Override
	@Column(name = "data_efetivacao")
	public Calendar getDataEfetivacao() {
		return this.dataEfetivacao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.earfinanceiro.entidades.IConta#isEfetiva()
	 */
	@Override
	@Column(name = "efetiva")
	public Boolean isEfetiva() {
		return this.efetiva;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.earfinanceiro.entidades.IConta#getSubGrupo()
	 */
	@Override
	@ManyToOne(targetEntity = Subgrupo.class, fetch = FetchType.EAGER)
	public Subgrupo getSubgrupo() {
		return this.subgrupo;
	}

	/**
	 * 
	 * Insere um subgrupo
	 * 
	 * @param subgrupo
	 *            - {@link Subgrupo} da conta
	 */
	public void setSubgrupo(Subgrupo subgrupo) {
		this.subgrupo = subgrupo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.earfinanceiro.entidades.IConta#getReincidencia()
	 */
	@Override
	@Column(name = "reincidencia")
	public Integer getReincidencia() {
		return this.reincidencia;
	}

	/**
	 * 
	 * Retorna se a conta é reincidente
	 * 
	 * @return boolean que define se a conta é reincidente
	 */
	@Column(name = "eh_reincidente")
	public boolean isReincidente() {
		return this.reincidente;
	}

	@SuppressWarnings("unused")
	private void setDataEfetivacao(Calendar dataEfetivacao) {
		this.dataEfetivacao = dataEfetivacao;
	}

	@SuppressWarnings("unused")
	private void setEfetiva(boolean efetiva) {
		this.efetiva = efetiva;
	}

	@SuppressWarnings("unused")
	private void setValor(double valor) {
		this.valor = valor;
	}

	@SuppressWarnings("unused")
	private void setReincidencia(int reincidencia) {
		this.reincidencia = reincidencia;
	}

	@SuppressWarnings("unused")
	private void setReincidente(boolean reincidente) {
		this.reincidente = reincidente;
	}

}