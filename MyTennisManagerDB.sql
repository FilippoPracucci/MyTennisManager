-- *********************************************
-- * Standard SQL generation                   
-- *--------------------------------------------
-- * DB-MAIN version: 11.0.2              
-- * Generator date: Sep 14 2021              
-- * Generation date: Fri Aug 25 17:08:37 2023 
-- * LUN file: C:\Users\Utente\Desktop\FILIPPO\UNIVERSITA\BASI DI DATI\PROGETTO\MyTennisManager.lun 
-- * Schema: Schema logico/SQL 
-- ********************************************* 


-- Database Section
-- ________________ 

create database mytennismanager;


-- DBSpace Section
-- _______________

use mytennismanager;

-- Tables Section
-- _____________ 

create table circoli (
     Id_Circolo int not null auto_increment,
     Organizzatore int not null,
     Nome varchar(40) not null,
     Citta varchar(40) not null,
     Indirizzo varchar(40) not null,
     Telefono varchar(12) not null,
     constraint ID_CIRCOLO_ID primary key (Id_Circolo),
     constraint SID_CIRCO_ORGAN_ID unique (Organizzatore),
     constraint SID_CIRCOLO_ID unique (Telefono));

create table coppie (
     Id_Coppia int not null auto_increment,
     constraint ID_COPPIA_ID primary key (Id_Coppia));

create table edizioni_torneo (
     Id_Torneo int not null,
     Numero_Edizione int not null,
     Data_Inizio date not null,
     Data_Fine date not null,
     Id_Circolo int not null,
     constraint ID_EDIZIONE_TORNEO_ID primary key (Id_Torneo, Numero_Edizione),
     constraint SID_EDIZIONE_TORNEO_ID unique (Id_Torneo, Data_Inizio, Id_Circolo));

create table giocatori (
     Id_Utente int not null auto_increment,
     Nome varchar(40) not null,
     Cognome varchar(40) not null,
     Email varchar(40) not null,
     Password varchar(12) not null,
     Tessera varchar(11) not null,
     Classifica varchar(4) not null,
     Eta int not null,
     Sesso char(1) not null,
     Telefono varchar(11) not null,
     Id_Circolo int not null,
     constraint ID_GIOCATORE_ID primary key (Id_Utente),
     constraint SID_GIOCATORE_2_ID unique (Tessera),
     constraint SID_GIOCATORE_1_ID unique (Email),
     constraint SID_GIOCATORE_ID unique (Telefono));

create table iscrizioni (
     Id_Iscrizione int not null auto_increment,
     Preferenza_Orario varchar(12) default null,
     Id_Torneo int not null,
     Numero_Edizione int not null,
     Id_Utente int default null,
     Id_Coppia int default null,
     constraint ID_ISCRIZIONE_ID primary key (Id_Iscrizione));

create table organizzatori (
     Id_Utente int not null auto_increment,
     Nome varchar(40) not null,
     Cognome varchar(40) not null,
     Email varchar(40) not null,
     Password varchar(12) not null,
     constraint ID_ORGANIZZATORE_ID primary key (Id_Utente),
     constraint SID_ORGANIZZATORE_ID unique (Email));

create table tornei (
     Id_Torneo int not null auto_increment,
     Tipo varchar(20) not null,
     Limite_Categoria int default null,
     Limite_Eta int default null,
     Montepremi int default null,
     constraint ID_TORNEO_ID primary key (Id_Torneo));

create table unioni (
     Id_Coppia int not null,
     Id_Utente int not null,
     constraint ID_UNIONE_ID primary key (Id_Utente, Id_Coppia));

create view giocatori_iscritti as (
	select g.Id_Utente, g.Nome, g.Cognome, g.Email, g.Tessera, g.Classifica, g.Eta, g.Telefono, i.Id_Torneo, i.Numero_Edizione, i.Preferenza_Orario
    from giocatori g join iscrizioni i
    on (g.Id_Utente = i.Id_Utente));

create view coppie_iscritte as (
	select distinct u.Id_Coppia, i.Id_Torneo, i.Numero_Edizione, i.Preferenza_Orario
    from unioni u join iscrizioni i
    on (u.Id_Coppia = i.Id_Coppia));
    
create view tornei_con_edizioni as (
	select t.Id_Torneo, et.Numero_Edizione, t.Tipo, et.Data_Inizio, et.Data_Fine, t.Limite_Categoria, t.Limite_Eta, t.Montepremi, et.Id_Circolo
    from tornei t join edizioni_torneo et
    on (t.Id_Torneo = et.Id_Torneo));
    
create view iscrizioni_con_torneo as (
	select et.Id_Torneo, et.Numero_Edizione, et.Tipo, et.Data_Inizio, et.Data_Fine, c.Nome as Circolo, et.Limite_Categoria, et.Limite_Eta, et.Montepremi, i.Preferenza_Orario, i.Id_Utente, i.Id_Coppia
    from iscrizioni i join tornei_con_edizioni et join circoli c
    on (et.Id_Torneo = i.Id_Torneo and et.Numero_Edizione = i.Numero_Edizione and et.Id_Circolo = c.Id_Circolo));
    
create view compagno_unioni as (
	select u.Id_Coppia, g.Id_Utente, g.Nome, g.Cognome, g.Classifica, g.Sesso
    from unioni u join giocatori g
    on (u.Id_Utente = g.Id_Utente));


-- Constraints Section
-- ___________________ 

alter table circoli add constraint SID_CIRCO_ORGAN_FK
     foreign key (Organizzatore)
     references organizzatori (Id_Utente);

alter table edizioni_torneo add constraint REF_EDIZI_CIRCO_FK
     foreign key (Id_Circolo)
     references circoli (Id_Circolo);

alter table edizioni_torneo add constraint EQU_EDIZI_TORNE
     foreign key (Id_Torneo)
     references tornei (Id_Torneo);

alter table giocatori add constraint EQU_GIOCA_CIRCO_FK
     foreign key (Id_Circolo)
     references circoli (Id_Circolo);

alter table iscrizioni add constraint REF_ISCRI_EDIZI_FK
     foreign key (Id_Torneo, Numero_Edizione)
     references edizioni_torneo (Id_Torneo, Numero_Edizione);

alter table iscrizioni add constraint REF_ISCRI_GIOCA_FK
     foreign key (Id_Utente)
     references giocatori (Id_Utente);

alter table iscrizioni add constraint REF_ISCRI_COPPI_FK
     foreign key (Id_Coppia)
     references coppie (Id_Coppia);

alter table unioni add constraint REF_UNION_GIOCA
     foreign key (Id_Utente)
     references giocatori (Id_Utente);

alter table unioni add constraint EQU_UNION_COPPI_FK
     foreign key (Id_Coppia)
     references coppie (Id_Coppia);


-- Index Section
-- _____________ 

create unique index ID_CIRCOLO_IND
     on circoli (Id_Circolo);

create unique index SID_CIRCO_ORGAN_IND
     on circoli (Organizzatore);

create unique index SID_CIRCOLO_IND
     on circoli (Telefono);

create unique index ID_COPPIA_IND
     on coppie (Id_Coppia);

create unique index ID_EDIZIONE_TORNEO_IND
     on edizioni_torneo (Id_Torneo, Numero_Edizione);

create index REF_EDIZI_CIRCO_IND
     on edizioni_torneo (Id_Circolo);

create unique index SID_EDIZIONE_TORNEO_IND
     on edizioni_torneo (Id_Torneo, Data_Inizio, Id_Circolo);

create unique index SID_GIOCATORE_2_IND
     on giocatori (Tessera);

create unique index ID_GIOCATORE_IND
     on giocatori (Id_Utente);

create index EQU_GIOCA_CIRCO_IND
     on giocatori (Id_Circolo);

create unique index SID_GIOCATORE_1_IND
     on giocatori (Email);

create unique index SID_GIOCATORE_IND
     on giocatori (Telefono);

create unique index ID_ISCRIZIONE_IND
     on iscrizioni (Id_Iscrizione);

create index REF_ISCRI_EDIZI_IND
     on iscrizioni (Id_Torneo, Numero_Edizione);

create index REF_ISCRI_GIOCA_IND
     on iscrizioni (Id_Utente);

create index REF_ISCRI_COPPI_IND
     on iscrizioni (Id_Coppia);

create unique index ID_ORGANIZZATORE_IND
     on organizzatori (Id_Utente);

create unique index SID_ORGANIZZATORE_IND
     on organizzatori (Email);

create unique index ID_TORNEO_IND
     on tornei (Id_Torneo);

create unique index ID_UNIONE_IND
     on unioni (Id_Utente, Id_Coppia);

create index EQU_UNION_COPPI_IND
     on unioni (Id_Coppia);

