package com.david.agenda_api.cliente;

import com.david.agenda_api.cliente.dto.ClienteRequestDto;
import com.david.agenda_api.cliente.dto.ClienteResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ClienteServiceImpl implements ClienteService{
    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public ClienteResponseDto registrarOSiExisteObtener(String email) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseGet(() -> {
                    // Si no existe en la BD, lo registramos al vuelo con proveedor MAGIC_LINK
                    String nombreTemporal = email.split("@")[0];

                    Cliente nuevoCliente = Cliente.builder()
                            .email(email)
                            .nombre(nombreTemporal)
                            .authProvider("MAGIC_LINK")
                            .activo(true)
                            .build();

                    return clienteRepository.save(nuevoCliente);
                });

        // Mapeamos la entidad (que ya tiene el ID de Postgres) al ResponseDto
        return new ClienteResponseDto(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getEmail(),
                cliente.getAuthProvider(),
                cliente.getFechaRegistro(),
                cliente.getActivo()
        );
    }

    @Override
    public ClienteResponseDto buscarPorId(Long id) {
        return null;
    }

    @Override
    public Optional<ClienteResponseDto> buscarPorEmail(String email) {
        return Optional.empty();
    }

    @Override
    public ClienteResponseDto crearClienteManual(ClienteRequestDto request) {
        return null;
    }

    @Override
    public ClienteResponseDto actualizarCliente(Long id, ClienteRequestDto request) {
        return null;
    }

    @Override
    public Page<ClienteResponseDto> listarTodosLosClientes(Pageable pageable) {
        return null;
    }

    @Override
    public void eliminarCliente(Long id) {

    }
}

