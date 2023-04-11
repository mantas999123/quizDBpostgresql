CREATE TABLE "Users" (
  "Id" serial PRIMARY KEY,
  "UserName" varchar(100) UNIQUE NOT NULL,
  "Name" varchar(80) NOT NULL,
  "Surname" varchar(100) NOT NULL,
  "Password" varchar(80) NOT NULL,
  "Email" varchar(120),
  "Role" varchar(20) NOT NULL
);

CREATE TABLE "Questions" (
  "Id" serial PRIMARY KEY,
  "ExamId" int NOT NULL,
  "Question" varchar(250) NOT NULL,
  "OptionA" varchar(250) NOT NULL,
  "OptionB" varchar(250) NOT NULL,
  "OptionC" varchar(250) NOT NULL,
  "OptionD" varchar(250) NOT NULL,
  "CorrectAnswer" varchar(10) NOT NULL
);

CREATE TABLE "User_Answers" (
  "Id" serial PRIMARY KEY,
  "Q_id" int,
  "Answer" varchar(20),
  "C_CorrectAnswer" varchar(10) NOT NULL,
  "U_username" varchar(80) NOT NULL
);

CREATE TABLE "User_Final_Result" (
  "Id" serial PRIMARY KEY,
  "Exam_Id" int NOT NULL,
  "U_username" varchar(80) NOT NULL,
  "DateAndTime" timestamp,
  "assessment" int(10) NOT NULL
);

CREATE TABLE "Exam_Name" (
  "Id" serial PRIMARY KEY,
  "Ex_Name" varchar(250)
);

ALTER TABLE "User_Answers" ADD FOREIGN KEY ("Q_id") REFERENCES "Questions" ("Id");

ALTER TABLE "User_Answers" ADD FOREIGN KEY ("U_username") REFERENCES "Users" ("UserName");

ALTER TABLE "Questions" ADD FOREIGN KEY ("ExamId") REFERENCES "Exam_Name" ("Id");

ALTER TABLE "User_Final_Result" ADD FOREIGN KEY ("U_username") REFERENCES "Users" ("UserName");
