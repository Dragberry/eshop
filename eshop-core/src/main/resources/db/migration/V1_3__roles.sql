-- Admin roles
INSERT INTO ROLE (ROLE_KEY, MODULE, ACTION)
	VALUES ((SELECT MAX(GEN_VALUE) FROM GENERATOR WHERE GEN_NAME = 'ROLE_GEN'), 'ADMIN', 'VIEW');
UPDATE GENERATOR SET GEN_VALUE = GEN_VALUE + 1 WHERE GEN_NAME = 'ROLE_GEN';