DROP DATABASE IF EXISTS `Hotel`;
CREATE DATABASE `Hotel`;
USE `Hotel`;

###
###  Tabla Roles 
###

DROP TABLE IF EXISTS `Hotel`.`Roles`;
CREATE TABLE `Hotel`.`Roles` (   
    `id` BIGINT NOT NULL AUTO_INCREMENT,   
    `nombreRol` VARCHAR(45) NOT NULL,   
    `fechaCreacion` TIMESTAMP, 
    `fechaEliminacion` TIMESTAMP, 
    PRIMARY KEY (`id`))   
ENGINE=InnoDB DEFAULT CHARSET=utf8; 

###
### Tabla Paises
###

DROP TABLE IF EXISTS `Hotel`.`Paises`;
CREATE TABLE `Hotel`.`Paises` (   
    `id` BIGINT NOT NULL AUTO_INCREMENT,   
    `nombre` VARCHAR(45) NOT NULL,   
    `fechaCreacion` TIMESTAMP, 
    `fechaEliminacion` TIMESTAMP, 
    PRIMARY KEY (`id`))   
ENGINE=InnoDB DEFAULT CHARSET=utf8; 

##
## Tabla Provincias
##

DROP TABLE IF EXISTS `Hotel`.`Provincias`;
CREATE TABLE `Hotel`.`Provincias` (   
    `id` BIGINT NOT NULL AUTO_INCREMENT,   
    `nombre` VARCHAR(45) NOT NULL,   
    `fechaCreacion` TIMESTAMP, 
    `fechaEliminacion` TIMESTAMP,
    `idPais` BIGINT NOT NULL, 
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8; 

-- FK a la tabla Paises
ALTER TABLE `Provincias` ADD CONSTRAINT `Pais_FK` FOREIGN KEY (`idPais`)  
REFERENCES `Paises` (`id`) 
ON DELETE NO ACTION 
ON UPDATE NO ACTION; 

##
## Tabla Localidades
##

DROP TABLE IF EXISTS `Hotel`.`Localidades`;
CREATE TABLE `Hotel`.`Localidades` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `nombre` VARCHAR(45) NOT NULL,
    `codigoPostal` VARCHAR(45) NOT NULL,
    `fechaCreacion` TIMESTAMP, 
    `fechaEliminacion` TIMESTAMP,
    `idProvincia` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- FK a la tabla Provincias
ALTER TABLE `Localidades` ADD CONSTRAINT `Provincia_FK` FOREIGN KEY(`idProvincia`) 
REFERENCES `Provincias`(`id`)
ON DELETE NO ACTION 
ON UPDATE NO ACTION; 

##
## Tabla Domicilios
##

DROP TABLE IF EXISTS `Hotel`.`Domicilios`;
CREATE TABLE `Hotel`.`Domicilios` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `calle` VARCHAR(45) NOT NULL,
    `numero` VARCHAR(45) NOT NULL,
    `piso` VARCHAR(45),
    `departamento` VARCHAR(45),
	`fechaCreacion` TIMESTAMP, 
    `fechaEliminacion` TIMESTAMP,
    `idLocalidad` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- FK a la tabla Localidades
ALTER TABLE `Domicilios` ADD CONSTRAINT `Localidad_FK` FOREIGN KEY(`idLocalidad`) 
REFERENCES `Localidades`(`id`)
ON DELETE CASCADE
ON UPDATE CASCADE;

##
## Tabla Personas
##

DROP TABLE IF EXISTS `Hotel`.`Personas`;
CREATE TABLE `Hotel`.`Personas` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `nombre` VARCHAR(45) NOT NULL,
    `apellido` VARCHAR(45) NOT NULL,
    `email` VARCHAR(60),
    `nroDocumento` BIGINT,
    `tipoDocumento` VARCHAR(10),
    `cuit` VARCHAR(30),
    `telefono` BIGINT,
    `fechaNacimiento` DATETIME,
    `genero` VARCHAR(45),
    `fechaCreacion` TIMESTAMP, 
    `fechaEliminacion` TIMESTAMP,
    `sueldoMensual` FLOAT DEFAULT NULL,
    `descripcion` VARCHAR(45) DEFAULT NULL,
    `legajo` BIGINT DEFAULT NULL,
    `tipoPersona` VARCHAR(45),
    `idDomicilio` BIGINT NOT NULL,
    `idRol` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- FK a la tabla Domicilios
ALTER TABLE `Personas` ADD CONSTRAINT `Domicilio_FK` FOREIGN KEY(`idDomicilio`) 
REFERENCES `Domicilios`(`id`)
ON DELETE CASCADE
ON UPDATE CASCADE;

-- FK a la tabla Roles
ALTER TABLE `Personas` ADD CONSTRAINT `Rol_FK` FOREIGN KEY(`idRol`) 
REFERENCES `Roles`(`id`)
ON DELETE CASCADE
ON UPDATE CASCADE;


/* Pasados a la superclase, modificadas las FKs
##
## Tabla Empleados
##
DROP TABLE IF EXISTS `Hotel`.`Empleados`;
CREATE TABLE `Hotel`.`Empleados` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `sueldoMensual` FLOAT,
    `descripcion` VARCHAR(45),
    `legajo` BIGINT,
    `fechaCreacion` TIMESTAMP, 
    `fechaEliminacion` TIMESTAMP,
    `idPersona` BIGINT NOT NULL UNIQUE,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- FK a la tabla Personas
ALTER TABLE `Empleados` ADD CONSTRAINT `Empleado_Persona_FK` FOREIGN KEY(`idPersona`) 
REFERENCES `Personas`(`id`)
ON DELETE CASCADE
ON UPDATE CASCADE;
##
## Tabla Clientes
##
DROP TABLE IF EXISTS `Hotel`.`Clientes`;
CREATE TABLE `Hotel`.`Clientes` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `fechaCreacion` TIMESTAMP, 
    `fechaEliminacion` TIMESTAMP,
    `idPersona` BIGINT NOT NULL UNIQUE,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
ALTER TABLE `Clientes` ADD CONSTRAINT `Cliente_Persona_FK` FOREIGN KEY(`idPersona`) 
REFERENCES `Personas`(`id`)
ON DELETE CASCADE
ON UPDATE CASCADE;
*/

##
## Tabla Tarjetas
##

DROP TABLE IF EXISTS `Hotel`.`Tarjetas`; 
CREATE TABLE `Hotel`.`Tarjetas` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `numeroTarjeta` BIGINT NOT NULL,
    `fechaVencimiento` DATETIME NOT NULL,
    `nombreTarjeta` VARCHAR(45),
    `monto` FLOAT,
    `fechaCreacion` TIMESTAMP, 
    `fechaEliminacion` TIMESTAMP,
    `idPersona` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- FK a la tabla Personas
ALTER TABLE `Tarjetas` ADD CONSTRAINT `Tarjeta_Persona_FK` FOREIGN KEY(`idPersona`) 
REFERENCES `Personas`(`id`)
ON DELETE CASCADE
ON UPDATE CASCADE;


##
## Tabla Facturas
##

DROP TABLE IF EXISTS `Hotel`.`Facturas`;
CREATE TABLE `Hotel`.`Facturas` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `numeroFactura` BIGINT NOT NULL,
    `monto` FLOAT,
    `fechaCreacion` TIMESTAMP, 
    `fechaEliminacion` TIMESTAMP,
    `idTarjeta` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- FK a la tabla Tarjetas
ALTER TABLE `Facturas` ADD CONSTRAINT `Tarjeta_FK` FOREIGN KEY(`idTarjeta`) 
REFERENCES `Tarjetas`(`id`)
ON DELETE CASCADE
ON UPDATE CASCADE;

##
## Tabla LineaFacturas
##

DROP TABLE IF EXISTS `Hotel`.`LineaFacturas`;
CREATE TABLE `Hotel`.`LineaFacturas` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `cantidad` INT NOT NULL,
    `monto` FLOAT NOT NULL,
    `fechaCreacion` TIMESTAMP, 
    `fechaEliminacion` TIMESTAMP,
    `idFactura` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- FK a la tabla Facturas
ALTER TABLE `LineaFacturas` ADD CONSTRAINT `Factura_FK` FOREIGN KEY(`idFactura`) 
REFERENCES `Facturas`(`id`)
ON DELETE CASCADE
ON UPDATE CASCADE;

##
## Tabla TipoHabitaciones
##

DROP TABLE IF EXISTS `Hotel`.`TipoHabitaciones`;
CREATE TABLE `Hotel`.`TipoHabitaciones` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `descripcion` VARCHAR(45),
    `capacidad` INT,
    `foto` BLOB,
    `denominacion` VARCHAR(45),
    `precioPorDia` FLOAT,
    `fechaCreacion` TIMESTAMP, 
    `fechaEliminacion` TIMESTAMP,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

##
## Tabla Habitaciones
##

DROP TABLE IF EXISTS `Hotel`.`Habitaciones`;
CREATE TABLE `Hotel`.`Habitaciones` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `numeroHabitacion` INT,
    `fechaCreacion` TIMESTAMP, 
    `fechaEliminacion` TIMESTAMP,
    `idTipoHabitacion` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- FK a la tabla TipoHabitaciones
ALTER TABLE `Habitaciones` ADD CONSTRAINT `TipoHabitacion_FK` FOREIGN KEY(`idTipoHabitacion`) 
REFERENCES `TipoHabitaciones`(`id`)
ON DELETE CASCADE
ON UPDATE CASCADE;

##
## Tabla Salones
##

DROP TABLE IF EXISTS `Hotel`.`Salones`;
CREATE TABLE `Hotel`.`Salones` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `capacidad` INT,
	`nombreSalon` VARCHAR(45),
    `descripcion` VARCHAR(45),
    `foto` BLOB,    
    `precioPorDia` FLOAT,
    `fechaCreacion` TIMESTAMP, 
    `fechaEliminacion` TIMESTAMP,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/* VER SI CORRESPONDE, En alquiler, En reparación, Disponible, etc.
##
## Tabla EstadoHabitacionesSalones
##
DROP TABLE IF EXISTS `Hotel`.`EstadoHabitacionesSalones`;
CREATE TABLE `Hotel`.`EstadoHabitacionesSalones` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `descripcion` VARCHAR(45),
    `fechaCreacion` TIMESTAMP, 
    `fechaEliminacion` TIMESTAMP,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/

##
## Tabla EstadoReservas
##

DROP TABLE IF EXISTS `Hotel`.`EstadoReservas`;
CREATE TABLE `Hotel`.`EstadoReservas` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `descripcion` VARCHAR(45),
    `fechaCreacion` TIMESTAMP, 
    `fechaEliminacion` TIMESTAMP,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

##
## Tabla Reservas
##

DROP TABLE IF EXISTS `Hotel`.`Reservas`;
CREATE TABLE `Hotel`.`Reservas` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `fechaReserva` DATETIME NOT NULL,
    `fechaCancelacion` DATETIME DEFAULT NULL,
    `cantDias` INT NOT NULL,
    `fechaEntrada` DATETIME DEFAULT NULL,
    `fechaSalida` DATETIME DEFAULT NULL,
    `fechaCreacion` TIMESTAMP, 
    `fechaEliminacion` TIMESTAMP,
    `idPersona` BIGINT NOT NULL,
    `idEstadoReserva` BIGINT NOT NULL,
    `idHabitacion` BIGINT,
    `idSalon` BIGINT,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- FK a la tabla Clientes
ALTER TABLE `Reservas` ADD CONSTRAINT `Reserva_Persona_FK` FOREIGN KEY(`idPersona`) 
REFERENCES `Personas`(`id`)
ON DELETE CASCADE
ON UPDATE CASCADE;

-- FK a la tabla EstadoReserva
ALTER TABLE `Reservas` ADD CONSTRAINT `EstadoReserva_FK` FOREIGN KEY(`idEstadoReserva`) 
REFERENCES `EstadoReservas`(`id`)
ON DELETE CASCADE
ON UPDATE CASCADE;

-- FK a la tabla Habitaciones
ALTER TABLE `Reservas` ADD CONSTRAINT `Habitacion_FK` FOREIGN KEY(`idHabitacion`) 
REFERENCES `Habitaciones`(`id`)
ON DELETE CASCADE
ON UPDATE CASCADE;

-- FK a la tabla Salones
ALTER TABLE `Reservas` ADD CONSTRAINT `Salon_FK` FOREIGN KEY(`idSalon`) 
REFERENCES `Salones`(`id`)
ON DELETE CASCADE
ON UPDATE CASCADE;