package br.com.jdt.jdtspringboot.model.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.jdt.jdtspringboot.model.enums.SexoEnum;

@Entity
@Table(name = "pessoa")
public class Pessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "Nome da pessoa não pode ser vazio.")
	private String nome;

	@NotEmpty(message = "Sobrenome da pessoa não pode ser vazio.")
	private String sobrenome;

	@OneToMany(mappedBy = "pessoa")
	private List<Telefone> telefones;

	@NotNull(message = "Selecione o sexo.")
	@Enumerated(EnumType.STRING)
	private SexoEnum sexo;

	@ManyToOne(fetch = FetchType.EAGER)
	private Profissao profissao;

	public Pessoa() {
	}

	public Pessoa(String nome, String sobrenome, List<Telefone> telefones, SexoEnum sexo) {
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.telefones = telefones;
		this.sexo = sexo;
	}

	public Pessoa(Long id, String nome, String sobrenome, SexoEnum sexo, Profissao profissao) {
		this.id = id;
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.sexo = sexo;
		this.profissao = profissao;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public SexoEnum getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = SexoEnum.valueOf(sexo);
	}

	public Profissao getProfissao() {
		return profissao;
	}

	public void setProfissao(Profissao profissao) {
		this.profissao = profissao;
	}

	@Override
	public String toString() {
		return "Pessoa [id=" + id + ", nome=" + nome + ", sobrenome=" + sobrenome + ", telefones=" + telefones
				+ ", sexo=" + sexo + ", profissao=" + profissao + "]";
	}

}
