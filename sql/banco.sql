CREATE DATABASE banco;

USE banco;



#-------------------------------------------------------------------------
# Creación Tablas 
#-------------------------------------------------------------------------



CREATE TABLE Ciudad (
	nombre VARCHAR(45) NOT NULL,
	cod_postal SMALLINT UNSIGNED NOT NULL,
	

	CONSTRAINT pk_ciudad
	PRIMARY KEY (cod_postal)

) ENGINE=InnoDB;


CREATE TABLE Sucursal (
	nro_suc SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
	nombre VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	telefono VARCHAR(25) NOT NULL,
	horario VARCHAR(45) NOT NULL,
	cod_postal SMALLINT UNSIGNED NOT NULL,


	CONSTRAINT pk_sucursal
	PRIMARY KEY (nro_suc),

	CONSTRAINT fk_sucursal_ciudad
	FOREIGN KEY (cod_postal) REFERENCES Ciudad (cod_postal)
		ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE Empleado (
	legajo SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
	apellido VARCHAR(45) NOT NULL, 
	nombre VARCHAR(45) NOT NULL,
	tipo_doc VARCHAR(20) NOT NULL,
	nro_doc INT UNSIGNED NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	telefono VARCHAR(25) NOT NULL,
	cargo VARCHAR(45) NOT NULL,
	password VARCHAR(32) NOT NULL,
	nro_suc SMALLINT UNSIGNED NOT NULL,


	CONSTRAINT pk_empleado
	PRIMARY KEY (legajo),

	CONSTRAINT fk_empleado_sucursal
	FOREIGN KEY (nro_suc) REFERENCES Sucursal (nro_suc)
		ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE Cliente (
	nro_cliente INT UNSIGNED NOT NULL AUTO_INCREMENT,
	apellido VARCHAR(45) NOT NULL, 
	nombre VARCHAR(45) NOT NULL,
	tipo_doc VARCHAR(20) NOT NULL,
	nro_doc INT UNSIGNED NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	telefono VARCHAR(25) NOT NULL,
	fecha_nac DATE NOT NULL,


	CONSTRAINT pk_cliente
	PRIMARY KEY (nro_cliente)

) ENGINE=InnoDB;


CREATE TABLE Plazo_Fijo (
	nro_plazo INT UNSIGNED NOT NULL AUTO_INCREMENT,
	capital DECIMAL(16,2) UNSIGNED NOT NULL,
	fecha_inicio DATE NOT NULL,
	fecha_fin DATE NOT NULL,
	tasa_interes DECIMAL(4,2) UNSIGNED NOT NULL,
	interes DECIMAL(16,2) UNSIGNED NOT NULL,
	nro_suc SMALLINT UNSIGNED NOT NULL,

	
	CONSTRAINT pk_plazo
	PRIMARY KEY (nro_plazo),

	CONSTRAINT fk_plazo_sucursal
	FOREIGN KEY (nro_suc) REFERENCES Sucursal (nro_suc)
		ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE Tasa_Plazo_Fijo (
	periodo SMALLINT UNSIGNED NOT NULL,
	monto_inf DECIMAL(16,2) UNSIGNED NOT NULL,
	monto_sup DECIMAL(16,2) UNSIGNED NOT NULL,
	tasa DECIMAL(4,2) UNSIGNED NOT NULL,
	

	CONSTRAINT pk_tasa_plazo_fijo
	PRIMARY KEY (periodo, monto_sup, monto_inf)

) ENGINE=InnoDB;


CREATE TABLE Plazo_Cliente (
	nro_plazo INT UNSIGNED NOT NULL,
	nro_cliente INT UNSIGNED NOT NULL,


	CONSTRAINT pk_plazo_cliente
	PRIMARY KEY (nro_plazo, nro_cliente),
	
	CONSTRAINT fk_plazo_cliente_plazo_fijo
	FOREIGN KEY (nro_plazo) REFERENCES Plazo_Fijo (nro_plazo)
		ON DELETE RESTRICT ON UPDATE CASCADE,

	CONSTRAINT fk_plazo_cliente_cliente
	FOREIGN KEY (nro_cliente) REFERENCES Cliente (nro_cliente)
		ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE Prestamo (
	nro_prestamo INT UNSIGNED NOT NULL AUTO_INCREMENT,
	fecha DATE NOT NULL,
	cant_meses TINYINT UNSIGNED NOT NULL,
	monto DECIMAL(10,2) UNSIGNED NOT NULL,
	tasa_interes DECIMAL(4,2) UNSIGNED NOT NULL,
	interes DECIMAL(9,2) UNSIGNED NOT NULL,
	valor_cuota DECIMAL(9,2) UNSIGNED NOT NULL,
	legajo SMALLINT UNSIGNED NOT NULL,
	nro_cliente INT UNSIGNED NOT NULL,
	

	CONSTRAINT pk_prestamo
	PRIMARY KEY (nro_prestamo),

	CONSTRAINT fk_prestamo_empleado
	FOREIGN KEY (legajo) REFERENCES Empleado (legajo)
		ON DELETE RESTRICT ON UPDATE CASCADE,

	CONSTRAINT fk_prestamo_cliente
	FOREIGN KEY (nro_cliente) REFERENCES Cliente (nro_cliente)
		ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE Pago (
	nro_prestamo INT UNSIGNED NOT NULL,
	nro_pago SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
	fecha_venc DATE NOT NULL,
	fecha_pago DATE,


	CONSTRAINT pk_pago
	PRIMARY KEY (nro_pago, nro_prestamo),

	CONSTRAINT fk_pago_prestamo
	FOREIGN KEY (nro_prestamo) REFERENCES Prestamo (nro_prestamo)

) ENGINE=InnoDB;


CREATE TABLE Tasa_Prestamo (
	periodo SMALLINT UNSIGNED NOT NULL,
	monto_inf DECIMAL(10,2) UNSIGNED NOT NULL,
	monto_sup DECIMAL(10,2) UNSIGNED NOT NULL,
	tasa DECIMAL(4,2) UNSIGNED NOT NULL,


	CONSTRAINT pk_tasa_prestamo
	PRIMARY KEY (periodo, monto_inf, monto_sup)

) ENGINE=InnoDB;


CREATE TABLE Caja_Ahorro (
	nro_ca INT UNSIGNED NOT NULL AUTO_INCREMENT,
	CBU BIGINT UNSIGNED NOT NULL,
	saldo DECIMAL(16,2) UNSIGNED NOT NULL,


	CONSTRAINT pk_caja_ahorro
	PRIMARY KEY (nro_ca)

) ENGINE=InnoDB;


CREATE TABLE Cliente_CA (
	nro_cliente INT UNSIGNED NOT NULL,
	nro_ca INT UNSIGNED NOT NULL,

	
	CONSTRAINT pk_cliente_ca
	PRIMARY KEY(nro_cliente, nro_ca),

	CONSTRAINT fk_cliente_ca_cliente
	FOREIGN KEY (nro_cliente) REFERENCES Cliente (nro_cliente)
		ON DELETE RESTRICT ON UPDATE CASCADE,

	CONSTRAINT fk_cliente_ca_caja_ahorro
	FOREIGN KEY (nro_ca) REFERENCES Caja_Ahorro (nro_ca)
		ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE Tarjeta (
	nro_tarjeta BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	PIN VARCHAR(32) NOT NULL,
	CVT VARCHAR(32) NOT NULL,
	fecha_venc DATE NOT NULL,
	nro_cliente INT UNSIGNED NOT NULL,
	nro_ca INT UNSIGNED NOT NULL,


	CONSTRAINT pk_tarjeta
	PRIMARY KEY (nro_tarjeta),
	
	CONSTRAINT fk_tarjeta_cliente_ca_cliente
	FOREIGN KEY (nro_cliente, nro_ca) REFERENCES Cliente_CA (nro_cliente, nro_ca)
		ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;



CREATE TABLE Caja (
	cod_caja INT UNSIGNED NOT NULL AUTO_INCREMENT,


	CONSTRAINT pk_caja
	PRIMARY KEY (cod_caja)
	
) ENGINE=InnoDB;


CREATE TABLE Ventanilla (
	cod_caja INT UNSIGNED NOT NULL,
	nro_suc SMALLINT UNSIGNED NOT NULL,


	CONSTRAINT pk_ventanilla
	PRIMARY KEY (cod_caja),

	CONSTRAINT fk_ventanilla_cod_caja
	FOREIGN KEY (cod_caja) REFERENCES Caja (cod_caja)
		ON DELETE RESTRICT ON UPDATE CASCADE,

	CONSTRAINT fk_ventanilla_sucursal
	FOREIGN KEY (nro_suc) REFERENCES Sucursal (nro_suc)
		ON DELETE RESTRICT ON UPDATE CASCADE
	
) ENGINE=InnoDB;


CREATE TABLE ATM (
	cod_caja INT UNSIGNED NOT NULL,
	cod_postal SMALLINT UNSIGNED NOT NULL,
	direccion VARCHAR(45) NOT NULL,


	CONSTRAINT pk_atm
	PRIMARY KEY (cod_caja),

	CONSTRAINT fk_atm_cod_caja
	FOREIGN KEY (cod_caja) REFERENCES Caja (cod_caja)
		ON DELETE RESTRICT ON UPDATE CASCADE,

	CONSTRAINT fk_atm_ciudad
	FOREIGN KEY (cod_postal) REFERENCES Ciudad (cod_postal)
		ON DELETE RESTRICT ON UPDATE CASCADE
	
) ENGINE=InnoDB;


CREATE TABLE Transaccion (
	nro_trans BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	fecha DATE NOT NULL,
	hora TIME NOT NULL,
	monto DECIMAL(16,2) UNSIGNED NOT NULL,


	CONSTRAINT pk_transaccion
	PRIMARY KEY (nro_trans)

) ENGINE=InnoDB;


CREATE TABLE Debito (
	nro_trans BIGINT UNSIGNED NOT NULL,
	descripcion TEXT,
	nro_cliente INT UNSIGNED NOT NULL,
	nro_ca INT UNSIGNED NOT NULL,


	CONSTRAINT pk_debito
	PRIMARY KEY (nro_trans),

	CONSTRAINT fk_debito_transaccion
	FOREIGN KEY (nro_trans) REFERENCES Transaccion (nro_trans)
		ON DELETE RESTRICT ON UPDATE CASCADE,

	CONSTRAINT fk_debito_cliente_ca_cliente
	FOREIGN KEY (nro_cliente, nro_ca) REFERENCES Cliente_CA (nro_cliente,nro_ca)
		ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE Transaccion_por_caja (
	nro_trans BIGINT UNSIGNED NOT NULL,
	cod_caja INT UNSIGNED NOT NULL,


	CONSTRAINT pk_transaccion_por_caja
	PRIMARY KEY (nro_trans),

	CONSTRAINT fk_transaccion_por_caja_transaccion
	FOREIGN KEY (nro_trans) REFERENCES Transaccion (nro_trans)
		ON DELETE RESTRICT ON UPDATE CASCADE,

	CONSTRAINT fk_transaccion_por_caja_caja
	FOREIGN KEY (cod_caja) REFERENCES Caja (cod_caja)
		ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE Deposito (
	nro_trans BIGINT UNSIGNED NOT NULL,
	nro_ca INT UNSIGNED NOT NULL,


	CONSTRAINT pk_deposito_transaccion_por_caja
	PRIMARY KEY (nro_trans),

	CONSTRAINT fk_deposito_transaccion_por_caja
	FOREIGN KEY (nro_trans) REFERENCES Transaccion_por_caja (nro_trans)
		ON DELETE RESTRICT ON UPDATE CASCADE,

	CONSTRAINT fk_deposito_caja_ahorro
	FOREIGN KEY (nro_ca) REFERENCES Caja_Ahorro (nro_ca)
		ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE Extraccion (
	nro_trans BIGINT UNSIGNED NOT NULL,
	nro_cliente INT UNSIGNED NOT NULL,
	nro_ca INT UNSIGNED NOT NULL,


	CONSTRAINT pk_extraccion
	PRIMARY KEY (nro_trans),

	CONSTRAINT fk_extraccion_transaccion_por_caja
	FOREIGN KEY (nro_trans) REFERENCES Transaccion_por_caja (nro_trans)
		ON DELETE RESTRICT ON UPDATE CASCADE,

	CONSTRAINT fk_extraccion_cliente_ca_cliente
	FOREIGN KEY (nro_cliente, nro_ca) REFERENCES Cliente_CA (nro_cliente,nro_ca)
		ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE Transferencia (
	nro_trans BIGINT UNSIGNED NOT NULL,
	nro_cliente INT UNSIGNED NOT NULL,
	origen INT UNSIGNED NOT NULL,
	destino INT UNSIGNED NOT NULL,


	CONSTRAINT pk_transferencia
	PRIMARY KEY (nro_trans),

	CONSTRAINT fk_transferencia_transaccion_por_caja
	FOREIGN KEY (nro_trans) REFERENCES Transaccion_por_caja (nro_trans)
		ON DELETE RESTRICT ON UPDATE CASCADE,

	CONSTRAINT fk_transferencia_cliente_ca_cliente
	FOREIGN KEY (nro_cliente,origen) REFERENCES Cliente_CA (nro_cliente,nro_ca)
		ON DELETE RESTRICT ON UPDATE CASCADE,

	CONSTRAINT fk_transferencia_caja_ahorro
	FOREIGN KEY (destino) REFERENCES Caja_Ahorro (nro_ca)
		ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;



#-------------------------------------------------------------------------
# Creación de procedimientos
#-------------------------------------------------------------------------



delimiter !
CREATE PROCEDURE realizar_extraccion(IN clienteIN INT, IN monto DECIMAL(16,2))
BEGIN

	DECLARE saldo_actual_cuentaIN DECIMAL(16,2);
	DECLARE cuentaIN INT;


	DECLARE codigo_SQL  CHAR(5) DEFAULT '00000';	 
	DECLARE codigo_MYSQL INT DEFAULT 0;
	DECLARE mensaje_error TEXT;

	DECLARE EXIT HANDLER FOR SQLEXCEPTION 	 	 
  	BEGIN
		GET DIAGNOSTICS CONDITION 1  codigo_MYSQL= MYSQL_ERRNO, codigo_SQL= RETURNED_SQLSTATE, mensaje_error= MESSAGE_TEXT;
	    SELECT 'SQLEXCEPTION!, transacción abortada' AS resultado, codigo_MySQL, codigo_SQL,  mensaje_error;		
    	ROLLBACK;
	END;	

	START TRANSACTION;

	IF EXISTS(SELECT * FROM Cliente_CA WHERE nro_cliente=clienteIN)
	THEN

		SELECT nro_ca INTO cuentaIN FROM Cliente_CA WHERE nro_cliente=clienteIN LOCK IN SHARE MODE;
		SELECT saldo INTO saldo_actual_cuentaIN FROM Caja_Ahorro WHERE nro_ca=cuentaIN FOR UPDATE;

		IF saldo_actual_cuentaIN >= monto THEN
			UPDATE Caja_Ahorro SET saldo = saldo - monto  WHERE nro_ca=cuentaIN;
			SELECT 'exito' AS resultado;
	    ELSE
	    	SELECT 'Saldo insuficiente' AS resultado;
	    END IF;
	ELSE
		SELECT 'cliente inexistente' AS resultado;
	END IF;
		
	COMMIT;
END; !
delimiter ;



delimiter !
CREATE PROCEDURE realizar_transferencia(IN clienteIN INT, IN nro_ca_destino INT, IN monto DECIMAL(16,2))
BEGIN

		DECLARE saldo_actual_cuentaIN DECIMAL(16,2);
		DECLARE cuentaIN INT;


		DECLARE codigo_SQL  CHAR(5) DEFAULT '00000';	 
		DECLARE codigo_MYSQL INT DEFAULT 0;
		DECLARE mensaje_error TEXT;

    	DECLARE EXIT HANDLER FOR SQLEXCEPTION 	 	 
	  	BEGIN
			GET DIAGNOSTICS CONDITION 1  codigo_MYSQL= MYSQL_ERRNO, codigo_SQL= RETURNED_SQLSTATE, mensaje_error= MESSAGE_TEXT;
		    SELECT 'SQLEXCEPTION!, transacción abortada' AS resultado, codigo_MySQL, codigo_SQL,  mensaje_error;		
        	ROLLBACK;
		END;	

	START TRANSACTION;
		IF EXISTS(SELECT * FROM Cliente_CA WHERE nro_cliente=clienteIN) AND
			EXISTS(SELECT * FROM Caja_Ahorro WHERE nro_ca=nro_ca_destino)
		THEN
			SELECT nro_ca INTO cuentaIN FROM Cliente_CA WHERE nro_cliente=clienteIN LOCK IN SHARE MODE;
			SELECT saldo INTO saldo_actual_cuentaIN FROM Caja_Ahorro WHERE nro_ca=cuentaIN FOR UPDATE;

			IF saldo_actual_cuentaIN >= monto THEN
		        UPDATE Caja_Ahorro SET saldo = saldo - monto  WHERE nro_ca=cuentaIN;
	    	    UPDATE cuentas SET saldo = saldo + monto  WHERE numero=nro_ca_destino;
				SELECT 'exito' AS resultado;	    	ELSE
	    		SELECT 'Saldo insuficiente' AS resultado;
	    	END IF;

	    ELSE
			SELECT 'cliente inexistente' AS resultado;
	    END IF;		
	COMMIT;
END; !
delimiter ;



#-------------------------------------------------------------------------
# Creación de triggers
#-------------------------------------------------------------------------


delimiter !
CREATE TRIGGER cargar_pagos AFTER INSERT ON Prestamo FOR EACH ROW
BEGIN
	DECLARE cnt INT DEFAULT 1;
	WHILE cnt <= NEW.cant_meses DO	
   		INSERT INTO Pago(nro_prestamo, nro_pago, fecha_venc, fecha_pago) 
   		VALUES(NEW.nro_prestamo, cnt, date_add(NEW.fecha, interval cnt month), NULL);
   		
   		SET cnt = cnt + 1;
	END WHILE;
END; !
delimiter ;


#-------------------------------------------------------------------------
# Creación de vistas 
#-------------------------------------------------------------------------



CREATE VIEW trans_cajas_ahorro AS (
	SELECT nro_ca, saldo, nro_trans, fecha, hora, "Debito" AS tipo, monto, NULL AS cod_caja, nro_cliente, tipo_doc, nro_doc, nombre, apellido, NULL AS destino
	FROM (Cliente_CA NATURAL JOIN Cliente NATURAL JOIN Caja_Ahorro NATURAL JOIN Debito NATURAL JOIN Transaccion)

	UNION

	SELECT nro_ca, saldo, nro_trans, fecha, hora, "Extraccion" AS tipo, monto, cod_caja, nro_cliente, tipo_doc, nro_doc, nombre, apellido, NULL AS destino
	FROM (Cliente_CA NATURAL JOIN Cliente NATURAL JOIN Caja_Ahorro NATURAL JOIN Extraccion NATURAL JOIN Transaccion_por_caja NATURAL JOIN Transaccion)

	UNION

	SELECT nro_ca, saldo, nro_trans, fecha, hora, "Transferencia" AS tipo, monto, cod_caja, nro_cliente, tipo_doc, nro_doc, nombre, apellido, destino
	FROM (Cliente_CA NATURAL JOIN Cliente NATURAL JOIN Caja_Ahorro NATURAL JOIN Transferencia NATURAL JOIN Transaccion_por_caja NATURAL JOIN Transaccion)
	WHERE (nro_ca = origen)

	UNION

	SELECT nro_ca, saldo, nro_trans, fecha, hora, "Deposito" AS tipo, monto, cod_caja, NULL AS nro_cliente, NULL AS tipo_doc, NULL AS nro_doc, NULL AS nombre, NULL AS apellido, NULL AS destino
	FROM (Caja_Ahorro NATURAL JOIN Deposito NATURAL JOIN Transaccion_por_caja NATURAL JOIN Transaccion)

);



#-------------------------------------------------------------------------
# Creación de usuarios y otorgamiento de privilegios
#-------------------------------------------------------------------------



CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';


GRANT ALL PRIVILEGES ON banco.* TO 'admin'@'localhost' WITH GRANT OPTION;



CREATE USER 'empleado'@'%' IDENTIFIED BY 'empleado';


GRANT SELECT ON banco.Empleado TO 'empleado'@'%';

GRANT SELECT ON banco.Sucursal TO 'empleado'@'%';

GRANT SELECT ON banco.Tasa_Plazo_Fijo TO 'empleado'@'%';

GRANT SELECT ON banco.Tasa_Prestamo TO 'empleado'@'%';


GRANT SELECT, INSERT ON banco.Prestamo TO 'empleado'@'%';

GRANT SELECT, INSERT ON banco.Plazo_Fijo TO 'empleado'@'%';

GRANT SELECT, INSERT ON banco.Plazo_Cliente TO 'empleado'@'%';

GRANT SELECT, INSERT ON banco.Caja_Ahorro TO 'empleado'@'%';

GRANT SELECT, INSERT ON banco.Tarjeta TO 'empleado'@'%';


GRANT SELECT, INSERT, UPDATE ON banco.Cliente_CA TO 'empleado'@'%';

GRANT SELECT, INSERT, UPDATE ON banco.Cliente TO 'empleado'@'%';

GRANT SELECT, INSERT, UPDATE ON banco.Pago TO 'empleado'@'%';
	


CREATE USER 'atm'@'%' IDENTIFIED BY 'atm';

GRANT SELECT, UPDATE ON banco.Tarjeta TO 'atm'@'%';


GRANT SELECT ON banco.trans_cajas_ahorro TO 'atm'@'%';