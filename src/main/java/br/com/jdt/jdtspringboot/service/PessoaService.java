package br.com.jdt.jdtspringboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.jdt.jdtspringboot.model.entity.Pessoa;
import br.com.jdt.jdtspringboot.repository.PessoaRepository;
import br.com.jdt.jdtspringboot.repository.TelefoneRepository;

@Service
public class PessoaService {

	@Value("${spring.profiles.active}")
	String profile;

	private PessoaRepository pessoaRepository;
	private TelefoneRepository telefoneRepository;

	@Autowired
	public PessoaService(PessoaRepository pessoaRepository, TelefoneRepository telefoneRepository) {
		this.pessoaRepository = pessoaRepository;
		this.telefoneRepository = telefoneRepository;
	}

	public Pessoa salvar(Pessoa pessoa) {
		if ("prod".equalsIgnoreCase(profile))
			this.removerUmaPessoa();
		return this.pessoaRepository.save(pessoa);
	}

	public List<Pessoa> listar() {
		return this.pessoaRepository.listarOrdenadoPorId();
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

	private void removerUmaPessoa() {
		List<Pessoa> pessoas = this.listar();
		pessoas.get(0).getTelefones().forEach(telefone -> this.telefoneRepository.deleteById(telefone.getId()));
		this.remover(pessoas.get(0).getId());
	}

}
