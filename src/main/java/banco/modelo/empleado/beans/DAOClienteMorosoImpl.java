package banco.modelo.empleado.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import banco.utils.Fechas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DAOClienteMorosoImpl implements DAOClienteMoroso {

	private static Logger logger = LoggerFactory.getLogger(DAOClienteMorosoImpl.class);
	
	private Connection conexion;
	
	public DAOClienteMorosoImpl(Connection c) {
		this.conexion = c;
	}
	
	@Override
	public ArrayList<ClienteMorosoBean> recuperarClientesMorosos() throws Exception {
		
		logger.info("Busca los clientes morosos.");
		
		DAOPrestamo daoPrestamo = new DAOPrestamoImpl(this.conexion);		
		DAOCliente daoCliente = new DAOClienteImpl(this.conexion);
		
		/**
		 * TODO Deberá recuperar un listado de clientes morosos los cuales consisten de un bean ClienteMorosoBeanImpl
		 *      deberá indicar para dicho cliente cual es el prestamo sobre el que está moroso y la cantidad de cuotas que 
		 *      tiene atrasadas. En todos los casos deberá generar excepciones que será capturadas por el controlador
		 *      si hay algún error que necesita ser informado al usuario. 
		 */
		
		ArrayList<ClienteMorosoBean> morosos = new ArrayList<ClienteMorosoBean>();
		PrestamoBean prestamo = null;
		ClienteBean cliente = null;
		
		java.sql.Statement st = this.conexion.createStatement();
		String query="SELECT nro_prestamo, COUNT(fecha_venc) FROM Prestamo NATURAL JOIN Pago WHERE fecha_pago IS NULL AND fecha_venc < CURDATE() GROUP BY nro_prestamo HAVING COUNT(fecha_venc) > 1;";
		java.sql.ResultSet rs = st.executeQuery(query);
		
		while(rs.next()){
		ClienteMorosoBean moroso = new ClienteMorosoBeanImpl();
		prestamo = daoPrestamo.recuperarPrestamo(rs.getInt("nro_prestamo")); // El prestamo 1 tiene cuotas atrasadas - valor que deberá ser obtenido por la SQL
		cliente = daoCliente.recuperarCliente(prestamo.getNroCliente());
		moroso.setCliente(cliente);
		moroso.setPrestamo(prestamo);
		moroso.setCantidadCuotasAtrasadas(rs.getInt("COUNT(fecha_venc)"));  //valor que deberá ser obtenido por la SQL
		morosos.add(moroso);
		}
		
		rs.close();
		st.close();
		return morosos;		
		
	}

}

