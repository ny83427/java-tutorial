/* contests */
CREATE TABLE contests
(
  id           VARCHAR(128) PRIMARY KEY NOT NULL ON CONFLICT REPLACE,
  url          VARCHAR(128),
  startTime    DATETIME,
  problemCount INT(8),
  totalPoints  INT(8),
  duration     INT(8),
  createTime   DATETIME                 NOT NULL,
  updateTime   DATETIME                 NOT NULL
);
/* problems */
CREATE TABLE problems
(
  id         VARCHAR(128) PRIMARY KEY NOT NULL ON CONFLICT REPLACE,
  number     INT(8),
  url        VARCHAR(128),
  point      INT(8),
  contestId  VARCHAR(128)             NOT NULL,
  createTime DATETIME                 NOT NULL,
  updateTime DATETIME                 NOT NULL
);
/* ranks */
CREATE TABLE ranks
(
  id               VARCHAR(128) PRIMARY KEY NOT NULL ON CONFLICT REPLACE,
  contestId        VARCHAR(128)             NOT NULL,
  rank             INT(8),
  user             VARCHAR(128),
  country          VARCHAR(128),
  score            INT(8),
  problemsResolved INT(8),
  finishTime       INT(8),
  q1FinishTime     INT(8),
  q1Failures       INT(8),
  q2FinishTime     INT(8),
  q2Failures       INT(8),
  q3FinishTime     INT(8),
  q3Failures       INT(8),
  q4FinishTime     INT(8),
  q4Failures       INT(8),
  q5FinishTime     INT(8),
  q5Failures       INT(8),
  createTime       DATETIME                 NOT NULL,
  updateTime       DATETIME                 NOT NULL
);
/* tasks */
CREATE TABLE tasks
(
  id         VARCHAR(128) PRIMARY KEY NOT NULL ON CONFLICT REPLACE,
  contestId  VARCHAR(128)             NOT NULL,
  url        VARCHAR(256)             NOT NULL,
  pageNo     INT(8),
  status     INT(8),
  createTime DATETIME                 NOT NULL,
  updateTime DATETIME                 NOT NULL
);
/* action_logs */
CREATE TABLE action_logs
(
  id         VARCHAR(128) PRIMARY KEY NOT NULL ON CONFLICT REPLACE,
  status     INT(8),
  createTime DATETIME                 NOT NULL,
  updateTime DATETIME                 NOT NULL
)