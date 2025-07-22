# Notes

- Please contact me if you need specific user credential
- Ready-to-use database with sample data is included in this project in the file called database.db
- The database in production will be regularly wiped and restored with this sample database.db (obviously, the password
  will be different)
- Swagger is in `(domain)/swagger-ui/index.html#/`

# application.properties

> **NOTE:**
> These values are for sample/demonstration purpose only, adjust them accordingly

> spring.application.name=exam-be  
> spring.mvc.dispatch-options-request=true  
> server.port=8085
> secure.cookie=false #this determine whether cookie is https and adjust the sameSite policy accordingly  
> frontend-address=http://localhost:5173 #this is for cors handling
>
>springdoc.swagger-ui.authorization-bearer-format="JWT"
> springdoc.swagger-ui.request-redirect-enabled=true
> springdoc.swagger-ui.persist-authorize=true
>
>spring.datasource.url=jdbc:sqlite:db.db  
> spring.datasource.driver-class-name=org.sqlite.JDBC  
> spring.datasource.hikari.connection-test-query=SELECT 1
> spring.jpa.properties.hibernate.dialect=org.hibernate.community.dialect.SQLiteDialect
>
>jwt.secret=[secret here]  
> jwt.access.expiration=600 #10 minutes  
> jwt.refresh.expiration=604800 #7 days

# Database schema

> **NOTE:**
> Make sure to enable foreign key first with `PRAGMA foreign_keys = ON;`

> **NOTE:**
> Internally, SQLite store INTEGER datatype as int64 (long) so the token expiration time won't overflow


CREATE TABLE IF NOT EXISTS user (
user_id TEXT PRIMARY KEY,
name TEXT NOT NULL,
email TEXT UNIQUE NOT NULL,
password TEXT NOT NULL,
role TEXT NOT NULL CHECK (role IN ('STUDENT', 'TEACHER', 'ADMIN'))
);

CREATE TABLE IF NOT EXISTS token (
token_id TEXT PRIMARY KEY,
token TEXT NOT NULL UNIQUE,
expires_in INTEGER NOT NULL,
user_id TEXT NOT NULL REFERENCES user (user_id)
);

CREATE TABLE IF NOT EXISTS course (
course_id TEXT PRIMARY KEY,
name TEXT NOT NULL
);

-- association table  
CREATE TABLE IF NOT EXISTS course_teacher (
course_teacher_id TEXT PRIMARY KEY,
course_id TEXT NOT NULL REFERENCES course (course_id),
teacher_id TEXT NOT NULL REFERENCES user (user_id)
);

-- association table  
CREATE TABLE IF NOT EXISTS enrollment (
student_id TEXT NOT NULL REFERENCES user (user_id),
course_teacher_id TEXT NOT NULL REFERENCES course_teacher (course_teacher_id),
PRIMARY KEY (course_teacher_id, student_id)
);

CREATE TABLE IF NOT EXISTS exam (
exam_id TEXT PRIMARY KEY,
type TEXT NOT NULL CHECK (type IN ('QUIZ', 'MID', 'FINAL')),
passing_grade INTEGER NOT NULL,
start_date TEXT NOT NULL,
end_date TEXT NOT NULL,
is_graded INTEGER NOT NULL CHECK (is_correct IN (0, 1)),
course_teacher_id TEXT NOT NULL REFERENCES course_teacher (course_teacher_id)
);

-- association table  
CREATE TABLE IF NOT EXISTS student_exam (
grade INTEGER,
submit_date TEXT,
student_id TEXT NOT NULL REFERENCES user (user_id),
exam_id TEXT NOT NULL REFERENCES exam (exam_id),
PRIMARY KEY (student_id, exam_id)
);

CREATE TABLE IF NOT EXISTS question (
question_id TEXT PRIMARY KEY,
text TEXT NOT NULL,
exam_id TEXT NOT NULL REFERENCES exam (exam_id)
);

CREATE TABLE IF NOT EXISTS answer (
answer_id TEXT PRIMARY KEY,
text TEXT NOT NULL,
is_correct INTEGER NOT NULL CHECK (is_correct IN (0, 1)),
question_id INTEGER NOT NULL REFERENCES question (question_id)
);

-- association table  
CREATE TABLE IF NOT EXISTS student_answer (
student_answer_id TEXT PRIMARY KEY,
question_id TEXT NOT NULL REFERENCES question (question_id),
answer_id TEXT NOT NULL REFERENCES answer (answer_id),
student_id TEXT NOT NULL REFERENCES user(user_id)
);