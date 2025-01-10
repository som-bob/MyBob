-- 테스트 재료 5개 추가
INSERT INTO bob_ingredients (ingredient_name, ingredient_description, reg_id, reg_date, mod_date) VALUES ('1_재료', '1번 테스트 재료.','system@system.com',CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bob_ingredients (ingredient_name, ingredient_description, reg_id, reg_date, mod_date) VALUES ('2_재료', '2번 테스트 재료.','system@system.com',CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bob_ingredients (ingredient_name, ingredient_description, reg_id, reg_date, mod_date) VALUES ('3_재료', '3번 테스트 재료.','system@system.com',CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bob_ingredients (ingredient_name, ingredient_description, reg_id, reg_date, mod_date) VALUES ('4_재료', '4번 테스트 재료.','system@system.com',CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bob_ingredients (ingredient_name, ingredient_description, reg_id, reg_date, mod_date) VALUES ('5_재료', '5번 테스트 재료.','system@system.com',CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 1번 레시피 (재료: 1, 2, 3, 4, 5)
INSERT INTO bob_recipe (recipe_name, recipe_description, difficulty, cooking_time, source, image_url, servings, reg_id, reg_date, mod_date)
VALUES ('1번 레시피', '1번 레시피입니다. 1번 레시피 조회', 'BEGINNER', 30, 'http://example.com/recipe/onion-soup', null, 2, 'system@system.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bob_recipe_ingredients (recipe_id, ingredient_id, reg_id, reg_date, mod_date) VALUES (1, 1, 'system@system.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bob_recipe_ingredients (recipe_id, ingredient_id, reg_id, reg_date, mod_date) VALUES (1, 1, 'system@system.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bob_recipe_ingredients (recipe_id, ingredient_id, reg_id, reg_date, mod_date) VALUES (1, 2, 'system@system.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bob_recipe_ingredients (recipe_id, ingredient_id, reg_id, reg_date, mod_date) VALUES (1, 3, 'system@system.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bob_recipe_ingredients (recipe_id, ingredient_id, reg_id, reg_date, mod_date) VALUES (1, 4, 'system@system.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bob_recipe_ingredients (recipe_id, ingredient_id, reg_id, reg_date, mod_date) VALUES (1, 5, 'system@system.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 2번 레피시 (재료: 1, 5)
INSERT INTO bob_recipe (recipe_name, recipe_description, difficulty, cooking_time, source, image_url, servings, reg_id, reg_date, mod_date)
VALUES ('2번 레시피', '2번 레시피. 검색 조건', 'BEGINNER', 60, 'http://example.com/recipe/onion-soup', null, 1, 'system@system.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bob_recipe_ingredients (recipe_id, ingredient_id, reg_id, reg_date, mod_date) VALUES (2, 1, 'system@system.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bob_recipe_ingredients (recipe_id, ingredient_id, reg_id, reg_date, mod_date) VALUES (2, 5, 'system@system.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 3번 레시피 (3, 4)
INSERT INTO bob_recipe (recipe_name, recipe_description, difficulty, cooking_time, source, image_url, servings, reg_id, reg_date, mod_date)
VALUES ('3번 레시피', '3번 레시피. 맛있는 레시피.', 'BEGINNER', 60, 'http://example.com/recipe/onion-soup', null, 1, 'system@system.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bob_recipe_ingredients (recipe_id, ingredient_id, reg_id, reg_date, mod_date) VALUES (3, 3, 'system@system.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bob_recipe_ingredients (recipe_id, ingredient_id, reg_id, reg_date, mod_date) VALUES (3, 4, 'system@system.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);