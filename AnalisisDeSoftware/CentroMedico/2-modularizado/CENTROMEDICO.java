import java.io.*;

class CENTROMEDICO {

	public static void ps(String x) {
		System.out.print(x);
	}

	public static int LeerEntero() {
		int entero = 0;
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			entero = Integer.parseInt(br.readLine());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (entero);
	}

	public static String LeerCadena() {
		String linea = new String("");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			linea = br.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return (linea);
	}
	
	public static void menuIngresoDeDatos() {
		int opcion = 0;
		
		do {
			ps("   ..............................................." + "\n");
			ps("   :-: -I N G R E S O  D E  P A C I E N T E S- :-:" + "\n");
			ps("   :-:.........................................:-:" + "\n");
			ps("   :-: 1.  Datos del paciente                  :-:" + "\n");
			ps("   :-: 2.  Situacion del paciente              :-:" + "\n");
			ps("   :-: 3.  Datos del medico                    :-:" + "\n");
			ps("   :-: 4.  VOLVER                              :-:" + "\n");
			ps("   ..............................................." + "\n");
			ps("   ....Elija la opcion deseada: ");

			opcion = LeerEntero();

			if (opcion == 1) 
				ingresarPaciente();
			else if(opcion == 2)
				ingresarSituacionPaciente();
			else if(opcion == 3)
				ingresarMedico();
			else if(opcion != 4)
				ps("Debe digitar una opcion del menu" + "\n");

		} while (opcion != 4);
	}
	
	public static void menuInformes() {
		int opcion;
		
		do {
			ps("   ..............................................." + "\n");
			ps("   :-:  C O N T R O L  D E  P A C I E N T E S  :-:" + "\n");
			ps("   ..............................................." + "\n");
			ps("   :-:           - I N F O R M E S -           :-:" + "\n");
			ps("   :-:.........................................:-:" + "\n");
			ps("   :-: 1. Listado de pacientes por medico      :-:" + "\n");
			ps("   :-: 2. Enfermedades que atiende cada medico :-:" + "\n");
			ps("   :-: 3. Anterior                             :-:" + "\n");
			ps("   ..............................................." + "\n");
			ps("   ....Elija la opcion deseada: ");
			
			opcion = LeerEntero();
			
			if(opcion == 1)
				listarPacientesPorMedico();
			else if(opcion == 2)
				listarEnfermedadesPorMedico();
			else if(opcion != 3)
				ps("Seleccione una de las opciones del menu" + "\n");
			
		} while (opcion != 3);
	}
	
	public static void ingresarPaciente() {
		try {
			String codpac, nompac, op;
			DataOutputStream datopac = new DataOutputStream(new FileOutputStream("C:\\datopac.txt"));
			
			do {
				ps("   ..............................................." + "\n");
				ps("   :-:  - D A T O S  D E L  P A C I E N T E -  :-:" + "\n");
				ps("   :-:.........................................:-:" + "\n");

				ps("Digite el codigo del paciente: ");
				codpac = LeerCadena();
				datopac.writeUTF(codpac);
				
				ps("Digite el nombre del paciente: ");
				nompac = LeerCadena();
				datopac.writeUTF(nompac);

				ps("Desea ingresar otro paciente? S/N" + "\n");
				op = LeerCadena();

			} while (op.equals("S") || op.equals("s"));
			datopac.close();

		} catch (IOException ioe) {
			//mensaje de error
		}
	}
	
	public static void ingresarSituacionPaciente() {
		try {
			String codp, codm, enfpac, op;
			DataOutputStream situpac = new DataOutputStream(new FileOutputStream("C:\\situpac.txt"));
			
			do {
				ps("   ....................................................." + "\n");
				ps("   :-: - S I T U A C I O N  D E L  P A C I E N T E - :-:" + "\n");
				ps("   :-:...............................................:-:" + "\n");

				ps("Digite el codigo del paciente: ");
				codp = LeerCadena();
				situpac.writeUTF(codp);
				ps("Digite el codigo del medico: ");
				codm = LeerCadena();
				situpac.writeUTF(codm);
				ps("Digite el diagnostico del medico: ");
				enfpac = LeerCadena();
				situpac.writeUTF(enfpac);

				ps("Desea ingresar otro registro al historial? S/N");
				ps("\n");
				op = LeerCadena();

			} while (op.equals("S") || op.equals("s"));
			
			situpac.close();
		} catch (IOException ioe) {
			//Mensaje Error
		}
	}
	
	public static void ingresarMedico() {
		try {
			String codmed, nommed, espmed, op;
			DataOutputStream datomed = new DataOutputStream(new FileOutputStream("C:\\datomed.txt"));
			
			do {
				ps("   ....................................................." + "\n");
				ps("   :-:      - D A T O S   D E L   M E D I C O -      :-:" + "\n");
				ps("   :-:...............................................:-:" + "\n");

				ps("Digite el codigo del medico: ");
				codmed = LeerCadena();
				datomed.writeUTF(codmed);

				ps("Digite el nombre del medico: ");
				nommed = LeerCadena();
				datomed.writeUTF(nommed);

				ps("Digite la especializacion del medico: ");
				espmed = LeerCadena();
				datomed.writeUTF(espmed);

				ps("Desea ingresar otro medico? S/N");
				ps("\n");

				op = LeerCadena();

			} while (op.equals("S") || op.equals("s"));
			
			datomed.close();
		} catch (IOException ioe) {
			//Mensaje de Error
		}
	}
	
	@SuppressWarnings("resource")
	public static void listarPacientesPorMedico(){
		
			int sw = 0, sw1 = 0;
			String codtem, codm = "", nomm = "", espm, codp, codme, enfp, codpa, nompa;
			
			ps("Digite el codigo del medico que desea consultar: ");
			codtem = LeerCadena();

			

			sw = 1;
			while (sw != 0) {
				try {
					DataInputStream datomed = null;
					datomed = new DataInputStream(new FileInputStream("C:\\datomed.txt"));
					
					codm = datomed.readUTF();
					nomm = datomed.readUTF();
					espm = datomed.readUTF();
				} catch (IOException e) {
					sw = 0;
				}

				// compara el codigo extraido de la tabla "datomed" con el codigo digitado
				if (codm.equals(codtem)) {
					sw = 1;
					while (sw != 0) {
						try {
							ps("::: El medico " + nomm + " atiende a los siguientes pacientes: " + "\n");
							DataInputStream situpac = new DataInputStream(new FileInputStream("C:\\situpac.txt"));

							codp = situpac.readUTF();
							codme = situpac.readUTF();
							enfp = situpac.readUTF();

					// compara el codigo medico de la tabla "datomed" con el de la tabla "situpac"
							if (codme.equals(codtem)) {
								DataInputStream datopac = new DataInputStream(new FileInputStream("C:\\datopac.txt"));

								sw1 = 1;
								while (sw1 != 0) {
									try {
										codpa = datopac.readUTF();
										nompa = datopac.readUTF();

					// compara el codigo del paciente de la tabla "situpac" con el codigo del paciente de la tabla "datopac"
										if (codpa.equals(codp)){
											ps("::: Paciente: " + nompa + "\n");
										}
									} catch (EOFException e) {
										sw1 = 0;
									}
								}
								
								datopac.close();
							}
							situpac.close();
						} catch (IOException e) {
							sw = 0;
						}
					}
					
				}
			}
		}
	
	
	@SuppressWarnings("resource")
	public static void listarEnfermedadesPorMedico() {
		String codtem;
		int sw = 0, sw1 = 0;
		String codm = "", codme = "", enfp = "", nomm = "", espm = "", codp; // variables usadas en la lectura de datos
		
		ps("Digite el codigo del medico que desea consultar: ");
		codtem = LeerCadena();

		DataInputStream datomed = null;
		try {
			datomed = new DataInputStream(new FileInputStream("C:\\datomed.txt"));
			sw1 = 1;
		} catch (FileNotFoundException e1) {
			sw=0;
		}
		
		while (sw1 != 0) {
			try {
				codm = datomed.readUTF();
				nomm = datomed.readUTF();
				espm = datomed.readUTF();
	
				 // compara el codigo digitado
				// con el codigo del medico de la
				// tabla "datomed"
				if (codm.equals(codtem)){
					ps("El medico " + nomm + " trata las siguientes enfermedades:" + "\n");
	
					DataInputStream situpac = new DataInputStream(
							new FileInputStream("C:\\situpac.txt"));
	
					sw = 1;
					while (sw != 0) {
						try {
							codp = situpac.readUTF();
							codme = situpac.readUTF();
							enfp = situpac.readUTF();
	
							// compara el codigo del medico
							// de la tabla "datomed"
							// con el codigo del medico en la
							// tabla "situpac"
							if (codtem.equals(codme)){
								ps(">>>> " + enfp + "\n");
							}
						} catch (EOFException e) {
							sw = 0;
						}
					}
				}
			} catch (IOException e) {
				sw1 = 0;
			}
		}
	}

	public static void main (String args[]) throws Exception {

		VentanaConectar ventana = new VentanaConectar();
		/*
		int opcion; 

		do {
			ps("   .............................................." + "\n");
			ps("   :-:           CENTRO MEDICO                :-:" + "\n");
			ps("   :-:   >>>>     U N L A M   <<<<            :-:" + "\n");
			ps("   :-:    M E S A   D E    A D M I S I O N    :-:" + "\n");
			ps("   :-:........................................:-:" + "\n");
			ps("   :-: 1.  Ingreso de datos                   :-:" + "\n");
			ps("   :-: 2.  Informes                           :-:" + "\n");
			ps("   :-: 3.  Salir                              :-:" + "\n");
			ps("   .............................................." + "\n");
			ps("   ....Elija la opcion deseada: ");
			
			opcion = LeerEntero();

			//Seleccion menu ingreso de pacientes
			if (opcion == 1) 
				menuIngresoDeDatos();
			else if(opcion == 2)  // Seleccion menu informes
				menuInformes();
			else if(opcion != 3)
				ps("Debe digitar una opcion del menu" + "\n");
			
		} while (opcion != 3);
		 */
	}
}
