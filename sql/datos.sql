

USE banco;


INSERT INTO Ciudad VALUES("Bahía Blanca",8000);
INSERT INTO Ciudad VALUES("Buenos Aires",1160);
INSERT INTO Ciudad VALUES("Marcos Paz",1727);
INSERT INTO Ciudad VALUES("Quilmes",1878);
INSERT INTO Ciudad VALUES("La Plata",1900);
INSERT INTO Ciudad VALUES("Tandil",7000);
INSERT INTO Ciudad VALUES("Córdoba",5000);
INSERT INTO Ciudad VALUES("Rosario",2000);
INSERT INTO Ciudad VALUES("Mendoza",5500);
INSERT INTO Ciudad VALUES("Bariloche",8400);



INSERT INTO Sucursal VALUES(321,"Sucursal CABA 27","Belgrano 1449","115252424","7 a 13, 16 a 21",1160);
INSERT INTO Sucursal VALUES(594,"Sucursal CABA 13","Sarmiento 2949","115377521","7 a 14, 16 a 21",1160);
INSERT INTO Sucursal VALUES(648,"Sucursal Bahia 2","Alem 3149","29155867924","7 a 13, 16 a 20",8000);
INSERT INTO Sucursal VALUES(112,"Sucursal LP Centro","Calle 17, 221","645353533","9 a 21:30",1900);
INSERT INTO Sucursal VALUES(532,"Sucursal M4","Chacabuco 3212","535354950","8 a 13, 16 a 22",5500);
INSERT INTO Sucursal VALUES(581,"Sucursal B2","Bustillo 14450","305860354","8 a 19:30",8400);


INSERT INTO Empleado VALUES(2490,"Benitez","Alberto","Libreta Verde",47389234,"San Martín 2239","563436412","Asistente de Secretaría",md5("43298374ba"),112);
INSERT INTO Empleado VALUES(1495,"Santos Herrera","Martín Alejandro","DNI",89549382,"Almirante Brown 439","7533245612","Gerente de Sede",md5("b832cc19"),581);
INSERT INTO Empleado VALUES(5831,"Morales","Roxana","DNI",54902131,"Sarmiento 3213","53636123","Directora de RH",md5("210981abce"),594);
INSERT INTO Empleado VALUES(6942,"Mateos","Gabriel Hernán","DNI",69503134,"Italia 620","635345612","Recepcionista",md5("fjdk4313"),321);
INSERT INTO Empleado VALUES(1591,"Ambrossi","Giovanni","DNI",54219382,"Chacabuco 2394","5492345","Secretario general de sede",md5("987876765654213b"),648);


INSERT INTO Cliente VALUES(38594,"Albarado","Juan José","DNI",67394130,"Estomba 1983","29148372929","1963/03/21");
INSERT INTO Cliente VALUES(47245,"Ferrero","Hernando","DNI",58349064,"Belgrano 3941","11583740321","2024/11/17");
INSERT INTO Cliente VALUES(58491,"Genovesi","María Doleres","Libreta Verde",58943123,"Caseros 721","11583675321","1982/04/13");
INSERT INTO Cliente VALUES(13593,"Del Campo","María Emilia","DNI",75432943,"Av. Asunción 4721","5863675321","2001/09/21");
INSERT INTO Cliente VALUES(67382,"Leonetti","Ignacio","DNI",93445785,"Portugal 982","6421475321","2003/01/16");
INSERT INTO Cliente VALUES(51493,"Pereyra","Santino","Libreta Celeste",68329330,"Calle 31, 687","57383941","1942/03/13");


INSERT INTO Plazo_Fijo VALUES(47583132,2454.21,"2032/01/21","2041/07/22",0.13,0.21,581);
INSERT INTO Plazo_Fijo VALUES(57894302,59403.32,"2011/08/11","2020/08/10",0.1,0.25,648);
INSERT INTO Plazo_Fijo VALUES(69580389,2.21,"2029/01/13","2031/02/11",0.21,0.3,594);
INSERT INTO Plazo_Fijo VALUES(69402141,65392.20,"2011/11/11","2022/11/22",0.3,0.15,321);
INSERT INTO Plazo_Fijo VALUES(59302342,24543.22,"2043/03/23","2046/02/21",0.12,0.23,594);
INSERT INTO Plazo_Fijo VALUES(10394246,246.21,"1989/02/03","2054/01/23",0.2,0.26,112);
INSERT INTO Plazo_Fijo VALUES(89203543,24432.13,"2022/01/20","2044/03/21",0.33,0.41,581);
INSERT INTO Plazo_Fijo VALUES(79039104,8823.69,"2032/01/21","2041/07/22",0.17,0.65,112);
INSERT INTO Plazo_Fijo VALUES(93294849,968241.58,"2012/12/21","2040/06/24",0.54,0.62,648);
INSERT INTO Plazo_Fijo VALUES(68391031,8629009.43,"2013/02/25","2021/07/11",0.15,0.11,532);

INSERT INTO Tasa_Plazo_Fijo VALUES(251,234.65,89925.21,0.21);
INSERT INTO Tasa_Plazo_Fijo VALUES(932,2693.12,15825.21,0.42);
INSERT INTO Tasa_Plazo_Fijo VALUES(146,2690.32,59275.21,0.11);
INSERT INTO Tasa_Plazo_Fijo VALUES(881,25925.59,69675.31,0.66);

INSERT INTO Plazo_Cliente VALUES(10394246,13593);
INSERT INTO Plazo_Cliente VALUES(93294849,47245);
INSERT INTO Plazo_Cliente VALUES(69580389,51493);
INSERT INTO Plazo_Cliente VALUES(59302342,13593);
INSERT INTO Plazo_Cliente VALUES(79039104,58491);
INSERT INTO Plazo_Cliente VALUES(69402141,67382);


INSERT INTO Prestamo VALUES(63526842,"2022/01/20",33,4738.53,0.22,0.13,353.25,1495,58491);
INSERT INTO Prestamo VALUES(58042145,"2040/06/24",72,6892.60,0.11,0.42,62.53,6942,13593);
INSERT INTO Prestamo VALUES(52469201,"2022/11/22",11,682959.42,0.14,0.21,5924.11,1591,67382);

INSERT INTO Pago VALUES(58042145,22,"2044/07/22","2044/07/21");
INSERT INTO Pago VALUES(58042145,23,"2044/08/25","2044/08/23");
INSERT INTO Pago VALUES(63526842,11,"2023/02/14","2023/02/03");

INSERT INTO Tasa_Prestamo VALUES(694,2639.65,896535.22,0.25);
INSERT INTO Tasa_Prestamo VALUES(592,2633.62,14225.11,0.22);
INSERT INTO Tasa_Prestamo VALUES(169,26290.32,529255.21,0.21);
INSERT INTO Tasa_Prestamo VALUES(701,28825.639,6920675.41,0.46);

INSERT INTO Caja_Ahorro VALUES(58925014,501450910398559036,57824.69);
INSERT INTO Caja_Ahorro VALUES(69257296,402858056729689206,56302.44);
INSERT INTO Caja_Ahorro VALUES(15935350,149505281055271914,5412.61);
INSERT INTO Caja_Ahorro VALUES(93841051,214525925852901451,524.29);
INSERT INTO Caja_Ahorro VALUES(92484241,194781034411558192,51.79);

INSERT INTO Cliente_CA VALUES(13593,58925014);
INSERT INTO Cliente_CA VALUES(13593,69257296);
INSERT INTO Cliente_CA VALUES(58491,15935350);
INSERT INTO Cliente_CA VALUES(47245,93841051);
INSERT INTO Cliente_CA VALUES(13593,92484241);


INSERT INTO Tarjeta VALUES(5279572192518445,md5("8391"),md5("391"),"2022/11/22",13593,69257296);
INSERT INTO Tarjeta VALUES(6829572915713145,md5("1495"),md5("492"),"2025/04/14",47245,93841051);
INSERT INTO Tarjeta VALUES(1495825799275294,md5("4914"),md5("692"),"2026/03/21",13593,92484241);

INSERT INTO Caja VALUES(48925);
INSERT INTO Caja VALUES(11449);
INSERT INTO Caja VALUES(58391);
INSERT INTO Caja VALUES(35928);
INSERT INTO Caja VALUES(49183);
INSERT INTO Caja VALUES(14592);
INSERT INTO Caja VALUES(48914);
INSERT INTO Caja VALUES(19683);

INSERT INTO Ventanilla VALUES(48925,112);
INSERT INTO Ventanilla VALUES(35928,532);
INSERT INTO Ventanilla VALUES(48914,581);
INSERT INTO Ventanilla VALUES(19683,532);
INSERT INTO Ventanilla VALUES(14592,321);

INSERT INTO ATM VALUES(11449,1727,"Belgrano 232");
INSERT INTO ATM VALUES(58391,2000,"Chacabuco 364");
INSERT INTO ATM VALUES(49183,1900,"Calle 91, 665");

INSERT INTO Transaccion VALUES(2940215195,"2029/01/13","21:31:00",4728.59);
INSERT INTO Transaccion VALUES(6820572190,"2013/05/16","11:35:20",43528.99);
INSERT INTO Transaccion VALUES(1148572005,"2019/05/21","17:51:31",1628.54);
INSERT INTO Transaccion VALUES(5209648174,"2042/03/24","11:59:03",74228.29);
INSERT INTO Transaccion VALUES(4915801675,"2015/11/24","15:42:11",98268.79);
INSERT INTO Transaccion VALUES(1957406929,"2021/02/16","13:51:00",4928.79);
INSERT INTO Transaccion VALUES(4592481056,"2029/02/14","19:49:49",89425.40);
INSERT INTO Transaccion VALUES(2405859106,"2039/11/23","21:49:39",902468.49);
INSERT INTO Transaccion VALUES(1957205630,"1983/06/23","22:03:49",3918.49);
INSERT INTO Transaccion VALUES(4927502098,"2009/03/23","13:41:38",1993.20);
INSERT INTO Transaccion VALUES(3029859626,"2094/03/23","07:12:30",47.42);

INSERT INTO Debito VALUES(1148572005,"Compras",13593,69257296);
INSERT INTO Debito VALUES(1957406929,"Pago Cuota",47245,93841051);
INSERT INTO Debito VALUES(4927502098,"Honorarios",58491,15935350);

INSERT INTO Transaccion_por_caja VALUES(4592481056,14592);
INSERT INTO Transaccion_por_caja VALUES(1957205630,11449);
INSERT INTO Transaccion_por_caja VALUES(4915801675,48914);
INSERT INTO Transaccion_por_caja VALUES(2405859106,11449);
INSERT INTO Transaccion_por_caja VALUES(3029859626,48914);
INSERT INTO Transaccion_por_caja VALUES(5209648174,35928);
INSERT INTO Transaccion_por_caja VALUES(6820572190,58391);
INSERT INTO Transaccion_por_caja VALUES(2940215195,19683);

INSERT INTO Deposito VALUES(2405859106,93841051);
INSERT INTO Deposito VALUES(3029859626,69257296);
INSERT INTO Deposito VALUES(4915801675,93841051);

INSERT INTO Extraccion VALUES(5209648174,13593,58925014);
INSERT INTO Extraccion VALUES(6820572190,58491,15935350);

INSERT INTO Transferencia VALUES(2940215195,13593,92484241,69257296);
INSERT INTO Transferencia VALUES(4592481056,47245,93841051,15935350);
INSERT INTO Transferencia VALUES(1957205630,58491,15935350,93841051);






