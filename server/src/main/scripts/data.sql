INSERT INTO APPLICATION_TOKEN
  (APPLICATION_TOKEN_ID, NAME, DESCRIPTION, APPLICATION_TOKEN)
VALUES
  ('1',  'android', 'android application', 'd5b87197-495c-47ca-959d-332921004a52'),
  ('2',  'web', 'web application', '08947ad8-fe88-4bb5-a0e1-54ea0417b04e'),
  ('3',  'desktop', 'desktop application', '75b92637-2750-4eb9-8da2-7df486fbfb23'),
  ('4',  'app1', 'application 1', '30587fcf-162a-4de0-bb9c-df52b381d166'),
  ('5',  'app2', 'application 2', '5e36aacf-9e7e-4f6f-9625-ff82aa70006d'),
  ('6',  'app3', 'application 3', '4495a301-115e-4be6-85e7-3ab98df05e02'),
  ('7',  'app4', 'application 4', 'dd49f29d-ccea-45cb-9d75-5595c19b7c5c'),
  ('8',  'app5', 'application 5', 'aa50083e-968e-4f33-8330-97745a497713'),
  ('9',  'app6', 'application 6', '060fc4d5-6ee4-4c05-8218-cd383d94c185'),
  ('10', 'app7', 'application 7', '9c7d2596-0eac-43be-acd3-fce44952aadc')
;



INSERT INTO MYCHAT_USER
  (MYCHAT_USER_ID, IDENTIFICATION_TYPE, IDENTIFICATION, PASSWORD, SALT,
   STATUS, CREATION_DATE, MODIFICATION_DATE, ACTIVATION_TOKEN)
VALUES
  ('1', 'EMAIL', 'toby@gmail.com',
   'ffffff90ffffffa752ffffffd9ffffffd1ffffff982f16ffffffe18ffffffc276fffffff1ffffffa835c6dffffffa8ffffff9b1effffff9e657433bffffffacffffffcf31ffffffb9497f7b',
   '2206ad2f', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'asdinhasd23'),
  ('2', 'EMAIL', 'jack@gmail.com',
   'ffffff90ffffffa752ffffffd9ffffffd1ffffff982f16ffffffe18ffffffc276fffffff1ffffffa835c6dffffffa8ffffff9b1effffff9e657433bffffffacffffffcf31ffffffb9497f7b',
   '2206ad2f', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '2ejhsadfjksd2'),
  ('3', 'EMAIL', 'noah@gmail.com',
  'ffffff90ffffffa752ffffffd9ffffffd1ffffff982f16ffffffe18ffffffc276fffffff1ffffffa835c6dffffffa8ffffff9b1effffff9e657433bffffffacffffffcf31ffffffb9497f7b',
  '2206ad2f', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'asdjhk3riwsfnlksd'),
  ('4', 'EMAIL', 'evan@gmail.com',
  'ffffff90ffffffa752ffffffd9ffffffd1ffffff982f16ffffffe18ffffffc276fffffff1ffffffa835c6dffffffa8ffffff9b1effffff9e657433bffffffacffffffcf31ffffffb9497f7b',
  '2206ad2f', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'jklsdvi903edfv'),
  ('5', 'EMAIL', 'biz@gmail.com',
  'ffffff90ffffffa752ffffffd9ffffffd1ffffff982f16ffffffe18ffffffc276fffffff1ffffffa835c6dffffffa8ffffff9b1effffff9e657433bffffffacffffffcf31ffffffb9497f7b',
  '2206ad2f', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'jknrf9qe2nbd')
;

INSERT INTO CONTACT
  (MYCHAT_USER_ID, CONTACT_MYCHAT_USER_ID, STATUS, CREATION_DATE)
VALUES
  (1, 2, 'PENDING_CONFIRMATION', CURRENT_TIMESTAMP),
  (1, 3, 'PENDING_ACCEPTANCE', CURRENT_TIMESTAMP),
  (1, 4, 'ACCEPTED', CURRENT_TIMESTAMP),
  (1, 5, 'ACCEPTED', CURRENT_TIMESTAMP)
;


INSERT INTO PROFILE
  (PROFILE_ID, MYCHAT_USER_ID, WHATSUP, FULLNAME, GENDER, CREATION_DATE, MODIFICATION_DATE)
VALUES
  ('1', '1', 'This is Toby', 'Toby', 'MALE', CURRENT_TIMESTAMP, NULL),
  ('2', '2', 'This is Jack', 'Jack', 'MALE', CURRENT_TIMESTAMP, NULL),
  ('3', '3', 'This is Noah', 'Noah', 'MALE', CURRENT_TIMESTAMP, NULL),
  ('4', '4', 'This is Evan', 'Evan', 'MALE', CURRENT_TIMESTAMP, NULL),
  ('5', '5', 'This is Biz', 'Biz', 'MALE', CURRENT_TIMESTAMP, NULL)
;

INSERT INTO CHANNEL
  (CHANNEL_ID, MYCHAT_USER_ID,  TYPE, TYPE_ID, CREATION_DATE)
VALUES
  ('1', '1', 'MYCHAT', '1', CURRENT_TIMESTAMP),
  ('2', '2', 'MYCHAT', '2', CURRENT_TIMESTAMP),
  ('3', '3', 'MYCHAT', '3', CURRENT_TIMESTAMP),
  ('4', '4', 'MYCHAT', '4', CURRENT_TIMESTAMP),
  ('5', '5', 'MYCHAT', '5', CURRENT_TIMESTAMP)
;

INSERT INTO ROLE
  (ROLE_ID, MYCHAT_USER_ID, ROLE)
VALUES
  ('1', '1', 'USER'),
  ('2', '2', 'USER'),
  ('3', '3', 'USER'),
  ('4', '4', 'USER'),
  ('5', '5', 'USER')
;
