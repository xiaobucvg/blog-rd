create table sm_admin
(
    id       bigint auto_increment
        primary key,
    nickname char(30)     not null comment '昵称',
    username char(30)     not null comment '管理员账号',
    password varchar(255) not null comment '管理员密码',
    avatar   varchar(255) null comment '管理员头像地址',
    constraint sm_admin_username_uindex
        unique (username)
)
    comment '管理员表';

create table sm_article
(
    id            bigint auto_increment
        primary key,
    status        int      default 1002              not null comment '1001 发布中
1002 未发布
1003 已删除
',
    title         char(50)                           not null comment '不能超过50个字符',
    abstract_info char(255)                          not null,
    content       longtext                           not null,
    reading       bigint   default 0                 not null comment '阅读量
',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null comment '修改时间',
    constraint sm_article_title_uindex
        unique (title)
)
    comment '文章';

create fulltext index article_full_index
    on sm_article (title, abstract_info, content);

create table sm_log
(
    id          bigint auto_increment
        primary key,
    ip          char(15)                           not null comment 'IP 地址',
    msg         char(100)                          not null comment '操作记录',
    create_time datetime default CURRENT_TIMESTAMP not null comment '操作时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间
'
)
    comment '操作记录';

create table sm_tag
(
    id          bigint auto_increment
        primary key,
    name        char(10)                           null comment '标签名字',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null comment '修改时间'
)
    comment '文章标签';

create table sm_article__tag
(
    id         bigint auto_increment
        primary key,
    article_id bigint not null comment '文章ID
',
    tag_id     bigint null comment '标签ID
',
    constraint article_id_fk
        foreign key (article_id) references sm_article (id),
    constraint tag_id_fk
        foreign key (tag_id) references sm_tag (id)
)
    comment '文章与标签的中间表';

