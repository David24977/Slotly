package com.david.agenda_api.cliente;

import com.david.agenda_api.cliente.dto.ClienteRequestDto;
import com.david.agenda_api.cliente.dto.ClienteResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface ClienteService {
    /**
     * Busca un cliente por email. Si no existe, lo registra automáticamente
     * con el proveedor MAGIC_LINK y devuelve su DTO con el ID de Postgres.
     */
    ClienteResponseDto registrarOSiExisteObtener(String email);

    // --- OPERACIONES DE NEGOCIO Y BÚSQUEDAS ---
    /**
     * Busca un cliente por su ID de Postgres.
     */
    ClienteResponseDto buscarPorId(Long id);

    /**
     * Busca un cliente por su email (útil para validaciones manuales).
     */
    Optional<ClienteResponseDto> buscarPorEmail(String email);


    // --- CRUD TRADICIONAL (Para el Comercio o Admin) ---
    /**
     * Crea un cliente de forma manual (por ejemplo, si un comercio añade a un cliente desde su panel).
     */
    ClienteResponseDto crearClienteManual(ClienteRequestDto request);

    /**
     * Actualiza los datos del cliente (cuando el cliente entra a su perfil y cambia su nombre o teléfono).
     */
    ClienteResponseDto actualizarCliente(Long id, ClienteRequestDto request);

    /**
     * Paginación de clientes para que el comercio pueda ver su listado sin saturar la app.
     */
    Page<ClienteResponseDto> listarTodosLosClientes(Pageable pageable);

    /**
     * Borrado lógico (desactivar) o físico de un cliente.
     */
    void eliminarCliente(Long id);
}
