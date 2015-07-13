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
  (CONTACT_ID, MYCHAT_USER_ID, CONTACT_MYCHAT_USER_ID, STATUS, CREATION_DATE)
VALUES
  (1, 1, 2, 'PENDING_CONFIRMATION', CURRENT_TIMESTAMP),
  (2, 2, 1, 'PENDING_ACCEPTANCE', CURRENT_TIMESTAMP),
  (3, 1, 3, 'PENDING_ACCEPTANCE', CURRENT_TIMESTAMP),
  (4, 3, 1, 'PENDING_CONFIRMATION', CURRENT_TIMESTAMP),
  (5, 1, 4, 'ACCEPTED', CURRENT_TIMESTAMP),
  (6, 4, 1, 'ACCEPTED', CURRENT_TIMESTAMP),
  (7, 1, 5, 'ACCEPTED', CURRENT_TIMESTAMP),
  (8, 5, 1, 'ACCEPTED', CURRENT_TIMESTAMP)
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


INSERT INTO CHAT
  (CHAT_ID, CREATOR_MYCHAT_USER_ID, NAME, CREATION_DATE)
VALUES
  (1, 1, 'Toby_Chat_1', CURRENT_TIMESTAMP),
  (2, 1, 'Toby_Chat_2', CURRENT_TIMESTAMP),
  (3, 1, 'Toby_Chat_3', CURRENT_TIMESTAMP),
  (4, 1, 'Toby_Chat_4', CURRENT_TIMESTAMP),
  (5, 1, 'Toby_Chat_5', CURRENT_TIMESTAMP),
  (6, 1, 'Toby_Chat_6', CURRENT_TIMESTAMP),
  (7, 2, 'Jack_Chat_1', CURRENT_TIMESTAMP),
  (8, 2, 'Jack_Chat_1', CURRENT_TIMESTAMP),
  (9, 2, 'Jack_Chat_1', CURRENT_TIMESTAMP),
  (10, 2, 'Jack_Chat_1', CURRENT_TIMESTAMP)
;


INSERT INTO CHAT_MEMBER
  (CHAT_MEMBER_ID, CHAT_MEMBER_MYCHAT_USER_ID, CHAT_ID, STATUS)
VALUES
  (1, 1, 1, 'JOINED'),
  (2, 2, 1, 'JOINED'),
  (3, 3, 1, 'LEFT'),
  (4, 4, 1, 'JOINED'),
  (5, 5, 1, 'JOINED'),
  (6, 1, 2, 'JOINED'),
  (7, 2, 2, 'JOINED'),
  (8, 3, 2, 'LEFT'),
  (9, 4, 2, 'JOINED'),
  (10, 5, 2, 'JOINED'),
  (11, 1, 3, 'JOINED'),
  (12, 2, 3, 'JOINED'),
  (13, 3, 3, 'LEFT'),
  (14, 4, 3, 'JOINED'),
  (15, 5, 3, 'JOINED'),
  (16, 1, 4, 'JOINED'),
  (17, 2, 4, 'JOINED'),
  (18, 3, 4, 'LEFT'),
  (19, 4, 4, 'JOINED'),
  (20, 5, 4, 'JOINED'),
  (21, 1, 4, 'JOINED'),
  (22, 2, 4, 'JOINED'),
  (23, 3, 4, 'LEFT'),
  (24, 4, 4, 'JOINED'),
  (25, 5, 4, 'JOINED'),
  (26, 1, 5, 'JOINED'),
  (27, 2, 5, 'JOINED'),
  (28, 3, 5, 'LEFT'),
  (29, 4, 5, 'JOINED'),
  (30, 5, 5, 'JOINED'),
  (31, 1, 6, 'JOINED'),
  (32, 2, 6, 'JOINED'),
  (33, 3, 6, 'LEFT'),
  (34, 4, 6, 'JOINED'),
  (35, 5, 6, 'JOINED'),
  (46, 1, 7, 'JOINED'),
  (47, 2, 7, 'JOINED'),
  (48, 1, 8, 'LEFT'),
  (49, 2, 8, 'JOINED'),
  (50, 1, 9, 'JOINED'),
  (51, 2, 9, 'JOINED'),
  (52, 1, 10, 'JOINED'),
  (53, 2, 10, 'LEFT')
;


INSERT INTO CHAT_MESSAGE
  (CHAT_MESSAGE_ID, CHAT_MEMBER_ID, CHAT_ID, CHAT_MESSAGE, CREATION_DATE)
VALUES
  (1, 1, 1, 'toby message 1', CURRENT_TIMESTAMP),
  (2, 2, 1, 'jack message 1', CURRENT_TIMESTAMP),
  (3, 3, 1, 'noah message 1', CURRENT_TIMESTAMP),
  (4, 4, 1, 'evan message 1', CURRENT_TIMESTAMP),
  (5, 5, 1, 'biz message 1', CURRENT_TIMESTAMP),
  (6, 1, 1, 'toby message 2', CURRENT_TIMESTAMP),
  (7, 2, 1, 'jack message 2', CURRENT_TIMESTAMP),
  (8, 3, 1, 'noah message 2', CURRENT_TIMESTAMP),
  (9, 4, 1, 'evan message 2', CURRENT_TIMESTAMP),
  (10, 5, 1, 'biz message 2', CURRENT_TIMESTAMP),
  (11, 1, 1, 'toby message 3', CURRENT_TIMESTAMP),
  (12, 2, 1, 'jack message 3', CURRENT_TIMESTAMP),
  (13, 3, 1, 'noah message 3', CURRENT_TIMESTAMP),
  (14, 4, 1, 'evan message 3', CURRENT_TIMESTAMP),
  (15, 5, 1, 'biz message 3', CURRENT_TIMESTAMP),
  (16, 1, 1, 'toby message 4', CURRENT_TIMESTAMP),
  (17, 2, 1, 'jack message 4', CURRENT_TIMESTAMP),
  (18, 3, 1, 'noah message 4', CURRENT_TIMESTAMP),
  (19, 4, 1, 'evan message 4', CURRENT_TIMESTAMP),
  (20, 5, 1, 'biz message 4', CURRENT_TIMESTAMP),
  (21, 1, 1, 'toby message 5', CURRENT_TIMESTAMP),
  (22, 2, 1, 'jack message 5', CURRENT_TIMESTAMP),
  (23, 3, 1, 'noah message 5', CURRENT_TIMESTAMP),
  (24, 4, 1, 'evan message 5', CURRENT_TIMESTAMP),
  (25, 5, 1, 'biz message 5', CURRENT_TIMESTAMP),
  (31, 1, 1, 'toby message 6', CURRENT_TIMESTAMP),
  (32, 2, 1, 'jack message 6', CURRENT_TIMESTAMP),
  (33, 3, 1, 'noah message 6', CURRENT_TIMESTAMP),
  (34, 4, 1, 'evan message 6', CURRENT_TIMESTAMP),
  (35, 5, 1, 'biz message 6', CURRENT_TIMESTAMP),
  (36, 1, 1, 'toby message 7', CURRENT_TIMESTAMP),
  (37, 2, 1, 'jack message 7', CURRENT_TIMESTAMP),
  (38, 3, 1, 'noah message 7', CURRENT_TIMESTAMP),
  (39, 4, 1, 'evan message 7', CURRENT_TIMESTAMP),
  (40, 5, 1, 'biz message 7', CURRENT_TIMESTAMP),
  (41, 1, 1, 'toby message 8', CURRENT_TIMESTAMP),
  (42, 2, 1, 'jack message 8', CURRENT_TIMESTAMP),
  (43, 3, 1, 'noah message 8', CURRENT_TIMESTAMP),
  (44, 4, 1, 'evan message 8', CURRENT_TIMESTAMP),
  (45, 5, 1, 'biz message 8', CURRENT_TIMESTAMP),
  (46, 1, 1, 'toby message 9', CURRENT_TIMESTAMP),
  (47, 2, 1, 'jack message 9', CURRENT_TIMESTAMP),
  (48, 3, 1, 'noah message 9', CURRENT_TIMESTAMP),
  (49, 4, 1, 'evan message 9', CURRENT_TIMESTAMP),
  (50, 1, 2, 'Toby message', CURRENT_TIMESTAMP),
  (52, 2, 2, 'Jack message', CURRENT_TIMESTAMP),
  (53, 1, 2, 'Toby message', CURRENT_TIMESTAMP),
  (54, 2, 3, 'Jack message', CURRENT_TIMESTAMP),
  (55, 1, 3, 'Toby message', CURRENT_TIMESTAMP),
  (56, 2, 3, 'Jack message', CURRENT_TIMESTAMP),
  (57, 1, 4, 'Toby message', CURRENT_TIMESTAMP),
  (58, 2, 5, 'Jack message', CURRENT_TIMESTAMP)
;
