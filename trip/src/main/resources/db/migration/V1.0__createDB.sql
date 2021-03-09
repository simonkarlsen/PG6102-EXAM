create sequence hibernate_sequence start with 1 increment by 1;

create table trip
(
    trip_id     integer not null,
    place       varchar(255) not null,
    duration    varchar(255) not null,
    cost        integer      not null check (cost >= 0),
    primary key (trip_id)
);

----
--insert into trip values(10001, 'place1', '1 week', 200);
--insert into trip values(10002, 'place2', '1 week', 200);
--insert into trip values(10003, 'place3', '1 week', 200);
--insert into trip values(10004, 'place4', '1 week', 200);
--insert into trip values(10005, 'place5', '1 week', 250);
--
--insert into trip values(10006, 'place6', '2 weeks', 400);
--insert into trip values(10007, 'place7', '2 weeks', 400);
--insert into trip values(10008, 'place8', '2 weeks', 400);
--insert into trip values(10009, 'place9', '2 weeks', 400);
--insert into trip values(10010, 'place10', '2 weeks', 400);
--
--insert into trip values(10011, 'place11', '2 weeks', 450);
--insert into trip values(10012, 'place12', '2 weeks', 450);
--insert into trip values(10013, 'place13', '2 weeks', 450);
--insert into trip values(10014, 'place14', '2 weeks', 450);
--insert into trip values(10015, 'place15', '2 weeks', 450);
--
--insert into trip values(10016, 'place16', '3 weeks', 600);
--insert into trip values(10017, 'place17', '3 weeks', 600);
--insert into trip values(10018, 'place18', '3 weeks', 600);
--insert into trip values(10019, 'place19', '3 weeks', 600);
--insert into trip values(10020, 'place20', '3 weeks', 600);
----
