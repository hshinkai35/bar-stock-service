CREATE DATABASE `bar`;

CREATE TABLE `bar`.`users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT '',
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `bar`.`products` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT '',
  `number_of_stock` INT DEFAULT 0,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `bar`.`login_device` (
  `user_id` BIGINT NOT NULL,
  `device` varchar(10),
  `user_agent`    TEXT,
  UNIQUE (`user_id`, `device`  )
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
