-- Drop table

DROP TABLE if exists report_activity;

CREATE TABLE report_activity (
  activity_id varchar(255) NOT NULL,
  status varchar(255) NOT NULL,
  resumed CHAR,

  report_data text NULL, /* json */
  error TEXT NULL,
  template_name VARCHAR(255) NOT NULL,
  report_name VARCHAR(255) NOT NULL,
  report_locale VARCHAR(255) ,
  object_info VARCHAR(255),
  mime_type_id VARCHAR(60),
  completed_stamp datetime NULL,
  LAST_UPDATED_STAMP datetime DEFAULT NULL,
  LAST_UPDATED_TX_STAMP datetime DEFAULT NULL,
  CREATED_STAMP datetime DEFAULT NULL,
  CREATED_TX_STAMP datetime DEFAULT NULL,
  CREATED_BY_USER_LOGIN varchar(250) DEFAULT NULL,
  LAST_MODIFIED_BY_USER_LOGIN varchar(250) DEFAULT NULL,
  CONTENT_NAME varchar(100) DEFAULT NULL,
  PRIMARY KEY (activity_id),
  KEY CONTENT_TXSTMP (`LAST_UPDATED_TX_STAMP`),
  KEY CONTENT_TXCRTS (`CREATED_TX_STAMP`)
  
)
;
