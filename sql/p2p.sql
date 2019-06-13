drop database if exists `p2p`;
create database `p2p` default character set utf8mb4 collate utf8mb4_general_ci;
use p2p;

drop table if exists `user`;
create table `user` (
	`user_id` varchar(12) not null comment '用户id， 工号',
	`password` varchar(64) not null comment '密码',
	`phone` varchar(11) not null comment '手机号',
	`id_card` varchar(20) not null comment '身份证',
	`third_party_id` varchar(12) not null comment '第三方平台账号',
	`name` varchar(64) not null comment '姓名',
	`address` varchar(255) not null comment '地址',
	primary key(`user_id`)
) engine=InnoDB default charset=utf8mb4 comment '用户信息表';

drop table if exists `credit_info`;
create table `credit_id` (
	`user_id` varchar(12) not null,
	`income` decimal(10,2) not null comment '收入',
	`famiy_income` decimal(10,2) not null comment '家庭收入',
	`assets` decimal(12,2) not null comment '个人资产',
	`family_number` int(2) not null comment '家庭成员数',
	`debt` decimal(12,2) not null comment '债务',
	`credit_score` int(3) not null comment '信用积分',
	primary key(`user_id`)
-- 	foreign key(`user_id`) references `user`(`user_id`)
) engine=InnoDB default charset=utf8mb4 comment '用户征信表';

drop table if exists `product`;
create table `product` (
	`product_id` int(12) not null auto_increment,
	`borrower_id` varchar(12) not null comment '贷款人id',
	`state` int(1) not null comment '产品状态',
	`amount`decimal(12, 2) not null comment '贷款金额',
	`interest_rate` decimal(5, 4) not null comment '利率',
	`repay_deadline` date not null comment '还款期限',
	`purchase_deadline` date not null comment '认购期限',
	`repay_data` int(2) not null comment '每月还款时间',
	`create_time` timestamp not null default current_timestamp comment '创建时间',
	`update_time` timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
	primary key (`product_id`)
-- 	foreign key (`borrower_id`) references `user`(`user_id`)
) engine=InnoDB default charset=utf8mb4 comment '产品信息表';

drop table if exists `purchase`;
create table `purchase` (
	`product_id` int(12) not null,
	`investor_id` varchar(12) not null comment '投资人id',
	`purchase_time` date not null comment '认购时间',
	`amount` decimal(12, 2) not null comment '金额',
	`state` int(1) not null comment '状态， 0表示合约中，1表示逾期',
	`repay_time` date not null comment '还款日期',
	primary key (`product_id`)
-- 	foreign key (`product_id`) references `product`(`product_id`),
-- 	foreign key (`investor_id`) references `user`(`user_id`)
) engine=InnoDB default charset=utf8mb4 comment '认购信息表';

drop table if exists `water_bill`;
create table `water_bill` (
	`water_bill_id` varchar(32) not null,
	`payee_id` varchar(12) not null comment '收款人id',
	`payer_id` varchar(12) not null comment '支付人id',
	`amount` decimal(12, 2) not null comment '金额',
	`mode` int(1) not null comment '模式，0为贷款， 1为还款',
	`time` timestamp not null default current_timestamp comment '交易时间',
	primary key(`water_bill_id`)
-- 	foreign key(`payee_id`) references `user`(`user_id`),
-- 	foreign key(`payer_id`) references `user`(`user_id`)
) engine=InnoDB default charset=utf8mb4 comment '流水账';