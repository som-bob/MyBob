alter table bob_recipe add difficulty varchar(20) null comment '난이도' after servings;

alter table bob_recipe add cooking_time smallint null comment '소요시간' after difficulty;
