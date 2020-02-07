package br.com.jdt.jdtspringboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.jdt.jdtspringboot.model.entity.Usuario;
import br.com.jdt.jdtspringboot.repository.UsuarioRepository;

@Service
public class UsuarioService  implements UserDetailsService {
	
	private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = this.usuarioRepository.findByUsername(username);
		log.info("Usuario encontrado: {}", usuario);
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário não foi encontrado");
		}
		return usuario;
	}

}
