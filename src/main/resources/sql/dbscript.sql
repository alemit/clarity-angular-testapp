CREATE SEQUENCE netinfo.network_info_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
CREATE SEQUENCE netinfo.user_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE netinfo.network_info (
    id bigint NOT NULL,
    ip character varying(15) NOT NULL,
    hostname character varying(255) NOT NULL,
    create_user character varying(32) NOT NULL,
);


ALTER TABLE ONLY netinfo.network_info
    ADD CONSTRAINT network_info_pkey PRIMARY KEY (id);


CREATE TABLE netinfo."user"(
    id bigint NOT NULL,
    username character varying(32) NOT NULL,
    password character varying(255) NOT NULL
)

ALTER TABLE ONLY netinfo."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);

ALTER TABLE ONLY netinfo."user"
    ADD CONSTRAINT username_uq UNIQUE (username);
