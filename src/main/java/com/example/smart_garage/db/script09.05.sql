create table `smart-garage`.brands
(
    brand_id   int auto_increment
        primary key,
    brand_name varchar(32)          not null,
    isArchived tinyint(1) default 0 not null,
    constraint brands_pk
        unique (brand_name)
);

create table `smart-garage`.car_maintenances
(
    car_maintenance_id   int auto_increment
        primary key,
    car_maintenance_name varchar(100)         not null,
    isArchived           tinyint(1) default 0 not null
);

create table `smart-garage`.car_models
(
    car_model_id   int auto_increment
        primary key,
    car_model_name varchar(32)          not null,
    brand_id       int                  not null,
    isArchived     tinyint(1) default 0 not null,
    constraint models_brands_brand_id_fk
        foreign key (brand_id) references `smart-garage`.brands (brand_id)
);

create table `smart-garage`.prices
(
    price_id           int auto_increment
        primary key,
    car_maintenance_id int    not null,
    amount             double not null,
    valid_from         date   not null,
    valid_to           date   null,
    constraint prices_car_maintenances_car_maintenance_id_fk
        foreign key (car_maintenance_id) references `smart-garage`.car_maintenances (car_maintenance_id)
);

create table `smart-garage`.roles
(
    role_id   int auto_increment
        primary key,
    role_name varchar(15) null
);

create table `smart-garage`.users
(
    user_id      int auto_increment
        primary key,
    first_name   varchar(32)          not null,
    last_name    varchar(32)          not null,
    username     varchar(20)          not null,
    password     varchar(30)          not null,
    email        varchar(20)          not null,
    phone_number varchar(20)          not null,
    role_id      int                  not null,
    isActivated  tinyint(1) default 0 not null,
    isArchived   tinyint(1) default 0 not null,
    constraint users_pk
        unique (email),
    constraint users_pk2
        unique (phone_number),
    constraint users_roles_role_id_fk
        foreign key (role_id) references `smart-garage`.roles (role_id)
);

create table `smart-garage`.discounts
(
    discount_id     int auto_increment
        primary key,
    user_id         int                  not null,
    discount_amount double               not null,
    valid_from      date                 not null,
    valid_to        date                 null,
    discount_name   varchar(50)          not null,
    isArchived      tinyint(1) default 0 not null,
    constraint discounts_users_user_id_fk
        foreign key (user_id) references `smart-garage`.users (user_id)
);

create table `smart-garage`.password_reset_tokens
(
    reset_token_id int auto_increment
        primary key,
    token          varchar(50) null,
    user_id        int         null,
    expiry_date    datetime    null,
    constraint password_reset_tokens_users_user_id_fk
        foreign key (user_id) references `smart-garage`.users (user_id)
);

create table `smart-garage`.vehicles
(
    vehicle_id       int auto_increment
        primary key,
    plate            varchar(8)           not null,
    vin              varchar(17)          not null,
    year_of_creation int(4)               not null,
    model_id         int                  not null,
    user_id          int                  not null,
    isArchived       tinyint(1) default 0 not null,
    constraint vehicles_pk
        unique (plate),
    constraint vehicles_pk2
        unique (vin),
    constraint vehicles_models_model_id_fk
        foreign key (model_id) references `smart-garage`.car_models (car_model_id),
    constraint vehicles_users_user_id_fk
        foreign key (user_id) references `smart-garage`.users (user_id)
);

create table `smart-garage`.visits
(
    visit_id            int auto_increment
        primary key,
    start_date_of_visit date       default current_timestamp() not null,
    vehicle_id          int                                    not null,
    end_date_of_visit   date                                   null,
    isArchived          tinyint(1) default 0                   not null,
    payment_id          int        default 0                   null,
    notes               varchar(100)                           null,
    constraint visits_vehicles_vehicle_id_fk
        foreign key (vehicle_id) references `smart-garage`.vehicles (vehicle_id)
);

create table `smart-garage`.orders
(
    order_id           int auto_increment
        primary key,
    car_maintenance_id int                              not null,
    date_of_creation   date default current_timestamp() not null,
    visit_id           int                              not null,
    price_id           int                              not null,
    order_name         varchar(20)                      not null,
    constraint orders_car_maintenances_car_maintenance_id_fk
        foreign key (car_maintenance_id) references `smart-garage`.car_maintenances (car_maintenance_id),
    constraint orders_prices_price_id_fk
        foreign key (price_id) references `smart-garage`.prices (price_id),
    constraint orders_visits_visit_id_fk
        foreign key (visit_id) references `smart-garage`.visits (visit_id)
);

create table `smart-garage`.payments
(
    payment_id        int auto_increment
        primary key,
    date_of_payment   datetime   default current_timestamp() null,
    total_price_BGN   double     default 0                   null,
    visit_id          int                                    not null,
    isArchived        tinyint(1) default 0                   not null,
    payment_status    varchar(10)                            not null,
    list_price        double                                 null,
    discount          int                                    null,
    VAT               double                                 null,
    original_currency varchar(3)                             not null,
    exchange_rate     double     default 1                   not null,
    constraint payments_visits_visit_id_fk
        foreign key (visit_id) references `smart-garage`.visits (visit_id)
);


