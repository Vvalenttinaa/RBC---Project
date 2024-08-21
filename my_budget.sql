-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema my_budget
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema my_budget
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `my_budget` DEFAULT CHARACTER SET utf8 ;
USE `my_budget` ;

-- -----------------------------------------------------
-- Table `my_budget`.`account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `my_budget`.`account` (
  `id` VARCHAR(36) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `currency` VARCHAR(20) NOT NULL,
  `balance` DECIMAL NOT NULL,
  `deleted` TINYINT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `my_budget`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `my_budget`.`user` (
  `id` VARCHAR(36) NOT NULL,
  `updated_at` DATETIME NOT NULL,
  `current_currency` VARCHAR(20) NOT NULL,
  `updated` TINYINT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `my_budget`.`transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `my_budget`.`transaction` (
  `id` VARCHAR(36) NOT NULL,
  `amount` DECIMAL NOT NULL,
  `description` VARCHAR(100) NOT NULL,
  `account_id` VARCHAR(36) NOT NULL,
  `deleted` TINYINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_transaction_account_idx` (`account_id` ASC) VISIBLE,
  CONSTRAINT `fk_transaction_account`
    FOREIGN KEY (`account_id`)
    REFERENCES `my_budget`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
