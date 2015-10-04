# --- !Ups

CREATE SEQUENCE candidatemodel_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

CREATE SEQUENCE companymodel_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

CREATE SEQUENCE conversationmodel_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

CREATE SEQUENCE countrymodel_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

CREATE SEQUENCE educationmodel_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

CREATE SEQUENCE employermodel_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

CREATE SEQUENCE experiencemodel_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

CREATE SEQUENCE jobapplymodel_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

CREATE SEQUENCE jobmodel_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

CREATE SEQUENCE messagemodel_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

CREATE SEQUENCE readmessage_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

CREATE SEQUENCE skillmodel_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

CREATE SEQUENCE statemodel_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

CREATE SEQUENCE usermodel_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;

CREATE TABLE candidatemodel (
	id bigint DEFAULT nextval('candidatemodel_id_seq'::regclass) NOT NULL,
	cityname character varying(256),
	countrycode character varying(256),
	firstname character varying(256),
	lastname character varying(256),
	statecode character varying(256),
	summary character varying(256),
	user_id bigint
);

CREATE TABLE candidatemodel_skillmodel (
	candidatemodel_id bigint NOT NULL,
	skills_id bigint NOT NULL
);

CREATE TABLE companymodel (
	id bigint DEFAULT nextval('companymodel_id_seq'::regclass) NOT NULL,
	description character varying(256),
	name character varying(256),
	user_id bigint
);

CREATE TABLE conversationmodel (
	id bigint DEFAULT nextval('conversationmodel_id_seq'::regclass) NOT NULL,
	title character varying(256) NOT NULL,
	jobapplication_id bigint
);

CREATE TABLE conversationmodel_usermodel (
	conversations_id bigint NOT NULL,
	members_id bigint NOT NULL
);

CREATE TABLE countrymodel (
	id bigint DEFAULT nextval('countrymodel_id_seq'::regclass) NOT NULL,
	countrycode character varying(10) NOT NULL,
	countryname character varying(256) NOT NULL
);

CREATE TABLE educationmodel (
	id bigint DEFAULT nextval('educationmodel_id_seq'::regclass) NOT NULL,
	degree character varying(255),
	enddate timestamp without time zone,
	school character varying(255),
	startdate timestamp without time zone,
	candidate_id bigint
);

CREATE TABLE employermodel (
	id bigint DEFAULT nextval('employermodel_id_seq'::regclass) NOT NULL,
	firstname character varying(256),
	lastname character varying(256),
	company_id bigint,
	user_id bigint
);

CREATE TABLE experiencemodel (
	id bigint DEFAULT nextval('experiencemodel_id_seq'::regclass) NOT NULL,
	company character varying(255),
	enddate timestamp without time zone,
	"position" character varying(255),
	startdate timestamp without time zone,
	candidate_id bigint
);

CREATE TABLE jobapplymodel (
	id bigint DEFAULT nextval('jobapplymodel_id_seq'::regclass) NOT NULL,
	status character varying(256) NOT NULL,
	candidate_id bigint,
	job_id bigint
);

CREATE TABLE jobmodel (
	id bigint DEFAULT nextval('jobmodel_id_seq'::regclass) NOT NULL,
	cityname character varying(256),
	countrycode character varying(256),
	description character varying(256) NOT NULL,
	experience integer,
	maxsalary integer,
	minsalary integer,
	statecode character varying(256),
	title character varying(256) NOT NULL,
	company_id bigint
);

CREATE TABLE jobmodel_candidatemodel (
	jobmodel_id bigint NOT NULL,
	visitors_id bigint NOT NULL
);

CREATE TABLE jobmodel_skillmodel (
	jobmodel_id bigint NOT NULL,
	skills_id bigint NOT NULL
);

CREATE TABLE jobsearchresult (
	jobmodelid bigint NOT NULL
);

CREATE TABLE messagemodel (
	id bigint DEFAULT nextval('messagemodel_id_seq'::regclass) NOT NULL,
	message character varying(256) NOT NULL,
	conversation_id bigint,
	owner_id bigint
);

CREATE TABLE readmessage (
	id bigint DEFAULT nextval('readmessage_id_seq'::regclass) NOT NULL,
	status character varying(255) NOT NULL,
	message_id bigint,
	user_id bigint
);

CREATE TABLE searchresult (
	id bigint NOT NULL
);

CREATE TABLE skillmodel (
	id bigint DEFAULT nextval('skillmodel_id_seq'::regclass) NOT NULL,
	name character varying(256) NOT NULL
);

CREATE TABLE statemodel (
	id bigint DEFAULT nextval('statemodel_id_seq'::regclass) NOT NULL,
	code character varying(10) NOT NULL,
	name character varying(256) NOT NULL
);

CREATE TABLE usermodel (
	id bigint DEFAULT nextval('usermodel_id_seq'::regclass) NOT NULL,
	authtoken character varying(255),
	emailaddress character varying(256),
	imagefilename character varying(255),
	imageurl character varying(255),
	"role" character varying(256) NOT NULL,
	shapassword bytea NOT NULL,
	twitterid bigint,
	candidate_id bigint,
	company_id bigint,
	employer_id bigint
);

ALTER SEQUENCE candidatemodel_id_seq
	OWNED BY candidatemodel.id;

ALTER SEQUENCE companymodel_id_seq
	OWNED BY companymodel.id;

ALTER SEQUENCE conversationmodel_id_seq
	OWNED BY conversationmodel.id;

ALTER SEQUENCE countrymodel_id_seq
	OWNED BY countrymodel.id;

ALTER SEQUENCE educationmodel_id_seq
	OWNED BY educationmodel.id;

ALTER SEQUENCE employermodel_id_seq
	OWNED BY employermodel.id;

ALTER SEQUENCE experiencemodel_id_seq
	OWNED BY experiencemodel.id;

ALTER SEQUENCE jobapplymodel_id_seq
	OWNED BY jobapplymodel.id;

ALTER SEQUENCE jobmodel_id_seq
	OWNED BY jobmodel.id;

ALTER SEQUENCE messagemodel_id_seq
	OWNED BY messagemodel.id;

ALTER SEQUENCE readmessage_id_seq
	OWNED BY readmessage.id;

ALTER SEQUENCE skillmodel_id_seq
	OWNED BY skillmodel.id;

ALTER SEQUENCE statemodel_id_seq
	OWNED BY statemodel.id;

ALTER SEQUENCE usermodel_id_seq
	OWNED BY usermodel.id;

ALTER TABLE candidatemodel
	ADD CONSTRAINT candidatemodel_pkey PRIMARY KEY (id);

ALTER TABLE candidatemodel_skillmodel
	ADD CONSTRAINT candidatemodel_skillmodel_pkey PRIMARY KEY (candidatemodel_id, skills_id);

ALTER TABLE companymodel
	ADD CONSTRAINT companymodel_pkey PRIMARY KEY (id);

ALTER TABLE conversationmodel
	ADD CONSTRAINT conversationmodel_pkey PRIMARY KEY (id);

ALTER TABLE conversationmodel_usermodel
	ADD CONSTRAINT conversationmodel_usermodel_pkey PRIMARY KEY (conversations_id, members_id);

ALTER TABLE countrymodel
	ADD CONSTRAINT countrymodel_pkey PRIMARY KEY (id);

ALTER TABLE educationmodel
	ADD CONSTRAINT educationmodel_pkey PRIMARY KEY (id);

ALTER TABLE employermodel
	ADD CONSTRAINT employermodel_pkey PRIMARY KEY (id);

ALTER TABLE experiencemodel
	ADD CONSTRAINT experiencemodel_pkey PRIMARY KEY (id);

ALTER TABLE jobapplymodel
	ADD CONSTRAINT jobapplymodel_pkey PRIMARY KEY (id);

ALTER TABLE jobmodel
	ADD CONSTRAINT jobmodel_pkey PRIMARY KEY (id);

ALTER TABLE jobmodel_skillmodel
	ADD CONSTRAINT jobmodel_skillmodel_pkey PRIMARY KEY (jobmodel_id, skills_id);

ALTER TABLE jobsearchresult
	ADD CONSTRAINT jobsearchresult_pkey PRIMARY KEY (jobmodelid);

ALTER TABLE messagemodel
	ADD CONSTRAINT messagemodel_pkey PRIMARY KEY (id);

ALTER TABLE readmessage
	ADD CONSTRAINT readmessage_pkey PRIMARY KEY (id);

ALTER TABLE searchresult
	ADD CONSTRAINT searchresult_pkey PRIMARY KEY (id);

ALTER TABLE skillmodel
	ADD CONSTRAINT skillmodel_pkey PRIMARY KEY (id);

ALTER TABLE statemodel
	ADD CONSTRAINT statemodel_pkey PRIMARY KEY (id);

ALTER TABLE usermodel
	ADD CONSTRAINT usermodel_pkey PRIMARY KEY (id);

ALTER TABLE candidatemodel
	ADD CONSTRAINT fk_9ce6s4howofelq9qylljs3g6v FOREIGN KEY (user_id) REFERENCES usermodel(id);

ALTER TABLE candidatemodel_skillmodel
	ADD CONSTRAINT fk_a9kw4yvg46vvmnxbehmujvxtl FOREIGN KEY (candidatemodel_id) REFERENCES candidatemodel(id);

ALTER TABLE candidatemodel_skillmodel
	ADD CONSTRAINT fk_p2r3s3ie73n19xyfsw12bollp FOREIGN KEY (skills_id) REFERENCES skillmodel(id);

ALTER TABLE companymodel
	ADD CONSTRAINT fk_bqkxaemywc6ot470859rqkm9c FOREIGN KEY (user_id) REFERENCES usermodel(id);

ALTER TABLE conversationmodel
	ADD CONSTRAINT fk_ra6d4492vcbmf4he984dg50vv FOREIGN KEY (jobapplication_id) REFERENCES jobapplymodel(id);

ALTER TABLE conversationmodel_usermodel
	ADD CONSTRAINT fk_73mqs87eudwl80vmspk8t59ro FOREIGN KEY (members_id) REFERENCES usermodel(id);

ALTER TABLE conversationmodel_usermodel
	ADD CONSTRAINT fk_an82labaoawtjgtydpi9d3qpi FOREIGN KEY (conversations_id) REFERENCES conversationmodel(id);

ALTER TABLE countrymodel
	ADD CONSTRAINT uk_crcmunx0at9vtdllhhciby74s UNIQUE (countrycode);

ALTER TABLE countrymodel
	ADD CONSTRAINT uk_i4xd0xyajex93sps4a8ge9axa UNIQUE (countryname);

ALTER TABLE educationmodel
	ADD CONSTRAINT fk_6ddgpldyh511ubgyoileacm4b FOREIGN KEY (candidate_id) REFERENCES candidatemodel(id);

ALTER TABLE employermodel
	ADD CONSTRAINT fk_pr8uvl6vr1jwvdshldobi56da FOREIGN KEY (company_id) REFERENCES companymodel(id);

ALTER TABLE employermodel
	ADD CONSTRAINT fk_rmkhjstxfqrurf30cx6ovj604 FOREIGN KEY (user_id) REFERENCES usermodel(id);

ALTER TABLE experiencemodel
	ADD CONSTRAINT fk_ji6vulh2p0n5edo52acfftd3l FOREIGN KEY (candidate_id) REFERENCES candidatemodel(id);

ALTER TABLE jobapplymodel
	ADD CONSTRAINT uk_7vl6u9v0jr7o7co9ioiepbfjm UNIQUE (job_id, candidate_id);

ALTER TABLE jobapplymodel
	ADD CONSTRAINT fk_8vliy0km35oc6ffqtt10qb2uv FOREIGN KEY (job_id) REFERENCES jobmodel(id);

ALTER TABLE jobapplymodel
	ADD CONSTRAINT fk_l2i46fjly92hw7fwxtopr6wve FOREIGN KEY (candidate_id) REFERENCES candidatemodel(id);

ALTER TABLE jobmodel
	ADD CONSTRAINT fk_r7gimvavnn7j6alj0rgboi6o2 FOREIGN KEY (company_id) REFERENCES companymodel(id);

ALTER TABLE jobmodel_candidatemodel
	ADD CONSTRAINT fk_c6a6d4s5r2uukn8510s53pfi2 FOREIGN KEY (jobmodel_id) REFERENCES jobmodel(id);

ALTER TABLE jobmodel_candidatemodel
	ADD CONSTRAINT fk_j3xtg02o5631wvs3vusf3af59 FOREIGN KEY (visitors_id) REFERENCES candidatemodel(id);

ALTER TABLE jobmodel_skillmodel
	ADD CONSTRAINT fk_ejdculaekip3pxv4mcofsbdfx FOREIGN KEY (jobmodel_id) REFERENCES jobmodel(id);

ALTER TABLE jobmodel_skillmodel
	ADD CONSTRAINT fk_f5wg1alra9eigiu7vgwobl6wx FOREIGN KEY (skills_id) REFERENCES skillmodel(id);

ALTER TABLE messagemodel
	ADD CONSTRAINT fk_851ksllx4d6de0vm50si7w8io FOREIGN KEY (owner_id) REFERENCES usermodel(id);

ALTER TABLE messagemodel
	ADD CONSTRAINT fk_iwenws0wwuxqsytvl9fxnfh FOREIGN KEY (conversation_id) REFERENCES conversationmodel(id);

ALTER TABLE readmessage
	ADD CONSTRAINT fk_5j20i7h2xu5h23ptusqokkx1 FOREIGN KEY (message_id) REFERENCES messagemodel(id);

ALTER TABLE readmessage
	ADD CONSTRAINT fk_j76pbtdia3v8q6uoy1bmfv0o0 FOREIGN KEY (user_id) REFERENCES usermodel(id);

ALTER TABLE skillmodel
	ADD CONSTRAINT uk_lrqg6q7gt7d6cgi6crnkt199n UNIQUE (name);

ALTER TABLE statemodel
	ADD CONSTRAINT uk_oilmq319wws0l7q85w9el07gq UNIQUE (name);

ALTER TABLE statemodel
	ADD CONSTRAINT uk_r07mx8q7gav66h0be2bo4mbee UNIQUE (code);

ALTER TABLE usermodel
	ADD CONSTRAINT uk_b1vifgtbua25udkhjop2ruqpp UNIQUE (authtoken);

ALTER TABLE usermodel
	ADD CONSTRAINT uk_ggj3i2ep1r9mpyrskxu4bubho UNIQUE (twitterid);

ALTER TABLE usermodel
	ADD CONSTRAINT uk_ke6lmy1fjf6866hs3585kunf4 UNIQUE (emailaddress);

ALTER TABLE usermodel
	ADD CONSTRAINT fk_5frptgpsk2vbsdowbfvv9wyk1 FOREIGN KEY (candidate_id) REFERENCES candidatemodel(id);

ALTER TABLE usermodel
	ADD CONSTRAINT fk_i6cw7y31hjlxmyjn1dtl3dnp4 FOREIGN KEY (employer_id) REFERENCES employermodel(id);

ALTER TABLE usermodel
	ADD CONSTRAINT fk_jfj4wh8yo9hvt7ceqpatyh2lq FOREIGN KEY (company_id) REFERENCES companymodel(id);
# --- !Downs

DROP TABLE candidatemodel;

DROP TABLE candidatemodel_skillmodel;

DROP TABLE companymodel;

DROP TABLE conversationmodel;

DROP TABLE conversationmodel_usermodel;

DROP TABLE countrymodel;

DROP TABLE educationmodel;

DROP TABLE employermodel;

DROP TABLE experiencemodel;

DROP TABLE jobapplymodel;

DROP TABLE jobmodel;

DROP TABLE jobmodel_candidatemodel;

DROP TABLE jobmodel_skillmodel;

DROP TABLE jobsearchresult;

DROP TABLE messagemodel;

DROP TABLE readmessage;

DROP TABLE searchresult;

DROP TABLE skillmodel;

DROP TABLE statemodel;

DROP TABLE usermodel;

DROP SEQUENCE candidatemodel_id_seq;

DROP SEQUENCE companymodel_id_seq;

DROP SEQUENCE conversationmodel_id_seq;

DROP SEQUENCE countrymodel_id_seq;

DROP SEQUENCE educationmodel_id_seq;

DROP SEQUENCE employermodel_id_seq;

DROP SEQUENCE experiencemodel_id_seq;

DROP SEQUENCE jobapplymodel_id_seq;

DROP SEQUENCE jobmodel_id_seq;

DROP SEQUENCE messagemodel_id_seq;

DROP SEQUENCE readmessage_id_seq;

DROP SEQUENCE skillmodel_id_seq;

DROP SEQUENCE statemodel_id_seq;

DROP SEQUENCE usermodel_id_seq;
