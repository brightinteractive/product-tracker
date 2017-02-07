Create table Continent (
	Id Bigint NOT NULL AUTO_INCREMENT,
	Name Varchar(100) NOT NULL,
	Primary Key (Id)) TYPE = InnoDB
ROW_FORMAT = Default;


ALTER TABLE Country ADD ContinentId INTEGER;

Alter table Country add Index IX_RelationshipCountryContinent (ContinentId);


INSERT INTO Continent (Id, Name) VALUES (1, 'Asia');
INSERT INTO Continent (Id, Name) VALUES (2, 'Africa');
INSERT INTO Continent (Id, Name) VALUES (3, 'Europe');
INSERT INTO Continent (Id, Name) VALUES (4, 'North America');
INSERT INTO Continent (Id, Name) VALUES (5, 'South America');
INSERT INTO Continent (Id, Name) VALUES (6, 'Oceania');
INSERT INTO Continent (Id, Name) VALUES (7, 'Antartica');


UPDATE Country SET ContinentId=1 WHERE Id=2;
UPDATE Country SET ContinentId=3 WHERE Id=3;
UPDATE Country SET ContinentId=2 WHERE Id=4;
UPDATE Country SET ContinentId=3 WHERE Id=5;
UPDATE Country SET ContinentId=2 WHERE Id=6;
UPDATE Country SET ContinentId=4 WHERE Id=7;
UPDATE Country SET ContinentId=5 WHERE Id=8;
UPDATE Country SET ContinentId=1 WHERE Id=9;
UPDATE Country SET ContinentId=6 WHERE Id=10;
UPDATE Country SET ContinentId=3 WHERE Id=11;
UPDATE Country SET ContinentId=1 WHERE Id=12;
UPDATE Country SET ContinentId=4 WHERE Id=13;
UPDATE Country SET ContinentId=1 WHERE Id=14;
UPDATE Country SET ContinentId=1 WHERE Id=15;
UPDATE Country SET ContinentId=4 WHERE Id=16;
UPDATE Country SET ContinentId=3 WHERE Id=17;
UPDATE Country SET ContinentId=3 WHERE Id=18;
UPDATE Country SET ContinentId=4 WHERE Id=19;
UPDATE Country SET ContinentId=2 WHERE Id=20;
UPDATE Country SET ContinentId=1 WHERE Id=21;
UPDATE Country SET ContinentId=5 WHERE Id=22;
UPDATE Country SET ContinentId=3 WHERE Id=23;
UPDATE Country SET ContinentId=2 WHERE Id=24;
UPDATE Country SET ContinentId=5 WHERE Id=25;
UPDATE Country SET ContinentId=1 WHERE Id=26;
UPDATE Country SET ContinentId=3 WHERE Id=27;
UPDATE Country SET ContinentId=2 WHERE Id=28;
UPDATE Country SET ContinentId=2 WHERE Id=29;
UPDATE Country SET ContinentId=1 WHERE Id=30;
UPDATE Country SET ContinentId=2 WHERE Id=31;
UPDATE Country SET ContinentId=4 WHERE Id=32;
UPDATE Country SET ContinentId=2 WHERE Id=33;
UPDATE Country SET ContinentId=2 WHERE Id=34;
UPDATE Country SET ContinentId=2 WHERE Id=35;
UPDATE Country SET ContinentId=5 WHERE Id=36;
UPDATE Country SET ContinentId=1 WHERE Id=37;
UPDATE Country SET ContinentId=5 WHERE Id=38;
UPDATE Country SET ContinentId=2 WHERE Id=39;
UPDATE Country SET ContinentId=2 WHERE Id=40;
UPDATE Country SET ContinentId=2 WHERE Id=41;
UPDATE Country SET ContinentId=4 WHERE Id=42;
UPDATE Country SET ContinentId=3 WHERE Id=43;
UPDATE Country SET ContinentId=5 WHERE Id=44;
UPDATE Country SET ContinentId=3 WHERE Id=45;
UPDATE Country SET ContinentId=3 WHERE Id=46;
UPDATE Country SET ContinentId=2 WHERE Id=47;
UPDATE Country SET ContinentId=3 WHERE Id=48;
UPDATE Country SET ContinentId=2 WHERE Id=49;
UPDATE Country SET ContinentId=4 WHERE Id=50;
UPDATE Country SET ContinentId=4 WHERE Id=51;
UPDATE Country SET ContinentId=1 WHERE Id=52;
UPDATE Country SET ContinentId=5 WHERE Id=53;
UPDATE Country SET ContinentId=2 WHERE Id=54;
UPDATE Country SET ContinentId=4 WHERE Id=55;
UPDATE Country SET ContinentId=2 WHERE Id=56;
UPDATE Country SET ContinentId=2 WHERE Id=57;
UPDATE Country SET ContinentId=3 WHERE Id=58;
UPDATE Country SET ContinentId=2 WHERE Id=59;
UPDATE Country SET ContinentId=6 WHERE Id=60;
UPDATE Country SET ContinentId=3 WHERE Id=61;
UPDATE Country SET ContinentId=3 WHERE Id=62;
UPDATE Country SET ContinentId=2 WHERE Id=63;
UPDATE Country SET ContinentId=2 WHERE Id=64;
UPDATE Country SET ContinentId=1 WHERE Id=65;
UPDATE Country SET ContinentId=3 WHERE Id=66;
UPDATE Country SET ContinentId=2 WHERE Id=67;
UPDATE Country SET ContinentId=3 WHERE Id=68;
UPDATE Country SET ContinentId=4 WHERE Id=69;
UPDATE Country SET ContinentId=4 WHERE Id=70;
UPDATE Country SET ContinentId=2 WHERE Id=71;
UPDATE Country SET ContinentId=2 WHERE Id=72;
UPDATE Country SET ContinentId=5 WHERE Id=73;
UPDATE Country SET ContinentId=4 WHERE Id=74;
UPDATE Country SET ContinentId=4 WHERE Id=75;
UPDATE Country SET ContinentId=3 WHERE Id=76;
UPDATE Country SET ContinentId=3 WHERE Id=77;
UPDATE Country SET ContinentId=1 WHERE Id=78;
UPDATE Country SET ContinentId=1 WHERE Id=79;
UPDATE Country SET ContinentId=1 WHERE Id=80;
UPDATE Country SET ContinentId=1 WHERE Id=81;
UPDATE Country SET ContinentId=3 WHERE Id=82;
UPDATE Country SET ContinentId=1 WHERE Id=83;
UPDATE Country SET ContinentId=3 WHERE Id=84;
UPDATE Country SET ContinentId=4 WHERE Id=85;
UPDATE Country SET ContinentId=1 WHERE Id=86;
UPDATE Country SET ContinentId=1 WHERE Id=87;
UPDATE Country SET ContinentId=1 WHERE Id=88;
UPDATE Country SET ContinentId=2 WHERE Id=89;
UPDATE Country SET ContinentId=6 WHERE Id=90;
UPDATE Country SET ContinentId=1 WHERE Id=91;
UPDATE Country SET ContinentId=1 WHERE Id=92;
UPDATE Country SET ContinentId=1 WHERE Id=93;
UPDATE Country SET ContinentId=1 WHERE Id=94;
UPDATE Country SET ContinentId=1 WHERE Id=95;
UPDATE Country SET ContinentId=3 WHERE Id=96;
UPDATE Country SET ContinentId=1 WHERE Id=97;
UPDATE Country SET ContinentId=2 WHERE Id=98;
UPDATE Country SET ContinentId=2 WHERE Id=99;
UPDATE Country SET ContinentId=2 WHERE Id=100;
UPDATE Country SET ContinentId=3 WHERE Id=101;
UPDATE Country SET ContinentId=3 WHERE Id=102;
UPDATE Country SET ContinentId=3 WHERE Id=103;
UPDATE Country SET ContinentId=3 WHERE Id=104;
UPDATE Country SET ContinentId=2 WHERE Id=105;
UPDATE Country SET ContinentId=2 WHERE Id=106;
UPDATE Country SET ContinentId=1 WHERE Id=107;
UPDATE Country SET ContinentId=1 WHERE Id=108;
UPDATE Country SET ContinentId=2 WHERE Id=109;
UPDATE Country SET ContinentId=3 WHERE Id=110;
UPDATE Country SET ContinentId=6 WHERE Id=111;
UPDATE Country SET ContinentId=2 WHERE Id=112;
UPDATE Country SET ContinentId=2 WHERE Id=113;
UPDATE Country SET ContinentId=4 WHERE Id=114;
UPDATE Country SET ContinentId=6 WHERE Id=115;
UPDATE Country SET ContinentId=3 WHERE Id=116;
UPDATE Country SET ContinentId=3 WHERE Id=117;
UPDATE Country SET ContinentId=1 WHERE Id=118;
UPDATE Country SET ContinentId=2 WHERE Id=119;
UPDATE Country SET ContinentId=2 WHERE Id=120;
UPDATE Country SET ContinentId=1 WHERE Id=121;
UPDATE Country SET ContinentId=2 WHERE Id=122;
UPDATE Country SET ContinentId=6 WHERE Id=123;
UPDATE Country SET ContinentId=1 WHERE Id=124;
UPDATE Country SET ContinentId=3 WHERE Id=125;
UPDATE Country SET ContinentId=6 WHERE Id=126;
UPDATE Country SET ContinentId=4 WHERE Id=127;
UPDATE Country SET ContinentId=2 WHERE Id=128;
UPDATE Country SET ContinentId=2 WHERE Id=129;
UPDATE Country SET ContinentId=3 WHERE Id=130;
UPDATE Country SET ContinentId=1 WHERE Id=131;
UPDATE Country SET ContinentId=1 WHERE Id=132;
UPDATE Country SET ContinentId=6 WHERE Id=133;
UPDATE Country SET ContinentId=4 WHERE Id=134;
UPDATE Country SET ContinentId=6 WHERE Id=135;
UPDATE Country SET ContinentId=5 WHERE Id=136;
UPDATE Country SET ContinentId=5 WHERE Id=137;
UPDATE Country SET ContinentId=1 WHERE Id=138;
UPDATE Country SET ContinentId=3 WHERE Id=139;
UPDATE Country SET ContinentId=3 WHERE Id=140;
UPDATE Country SET ContinentId=1 WHERE Id=141;
UPDATE Country SET ContinentId=3 WHERE Id=142;
UPDATE Country SET ContinentId=3 WHERE Id=143;
UPDATE Country SET ContinentId=2 WHERE Id=144;
UPDATE Country SET ContinentId=4 WHERE Id=145;
UPDATE Country SET ContinentId=4 WHERE Id=146;
UPDATE Country SET ContinentId=4 WHERE Id=147;
UPDATE Country SET ContinentId=6 WHERE Id=148;
UPDATE Country SET ContinentId=3 WHERE Id=149;
UPDATE Country SET ContinentId=2 WHERE Id=150;
UPDATE Country SET ContinentId=1 WHERE Id=151;
UPDATE Country SET ContinentId=2 WHERE Id=152;
UPDATE Country SET ContinentId=3 WHERE Id=153;
UPDATE Country SET ContinentId=2 WHERE Id=154;
UPDATE Country SET ContinentId=2 WHERE Id=155;
UPDATE Country SET ContinentId=1 WHERE Id=156;
UPDATE Country SET ContinentId=3 WHERE Id=157;
UPDATE Country SET ContinentId=3 WHERE Id=158;
UPDATE Country SET ContinentId=6 WHERE Id=159;
UPDATE Country SET ContinentId=2 WHERE Id=160;
UPDATE Country SET ContinentId=2 WHERE Id=161;
UPDATE Country SET ContinentId=3 WHERE Id=162;
UPDATE Country SET ContinentId=1 WHERE Id=163;
UPDATE Country SET ContinentId=2 WHERE Id=164;
UPDATE Country SET ContinentId=5 WHERE Id=165;
UPDATE Country SET ContinentId=2 WHERE Id=166;
UPDATE Country SET ContinentId=3 WHERE Id=167;
UPDATE Country SET ContinentId=3 WHERE Id=168;
UPDATE Country SET ContinentId=1 WHERE Id=169;
UPDATE Country SET ContinentId=1 WHERE Id=170;
UPDATE Country SET ContinentId=1 WHERE Id=171;
UPDATE Country SET ContinentId=2 WHERE Id=172;
UPDATE Country SET ContinentId=1 WHERE Id=173;
UPDATE Country SET ContinentId=2 WHERE Id=174;
UPDATE Country SET ContinentId=6 WHERE Id=175;
UPDATE Country SET ContinentId=4 WHERE Id=176;
UPDATE Country SET ContinentId=2 WHERE Id=177;
UPDATE Country SET ContinentId=1 WHERE Id=178;
UPDATE Country SET ContinentId=1 WHERE Id=179;
UPDATE Country SET ContinentId=6 WHERE Id=180;
UPDATE Country SET ContinentId=2 WHERE Id=181;
UPDATE Country SET ContinentId=3 WHERE Id=182;
UPDATE Country SET ContinentId=1 WHERE Id=183;
UPDATE Country SET ContinentId=3 WHERE Id=184;
UPDATE Country SET ContinentId=4 WHERE Id=185;
UPDATE Country SET ContinentId=5 WHERE Id=186;
UPDATE Country SET ContinentId=1 WHERE Id=187;
UPDATE Country SET ContinentId=6 WHERE Id=188;
UPDATE Country SET ContinentId=3 WHERE Id=189;
UPDATE Country SET ContinentId=5 WHERE Id=190;
UPDATE Country SET ContinentId=1 WHERE Id=191;
UPDATE Country SET ContinentId=2 WHERE Id=192;
UPDATE Country SET ContinentId=1 WHERE Id=193;
UPDATE Country SET ContinentId=2 WHERE Id=194;
UPDATE Country SET ContinentId=2 WHERE Id=195;


INSERT INTO ListValue (Id, ListId, Value) VALUES (68, 'ProductType', 'Equipment');


ALTER TABLE Product ADD Status VARCHAR(100);
ALTER TABLE Product ADD StatusLastTreatmentDate DATE;
ALTER TABLE Product ADD StatusLastTreatmentName VARCHAR(100);
ALTER TABLE Product ADD LastUpdateDate DATE;
ALTER TABLE Product ADD LastUpdateDoneBy VARCHAR(200);

ALTER TABLE Product ADD EquipmentSize Varchar(50);
ALTER TABLE Product ADD EquipmentAssemblyInstructions Text;
ALTER TABLE Product ADD ManufacturerName Varchar(200) NOT NULL;
ALTER TABLE Product ADD ManufacturerAddress Text;
ALTER TABLE Product ADD ManufacturerContactName Varchar(200);
ALTER TABLE Product ADD ManufacturerTelephone Varchar(50);
ALTER TABLE Product ADD ManufacturerEmailAddress Varchar(100);


INSERT INTO List (Id, Description) VALUES ('EventLocation', 'Location');
INSERT INTO ListValue (Value, ListId) SELECT DISTINCT Description, 'EventLocation' FROM ProductMovement ORDER BY Description;
ALTER TABLE ProductMovement ADD LocationId BIGINT;
Alter table ProductMovement add Index IX_PMLocationListValueId (LocationId);

CREATE TABLE ProductMovementTemp (
  Id int(11) NOT NULL,
  ProductId int(11) NOT NULL default '0',
  EventId int(11) default NULL,
  Description text,
  ArrivalDate date default '0000-00-00',
  DepartureDate date default '0000-00-00',
  MovedOn tinyint(4) NOT NULL default '0',
  CountryId int(11) default NULL
);

INSERT INTO ProductMovementTemp (Id, ProductId, EventId, Description, ArrivalDate, DepartureDate, MovedOn, CountryId) SELECT Id, ProductId, EventId, Description, ArrivalDate, DepartureDate, MovedOn, CountryId FROM ProductMovement;
DELETE FROM ProductMovement;
INSERT INTO ProductMovement (LocationId, Id, ProductId, EventId, Description, ArrivalDate, DepartureDate, MovedOn, CountryId) SELECT l.Id valueId, pm.Id, pm.ProductId, pm.EventId, pm.Description, pm.ArrivalDate, pm.DepartureDate, pm.MovedOn, pm.CountryId FROM ProductMovementTemp pm LEFT JOIN ListValue l ON pm.Description=l.Value AND l.ListId='EventLocation';
DROP TABLE ProductMovementTemp;

ALTER TABLE ProductMovement DROP Description;

ALTER TABLE Event ADD CoordinatorEmailAddress VARCHAR(150);


ALTER TABLE ProductMovement ADD ReminderEmail VARCHAR(150);


###################
#   21/07/2010    #
###################

ALTER TABLE Product CHANGE COLUMN Code OldCode varchar(200) DEFAULT NULL;
ALTER TABLE Product ADD COLUMN NewCode varchar(200) DEFAULT NULL;

ALTER TABLE Product CHANGE COLUMN Weight GrossWeight varchar(50) DEFAULT NULL;
ALTER TABLE Product ADD COLUMN NetWeight varchar(50) DEFAULT NULL;

CREATE TABLE ProductSegment (
  Id BIGINT NOT NULL,
  NAME VARCHAR(250) NOT NULL,
  Icon VARCHAR(250) NOT NULL,
  PRIMARY KEY (Id)) TYPE = INNODB
ROW_FORMAT = DEFAULT;

INSERT INTO ProductSegment (Id,Name,Icon) VALUES (1,'CVJ Systems','cvj.png');
INSERT INTO ProductSegment (Id,Name,Icon) VALUES (2,'AWD Systems','awd.png');
INSERT INTO ProductSegment (Id,Name,Icon) VALUES (3,'TransAxle Solutions','trans-axle.png');
INSERT INTO ProductSegment (Id,Name,Icon) VALUES (4,'eDrive Systems','edrive.png');

ALTER TABLE Product ADD COLUMN ProductSegmentId Bigint DEFAULT NULL;
ALTER table Product add Index IX_ProductToProductSegment (ProductSegmentId);
ALTER table Product add Foreign Key (ProductSegmentId) references ProductSegment (Id) on delete  restrict on update  restrict;

###################
#   05/08/2011    #
###################

ALTER TABLE Product ADD COLUMN HsCode varchar(200) DEFAULT NULL;

###################
#   22/03/2012    #
###################

ALTER TABLE Product ADD COLUMN TechnologySheetAvailable Boolean DEFAULT 0;