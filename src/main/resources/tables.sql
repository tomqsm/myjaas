drop table comment;
drop table chronicle;

CREATE TABLE chronicle (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) CONSTRAINT chronicle_pk PRIMARY KEY,
    parentId INT DEFAULT 0,
    tag VARCHAR(50) DEFAULT NULL,
    description VARCHAR(400) DEFAULT NULL,
    inserted TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE comment (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) CONSTRAINT comment_pk PRIMARY KEY,
    chronicleId INT,
    description VARCHAR(400) DEFAULT NULL,
    inserted TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chronicleId_fk FOREIGN KEY (chronicleId) REFERENCES chronicle (id)
);
create index parentIdIdx on chronicle(parentId);
INSERT INTO CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) VALUES (0, 'esgo4', 'project for banking', '2014-12-03 09:54:57.042');
INSERT INTO CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) VALUES (1, 'story', 'bug fixing', '2014-12-03 09:58:36.365');
INSERT INTO CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) VALUES (10, 'fault123423', 'insert optimisation properties', '2014-12-03 09:59:12.755');
INSERT INTO CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) VALUES (0, 'letsweb', 'own r&D', '2014-12-03 09:59:35.235');
INSERT INTO CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) VALUES (4, 'story - tasker', 'an application for monitoring work time', '2014-12-03 10:00:14.552');
INSERT INTO CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) VALUES (5, 'task improving sqls', 'self-linking table', '2014-12-03 10:00:57.878');
INSERT INTO CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) VALUES (3, 'blocker', 'no internet connection', '2014-12-03 10:01:20.208');
INSERT INTO CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) VALUES (3, 'internat is back', 'intervention of an IT team', '2014-12-03 10:29:44.614');
INSERT INTO CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) VALUES (3, 'internet blocked again', 'no internet connection, can''t work', '2014-12-03 11:52:49.958');
INSERT INTO CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) VALUES (1, 'story1', 'refrev', '2014-12-03 11:54:27.801');




select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where CNT > (select count (*) from chronicle) - 1;
select * from COMMENT;
select * from (select ROW_NUMBER() OVER() as R, chronicle.* from chronicle) AS CR where R <= 2;
select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where CNT > (select count (*) from chronicle) - 2;
select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where CNT > (select count (*) from chronicle) - 2 order by CNT desc offset 1 rows;
select count (*) as cnt from chronicle;
select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where inserted between '2014-12-01 09:00:52.985' and '2014-12-01 12:09:52.985';
select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle where inserted between '2014-12-01 09:00:52.985' and '2014-12-01 12:09:52.985') AS CR;
select * from chronicle where tag!='work' and tag!='break';
select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where CNT=1;
select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle where tag='work') AS CR where CNT > (select count (*) from chronicle where tag='work')-3 order by CNT desc;
select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle where tag!='work' and tag!='break') AS CR where CNT > (select count (*) from chronicle where tag!='work' and tag!='break')-2 order by CNT desc;
select * from chronicle where id in (select p.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId or p.ID=302);
select * from chronicle where id in (select p.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId);
select * from chronicle where id in (select c.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId);
select * from chronicle where id in (select c.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId);
select * from chronicle where id in (select c.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId) or id in (select p.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId);
select * from chronicle where id in (select c.id from CHRONICLE as p, CHRONICLE as c where p.ID in (select p.id from CHRONICLE as p, CHRONICLE as c where p.ID = c.parentId));
select p.parentId, count(*) as "references" from CHRONICLE as p group by p.parentId;

INSERT INTO CHRONICLE (parentId, TAG, DESCRIPTION, INSERTED) VALUES (0, 'esg04', 'banking', DEFAULT);

-- proposed view of relationships
select p.id as "id", p.parentId as "belongsTo", c.id as "has" from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId;

CREATE VIEW relations (thisId, belongsTo, has) AS select p.id as "id", p.parentId as "belongsTo", c.id as "has" from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId;
DROP VIEW relations;

-- select trunk entries
select * from CHRONICLE where PARENTID = 0;

select * from RELATIONS;

-- select leaf entries
select * from CHRONICLE c where ID in (select has from RELATIONS where has not in (select BELONGSTO from RELATIONS) and has not in (select thisid from RELATIONS));
select id from CHRONICLE where id not in (select PARENTID from CHRONICLE);
select * from CHRONICLE where tag like '%ck%';
select * from CHRONICLE;

select BELONGSTO, HAS from RELATIONS where THISID = 3;
select BELONGSTO, HAS from RELATIONS where THISID = 5;
select BELONGSTO, HAS from RELATIONS where THISID = 6;
select BELONGSTO, HAS from RELATIONS where THISID = 10;
select id, PARENTID as "belongsTo" from CHRONICLE where ID=7;
select id, PARENTID as "belongsTo" from CHRONICLE where ID=3;
select id, PARENTID as "belongsTo" from CHRONICLE where ID=10;
select id, PARENTID as "belongsTo" from CHRONICLE where ID=1;
select id, PARENTID as "belongsTo" from CHRONICLE where ID=9;
select PARENTID as "id", id as "has" from CHRONICLE where PARENTID=1;
select PARENTID as "id", id as "has" from CHRONICLE where PARENTID=2;
select PARENTID as "id", id as "has" from CHRONICLE where PARENTID=10;
select * from CHRONICLE c where PARENTID = 3;
select PARENTID as "id", id as "has" from CHRONICLE where PARENTID=3;
select PARENTID as "id", id as "has" from CHRONICLE where PARENTID=7;
-- 7 - 3 - 10 - 1
select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where id=(select max(id) from chronicle);