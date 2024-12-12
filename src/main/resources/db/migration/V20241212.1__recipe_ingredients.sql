ALTER TABLE bob_recipe_ingredients ADD ingredient_detail_name varchar(200) NULL COMMENT '재료 자세한 이름';
ALTER TABLE bob_recipe_ingredients CHANGE ingredient_detail_name ingredient_detail_name varchar(200) NULL COMMENT '재료 자세한 이름' AFTER ingredient_id;
