package banco.modelo.empleado.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import banco.utils.Fechas;

public class DAOPrestamoImpl implements DAOPrestamo {

	private static Logger logger = LoggerFactory.getLogger(DAOPrestamoImpl.class);
	
	private Connection conexion;
	
	public DAOPrestamoImpl(Connection c) {
		this.conexion = c;
	}
	
	
	@Override
	public void crearActualizarPrestamo(PrestamoBean prestamo) throws Exception {
		
		
		logger.info("Creación o actualizacion del prestamo.");
		try {
		this.conexion.setAutoCommit(false);
		String insert="INSERT INTO Prestamo (nro_prestamo, fecha, cant_meses, monto, tasa_interes, interes, valor_cuota, legajo, nro_cliente) VALUES (?, ?, ?,? ,?, ?, ?, ?, ?);";
		java.sql.PreparedStatement st = this.conexion.prepareStatement(insert);
		st.setInt(1,prestamo.getNroPrestamo());
		st.setDate(2,Fechas.convertirDateADateSQL(prestamo.getFecha()));
		logger.debug("meses : {}", prestamo.getCantidadMeses());
		st.setInt(3,prestamo.getCantidadMeses());
		logger.debug("monto : {}", prestamo.getMonto());
		st.setDouble(4,prestamo.getMonto());
		logger.debug("tasa : {}", prestamo.getTasaInteres());
		st.setDouble(5,prestamo.getTasaInteres());
		logger.debug("interes : {}", prestamo.getInteres());
		st.setDouble(6,prestamo.getInteres());
		logger.debug("cuota : {}", prestamo.getValorCuota());
		st.setDouble(7,prestamo.getValorCuota());
		logger.debug("legajo : {}", prestamo.getLegajo());
		st.setInt(8,prestamo.getLegajo());
		logger.debug("cliente : {}", prestamo.getNroCliente());
		st.setInt(9,prestamo.getNroCliente());
		st.executeUpdate();
		st.close();
		this.conexion.commit();
		} 
		catch(SQLException e) {
			this.conexion.rollback();
			throw e;
		}
		/**
		 * TODO Crear o actualizar el Prestamo segun el PrestamoBean prestamo. 
		 *      Si prestamo tiene nroPrestamo es una actualizacion, si el nroPrestamo es null entonces es un nuevo prestamo.
		 * 
		 * @throws Exception deberá propagar la excepción si ocurre alguna. Puede capturarla para loguear los errores, ej.
		 *				logger.error("SQLException: " + ex.getMessage());
		 * 				logger.error("SQLState: " + ex.getSQLState());
		 *				logger.error("VendorError: " + ex.getErrorCode());
		 *		   pero luego deberá propagarla para que se encargue el controlador. 
		 */

	}

	@Override
	public PrestamoBean recuperarPrestamo(int nroPrestamo) throws Exception {
		
		logger.info("Recupera el prestamo nro {}.", nroPrestamo);
		
		/**
		 * TODO Obtiene el prestamo según el id nroPrestamo
		 * 
		 * @param nroPrestamo
		 * @return Un prestamo que corresponde a ese id o null
		 * @throws Exception si hubo algun problema de conexión
		 */		

		PrestamoBean prestamo = null;
		
		java.sql.Statement st = this.conexion.createStatement();
		String query="SELECT * FROM Prestamo WHERE nro_prestamo= "+nroPrestamo+";";
		java.sql.ResultSet rs = st.executeQuery(query);
		prestamo = new PrestamoBeanImpl();
		prestamo.setNroPrestamo(nroPrestamo);
		prestamo.setFecha(Fechas.convertirStringADate(rs.getString("fecha")));
		prestamo.setCantidadMeses(rs.getInt("cant_meses"));
		prestamo.setMonto(rs.getDouble("monto"));
		prestamo.setTasaInteres(rs.getDouble("tasa_interes"));
		prestamo.setInteres(rs.getDouble("interes"));
		prestamo.setValorCuota(rs.getDouble("valor_cuota"));
		prestamo.setLegajo(rs.getInt("legajo"));
		prestamo.setNroCliente(rs.getInt("nro_cliente"));
		rs.close();
		st.close();
		return prestamo;
	}

}
