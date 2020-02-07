package br.com.jdt.jdtspringboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jdt.jdtspringboot.model.entity.Pessoa;
import br.com.jdt.jdtspringboot.repository.PessoaRepository;

@Service
public class PessoaService {
	
	private PessoaRepository pessoaRepository;
	
	@Autowired
	public PessoaService(PessoaRepository pessoaRepository) {
		this.pessoaRepository = pessoaRepository;
	}

	public Pessoa salvar(Pessoa pessoa) {
		return this.pessoaRepository.save(pessoa);
	}

	public List<Pessoa> listar() {
		return this.pessoaRepository.findAll();
	}

	public Optional<Pessoa> buscarPorId(Long id) {
		return this.pessoaRepository.findById(id);
	}

	public Pessoa editar(Long id, Pessoa pessoa) {
		pessoa.setId(id);
		return this.pessoaRepository.save(pessoa);
	}

	public void remover(Long id) {
		this.pessoaRepository.deleteById(id);
	}

	public List<Pessoa> buscaPorNome(String nome) {
		return this.pessoaRepository.findByNomeContainingOrSobrenomeContaining(nome, nome);
	}

	public List<Pessoa> buscaPorQualquerParametro(String query) {
		return this.pessoaRepository.buscaPorQualquerParametro(query);
	}
	
}
