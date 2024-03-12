# DatabaseBridge

[TOC]



## Description
DatabaseBridge is an application designed to facilitate the smooth migration of an existing database to another database system. For instance, it allows the transfer of a Postgresql database to a Mysql database, ensuring all tables and relationships are migrated to the new database system. Moreover, it aims to retain as many integrity constraints as possible. The application features a command-line interface and is compatible with various database systems.

## Supported DB systems

- Postgres
- MySQL
- MariaDB

## Features

- Transfer schema definitions including tables, column-constraints, foreign key relations, views
- Transfer users and their privileges on table level
- Transfer data

## Important hints

- The transfer of passwords is not implemented. This means that a default password is set for each transferred user, except for the one defined in the configuration file.
- A number of predefined system users are explicitly excluded from the transfer: (if the user in the config file corresponds to one of the system users, it will still be transferred)
  - Postgres: `postgres,'postgres'@'%'`
  - MariaDB: `mariadb.sys,healthcheck,root,'mariadb.sys'@'localhost',mariadb.sys'@'localhost`
  - MySQL: `mysql.session,mysql.sys,mysql.infoschema,healthcheck,root,mysql.session@localhost,mysql.sys@localhost,mysql.infoschema@localhost`

## Known issues

- Views referencing other views might cause problems

## Usage

Run the [here](DatabaseBridge.jar) provided `DatabaseBridge.jar` with the according CLI command. (see section below for information about the CLI)

Example usage:

```
java -jar DatabaseBridge.jar help
```

### CLI

The CLI defines the following commands:

- `execute` -> execute the transfer
- `new` -> create a configuration file template

To create an config file template, run:

```
databasebridge new <path_to_save_location_template_file>
```

For example:

```
databasebridge new /Users/exampleUser/Desktop/template.json
```

To start the transfer, run:

```
databasebridge execute <path_to_config_file> 
```

To start the transfer and additionally get the created DDL-script for insight or debugging, run:

```
databasebridge execute <path_to_config_file> -s <path_to_save_location_ddl_script>
```

To get help, run:

```
databasebridge help
```

Or to get help with a specific command, run:

```
databasebridge <command> --help 
```

### IntelliJ

To run DatabaseBridge in IntelliJ run the main-method in the class `DataBaseBridge`. Don't forget the edit the run configurations to pass the required parameters according to the commands from above. 

## Documentation
Detailed documentation can be found under [docs](https://gitlab.mi.hdm-stuttgart.de/ps149/databasebridge/-/tree/dev/docs) in the root directory of the repository.

## Team
The application is being developed by Kiara Nagler (kn022) and Pia Schilling (ps149).

