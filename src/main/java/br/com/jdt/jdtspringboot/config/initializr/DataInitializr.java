package br.com.jdt.jdtspringboot.config.initializr;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import br.com.jdt.jdtspringboot.model.entity.Pessoa;
import br.com.jdt.jdtspringboot.model.entity.Profissao;
import br.com.jdt.jdtspringboot.model.entity.Role;
import br.com.jdt.jdtspringboot.model.entity.Telefone;
import br.com.jdt.jdtspringboot.model.entity.Usuario;
import br.com.jdt.jdtspringboot.model.enums.SexoEnum;
import br.com.jdt.jdtspringboot.repository.PessoaRepository;
import br.com.jdt.jdtspringboot.repository.ProfissaoRepository;
import br.com.jdt.jdtspringboot.repository.RoleRepository;
import br.com.jdt.jdtspringboot.repository.TelefoneRepository;
import br.com.jdt.jdtspringboot.repository.UsuarioRepository;
import br.com.jdt.jdtspringboot.utils.BCryptUtils;

@Component
@Profile("dev")
public class DataInitializr implements ApplicationListener<ContextRefreshedEvent> {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private UsuarioRepository usuarioRepository;
	private RoleRepository roleRepository;
	private ProfissaoRepository profissaoRepository;
	private PessoaRepository pessoaRepository;
	private TelefoneRepository telefoneRepository;

	@Autowired
	public DataInitializr(UsuarioRepository usuarioRepository, RoleRepository roleRepository,
			ProfissaoRepository profissaoRepository, PessoaRepository pessoaRepository,
			TelefoneRepository telefoneRepository) {
		this.usuarioRepository = usuarioRepository;
		this.roleRepository = roleRepository;
		this.profissaoRepository = profissaoRepository;
		this.pessoaRepository = pessoaRepository;
		this.telefoneRepository = telefoneRepository;
	}



	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.info("Inserindo Roles");
		Role roleAdmin = new Role("ROLE_ADMIN");
		Role roleGerente = new Role("ROLE_GERENTE");
		Role roleOpCaixa = new Role("ROLE_OPCAIXA");
		List<Role> roles = Arrays.asList(roleAdmin, roleGerente, roleOpCaixa);
		roles.forEach(role -> {
			try {
				this.roleRepository.save(role);
			} catch (DataIntegrityViolationException e) {
			}
		});
		roles = roleRepository.findAll();
		for (Role role : roles) {
			if (role.equals(roleAdmin))
				roleAdmin = role;
			if (role.equals(roleGerente))
				roleGerente = role;
			if (role.equals(roleOpCaixa))
				roleOpCaixa = role;
		}

		log.info("Inserindo usuários");
		List<Usuario> usuarios = Arrays.asList(
				new Usuario("admin", BCryptUtils.criptografar("admin"), Arrays.asList(roleAdmin, roleGerente)),
				new Usuario("user01", BCryptUtils.criptografar("user01"), Arrays.asList(roleOpCaixa)));
		usuarios.forEach(usuario -> {
			try {
				this.usuarioRepository.save(usuario);
			} catch (DataIntegrityViolationException e) {
			}
		});
		List<Usuario> users = this.usuarioRepository.findAll();
		for (Usuario user : users) {
			if ("admin".equalsIgnoreCase(user.getUsername())) {
				user.setRoles(Arrays.asList(roleAdmin, roleGerente));
			} else if ("user01".equalsIgnoreCase(user.getUsername())) {
				user.setRoles(Arrays.asList(roleOpCaixa));
			}
		}

		log.info("Inserindo profissoes");
		List<Profissao> profissoes = Arrays.asList(new Profissao("Programador Java"),
				new Profissao("Programador Cobol"), new Profissao("Programador Python"),
				new Profissao("Pessoa Desenvolvedora"), new Profissao("Um milhão de uns"));
		profissoes.forEach(profissao -> {
			try {
				this.profissaoRepository.save(profissao);
			} catch (DataIntegrityViolationException e) {
			}
		});

		profissoes = this.profissaoRepository.findAll();
		
		log.info("Inserindo pessoas");
		List<Pessoa> pessoas = Arrays.asList(new Pessoa(1L, "Fernando", "Silva", SexoEnum.MASCULINO, profissoes.get(3)),
				new Pessoa(2L, "Miguel", "Josiah", SexoEnum.MASCULINO, profissoes.get(new Random().nextInt(5))),
				new Pessoa(3L, "Edrieli", "Silva", SexoEnum.FEMININO, profissoes.get(new Random().nextInt(5))),
				new Pessoa(4L, "Mirian", "Pereira", SexoEnum.FEMININO, profissoes.get(new Random().nextInt(5))),
				new Pessoa(5L, "Geovanna", "de Oliveira", SexoEnum.FEMININO, profissoes.get(new Random().nextInt(5))));
		pessoas.forEach(pessoa -> {
			try {
				this.pessoaRepository.save(pessoa);
			} catch (DataIntegrityViolationException e) {
			}
			Telefone telefone = new Telefone("1155555555", "celular", pessoa);
			this.telefoneRepository.save(telefone);
		});

	}

}
