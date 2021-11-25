package banco.modelo.atm;

import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import banco.modelo.ModeloImpl;
import banco.utils.Fechas;


public class ModeloATMImpl extends ModeloImpl implements ModeloATM {
	
	private static Logger logger = LoggerFactory.getLogger(ModeloATMImpl.class);	

	private String tarjeta = null;   // mantiene la tarjeta del cliente actual
	private Integer codigoATM = null;
	
	/*
	 * La información del cajero ATM se recupera del archivo que se encuentra definido en ModeloATM.CONFIG
	 */
	public ModeloATMImpl() {
		logger.debug("Se crea el modelo ATM.");

		logger.debug("Recuperación de la información sobre el cajero");
		
		Properties prop = new Properties();
		try (FileInputStream file = new FileInputStream(ModeloATM.CONFIG))
		{
			logger.debug("Se intenta leer el archivo de propiedades {}",ModeloATM.CONFIG);
			prop.load(file);

			codigoATM = Integer.valueOf(prop.getProperty("atm.codigo.cajero"));

			logger.debug("Código cajero ATM: {}", codigoATM);
		}
		catch(Exception ex)
		{
        	logger.error("Se produjo un error al recuperar el archivo de propiedades {}.",ModeloATM.CONFIG); 
		}
		return;
	}

	@Override
	public boolean autenticarUsuarioAplicacion(String tarjeta, String pin) throws Exception	{
		
		logger.info("Se intenta autenticar la tarjeta {} con pin {}", tarjeta, pin);

		/** 
		 * TODO Código que autentica que exista una tarjeta con ese pin (el pin guardado en la BD está en MD5)
		 *      En caso exitoso deberá registrar la tarjeta en la propiedad tarjeta y retornar true.
		 *      Si la autenticación no es exitosa porque no coincide el pin o la tarjeta no existe deberá retornar falso
		 *      y si hubo algún otro error deberá producir una excepción.
		 */
		boolean validado=false;
		if(tarjeta!=null&&pin!=null) {
		char comillas= '"';
		java.sql.ResultSet rs=consulta("SELECT nro_tarjeta FROM Tarjeta WHERE pin= md5(" +comillas + pin + comillas +");"); 
		if (rs==null) throw new Exception("Error del servidor SQL para resolver la consulta"); 
		
		if(rs.next()!=false){
			try {
				long tar= Long.parseLong(tarjeta);
				validado= tar == rs.getLong("nro_tarjeta");	
				if (validado) this.tarjeta=tarjeta;
			} 
			catch(NumberFormatException e){
				rs.close(); 
				throw new Exception("Error en el parsing del número de tarjeta");
			}
		}
			
			rs.close();
		}
			return validado;


	}
	
	
	@Override
	public Double obtenerSaldo() throws Exception
	{
		logger.info("Se intenta obtener el saldo de cliente {}", 3);

		if (this.tarjeta == null ) {
			throw new Exception("El cliente no ingresó la tarjeta");
		}

		/** 
		 * TODO Obtiene el saldo.
		 *      Debe capturar la excepción SQLException y propagar una Exception más amigable.
		 */
		java.sql.ResultSet rs=consulta("SELECT saldo FROM trans_cajas_ahorro JOIN Tarjeta ON trans_cajas_ahorro.nro_ca = Tarjeta.nro_ca WHERE nro_tarjeta= "+this.tarjeta+" ;"); //No hace falta capturar excepción de SQL porque eso ya lo hacer el método Modelo.consulta()
		if(rs==null) throw new Exception("Error del servidor SQL para resolver la consulta");
		if(!rs.next()) throw new Exception("La tarjeta no está relacionada con ninguna caja de ahorro");
		Double saldo = parseMonto(rs.getString("saldo"));
		return saldo;
	}	

	@Override
	public ArrayList<TransaccionCajaAhorroBean> cargarUltimosMovimientos() throws Exception {
		return this.cargarUltimosMovimientos(ModeloATM.ULTIMOS_MOVIMIENTOS_CANTIDAD);
	}	
	
	@Override
	public ArrayList<TransaccionCajaAhorroBean> cargarUltimosMovimientos(int cantidad) throws Exception
	{
		logger.info("Busca las ultimas {} transacciones en la BD de la tarjeta {}",cantidad, Integer.valueOf(this.tarjeta.trim()));

		/**
		 * TODO Deberá recuperar los ultimos movimientos del cliente, la cantidad está definida en el parámetro.
		 * 		Debe capturar la excepción SQLException y propagar una Exception más amigable. 
		 */
		
		/*
		 * Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reales.
		 * 
		+------------+----------+---------------+---------+----------+---------+
		| fecha      | hora     | tipo          | monto   | cod_caja | destino |
		+------------+----------+---------------+---------+----------+---------+
		| 2021-09-16 | 11:10:00 | transferencia | -700.00 |       18 |      32 |
		| 2021-09-15 | 17:20:00 | extraccion    | -200.00 |        2 |    NULL |
		| 2021-09-14 | 09:03:00 | deposito      | 1600.00 |        2 |    NULL |
		| 2021-09-13 | 13:30:00 | debito        |  -50.00 |     NULL |    NULL |
		| 2021-09-12 | 15:00:00 | transferencia | -400.00 |       41 |       7 |
		+------------+----------+---------------+---------+----------+---------+
 		 */
		 
		 java.sql.ResultSet rs=consulta("SELECT fecha, tipo, monto, cod_caja, destino FROM trans_cajas_ahorro JOIN Tarjeta ON trans_cajas_ahorro.nro_ca = Tarjeta.nro_ca" +
										"WHERE nro_tarjeta="+this.tarjeta+" GROUP BY fecha;");
		
		if(rs==null) throw new Exception("Error del servidor SQL para resolver la consulta");
		ArrayList<TransaccionCajaAhorroBean> lista = new ArrayList<TransaccionCajaAhorroBean>();
		for(int i=0;i<cantidad&&rs.next();i++){
		TransaccionCajaAhorroBean fila1 = new TransaccionCajaAhorroBeanImpl();
		fila1.setTransaccionFechaHora(Fechas.convertirStringADate(rs.getString("fecha")));
		fila1.setTransaccionTipo(rs.getString("tipo"));
		fila1.setTransaccionMonto(parseMonto(rs.getString("monto")));
		fila1.setTransaccionCodigoCaja(rs.getInt("cod_caja"));
		fila1.setCajaAhorroDestinoNumero(rs.getInt("destino"));
		lista.add(fila1);
		}
		
		return lista;
		
	}	
	
	@Override
	public ArrayList<TransaccionCajaAhorroBean> cargarMovimientosPorPeriodo(Date desde, Date hasta)
			throws Exception {

		/**
		 * TODO Deberá recuperar los ultimos del cliente que se han realizado entre las fechas indicadas.
		 * 		Debe capturar la excepción SQLException y propagar una Exception más amigable. 
		 * 		Debe generar excepción sin las fechas son erroneas (ver descripción en interface)
		 */
		
		/*
		 * Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reales.
		 * 
		+------------+----------+---------------+---------+----------+---------+
		| fecha      | hora     | tipo          | monto   | cod_caja | destino |
		+------------+----------+---------------+---------+----------+---------+
		| 2021-09-16 | 11:10:00 | transferencia | -700.00 |       18 |      32 |
		| 2021-09-15 | 17:20:00 | extraccion    | -200.00 |        2 |    NULL |
		| 2021-09-14 | 09:03:00 | deposito      | 1600.00 |        2 |    NULL |
		| 2021-09-13 | 13:30:00 | debito        |  -50.00 |     NULL |    NULL |
		| 2021-09-12 | 15:00:00 | transferencia | -400.00 |       41 |       7 |
		+------------+----------+---------------+---------+----------+---------+
 		 */
		java.sql.ResultSet rsaux= consulta("SELECT CURDATE()");
		Date ahora= Fechas.convertirStringADate(rsaux.getString("CURDATE()"));
		if(desde==null||hasta==null||desde.after(hasta)||hasta.after(ahora)) throw new Exception("Fecha Invalida");
		java.sql.ResultSet rs=consulta("SELECT fecha, tipo, monto, cod_caja, destino FROM trans_cajas_ahorro JOIN Tarjeta ON trans_cajas_ahorro.nro_ca = Tarjeta.nro_ca" +
										"WHERE nro_tarjeta= "+this.tarjeta+" AND fecha > "+Fechas.convertirDateADateSQL(desde)+" AND fecha < "+ Fechas.convertirDateADateSQL(hasta) + " GROUP BY fecha;");
		
		if(rs==null) throw new Exception("Error del servidor SQL para resolver la consulta");
		ArrayList<TransaccionCajaAhorroBean> lista = new ArrayList<TransaccionCajaAhorroBean>();
		while(rs.next()){
		TransaccionCajaAhorroBean fila1 = new TransaccionCajaAhorroBeanImpl();
		fila1.setTransaccionFechaHora(Fechas.convertirStringADate(rs.getString("fecha")));
		fila1.setTransaccionTipo(rs.getString("tipo"));
		fila1.setTransaccionMonto(parseMonto(rs.getString("monto")));
		fila1.setTransaccionCodigoCaja(rs.getInt("cod_caja"));
		fila1.setCajaAhorroDestinoNumero(rs.getInt("destino"));
		lista.add(fila1);
		}
		
		return lista;
	}
	
	@Override
	public Double extraer(Double monto) throws Exception {
		logger.info("Realiza la extraccion de ${} sobre la cuenta", monto);
		
		/**
		 * TODO Deberá extraer de la cuenta del cliente el monto especificado (ya validado) y de obtener el saldo de la cuenta como resultado.
		 * 		Debe capturar la excepción SQLException y propagar una Exception más amigable. 
		 * 		Debe generar excepción si las propiedades codigoATM o tarjeta no tienen valores
		 */		
		if (this.tarjeta == null || this.codigoATM==null ) {
			throw new Exception("El cliente no ingresó la tarjeta");
		}
		
		
		String resultado = ModeloATM.EXTRACCION_EXITOSA;
		java.sql.ResultSet r1= consulta("SELECT nro_cliente FROM Tarjeta WHERE nro_tarjeta= "+this.tarjeta+" ;");
		if(r1==null) resultado= "Error de SQL para obtener el clinete relacionado a la tarjeta";
		else {
		java.sql.Statement st= this.conexion.createStatement();
		String call ="call realizar_extraccion("+r1.getInt("nro_cliente")+", +"+monto+")";
		st.execute(call);
		java.sql.ResultSet r2 = st.getResultSet();
		resultado= r2.getString("resultado");
		}
		if (!resultado.equals(ModeloATM.EXTRACCION_EXITOSA)) {
			throw new Exception(resultado);
		}
		return this.obtenerSaldo();
	}

	
	@Override
	public int parseCuenta(String p_cuenta) throws Exception {
		
		logger.info("Intenta realizar el parsing de un codigo de cuenta {}", p_cuenta);
		
		/**
		 * TODO Verifica que el codigo de la cuenta sea valido. 
		 * 		Debe capturar la excepción SQLException y propagar una Exception más amigable. 
		 * 		Debe generar excepción si la cuenta es vacia, entero negativo o no puede generar el parsing.
		 * retorna la cuenta en formato int
		 */	
		if(p_cuenta==null)
			throw new Exception("La cuenta no puede ser vacía");
		try {
			int cuenta= Integer.parseInt(p_cuenta);
			if(cuenta<0)
				throw new Exception("El número de cuenta no puede ser negativo");
			logger.info("Encontró la cuenta en la BD.");
			return cuenta;
		} 
		catch(NumberFormatException e){
			throw new Exception("No pudo generar el parcing de la cuenta");
		}
	}	
	
	@Override
	public Double transferir(Double monto, int cajaDestino) throws Exception {
		logger.info("Realiza la transferencia de ${} sobre a la cuenta {}", monto, cajaDestino);
		
		/**
		 * TODO Deberá extraer de la cuenta del cliente el monto especificado (ya validado) y de obtener el saldo de la cuenta como resultado.
		 * 		Debe capturar la excepción SQLException y propagar una Exception más amigable. 
		 * 		Debe generar excepción si las propiedades codigoATM o tarjeta no tienen valores
		 */		
		if (this.tarjeta == null || this.codigoATM==null ) {
			throw new Exception("El cliente no ingresó la tarjeta");
		}
		
		String resultado = ModeloATM.TRANSFERENCIA_EXITOSA;
		java.sql.ResultSet r1= consulta("SELECT nro_cliente FROM Tarjeta WHERE nro_tarjeta= "+this.tarjeta+" ;");
		if(r1==null) resultado= "Error de SQL para obtener el clinete relacionado a la tarjeta";
		else {
		java.sql.Statement st= this.conexion.createStatement();
		String call ="call realizar_transferencia("+r1.getInt("nro_cliente")+", "+cajaDestino+", +"+monto+")";
		st.execute(call);
		java.sql.ResultSet r2 = st.getResultSet();
		resultado= r2.getString("resultado");
		}
		if (!resultado.equals(ModeloATM.TRANSFERENCIA_EXITOSA)) {
			throw new Exception(resultado);
		}
		return this.obtenerSaldo();
	}


	@Override
	public Double parseMonto(String p_monto) throws Exception {
		
		logger.info("Intenta realizar el parsing del monto {}", p_monto);
		
		if (p_monto == null) {
			throw new Exception("El monto no puede estar vacío");
		}

		try 
		{
			double monto = Double.parseDouble(p_monto);
			DecimalFormat df = new DecimalFormat("#.00");

			monto = Double.parseDouble(corregirComa(df.format(monto)));
			
			if(monto < 0)
			{
				throw new Exception("El monto no debe ser negativo.");
			}
			
			return monto;
		}		
		catch (NumberFormatException e)
		{
			throw new Exception("El monto no tiene un formato válido.");
		}	
	}

	private String corregirComa(String n)
	{
		String toReturn = "";
		
		for(int i = 0;i<n.length();i++)
		{
			if(n.charAt(i)==',')
			{
				toReturn = toReturn + ".";
			}
			else
			{
				toReturn = toReturn+n.charAt(i);
			}
		}
		
		return toReturn;
	}	
	
	

	
}
