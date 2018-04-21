CREATE TABLE dev.network_info (
    id bigint NOT NULL,
    ip character varying(15) NOT NULL,
    hostname character varying(255) NOT NULL
);

CREATE SEQUENCE dev.network_info_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE ONLY dev.network_info
    ADD CONSTRAINT network_info_pkey PRIMARY KEY (id);
