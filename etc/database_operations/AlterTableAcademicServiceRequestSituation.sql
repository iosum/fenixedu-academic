ALTER TABLE ACADEMIC_SERVICE_REQUEST_SITUATION ADD COLUMN SITUATION_DATE TIMESTAMP NOT NULL;

UPDATE ACADEMIC_SERVICE_REQUEST_SITUATION SET ACADEMIC_SERVICE_REQUEST_SITUATION.SITUATION_DATE = ACADEMIC_SERVICE_REQUEST_SITUATION.CREATION_DATE;
