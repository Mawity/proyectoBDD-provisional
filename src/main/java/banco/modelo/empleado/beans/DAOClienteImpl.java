package banco.modelo.empleado.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import banco.utils.Fechas;

public class DAOClienteImpl implements DAOCliente {

	private static Logger logger = LoggerFactory.getLogger(DAOClienteImpl.class);
	
	private Connection conexion;
	
	public DAOClienteImpl(Connection c) {
		this.conexion = c;
	}
	
	@Override
	public ClienteBean recuperarCliente(String tipoDoc, int nroDoc) throws Exception {

		logger.info("recupera el cliente con documento de tipo {} y nro {}.", tipoDoc, nroDoc);
		
		/**
		 * TODO Recuperar el cliente que tenga un documento que se corresponda con los parámetros recibidos.  
		 *		Deberá generar o propagar una excepción si no existe dicho cliente o hay un error de conexión.
		 * Dejo el TODO por si hay algún error, propago la excepción que tiran el createStatement() o el executeQuery()
		 */
		
		
		ClienteBean cliente = new ClienteBeanImpl();
		java.sql.Statement st = this.conexion.createStatement();
		String query="SELECT nro_cliente, apellido, nombre, direccion, telefono, fecha_nac FROM Cliente WHERE tipo_doc= "+tipoDoc+" AND nro_doc= "+nroDoc+";";
		java.sql.ResultSet rs = st.executeQuery(query);
		cliente.setNroCliente(rs.getInt("nro_cliente"));
		cliente.setApellido(rs.getString("apellido"));
		cliente.setNombre(rs.getString("nombre"));
		cliente.setTipoDocumento(tipoDoc);
		cliente.setNroDocumento(nroDoc);
		cliente.setDireccion(rs.getString("direccion"));
		cliente.setTelefono(rs.getString("telefono"));
		cliente.setFechaNacimiento(Fechas.convertirStringADate(rs.getString("fecha_nac")));
		rs.close();
		st.close();
	
		return cliente;		

	}

	@Override
	public ClienteBean recuperarCliente(Integer nroCliente) throws Exception {
		logger.info("recupera el cliente por nro de cliente.");
		
		/**
		 * TODO Recuperar el cliente que tenga un documento que se corresponda con los parámetros recibidos.  
		 *		Deberá generar o propagar una excepción si no existe dicho cliente o hay un error de conexión.
		 * Dejo el TODO por si hay algún error, propago la excepción que tiran el createStatement() o el executeQuery()
		 */
		 
		ClienteBean cliente = new ClienteBeanImpl();
		java.sql.Statement st = this.conexion.createStatement();
		String query="SELECT apellido, nombre, tipo_doc, nro_doc, direccion, telefono, fecha_nac FROM Cliente WHERE nro_cliente= "+nroCliente+";";
		java.sql.ResultSet rs = st.executeQuery(query);
		cliente.setNroCliente(nroCliente);
		cliente.setApellido(rs.getString("apellido"));
		cliente.setNombre(rs.getString("nombre"));
		cliente.setTipoDocumento(rs.getString("tipo_doc"));
		cliente.setNroDocumento(rs.getInt("nro_doc"));
		cliente.setDireccion(rs.getString("direccion"));
		cliente.setTelefono(rs.getString("telefono"));
		cliente.setFechaNacimiento(Fechas.convertirStringADate(rs.getString("fecha_nac")));
		rs.close();
		st.close();
		
		return cliente;		

	}

}
