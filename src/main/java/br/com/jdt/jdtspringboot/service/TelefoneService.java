package br.com.jdt.jdtspringboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.jdt.jdtspringboot.model.entity.Pessoa;
import br.com.jdt.jdtspringboot.model.entity.Telefone;
import br.com.jdt.jdtspringboot.repository.TelefoneRepository;

@Service
public class TelefoneService {

	@Value("${spring.profiles.active}")
	String profile;

	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private TelefoneRepository telefoneRepository;

	public void adicionarTelefone(Telefone telefone, Long id) {
		Pessoa pessoa = this.pessoaService.buscarPorId(id).get();
		if ("prod".equalsIgnoreCase(profile))
			this.removerTelefonesPessoa(pessoa);
		telefone.setPessoa(pessoa);
		this.telefoneRepository.save(telefone);
	}

	private void removerTelefonesPessoa(Pessoa pessoa) {
		pessoa.getTelefones().forEach(tel -> {
			if (tel != null)
				this.removerTelefone(tel.getId());
		});
	}

	public List<Telefone> listarPorPessoa(Long idPessoa) {
		Pessoa pessoa = this.pessoaService.buscarPorId(idPessoa).get();
		return this.telefoneRepository.findByPessoa(pessoa);
	}

	public void removerTelefone(Long id) {
		this.telefoneRepository.deleteById(id);
	}

}
