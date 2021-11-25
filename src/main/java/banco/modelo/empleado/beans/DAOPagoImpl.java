package banco.modelo.empleado.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import banco.utils.Fechas;


public class DAOPagoImpl implements DAOPago {

	private static Logger logger = LoggerFactory.getLogger(DAOPagoImpl.class);
	
	private Connection conexion;
	
	public DAOPagoImpl(Connection c) {
		this.conexion = c;
	}

	@Override
	public ArrayList<PagoBean> recuperarPagos(int nroPrestamo) throws Exception {
		logger.info("Inicia la recuperacion de los pagos del prestamo {}", nroPrestamo);
		
		/** 
		 * TODO Recupera todos los pagos del prestamo (pagos e impagos) del prestamo nroPrestamo
		 * 	    Si ocurre algún error deberá propagar una excepción.
		 */
		
		 
		java.sql.Statement st = this.conexion.createStatement();
		String query="SELECT nro_pago, fecha_venc, fecha_pago FROM Pago WHERE nro_prestamo= "+nroPrestamo+";";
		java.sql.ResultSet rs = st.executeQuery(query);
		
		ArrayList<PagoBean> lista = new ArrayList<PagoBean>();
		
		PagoBean fila;
		
		while(rs.next()){
		fila= new PagoBeanImpl();
		fila.setNroPrestamo(nroPrestamo);
		fila.setNroPago(rs.getInt("nro_pago"));
		fila.setFechaVencimiento(Fechas.convertirStringADate(rs.getString("fecha_venc")));
		fila.setFechaPago(Fechas.convertirStringADate(rs.getString("fecha_pago")));
		lista.add(fila);
		}
	
		rs.close();
		st.close();
		return lista;
	}

	@Override
	public void registrarPagos(int nroCliente, int nroPrestamo, List<Integer> cuotasAPagar)  throws Exception {

		logger.info("Inicia el pago de las {} cuotas del prestamo {}", cuotasAPagar.size(), nroPrestamo);

		/**
		 * TODO Registra los pagos de cuotas definidos en cuotasAPagar.
		 * 
		 * nroCliente asume que esta validado
		 * nroPrestamo asume que está validado
		 * cuotasAPagar Debe verificar que las cuotas a pagar no estén pagas (fecha_pago = NULL)
		 * @throws Exception Si hubo error en la conexión
		 */	
		try {
		this.conexion.setAutoCommit(false);
		String query2="UPDATE Pago SET fecha_pago= CURDATE() WHERE nro_pago=? AND nro_prestamo=? AND fecha_pago is NULL ;";		
		java.sql.PreparedStatement st2= this.conexion.prepareStatement(query2);
		for(Integer i:cuotasAPagar) {
			st2.setInt(1, i);
			st2.setInt(2, nroPrestamo);
			st2.executeUpdate();
			logger.info("Pago realizado");
		}
		st2.close();
		this.conexion.commit();
		}
		catch(SQLException e) {
			this.conexion.rollback();
			throw e;
		}
	}
}
