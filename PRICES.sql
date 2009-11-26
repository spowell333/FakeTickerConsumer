drop sequence PRICE_ID;
create sequence PRICE_ID
  MINVALUE 1
  INCREMENT BY 1
  CACHE 20;


drop table PRICES;
create table PRICES (
  PRICE_ID   number(11,0)  NOT NULL,
  TICKER     varchar2(255) not null,
  PRICE_TYPE varchar2(3)   not null,
  PRICE      NUMBER(11,4)  not null, 
  VALID_FROM TIMESTAMP     NOT NULL, 
  VALID_TO   TIMESTAMP         NULL)
--PARTITION BY LIST (valid_to)
--(
--  PARTITION LATEST  VALUES (NULL),
--  PARTITION HISTORY VALUES (DEFAULT)
--)
;
  
create index PRICE_SI1 on PRICES(TICKER, PRICE_TYPE);
create index PRICE_SI2 on PRICES(VALID_FROM);

drop table LATEST_PRICES;
create table LATEST_PRICES (
TICKER varchar2(255) not null,
PRICE_TYPE varchar2(3) not null,
price number(11,4) not null);
create unique index LATEST_PRICE_SI1 on LATEST_PRICES(TICKER, PRICE_TYPE);



CREATE OR REPLACE TRIGGER price_defaults 
BEFORE INSERT ON PRICES
FOR EACH ROW
DECLARE
  now timestamp := systimestamp;
BEGIN
  
  update PRICES set VALID_TO = now
    where ticker     = :new.ticker
      and price_type = :new.price_type
      and valid_to  is NULL;
  
  SELECT PRICE_ID.NEXTVAL, now
  INTO   :new.PRICE_ID, :new.VALID_FROM
  FROM   dual;
  
  BEGIN
    update  LATEST_PRICES set PRICE = :new.PRICE
      where TICKER = :new.ticker
        and PRICE_TYPE = :new.price_type;
    IF SQL%NOTFOUND THEN    
    INSERT INTO LATEST_PRICES(TICKER, PRICE_TYPE, PRICE)
        values(:new.TICKER, :new.PRICE_TYPE, :new.PRICE);
    END IF;
  
  END;
      
  
END;
/


insert INTO PRICES(TICKER, price_type, price) values ('GS','ask','173.89');
insert INTO PRICES(TICKER, price_type, price) values ('GS','ask','173.90');

select * from latest_prices;
COMMIT;



select * from prices 
  where price_id > (select max(price_id) from prices) -30 
    and VALID_TO is NULL
  order by price_id desc;