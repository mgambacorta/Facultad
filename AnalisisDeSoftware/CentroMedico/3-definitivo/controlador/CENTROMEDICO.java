package controlador.centroMedico;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import ventana.centroMedico.VentanaConectar;

public class CENTROMEDICO {
	// Dimensiones de la interfaz
	public final static int ALTO = 640;
	public final static int ANCHO = 480;

	// Titulo de la interfaz
	public final static String TITULO = "CENTRO MEDICO UNLAM";

	// Mensajes de Errores
	public final static String ERROR_CODIGO_RANGO_MEDICO = "El código del medico debe ser un número entre 1 y 9999.";
	public final static String ERROR_NOMBRE_RANGO_MEDICO = "El nombre del medico debe contener un mínimo de 2 caracteres y un máximo de 20.";
	public final static String ERROR_NOMBRE_FORMATO_MEDICO = "El nombre del medico debe empezar con una letra y solo puede contener caracteres alfanúmericos, tildes y espacios.";
	public final static String ERROR_ESPECIALIDAD_VALORES_MEDICO = "La especialidad solo puede ser Pediatría, Traumatología o Cardiología.";
	
	public final static String ERROR_CODIGO_RANGO_PACIENTE = "El código del paciente debe ser un número entre 1 y 9999.";
	public final static String ERROR_NOMBRE_RANGO_PACIENTE = "El nombre del paciente debe contener un mínimo de 2 caracteres y un máximo de 20.";
	public final static String ERROR_NOMBRE_FORMATO_PACIENTE = "El nombre del paciente debe empezar con una letra y solo puede contener caracteres alfanúmericos, tildes y espacios.";
	public final static String ERROR_RANGO_SITUACION = "El diagnostico del paciente debe contener un minimo de 10 y un maximo de 200 caracteres.";
	public final static String ERROR_FORMATO_SITUACION = "El diagnostico del paciente debe empezar con una letra y solo puede contener caracteres alfanúmericos, tildes y espacios.";
	
	public final static String ERROR_ARCHIVO_PACIENTE = "No se pudo guardar los datos del paciente en el archivo.";
	public final static String ERROR_ARCHIVO_MEDICO = "No se pudo guardar los datos del medico en el archivo.";
	public final static String ERROR_ARCHIVO_SITUACION = "No se pudo guardar el historial en el archivo.";
	
	// Mensajes de Ayuda
	public final static String COD_MEDICO_AYUDA = "<html> El codigo del medico debe ser un <br>numero entero entre 1 y 9999.</html>";
	public final static String NOM_MEDICO_AYUDA = "<html>El nombre del medico debe contener un mínimo de 2 caracteres <br>y un máximo de 20, empezar con una letra y solo puede contener <br>caracteres alfanúmericos, tildes y espacios.</html>";
	public final static String COD_PACIENTE_AYUDA = "<html>El codigo del paciente debe ser un <br>numero entero entre 1 y 9999.</html>";
	public final static String NOM_PACIENTE_AYUDA = "<html>El nombre del paciente debe contener un mínimo de 2 caracteres <br>y un máximo de 20, empezar con una letra y solo puede contener <br>caracteres alfanúmericos, tildes y espacios.</html>";	
	public final static String SIT_PACIENTE_AYUDA = "<html>El diagnostico del paciente debe contener un mínimo de 10 caracteres <br>y un máximo de 200, empezar con una letra y solo puede contener <br>caracteres alfanúmericos, tildes y espacios.</html>";
	
	// Path de archivos
	private static final String DATO_MED_PATH = "./datomed.txt";
	private static final String DATO_PAC_PATH = "./datopac.txt";
	private static final String SITU_PAC_PATH = "./situpac.txt";

	//Especialidades de los Medicos
	public static final String[] ESPECIALIDADES = {"Pediatria", "Traumatologia", "Cardiologia"};
	
	// Clave de Encriptado
	private static final String CLAVE="AnalisiSdeSoftwarE2022";

	public static void ingresarPaciente(String codPac, String nomPac) throws Exception{
		// Valida el codigo del paciente.
		if(!esCodigoValido(codPac)){
			throw new Exception(ERROR_CODIGO_RANGO_PACIENTE);
		}
		
		// Valida el Rango del nombre del paciente.
		if(!esRangoNombreValido(nomPac, 1, 21)) {
			throw new Exception(ERROR_NOMBRE_RANGO_PACIENTE);
		}
		
		// Valida el Formato del nombre del paciente.
		if(!esFormatoNombreValido(nomPac)) {
			throw new Exception(ERROR_NOMBRE_FORMATO_PACIENTE);
		}
		
		try {
			// Abre el archivo haciendo referencia al final del mismo.
			DataOutputStream archivo = new DataOutputStream(new FileOutputStream(DATO_PAC_PATH, true));

			// Escribe en el archivo los datos del paciente encriptados.
			archivo.writeUTF(encriptar(codPac));
			archivo.writeUTF(encriptar(nomPac));

			archivo.close();
		} catch (IOException e) {
			// Envia una excepción en caso de no tener permisos en el archivo.
			throw new Exception(ERROR_ARCHIVO_PACIENTE);
		}
	}

	public static void ingresarSituacionPaciente(String codPac, String codMed, String sit) throws Exception {
		// Valida el codigo del paciente.
		if(!esCodigoValido(codPac)){
			throw new Exception(ERROR_CODIGO_RANGO_PACIENTE);
		}
		
		// Valida el codigo del medico.
		if(!esCodigoValido(codMed)){
			throw new Exception(ERROR_CODIGO_RANGO_MEDICO);
		}
		
		// Valida el Rango del nombre del paciente.
		if(!esRangoNombreValido(sit, 9, 201)) {
			throw new Exception(ERROR_RANGO_SITUACION);
		}
		
		// Valida el formato de la situacion.
		if(!esFormatoNombreValido(sit)) {
			throw new Exception(ERROR_FORMATO_SITUACION);
		}
		
		try {
			// Abre el archivo haciendo referencia al final del mismo.
			DataOutputStream archivo = new DataOutputStream(new FileOutputStream(SITU_PAC_PATH, true));

			// Escribe en el archivo el historial encriptado.
			archivo.writeUTF(encriptar(codPac));
			archivo.writeUTF(encriptar(codMed));
			archivo.writeUTF(encriptar(sit));
			
			archivo.close();
		} catch (IOException ioe) {
			// Envia una excepción en caso de no tener permisos en el archivo.
			throw new Exception(ERROR_ARCHIVO_SITUACION);
		}
	}

	public static void ingresarMedico(String codmed, String nommed, String espmed) throws Exception{
		// Valida el codigo del medico.
		if(!esCodigoValido(codmed)){
			throw new Exception(ERROR_CODIGO_RANGO_MEDICO);
		}
		
		// Valida el Rango del nombre del medico.
		if(!esRangoNombreValido(nommed, 1, 21)) {
			throw new Exception(ERROR_NOMBRE_RANGO_MEDICO);
		}
		
		// Valida el Formato del nombre del medico.
		if(!esFormatoNombreValido(nommed)) {
			throw new Exception(ERROR_NOMBRE_FORMATO_MEDICO);
		}
		
		// Valida la especialidad del medico.
		if(!esEspecialidadValida(espmed)) {
			throw new Exception(ERROR_ESPECIALIDAD_VALORES_MEDICO);
		}
		
		try {
			// Abre el archivo haciendo referencia al final del mismo.
			DataOutputStream archivo = new DataOutputStream(new FileOutputStream(DATO_MED_PATH, true));
		
			// Escribe en el archivo los datos del medico encriptados.
			archivo.writeUTF(encriptar(codmed));
			archivo.writeUTF(encriptar(nommed));
			archivo.writeUTF(encriptar(espmed));
			
			archivo.close();
		} catch (IOException e) {
			// Envia una excepción en caso de no tener permisos en el archivo.
			throw new Exception(ERROR_ARCHIVO_MEDICO);
		} 
	}

	public static ArrayList<String> buscarPacientesPorMedico(String codigo, ArrayList<String> pacientes) throws Exception{
		int sw = 0, sw1 = 0;
		String codp, codme, enfp, codpa, nompa;
		
		try {
			DataInputStream situpac = new DataInputStream(new FileInputStream(SITU_PAC_PATH));
			
			sw = 1;
			while (sw != 0) {
				try {
					codp = desencriptar(situpac.readUTF());
					codme = desencriptar(situpac.readUTF());
					enfp = desencriptar(situpac.readUTF());
	
					// compara el codigo medico de la tabla "datomed" 
					// con el de la tabla "situpac"
					if (codme.equals(codigo)) {
						DataInputStream datopac = new DataInputStream(new FileInputStream(DATO_PAC_PATH));
	
						sw1 = 1;
						while (sw1 != 0) {
							try {
								codpa = desencriptar(datopac.readUTF());
								nompa = desencriptar(datopac.readUTF());
	
								// compara el codigo del paciente de la tabla 
								// "situpac" con el codigo del
								// paciente de la tabla "datopac"
								if (codpa.equals(codp)) {
									pacientes.add("   " + nompa);
								}
							} catch (EOFException e) {
								sw1 = 0;
							}
						}
						datopac.close();
					}
				} catch (EOFException e) {
					sw = 0;
				}
			}
			situpac.close();
		} catch(FileNotFoundException e) {
			sw = 0;
		}
		
		return pacientes;
	}
	
	public static ArrayList<String> listarPacientesPorMedico(String codigo) throws Exception{
		
		// Valida el codigo del medico.
		if(!esCodigoValido(codigo)){
			throw new Exception(ERROR_CODIGO_RANGO_MEDICO);
		}
	
		int sw = 0;
		String codm = "", nomm = "", espm;

		// Arreglo de información
		ArrayList<String> pacientes = new ArrayList<String>();
		
		try {
			DataInputStream datomed = new DataInputStream(new FileInputStream(DATO_MED_PATH));

			sw = 1;
			while (sw != 0) {
				try {
					codm = desencriptar(datomed.readUTF());
					nomm = desencriptar(datomed.readUTF());
					espm = desencriptar(datomed.readUTF());
					
					// compara el codigo extraido de la tabla "datomed" con el codigo digitado
					if (codm.equals(codigo)) {
						pacientes.add("Nombre del Medico: " + nomm);
						pacientes.add("Nombre de los pacientes que atiende: ");
						
						pacientes = buscarPacientesPorMedico(codigo, pacientes);
					}
				}catch(EOFException e) {
					sw = 0;
				}
			}
			
			datomed.close();
		} catch (FileNotFoundException e) {
			sw = 0;
		}
		
		return pacientes;
	}

	public static ArrayList<String> listarEnfermedadesPorMedico(String codigo) throws Exception {
		
		// Valida el codigo del medico.
		if(!esCodigoValido(codigo)){
			throw new Exception(ERROR_CODIGO_RANGO_MEDICO);
		}
		
		// Variables de corte de control
		int sw = 0, sw1 = 0;

		// Variables usadas en la lectura de datos.
		String codm = "", codme = "", enfp = "", nomm = "", espm = "", codp;

		// Arreglo de información
		ArrayList<String> enfermedades = new ArrayList<String>();

		try {
			DataInputStream datomed = new DataInputStream(new FileInputStream(DATO_MED_PATH));
			
			sw1 = 1;
			while (sw1 != 0) {
				try {
					codm = desencriptar(datomed.readUTF());
					nomm = desencriptar(datomed.readUTF());
					espm = desencriptar(datomed.readUTF());

					// compara el codigo digitado
					// con el codigo del medico de la
					// tabla "datomed"
					if (codm.equals(codigo)) {
						enfermedades.add("Nombre del Medico: " + nomm);
						enfermedades.add("Nombre de las enfermedades que atiende: ");
						
						DataInputStream situpac = new DataInputStream(new FileInputStream(SITU_PAC_PATH));

						sw = 1;
						while (sw != 0) {
							try {
								codp = desencriptar(situpac.readUTF());
								codme = desencriptar(situpac.readUTF());
								enfp = desencriptar(situpac.readUTF());

								// compara el codigo del medico
								// de la tabla "datomed"
								// con el codigo del medico en la
								// tabla "situpac"
								if (codme.equals(codigo)) {
									enfermedades.add("   " + enfp);
								}
							} catch (EOFException e) {
								sw = 0;
							}
						}
						situpac.close();
					}
				} catch (EOFException e) {
					sw1 = 0;
				}
			}
			datomed.close();
		} catch (FileNotFoundException e) {
			sw = 0;
		}

		return enfermedades;
	}
	
	/**
	 *  VALIDACIONES
	 */
	private static boolean esCodigoValido(String codigo) {
		return codigo.matches("^[1-9][0-9]{0,3}$");
	}
	
	private static boolean esRangoNombreValido(String nombreMedico, int min, int max) {
		int longitud = nombreMedico.length();
		
		return longitud > min && longitud < max;
	}
	
	private static boolean esFormatoNombreValido(String nombreMedico) {
		return nombreMedico.matches("^[A-Za-záéíóúÁÉÍÓÚ][A-Za-z0-9 áéíóúÁÉÍÓÚ]*$");
	}
	
	private static boolean esEspecialidadValida(String especialidadMedico) {
		return Arrays.stream(ESPECIALIDADES).anyMatch(especialidad -> especialidad.equals(especialidadMedico));
	}
	/**
	 * FIN VALIDACIONES
	 */

	/**
	 *  SEGURIDAD
	 */
	private static String encriptar(String mensaje) throws Exception {	
		// Encripta el mensaje.
		Key aesKey = new SecretKeySpec(CLAVE.getBytes(), 0, 16, "AES");

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, aesKey);

		byte[] encrypted = cipher.doFinal(mensaje.getBytes());
			
		return Base64.getEncoder().encodeToString(encrypted);
	}

	private static String desencriptar(String mensaje) throws Exception {
		// Desencripta el mensaje.
		Key aesKey = new SecretKeySpec(CLAVE.getBytes(), 0, 16, "AES");

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, aesKey);

		String decrypted = new String(cipher.doFinal(Base64.getDecoder().decode(mensaje)));
	        
		return decrypted;
	}
	/**
	 *  FIN SEGURIDAD
	 */

	public static void main(String args[]) throws Exception {
		VentanaConectar ventanaConectar = new VentanaConectar();
		ventanaConectar.setVisible(true);
	}
}
