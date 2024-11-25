-- 재료 양에 대한 값을 bob_recipe_ingredients 테이블로 이동
ALTER TABLE bob_recipe_ingredients ADD amount varchar(100) DEFAULT NULL NULL COMMENT '양 텍스트 (ex: 1/2T, 3T, 적당량 등)';
ALTER TABLE bob_recipe_ingredients CHANGE amount amount varchar(100) DEFAULT NULL NULL COMMENT '양 텍스트 (ex: 1/2T, 3T, 적당량 등)' AFTER ingredient_id;

ALTER TABLE bob_recipe_detail_ingredient DROP COLUMN amount;
