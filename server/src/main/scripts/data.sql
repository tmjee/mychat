INSERT INTO "APPLICATION_TOKEN"
  ("APPLICATION_TOKEN_ID", "NAME", "DESCRIPTION", "KEY")
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



INSERT INTO "MYCHAT_USER"
  ("MYCHAT_USER_ID", "IDENTIFICATION_TYPE", "IDENTIFICATION", "PASSWORD", "SALT",
   "STATUS", "CREATION_DATE", "MODIFICATION_DATE")
VALUES
  ('1', 'EMAIL', 'toby@gmail.com',
   'ffffff90ffffffa752ffffffd9ffffffd1ffffff982f16ffffffe18ffffffc276fffffff1ffffffa835c6dffffffa8ffffff9b1effffff9e657433bffffffacffffffcf31ffffffb9497f7b',
   '2206ad2f', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('2', 'EMAIL', 'jack@gmail.com',
   'ffffff90ffffffa752ffffffd9ffffffd1ffffff982f16ffffffe18ffffffc276fffffff1ffffffa835c6dffffffa8ffffff9b1effffff9e657433bffffffacffffffcf31ffffffb9497f7b',
   '2206ad2f', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('3', 'EMAIL', 'noah@gmail.com',
  'ffffff90ffffffa752ffffffd9ffffffd1ffffff982f16ffffffe18ffffffc276fffffff1ffffffa835c6dffffffa8ffffff9b1effffff9e657433bffffffacffffffcf31ffffffb9497f7b',
  '2206ad2f', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('4', 'EMAIL', 'evan@gmail.com',
  'ffffff90ffffffa752ffffffd9ffffffd1ffffff982f16ffffffe18ffffffc276fffffff1ffffffa835c6dffffffa8ffffff9b1effffff9e657433bffffffacffffffcf31ffffffb9497f7b',
  '2206ad2f', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('5', 'EMAIL', 'biz@gmail.com',
  'ffffff90ffffffa752ffffffd9ffffffd1ffffff982f16ffffffe18ffffffc276fffffff1ffffffa835c6dffffffa8ffffff9b1effffff9e657433bffffffacffffffcf31ffffffb9497f7b'
  '2206ad2f', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
;


INSERT INTO "PROFILE"
  ("PROFILE_ID", "MYCHAT_USER_ID", "WHATSUP", "FULLNAME", "GENDER", "CREATION_DATE", "MODIFICATION_DATE")
VALUES
  ('1', '1', 'This is Toby', 'Toby', 'MALE', CURRENT_TIMESTAMP, NULL),
  ('2', '2', 'This is Jack', 'Jack', 'MALE', CURRENT_TIMESTAMP, NULL),
  ('3', '3', 'This is Noah', 'Noah', 'MALE', CURRENT_TIMESTAMP, NULL),
  ('4', '4', 'This is Evan', 'Evan', 'MALE', CURRENT_TIMESTAMP, NULL),
  ('5', '5', 'This is Biz', 'Biz', 'MALE', CURRENT_TIMESTAMP, NULL)
;


