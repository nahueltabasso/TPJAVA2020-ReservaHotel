###########
# INSERTS #
########### 

INSERT INTO `Hotel`.`Roles` (`nombreRol`, `fechaCreacion`) VALUES
('Administrador', '2020-01-01 13:00:00'),
('Empleado', '2020-01-01 13:00:00'),
('Cliente', '2020-01-01 13:00:00');

INSERT INTO `Hotel`.`Paises` (`nombre`, `fechaCreacion`) VALUES
('Argentina', '2020-01-01 13:00:00'),
('Brasil', '2020-01-01 13:00:00'),
('Uruguay', '2020-01-01 13:00:00'),
('Paraguay', '2020-01-01 13:00:00'),
('Chile', '2020-01-01 13:00:00');

INSERT INTO `Hotel`.`Provincias` (`nombre`, `fechaCreacion`, `idPais`) VALUES
('Santa Fe', '2020-01-01 13:00:00', 1),
('Buenos Aires', '2020-01-01 13:00:00', 1),
('Cordoba', '2020-01-01 13:00:00', 1),
('Enre Rios', '2020-01-01 13:00:00', 1),
('Corrientes', '2020-01-01 13:00:00', 1),
('San Pablo', '2020-01-01 13:00:00', 2),
('Mina Gerais', '2020-01-01 13:00:00', 2),
('Montevideo', '2020-01-01 13:00:00', 3),
('Colonia', '2020-01-01 13:00:00', 3),
('Boqueron', '2020-01-01 13:00:00', 4),
('Concepcion', '2020-01-01 13:00:00', 4),
('Valdivia', '2020-01-01 13:00:00', 5),
('Valparaiso', '2020-01-01 13:00:00', 5);

INSERT INTO `Hotel`.`Localidades` (`nombre`, `codigoPostal`, `fechaCreacion`, `idProvincia`) VALUES
('Rosario', 2000, '2020-01-01 13:00:00', 1),
('Funes', 2132, '2020-01-01 13:00:00', 1),
('Roldan', 2134, '2020-01-01 13:00:00', 1),
('San Lorenzo', 2200, '2020-01-01 13:00:00', 1),
('Buenos Aires', 1000, '2020-01-01 13:00:00', 2),
('La Plata', 1900, '2020-01-01 13:00:00', 2),
('Cordoba', 5000, '2020-01-01 13:00:00', 3),
('Villa Carlos Paz', 5152, '2020-01-01 13:00:00', 3),
('La Cumbrecita', 5194, '2020-01-01 13:00:00', 3),
('Parana', 3100, '2020-01-01 13:00:00', 4),
('Victoria', 3153, '2020-01-01 13:00:00', 4),
('Corrientes', 3400, '2020-01-01 13:00:00', 5),
('Paso de los libres', 3230, '2020-01-01 13:00:00', 5);

INSERT INTO `Hotel`.`Domicilios` (`calle`, `numero`, `fechaCreacion`, `idLocalidad`) VALUES
('Zeballos', 1341, '2020-01-01 13:00:00', 1),
('Rioja', 2211, '2020-01-01 13:00:00', 1),
('Salta', 3575, '2020-01-01 13:00:00', 1),
('Urquiza', 757, '2020-01-01 13:00:00', 2),
('Pellegrini', 1414, '2020-01-01 13:00:00', 3),
('Mitre', 1090, '2020-01-01 13:00:00', 9);

INSERT INTO `Hotel`.`Personas` (`nombre`, `apellido`, `email`, `nroDocumento`, `tipoDocumento`, `genero`, `fechaNacimiento`, `cuit`, `telefono`, `fechaCreacion`, `legajo`, `descripcion`, `sueldoMensual`, `tipoPersona`, `idDomicilio`, `idRol`) VALUES
('Juan', 'Perez', 'jperez@gmail.com', 30444888, 'DNI', 'Masculino', '1990-10-10', '20-30444888-3', 3416777888, '2020-01-01 13:00:00', 101010, 'Gerente', 70000, "Empleado", 1, 1),
('Pedro', 'Rodriguez', 'prodriguez@gmail.com', 20555888, 'DNI', 'Masculino', '1980-04-15', '20-20555888-1', 3416123456, '2020-01-01 13:00:00', 202020, 'Administrativo', 35000, "Empleado", 2, 2),
('Horacio', 'Carpatto', 'hcarpatto@gmail.com', 36000333, 'DNI', 'Masculino', '1999-05-20', '20-36000333-3', 3415222111, '2020-01-01 13:00:00', 303030, 'Conserje', 35000, "Empleado", 5, 2),
('Esteban', 'Quito', 'equito@gmail.com', 35777888, 'DNI', 'Masculino', '1992-01-16', '20-35777888-7', 3416999456, '2020-01-01 13:00:00', NULL, NULL, NULL, "Cliente", 3, 3),
('Federico', 'Fernandez', 'ffernandez@gmail.com', 25444888, 'DNI', 'Masculino', '1985-02-14', '20-25444888-4', 3416123888, '2020-01-01 13:00:00', NULL, NULL, NULL, "Cliente", 4, 3),
('Laura', 'Giorgio', 'lgiorgio@gmail.com', 32666777, 'DNI', 'Femenino', '1986-07-09', '27-32666777-0', 3516000123, '2020-01-01 13:00:00', NULL, NULL, NULL, "Cliente", 6, 3);

/* Pasados a la superclase como tipoPersona
INSERT INTO `Hotel`.`Clientes` (`fechaCreacion`, `idPersona`) VALUES
('2020-01-01 13:00:00', 3),
('2020-01-01 13:00:00', 4);
INSERT INTO `Hotel`.`Empleados` (`legajo`, `descripcion`, `sueldoMensual`, `fechaCreacion`, `idPersona`) VALUES
(101010, 'Gerente', 70000, '2020-01-01 13:00:00', 1),
(202020, 'Administrativo', 35000, '2020-01-01 13:00:00', 2);
*/

INSERT INTO `Hotel`.`Tarjetas` (`numeroTarjeta`, `nombreTarjeta`, `fechaVencimiento`, `monto`, `fechaCreacion`, `idPersona`) VALUES
(4546102010204546, 'Visa', '2023-01-01 13:00:00', 123000, '2020-01-01 13:00:00', 4),
(4558999988887777, 'Mastercard', '2023-03-01 13:00:00', 64000, '2020-01-01 13:00:00', 4),
(5896102010204546, 'Mastercard', '2024-10-01 13:00:00', 157000, '2020-01-01 13:00:00', 5),
(4546102010204546, 'Visa', '2023-11-01 13:00:00', 85000, '2020-01-01 13:00:00', 6);

INSERT INTO `Hotel`.`Facturas` (`numeroFactura`, `monto`, `fechaCreacion`, `idTarjeta`) VALUES
(1111111111, 3000, '2020-01-01 13:00:00', 1),
(2222222222, 4000, '2020-01-01 13:00:00', 2),
(3333333333, 5000, '2020-01-01 13:00:00', 3);

INSERT INTO `Hotel`.`LineaFacturas` (`cantidad`, `monto`, `fechaCreacion`, `idFactura`) VALUES
(1, 3000, '2020-01-01 13:00:00', 1),
(2, 2000, '2020-01-01 13:00:00', 2),
(3, 1000, '2020-01-01 13:00:00', 3),
(3, 2000, '2020-01-01 13:00:00', 3),
(3, 2000, '2020-01-01 13:00:00', 3);

INSERT INTO `Hotel`.`EstadoReservas` (`descripcion`, `fechaCreacion`) VALUES
('Reservada', '2020-01-01 13:00:00'),
('Cancelada', '2020-01-01 13:00:00'),
('Anulada', '2020-01-01 13:00:00'),
('Activa', '2020-01-01 13:00:00');

INSERT INTO `Hotel`.`Salones` (`nombreSalon`, `capacidad`, `descripcion`, `precioPorDia`, `fechaCreacion`) VALUES
('Salón Embajador', 1000, 'Grandes eventos', 50000, '2020-01-01 13:00:00'),
('Salón de Fiestas', 400, 'Fiestas y reuniones', 30000, '2020-01-01 13:00:00'),
('Salón de Usos Múltiples', 200, 'Eventos menores', 15000, '2020-01-01 13:00:00');

INSERT INTO `Hotel`.`TipoHabitaciones` (`descripcion`, `capacidad`, `denominacion`, `precioPorDia`, `fechaCreacion`) VALUES
('Habitación Simple', 1, 'Simple', 2000, '2020-01-01 13:00:00'),
('Habitación Doble', 2, 'Doble', 3000, '2020-01-01 13:00:00'),
('Habitación Triple', 3, 'Triple', 4000, '2020-01-01 13:00:00'),
('Habitación Cuádruple', 4, 'Cuádruple', 5000, '2020-01-01 13:00:00');

INSERT INTO `Hotel`.`Habitaciones` (`numeroHabitacion`, `fechaCreacion`, `idTipoHabitacion`) VALUES
( 101, '2020-01-01 13:00:00', 1),
( 102, '2020-01-01 13:00:00', 1),
( 103, '2020-01-01 13:00:00', 1),
( 104, '2020-01-01 13:00:00', 1),
( 201, '2020-01-01 13:00:00', 2),
( 202, '2020-01-01 13:00:00', 2),
( 203, '2020-01-01 13:00:00', 2),
( 204, '2020-01-01 13:00:00', 2),
( 301, '2020-01-01 13:00:00', 3),
( 302, '2020-01-01 13:00:00', 3),
( 303, '2020-01-01 13:00:00', 3),
( 304, '2020-01-01 13:00:00', 3),
( 401, '2020-01-01 13:00:00', 4),
( 402, '2020-01-01 13:00:00', 4),
( 403, '2020-01-01 13:00:00', 4),
( 404, '2020-01-01 13:00:00', 4);

INSERT INTO `Hotel`.`Reservas` (`fechaReserva`, `cantDias`, `fechaEntrada`, `fechaCreacion`, `idHabitacion`, `idEstadoReserva`, `idPersona`) VALUES
('2020-02-01 13:00:00', 7, '2021-02-02 13:00:00', '2020-01-01 13:00:00', 12, 1, 4),
('2020-02-10 13:00:00', 1, '2021-03-02 13:00:00', '2020-01-01 13:00:00', 4, 1, 4),
('2020-04-01 13:00:00', 14, '2021-04-02 13:00:00', '2020-01-01 13:00:00', 5, 1, 5),
('2020-05-01 13:00:00', 7, '2021-06-02 13:00:00', '2020-01-01 13:00:00', 16, 1, 6);

INSERT INTO `Hotel`.`Reservas` (`fechaReserva`, `fechaCancelacion`, `cantDias`, `fechaEntrada`, `fechaCreacion`, `idHabitacion`, `idEstadoReserva`, `idPersona`) VALUES
('2020-08-01 13:00:00', '2020-09-01 13:00:00', 7, '2021-04-02 13:00:00', '2020-01-01 13:00:00', 7, 2, 5),
('2020-08-01 13:00:00', '2020-09-01 13:00:00', 7, '2021-04-02 13:00:00', '2020-01-01 13:00:00', 8, 2, 6);

INSERT INTO `Hotel`.`Reservas` (`fechaReserva`, `cantDias`, `fechaEntrada`, `fechaCreacion`, `idSalon`, `idEstadoReserva`, `idPersona`) VALUES
('2020-06-01 13:00:00', 1, '2021-10-10 13:00:00', '2020-01-01 13:00:00', 2, 1, 4),
('2020-07-10 13:00:00', 1, '2021-11-11 13:00:00', '2020-01-01 13:00:00', 3, 1, 5),
('2020-06-01 13:00:00', 1, '2021-10-10 13:00:00', '2020-01-01 13:00:00', 1, 1, 6),
('2020-07-10 13:00:00', 1, '2021-11-11 13:00:00', '2020-01-01 13:00:00', 3, 1, 2);


/*
select * from roles;
select * from paises;
select * from provincias;
select * from localidades;
select * from domicilios;
select * from personas;
select * from clientes;
select * from empleados;
select * from tarjetas;
select * from facturas;
select * from lineafacturas;
select * from estadoreservas;
select * from salones;
select * from tipohabitaciones;
select * from habitaciones;
select * from reservas;
*/