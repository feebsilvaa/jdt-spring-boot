package br.com.jdt.jdtspringboot.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "profissao")
public class Profissao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nome", unique = true)
	private String nomeProfissao;

	public Profissao() {
	}

	public Profissao(String nomeProfissao) {
		this.nomeProfissao = nomeProfissao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeProfissao() {
		return nomeProfissao;
	}

	public void setNomeProfissao(String nomeProfissao) {
		this.nomeProfissao = nomeProfissao;
	}

	@Override
	public String toString() {
		return "Profissao [id=" + id + ", nomeProfissao=" + nomeProfissao + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nomeProfissao == null) ? 0 : nomeProfissao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Profissao other = (Profissao) obj;
		if (nomeProfissao == null) {
			if (other.nomeProfissao != null)
				return false;
		} else if (!nomeProfissao.equals(other.nomeProfissao))
			return false;
		return true;
	}

}
