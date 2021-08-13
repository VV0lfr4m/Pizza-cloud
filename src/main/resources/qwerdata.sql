delete from "pizza_order_pizzas";
delete from "pizza_ingredients";
delete from "pizza";
delete from "pizza_order";
delete from "ingredient";
insert into "ingredient" (id, name, type)
values ('YD', 'Yeast Dough', 'DOUGH');
insert into "ingredient" (id, name, type)
values ('YFD', 'Yeast-free Dough', 'DOUGH');
insert into "ingredient" (id, name, type)
values ('BCN', 'Bacon', 'PROTEIN');
insert into "ingredient" (id, name, type)
values ('SSGE', 'Sausage', 'PROTEIN');
insert into "ingredient" (id, name, type)
values ('TMTO', 'Diced Tomatoes', 'VEGGIES');
insert into "ingredient" (id, name, type)
values ('ONI', 'Onion', 'VEGGIES');
insert into "ingredient" (id, name, type)
values ('CHED', 'Cheddar', 'CHEESE');
insert into "ingredient" (id, name, type)
values ('JACK', 'Monterrey Jack', 'CHEESE');
insert into "ingredient" (id, name, type)
values ('KTCH', 'Ketchup', 'SAUCE');
insert into "ingredient" (id, name, type)
values ('MAYO', 'Mayonnaise', 'SAUCE');