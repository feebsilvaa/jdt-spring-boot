package br.com.jdt.jdtspringboot.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.jdt.jdtspringboot.service.ReportService;
import net.sf.jasperreports.engine.JRException;

@RequestMapping("/relatorio")
public class RelatorioController {
	
	private static final Logger log = LoggerFactory.getLogger(RelatorioController.class);
	
	@Autowired
	private ReportService reportService;

	@GetMapping("/v1/pessoas/pdf")
	public void exportarPessoasPdf(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			byte[] pdfReport = this.reportService.exportarPdfPessoas(request.getServletContext());
			String fileName = "export-pessoas-pdf";
			
			response.setContentType("application/force-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=%s.pdf", fileName));
			// copies all bytes from a file to an output stream
			InputStream is = new ByteArrayInputStream(pdfReport);
			FileCopyUtils.copy(is, response.getOutputStream());
			// flushes output stream
			response.getOutputStream().flush();
		} catch (JRException e) {
			log.error("Ocorreu um erro ao tentar extrair o relatório de pessoas - Erro: {}", e);
			response.setStatus(HttpServletResponse.SC_OK);
		}
	}

	@GetMapping("/v2/pessoas/pdf")
	public ResponseEntity<Resource> exportarPessoasPdfResource(HttpServletRequest request) throws IOException {
		try {
			byte[] pdfReport = this.reportService.exportarPdfPessoas(request.getServletContext());
			String fileName = "export-pessoas";
			Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "force-download"));
			headers.set("Content-Disposition", String.format("attachment; filename=%s-%s.pdf", fileName, timestamp));
			headers.setContentLength(pdfReport.length);
			return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(new ByteArrayResource(pdfReport));
		} catch (JRException e) {
			log.error("Ocorreu um erro ao tentar extrair o relatório de pessoas - Erro: {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
}
