drop database if exists `p2p`;
create database `p2p` default character set utf8mb4 collate utf8mb4_general_ci;
use p2p;

drop table if exists`department`;
create table `department` (
	`department_id` int not null auto_increment comment '部门id',
	`department_name` varchar(32) not null comment '部门名字',
	primary key(`department_id`)
) engine=InnoDB default charset=utf8mb4 comment '部门信息';

insert into `department`(`department_name`) values("财务部");
insert into `department`(`department_name`) values("人事部");
insert into `department`(`department_name`) values("策划部");
insert into `department`(`department_name`) values("营销部");
insert into `department`(`department_name`) values("技术部");
insert into `department`(`department_name`) values("管理部");

drop table if exists `user`;
create table `user` (
	`user_id` varchar(12) not null comment '用户id， 工号',
	`department_id` int not null,
	`password` varchar(64) not null comment '密码',
	`phone` varchar(11) not null comment '手机号',
	`id_card` varchar(20) not null comment '身份证',
	`third_party_id` varchar(12) not null comment '第三方平台账号',
	`name` varchar(64) not null comment '姓名',
	`address` varchar(255) not null comment '地址',
	primary key (`user_id`)
--	foreign key (`department_id`) references `department`(`department_id`)
) engine=InnoDB default charset=utf8mb4 comment '用户信息表';

drop table if exists `authority`;
create table `authority` (
	`authority_id` int not null auto_increment,
	`authority_amount` decimal(12,2) not null comment '最高担保金额',
	primary key (`authority_id`)
)engine=InnoDB default charset=utf8mb4 comment '担保权限表';

insert into `authority`(`authority_amount`) values(10000);
insert into `authority`(`authority_amount`) values(20000);
insert into `authority`(`authority_amount`) values(50000);
insert into `authority`(`authority_amount`) values(100000);

drop table if exists `guarantor`;
create table `guarantor` (
	`guarantor_id` varchar(12) not null comment '工具人id',
	`password` varchar(64) not null comment '密码',
	`name` varchar(64) not null comment '担保人姓名',
	`third_party_id` varchar(12) not null comment '第三方平台账号',
	`authority_id` int not null comment '权限id', 
	primary key (`guarantor_id`)
--	foreign key (`authority_id`) references `authority`(`authority_id`)
) engine=InnoDB default charset=utf8mb4 comment '工具人表';

drop table if exists `credit_info`;
create table `credit_info` (
	`user_id` varchar(12) not null,
	`income` decimal(10,2) not null comment '月收入',
	`famiy_income` decimal(10,2) not null comment '家庭收入',
	`assets` decimal(12,2) not null comment '个人资产',
	`family_number` int(2) not null comment '家庭成员数',
	`debt` decimal(12,2) not null comment '债务',
	`credit_score` int(3) not null comment '信用积分',
	primary key(`user_id`)
-- 	foreign key(`user_id`) references `user`(`user_id`)
) engine=InnoDB default charset=utf8mb4 comment '用户征信表';

drop table if exists `grant_credit`;
create table `grant_credit` (
	`user_id` varchar(12) not null,
	`income` decimal(10,2) not null comment '月收入',
	`quota` decimal(12,2) not null comment '额度',
	`rate` decimal(3,2) not null comment '额度系数',
	`expire` timestamp not null comment '过期时间',
	primary key(`user_id`)
--	foreign key(`user_id`) references `user`(`user_id`)
) engine=InnoDB default charset=utf8mb4 comment '用户授信表';

drop table if exists `product`;
create table `product` (
	`product_id` int(12) not null auto_increment,
	`borrower_id` varchar(12) not null comment '贷款人id',
	`guarantor_id` varchar(12) not null comment '担保人id',
	`status` int not null comment '产品状态, 0为未审核，1为审核通过，2为审核失败，3为已被认购，4为过期',
	`amount` decimal(12, 2) not null comment '贷款金额',
	`interest_rate` decimal(5, 4) not null comment '利率',
	`loan_month` int not null comment '借款月数',
	`purchase_deadline` date not null comment '认购期限',
	`create_time` timestamp not null default current_timestamp comment '创建时间',
	`update_time` timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
	primary key (`product_id`)
--	foreign key (`borrower_id`) references `user`(`user_id`),
--	foreign key (`guarantor_id`) references `guarantor`(`guarantor_id`)
) engine=InnoDB default charset=utf8mb4 comment '产品信息表';

drop table if exists `purchase`;
create table `purchase` (
	`purchase_id` int(12) not null auto_increment,
	`borrower_id` varchar(12) not null comment '贷款人id',
	`guarantor_id` varchar(12) not null comment '担保人id',
	`investor_id` varchar(12) not null comment '投资人id',
	`purchase_time` date not null comment '认购时间',
	`interest_rate` decimal(5, 4) not null comment '利率',
	`loan_month` int not null comment '借款月数',
	`amount` decimal(12, 2) not null comment '贷款金额',
	`status` int(1) not null comment '状态， 0表示合约中，1表示逾期',
	`create_time` timestamp not null default current_timestamp comment '创建时间',
	`update_time` timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
	primary key (`purchase_id`)
--	foreign key (`guarantor_id`) references `guarantor`(`guarantor_id`),
-- 	foreign key (`investor_id`) references `user`(`user_id`),
--	foreign key (`borrower_id`) references `user`(`user_id`)
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

drop table if exists `repay_plan`;
create table `repay_plan` (
	`plan_id` binary(32) not null comment '计划ID',
	`purchase_id` int(12) not null comment '认购ID',
	`repay_date` date not null comment '计划还款日期',
	`real_repay_date` date comment '实际还款日期',
	`amount` decimal(12, 2) not null comment '还款金额',
	`status` int not null comment '还款状态，0为未还，1为已还, 2为逾期未还',
	primary key(`plan_id`),
	key `index_repay_date`(`repay_date`),
    key `index_status`(`status`)
--	foreign key(`purchase_id`) references `purchase`(`purchase_id`)
) engine=InnoDB default charset=utf8mb4 comment '还款计划';

drop table if exists `notify`;
create table `notify` (
	`user_id` varchar(12) not null,
	`content` text not null,
	`status` int not null comment '读取状态，1为已读，0为未读',
	key `index_user_id`(`user_id`)
--	foreign key (`user_id`) references `user`(`user_id`)
) engine=InnoDB default charset=utf8mb4 comment '通知表';



