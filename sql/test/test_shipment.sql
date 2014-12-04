/* 선적 테스트: 복구용 */
select * from orders order by order_id desc;

select * from order_items where order_id = 2463;

select * from inventories where product_id in (1788, 3091, 2293) and warehouse_id = 2;

select * from inventories where product_id in 
(select product_id from order_items where order_id = 2463) and
warehouse_id = 2;

update inventories set quantity_on_hand = 107 where 
product_id = 1788 and warehouse_id = 2;
update inventories set quantity_on_hand = 174 where 
product_id = 3091 and warehouse_id = 2;
commit;