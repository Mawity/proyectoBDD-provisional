package banco.modelo.empleado;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import banco.modelo.ModeloImpl;
import banco.modelo.empleado.beans.ClienteBean;
import banco.modelo.empleado.beans.ClienteMorosoBean;
import banco.modelo.empleado.beans.DAOCliente;
import banco.modelo.empleado.beans.DAOClienteImpl;
import banco.modelo.empleado.beans.DAOClienteMoroso;
import banco.modelo.empleado.beans.DAOClienteMorosoImpl;
import banco.modelo.empleado.beans.DAOEmpleado;
import banco.modelo.empleado.beans.DAOEmpleadoImpl;
import banco.modelo.empleado.beans.DAOPago;
import banco.modelo.empleado.beans.DAOPagoImpl;
import banco.modelo.empleado.beans.DAOPrestamo;
import banco.modelo.empleado.beans.DAOPrestamoImpl;
import banco.modelo.empleado.beans.EmpleadoBean;
import banco.modelo.empleado.beans.PagoBean;
import banco.modelo.empleado.beans.PrestamoBean;
import banco.utils.Parsing;

public class ModeloEmpleadoImpl extends ModeloImpl implements ModeloEmpleado {

	private static Logger logger = LoggerFactory.getLogger(ModeloEmpleadoImpl.class);	

	
	private Integer legajo = null;
	
	public ModeloEmpleadoImpl() {
		logger.debug("Se crea el modelo Empleado.");
	}
	

	@Override
	public boolean autenticarUsuarioAplicacion(String legajo, String password) throws Exception {
		logger.info("Se intenta autenticar el legajo {} con password {}", legajo, password);
		/** 
		 * TODO Código que autentica que exista un legajo de empleado y que el password corresponda a ese legajo
		 *      (el password guardado en la BD está en MD5) 
		 *      En caso exitoso deberá registrar el legajo en la propiedad legajo y retornar true.
		 *      Si la autenticación no es exitosa porque el legajo no es válido o el password es incorrecto
		 *      deberá retornar falso y si hubo algún otro error deberá producir una excepción.
		 */
		boolean validado=false;
		if(legajo!=null&&password!=null) {
		char comillas= '"';
		java.sql.ResultSet rs=consulta("SELECT legajo FROM Empleado WHERE password= md5(" +comillas + password + comillas +");");
		if (rs==null) throw new Exception("Error del servidor SQL para resolver la consulta"); //No hace falta capturar excepción de SQL porque eso ya lo hacer el método Modelo.consulta()
		if(rs.next()!=false){
				
				try {
				int leg= Integer.parseInt(legajo);
				validado= leg == rs.getInt("legajo");	
				if (validado) this.legajo=leg;
				} catch(NumberFormatException e) 
					{
						rs.close();
					 	throw new Exception("Error en el parsing del legajo");
					}
				rs.close();	
		}
		}
			return validado;
	}
	
	@Override
	public EmpleadoBean obtenerEmpleadoLogueado() throws Exception {
		logger.info("Solicita al DAO un empleado con legajo {}", this.legajo);
		if (this.legajo == null) {
			logger.info("No hay un empleado logueado.");
			throw new Exception("No hay un empleado logueado. La sesión terminó.");
		}
		
		DAOEmpleado dao = new DAOEmpleadoImpl(this.conexion);
		return dao.recuperarEmpleado(this.legajo);
	}	
	
	@Override
	public ArrayList<String> obtenerTiposDocumento() throws Exception{
		logger.info("recupera los tipos de documentos.");
		/** 
		 * TODO Debe retornar una lista de strings con los tipos de documentos. 
		 *      Deberia propagar una excepción si hay algún error en la consulta.
		 */
		java.sql.ResultSet rs= consulta("(SELECT DISTINCT tipo_doc FROM Cliente) UNION (SELECT DISTINCT tipo_doc FROM Empleado);");
		if(rs==null) throw new Exception("Error del servidor SQL para resolver la consulta"); //No hace falta capturar excepción de SQL porque eso ya lo hacer el método Modelo.consulta()
		ArrayList<String> tipos = new ArrayList<String>();
		while(rs.next()){
		tipos.add(rs.getString("tipo_doc"));
		}
		return tipos;
	}	

	@Override
	public double obtenerTasa(double monto, int cantidadMeses) throws Exception {

		logger.info("Busca la tasa correspondiente a el monto {} con una cantidad de meses {}", monto, cantidadMeses);

		/** 
		 * TODO Debe buscar la tasa correspondiente según el monto y la cantidadMeses. 
		 *      Deberia propagar una excepción si hay algún error de conexión o 
		 *      no encuentra el monto dentro del [monto_inf,monto_sup] y la cantidadMeses.
		 */
		
		java.sql.ResultSet rs= consulta("SELECT tasa FROM Tasa_Plazo_Fijo WHERE periodo= "+cantidadMeses+" AND monto_inf <= "+Double.toString(monto)+" AND monto_sup >= "+Double.toString(monto)+" ;");
		if(rs==null) throw new Exception("Error del servidor SQL para resolver la consulta"); //No hace falta capturar excepción de SQL porque eso ya lo hacer el método Modelo.consulta()
		if(!rs.next()) throw new Exception("No encuentra el monto dentro del rango y la cantidad de meses");
		double tasa = Parsing.parseMonto(rs.getString("tasa"));
   		return tasa;

	}

	@Override
	public double obtenerInteres(double monto, double tasa, int cantidadMeses) {
		return (monto * tasa * cantidadMeses) / 1200;
	}


	@Override
	public double obtenerValorCuota(double monto, double interes, int cantidadMeses) {
		return (monto + interes) / cantidadMeses;
	}
		

	@Override
	public ClienteBean recuperarCliente(String tipoDoc, int nroDoc) throws Exception {
		DAOCliente dao = new DAOClienteImpl(this.conexion);
		return dao.recuperarCliente(tipoDoc, nroDoc);
	}


	@Override
	public ArrayList<Integer> obtenerCantidadMeses(double monto) throws Exception {
		logger.info("recupera los períodos (cantidad de meses) según el monto {} para el prestamo.", monto);

		/** 
		 * TODO Debe buscar los períodos disponibles según el monto. 
		 *      Deberia propagar una excepción si hay algún error de conexión o 
		 *      no encuentra el monto dentro del [monto_inf,monto_sup].
		 */
		java.sql.ResultSet rs= consulta("SELECT tasa FROM Tasa_Plazo_Fijo WHERE monto_inf <= "+Double.toString(monto)+" AND monto_sup >= "+Double.toString(monto)+" ;"); 
		if(rs==null) throw new Exception("Error del servidor SQL para resolver la consulta"); //No hace falta capturar excepción de SQL porque eso ya lo hacer el método Modelo.consulta()
		if(!rs.next()) throw new Exception("No encuentra el monto dentro del rango");
		ArrayList<Integer> cantMeses = new ArrayList<Integer>();
		do{
		cantMeses.add(rs.getInt("periodo"));
		} while(rs.next());
		return cantMeses;

	}

	@Override	
	public Integer prestamoVigente(int nroCliente) throws Exception 
	{
		logger.info("Verifica si el cliente {} tiene algun prestamo que tienen cuotas por pagar.", nroCliente);

		/** 
		 * TODO Busca algún prestamo del cliente que tenga cuotas sin pagar (vigente) retornando el nro_prestamo
		 *      si no existe prestamo del cliente o todos están pagos retorna null.
		 *      Si hay una excepción la propaga con un mensaje apropiado.
		 */
		String query="SELECT nro_prestamo, FROM Cliente NATURAL JOIN Prestamo NATURAL JOIN Pago WHERE nro_cliente= "+nroCliente+" AND fecha_pago IS NULL AND fecha_venc < CURDATE() GROUP BY nro_prestamo HAVING COUNT(fecha_venc) >= 1;";
		java.sql.ResultSet rs = consulta(query);
		if(rs==null) throw new Exception("Error del servidor SQL para resolver la consulta"); //No hace falta capturar excepción de SQL porque eso ya lo hacer el método Modelo.consulta()
		Integer pres=null;
		while(rs.next()&&pres==null) {
			pres=rs.getInt("nro_prestamo");
		}
		return pres;
	
	}


	@Override
	public void crearPrestamo(PrestamoBean prestamo) throws Exception {
		logger.info("Crea un nuevo prestamo.");
		
		if (this.legajo == null) {
			throw new Exception("No hay un empleado registrado en el sistema que se haga responsable por este prestamo.");
		}
		else 
		{
			logger.info("Actualiza el prestamo con el legajo {}",this.legajo);
			prestamo.setLegajo(this.legajo);
			
			DAOPrestamo dao = new DAOPrestamoImpl(this.conexion);		
			dao.crearActualizarPrestamo(prestamo);
		}
	}
	
	@Override
	public PrestamoBean recuperarPrestamo(int nroPrestamo) throws Exception {
		logger.info("Busca el prestamo número {}", nroPrestamo);
		
		DAOPrestamo dao = new DAOPrestamoImpl(this.conexion);		
		return dao.recuperarPrestamo(nroPrestamo);
	}
	
	@Override
	public ArrayList<PagoBean> recuperarPagos(Integer prestamo) throws Exception {
		logger.info("Solicita la busqueda de pagos al modelo sobre el prestamo {}.", prestamo);
		
		DAOPago dao = new DAOPagoImpl(this.conexion);		
		return dao.recuperarPagos(prestamo);
	}
	

	@Override
	public void pagarCuotas(String p_tipo, int p_dni, int nroPrestamo, List<Integer> cuotasAPagar) throws Exception {
		
		// Valida que sea un cliente que exista sino genera una excepción
		ClienteBean c = this.recuperarCliente(p_tipo.trim(), p_dni);

		// Valida el prestamo
		if (nroPrestamo != this.prestamoVigente(c.getNroCliente())) {
			throw new Exception ("El nro del prestamo no coincide con un prestamo vigente del cliente");
		}

		if (cuotasAPagar.size() == 0) {
			throw new Exception ("Debe seleccionar al menos una cuota a pagar.");
		}
		
		DAOPago dao = new DAOPagoImpl(this.conexion);
		dao.registrarPagos(c.getNroCliente(), nroPrestamo, cuotasAPagar);		
	}


	@Override
	public ArrayList<ClienteMorosoBean> recuperarClientesMorosos() throws Exception {
		logger.info("Modelo solicita al DAO que busque los clientes morosos");
		DAOClienteMoroso dao = new DAOClienteMorosoImpl(this.conexion);
		return dao.recuperarClientesMorosos();	
	}
	

	
}
