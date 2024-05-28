# add 好友附加消息 -- 已执行
alter table im_friend
    add column attach_msg varchar(100) comment '附加消息';


# add 好友备注字段,并且设置为索引 -- 已执行
alter table im_friend
    add column notes varchar(50) comment '备注';
alter table im_friend
    add index idx_notes (notes);


