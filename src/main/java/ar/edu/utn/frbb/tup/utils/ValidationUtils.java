package ar.edu.utn.frbb.tup.utils;

import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;

public class ValidationUtils {

    private ValidationUtils() {
    }

    public static boolean comprobarDni(final long dni) throws DatoInvalidoException {
        String numeroComoCadena = Long.toString(dni);
        int cantidadDeDigitos = numeroComoCadena.length();
        if (cantidadDeDigitos > 8 || cantidadDeDigitos < 7) {
            throw new DatoInvalidoException("El DNI debe tener entre 7 y 8 dígitos.");
        }
        return true;
    }

    public static boolean checkStringField(final String field, final String fieldName) throws DatoInvalidoException {
        if (field == null || field.isBlank()) {
            throw new DatoInvalidoException("El " + fieldName + " no puede ser nulo ni estar vacío.");
        }
        if (field.matches(".*\\d+.*")) {
            throw new DatoInvalidoException("El " + fieldName + " no puede contener números.");
        }
        return true;
    }
}
