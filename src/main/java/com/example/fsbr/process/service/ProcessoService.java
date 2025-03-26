package com.example.fsbr.process.service;

import com.example.fsbr.process.dto.ProcessoDto;
import com.example.fsbr.process.dto.ProcessoListDto;
import com.example.fsbr.process.entity.Processo;
import com.example.fsbr.process.exception.ResourceNotFoundException;
import com.example.fsbr.process.repository.ProcessoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProcessoService {

    private final ProcessoRepository processoRepository;
    private final StorageService storageService;

    public Page<ProcessoListDto> listarProcessos(Pageable pageable) {
        Page<Processo> processosPage = processoRepository.findAll(pageable);
        return processosPage.map(this::convertToDTOList);
    }

    public ProcessoDto buscarProcessoPorId(Long id) {
        Processo processo = processoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Processo não encontrado com o ID: " + id));
        processo.setDataVisualizacao(LocalDateTime.now());
        processoRepository.save(processo);
        return convertToDTO(processo);
    }

    @Transactional
    public ProcessoDto criarProcesso(ProcessoDto processoDTO, byte[] pdfBytes, String filename) {
        Processo processo = convertToEntity(processoDTO);
        String storedFilename = storageService.storeFile(pdfBytes, filename);
        processo.setDocumentoPdf(storedFilename);
        Processo savedProcesso = processoRepository.save(processo);
        return convertToDTO(savedProcesso);
    }

    @Transactional
    public ProcessoDto atualizarProcesso(Long id, ProcessoDto processoDTO, byte[] pdfBytes, String filename) {
        Processo processoExistente = processoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Processo não encontrado com o ID: " + id));

        BeanUtils.copyProperties(processoDTO, processoExistente, "id", "dataCadastro", "dataVisualizacao");

        // Atualiza o documento PDF, se fornecido
        if (pdfBytes != null && pdfBytes.length > 0) {
            // Exclui o arquivo antigo
            storageService.deleteFile(processoExistente.getDocumentoPdf());
            // Salva o novo arquivo
            String storedFilename = storageService.storeFile(pdfBytes, filename);
            processoExistente.setDocumentoPdf(storedFilename);
        }

        Processo processoAtualizado = processoRepository.save(processoExistente);
        return convertToDTO(processoAtualizado);
    }


    public void deletarProcesso(Long id) {
        Processo processo = processoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Processo não encontrado com o ID: " + id));
        storageService.deleteFile(processo.getDocumentoPdf());
        processoRepository.delete(processo);
    }

    private ProcessoDto convertToDTO(Processo processo) {
        ProcessoDto processoDTO = new ProcessoDto();
        BeanUtils.copyProperties(processo, processoDTO);
        return processoDTO;
    }

    private Processo convertToEntity(ProcessoDto processoDTO) {
        Processo processo = new Processo();
        BeanUtils.copyProperties(processoDTO, processo);
        return processo;
    }

    private ProcessoListDto convertToDTOList(Processo processo) {
        ProcessoListDto processoListDTO = new ProcessoListDto();
        processoListDTO.setId(processo.getId());
        processoListDTO.setNpu(processo.getNpu());
        processoListDTO.setDataCadastro(processo.getDataCadastro());
        processoListDTO.setUf(processo.getUf());
        return processoListDTO;
    }
}
