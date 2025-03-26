package com.example.fsbr.process.controller;

import com.example.fsbr.process.dto.IbgeUF;
import com.example.fsbr.process.dto.ProcessoDto;
import com.example.fsbr.process.dto.ProcessoListDto;
import com.example.fsbr.process.service.IbgeService;
import com.example.fsbr.process.service.ProcessoService;
import com.example.fsbr.process.service.StorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/processos")
@RequiredArgsConstructor
public class ProcessoController {

    private final ProcessoService processoService;
    private final StorageService storageService;
    private final IbgeService ibgeService;

    @GetMapping
    public Page<ProcessoListDto> listarProcessos(Pageable pageable) {
        return processoService.listarProcessos(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcessoDto> buscarProcesso(@PathVariable Long id) {
        return ResponseEntity.ok(processoService.buscarProcessoPorId(id));
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ProcessoDto> criarProcesso(
            @RequestPart("processo") @Valid ProcessoDto processoDTO,
            @RequestPart("pdf") MultipartFile pdf) throws IOException {
        return new ResponseEntity<>(processoService.criarProcesso(processoDTO, pdf.getBytes(), pdf.getOriginalFilename()), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ProcessoDto> atualizarProcesso(
            @PathVariable Long id,
            @RequestPart("processo") @Valid ProcessoDto processoDTO,
            @RequestPart(value = "pdf", required = false) MultipartFile pdf) throws IOException {
        byte[] pdfBytes = (pdf != null && !pdf.isEmpty()) ? pdf.getBytes() : null;
        String filename = (pdf != null && !pdf.isEmpty()) ? pdf.getOriginalFilename() : null;
        return ResponseEntity.ok(processoService.atualizarProcesso(id, processoDTO, pdfBytes, filename));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProcesso(@PathVariable Long id) {
        processoService.deletarProcesso(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ufs")
    public List<IbgeUF> listarUfs() {
        return ibgeService.listarUfs();
    }

    @GetMapping("/municipios/{uf}")
    public List<String> listarMunicipiosPorUf(@PathVariable String uf) {
        return ibgeService.listarMunicipiosPorUf(uf);
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "Anexo: Nome do Arquivo = \"" + file.getFilename() + "\"")
                .body(file);
    }
}